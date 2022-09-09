package dev.adamko.kafkatorio.webmap.state

import dev.adamko.kafkatorio.schema.common.FactorioServerId
import dev.adamko.kafkatorio.schema.common.PlayerIndex
import dev.adamko.kafkatorio.webmap.FactorioMap
import io.kvision.maps.externals.leaflet.layer.LayerGroup


//fun createFactorioReduxStore(): ReduxStore<FactorioGameState, FactorioUpdate> =
//  createReduxStore(
//    ::factorioGameReducer,
//    FactorioGameState(
//      map = FactorioMap(),
//      players = emptyMap(),
//    )
//  )


data class FactorioGameState(
  val serverId: FactorioServerId,
  val map: FactorioMap = FactorioMap(serverId),
  val players: Map<PlayerIndex, PlayerState> = emptyMap(),
) {

  val playerIconsLayer: LayerGroup
    get() = map.playerIconsLayer

  operator fun plus(action: SiteAction.FactorioUpdate): FactorioGameState =
    when (action) {
      is SiteAction.FactorioUpdate.Player -> handlePlayerUpdate(this, action)
    }
}
