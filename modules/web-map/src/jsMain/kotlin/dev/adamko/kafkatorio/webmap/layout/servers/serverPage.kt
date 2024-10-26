package dev.adamko.kafkatorio.webmap.layout.servers

import dev.adamko.kafkatorio.webmap.state.SiteState
import io.kvision.core.Container
import io.kvision.html.div
import io.kvision.maps.externals.leaflet.layer.tile.GridLayer


fun Container.serverPage(state: SiteState) {
  val factorioGameState = state.factorioGameState

  div(className = "server-page container-fluid") {
    if (factorioGameState != null) {

      add(factorioGameState.map.kvMap)
    }
  }


// TODO figure out how to run this after adding kvMap to the container...
// (doing it now causes an NPE because kvMap isn't added to the DOM yet
//  factorioGameState?.map?.kvMap?.leafletMap {
//    invalidateSize(true)
//    eachLayer({
//      if (it is GridLayer<*>) {
//        it.redraw()
//      }
//    })
//  }
}
