package dev.adamko.kafkatorio.schema2

import dev.adamko.kafkatorio.schema.common.Tick
import kotlinx.serialization.Serializable


@Serializable
data class KafkatorioPacket2(
  val modVersion: String,
  val tick: Tick,
  val data: KafkatorioPacketData2,
)


@Serializable
sealed class KafkatorioPacketData2
