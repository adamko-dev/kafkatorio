package dev.adamko.kafkatorio.processor.topology

import com.sksamuel.scrimage.ImmutableImage
import com.sksamuel.scrimage.color.RGBColor
import com.sksamuel.scrimage.nio.PngWriter
import dev.adamko.kotka.extensions.tables.toStream
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.time.Duration
import kotlin.math.abs
import kotlin.math.roundToInt
import org.apache.kafka.streams.kstream.KTable
import org.apache.kafka.streams.kstream.Suppressed


fun saveTileImages(
  webMapTiles: KTable<WebMapTileChunkPosition, WebMapTileChunkPixels>
) {

  webMapTiles
    .suppress(
      Suppressed.untilTimeLimit<WebMapTileChunkPosition>(
        Duration.ofSeconds(30),
        Suppressed.BufferConfig.maxRecords(30)
      ).withName("save-tile-images-debounce")
    )
    .toStream("get-webmap-tile-updates")
    .foreach { position: WebMapTileChunkPosition, pixels: WebMapTileChunkPixels ->
      runCatching {
        saveMapTilesPng(position, pixels.pixels)
      }.onFailure { e ->
        println("error saving map tile png chunk: $position")
        e.printStackTrace()
        throw e
      }
    }
}

//val saveMapTilesContext = Dispatchers.IO.limitedParallelism(1)


private fun saveMapTilesPng(
  chunkPosition: WebMapTileChunkPosition,
  pixels: Set<WebMapTilePixel>
) {
  val chunkOriginX: Int = chunkPosition.x * chunkPosition.chunkSize // - 1
  val chunkOriginY: Int = chunkPosition.y * chunkPosition.chunkSize // - 1
  val surfaceIndex: Int = chunkPosition.surfaceIndex.index

  val chunkImage =
    ImmutableImage.filled(
      chunkPosition.chunkSize,
      chunkPosition.chunkSize,
      Color.BLACK,
      BufferedImage.TYPE_INT_ARGB
    )

  pixels.forEach { pixel ->

    val rgbColour = RGBColor(
      pixel.mapColour.red.roundToInt(),
      pixel.mapColour.green.roundToInt(),
      pixel.mapColour.blue.roundToInt(),
      pixel.mapColour.alpha.roundToInt(),
    )

    val pixelX = abs(abs(pixel.tilePosition.x) - abs(chunkOriginX))
    val pixelY = abs(abs(pixel.tilePosition.y) - abs(chunkOriginY))

    chunkImage.setColor(
      pixelX,
      pixelY,
      rgbColour
    )

  }

  val zoom = 1u
  val file =
    File(tileFilename("$surfaceIndex", "$zoom", "${chunkPosition.x}", "${chunkPosition.y}"))

  if (file.parentFile.mkdirs()) {
    println("created new map tile parentFile directory ${file.absolutePath}")
  }

  println("saving map tile $file")
  val savedTile = chunkImage.output(PngWriter.NoCompression, file)
  println("savedTile: $savedTile")
}

fun tileFilename(
  surfaceIndex: String,
  zoom: String,
  chunkX: String,
  chunkY: String,
) = "src/main/resources/kafkatorio-web-map/s${surfaceIndex}/z$zoom/x${chunkX}/y${chunkY}.png"

//fun subdivide(tile: ImmutableImage, size: Int) {
//
//  val halfSize = size / 2
//
//  val bl = tile.resizeTo(halfSize, halfSize, Position.BottomLeft)
//  val savedTile = bl.output(PngWriter.NoCompression, file)
//
//  tile.resizeTo(halfSize, halfSize, Position.BottomRight)
//  tile.resizeTo(halfSize, halfSize, Position.TopLeft)
//  tile.resizeTo(halfSize, halfSize, Position.TopRight)
//
//}
