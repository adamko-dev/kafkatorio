package dev.adamko.kafkatorio.processors

import dev.adamko.kafkatorio.processor.core.launchTopology
import dev.adamko.kafkatorio.server.processor.topology.factorioServerPacketStream
import dev.adamko.kafkatorio.server.processor.topology.splitFactorioServerPacketStream
import org.apache.kafka.streams.StreamsBuilder


suspend fun splitPackets() {
  val builder = StreamsBuilder()
  val packets = factorioServerPacketStream(builder)
  splitFactorioServerPacketStream(packets)

  val topology = builder.build()

  launchTopology("splitPackets", topology)
}
