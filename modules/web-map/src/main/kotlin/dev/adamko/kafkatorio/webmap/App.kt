package dev.adamko.kafkatorio.webmap

import dev.adamko.kafkatorio.schema.packets.EventServerPacket
import dev.adamko.kafkatorio.webmap.layout.headerNav
import dev.adamko.kafkatorio.webmap.layout.homePage
import dev.adamko.kafkatorio.webmap.layout.serverPage
import dev.adamko.kafkatorio.webmap.services.WebsocketService
import io.kvision.Application
import io.kvision.BootstrapCssModule
import io.kvision.BootstrapIconsModule
import io.kvision.BootstrapModule
import io.kvision.ChartModule
import io.kvision.CoreModule
import io.kvision.FontAwesomeModule
import io.kvision.html.div
import io.kvision.html.header
import io.kvision.html.main
import io.kvision.module
import io.kvision.panel.root
import io.kvision.redux.ReduxStore
import io.kvision.redux.createReduxStore
import io.kvision.require
import io.kvision.routing.Routing
import io.kvision.startApplication
import io.kvision.state.bind
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


val rootJob = Job()


class App : Application() {

  private val coroutineScope: CoroutineScope = CoroutineScope(
    Dispatchers.Default
        + SupervisorJob(rootJob)
        + CoroutineName("WebMapApp")
  )

  private var appState: MutableMap<String, Any> = mutableMapOf()

  var reduxStore: ReduxStore<SiteState, SiteAction> by appState
    private set
//  var wsService: WebsocketService by appState
//    private set

  init {
    this.reduxStore = createReduxStore(SiteState::plus, SiteState())
//    this.wsService = wsService
    require("./css/kafkatorio.css")
  }

//  private val gameState: FactorioGameState
//    get() = reduxStore.getState()

//  private val kvMaps: Maps
//    get() = gameState.map.kvMap

  override fun start(state: Map<String, Any>) {
    Routing.init(useHash = false)

    this.appState = state.toMutableMap()

    val siteRouting = SiteRouting(reduxStore)
    val websocketService = WebsocketService(reduxStore)

    siteRouting.init()

    root("kvapp") {

      header().bind(siteRouting.siteStateStore) { state ->
        headerNav(state)
      }

      div("Kafkatorio Web Map")

      main().bind(siteRouting.siteStateStore) { state ->
        when (state.view) {
          SiteView.HOME   -> homePage(state)
          SiteView.SERVER -> serverPage(state)
        }
      }

//      div("Kafkatorio Web Map")
//      add(kvMaps)
    }

//    kvMaps.leafletMap {
//      invalidateSize(true)
//      eachLayer({
//        if (it is GridLayer<*>) {
//          it.redraw()
//        }
//      })
//    }

    websocketService.packetsFlow
      .filterIsInstance<EventServerPacket.ChunkTileSaved>()
      .onEach { tileSavedEvent ->
//        println("[packetsFlow] triggering tile refresh ${tileSavedEvent.filename.value}")
        reduxStore.dispatch(SiteAction.EventServerUpdate(tileSavedEvent))
//        reduxStore.map.refreshUpdatedTilePng(tileSavedEvent.filename)
      }.launchIn(coroutineScope)

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
//      val reduxStore = createFactorioReduxStore()
//      val wsService = WebsocketService(
//        reduxStore = reduxStore,
//      )
      App(
//        reduxStore = reduxStore,
//        wsService = wsService,
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
