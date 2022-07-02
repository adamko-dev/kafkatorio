package dev.adamko.kafkatorio.schema.common

import dev.adamko.kxstsgen.core.experiments.TupleSerializer
import kotlin.math.roundToInt
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Red, green, blue and alpha values, all in range `[0, 1]` or all in range `[0, 255]` if any
 * value is > 1.
 *
 * All values here are optional. Colour channels default to `0`, the alpha channel defaults to `1`.
 */
@Serializable(with = Colour.Serializer::class)
@SerialName("kafkatorio.common.Colour")
data class Colour(
  val red: Float = 0f,
  val green: Float = 0f,
  val blue: Float = 0f,
  val alpha: Float = 1f,
) {
  object Serializer : TupleSerializer<Colour>(
    "Colour",
    {
      element(Colour::red)
      element(Colour::green)
      element(Colour::blue)
      element(Colour::alpha)
    }
  ) {
    override fun tupleConstructor(elements: Iterator<*>): Colour {
      return Colour(
        elements.next() as Float,
        elements.next() as Float,
        elements.next() as Float,
        elements.next() as Float,
      )
    }
  }
}


/** Size-efficient version of [Colour] (`4*4` bytes vs `4*1` bytes) */
@Serializable
@SerialName("kafkatorio.common.ColourHex")
data class ColourHex(
  val red: UByte = UByte.MIN_VALUE,
  val green: UByte = UByte.MIN_VALUE,
  val blue: UByte = UByte.MIN_VALUE,
  val alpha: UByte = UByte.MAX_VALUE,
) {
  companion object {
    val TRANSPARENT = ColourHex(0u, 0u, 0u, 0u)
  }
}


/* *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  * */


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
