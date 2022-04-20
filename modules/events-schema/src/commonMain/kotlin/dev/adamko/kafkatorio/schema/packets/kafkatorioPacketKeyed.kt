package dev.adamko.kafkatorio.schema.packets

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
import kotlinx.serialization.Serializable


/** Has a key, so it can be debounced */
@Serializable
sealed class KafkatorioKeyedPacketData : KafkatorioPacketData() {
  /** Count how many events this packet is aggregating data from */
  abstract val key: KafkatorioKeyedPacketKey2
  abstract val eventCounts: Map<String, UInt>?
}

@Serializable
sealed class KafkatorioKeyedPacketKey2


/*  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  *** */


@Serializable
data class EntityUpdateKey(
  /** While normally optional, here a [unitNumber] is required for caching */
  override val unitNumber: UnitNumber,
  override val name: String,
  override val protoType: String,
) : EntityIdentifiers, KafkatorioKeyedPacketKey2()


@Serializable
data class EntityUpdate(
  override val key: EntityUpdateKey,

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
) : KafkatorioKeyedPacketData()


/*  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  *** */


@Serializable
data class MapChunkUpdateKey(
  val chunkPosition: MapChunkPosition,
  val surfaceIndex: SurfaceIndex,
) : KafkatorioKeyedPacketKey2()


@Serializable
data class MapChunkUpdate(
  override val key: MapChunkUpdateKey,

  override val eventCounts: Map<String, UInt>? = null,

  val player: PlayerIndex? = null,
  val robot: EntityIdentifiersData? = null,
  val force: ForceIndex? = null,
  /** updated tiles - might be partial and not all tiles in the chunk */
  val tileDictionary: MapTileDictionary? = null,
  val isDeleted: Boolean? = null,
) : KafkatorioKeyedPacketData()


/*  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  *** */

@Serializable
data class PlayerUpdateKey(
  val index: PlayerIndex,
) : KafkatorioKeyedPacketKey2()


@Serializable
data class PlayerUpdate(
  override val key: PlayerUpdateKey,

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
) : KafkatorioKeyedPacketData()


/*  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  *** */
