package dev.adamko.kafkatorio.webmap.state


import dev.adamko.kafkatorio.schema.common.Colour
import dev.adamko.kafkatorio.schema.common.MapEntityPosition
import dev.adamko.kafkatorio.schema.common.SurfaceIndex
import dev.adamko.kafkatorio.schema.common.toHexString
import dev.adamko.kafkatorio.schema.packets.PlayerUpdate
import dev.adamko.kafkatorio.webmap.services.latLng
import io.kvision.maps.externals.leaflet.geo.LatLng

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
    val isConnected = update.isConnected
    val isShowOnMap = update.isShowOnMap

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
