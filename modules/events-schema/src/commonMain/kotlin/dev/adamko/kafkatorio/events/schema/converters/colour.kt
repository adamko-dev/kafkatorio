package dev.adamko.kafkatorio.events.schema.converters

import dev.adamko.kafkatorio.events.schema.Colour
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

fun Colour.toHexadecimal() = if (isPercentage()) {
  Colour(
    (red * 255).coerceIn(0f..255f),
    (green * 255).coerceIn(0f..255f),
    (blue * 255).coerceIn(0f..255f),
    (alpha * 255).coerceIn(0f..255f),
  )
} else {
  Colour(
    red.coerceIn(0f..255f),
    green.coerceIn(0f..255f),
    blue.coerceIn(0f..255f),
    alpha.coerceIn(0f..255f),
  )
}

fun Colour.toPercentage() = if (isPercentage()) {
  this
} else {
  Colour(
    (red / 255).coerceIn(0f..1f),
    (green / 255).coerceIn(0f..1f),
    (blue / 255).coerceIn(0f..1f),
    (alpha / 255).coerceIn(0f..1f),
  )
}

fun Colour.toHexString(includeAlpha: Boolean = false): String {

  val hexConverter: Float.() -> String = if (isHexadecimal()) {
    Float::decimalToHex
  } else {
    Float::percentageToHex
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

fun Float.decimalToHex(): String =
  roundToInt().toString(16).padStart(2, ' ')

fun Float.percentageToHex(): String =
  times(255).roundToInt().toString(16).padStart(2, ' ')
