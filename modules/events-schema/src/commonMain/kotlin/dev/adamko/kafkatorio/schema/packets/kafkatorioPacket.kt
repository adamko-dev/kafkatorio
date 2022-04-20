package dev.adamko.kafkatorio.schema.packets

import dev.adamko.kafkatorio.schema.common.Tick
import kotlinx.serialization.Serializable


@Serializable
data class KafkatorioPacket(
  val modVersion: String,
  val tick: Tick,
  val data: KafkatorioPacketData,
)


@Serializable
sealed class KafkatorioPacketData
