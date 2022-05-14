package dev.adamko.kafkatorio.webmap

import io.kvision.Application
import io.kvision.BootstrapCssModule
import io.kvision.BootstrapIconsModule
import io.kvision.BootstrapModule
import io.kvision.ChartModule
import io.kvision.CoreModule
import io.kvision.FontAwesomeModule
import io.kvision.html.div
import io.kvision.maps.Maps
import io.kvision.maps.externals.leaflet.layer.tile.GridLayer
import io.kvision.module
import io.kvision.panel.root
import io.kvision.redux.ReduxStore
import io.kvision.require
import io.kvision.startApplication
import kotlin.time.Duration.Companion.seconds
import kotlinx.js.timers.setInterval
import org.w3c.dom.Image
import org.w3c.dom.asList


class App(
  reduxStore: ReduxStore<FactorioGameState, FactorioUpdate>,
  wsService: WebsocketService
) : Application() {
  private var appState: MutableMap<String, Any> = mutableMapOf()

  var reduxStore: ReduxStore<FactorioGameState, FactorioUpdate> by appState
  var wsService: WebsocketService by appState

  init {
    this.reduxStore = reduxStore
    this.wsService = wsService
    require("./css/kafkatorio.css")
  }

  private val kvMaps: Maps
    get() = reduxStore.getState().kvMaps

  override fun start(state: Map<String, Any>) {
    this.appState = state.toMutableMap()

    val root = root("kvapp") {
      div("Kafkatorio Web Map")
      add(kvMaps)
    }

    kvMaps.leafletMap {
      invalidateSize(true)
      eachLayer({
        if (it is GridLayer<*>) {
          it.redraw()
        }
      })
//      this.asDynamic()._fadeAnimated = false
    }

    setInterval(1.seconds) {
      val doc = root.getElement()?.ownerDocument
      val window = doc!!.defaultView!!

      doc
        .querySelectorAll("img.leaflet-tile-loaded")
        .asList()
        .filterIsInstance<Image>()
        .map { img ->
          val imgSrc = img.src.substringBeforeLast('?')
          println("fetching $imgSrc")

          var newImg : Image? = Image()

          newImg!!.onload = {

            window.requestAnimationFrame {
              println("image loaded ${newImg!!.src}")
              img.setAttribute("dynamic-reload", "true")
              img.src = newImg!!.src
              newImg = null // try to encourage garbage collection
              Unit
            }
          }
          newImg!!.src = imgSrc //+ "?t=${currentTimeMillis()}"
        }
    }
  }

  override fun dispose(): Map<String, Any> {
    return appState
  }

}

fun main() {
  startApplication(
    {
      val reduxStore = createFactorioReduxStore()
      val wsService = WebsocketService(
        "ws://localhost:9073/ws",
        reduxStore,
      )
      App(
        reduxStore = reduxStore,
        wsService = wsService
      )
    },
    module.hot,
    BootstrapModule,
    BootstrapCssModule,
    BootstrapIconsModule,
    FontAwesomeModule,
    ChartModule,
    CoreModule,
  )
}
