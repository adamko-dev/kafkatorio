package dev.adamko.kafkatorio.processor

import dev.adamko.kafkatorio.events.schema.FactorioEvent
import dev.adamko.kafkatorio.events.schema.FactorioObjectData
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import org.apache.kafka.common.serialization.Deserializer
import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.common.serialization.Serializer
import org.http4k.format.ConfigurableKotlinxSerialization
import org.http4k.format.asConfigurable
import org.http4k.format.withStandardMappings


object JsonSerdes : Serde<FactorioEvent<FactorioObjectData>> {
  override fun serializer() = Serializer<FactorioEvent<FactorioObjectData>> { _, message ->
    jsonMapper.encodeToString<FactorioEvent<FactorioObjectData>>(message).encodeToByteArray()
  }

  override fun deserializer() = Deserializer<FactorioEvent<FactorioObjectData>> { _, bytes ->
    jsonMapper.decodeFromString(bytes.decodeToString())
  }
}

val jsonMapper = Json {
  prettyPrint = true
  prettyPrintIndent = "  "
  serializersModule = SerializersModule { }
}

object KXS : ConfigurableKotlinxSerialization({
  ignoreUnknownKeys = true
  prettyPrint = true
  prettyPrintIndent = "  "
  asConfigurable().withStandardMappings().done()
  serializersModule = SerializersModule { }
})
