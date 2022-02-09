package dev.adamko.kafkatorio.webmap

import io.kvision.maps.Maps
import io.kvision.maps.Maps.Companion.L
import io.kvision.maps.externals.leaflet.geo.CRS
import io.kvision.maps.externals.leaflet.geo.LatLng
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


    L.tileLayer(
      """http://localhost:9073/tiles/s1/z{z}/x{x}/y{y}.png"""
    ) {
      attribution = "kafkatorio"
      tileSize = 256
      tms = true

      minZoom = -2
      maxZoom = 6

      // NOTE: offset the zoom, so 1 meter = 1 tile
      maxNativeZoom = 3
      minNativeZoom = -1
      zoomOffset = 1

      noWrap = true
      updateWhenIdle = false
      updateWhenZooming = true
    }.addTo(this)


    L.scale {
      metric = true
      imperial = false
    }.addTo(this)


    options.crs = CRS.Simple
    options.zoomSnap = 0.1

  }
}
