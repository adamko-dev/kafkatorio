package dev.adamko.kafkatorio.processor.topology

import dev.adamko.kafkatorio.events.schema.FactorioConfigurationUpdate
import dev.adamko.kafkatorio.events.schema.FactorioEvent
import dev.adamko.kafkatorio.events.schema.FactorioPrototypes
import dev.adamko.kafkatorio.events.schema.KafkatorioPacket
import dev.adamko.kafkatorio.processor.KafkatorioPacketSerde
import dev.adamko.kafkatorio.processor.KafkatorioTopology
import dev.adamko.kafkatorio.processor.jsonMapper
import dev.adamko.kotka.extensions.consumedAs
import dev.adamko.kotka.extensions.producedAs
import dev.adamko.kotka.extensions.streams.mapValues
import dev.adamko.kotka.extensions.streams.to
import kotlinx.serialization.decodeFromString
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.processor.RecordContext


fun splitFactorioServerLog(
  builder: StreamsBuilder = StreamsBuilder(),
) {

  builder.stream(
    KafkatorioTopology.sourceTopic,
    consumedAs(
      "read-raw-packets-from-server",
      Serdes.String(),
      Serdes.String(),
    )
  )
    .mapValues("decode-packets") { _: String, value: String ->
//        println("Mapping $readOnlyKey:$value")
      jsonMapper.decodeFromString<KafkatorioPacket>(value)
    }
    .to(
      producedAs(
        "split-server-log",
        Serdes.String(),
        KafkatorioPacketSerde
      )
    ) { _: String, value: KafkatorioPacket, _: RecordContext ->
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
