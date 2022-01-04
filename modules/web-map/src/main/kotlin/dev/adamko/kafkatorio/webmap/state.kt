package dev.adamko.kafkatorio.webmap

import dev.adamko.kafkatorio.events.schema.FactorioEvent
import dev.adamko.kafkatorio.events.schema.PlayerData
import externals.leaflet.geo.LatLng
import externals.leaflet.layer.overlay.Tooltip
import externals.leaflet.layer.vector.CircleMarker
import io.kvision.maps.Maps
import io.kvision.maps.Maps.Companion.L
import io.kvision.redux.ReduxStore
import io.kvision.redux.createReduxStore
import io.kvision.utils.obj
import redux.RAction

fun createFactorioReduxStore(): ReduxStore<FactorioGameState, FactorioUpdate> =
  createReduxStore(
    ::factorioGameReducer,
    FactorioGameState(
      kvMaps = createFactorioMap(),
      players = emptyMap()
    )
  )

data class FactorioGameState(
  val kvMaps: Maps,
  val players: Map<String, PlayerState>,
)


sealed class FactorioUpdate : RAction {
  data class PlayerUpdate(val event: FactorioEvent<*>, val playerData: PlayerData) :
    FactorioUpdate()
}

fun factorioGameReducer(state: FactorioGameState, update: FactorioUpdate): FactorioGameState =
  when (update) {
    is FactorioUpdate.PlayerUpdate -> {

      val playerState = state.players.getOrElse(update.playerData.name) {
        val newState = PlayerState(update.playerData)
        state.kvMaps.leafletMap {
          addLayer(newState.mapMarker)
          newState.mapMarker.openPopup()
//          setView(update.playerData.latLng())
        }
        newState
      }

      playerState.update(update.playerData)

      val updatedPlayersState = state.players + (update.playerData.name to playerState)
      state.copy(players = updatedPlayersState)
    }
  }


data class PlayerState(
  private var playerData: PlayerData,
  val mapMarker: CircleMarker = createMapMarker(playerData)
) {

  fun update(playerData: PlayerData) {
    this.playerData = playerData
    mapMarker.setLatLng(playerData.latLng())
    println("updated player ${playerData.name}")
  }

  companion object {

    fun createMapMarker(playerData: PlayerData): CircleMarker {

      val playerLatLng = playerData.latLng()
      val playerMapTooltip = "${playerData.name} [$playerLatLng]"

      val circle = L.circleMarker(playerLatLng) {
        color = playerData.colour.toHexString(false)
        fillColor = color
        fillOpacity = 1f
        radius = 10
        className = playerData.objectName
//        interactive = false
      }

      circle.bindTooltip(playerMapTooltip, obj<Tooltip.TooltipOptions> {
//        interactive = false
      })

      return circle
    }
  }

}
