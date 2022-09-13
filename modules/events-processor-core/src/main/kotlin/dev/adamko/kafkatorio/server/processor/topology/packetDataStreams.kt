package dev.adamko.kafkatorio.server.processor.topology

import dev.adamko.kafkatorio.processor.config.jsonMapper
import dev.adamko.kafkatorio.schema.common.FactorioServerId
import dev.adamko.kafkatorio.schema.packets.KafkatorioPacket
import dev.adamko.kafkatorio.schema.packets.KafkatorioPacketData
import dev.adamko.kafkatorio.processor.config.topicName
import dev.adamko.kotka.extensions.consumedAs
import dev.adamko.kotka.extensions.streams.filter
import dev.adamko.kotka.extensions.streams.mapValues
import dev.adamko.kotka.kxs.serde
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.kstream.KStream


inline fun <reified T : KafkatorioPacketData> StreamsBuilder.streamPacketData(): KStream<FactorioServerId, T> {

  val simpleName = T::class.simpleName!!
  val topicName = T::class.topicName

  val taskName = "consume.${simpleName}"

  return stream<FactorioServerId, KafkatorioPacket>(
    topicName,
    consumedAs("$taskName.input", jsonMapper.serde(), jsonMapper.serde())
  ).filter("$taskName.filter-is-instance") { key: FactorioServerId?, value: KafkatorioPacket? ->
    key != null && value?.data is T
  }.mapValues("$taskName.map-values") { _: FactorioServerId, packet: KafkatorioPacket ->
    packet.data as T
  }
}
