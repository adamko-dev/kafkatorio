package dev.adamko.kafkatorio.webmap

import dev.adamko.kafkatorio.events.schema.PlayerData
import io.kvision.maps.Maps
import io.kvision.maps.externals.leaflet.geo.LatLng

fun PlayerData.latLng(): LatLng = Maps.L.latLng((position.y * -1) / 2 to position.x / 2)
