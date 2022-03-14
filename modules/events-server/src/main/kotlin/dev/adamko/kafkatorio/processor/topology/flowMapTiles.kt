package dev.adamko.kafkatorio.processor.topology

import com.sksamuel.scrimage.ImmutableImage
import com.sksamuel.scrimage.ScaleMethod
import com.sksamuel.scrimage.color.RGBColor
import com.sksamuel.scrimage.nio.PngWriter
import dev.adamko.kafkatorio.events.schema.ColourHex
import dev.adamko.kafkatorio.events.schema.MapChunkPosition
import dev.adamko.kafkatorio.events.schema.MapTilePosition
import dev.adamko.kafkatorio.events.schema.SurfaceIndex
import dev.adamko.kafkatorio.events.schema.converters.toMapChunkPosition
import dev.adamko.kafkatorio.processor.KafkatorioTopology
import dev.adamko.kafkatorio.processor.serdes.kxsBinary
import dev.adamko.kotka.extensions.consumedAs
import dev.adamko.kotka.extensions.streams.forEach
import dev.adamko.kotka.kxs.serde
import java.awt.image.BufferedImage
import java.io.File
import kotlin.coroutines.CoroutineContext
import kotlin.math.abs
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
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
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.Topology
import org.apache.kafka.streams.kstream.KStream


private const val WEB_MAP_TILE_SIZE_PX = 256

private val cScope = CoroutineScope(
  Dispatchers.Default + SupervisorJob() + CoroutineName("f-server-map-updates")
)


/** A bridge between Kafka and Kotlin */
private val serverMapChunkHandler = ServerMapChunkHandler()


fun saveMapTiles(
  builder: StreamsBuilder,
): Topology {

  val groupedMapChunkTiles: KStream<ServerMapChunkId, ServerMapChunkTiles<ColourHex>> =
    builder.stream(
      KafkatorioTopology.TOPIC_GROUPED_MAP_CHUNKS,
      consumedAs("consume.grouped-map-chunks", kxsBinary.serde(), kxsBinary.serde())
    )

  groupedMapChunkTiles
    .forEach("save-chunked-tiles") { _, value ->
      if (value != null) {
        runBlocking(Dispatchers.Default) {
          println("emitting chunkedTiles ${value.chunkId} tiles: ${value.map.size}")
          serverMapChunkHandler.emit(value)
        }
      }
    }

  return builder.build()
}


@JvmInline
private value class TilePngFilename(
  val value: String,
) {
  constructor(cmd: Cmd.CreateImage) : this(buildString {
    append("src/main/resources/kafkatorio-web-map")
    append("/s${cmd.surfaceIndex}")
    append("/z${cmd.chunkSize.zoomLevel}")
    append("/x${cmd.chunkPosition.x}")
    append("/y${cmd.chunkPosition.y}")
    append(".png")
  })
}


private val TRANSPARENT_AWT = ColourHex.TRANSPARENT.toRgbColor().awt()


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


  /** The inbox for this handler. */
  private val allChunksFlow = MutableSharedFlow<ServerMapChunkTiles<ColourHex>>(
    replay = 0,
    extraBufferCapacity = 10,
    onBufferOverflow = BufferOverflow.SUSPEND,
  )

  fun emit(mapChunkTiles: ServerMapChunkTiles<ColourHex>) {
    launch {
      allChunksFlow.emit(mapChunkTiles)
    }
  }

  /** This flow saves images one-by-one */
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
          createSubdivisionFlow()
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
        if (filename.value.contains("z-1")) {
          println("savedTile: $savedTile")
        }
      }
      .launchIn(this)

  }


  /**
   * Given a chunk, split it into multiple, smaller sub-tiles.
   *
   * Send each sub-tile to [saveImagesFlow] to be saved as a PNG
   */
  private fun createSubdivisionFlow(): MutableSharedFlow<Cmd.ChunkSubdivide> {

    val subdivisionFlow = MutableSharedFlow<Cmd.ChunkSubdivide>(
      replay = 0,
      extraBufferCapacity = 10,
      onBufferOverflow = BufferOverflow.SUSPEND,
    )

    val saveImgCommands = subdivisionFlow
      .flatMapConcat { subdivideCmd ->
        ChunkSize
          .values
          .flatMap { chunkSize ->
            subdivideCmd.tiles
              .entries
              .groupBy(
                { (tile, _) -> tile.toMapChunkPosition(chunkSize.tilesPerChunk) }
              ) {
                it.key to it.value
              }
              .map { (chunkPos, tiles) ->
                async {
                  Cmd.CreateImage(
                    surfaceIndex = subdivideCmd.chunkId.surfaceIndex,
                    chunkSize = chunkSize,
                    chunkPosition = chunkPos,
                    tiles = tiles.toMap()
                  )
                }
              }
          }
          .awaitAll()
          .asFlow()
      }
      .map { createCmd ->
        val filename = TilePngFilename(createCmd)

        val img = createMapTileImage(
          createCmd.chunkPosition,
          createCmd.tiles,
          createCmd.chunkSize,
        )

        Cmd.SaveImage(filename, img)
      }

    launch {
      supervisorScope {
        println("emitting all saveImgCommands to saveImagesFlow")
        saveImagesFlow.emitAll(saveImgCommands)
      }
    }

    return subdivisionFlow
  }


  /** Create (but don't save) a PNG for the given chunk. */
  private fun createMapTileImage(
    chunkPosition: MapChunkPosition,
    chunkColours: Map<MapTilePosition, ColourHex>,
    chunkSize: ChunkSize
  ): ImmutableImage {

    val chunkImage = ImmutableImage.filled(
      chunkSize.tilesPerChunk,
      chunkSize.tilesPerChunk,
      TRANSPARENT_AWT,
      BufferedImage.TYPE_INT_ARGB
    )

    val chunkOriginX: Int = chunkPosition.x * chunkSize.tilesPerChunk
    val chunkOriginY: Int = chunkPosition.y * chunkSize.tilesPerChunk

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
}


private sealed interface Cmd {

  data class ChunkSubdivide(
    val chunkId: ServerMapChunkId,
    val tiles: Map<MapTilePosition, ColourHex>,
  ) : Cmd

  data class CreateImage(
    val surfaceIndex: SurfaceIndex,
    val chunkSize: ChunkSize,
    val chunkPosition: MapChunkPosition,
    val tiles: Map<MapTilePosition, ColourHex>,
  ) : Cmd

  data class SaveImage(
    val filename: TilePngFilename,
    val image: ImmutableImage,
  ) : Cmd

}
