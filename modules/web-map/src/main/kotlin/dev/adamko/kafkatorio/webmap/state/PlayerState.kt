package dev.adamko.kafkatorio.webmap.state

import dev.adamko.kafkatorio.schema.common.PlayerIndex
import dev.adamko.kafkatorio.schema.common.Tick
import dev.adamko.kafkatorio.schema.packets.PlayerUpdate
import io.kvision.maps.Maps
import io.kvision.maps.externals.leaflet.layer.vector.CircleMarker
import js.core.jso

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
