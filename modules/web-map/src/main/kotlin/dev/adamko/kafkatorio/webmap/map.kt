package dev.adamko.kafkatorio.webmap

import externals.leaflet.geo.CRS
import externals.leaflet.geo.LatLng
import io.kvision.maps.DefaultTileLayers
import io.kvision.maps.Maps
import io.kvision.utils.px

//
//val polyline = Maps.L.polyline(
//  listOf(
//    LatLng(55, 2),
//    LatLng(65, 2),
//    LatLng(65, 20),
//    LatLng(55, 20),
//    LatLng(55, 2),
//  )
//) {
//  noClip = true
//}

fun createFactorioMap() = Maps {
  width = 800.px
  height = 800.px
  margin = 10.px

  configureLeafletMap {
    setView(LatLng(0, 0), 13)

//    DefaultTileLayers.OpenStreetMap.addTo(this)

    with(options) {
      crs = CRS.Simple
    }

  }
}
