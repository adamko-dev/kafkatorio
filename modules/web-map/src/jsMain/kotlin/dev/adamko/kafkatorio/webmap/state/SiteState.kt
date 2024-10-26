package dev.adamko.kafkatorio.webmap.state

import dev.adamko.kafkatorio.schema.common.FactorioServerId
import dev.adamko.kafkatorio.schema.packets.EventServerPacket
import dev.adamko.kafkatorio.webmap.rootJob
import dev.adamko.kafkatorio.webmap.routing.SiteView
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch


data class SiteState(
  val view: SiteView = SiteView.HOME,

  val serverIds: List<FactorioServerId>? = null,

  val factorioGameState: FactorioGameState? = null,
) : CoroutineScope {

  override val coroutineContext: CoroutineContext =
    Dispatchers.Default + SupervisorJob(rootJob) + CoroutineName("SiteState")

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

    is SiteAction.EventServerUpdate -> when (action.packet) {
      is EventServerPacket.ChunkTileSaved -> {
        if (factorioGameState?.map != null) {
          launch {
            factorioGameState.map.refreshUpdatedTilePng(action.packet.filename)
          }
        }
        this
      }

      is EventServerPacket.Kafkatorio     -> {
        this
      }
    }
  }
}
