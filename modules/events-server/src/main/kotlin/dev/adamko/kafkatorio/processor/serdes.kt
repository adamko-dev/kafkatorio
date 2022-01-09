package dev.adamko.kafkatorio.processor

import dev.adamko.kafkatorio.events.schema.KafkatorioPacket
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


object KafkatorioPacketSerde : Serde<KafkatorioPacket> {
  override fun serializer() = Serializer<KafkatorioPacket> { _, message ->
    jsonMapper.encodeToString<KafkatorioPacket>(message).encodeToByteArray()
  }

  override fun deserializer() = Deserializer<KafkatorioPacket> { _, bytes ->
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