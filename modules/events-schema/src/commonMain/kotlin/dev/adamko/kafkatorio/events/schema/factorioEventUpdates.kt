package dev.adamko.kafkatorio.events.schema

import dev.adamko.kafkatorio.events.schema.FactorioEventUpdate.FactorioEventUpdateType
import kotlinx.serialization.Contextual
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


//sealed interface FactorioEventUpdateData {
//  val updateType: FactorioEventUpdateType
//  val event: String?
//}


@Serializable(with = FactorioEventUpdate.Companion.JsonSerializer::class)
sealed class FactorioEventUpdate : FactorioEventUpdateKey {

  @EncodeDefault
  abstract override val updateType: FactorioEventUpdateType
  @Contextual
  abstract val events: List<String>?

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


//sealed interface PlayerUpdateData : FactorioEventUpdateData {
//  val characterUnitNumber: UnitNumber?
//  val chatColour: Colour?
//  val colour: Colour?
//  val name: String?
//
//  val afkTime: Tick?
//  val ticksToRespawn: Tick?
//  val forceIndex: ForceIndex?
//  val isAdmin: Boolean?
//  val isConnected: Boolean?
//  val isShowOnMap: Boolean?
//  val isSpectator: Boolean?
//  val lastOnline: Tick?
//  val onlineTime: Tick?
//  val position: MapEntityPosition?
//  val surfaceIndex: SurfaceIndex?
//  val tag: String?
//  val diedCause: EntityIdentifiers?
//
//  val bannedReason: String?
//  val kickedReason: String?
//  val disconnectReason: String?
//  /** `true` when a player is removed (deleted) from the game */
//  val isRemoved: Boolean?
//}


@Serializable
data class PlayerUpdate(
  override val index: PlayerIndex,

  @Contextual
  override val events: List<String>? = null,

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
) : FactorioEventUpdate(), PlayerUpdateKey {
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

//sealed interface EntityUpdateData : FactorioEventUpdateData {
//  val chunkPosition: MapChunkPosition?
//  val graphicsVariation: UShort?
//  val health: Float?
//  val isActive: Boolean?
//  val isRotatable: Boolean?
//  val lastUser: UInt?
//  val localisedDescription: String?
//  val localisedName: String?
//  val prototype: PrototypeName?
//}

@Serializable
data class EntityUpdate(
  override val unitNumber: UnitNumber,
  override val name: String,
  override val type: String,

  @Contextual
  override val events: List<String>? = null,

  val chunkPosition: MapChunkPosition? = null,
  val graphicsVariation: UShort? = null,
  val health: Float? = null,
  val isActive: Boolean? = null,
  val isRotatable: Boolean? = null,
  val lastUser: UInt? = null,
  val localisedDescription: String? = null,
  val localisedName: String? = null,
  val prototype: PrototypeName? = null,
) : FactorioEventUpdate(), EntityIdentifiers, EntityUpdateKey {
  @EncodeDefault
  override val updateType: FactorioEventUpdateType = FactorioEventUpdateType.ENTITY
}


/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  */


sealed interface MapChunkUpdateKey : FactorioEventUpdateKey {
  val chunkPosition: MapChunkPosition
  val surfaceIndex: SurfaceIndex
}


//sealed interface MapChunkUpdateData : FactorioEventUpdateData {
//  val player: PlayerIndex?
//  val robot: EntityIdentifiers?
//  val force: ForceIndex?
//  val tiles: List<MapTile>?
//  val isDeleted: Boolean?
//}


@Serializable
data class MapChunkUpdate(
  override val chunkPosition: MapChunkPosition,
  override val surfaceIndex: SurfaceIndex,

  @Contextual
  override val events: List<String>? = null,

  val player: PlayerIndex? = null,
  val robot: EntityIdentifiers? = null,
  val force: ForceIndex? = null,
  /** updated tiles - might be partial */
  // note: this must be a List, not a Map<Location, ProtoName>
  // because JSON can't have non-string keys.
  @Contextual
  val tiles: List<MapTile>? = null,
  val isDeleted: Boolean? = null,
) : FactorioEventUpdate(), MapChunkUpdateKey {
  @EncodeDefault
  override val updateType: FactorioEventUpdateType = FactorioEventUpdateType.MAP_CHUNK
}


/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  */
