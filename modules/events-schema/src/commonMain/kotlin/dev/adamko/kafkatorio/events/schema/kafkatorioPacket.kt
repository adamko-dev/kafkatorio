package dev.adamko.kafkatorio.events.schema;

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive


@Serializable(with = KafkatorioPacketSerializer::class)
sealed class KafkatorioPacket {
  /** Schema versioning */
  abstract val modVersion: String
  abstract val packetType: PacketType

  enum class PacketType {
    EVENT,
    CONFIG,
  }
}


object KafkatorioPacketSerializer : JsonContentPolymorphicSerializer<KafkatorioPacket>(
  KafkatorioPacket::class
) {
  private const val key = "packetType"

  override fun selectDeserializer(element: JsonElement): DeserializationStrategy<out KafkatorioPacket> {

    val type = element
      .jsonObject[key]
      ?.jsonPrimitive
      ?.contentOrNull
      ?.let(KafkatorioPacket.PacketType::valueOf)

    return when (type) {
      KafkatorioPacket.PacketType.EVENT  -> FactorioEventSerializer
      KafkatorioPacket.PacketType.CONFIG -> FactorioConfigurationUpdate.serializer()
      else                               ->
        throw Exception("Unknown Packet: key '$key' ")
    }
  }
}
