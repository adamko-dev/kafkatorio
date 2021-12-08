package dev.adamko.kafkatorio.events.schema


//import kotlin.reflect.full.starProjectedType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator


//@Serializable(with = FactorioEventSerializer::class)
@Serializable
data class FactorioEvent<out T : FactorioObjectData>(
  /** the initial Factorio event ({defines.events}) trigger */
  @SerialName("event_type")
  val eventType: String,
  /** Schema versioning */
  @SerialName("mod_version")
  val modVersion: String,
  @SerialName("factorio_version")
  val factorioVersion: String,
  /** game time */
  @SerialName("tick")
  val tick: UInt,
  @SerialName("data")
  val data: T
)


/** The [JsonClassDiscriminator] for [FactorioEvent] */
const val FactorioObjectDataDiscriminatorKey: String = "object_name"


@Serializable
@JsonClassDiscriminator(FactorioObjectDataDiscriminatorKey)
sealed class FactorioObjectData {

  // workaround - https://github.com/Kotlin/kotlinx.serialization/issues/1664
  val objectName: String by lazy {
    serializer().descriptor.serialName
  }

//  val objectName: String by lazy {
//    kotlinx.serialization.serializer(this::class.starProjectedType).descriptor.serialName
//  }

  // alternative: find the @SerialName annotation
  // Again it must be delegated so there's no backing field and kxs ignores it
//  val objectNameByAnnotation: String by lazy {
//    requireNotNull(this::class.findAnnotation<SerialName>()?.value) {
//      "Couldn't find @SerialName for ${this::class}!"
//    }
//  }
}

@Serializable
@SerialName("LuaPlayer")
data class PlayerData(
  @Serializable(with = ListAsObjectSerializer::class)
  @SerialName("associated_characters_unit_numbers")
  val associatedCharactersUnitNumbers: List<UInt>,
  @SerialName("character_unit_number")
  val characterUnitNumber: UInt?,
  @SerialName("name")
  val name: String,
  @SerialName("position")
  val position: PositionData,
) : FactorioObjectData()

@Serializable
@SerialName("LuaEntity")
data class EntityData(
  @SerialName("active")
  val active: Boolean,
  @SerialName("health")
  val health: Double?,
  @SerialName("health_ratio")
  val healthRatio: Double,
  @SerialName("name")
  val name: String,
  @SerialName("position")
  val position: PositionData,
  @SerialName("surface_index")
  val surfaceIndex: Int,
  @SerialName("type")
  val type: String,
  @SerialName("unit_number")
  val unitNumber: UInt? = null,

  @SerialName("player_index")
  val playerIndex: UInt? = null,
) : FactorioObjectData()

@Serializable
@SerialName("LuaSurface")
data class SurfaceData(
  @SerialName("daytime")
  val daytime: Double,
  @SerialName("index")
  val index: UInt,
  @SerialName("name")
  val name: String,
) : FactorioObjectData()

@Serializable
data class PositionData(
  @SerialName("x")
  val x: Double,
  @SerialName("y")
  val y: Double,
)
