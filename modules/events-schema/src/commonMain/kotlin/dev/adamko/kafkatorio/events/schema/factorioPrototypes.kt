package dev.adamko.kafkatorio.events.schema

import dev.adamko.kafkatorio.events.schema.FactorioPrototype.PrototypeObjectName
import kotlinx.serialization.Contextual
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive



@Serializable(with = FactorioPrototypeJsonSerializer::class)
sealed class FactorioPrototype {
  @EncodeDefault
  abstract val prototypeObjectName: PrototypeObjectName

  enum class PrototypeObjectName {
    LuaTilePrototype,
    ;

    companion object {
      val values: List<PrototypeObjectName> = values().toList()
    }
  }
}


object FactorioPrototypeJsonSerializer : JsonContentPolymorphicSerializer<FactorioPrototype>(
  FactorioPrototype::class
) {
  private val key = FactorioPrototype::prototypeObjectName.name

  override fun selectDeserializer(element: JsonElement): DeserializationStrategy<out FactorioPrototype> {

    val type = element
      .jsonObject[key]
      ?.jsonPrimitive
      ?.contentOrNull
      ?.let { json ->
        PrototypeObjectName.values.firstOrNull { it.name == json }
      }

    requireNotNull(type) { "Unknown FactorioPrototype ${key}: $element" }

    return when (type) {
      PrototypeObjectName.LuaTilePrototype -> MapTilePrototype.serializer()
    }
  }
}


@Serializable
data class MapTilePrototype(
  val name: PrototypeName,
  val layer: UInt,
  val mapColour: Colour,
  @Contextual
  val collisionMasks: List<String>,
  val order: String,
  /** Can the tile be mined for resources? */
  val canBeMined: Boolean,
) : FactorioPrototype() {
  @EncodeDefault
  override val prototypeObjectName: PrototypeObjectName = PrototypeObjectName.LuaTilePrototype
}
