package dev.adamko.kafkatorio.webmap.services

import dev.adamko.kafkatorio.schema.common.MapEntityPosition
import dev.adamko.kafkatorio.schema.packets.PlayerUpdate
import io.kvision.maps.Maps
import io.kvision.maps.externals.leaflet.geo.LatLng

fun PlayerUpdate.latLng(): LatLng? = position?.latLng()

fun MapEntityPosition.latLng(): LatLng = Maps.L.latLng((this.y * -1) to this.x)
