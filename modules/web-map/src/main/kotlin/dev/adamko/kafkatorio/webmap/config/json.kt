package dev.adamko.kafkatorio.webmap.config

import dev.adamko.kafkatorio.library.jsonMapperKafkatorio
import kotlinx.serialization.json.Json

val jsonMapper = Json {
  prettyPrint = true
  serializersModule = jsonMapperKafkatorio.serializersModule
}
