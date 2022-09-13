package dev.adamko.kafkatorio.server.web.config

import dev.adamko.kafkatorio.library.jsonMapperKafkatorio
import kotlinx.serialization.json.Json


internal val jsonMapper: Json = Json {
  prettyPrint = true
  prettyPrintIndent = "  "
  serializersModule = jsonMapperKafkatorio.serializersModule
}
