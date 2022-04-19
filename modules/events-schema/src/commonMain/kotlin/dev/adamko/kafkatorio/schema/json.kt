package dev.adamko.kafkatorio.schema

import kotlin.reflect.KClass
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonTransformingSerializer
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.modules.SerializersModule


val jsonMapperKafkatorio = Json {
  prettyPrint = true
  prettyPrintIndent = "  "

  serializersModule = SerializersModule {
    contextual(List::class) { args -> FactorioJsonListSerializer(args[0]) }
  }

}


/**
 * Factorio outputs an *empty* list as an *empty* Json object. This serde performs the same
 * conversion.
 */
class FactorioJsonListSerializer<T>(dataSerializer: KSerializer<T>) :
  JsonTransformingSerializer<List<T>>(ListSerializer(dataSerializer)) {

  override fun transformDeserialize(element: JsonElement): JsonElement {
    return when (element) {
      is JsonObject -> JsonArray(emptyList())
      else          -> element
    }
  }

  override fun transformSerialize(element: JsonElement): JsonElement {
    return when {
      element is JsonArray && element.isEmpty() -> buildJsonObject { }
      else                                      -> element
    }
  }

}


interface SerializerProvider<T : Any> {
  val serializer: KSerializer<out T>
}

open class SerializerProviderDeserializer<T : Any>(
  private val baseClass: KClass<T>,
  private val key: String,
  private val selector: (String) -> SerializerProvider<out T>?,
) : JsonContentPolymorphicSerializer<T>(baseClass) {

  override fun selectDeserializer(element: JsonElement): DeserializationStrategy<out T> {

    val type = element
      .jsonObject[key]
      ?.jsonPrimitive
      ?.contentOrNull
      ?.let { json -> selector(json) }

    requireNotNull(type) { "Unknown ${baseClass.simpleName} $key: $element" }

    return type.serializer
  }
}
