package dev.adamko.kafkatorio.processor.topology

import dev.adamko.kafkatorio.processor.admin.topicName
import dev.adamko.kafkatorio.processor.serdes.jsonMapper
import dev.adamko.kafkatorio.schema.packets.KafkatorioPacket
import dev.adamko.kafkatorio.schema.packets.KafkatorioPacketData
import dev.adamko.kotka.extensions.consumedAs
import dev.adamko.kotka.extensions.streams.mapValues
import dev.adamko.kotka.kxs.serde
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.kstream.KStream


inline fun <reified T : KafkatorioPacketData> StreamsBuilder.streamPacketData(): KStream<FactorioServerId, T> {

  val simpleName = T::class.simpleName!!
  val topicName = T::class.topicName

  return stream<FactorioServerId, KafkatorioPacket>(
    topicName,
    consumedAs("consume.${simpleName}", jsonMapper.serde(), jsonMapper.serde())
  ).filter { key: FactorioServerId?, value: KafkatorioPacket? ->
    key != null && value?.data is T
  }.mapValues("consume.map-values.${simpleName}") { _: FactorioServerId, packet: KafkatorioPacket ->
    packet.data as T
  }
}
