package dev.adamko.kafkatorio.webmap

import dev.adamko.kafkatorio.events.schema.MapEntityPosition
import dev.adamko.kafkatorio.events.schema.PlayerUpdate
import io.kvision.maps.Maps
import io.kvision.maps.externals.leaflet.geo.LatLng

fun PlayerUpdate.latLng(): LatLng? = position?.latLng()

fun MapEntityPosition.latLng(): LatLng = Maps.L.latLng((this.y * -1) to this.x)
