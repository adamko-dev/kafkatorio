package dev.adamko.kafkatorio.processor.config

import org.http4k.cloudnative.env.Port
import org.http4k.lens.BiDiLensSpec
import org.http4k.lens.StringBiDiMappings.int
import org.http4k.lens.map

/**
 * Improved (slightly) version of http4k's [Port].
 */
@JvmInline
value class PortVal(val value: Int) {

  init {
    require(value in VALID_RANGE) {
      "Out of range. Port: '$value', range: $VALID_RANGE"
    }
  }

  operator fun invoke(): Int = value

  companion object {
    val DEFAULT = PortVal(8080)
    val VALID_RANGE: IntRange
      get() = (1..65535)

    fun <IN : Any> BiDiLensSpec<IN, String>.portVal(): BiDiLensSpec<IN, PortVal> =
      map(int().map(::PortVal, PortVal::value))
  }
}
