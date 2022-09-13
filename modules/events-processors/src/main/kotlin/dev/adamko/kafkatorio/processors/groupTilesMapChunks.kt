package dev.adamko.kafkatorio.processors

import dev.adamko.kafkatorio.processor.core.launchTopology
import dev.adamko.kafkatorio.schema.common.FactorioServerId
import dev.adamko.kafkatorio.schema.common.ServerMapTileLayer
import dev.adamko.kafkatorio.schema.packets.PrototypesUpdate
import dev.adamko.kafkatorio.server.processor.topology.colourMapChunks
import dev.adamko.kafkatorio.server.processor.topology.streamPacketData
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.kstream.KStream


/** Terrain tiles */
suspend fun groupTilesMapChunks() {
  val builder = StreamsBuilder()

  val protosStream: KStream<FactorioServerId, PrototypesUpdate> =
    builder.streamPacketData()

  val topology = colourMapChunks(builder, protosStream)

  launchTopology("groupTilesMapChunks.${ServerMapTileLayer.TERRAIN.dir}", topology)
}
