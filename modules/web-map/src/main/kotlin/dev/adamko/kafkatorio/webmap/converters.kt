package dev.adamko.kafkatorio.webmap

import dev.adamko.kafkatorio.events.schema.Colour
import dev.adamko.kafkatorio.events.schema.PlayerData
import externals.leaflet.geo.LatLng
import io.kvision.maps.Maps
import kotlin.math.roundToInt


fun Colour.toHexString(includeAlpha: Boolean = false): String {

  val hexConverter: Float.() -> String = if (isDecimal()) {
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

fun PlayerData.latLng(): LatLng = Maps.L.latLng((position.y * -1) to position.x)
