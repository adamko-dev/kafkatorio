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
      Suppressed.untilTimeLimit(
        Duration.ofSeconds(30),
        Suppressed.BufferConfig.maxRecords(30)
      )
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


private fun saveMapTilesPng(
  chunkPosition: WebMapTileChunkPosition,
  pixels: Set<WebMapTilePixel>
) {
  val chunkOriginX: Int = chunkPosition.x * chunkPosition.chunkSize
  val chunkOriginY: Int = chunkPosition.y * chunkPosition.chunkSize
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
    File(
      "src/main/resources/kafkatorio-web-map/s${surfaceIndex}/z$zoom/x${chunkPosition.x}/y${chunkPosition.y}.png"
    )

  if (file.parentFile.mkdirs()) {
    println("created new map tile parentFile directory ${file.absolutePath}")
  }

  println("saving map tile $file")

  chunkImage.output(PngWriter.MaxCompression, file)
}
