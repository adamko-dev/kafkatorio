package dev.adamko.kafkatorio.webmap

import dev.adamko.kafkatorio.events.schema.Colour
import dev.adamko.kafkatorio.events.schema.PlayerData
import io.kvision.maps.externals.leaflet.geo.LatLng
import io.kvision.maps.Maps
import kotlin.math.roundToInt

fun PlayerData.latLng(): LatLng = Maps.L.latLng((position.y * -1) to position.x)
