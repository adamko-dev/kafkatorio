package dev.adamko.kafkatorio.processor.config


/** Improved (slightly) version of http4k's `Port`. */
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
      get() = (1..65535) // get() is required to prevent init ordering causing NPE
  }
}
