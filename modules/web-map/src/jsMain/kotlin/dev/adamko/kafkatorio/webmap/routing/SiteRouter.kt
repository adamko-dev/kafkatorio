package dev.adamko.kafkatorio.webmap.routing

import dev.adamko.kafkatorio.schema.common.FactorioServerId
import dev.adamko.kafkatorio.webmap.App
import dev.adamko.kafkatorio.webmap.state.SiteAction
import dev.adamko.kafkatorio.webmap.rootJob
import dev.adamko.kafkatorio.webmap.services.EventsServerClient
import io.kvision.navigo.Match
import io.kvision.navigo.Navigo
import io.kvision.routing.Routing
import io.kvision.routing.Strategy
import kotlin.coroutines.CoroutineContext
import kotlinx.browser.window
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch


object SiteRouter : CoroutineScope {

  override val coroutineContext: CoroutineContext =
    Dispatchers.Default + SupervisorJob(rootJob) + CoroutineName("SiteRouting")


  private val navigo: Routing = Routing(
    root = "/",
    useHash = false,
    strategy = Strategy.ONE
  )


  fun init() {
    navigo
      .on(SiteView.HOME) {
        log("routing.on HOME")
        homepageView()
      }
      .on(SiteView.SERVER) { match ->
        println("routing.on SERVER ${JSON.stringify(match)}")
        val serverId = match.data["serverId"] as? String ?: error("invalid server id")
        println("routing.on SERVER serverId:$serverId")
        serverView(FactorioServerId(serverId))
      }
      .resolve()

    log("routing.lastResolved(): " + JSON.stringify(navigo.lastResolved()))
  }


  fun updatePageLinks() {
    window.setTimeout(
      handler = {
        log("updating page links")
        navigo.updatePageLinks()
      },
      timeout = 0,
    )
  }


  fun homepageView() {
    App.siteStateStore.dispatch(SiteAction.HomePage)
    launch {
      val serverIds = EventsServerClient.serverIds()
      log("loaded server IDs $serverIds")
      App.siteStateStore.dispatch(SiteAction.ServerIdsLoaded(serverIds))
    }
  }


  fun serverView(serverId: FactorioServerId) {
    App.siteStateStore.dispatch(SiteAction.ServerPage(serverId))
  }


  private fun log(message: String) = println("[SiteRouting] $message")
}


private fun Navigo.on(view: SiteView, handler: (Match) -> Unit): Navigo {
  return when (view) {
    is SiteView.StaticPath  -> on(view.path, { match: Match -> handler(match) })
    is SiteView.DynamicPath -> on(view.path, { match: Match -> handler(match) })
  }
}
