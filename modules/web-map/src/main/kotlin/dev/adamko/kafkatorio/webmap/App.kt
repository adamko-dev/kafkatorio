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
import io.kvision.module
import io.kvision.panel.root
import io.kvision.redux.ReduxStore
import io.kvision.startApplication


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
  }

  private val kvMaps: Maps
    get() = reduxStore.getState().kvMaps

  override fun start(state: Map<String, Any>) {
    this.appState = state.toMutableMap()

    root("kvapp") {
      div("Kafkatorio Web Map")
      add(kvMaps)
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
    CoreModule
  )
}
