package dev.adamko.kafkatorio.processor.topology

import com.sksamuel.scrimage.ImmutableImage
import com.sksamuel.scrimage.ScaleMethod
import com.sksamuel.scrimage.color.RGBColor
import com.sksamuel.scrimage.nio.PngWriter
import dev.adamko.kafkatorio.processor.admin.TOPIC_GROUPED_MAP_CHUNKS_STATE_DEBOUNCED
import dev.adamko.kafkatorio.processor.admin.TOPIC_SUBDIVIDED_MAP_TILES
import dev.adamko.kafkatorio.processor.admin.TOPIC_SUBDIVIDED_MAP_TILES_DEBOUNCED
import dev.adamko.kafkatorio.processor.misc.DebounceProcessor.Companion.addDebounceProcessor
import dev.adamko.kafkatorio.processor.serdes.kxsBinary
import dev.adamko.kafkatorio.schema.common.ColourHex
import dev.adamko.kafkatorio.schema.common.MapTilePosition
import dev.adamko.kafkatorio.schema.common.toMapChunkPosition
import dev.adamko.kotka.extensions.consumedAs
import dev.adamko.kotka.extensions.producedAs
import dev.adamko.kotka.extensions.streams.flatMap
import dev.adamko.kotka.extensions.streams.forEach
import dev.adamko.kotka.kxs.serde
import java.awt.Color
import java.awt.image.BufferedImage
import java.nio.file.Path
import kotlin.io.path.fileSize
import kotlin.math.abs
import kotlin.time.Duration.Companion.seconds
import kotlin.time.measureTimedValue
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.Topology
import org.apache.kafka.streams.kstream.KStream


private const val pid = "saveMapTiles"

private const val WEB_MAP_TILE_SIZE_PX = 256


/**
 * @param[tileDirectory] [dev.adamko.kafkatorio.processor.config.ApplicationProperties.webmapTileDir]
 */
fun saveMapTiles(
  builder: StreamsBuilder,
  tileDirectory: Path,
): Topology {

  val groupedMapChunkTiles: KStream<ServerMapChunkId, ServerMapChunkTiles<ColourHex>> =
    builder.stream(
      TOPIC_GROUPED_MAP_CHUNKS_STATE_DEBOUNCED,
      consumedAs("$pid.consume.grouped-map-chunks", kxsBinary.serde(), kxsBinary.serde()),
    )

  groupedMapChunkTiles
    .flatMapSubdividedChunk()
    .to(
      TOPIC_SUBDIVIDED_MAP_TILES,
      producedAs(
        "$pid.produce.grouped-map-chunks",
        kxsBinary.serde(),
        kxsBinary.serde(),
      )
    )

  val subdividedMapChunkTilesDebounced: KStream<ServerMapChunkId, ServerMapChunkTiles<ColourHex>> =
    builder.stream(
      TOPIC_SUBDIVIDED_MAP_TILES_DEBOUNCED,
      consumedAs(
        "$pid.consume.debounced-subdivided-chunks",
        kxsBinary.serde(),
        kxsBinary.serde(),
      ),
    )

  subdividedMapChunkTilesDebounced.forEachChunkSaveImage(tileDirectory)

  return builder.build()
    .addDebounceProcessor<ServerMapChunkId, ServerMapChunkTiles<ColourHex>>(
      namePrefix = pid,
      sourceTopic = TOPIC_SUBDIVIDED_MAP_TILES,
      sinkTopic = TOPIC_SUBDIVIDED_MAP_TILES_DEBOUNCED,
      inactivityDuration = 15.seconds,
      keySerde = kxsBinary.serde(),
      valueSerde = kxsBinary.serde(),
    )
}


private fun KStream<ServerMapChunkId, ServerMapChunkTiles<ColourHex>>.flatMapSubdividedChunk() =
  flatMap("$pid.subdivide-chunks") { initialKey, initialChunk ->

    val (chunks, time) = measureTimedValue {
      ChunkSize.entries.flatMap { chunkSize ->
        initialChunk.map
          .entries
          .groupingBy { (tilePosition, _) ->
            val chunkPosition = tilePosition.toMapChunkPosition(chunkSize.lengthInTiles)
            initialKey.copy(
              chunkSize = chunkSize,
              chunkPosition = chunkPosition,
            )
          }.fold(
            initialValueSelector = { _, _ -> mutableMapOf<MapTilePosition, ColourHex>() }
          ) { _, accumulator, (tilePosition, colour) ->
            accumulator[tilePosition] = colour
            accumulator
          }
          .map { (chunkId, tiles) ->
            chunkId to ServerMapChunkTiles(chunkId, tiles)
          }
      }
    }

    println("subdivided ${initialKey.chunkPosition} to ${chunks.size} chunks in $time")

    chunks
  }


private fun KStream<ServerMapChunkId, ServerMapChunkTiles<ColourHex>>.forEachChunkSaveImage(
  tileDirectory: Path,
) = forEach(
  "$pid.save-tiles"
) { chunkId: ServerMapChunkId, tileColours: ServerMapChunkTiles<ColourHex> ->

  val chunkImage = ImmutableImage.filled(
    chunkId.chunkSize.lengthInTiles,
    chunkId.chunkSize.lengthInTiles,
    TRANSPARENT_AWT,
    BufferedImage.TYPE_INT_ARGB
  )

  val chunkOriginX: Int = chunkId.chunkPosition.x * chunkId.chunkSize.lengthInTiles
  val chunkOriginY: Int = chunkId.chunkPosition.y * chunkId.chunkSize.lengthInTiles

  tileColours.map.forEach { (tilePosition, colour) ->
    val rgbColour = colour.toRgbColor()

    val pixelX = abs(abs(tilePosition.x) - abs(chunkOriginX))
    val pixelY = abs(abs(tilePosition.y) - abs(chunkOriginY))

    chunkImage.setColor(pixelX, pixelY, rgbColour)
  }

  val scaledImage =
    chunkImage.scaleTo(WEB_MAP_TILE_SIZE_PX, WEB_MAP_TILE_SIZE_PX, ScaleMethod.FastScale)

  val filename = TilePngFilename(chunkId)
  saveTile(filename, tileDirectory, scaledImage)
}


@Synchronized
fun saveTile(
  filename: TilePngFilename,
  tileDirectory: Path,
  img: ImmutableImage,
) {
  val chunkImageFile = tileDirectory.resolve(filename.value).toFile()
  if (chunkImageFile.parentFile.mkdirs()) {
    println("created new map tile parentFile directory ${chunkImageFile.absolutePath}")
  }
  val sizeBefore = chunkImageFile.takeIf { it.exists() }?.toPath()?.fileSize()

  val savedTile = img.output(PngWriter.NoCompression, chunkImageFile)

  val sizeAfter = savedTile.takeIf { it.exists() }?.toPath()?.fileSize()

  val sizeDescription = if (sizeBefore != sizeAfter && sizeBefore != null) {
    "$sizeBefore/$sizeAfter"
  } else {
    "$sizeAfter"
  }
  println("savedTile $sizeDescription: $savedTile")
}


@JvmInline
value class TilePngFilename(
  val value: String,
) {
  constructor(id: ServerMapChunkId) : this(buildString {
    append("s${id.surfaceIndex}")
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
