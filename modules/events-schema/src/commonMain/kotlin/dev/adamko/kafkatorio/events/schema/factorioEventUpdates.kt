package dev.adamko.kafkatorio.events.schema

import dev.adamko.kafkatorio.events.schema.FactorioUpdateData.FactorioUpdateDataType
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive


@Serializable
sealed class KafkatorioPacket {
  /** Schema versioning */
  abstract val modVersion: String
  abstract val tick: Tick
  abstract val update: FactorioUpdateData
}


/* *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  * */


sealed interface FactorioUpdateDataKey {
  val updateType: FactorioUpdateDataType
}


@Serializable(with = FactorioUpdateData.JsonSerializer::class)
sealed class FactorioUpdateData : FactorioUpdateDataKey {

  /** Discriminator for [FactorioUpdateData] */
  @EncodeDefault
  abstract override val updateType: FactorioUpdateDataType

  /** the initial Factorio event (`defines.events`) trigger */
  abstract val eventCounts: Map<String, UInt>?

  @Serializable
  enum class FactorioUpdateDataType {
    PLAYER,
    MAP_CHUNK,
    ENTITY,
    CONSOLE_CHAT,
    CONSOLE_COMMAND,
    SURFACE,
    CONFIG,
    PROTOTYPES,
    ;

    companion object {
      val values: List<FactorioUpdateDataType> = values().toList()
    }
  }

  object JsonSerializer : JsonContentPolymorphicSerializer<FactorioUpdateData>(
    FactorioUpdateData::class
  ) {
    private val key = FactorioUpdateData::updateType.name

    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<out FactorioUpdateData> {

      val type = element
        .jsonObject[key]
        ?.jsonPrimitive
        ?.contentOrNull
        ?.let { json ->
          FactorioUpdateDataType.values.firstOrNull { it.name == json }
        }

      requireNotNull(type) { "Unknown FactorioEventUpdate $key: $element" }

      return when (type) {
        FactorioUpdateDataType.PLAYER          -> PlayerUpdate.serializer()
        FactorioUpdateDataType.MAP_CHUNK       -> MapChunkUpdate.serializer()
        FactorioUpdateDataType.ENTITY          -> EntityUpdate.serializer()
        FactorioUpdateDataType.CONSOLE_CHAT    -> ConsoleChatUpdate.serializer()
        FactorioUpdateDataType.CONSOLE_COMMAND -> ConsoleCommandUpdate.serializer()
        FactorioUpdateDataType.SURFACE         -> SurfaceUpdate.serializer()
        FactorioUpdateDataType.CONFIG          -> ConfigurationUpdate.serializer()
        FactorioUpdateDataType.PROTOTYPES      -> PrototypesUpdate.serializer()
      }
    }
  }
}


/* *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  * */


sealed interface PlayerUpdateKey : FactorioUpdateDataKey {
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
) : FactorioUpdateData(), PlayerUpdateKey {
  @EncodeDefault
  override val updateType: FactorioUpdateDataType = FactorioUpdateDataType.PLAYER
}


/* *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  * */


sealed interface EntityUpdateKey : EntityIdentifiers, FactorioUpdateDataKey {
  /** A [unitNumber] is required for caching */
  override val unitNumber: UnitNumber
  override val name: String
  override val type: String
}


@Serializable
data class EntityUpdate(
  override val unitNumber: UnitNumber,
  override val name: String,
  override val type: String,

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
) : FactorioUpdateData(), EntityUpdateKey {
  @EncodeDefault
  override val updateType: FactorioUpdateDataType = FactorioUpdateDataType.ENTITY
}


/* *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  * */


sealed interface MapChunkUpdateKey : FactorioUpdateDataKey {
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
) : FactorioUpdateData(), MapChunkUpdateKey {
  @EncodeDefault
  override val updateType: FactorioUpdateDataType = FactorioUpdateDataType.MAP_CHUNK
}


/* *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  * */


@Serializable
data class ConsoleChatUpdate(
  override val eventCounts: Map<String, UInt>? = null,

  val authorPlayerIndex: PlayerIndex?,
  val content: String,
) : FactorioUpdateData() {
  @EncodeDefault
  override val updateType: FactorioUpdateDataType = FactorioUpdateDataType.CONSOLE_CHAT

}

@Serializable
data class ConsoleCommandUpdate(
  override val eventCounts: Map<String, UInt>? = null,

  val authorPlayerIndex: PlayerIndex?,
  val command: String,
  val parameters: String,
) : FactorioUpdateData() {
  @EncodeDefault
  override val updateType: FactorioUpdateDataType = FactorioUpdateDataType.CONSOLE_COMMAND
}


/* *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  * */


sealed interface SurfaceUpdateKey : FactorioUpdateDataKey {
  val index: SurfaceIndex
}

@Serializable
data class SurfaceUpdate(
  override val index: SurfaceIndex,

  override val eventCounts: Map<String, UInt>? = null,

  val daytime: Double,
  val name: String,
) : FactorioUpdateData(), SurfaceUpdateKey {
  @EncodeDefault
  override val updateType: FactorioUpdateDataType = FactorioUpdateDataType.SURFACE
}


/* *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  * */


@Serializable
data class ConfigurationUpdate(
  override val eventCounts: Map<String, UInt>? = null,

  val factorioData: GameData,
  val allMods: List<ModData>,
) : FactorioUpdateData() {
  @EncodeDefault
  override val updateType: FactorioUpdateDataType = FactorioUpdateDataType.CONFIG

  @Serializable
  data class GameData(
    val oldVersion: String? = null,
    val newVersion: String? = null,
  )

  @Serializable
  data class ModData(
    val modName: String,
    val currentVersion: String? = null,
    val previousVersion: String? = null,
  )
}


/* *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  * */


@Serializable
data class PrototypesUpdate(
  override val eventCounts: Map<String, UInt>? = null,

  val prototypes: List<FactorioPrototype>,
) : FactorioUpdateData() {
  @EncodeDefault
  override val updateType: FactorioUpdateDataType = FactorioUpdateDataType.PROTOTYPES
}


/* *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  * */
