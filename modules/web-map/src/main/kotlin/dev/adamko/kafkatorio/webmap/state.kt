package dev.adamko.kafkatorio.webmap

import dev.adamko.kafkatorio.schema.common.Colour
import dev.adamko.kafkatorio.schema.common.MapEntityPosition
import dev.adamko.kafkatorio.schema.common.PlayerIndex
import dev.adamko.kafkatorio.schema.common.SurfaceIndex
import dev.adamko.kafkatorio.schema.common.Tick
import dev.adamko.kafkatorio.schema.common.toHexString
import dev.adamko.kafkatorio.schema.packets.PlayerUpdate
import io.kvision.maps.Maps
import io.kvision.maps.Maps.Companion.L
import io.kvision.maps.externals.leaflet.geo.LatLng
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
      kvMaps = createFactorioMap(),
      players = emptyMap()
    )
  )


data class FactorioGameState(
  val kvMaps: Maps,
  val players: Map<PlayerIndex, PlayerState>,
)


sealed class FactorioUpdate : RAction {
  data class Player(val tick: Tick, val data: PlayerUpdate) : FactorioUpdate()
}


fun factorioGameReducer(state: FactorioGameState, update: FactorioUpdate): FactorioGameState =
  when (update) {
    is FactorioUpdate.Player -> {

      val playerState = state.players.getOrElse(update.data.key.index) {
        PlayerState(update.data.key.index, update.tick)
      }

      playerState.update(update.tick, update.data)

      state.kvMaps.leafletMap {

        val playerMarker = playerState.mapMarker

        if (playerMarker != null) {
          when (playerState.playerDetails) {
            is PlayerDetailsState.Known   -> if (!hasLayer(playerMarker)) addLayer(playerMarker)
            is PlayerDetailsState.Unknown -> if (hasLayer(playerMarker)) removeLayer(playerMarker)
          }
        }
      }

      val updatedPlayersState = state.players + (playerState.index to playerState)
      state.copy(players = updatedPlayersState)
    }
  }


class PlayerState(
  val index: PlayerIndex,
  private var lastTick: Tick,
) {
  var playerDetails: PlayerDetailsState = PlayerDetailsState.Unknown(index)
    private set

  var mapMarker: CircleMarker? = null
    private set

  fun update(tick: Tick, update: PlayerUpdate) {

    if (lastTick.value > tick.value) {
      println("discarding old PlayerUpdate, ${lastTick.value} > ${tick.value}")
      return
    }
    this.lastTick = tick
    this.playerDetails = updatePlayerDetails(update)

    updateMapPosition()

    println("updated PlayerState index:${playerDetails.index}, name:${playerDetails.name}")
  }

  private fun updatePlayerDetails(update: PlayerUpdate): PlayerDetailsState {

    val position = update.position ?: playerDetails.position
    val surfaceIndex = update.surfaceIndex ?: playerDetails.surfaceIndex
    val colour = update.colour ?: playerDetails.colour
    val name = update.name ?: playerDetails.name

    return when {
      position != null
          && surfaceIndex != null
          && colour != null
          && name != null ->
        PlayerDetailsState.Known(
          index = playerDetails.index,
          position = position,
          surfaceIndex = surfaceIndex,
          colour = colour,
          name = name,
        )

      else                ->
        PlayerDetailsState.Unknown(
          index = playerDetails.index,
          position = position,
          surfaceIndex = surfaceIndex,
          colour = colour,
          name = name,
        )
    }
  }


  private fun updateMapPosition() {

    when (val details = playerDetails) {
      is PlayerDetailsState.Unknown -> {
        println("removing player ${details.index} map marker")
        mapMarker?.remove()
      }
      is PlayerDetailsState.Known   -> updateMapMarker(details)
    }

  }

  private fun updateMapMarker(playerDetails: PlayerDetailsState.Known) {

    if (mapMarker == null) {
      mapMarker = L.circleMarker(playerDetails.latLng) {
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

    mapMarker?.apply {
      val playerColour = playerDetails.colour.toHexString(false)
      options.color = playerColour
      options.fillColor = playerColour

      val lat = playerDetails.latLng.lat.toInt()
      val lng = playerDetails.latLng.lng.toInt()

      setTooltipContent("${playerDetails.name} [$lat, $lng]")
      setLatLng(playerDetails.latLng)
    }

  }
}


sealed interface PlayerDetailsState {

  val index: PlayerIndex

  val position: MapEntityPosition?
  val surfaceIndex: SurfaceIndex?
  val colour: Colour?
  val name: String?
  val latLng: LatLng?

  data class Unknown(
    override val index: PlayerIndex,

    override val position: MapEntityPosition? = null,
    override val surfaceIndex: SurfaceIndex? = null,
    override val colour: Colour? = null,
    override val name: String? = null,
  ) : PlayerDetailsState {
    override val latLng: LatLng? = null
  }

  data class Known(
    override val index: PlayerIndex,

    override val position: MapEntityPosition,
    override val surfaceIndex: SurfaceIndex,
    override val colour: Colour,
    override val name: String,
  ) : PlayerDetailsState {
    override val latLng: LatLng = position.latLng()
  }

}
