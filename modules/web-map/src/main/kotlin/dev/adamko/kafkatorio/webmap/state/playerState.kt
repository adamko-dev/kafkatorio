package dev.adamko.kafkatorio.webmap.state

import dev.adamko.kafkatorio.schema.common.PlayerIndex
import dev.adamko.kafkatorio.schema.common.Tick
import dev.adamko.kafkatorio.schema.packets.PlayerUpdate
import dev.adamko.kafkatorio.webmap.SiteAction
import io.kvision.maps.Maps
import io.kvision.maps.externals.leaflet.layer.vector.CircleMarker
import kotlinx.js.jso


fun handlePlayerUpdate(
  gameState: FactorioGameState,
  update: SiteAction.FactorioUpdate.Player,
): FactorioGameState {

  val previous = gameState.players[update.data.key.index]
    ?: PlayerState(update.data.key.index, update.tick)

  val updated = previous.update(update.tick, update.data)

  if (previous.mapMarker != updated.mapMarker) {
    if (previous.mapMarker != null) {
      println("removing player icon ${previous.index}")
      gameState.playerIconsLayer.removeLayer(previous.mapMarker)
    }
    if (updated.mapMarker != null) {
      println("adding player icon ${updated.index}")
      gameState.playerIconsLayer.addLayer(updated.mapMarker)
    }
  }

  val updatedPlayersState = gameState.players + (previous.index to updated)
  return gameState.copy(players = updatedPlayersState)
}


data class PlayerState(
  val index: PlayerIndex,
  val lastTick: Tick,
  val playerDetails: PlayerProperties = PlayerProperties.Partial(),
  val mapMarker: CircleMarker? = null,
) {

  fun update(tick: Tick, update: PlayerUpdate): PlayerState {

    if (lastTick.value > tick.value) {
      println("discarding old PlayerUpdate, ${lastTick.value} > ${tick.value}")
      return this
    }

    val newState = this.copy(
      lastTick = tick,
      playerDetails = playerDetails.update(update),
      mapMarker = updateMapMarker()
    )
    println("updated PlayerState index:${newState.index}, name:${newState.playerDetails.name}")
    return newState
  }


  private fun updateMapMarker(): CircleMarker? {
    return when (playerDetails) {

      is PlayerProperties.Partial  -> {
        println("removing player $index map marker")
        null
      }

      is PlayerProperties.Complete -> {

        val mapMarker = mapMarker ?: createMapMarker(playerDetails)

        mapMarker.apply {
          options.color = playerDetails.hexColour
          options.fillColor = playerDetails.hexColour

          val lat = playerDetails.latLng.lat.toInt()
          val lng = playerDetails.latLng.lng.toInt()

          setTooltipContent("${playerDetails.name} [$lat, $lng]")
          setLatLng(playerDetails.latLng)
        }
      }
    }
  }


  companion object {
    private fun createMapMarker(playerDetails: PlayerProperties.Complete): CircleMarker =
      Maps.L.circleMarker(playerDetails.latLng) {
        fillOpacity = 1f
        radius = 10
        className = "player-marker"
//        interactive = false
      }.also { circle ->
        circle.bindTooltip("", jso {
//        interactive = false
        })
      }
  }
}
