package dev.adamko.kafkatorio.schema.packets

import dev.adamko.kafkatorio.schema.common.Colour
import dev.adamko.kafkatorio.schema.common.EntityIdentifiers
import dev.adamko.kafkatorio.schema.common.EntityIdentifiersData
import dev.adamko.kafkatorio.schema.common.EventName
import dev.adamko.kafkatorio.schema.common.FactorioEntityData
import dev.adamko.kafkatorio.schema.common.ForceIndex
import dev.adamko.kafkatorio.schema.common.MapChunkPosition
import dev.adamko.kafkatorio.schema.common.MapEntityPosition
import dev.adamko.kafkatorio.schema.common.MapTileDictionary
import dev.adamko.kafkatorio.schema.common.PlayerIndex
import dev.adamko.kafkatorio.schema.common.PrototypeId
import dev.adamko.kafkatorio.schema.common.SurfaceIndex
import dev.adamko.kafkatorio.schema.common.Tick
import dev.adamko.kafkatorio.schema.common.UnitNumber
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/** Has a key, so it can be debounced */
@Serializable
@SerialName("kafkatorio.packet.keyed.KafkatorioKeyedPacketData")
sealed class KafkatorioKeyedPacketData : KafkatorioPacketData() {
  abstract val key: KafkatorioKeyedPacketKey
  /** A list of all events that have been aggregated into this packet. */
  // Maybe change to be Map<EventName, Tick>, where Tick is the most recent event?
  // A complete event timeline isn't necessary. The most recent tick of an event is good enough, and requires less space.
  abstract val events: Map<EventName, List<Tick>>?
}


@Serializable
@SerialName("kafkatorio.packet.keyed.KafkatorioKeyedPacketKey")
sealed class KafkatorioKeyedPacketKey


/*  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  *** */


@Serializable
@SerialName("kafkatorio.packet.keyed.EntityUpdateKey")
data class EntityUpdateKey(
  /** While normally optional, here a [unitNumber] is required for caching */
  override val unitNumber: UnitNumber,
  override val protoId: PrototypeId,
) : EntityIdentifiers, KafkatorioKeyedPacketKey()


@Serializable
@SerialName("kafkatorio.packet.keyed.EntityUpdate")
data class EntityUpdate(
  override val key: EntityUpdateKey,

  override val events: Map<EventName, List<Tick>>? = null,

  val chunkPosition: MapEntityPosition? = null,
  val graphicsVariation: UByte? = null,
  val health: Float? = null,
  val isActive: Boolean? = null,
  val isRotatable: Boolean? = null,
  val lastUser: UInt? = null,
  val localisedDescription: String? = null,
  val localisedName: String? = null,
) : KafkatorioKeyedPacketData()


/*  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  *** */


@Serializable
@SerialName("kafkatorio.packet.keyed.MapChunkTileUpdateKey")
data class MapChunkTileUpdateKey(
//  val index: PlayerIndex, // maybe add player/robot key, to allow metrics like 'player x mined y tiles'
  val chunkPosition: MapChunkPosition,
  val surfaceIndex: SurfaceIndex,
) : KafkatorioKeyedPacketKey()


@Serializable
@SerialName("kafkatorio.packet.keyed.MapChunkTileUpdate")
data class MapChunkTileUpdate(
  override val key: MapChunkTileUpdateKey,

  override val events: Map<EventName, List<Tick>>? = null,

  val player: PlayerIndex? = null,
  val robot: EntityIdentifiersData? = null,
  val force: ForceIndex? = null,
  /** updated tiles - might be partial and not all tiles in the chunk */
  val tileDictionary: MapTileDictionary? = null,
  val isDeleted: Boolean? = null,
) : KafkatorioKeyedPacketData()


/*  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  *** */


@Serializable
@SerialName("kafkatorio.packet.keyed.PlayerUpdateKey")
data class PlayerUpdateKey(
  val index: PlayerIndex,
) : KafkatorioKeyedPacketKey()


@Serializable
@SerialName("kafkatorio.packet.keyed.PlayerUpdate")
data class PlayerUpdate(
  override val key: PlayerUpdateKey,

  override val events: Map<EventName, List<Tick>>? = null,

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
  val diedCause: EntityIdentifiersData? = null,

  val bannedReason: String? = null,
  val kickedReason: String? = null,
  val disconnectReason: String? = null,
  /** `true` when a player is removed (deleted) from the game */
  val isRemoved: Boolean? = null,
) : KafkatorioKeyedPacketData()


/*  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  *** */


@Serializable
@SerialName("kafkatorio.packet.keyed.MapChunkEntityUpdateKey")
data class MapChunkEntityUpdateKey(
  val protoId: PrototypeId,
  val surfaceIndex: SurfaceIndex,
  val chunkPosition: MapChunkPosition,
) : KafkatorioKeyedPacketKey()


/**
 * @property[distinctEntities] Distinct entities. The key is arbitrary and is only used
 * for de-duplicating updates. It should only contain data that can be obtained from
 * [the value][FactorioEntityData].
 */
@Serializable
@SerialName("kafkatorio.packet.keyed.MapChunkEntityUpdate")
data class MapChunkEntityUpdate(
  override val key: MapChunkEntityUpdateKey,

  override val events: Map<EventName, List<Tick>>? = null,

  val distinctEntities: Map<String, FactorioEntityData>? = null,
) : KafkatorioKeyedPacketData()


/*  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  *** */
