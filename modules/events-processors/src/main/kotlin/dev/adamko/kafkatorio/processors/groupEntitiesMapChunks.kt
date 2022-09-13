package dev.adamko.kafkatorio.processors

import dev.adamko.kafkatorio.processor.core.launchTopology
import dev.adamko.kafkatorio.schema.common.FactorioServerId
import dev.adamko.kafkatorio.schema.common.PrototypeHashCode
import dev.adamko.kafkatorio.schema.common.ServerMapChunkId
import dev.adamko.kafkatorio.schema.common.ServerMapTileLayer
import dev.adamko.kafkatorio.schema.packets.MapChunkEntityUpdate
import dev.adamko.kafkatorio.schema.packets.MapChunkResourceUpdate
import dev.adamko.kafkatorio.schema.packets.PrototypesUpdate
import dev.adamko.kafkatorio.server.processor.topology.ServerMapChunkTiles
import dev.adamko.kafkatorio.server.processor.topology.colourEntityChunks
import dev.adamko.kafkatorio.server.processor.topology.convertEntityUpdateToServerMapChunkTiles
import dev.adamko.kafkatorio.server.processor.topology.convertResourceUpdateToServerMapChunkTiles
import dev.adamko.kafkatorio.server.processor.topology.streamPacketData
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.kstream.KStream


/** Building and resource tiles */
suspend fun groupEntitiesMapChunks(
  layer: ServerMapTileLayer,
  colouredUpdatesStreamTopic: String,
  colouredChunkStateTopic: String,
) {
  val builder = StreamsBuilder()

  val entityChunksStream: KStream<ServerMapChunkId, ServerMapChunkTiles<PrototypeHashCode>> =
    when (layer) {
      ServerMapTileLayer.TERRAIN  -> return

      ServerMapTileLayer.RESOURCE ->
        builder.streamPacketData<MapChunkResourceUpdate>()
          .convertResourceUpdateToServerMapChunkTiles("groupEntitiesMapChunks.${layer.dir}")

      ServerMapTileLayer.BUILDING ->
        builder.streamPacketData<MapChunkEntityUpdate>()
          .convertEntityUpdateToServerMapChunkTiles("groupEntitiesMapChunks.${layer.dir}")
    }

  val protosStream: KStream<FactorioServerId, PrototypesUpdate> =
    builder.streamPacketData()

  val topology = colourEntityChunks(
    builder,
    entityChunksStream,
    protosStream,
    layer,
    colouredUpdatesStreamTopic,
    colouredChunkStateTopic,
  )

  launchTopology("groupTilesMapChunks.${layer.dir}", topology)
}
