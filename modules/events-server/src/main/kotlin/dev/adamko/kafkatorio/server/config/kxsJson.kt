package dev.adamko.kafkatorio.server.config

import dev.adamko.kafkatorio.library.jsonMapperKafkatorio
import kotlinx.serialization.json.Json


val jsonMapper: Json = Json {
  prettyPrint = true
  prettyPrintIndent = "  "
  serializersModule = jsonMapperKafkatorio.serializersModule
}
