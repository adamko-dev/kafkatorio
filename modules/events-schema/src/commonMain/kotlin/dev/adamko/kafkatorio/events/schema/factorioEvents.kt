package dev.adamko.kafkatorio.events.schema


import kotlinx.serialization.Serializable


@Serializable
data class FactorioEvent<out T : FactorioObjectData>(
  override val modVersion: String,

  /** the initial Factorio event (`defines.events`) trigger */
  val eventType: String,
  /** game time */
  val tick: UInt,
  val data: T,
) : KafkatorioPacket
