package dev.adamko.kafkatorio.processor.topology

import com.sksamuel.scrimage.ImmutableImage
import com.sksamuel.scrimage.ScaleMethod
import com.sksamuel.scrimage.color.RGBColor
import com.sksamuel.scrimage.nio.PngWriter
import dev.adamko.kafkatorio.processor.admin.TOPIC_GROUPED_MAP_CHUNKS_STATE
import dev.adamko.kafkatorio.processor.serdes.kxsBinary
import dev.adamko.kafkatorio.schema.common.ColourHex
import dev.adamko.kafkatorio.schema.common.MapChunkPosition
import dev.adamko.kafkatorio.schema.common.MapTilePosition
import dev.adamko.kafkatorio.schema.common.SurfaceIndex
import dev.adamko.kafkatorio.schema.common.toMapChunkPosition
import dev.adamko.kotka.extensions.consumedAs
import dev.adamko.kotka.extensions.streams.forEach
import dev.adamko.kotka.kxs.serde
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import kotlin.coroutines.CoroutineContext
import kotlin.io.path.fileSize
import kotlin.math.abs
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.serialization.Serializable
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.Topology
import org.apache.kafka.streams.kstream.KStream


private const val WEB_MAP_TILE_SIZE_PX = 256


/** A bridge between Kafka and Kotlin */
private val serverMapChunkHandler = ServerMapChunkHandler()


fun saveMapTiles(
  builder: StreamsBuilder,
): Topology {
  val pid = "saveMapTiles"

  val groupedMapChunkTiles: KStream<ServerMapChunkId, ServerMapChunkTiles<ColourHex>> =
    builder.stream(
      TOPIC_GROUPED_MAP_CHUNKS_STATE,
      consumedAs("$pid.consume.grouped-map-chunks", kxsBinary.serde(), kxsBinary.serde())
    )

  groupedMapChunkTiles
    .forEach("save-chunked-tiles") { _, value ->
      if (value != null) {
        println("emitting chunkedTiles ${value.chunkId} tiles: ${value.map.size}")
        serverMapChunkHandler.emit(value)
      }
    }

  return builder.build()
}


@JvmInline
value class TilePngFilename(
  val value: String,
) {
  constructor(id: ServerMapChunkId) : this(buildString {
    append("src/main/resources/kafkatorio-web-map")
    append("/s${id.surfaceIndex}")
    append("/z${id.chunkSize.zoomLevel}")
    append("/x${id.chunkPosition.x}")
    append("/y${id.chunkPosition.y}")
    append(".png")
  })
}


val TRANSPARENT_AWT: Color = ColourHex.TRANSPARENT.toRgbColor().awt()


fun ColourHex.toRgbColor(): RGBColor {
  return RGBColor(
    red.toInt(),
    green.toInt(),
    blue.toInt(),
    alpha.toInt(),
  )
}


private class ServerMapChunkHandler : CoroutineScope {

  override val coroutineContext: CoroutineContext =
    Dispatchers.Default + SupervisorJob(rootJob) + CoroutineName("ServerMapChunkHandler")


  /** The inbox for this handler. */
  private val allChunksFlow = MutableSharedFlow<ServerMapChunkTiles<ColourHex>>(
    replay = 0,
    extraBufferCapacity = 10,
    onBufferOverflow = BufferOverflow.SUSPEND,
  )

  fun emit(mapChunkTiles: ServerMapChunkTiles<ColourHex>) = launch {
    allChunksFlow.emit(mapChunkTiles)
  }

  /** This flow saves images one-by-one */
  private val saveImagesFlow = MutableSharedFlow<Cmd.SaveImage>(
    replay = 0,
    extraBufferCapacity = 10,
    onBufferOverflow = BufferOverflow.SUSPEND,
  )

  init {

    allChunksFlow
      .runningFold(mutableMapOf<ServerMapChunkId, MutableSharedFlow<Cmd.ChunkSubdivide>>()) { acc, src ->

        val flow = acc.getOrPut(src.chunkId) {
          println("creating new subdivide-flow for chunkId ${src.chunkId}")
          createSubdivisionFlow()
        }

        flow.emit(Cmd.ChunkSubdivide(src.chunkId, src.map))

        acc
      }
      .distinctUntilChangedBy { it.size }
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

        val sizeBefore = chunkImageFile.takeIf { it.exists() }?.toPath()?.fileSize()

        val savedTile = img.output(PngWriter.NoCompression, chunkImageFile)

        val sizeAfter = savedTile.takeIf { it.exists() }?.toPath()?.fileSize()
        if (sizeBefore != sizeAfter) {
//          println("savedTile $sizeBefore/$sizeAfter: $savedTile")
        } else {
          println("savedTile NO-SIZE-CHANGE $sizeBefore/$sizeAfter: $savedTile")
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

    val saveImgCommands: Flow<Cmd.SaveImage> =
      subdivisionFlow
        .debounce(30.seconds)
        .flatMapConcat { subdivideCmd ->
          ChunkSize
            .entries
            .flatMap { chunkSize ->
              subdivideCmd
                .tiles
                .entries
                .groupBy(
                  { (tile, _) -> tile.toMapChunkPosition(chunkSize.tilesPerChunk) }
                ) { (tilePosition, colour) ->
                  tilePosition to colour
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
          val img = createMapTileImage(
            createCmd.chunkPosition,
            createCmd.tiles,
            createCmd.chunkSize,
          )

          Cmd.SaveImage(createCmd.tilePngFilename, img)
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

  @Serializable
  data class ChunkSubdivide(
    val chunkId: ServerMapChunkId,
    val tiles: Map<MapTilePosition, ColourHex>,
  ) : Cmd

  @Serializable
  data class CreateImage(
    val surfaceIndex: SurfaceIndex,
    val chunkSize: ChunkSize,
    val chunkPosition: MapChunkPosition,
    val tiles: Map<MapTilePosition, ColourHex>,
  ) : Cmd {
    val tilePngFilename: TilePngFilename by lazy {
      TilePngFilename(buildString {
        append("src/main/resources/kafkatorio-web-map")
        append("/s${surfaceIndex}")
        append("/z${chunkSize.zoomLevel}")
        append("/x${chunkPosition.x}")
        append("/y${chunkPosition.y}")
        append(".png")
      })
    }
  }


  data class SaveImage(
    val filename: TilePngFilename,
    val image: ImmutableImage,
  ) : Cmd

}
