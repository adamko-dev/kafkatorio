package dev.adamko.kafkatorio.webmap.state

import dev.adamko.kafkatorio.schema.common.PlayerIndex
import dev.adamko.kafkatorio.schema.common.Tick
import dev.adamko.kafkatorio.schema.packets.PlayerUpdate
import dev.adamko.kafkatorio.webmap.FactorioMap
import io.kvision.maps.externals.leaflet.layer.LayerGroup
import io.kvision.redux.ReduxStore
import io.kvision.redux.createReduxStore
import redux.RAction


fun createFactorioReduxStore(): ReduxStore<FactorioGameState, FactorioUpdate> =
  createReduxStore(
    ::factorioGameReducer,
    FactorioGameState(
      map = FactorioMap(),
      players = emptyMap(),
    )
  )


data class FactorioGameState(
  val map: FactorioMap,
  val players: Map<PlayerIndex, PlayerState>,
) {

  val playerIconsLayer: LayerGroup
    get() = map.playerIconsLayer
}


sealed class FactorioUpdate : RAction {
  data class Player(val tick: Tick, val data: PlayerUpdate) : FactorioUpdate()
}


fun factorioGameReducer(gameState: FactorioGameState, update: FactorioUpdate): FactorioGameState =
  when (update) {
    is FactorioUpdate.Player -> handlePlayerUpdate(gameState, update)
  }
