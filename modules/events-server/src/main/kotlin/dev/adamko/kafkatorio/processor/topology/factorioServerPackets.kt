package dev.adamko.kafkatorio.processor.topology


import dev.adamko.kafkatorio.processor.admin.TOPIC_SRC_SERVER_LOG
import dev.adamko.kafkatorio.processor.admin.topicName
import dev.adamko.kafkatorio.processor.serdes.jsonMapper
import dev.adamko.kafkatorio.schema.packets.KafkatorioPacket
import dev.adamko.kotka.extensions.consumedAs
import dev.adamko.kotka.extensions.producedAs
import dev.adamko.kotka.extensions.streams.map
import dev.adamko.kotka.extensions.streams.to
import dev.adamko.kotka.kxs.serde
import kotlinx.serialization.decodeFromString
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.kstream.KStream
import org.apache.kafka.streams.processor.RecordContext


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
    val packet = jsonMapper.decodeFromString<KafkatorioPacket>(value)
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
    ) { _: FactorioServerId, value: KafkatorioPacket, _: RecordContext ->
//        println("[$key] sending event:${value.eventType} to topic:${value.data.objectName()}")
      value.data.topicName
    }
}
