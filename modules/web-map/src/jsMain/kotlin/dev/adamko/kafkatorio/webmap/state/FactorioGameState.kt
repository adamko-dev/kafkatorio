package dev.adamko.kafkatorio.webmap.state

import dev.adamko.kafkatorio.schema.common.FactorioServerId
import dev.adamko.kafkatorio.schema.common.PlayerIndex
import dev.adamko.kafkatorio.webmap.FactorioMap
import io.kvision.maps.externals.leaflet.layer.LayerGroup


data class FactorioGameState(
  val serverId: FactorioServerId,
  val map: FactorioMap = FactorioMap(serverId),
  val players: Map<PlayerIndex, PlayerState> = emptyMap(),
) {

  val playerIconsLayer: LayerGroup
    get() = map.playerIconsLayer

  operator fun plus(action: SiteAction.FactorioUpdate): FactorioGameState =
    when (action) {
      is SiteAction.FactorioUpdate.Player -> handlePlayerUpdate(action)
    }
}


private fun FactorioGameState.handlePlayerUpdate(
  update: SiteAction.FactorioUpdate.Player,
): FactorioGameState {

  val previous = players[update.data.key.index]
    ?: PlayerState(update.data.key.index, update.tick)

  val updated = previous.update(update.tick, update.data)

  if (previous.mapMarker != updated.mapMarker) {
    if (previous.mapMarker != null) {
      println("removing player icon ${previous.index}")
      playerIconsLayer.removeLayer(previous.mapMarker)
    }
    if (updated.mapMarker != null) {
      println("adding player icon ${updated.index}")
      playerIconsLayer.addLayer(updated.mapMarker)
    }
  }

  val updatedPlayersState = players + (previous.index to updated)
  return copy(players = updatedPlayersState)
}
