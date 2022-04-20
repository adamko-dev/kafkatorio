package dev.adamko.kafkatorio.processor.serdes

import dev.adamko.kafkatorio.schema.jsonMapperKafkatorio
import kotlinx.serialization.json.Json
import org.http4k.format.ConfigurableKotlinxSerialization
import org.http4k.format.asConfigurable
import org.http4k.format.withStandardMappings

val jsonMapper: Json = Json {
  prettyPrint = true
  prettyPrintIndent = "  "
  serializersModule = jsonMapperKafkatorio.serializersModule
}

object KXS : ConfigurableKotlinxSerialization({
  ignoreUnknownKeys = true
  prettyPrint = true
  prettyPrintIndent = "  "
  asConfigurable().withStandardMappings().done()
  serializersModule = jsonMapperKafkatorio.serializersModule
})
