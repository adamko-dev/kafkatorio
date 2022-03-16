package dev.adamko.kafkatorio.events.schema

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonTransformingSerializer
import kotlinx.serialization.json.buildJsonObject
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
