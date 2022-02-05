package dev.adamko.kafkatorio.processor.topology

import com.sksamuel.scrimage.color.RGBColor
import dev.adamko.kafkatorio.events.schema.Colour
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
