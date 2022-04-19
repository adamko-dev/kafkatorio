package dev.adamko.kafkatorio.webmap

import dev.adamko.kafkatorio.schema.jsonMapperKafkatorio
import kotlinx.serialization.json.Json

val jsonMapper = Json {
  prettyPrint = true
  serializersModule = jsonMapperKafkatorio.serializersModule
}
