package dev.adamko.kafkatorio.webmap

import dev.adamko.kafkatorio.schema.common.FactorioServerId
import dev.adamko.kafkatorio.schema.common.Tick
import dev.adamko.kafkatorio.schema.packets.EventServerPacket
import dev.adamko.kafkatorio.schema.packets.PlayerUpdate
import dev.adamko.kafkatorio.webmap.services.EventsServerClient
import dev.adamko.kafkatorio.webmap.state.FactorioGameState
import io.kvision.navigo.Match
import io.kvision.navigo.Navigo
import io.kvision.redux.RAction
import io.kvision.redux.ReduxStore
import io.kvision.routing.routing
import kotlin.js.RegExp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch


class SiteRouting(
  val siteStateStore: ReduxStore<SiteState, SiteAction>
) : CoroutineScope by CoroutineScope(Dispatchers.Default + SupervisorJob()) {

  fun init() {
    routing
      .on(SiteView.HOME) { homepageView() }
      .on(SiteView.SERVER) { match ->
        serverView(match)
      }
      .resolve()
  }

  private fun homepageView() {
    siteStateStore.dispatch(SiteAction.HomePage)
    launch {
      val serverIds = EventsServerClient.serverIds()
      siteStateStore.dispatch(SiteAction.ServerIdsLoaded(serverIds))
    }
  }

  private fun serverView(match: Match) {
    val serverId = match.data[0] as? String ?: error("invalid server id")
    siteStateStore.dispatch(SiteAction.ServerPage(FactorioServerId(serverId)))
  }
}


data class SiteState(
  val view: SiteView = SiteView.HOME,

  val serverIds: List<FactorioServerId>? = null,

  val factorioGameState: FactorioGameState? = null,
) {

  operator fun plus(action: SiteAction): SiteState = when (action) {
    is SiteAction.HomePage          -> copy(view = SiteView.HOME)

    is SiteAction.ServerPage        -> copy(
      view = SiteView.SERVER,
      factorioGameState = FactorioGameState(action.serverId),
    )

    is SiteAction.ServerIdsLoaded   -> copy(serverIds = action.serverIds)

    is SiteAction.FactorioUpdate    -> copy(
      factorioGameState = factorioGameState?.let { it + action }
    )

    is SiteAction.EventServerUpdate -> copy(

    )
  }
}


sealed interface SiteAction : RAction {
  object HomePage : SiteAction
  data class ServerPage(val serverId: FactorioServerId) : SiteAction


  data class ServerIdsLoaded(val serverIds: List<FactorioServerId>) : SiteAction


  /** Updates from `events-server` */
  sealed class FactorioUpdate : SiteAction {
    data class Player(val tick: Tick, val data: PlayerUpdate) : FactorioUpdate()
  }

  data class EventServerUpdate(val packet: EventServerPacket) : SiteAction
}


sealed interface SiteView {
  val pathMatcher: Any

  sealed interface StaticPath : SiteView {
    override val pathMatcher: String
  }

  sealed interface DynamicPath : SiteView {
    override val pathMatcher: RegExp
  }


  object HOME : StaticPath {
    override val pathMatcher: String = "/"
  }

  object SERVER : DynamicPath {
    override val pathMatcher: RegExp = RegExp("^servers/${FactorioServerId.validIdRegex}")
  }
}

//val SiteView.urlMatcher: String
//  get() = when (this) {
//    SiteView.HOME   -> "/"
//    SiteView.SERVER -> "servers/${FactorioServerId.validIdRegex}"
//  }

private fun Navigo.on(view: SiteView, handler: (Match) -> Unit): Navigo {
  return when (view) {
    is SiteView.StaticPath  -> on(view.pathMatcher, { match: Match -> handler(match) })
    is SiteView.DynamicPath -> on(view.pathMatcher, { match: Match -> handler(match) })
  }
}

//private fun Navigo.on(
//  view: SiteView,
//  before: BeforeHook? = null,
//  after: AfterHook? = null,
//  leave: LeaveHook? = null,
//  already: AlreadyHook? = null,
//  handler: (Match) -> Unit,
//): Navigo {
//
//  val hooks: RouteHooks? =
//    if (before != null || after != null || leave != null || already != null) {
//      jso {
//        if (before != null) this@jso.before = before
//        if (after != null) this@jso.after = after
//        if (leave != null) this@jso.leave = leave
//        if (already != null) this@jso.already = already
//      }
//    } else {
//      null
//    }
//
//  return when (view) {
//    is SiteView.StaticPath  -> when (hooks) {
//      null -> on(view.pathMatcher, { match: Match -> handler(match) })
//      else -> on(view.pathMatcher, { match: Match -> handler(match) }, hooks)
//    }
//
//    is SiteView.DynamicPath -> when (hooks) {
//      null -> on(view.pathMatcher, { match: Match -> handler(match) })
//      else -> on(view.pathMatcher, { match: Match -> handler(match) }, hooks)
//    }
//  }
//}
