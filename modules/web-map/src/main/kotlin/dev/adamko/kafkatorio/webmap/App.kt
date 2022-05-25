package dev.adamko.kafkatorio.webmap

import dev.adamko.kafkatorio.schema.packets.EventServerPacket
import dev.adamko.kafkatorio.webmap.state.FactorioGameState
import dev.adamko.kafkatorio.webmap.state.FactorioUpdate
import dev.adamko.kafkatorio.webmap.state.createFactorioReduxStore
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
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


val rootJob = Job()


class App(
  reduxStore: ReduxStore<FactorioGameState, FactorioUpdate>,
  wsService: WebsocketService,
) : Application(), CoroutineScope {

  override val coroutineContext: CoroutineContext =
    CoroutineName("WebMapApp") + Job(rootJob)

  private var appState: MutableMap<String, Any> = mutableMapOf()

  var reduxStore: ReduxStore<FactorioGameState, FactorioUpdate> by appState
  var wsService: WebsocketService by appState

  init {
    this.reduxStore = reduxStore
    this.wsService = wsService
    require("./css/kafkatorio.css")
  }

  private val gameState: FactorioGameState
    get() = reduxStore.getState()

  private val kvMaps: Maps
    get() = gameState.map.kvMap


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

    val doc = root.getElement()?.ownerDocument
    val window = doc!!.defaultView!!

    wsService.packetsFlow
      .filterIsInstance<EventServerPacket.ChunkTileSaved>()
      .onEach {
        println("[packetsFlow] triggering tile refresh ${it.filename.value}")
        gameState.map.refreshUpdatedTilePng(doc, window, it.filename)
      }.launchIn(this)

  }

  override fun dispose(): Map<String, Any> {
    rootJob.cancelChildren()
    rootJob.cancel("app dispose")
    rootJob.complete()
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
        wsService = wsService,
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
