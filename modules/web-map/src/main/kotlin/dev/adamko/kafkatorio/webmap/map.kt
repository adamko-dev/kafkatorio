package dev.adamko.kafkatorio.webmap

import io.kvision.maps.Maps
import io.kvision.maps.Maps.Companion.L
import io.kvision.maps.externals.leaflet.DoneCallback
import io.kvision.maps.externals.leaflet.geo.CRS
import io.kvision.maps.externals.leaflet.geo.LatLng
import io.kvision.maps.externals.leaflet.layer.tile.TileLayer
import io.kvision.utils.px
import org.w3c.dom.HTMLElement

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

private const val tileUrlTemplate = """http://localhost:9073/tiles/s1/z{z}/x{x}/y{y}.png"""

fun createFactorioMap() = Maps {
  width = 800.px
  height = 800.px
  margin = 10.px

  configureLeafletMap {
    setView(center = LatLng(0, 0), zoom = 0)

    val baseTileLayer: TileLayer<TileLayer.TileLayerOptions> = L.tileLayer(
      tileUrlTemplate
    ) {
      attribution = "kafkatorio"
      tileSize = 256
      tms = true

      minZoom = -2
      maxZoom = 6

      maxNativeZoom = 3
      minNativeZoom = -1

      noWrap = true
      updateWhenIdle = false
      updateWhenZooming = true
    }

    val currentTileOnLoad: (DoneCallback, HTMLElement) -> Unit =
      baseTileLayer.asDynamic()._tileOnLoad as (DoneCallback, HTMLElement) -> Unit
    baseTileLayer.asDynamic()._tileOnLoad = { done: DoneCallback, tile: HTMLElement ->
      if (tile.hasAttribute("dynamic-reload")) {
        tile.removeAttribute("dynamic-reload")
      } else {
        currentTileOnLoad(done, tile)
      }
    }

    baseTileLayer.addTo(this)

    L.scale {
      metric = true
      imperial = false
    }.addTo(this)


    options.crs = CRS.Simple
    options.zoomSnap = 0
    options.zoomDelta = 0.1

//    options.fadeAnimation = false
  }
}
