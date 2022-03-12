package dev.adamko.kafkatorio.events.schema


import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.Serializable

@Serializable
data class FactorioEvent(
  override val modVersion: String,
  override val tick: Tick,
  /** the initial Factorio event (`defines.events`) trigger */
  val eventType: String,
  val data: FactorioObjectData,
) : KafkatorioPacket() {
  @EncodeDefault
  override val packetType: PacketType = PacketType.EVENT
}
