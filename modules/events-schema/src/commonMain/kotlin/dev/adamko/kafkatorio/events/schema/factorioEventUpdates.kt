package dev.adamko.kafkatorio.events.schema

import dev.adamko.kafkatorio.events.schema.FactorioEventUpdate.FactorioEventUpdateType
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


/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  */


sealed interface FactorioEventUpdateKey {
  val updateType: FactorioEventUpdateType
}


sealed interface FactorioEventUpdateData {
  val updateType: FactorioEventUpdateType
}


@Serializable(with = FactorioEventUpdate.Companion.JsonSerializer::class)
sealed class FactorioEventUpdate : FactorioEventUpdateKey, FactorioEventUpdateData {

  @EncodeDefault
  abstract override val updateType: FactorioEventUpdateType

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
          FactorioEventUpdateType.MAP_CHUNK -> MapChunkUpdate.serializer()
          FactorioEventUpdateType.ENTITY    -> EntityUpdate.serializer()
        }
      }
    }

  }
}


/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  */


sealed interface PlayerUpdateKey : FactorioEventUpdateKey {
  val index: PlayerIndex
}


sealed interface PlayerUpdateData : FactorioEventUpdateData {
  val characterUnitNumber: UnitNumber?
  val chatColour: Colour?
  val colour: Colour?
  val name: String?

  val afkTime: Tick?
  val ticksToRespawn: Tick?
  val forceIndex: ForceIndex?
  val isAdmin: Boolean?
  val isConnected: Boolean?
  val isShowOnMap: Boolean?
  val isSpectator: Boolean?
  val lastOnline: Tick?
  val onlineTime: Tick?
  val position: MapEntityPosition?
  val surfaceIndex: SurfaceIndex?
  val tag: String?
  val diedCause: EntityIdentifiers?

  val bannedReason: String?
  val kickedReason: String?
  val disconnectReason: String?
  /** `true` when a player is removed (deleted) from the game */
  val isRemoved: Boolean?
}


@Serializable
data class PlayerUpdate(
  override val index: PlayerIndex,

  override val characterUnitNumber: UnitNumber? = null,
  override val chatColour: Colour? = null,
  override val colour: Colour? = null,
  override val name: String? = null,

  override val afkTime: Tick? = null,
  override val ticksToRespawn: Tick? = null,
  override val forceIndex: ForceIndex? = null,
  override val isAdmin: Boolean? = null,
  override val isConnected: Boolean? = null,
  override val isShowOnMap: Boolean? = null,
  override val isSpectator: Boolean? = null,
  override val lastOnline: Tick? = null,
  override val onlineTime: Tick? = null,
  override val position: MapEntityPosition? = null,
  override val surfaceIndex: SurfaceIndex? = null,
  override val tag: String? = null,
  override val diedCause: EntityIdentifiers? = null,

  override val bannedReason: String? = null,
  override val kickedReason: String? = null,
  override val disconnectReason: String? = null,
  /** `true` when a player is removed (deleted) from the game */
  override val isRemoved: Boolean? = null,
) : FactorioEventUpdate(), PlayerUpdateKey, PlayerUpdateData {
  @EncodeDefault
  override val updateType: FactorioEventUpdateType = FactorioEventUpdateType.PLAYER
}


/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  */


sealed interface EntityUpdateKey : EntityIdentifiers, FactorioEventUpdateKey {
  /** A [unitNumber] is required for caching */
  override val unitNumber: UnitNumber
  override val name: String
  override val type: String
}

sealed interface EntityUpdateData : FactorioEventUpdateData {
  val chunkPosition: MapChunkPosition?
  val graphicsVariation: UShort?
  val health: Float?
  val isActive: Boolean?
  val isRotatable: Boolean?
  val lastUser: UInt?
  val localisedDescription: String?
  val localisedName: String?
  val prototype: PrototypeName?
}

@Serializable
data class EntityUpdate(
  override val unitNumber: UnitNumber,
  override val name: String,
  override val type: String,

  override val chunkPosition: MapChunkPosition? = null,
  override val graphicsVariation: UShort? = null,
  override val health: Float? = null,
  override val isActive: Boolean? = null,
  override val isRotatable: Boolean? = null,
  override val lastUser: UInt? = null,
  override val localisedDescription: String? = null,
  override val localisedName: String? = null,
  override val prototype: PrototypeName? = null,
) : FactorioEventUpdate(), EntityIdentifiers, EntityUpdateKey, EntityUpdateData {
  @EncodeDefault
  override val updateType: FactorioEventUpdateType = FactorioEventUpdateType.ENTITY
}


/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  */


sealed interface MapChunkUpdateKey : FactorioEventUpdateKey {
  val chunkPosition: MapChunkPosition
  val surfaceIndex: SurfaceIndex
}


sealed interface MapChunkUpdateData : FactorioEventUpdateData {
  val player: PlayerIndex?
  val robot: EntityIdentifiers?
  val force: ForceIndex?
  /** updated tiles - might be partial */
  val tiles: List<MapTile>?
  val isDeleted: Boolean?
}


@Serializable
data class MapChunkUpdate(
  override val chunkPosition: MapChunkPosition,
  override val surfaceIndex: SurfaceIndex,

  override val player: PlayerIndex? = null,
  override val robot: EntityIdentifiers? = null,
  override val force: ForceIndex? = null,
  // note: this must be a List, not a Map<Location, ProtoName>
  // because JSON can't have non-string keys.
  override val tiles: List<MapTile>? = null,
  override val isDeleted: Boolean? = null,
) : FactorioEventUpdate(), MapChunkUpdateKey, MapChunkUpdateData {
  @EncodeDefault
  override val updateType: FactorioEventUpdateType = FactorioEventUpdateType.MAP_CHUNK
}


/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  */
