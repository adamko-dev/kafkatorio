package dev.adamko.kafkatorio.events.schema

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@Serializable(with = FactorioObjectDataSerializer::class)
sealed class FactorioObjectData {

  @EncodeDefault
  abstract val objectName: ObjectName

  // workaround - https://github.com/Kotlin/kotlinx.serialization/issues/1664
//  val objectName: String by lazy {
//    serializer().descriptor.serialName
//  }

//  val objectName: String by lazy {
//    kotlinx.serialization.serializer(this::class.starProjectedType).descriptor.serialName
//  }

  // alternative: find the @SerialName annotation
  // Again it must be delegated so there's no backing field and kxs ignores it
//  val objectName: String by lazy {
//    requireNotNull(this::class.findAnnotation<SerialName>()?.value) {
//      "Couldn't find @SerialName for ${this::class}!"
//    }
//  }

  enum class ObjectName {
    LuaPlayer,
    LuaEntity,
    LuaSurface,
    MapChunk,
    LuaTile,
    LuaTiles,
    ConsoleChatMessage,
  }
}

object FactorioObjectDataSerializer : JsonContentPolymorphicSerializer<FactorioObjectData>(
  FactorioObjectData::class
) {
  private val key = FactorioObjectData::objectName.name

  override fun selectDeserializer(element: JsonElement): DeserializationStrategy<out FactorioObjectData> {

    val type = element
      .jsonObject[key]
      ?.jsonPrimitive
      ?.contentOrNull
      ?.let { json ->
        FactorioObjectData.ObjectName.values().firstOrNull { it.name == json }
      }

    return when (type) {
      FactorioObjectData.ObjectName.LuaPlayer          -> PlayerData.serializer()
      FactorioObjectData.ObjectName.LuaEntity          -> EntityData.serializer()
      FactorioObjectData.ObjectName.LuaSurface         -> SurfaceData.serializer()
      FactorioObjectData.ObjectName.MapChunk           -> MapChunk.serializer()
      FactorioObjectData.ObjectName.LuaTile            -> MapTile.serializer()
      FactorioObjectData.ObjectName.LuaTiles            -> MapTiles.serializer()
      FactorioObjectData.ObjectName.ConsoleChatMessage -> ConsoleChatMessage.serializer()
      null                                             ->
        throw Exception("Unknown FactorioObjectData $key: '$type' ")
    }
  }
}


@Serializable
data class PlayerData(
  @Serializable(with = ListAsObjectSerializer::class)
  val associatedCharactersUnitNumbers: List<UInt>,
  val characterUnitNumber: UInt?,
  val name: String,
  val position: MapEntityPosition,
  val colour: Colour,
  val chatColour: Colour,
  val lastOnline: UInt,
) : FactorioObjectData() {
  @EncodeDefault
  override val objectName = ObjectName.LuaPlayer
}

@Serializable
data class EntityData(
  val active: Boolean,
  val health: Double?,
  val healthRatio: Double,
  val name: String,
  val position: MapEntityPosition,
  val surfaceIndex: Int,
  val type: String,
  val unitNumber: UInt? = null,
  val playerIndex: UInt? = null,
) : FactorioObjectData() {
  @EncodeDefault
  override val objectName = ObjectName.LuaEntity
}

@Serializable
data class SurfaceData(
  val daytime: Double,
  val index: UInt,
  val name: String,
) : FactorioObjectData() {
  @EncodeDefault
  override val objectName = ObjectName.LuaSurface
}

/** This isn't a Factorio Lua type, but a helpful collection of Chunk info */
@Serializable
data class MapChunk(
  val tiles: MapTiles,
  val position: MapChunkPosition,
  val area: MapBoundingBox,
) : FactorioObjectData() {
  @EncodeDefault
  override val objectName = ObjectName.MapChunk
}

/** This isn't a Factorio Lua type, but a helpful collection of [MapTile]s */
@Serializable
data class MapTiles(
  val surfaceIndex: Int,
  val tiles: List<MapTile>,
) : FactorioObjectData() {
  @EncodeDefault
  override val objectName = ObjectName.LuaTiles
}

@Serializable
data class MapTile(
  val prototypeName: String,
  val position: MapTilePosition,
) : FactorioObjectData() {
  @EncodeDefault
  override val objectName = ObjectName.LuaTile
}

@Serializable
data class ConsoleChatMessage(
  val authorPlayerIndex: UInt?,
  val content: String,
) : FactorioObjectData() {
  @EncodeDefault
  override val objectName = ObjectName.ConsoleChatMessage
}
