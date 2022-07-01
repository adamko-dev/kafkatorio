package dev.adamko.kafkatorio.processor.serdes

import dev.adamko.kafkatorio.schema.jsonMapperKafkatorio
import kotlinx.serialization.json.Json

val jsonMapper: Json = Json {
  prettyPrint = true
  prettyPrintIndent = "  "
  serializersModule = jsonMapperKafkatorio.serializersModule
}
