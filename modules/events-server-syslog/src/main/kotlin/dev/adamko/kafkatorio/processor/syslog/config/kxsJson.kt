package dev.adamko.kafkatorio.processor.syslog.config

import dev.adamko.kafkatorio.library.jsonMapperKafkatorio
import kotlinx.serialization.json.Json


internal val jsonMapper: Json = Json {
  prettyPrint = true
  prettyPrintIndent = "  "
  serializersModule = jsonMapperKafkatorio.serializersModule
}
