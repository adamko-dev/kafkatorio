package dev.adamko.kafkatorio.events.schema

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive


@Serializable(with = FactorioPrototypeSerializer::class)
sealed class FactorioPrototype {
  @EncodeDefault
  abstract val prototypeObjectName: PrototypeObjectName

  enum class PrototypeObjectName {
    LuaTilePrototype
  }
}

object FactorioPrototypeSerializer : JsonContentPolymorphicSerializer<FactorioPrototype>(
  FactorioPrototype::class
) {
  private val key = FactorioPrototype::prototypeObjectName.name

  override fun selectDeserializer(element: JsonElement): DeserializationStrategy<out FactorioPrototype> {

    val type = element
      .jsonObject[key]
      ?.jsonPrimitive
      ?.contentOrNull
      ?.let { json ->
        FactorioPrototype.PrototypeObjectName.values().firstOrNull { it.name == json }
      }

    return when (type) {
      FactorioPrototype.PrototypeObjectName.LuaTilePrototype -> FactorioMapTilePrototype.serializer()
      null                                                   ->
        throw Exception("Unknown FactorioPrototype $key: '$type' ")
    }
  }
}


@Serializable
data class FactorioMapTilePrototype(
  val name: String,
  val layer: UInt,
  val mapColor: Colour,
  val collisionMasks: List<String>,
  val order: String,
  /** Can the tile be mined for resources? */
  val canBeMined: Boolean,
) : FactorioPrototype() {
  @EncodeDefault
  override val prototypeObjectName: PrototypeObjectName = PrototypeObjectName.LuaTilePrototype
}
