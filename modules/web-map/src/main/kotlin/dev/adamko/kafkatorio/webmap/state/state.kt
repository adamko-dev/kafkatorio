package dev.adamko.kafkatorio.webmap.state

import dev.adamko.kafkatorio.schema.common.Colour
import dev.adamko.kafkatorio.schema.common.MapEntityPosition
import dev.adamko.kafkatorio.schema.common.PlayerIndex
import dev.adamko.kafkatorio.schema.common.SurfaceIndex
import dev.adamko.kafkatorio.schema.common.Tick
import dev.adamko.kafkatorio.schema.common.toHexString
import dev.adamko.kafkatorio.schema.packets.PlayerUpdate
import dev.adamko.kafkatorio.webmap.FactorioMap
import dev.adamko.kafkatorio.webmap.latLng
import io.kvision.maps.Maps.Companion.L
import io.kvision.maps.externals.leaflet.geo.LatLng
import io.kvision.maps.externals.leaflet.layer.LayerGroup
import io.kvision.maps.externals.leaflet.layer.overlay.Tooltip
import io.kvision.maps.externals.leaflet.layer.vector.CircleMarker
import io.kvision.redux.ReduxStore
import io.kvision.redux.createReduxStore
import io.kvision.utils.obj
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

//  init {
//    kvMaps.leafletMap {
//      playerIconsLayer.addTo(this)
//    }
//  }
}


sealed class FactorioUpdate : RAction {
  data class Player(val tick: Tick, val data: PlayerUpdate) : FactorioUpdate()
}


fun factorioGameReducer(gameState: FactorioGameState, update: FactorioUpdate): FactorioGameState =
  when (update) {
    is FactorioUpdate.Player -> handlePlayerUpdate(gameState, update)
  }


private fun handlePlayerUpdate(
  gameState: FactorioGameState,
  update: FactorioUpdate.Player,
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
      L.circleMarker(playerDetails.latLng) {
        fillOpacity = 1f
        radius = 10
        className = "player-marker"
//        interactive = false
      }.also { circle ->
        circle.bindTooltip("", obj<Tooltip.TooltipOptions> {
//        interactive = false
        })
      }
  }
}


sealed interface PlayerProperties {

  val position: MapEntityPosition?
  val surfaceIndex: SurfaceIndex?
  val colour: Colour?
  val name: String?
  val latLng: LatLng?
  val isConnected: Boolean
  val isShowOnMap: Boolean


  data class Partial(
    override val position: MapEntityPosition? = null,
    override val surfaceIndex: SurfaceIndex? = null,
    override val colour: Colour? = null,
    override val name: String? = null,
    override val isConnected: Boolean = false,
    override val isShowOnMap: Boolean = false,
  ) : PlayerProperties {
    override val latLng: LatLng? = null
  }


  data class Complete(
    override val position: MapEntityPosition,
    override val surfaceIndex: SurfaceIndex,
    override val colour: Colour,
    override val name: String,
    override val isConnected: Boolean = false,
    override val isShowOnMap: Boolean = false,
  ) : PlayerProperties {
    override val latLng: LatLng = position.latLng()
    val hexColour: String = colour.toHexString(false)
  }


  fun update(update: PlayerUpdate): PlayerProperties {

    val position = update.position ?: this.position
    val surfaceIndex = update.surfaceIndex ?: this.surfaceIndex
    val colour = update.colour ?: this.colour
    val name = update.name ?: this.name
    val isConnected = update.isConnected ?: false
    val isShowOnMap = update.isShowOnMap ?: false

    return when {
      position != null
          && surfaceIndex != null
          && colour != null
          && name != null ->
        Complete(
          position = position,
          surfaceIndex = surfaceIndex,
          colour = colour,
          name = name,
          isConnected = isConnected,
          isShowOnMap = isShowOnMap,
        )

      else                ->
        Partial(
          position = position,
          surfaceIndex = surfaceIndex,
          colour = colour,
          name = name,
          isConnected = isConnected,
          isShowOnMap = isShowOnMap,
        )
    }
  }
}
