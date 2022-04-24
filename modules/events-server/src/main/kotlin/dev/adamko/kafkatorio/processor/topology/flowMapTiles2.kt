package dev.adamko.kafkatorio.processor.topology

import com.sksamuel.scrimage.ImmutableImage
import com.sksamuel.scrimage.ScaleMethod
import com.sksamuel.scrimage.nio.PngWriter
import dev.adamko.kafkatorio.processor.admin.TOPIC_GROUPED_MAP_CHUNKS_STATE
import dev.adamko.kafkatorio.processor.serdes.kxsBinary
import dev.adamko.kafkatorio.schema.common.ColourHex
import dev.adamko.kafkatorio.schema.common.toMapChunkPosition
import dev.adamko.kotka.extensions.consumedAs
import dev.adamko.kotka.extensions.streams.flatMap
import dev.adamko.kotka.extensions.streams.forEach
import dev.adamko.kotka.kxs.serde
import java.awt.image.BufferedImage
import java.io.File
import kotlin.io.path.fileSize
import kotlin.math.abs
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.Topology
import org.apache.kafka.streams.kstream.KStream


private const val WEB_MAP_TILE_SIZE_PX = 256


fun saveMapTiles2(
  builder: StreamsBuilder,
): Topology {
  val pid = "saveMapTiles"

  val groupedMapChunkTiles: KStream<ServerMapChunkId, ServerMapChunkTiles<ColourHex>> =
    builder.stream(
      TOPIC_GROUPED_MAP_CHUNKS_STATE,
      consumedAs("$pid.consume.grouped-map-chunks", kxsBinary.serde(), kxsBinary.serde())
    )

  groupedMapChunkTiles.flatMap("$pid.subdivide-chunks") { initialKey, initialChunk ->
    ChunkSize.entries.flatMap { chunkSize ->
      initialChunk.map.entries
        .groupBy(
          { (tile, _) -> tile.toMapChunkPosition(chunkSize.tilesPerChunk) }
        ) { (tilePosition, colour) ->
          tilePosition to colour
        }
        .map { (chunkPos, tiles) ->
          val key = initialKey.copy(
            chunkSize = chunkSize,
            chunkPosition = chunkPos
          )

          val value = ServerMapChunkTiles(key, tiles.toMap())

          key to value
        }
    }
  }.forEach("$pid.save-tiles") { key, value ->
    val filename = TilePngFilename(key)
    val chunkColours = value.map

    val chunkImage = ImmutableImage.filled(
      key.chunkSize.tilesPerChunk,
      key.chunkSize.tilesPerChunk,
      TRANSPARENT_AWT,
      BufferedImage.TYPE_INT_ARGB
    )

    val chunkOriginX: Int = key.chunkPosition.x * key.chunkSize.tilesPerChunk
    val chunkOriginY: Int = key.chunkPosition.y * key.chunkSize.tilesPerChunk

    chunkColours.forEach { (tilePosition, colour) ->
      val rgbColour = colour.toRgbColor()

      val pixelX = abs(abs(tilePosition.x) - abs(chunkOriginX))
      val pixelY = abs(abs(tilePosition.y) - abs(chunkOriginY))

      chunkImage.setColor(pixelX, pixelY, rgbColour)
    }

    val img = chunkImage.scaleTo(WEB_MAP_TILE_SIZE_PX, WEB_MAP_TILE_SIZE_PX, ScaleMethod.FastScale)

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

  return builder.build()
}
