package dev.adamko.kafkatorio.events.schema

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive


@Serializable(with = FactorioEventUpdate.Companion.JsonSerializer::class)
sealed class FactorioEventUpdate {

  @EncodeDefault
  abstract val updateType: FactorioEventUpdateType

  @Serializable
  enum class FactorioEventUpdateType {
    PLAYER,
    MAP_CHUNK,
    ENTITY,
    ;

    companion object {
      val values: List<FactorioEventUpdateType> = values().toList()
    }
  }

  companion object {

    object JsonSerializer : JsonContentPolymorphicSerializer<FactorioEventUpdate>(
      FactorioEventUpdate::class
    ) {
      private val key = FactorioEventUpdate::updateType.name

      override fun selectDeserializer(element: JsonElement): DeserializationStrategy<out FactorioEventUpdate> {

        val type = element
          .jsonObject[key]
          ?.jsonPrimitive
          ?.contentOrNull
          ?.let { json ->
            FactorioEventUpdateType.values.firstOrNull { it.name == json }
          }

        requireNotNull(type) { "Unknown FactorioEventUpdate $key: $element" }

        return when (type) {
          FactorioEventUpdateType.PLAYER    -> PlayerUpdate.serializer()
          FactorioEventUpdateType.MAP_CHUNK -> TODO()
          FactorioEventUpdateType.ENTITY    -> EntityUpdate.serializer()
        }
      }
    }

  }
}


@Serializable
data class PlayerUpdate(
  val index: PlayerIndex,

  val characterUnitNumber: UnitNumber? = null,
  val chatColour: Colour? = null,
  val colour: Colour? = null,
  val name: String? = null,

  val disconnectReason: String? = null,
  val forceIndex: ForceIndex? = null,
  val isAdmin: Boolean? = null,
  val isConnected: Boolean? = null,
  val isShowOnMap: Boolean? = null,
  val isSpectator: Boolean? = null,
  val lastOnline: Tick? = null,
  val onlineTime: Tick? = null,
  val position: MapEntityPosition? = null,
  val surfaceIndex: SurfaceIndex? = null,
  val tag: String? = null,
) : FactorioEventUpdate() {
  @EncodeDefault
  override val updateType: FactorioEventUpdateType = FactorioEventUpdateType.PLAYER
}


@Serializable
data class EntityUpdate(
  /**
   * A [unitNumber] is required for caching, else the Entity Update should be emitted without
   * debouncing/throttling
   */
  val unitNumber: UnitNumber,

  val name: String? = null,
  val type: String? = null,
  val chunkPosition: MapChunkPosition? = null,
  val graphicsVariation: UShort? = null,
  val health: Float? = null,
  val isActive: Boolean? = null,
  val isRotatable: Boolean? = null,
  val lastUser: UInt? = null,
  val localisedDescription: String? = null,
  val localisedName: String? = null,
  val prototype: PrototypeName? = null,
) : FactorioEventUpdate() {
  @EncodeDefault
  override val updateType: FactorioEventUpdateType = FactorioEventUpdateType.ENTITY
}


@Serializable
data class MapChunkUpdate(
  val chunkPosition: MapChunkPosition,
  val surfaceIndex: SurfaceIndex,

  /** Might be a partial list of updated tiles */
  val tiles: MapTiles? = null,
  val isDeleted: Boolean? = null,
) : FactorioEventUpdate() {
  @EncodeDefault
  override val updateType: FactorioEventUpdateType = FactorioEventUpdateType.MAP_CHUNK
}
