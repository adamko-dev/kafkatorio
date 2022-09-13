package dev.adamko.kafkatorio.webmap

import dev.adamko.kafkatorio.schema.packets.EventServerPacket
import dev.adamko.kafkatorio.webmap.layout.headerNav
import dev.adamko.kafkatorio.webmap.layout.homePage
import dev.adamko.kafkatorio.webmap.layout.servers.serverPage
import dev.adamko.kafkatorio.webmap.routing.SiteRouter
import dev.adamko.kafkatorio.webmap.routing.SiteView
import dev.adamko.kafkatorio.webmap.services.WebsocketService
import dev.adamko.kafkatorio.webmap.state.SiteAction
import dev.adamko.kafkatorio.webmap.state.SiteState
import io.kvision.Application
import io.kvision.BootstrapIconsModule
import io.kvision.BootstrapModule
import io.kvision.ChartModule
import io.kvision.CoreModule
import io.kvision.FontAwesomeModule
import io.kvision.html.Link
import io.kvision.html.div
import io.kvision.html.header
import io.kvision.html.main
import io.kvision.module
import io.kvision.panel.root
import io.kvision.redux.ReduxStore
import io.kvision.redux.createReduxStore
import io.kvision.require
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



object App : Application() {

  private val coroutineScope: CoroutineScope = CoroutineScope(
    Dispatchers.Default
        + SupervisorJob(rootJob)
        + CoroutineName("WebMapApp")
  )

  private var appState: MutableMap<String, Any> = mutableMapOf()

  var siteStateStore: ReduxStore<SiteState, SiteAction> by appState
    private set


  init {
    this.siteStateStore = createReduxStore(SiteState::plus, SiteState())

    require("./css/kafkatorio.css")

//    WebsocketService.packetsFlow
//      .filterIsInstance<EventServerPacket.ChunkTileSaved>()
//      .onEach { tileSavedEvent ->
////        println("[packetsFlow] triggering tile refresh ${tileSavedEvent.filename.value}")
////        siteStateStore.dispatch(SiteAction.EventServerUpdate(tileSavedEvent))
//        siteStateStore.getState().factorioGameState?.map?.refreshUpdatedTilePng(tileSavedEvent.filename)
//      }.launchIn(coroutineScope)
  }


  override fun start(state: Map<String, Any>) {
    SiteRouter.init()

    this.appState = state.toMutableMap()

    root("kvapp") {
      header().bind(siteStateStore) { state ->
        headerNav(state)
      }

      main().bind(siteStateStore) { state ->
        div(className = "container-fluid") {
          when (state.view) {
            SiteView.HOME   -> homePage(state)
            SiteView.SERVER -> serverPage(state)
          }
        }
        SiteRouter.updatePageLinks()
      }
    }

//    kvMaps.leafletMap {
//      invalidateSize(true)
//      eachLayer({
//        if (it is GridLayer<*>) {
//          it.redraw()
//        }
//      })
//    }

  }


  override fun dispose(): Map<String, Any> {
    rootJob.cancelChildren()
    rootJob.cancel("app dispose")
    rootJob.complete()
    return appState
  }

}
