package dev.adamko.kafkatorio.events.schema;

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.Required
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

//@Serializable
@Serializable(with = KafkatorioPacketSerializer::class)
//@JsonClassDiscriminator("packetType")
sealed class KafkatorioPacket {
  /** Schema versioning */
  abstract val modVersion: String
  @EncodeDefault
  abstract val packetType: PacketType

  enum class PacketType {
    EVENT,
    CONFIG,
  }

  companion object {
//    val kxsModule = SerializersModule {
//      polym
//    }
  }

}

object KafkatorioPacketSerializer : JsonContentPolymorphicSerializer<KafkatorioPacket>(
  KafkatorioPacket::class
) {
  private val key =
    KafkatorioPacket::packetType.name
//    "packetType"

  override fun selectDeserializer(element: JsonElement): DeserializationStrategy<out KafkatorioPacket> {

    val type = element
      .jsonObject[key]
      ?.jsonPrimitive
      ?.contentOrNull
      ?.let { json ->
        KafkatorioPacket.PacketType.values().firstOrNull { it.name == json }
      }

    return when (type) {
      KafkatorioPacket.PacketType.EVENT  -> FactorioEvent.serializer()
      KafkatorioPacket.PacketType.CONFIG -> FactorioConfigurationUpdate.serializer()
      null                               ->
        throw Exception("Unknown KafkatorioPacket ${key}: '$type' ")
    }
  }
}
