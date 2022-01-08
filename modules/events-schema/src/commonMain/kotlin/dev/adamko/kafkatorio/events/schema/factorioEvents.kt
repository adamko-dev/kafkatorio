package dev.adamko.kafkatorio.events.schema


import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient


@Serializable
data class FactorioEvent<out T : FactorioObjectData>(
  override val modVersion: String,
  /** the initial Factorio event (`defines.events`) trigger */
  val eventType: String,
  /** game time */
  val tick: UInt,
  val data: T,
) : KafkatorioPacket() {
  @Transient
  override val packetType: PacketType = PacketType.EVENT
}
