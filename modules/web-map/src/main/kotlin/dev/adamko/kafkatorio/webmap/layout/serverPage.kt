package dev.adamko.kafkatorio.webmap.layout

import dev.adamko.kafkatorio.webmap.SiteState
import io.kvision.core.Container
import io.kvision.html.div
import io.kvision.maps.externals.leaflet.layer.tile.GridLayer


fun Container.serverPage(state: SiteState) {
  val factorioGameState = state.factorioGameState
  div(className = "server-page") {
    if (factorioGameState != null) {

      add(factorioGameState.map.kvMap)

      factorioGameState.map.kvMap.leafletMap {
        invalidateSize(true)
        eachLayer({
          if (it is GridLayer<*>) {
            it.redraw()
          }
        })
      }
    }
  }
}
