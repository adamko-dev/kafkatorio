package dev.adamko.kafkatorio.processor.topology

import com.sksamuel.scrimage.color.RGBColor
import dev.adamko.kafkatorio.events.schema.Colour
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


fun Colour.toRgbColor(): RGBColor {
  return RGBColor(
    red.roundToInt(),
    green.roundToInt(),
    blue.roundToInt(),
    alpha.roundToInt(),
  )
}


enum class ZoomLevel(
  val level: Int
) {
  ZOOM_0(0),
  ZOOM_1(1),
  ZOOM_2(2),
  ZOOM_3(3),
  ZOOM_4(4),
  ZOOM_5(5),
  ;

  val chunkSize: Int = 2f.pow(10 - level).roundToInt()

  init {
    require(chunkSize > 0) { "chunkSize must be positive" }
  }

  companion object {
    val values: List<ZoomLevel> = values().asList() // better performance, KT-48872
  }
}
