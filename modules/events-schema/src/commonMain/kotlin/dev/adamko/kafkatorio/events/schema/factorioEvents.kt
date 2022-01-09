package dev.adamko.kafkatorio.events.schema


import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.Serializable

@Serializable
data class FactorioEvent(
  override val modVersion: String,
  /** the initial Factorio event (`defines.events`) trigger */
  val eventType: String,
  /** game time */
  val tick: UInt,
  val data: FactorioObjectData,
) : KafkatorioPacket() {
  @EncodeDefault
  override val packetType: PacketType = PacketType.EVENT
}
