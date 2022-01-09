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


/** Factorio outputs lists as Json objects - this serializer converts an object back to a list. */
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

val jsonMapperKafkatorio = Json {
  prettyPrint = true
  prettyPrintIndent = "  "
  serializersModule =  FactorioEvent.kxsModule
//  serializersModule = KafkatorioPacket.kxsModule + FactorioEvent.kxsModule
//  serializersModule = SerializersModule { }
}
