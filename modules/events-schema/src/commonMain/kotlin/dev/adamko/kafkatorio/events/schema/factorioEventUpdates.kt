package dev.adamko.kafkatorio.events.schema

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive


@Serializable
class FactorioEventUpdatePacket(
  override val modVersion: String,
  /** game time */
  val tick: Tick,
  val update: FactorioEventUpdate,
) : KafkatorioPacket() {
  @EncodeDefault
  override val packetType: PacketType = PacketType.UPDATE
}


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

  val afkTime: Tick? = null,
  val ticksToRespawn: Tick? = null,
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
  val diedCause: EntityIdentifiers? = null,

  val bannedReason: String? = null,
  val kickedReason: String? = null,
  val disconnectReason: String? = null,
  /** `true` when a player is removed (deleted) from the game */
  val isRemoved: Boolean? = null,
) : FactorioEventUpdate() {
  @EncodeDefault
  override val updateType: FactorioEventUpdateType = FactorioEventUpdateType.PLAYER
}


@Serializable
data class EntityUpdate(
  /** A [unitNumber] is required for caching */
  override val unitNumber: UnitNumber,
  override val name: String,
  override val type: String,

  val chunkPosition: MapChunkPosition? = null,
  val graphicsVariation: UShort? = null,
  val health: Float? = null,
  val isActive: Boolean? = null,
  val isRotatable: Boolean? = null,
  val lastUser: UInt? = null,
  val localisedDescription: String? = null,
  val localisedName: String? = null,
  val prototype: PrototypeName? = null,
) : FactorioEventUpdate(), EntityIdentifiers {
  @EncodeDefault
  override val updateType: FactorioEventUpdateType = FactorioEventUpdateType.ENTITY
}


@Serializable
data class MapChunkUpdate(
  val chunkPosition: MapChunkPosition,
  val surfaceIndex: SurfaceIndex,

  val player: PlayerIndex? = null,
  val robot: EntityIdentifiers? = null,
  val force: ForceIndex? = null,

  /** Might be a partial list of updated tiles */
  val tiles: Map<MapTilePosition, PrototypeName>? = null,
  val isDeleted: Boolean? = null,
) : FactorioEventUpdate() {
  @EncodeDefault
  override val updateType: FactorioEventUpdateType = FactorioEventUpdateType.MAP_CHUNK
}
