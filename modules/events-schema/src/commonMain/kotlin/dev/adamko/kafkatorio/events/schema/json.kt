package dev.adamko.kafkatorio.events.schema

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonTransformingSerializer
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject

val jsonMapperKafkatorio = Json {
  prettyPrint = true
  prettyPrintIndent = "  "
//  serializersModule =  FactorioEvent.kxsModule
//  serializersModule = KafkatorioPacket.kxsModule + FactorioEvent.kxsModule
//  serializersModule = SerializersModule { }
}


/** Factorio outputs lists as Json objects - this serializer converts an object back to a list. */
// actually I'm not sure that it does - I think *empty* lists get encoded as *empty* objects
internal class ListAsObjectSerializer<T>(dataSerializer: KSerializer<T>) :
  JsonTransformingSerializer<List<T>>(ListSerializer(dataSerializer)) {

  override fun transformDeserialize(element: JsonElement): JsonElement =
    JsonArray(
      element.jsonObject.values.toList()
    )

  override fun transformSerialize(element: JsonElement): JsonElement =
    JsonObject(
      element.jsonArray.mapIndexed { index, jsonElement -> "$index" to jsonElement }.toMap()
    )
}


/** Factorio outputs lists as Json objects - this serializer converts an object back to a list. */
// actually I'm not sure that it does - I think *empty* lists get encoded as *empty* objects
internal class FactorioJsonListSerializer<T>(dataSerializer: KSerializer<T>) :
  JsonTransformingSerializer<List<T>>(ListSerializer(dataSerializer)) {

  override fun transformDeserialize(element: JsonElement): JsonElement {
    return if (element is JsonObject) {
      JsonArray(emptyList())
    } else {
      element
    }
  }

}
