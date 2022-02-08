package dev.adamko.kafkatorio.events.schema.converters

import dev.adamko.kafkatorio.events.schema.Colour
import dev.adamko.kafkatorio.events.schema.ColourHex
import kotlin.math.roundToInt


/** True if any value is greater than 1, so the values are hexadecimal. */
fun Colour.isHexadecimal(): Boolean {
  return red > 1f
      || green > 1f
      || blue > 1f
      || alpha > 1f
}

/** True if all values are between `[0..1]`. */
fun Colour.isPercentage(): Boolean = !isHexadecimal()


fun Colour.toHexString(includeAlpha: Boolean = false): String {

  val hexConverter: Float.() -> String = when {
    isHexadecimal() -> Float::hexadecimalToString
    else            -> Float::percentageToHexadecimalString
  }

  val length = if (includeAlpha) 9 else 7
  return buildString(length) {
    append('#')
    append(red.hexConverter())
    append(green.hexConverter())
    append(blue.hexConverter())
    if (includeAlpha) {
      append(alpha.hexConverter())
    }
  }
}

fun Float.hexadecimalToString(): String =
  roundToInt().toString(16).padStart(2, ' ')

fun Float.percentageToHexadecimalString(): String =
  times(255).roundToInt().toString(16).padStart(2, ' ')


private val ubyteMaxFloat = UByte.MAX_VALUE.toFloat()
private val ubyteMinFloat = UByte.MIN_VALUE.toFloat()
private val ubyteRangeFloat = ubyteMinFloat..ubyteMaxFloat


private fun Float.toColourHex() = (this * ubyteMaxFloat)
  .coerceIn(ubyteRangeFloat)
  .toUInt()
  .toUByte()


private fun Float.coerceToColourHex() = coerceIn(ubyteRangeFloat)
  .toUInt()
  .toUByte()


fun Colour.toHex(): ColourHex {

  val converter: Float.() -> UByte = when {
    isPercentage() -> Float::toColourHex
    else           -> Float::coerceToColourHex
  }

  return ColourHex(
    red.converter(),
    green.converter(),
    blue.converter(),
    alpha.converter(),
  )
}

private fun UByte.toPercentileFloat() = (this.toFloat() / ubyteMaxFloat).coerceIn(0f..1f)

fun ColourHex.toPercentile(): Colour {
  return Colour(
    red.toPercentileFloat(),
    green.toPercentileFloat(),
    blue.toPercentileFloat(),
    alpha.toPercentileFloat(),
  )
}
