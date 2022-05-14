package dev.adamko.kafkatorio.processor.topology

import com.sksamuel.scrimage.color.RGBColor
import dev.adamko.kafkatorio.schema.common.ColourHex
import java.awt.Color
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlinx.serialization.Serializable


@JvmInline
@Serializable
value class FactorioServerId(private val id: String) {
  override fun toString() = id
}
