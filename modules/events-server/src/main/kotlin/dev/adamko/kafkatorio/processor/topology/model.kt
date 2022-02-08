package dev.adamko.kafkatorio.processor.topology

import com.sksamuel.scrimage.color.RGBColor
import dev.adamko.kafkatorio.events.schema.Colour
import dev.adamko.kafkatorio.events.schema.ColourHex
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlinx.serialization.Serializable


@Serializable
@JvmInline
value class SurfaceIndex(
  val index: Int
) {
  override fun toString() = "$index"
}


@JvmInline
@Serializable
value class FactorioServerId(val id: String) {
  override fun toString() = id
}


fun ColourHex.toRgbColor(): RGBColor {
  return RGBColor(
    red.toInt(),
    green.toInt(),
    blue.toInt(),
    alpha.toInt(),
  )
}


enum class ZoomLevel(
  val level: Int
) {
  ZOOM_0(0), // 512
  ZOOM_1(1), // 256
  ZOOM_2(2), // 128
  ZOOM_3(3), // 64
  ZOOM_4(4), // 32
  ;

  val chunkSize: Int = 2f.pow(9 - level).roundToInt()

  init {
    require(chunkSize > 0) { "chunkSize must be positive" }
  }

  companion object {
    val values: List<ZoomLevel> = values().asList() // better performance, KT-48872
  }
}
