package dev.adamko.kafkatorio.server.processor.topology

import dev.adamko.kafkatorio.schema.common.FactorioServerId
import dev.adamko.kafkatorio.schema.common.Tick
import dev.adamko.kafkatorio.schema.packets.KafkatorioPacket
import dev.adamko.kafkatorio.schema.packets.KafkatorioPacketDataError
import dev.adamko.kafkatorio.server.config.jsonMapper
import dev.adamko.kafkatorio.server.processor.TOPIC_SRC_SERVER_LOG
import dev.adamko.kafkatorio.server.processor.topicName
import dev.adamko.kotka.extensions.component1
import dev.adamko.kotka.extensions.component2
import dev.adamko.kotka.extensions.consumedAs
import dev.adamko.kotka.extensions.producedAs
import dev.adamko.kotka.extensions.streams.map
import dev.adamko.kotka.extensions.streams.to
import dev.adamko.kotka.kxs.serde
import kotlinx.serialization.decodeFromString
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.kstream.KStream


fun factorioServerPacketStream(
  builder: StreamsBuilder = StreamsBuilder(),
): KStream<FactorioServerId, KafkatorioPacket> {

  return builder.stream(
    TOPIC_SRC_SERVER_LOG,
    consumedAs(
      "read-raw-packets-from-server",
      Serdes.String(),
      Serdes.String(),
    )
  ).map("decode-packets") { serverId: String, value: String ->
    val packet = runCatching {
      jsonMapper.decodeFromString<KafkatorioPacket>(value)
    }.getOrElse { e ->
      KafkatorioPacket(
        modVersion = "unknown",
        tick = Tick(0u),
        data = KafkatorioPacketDataError(
          message = e.message,
          rawValue = value,
        )
      )
    }

    if (packet.data is KafkatorioPacketDataError) {
      println("error parsing $TOPIC_SRC_SERVER_LOG message: ${packet.data}")
    }

    FactorioServerId(serverId) to packet
  }
}


fun splitFactorioServerPacketStream(
  factorioServerPacketStream: KStream<FactorioServerId, KafkatorioPacket>
) {
  factorioServerPacketStream
    .to(
      producedAs(
        "split-server-log",
        jsonMapper.serde(),
        jsonMapper.serde(),
      )
    ) { (_: FactorioServerId, value: KafkatorioPacket) ->
//        println("[$key] sending event:${value.eventType} to topic:${value.data.objectName()}")
      value.data.topicName
    }
}
