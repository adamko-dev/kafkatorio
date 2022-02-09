package dev.adamko.kafkatorio.processor.topology

import com.sksamuel.scrimage.ImmutableImage
import com.sksamuel.scrimage.ScaleMethod
import com.sksamuel.scrimage.color.RGBColor
import com.sksamuel.scrimage.nio.PngWriter
import dev.adamko.kafkatorio.events.schema.ColourHex
import dev.adamko.kafkatorio.events.schema.MapChunkPosition
import dev.adamko.kafkatorio.events.schema.MapTilePosition
import dev.adamko.kafkatorio.events.schema.converters.toMapChunkPosition
import java.awt.image.BufferedImage
import java.io.File
import kotlin.coroutines.CoroutineContext
import kotlin.math.abs
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.supervisorScope
import org.apache.kafka.streams.kstream.KTable


private const val WEB_MAP_TILE_SIZE_PX = 256

private val cScope = CoroutineScope(
  Dispatchers.Default + SupervisorJob() + CoroutineName("f-server-map-updates")
)


/** A bridge between Kafka and Kotlin */
private val serverMapChunkHandler = ServerMapChunkHandler()


fun saveMapTiles(
  serverMapTable: KTable<ServerMapChunkId, ServerMapChunkTiles<ColourHex>>
) {

  serverMapTable
    .toStream()
    .foreach { _, value ->
      if (value != null) {
        runBlocking(Dispatchers.Default) {
          println("emitting chunkedTiles ${value.chunkId} tiles: ${value.map.size}")
          serverMapChunkHandler.emit(value)
        }
      }
    }

}


@JvmInline
private value class TilePngFilename(
  val value: String,
) {
  constructor(cmd: Cmd.CreateImage) : this(buildString {
    append("src/main/resources/kafkatorio-web-map")
    append("/s${cmd.surfaceIndex.index}")
    append("/z${cmd.zoomLevel.level}")
    append("/x${cmd.chunkPosition.x}")
    append("/y${cmd.chunkPosition.y}")
    append(".png")
  })
}


private val TRANSPARENT_AWT = ColourHex.TRANSPARENT.toRgbColor().awt()


private fun createMapTileImage(
  chunkPosition: MapChunkPosition,
  chunkColours: Map<MapTilePosition, ColourHex>,
  z: ZoomLevel
): ImmutableImage {

  val chunkImage = ImmutableImage.filled(
    z.chunkSize,
    z.chunkSize,
    TRANSPARENT_AWT,
    BufferedImage.TYPE_INT_ARGB
  )

  val chunkOriginX: Int = chunkPosition.x * z.chunkSize
  val chunkOriginY: Int = chunkPosition.y * z.chunkSize

  chunkColours.forEach { (tilePosition, colour) ->

    val rgbColour = colour.toRgbColor()

    val pixelX = abs(abs(tilePosition.x) - abs(chunkOriginX))
    val pixelY = abs(abs(tilePosition.y) - abs(chunkOriginY))

    chunkImage.setColor(
      pixelX,
      pixelY,
      rgbColour
    )
  }

  return chunkImage.scaleTo(WEB_MAP_TILE_SIZE_PX, WEB_MAP_TILE_SIZE_PX, ScaleMethod.FastScale)
}


private fun ColourHex.toRgbColor(): RGBColor {
  return RGBColor(
    red.toInt(),
    green.toInt(),
    blue.toInt(),
    alpha.toInt(),
  )
}

private class ServerMapChunkHandler : CoroutineScope {

  override val coroutineContext: CoroutineContext = Dispatchers.Default +
      SupervisorJob(cScope.coroutineContext.job) +
      CoroutineName("ServerMapChunkHandler")


  private val allChunksFlow = MutableSharedFlow<ServerMapChunkTiles<ColourHex>>(
    replay = 0,
    extraBufferCapacity = 10,
    onBufferOverflow = BufferOverflow.SUSPEND,
  )

  private val saveImagesFlow = MutableSharedFlow<Cmd.SaveImage>(
    replay = 0,
    extraBufferCapacity = 10,
    onBufferOverflow = BufferOverflow.SUSPEND,
  )


  init {

    allChunksFlow
      .runningFold(mapOf<ServerMapChunkId, MutableSharedFlow<Cmd.ChunkSubdivide>>()) { acc, src ->

        val flow = acc.getOrElse(src.chunkId) {
          println("creating new subdivide-flow for chunkId ${src.chunkId}")
          subdivisionFlow()
        }

        flow.emit(Cmd.ChunkSubdivide(src.chunkId, src.map))

        acc + (src.chunkId to flow)
      }
      .onEach {
        println("allChunksFlow has ${it.size} subdivide-flows: ${it.keys.joinToString()}")
      }
      .launchIn(this)

    saveImagesFlow
      .onEach { (filename, img) ->

        val chunkImageFile = File(filename.value)
        if (chunkImageFile.parentFile.mkdirs()) {
          println("created new map tile parentFile directory ${chunkImageFile.absolutePath}")
        }

        val savedTile = img.output(PngWriter.NoCompression, chunkImageFile)
        println("savedTile: $savedTile")
      }
      .launchIn(this)

  }

  fun emit(mapChunkTiles: ServerMapChunkTiles<ColourHex>) {
    launch {
      allChunksFlow.emit(mapChunkTiles)
    }
  }

  private fun subdivisionFlow(): MutableSharedFlow<Cmd.ChunkSubdivide> {

    val flow = MutableSharedFlow<Cmd.ChunkSubdivide>(
      replay = 0,
      extraBufferCapacity = 10,
      onBufferOverflow = BufferOverflow.SUSPEND,
    )

    val saveImgCommands = flow
//      .conflate()
      .flatMapConcat { subdivideCmd ->
        ZoomLevel.values
          .asFlow()
          .flatMapConcat { zoom ->
            subdivideCmd.tiles
              .entries
              .groupBy({ (tile, _) -> tile.toMapChunkPosition(zoom.chunkSize) }
              ) {
                it.key to it.value
              }
              .map { (chunkPos, tiles) ->
                Cmd.CreateImage(
                  surfaceIndex = subdivideCmd.chunkId.surfaceIndex,
                  zoomLevel = zoom,
                  chunkPosition = chunkPos,
                  tiles = tiles.toMap()
                )
              }
              .asFlow()
          }
      }
      .map { createCmd ->
        val filename = TilePngFilename(createCmd)

        val img = createMapTileImage(
          createCmd.chunkPosition,
          createCmd.tiles,
          createCmd.zoomLevel,
        )

        Cmd.SaveImage(filename, img)
      }

    launch {
      supervisorScope {
        println("emitting all saveImgCommands to saveImagesFlow")
        saveImagesFlow.emitAll(saveImgCommands)
      }
    }

    return flow
  }


}

private sealed interface Cmd {

  data class ChunkSubdivide(
    val chunkId: ServerMapChunkId,
    val tiles: Map<MapTilePosition, ColourHex>,
  ) : Cmd

  data class CreateImage(
    val surfaceIndex: SurfaceIndex,
    val zoomLevel: ZoomLevel,
    val chunkPosition: MapChunkPosition,
    val tiles: Map<MapTilePosition, ColourHex>,
  ) : Cmd

  data class SaveImage(
    val filename: TilePngFilename,
    val image: ImmutableImage,
  ) : Cmd

}
