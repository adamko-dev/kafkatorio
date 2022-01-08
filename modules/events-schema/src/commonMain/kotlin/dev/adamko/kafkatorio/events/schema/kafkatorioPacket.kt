package dev.adamko.kafkatorio.events.schema;


//@Serializable
sealed interface KafkatorioPacket {
  /** Schema versioning */
  val modVersion: String
}


//enum class PacketDataType {
//  EVENT, PROTOTYPE, CONFIG_DATA,
//  ;
//}


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
