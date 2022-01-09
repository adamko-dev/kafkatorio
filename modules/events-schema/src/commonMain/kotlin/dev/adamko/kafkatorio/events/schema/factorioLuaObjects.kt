package dev.adamko.kafkatorio.events.schema

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@Serializable
@JsonClassDiscriminator(FactorioObjectData.discriminatorKey)
sealed class FactorioObjectData {

  /** @see FactorioObjectData.discriminatorKey */
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
    ConsoleChatMessage,
  }

  companion object {
    /** The [JsonClassDiscriminator] for [FactorioEvent] */
    const val discriminatorKey: String = "objectName"
  }
}


@Serializable
@SerialName("LuaPlayer")
data class PlayerData(
  @Serializable(with = ListAsObjectSerializer::class)
  val associatedCharactersUnitNumbers: List<UInt>,
  val characterUnitNumber: UInt?,
  val name: String,
  val position: PositionData,
  val colour: Colour,
  val chatColour: Colour,
  val lastOnline: UInt,
) : FactorioObjectData() {
  override val objectName = ObjectName.LuaPlayer
}

@Serializable
@SerialName("LuaEntity")
data class EntityData(
  val active: Boolean,
  val health: Double?,
  val healthRatio: Double,
  val name: String,
  val position: PositionData,
  val surfaceIndex: Int,
  val type: String,
  val unitNumber: UInt? = null,
  val playerIndex: UInt? = null,
) : FactorioObjectData() {
  override val objectName = ObjectName.LuaEntity
}

@Serializable
@SerialName("LuaSurface")
data class SurfaceData(
  val daytime: Double,
  val index: UInt,
  val name: String,
) : FactorioObjectData() {
  override val objectName = ObjectName.LuaSurface
}

/** Quasi-object. This isn't a Factorio Lua type, but a helpful collection of [MapTile]s */
@Serializable
@SerialName("MapChunk")
data class MapChunk(
  val tiles: List<MapTile>,
) : FactorioObjectData() {
  override val objectName = ObjectName.MapChunk
}

@Serializable
@SerialName("LuaTile")
data class MapTile(
  val prototypeName: String,
  val position: PositionData,
  val surfaceIndex: Int,
) : FactorioObjectData() {
  override val objectName = ObjectName.LuaTile
}

@Serializable
@SerialName("ConsoleChatMessage")
data class ConsoleChatMessage(
  val authorPlayerIndex: UInt?,
  val content: String,
) : FactorioObjectData() {
  override val objectName = ObjectName.ConsoleChatMessage
}
