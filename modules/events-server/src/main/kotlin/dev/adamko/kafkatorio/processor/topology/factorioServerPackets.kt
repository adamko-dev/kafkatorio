package dev.adamko.kafkatorio.processor.topology

import dev.adamko.kafkatorio.events.schema.FactorioConfigurationUpdate
import dev.adamko.kafkatorio.events.schema.FactorioEvent
import dev.adamko.kafkatorio.events.schema.FactorioPrototypes
import dev.adamko.kafkatorio.events.schema.KafkatorioPacket
import dev.adamko.kafkatorio.processor.KafkatorioTopology
import dev.adamko.kafkatorio.processor.serdes.jsonMapper
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
    KafkatorioTopology.sourceTopic,
    consumedAs(
      "read-raw-packets-from-server",
      Serdes.String(),
      Serdes.String(),
    )
  )
    .map("decode-packets") { serverId: String, value: String ->
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
      when (value) {
        is FactorioEvent               ->
          "kafkatorio.${value.packetType.name}.${value.data.objectName.name}"
        is FactorioConfigurationUpdate ->
          "kafkatorio.${value.packetType.name}.FactorioConfigurationUpdate"
        is FactorioPrototypes          ->
          "kafkatorio.${value.packetType.name}.all"
      }
    }
}
