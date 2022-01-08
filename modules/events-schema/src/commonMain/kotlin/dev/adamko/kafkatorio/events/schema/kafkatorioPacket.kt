package dev.adamko.kafkatorio.events.schema;

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator


@Serializable
@JsonClassDiscriminator("packetType")
sealed class KafkatorioPacket {
  /** Schema versioning */
  abstract val modVersion: String
  abstract val packetType: PacketType

  enum class PacketType {
    EVENT, CONFIG,
  }
}


//@Serializable
//@JsonClassDiscriminator(KafkatorioPacketData.discriminatorKey)
//sealed class KafkatorioPacketData(
//  @SerialName(discriminatorKey)
//  val dataType: PacketDataType
//) {
//  companion object {
//    const val discriminatorKey: String = "dataType"
//  }
//}
