package dev.adamko.kafkatorio.webmap

import io.kvision.maps.Maps
import io.kvision.maps.Maps.Companion.L
import io.kvision.maps.externals.leaflet.DoneCallback
import io.kvision.maps.externals.leaflet.control.Control
import io.kvision.maps.externals.leaflet.control.Layers
import io.kvision.maps.externals.leaflet.control.set
import io.kvision.maps.externals.leaflet.geo.CRS
import io.kvision.maps.externals.leaflet.geo.LatLng
import io.kvision.maps.externals.leaflet.layer.LayerGroup
import io.kvision.maps.externals.leaflet.layer.tile.TileLayer
import io.kvision.utils.obj
import io.kvision.utils.px
import org.w3c.dom.HTMLElement


class FactorioMap(
  private val tileUrlTemplate: String = """http://localhost:9073/tiles/s1/z{z}/x{x}/y{y}.png""",
) {

  val kvMap: Maps = Maps {
    width = 800.px
    height = 800.px
    margin = 10.px
  }

  val factorioTerrainLayer: TileLayer<*> = buildFactorioTerrainLayer()

  val playerIconsLayer: LayerGroup = LayerGroup()

  init {
    val baseLayers: Control.LayersObject = obj<Control.LayersObject> {}
    baseLayers["Terrain"] = factorioTerrainLayer

    val overlays: Control.LayersObject = obj<Control.LayersObject> {}
    overlays["Players"] = playerIconsLayer

    val layersControl = Layers(
      baseLayers = baseLayers,
      overlays = overlays,
      options = obj<Layers.LayersOptions> {
        sortLayers = true
      }
    )

    kvMap.configureLeafletMap {
      setView(center = LatLng(0, 0), zoom = 0)

      L.scale {
        metric = true
        imperial = false
      }.addTo(this)

      options.crs = CRS.Simple
      options.zoomSnap = 0
      options.zoomDelta = 0.1
//    options.fadeAnimation = false
      addControl(layersControl)

      factorioTerrainLayer.addTo(this)
      playerIconsLayer.addTo(this)
    }
  }


  private fun buildFactorioTerrainLayer(): TileLayer<TileLayer.TileLayerOptions> {

    val baseTileLayer = L.tileLayer(
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

    return baseTileLayer
  }
}
