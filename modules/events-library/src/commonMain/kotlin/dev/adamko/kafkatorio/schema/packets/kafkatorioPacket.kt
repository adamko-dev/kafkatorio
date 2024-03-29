package dev.adamko.kafkatorio.schema.packets

import dev.adamko.kafkatorio.schema.common.Tick
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
@SerialName("kafkatorio.packet.KafkatorioPacket")
data class KafkatorioPacket(
  val modVersion: String,
  val tick: Tick,
  val data: KafkatorioPacketData,
)


@Serializable
@SerialName("kafkatorio.packet.KafkatorioPacketData")
sealed class KafkatorioPacketData


@Serializable
@SerialName("kafkatorio.packet.KafkatorioPacketData.Error")
data class KafkatorioPacketDataError(
  val message: String? = null,
  val rawValue: String? = null,
) : KafkatorioPacketData() {
  constructor(
    exception: Exception,
    rawValue: String? = null
  ) : this(
    message = """
      $exception ${exception.message}
      
      ${exception.stackTraceToString()}
    """.trimIndent(),
    rawValue = rawValue,
  )
}
