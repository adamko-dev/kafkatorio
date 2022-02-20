package dev.adamko.kafkatorio.events.schema;

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive


@Serializable(with = KafkatorioPacketJsonSerializer::class)
sealed class KafkatorioPacket {
  /** Schema versioning */
  abstract val modVersion: String
  @EncodeDefault
  abstract val packetType: PacketType

  enum class PacketType {
    EVENT,
    CONFIG,
    PROTOTYPES,
    ;

    companion object {
      val values: List<PacketType> = values().toList()
    }
  }
}


object KafkatorioPacketJsonSerializer : JsonContentPolymorphicSerializer<KafkatorioPacket>(
  KafkatorioPacket::class
) {
  private val key = KafkatorioPacket::packetType.name

  override fun selectDeserializer(element: JsonElement): DeserializationStrategy<out KafkatorioPacket> {

    val type = element
      .jsonObject[key]
      ?.jsonPrimitive
      ?.contentOrNull
      ?.let { json ->
        KafkatorioPacket.PacketType.values.firstOrNull { it.name == json }
      }

    requireNotNull(type) { "Unknown KafkatorioPacket ${key}: $element" }

    return when (type) {
      KafkatorioPacket.PacketType.EVENT      -> FactorioEvent.serializer()
      KafkatorioPacket.PacketType.CONFIG     -> FactorioConfigurationUpdate.serializer()
      KafkatorioPacket.PacketType.PROTOTYPES -> FactorioPrototypes.serializer()
    }
  }
}
