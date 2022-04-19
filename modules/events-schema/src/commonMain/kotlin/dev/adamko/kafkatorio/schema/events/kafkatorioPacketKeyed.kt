package dev.adamko.kafkatorio.schema.events

import dev.adamko.kafkatorio.schema.common.Colour
import dev.adamko.kafkatorio.schema.common.EntityIdentifiers
import dev.adamko.kafkatorio.schema.common.EntityIdentifiersData
import dev.adamko.kafkatorio.schema.common.ForceIndex
import dev.adamko.kafkatorio.schema.common.MapChunkPosition
import dev.adamko.kafkatorio.schema.common.MapEntityPosition
import dev.adamko.kafkatorio.schema.common.MapTileDictionary
import dev.adamko.kafkatorio.schema.common.PlayerIndex
import dev.adamko.kafkatorio.schema.common.PrototypeName
import dev.adamko.kafkatorio.schema.common.SurfaceIndex
import dev.adamko.kafkatorio.schema.common.Tick
import dev.adamko.kafkatorio.schema.common.UnitNumber
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.Serializable


sealed interface KafkatorioKeyedPacketKey


/** Has a key, so it can be debounced */
sealed class KafkatorioKeyedPacketData : KafkatorioPacketData() {
  /** Count how many events this packet is aggregating data from */
  abstract val eventCounts: Map<String, UInt>?
}


/*  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  *** */


sealed interface EntityUpdateKey : EntityIdentifiers, KafkatorioKeyedPacketKey {
  /** A [unitNumber] is required for caching */
  override val unitNumber: UnitNumber
  override val name: String
  override val protoType: String
}


@Serializable
data class EntityUpdate(
  override val unitNumber: UnitNumber,
  override val name: String,
  override val protoType: String,

  override val eventCounts: Map<String, UInt>? = null,

  val chunkPosition: MapChunkPosition? = null,
  val graphicsVariation: UByte? = null,
  val health: Float? = null,
  val isActive: Boolean? = null,
  val isRotatable: Boolean? = null,
  val lastUser: UInt? = null,
  val localisedDescription: String? = null,
  val localisedName: String? = null,
  val prototype: PrototypeName? = null,
) : KafkatorioKeyedPacketData(), EntityUpdateKey {
  @EncodeDefault
  override val updateType: KafkatorioPacketDataType = KafkatorioPacketDataType.ENTITY
}


/*  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  *** */


sealed interface MapChunkUpdateKey : KafkatorioKeyedPacketKey {
  val chunkPosition: MapChunkPosition
  val surfaceIndex: SurfaceIndex
}


@Serializable
data class MapChunkUpdate(
  override val chunkPosition: MapChunkPosition,
  override val surfaceIndex: SurfaceIndex,

  override val eventCounts: Map<String, UInt>? = null,

  val player: PlayerIndex? = null,
  val robot: EntityIdentifiersData? = null,
  val force: ForceIndex? = null,
  /** updated tiles - might be partial and not all tiles in the chunk */
  val tileDictionary: MapTileDictionary? = null,
  val isDeleted: Boolean? = null,
) : KafkatorioKeyedPacketData(), MapChunkUpdateKey {
  @EncodeDefault
  override val updateType: KafkatorioPacketDataType = KafkatorioPacketDataType.MAP_CHUNK
}


/*  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  *** */


sealed interface PlayerUpdateKey : KafkatorioKeyedPacketKey {
  val index: PlayerIndex
}


@Serializable
data class PlayerUpdate(
  override val index: PlayerIndex,

  override val eventCounts: Map<String, UInt>? = null,

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
) : KafkatorioKeyedPacketData(), PlayerUpdateKey {
  @EncodeDefault
  override val updateType: KafkatorioPacketDataType = KafkatorioPacketDataType.PLAYER
}


/*  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  *** */
