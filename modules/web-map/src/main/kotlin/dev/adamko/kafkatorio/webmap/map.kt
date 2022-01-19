package dev.adamko.kafkatorio.webmap

import io.kvision.maps.DefaultTileLayers
import io.kvision.maps.externals.leaflet.geo.CRS
import io.kvision.maps.externals.leaflet.geo.LatLng
import io.kvision.maps.Maps
import io.kvision.maps.Maps.Companion.L
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
    setView(LatLng(0, 0), 1)

//    DefaultTileLayers.OpenStreetMap.addTo(this)
    L.tileLayer(
      """http://localhost:9073/tiles/s1/z1/x{x}/y{y}.png"""
    ) {
      attribution = "kafkatorio"
      tms = true
      minZoom = 1
    }.addTo(this)

    with(options) {
      crs = CRS.Simple
    }

  }
}
