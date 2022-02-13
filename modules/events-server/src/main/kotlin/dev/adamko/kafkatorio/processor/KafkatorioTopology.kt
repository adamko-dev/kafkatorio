package dev.adamko.kafkatorio.processor

import dev.adamko.kafkatorio.events.schema.ColourHex
import dev.adamko.kafkatorio.events.schema.FactorioEvent
import dev.adamko.kafkatorio.events.schema.FactorioObjectData
import dev.adamko.kafkatorio.events.schema.FactorioPrototypes
import dev.adamko.kafkatorio.events.schema.KafkatorioPacket
import dev.adamko.kafkatorio.processor.config.ApplicationProperties
import dev.adamko.kafkatorio.processor.serdes.jsonMapper
import dev.adamko.kafkatorio.processor.serdes.kxsBinary
import dev.adamko.kafkatorio.processor.topology.FactorioServerId
import dev.adamko.kafkatorio.processor.topology.ServerMapChunkId
import dev.adamko.kafkatorio.processor.topology.ServerMapChunkTiles
import dev.adamko.kafkatorio.processor.topology.factorioServerPacketStream
import dev.adamko.kafkatorio.processor.topology.groupTilesIntoChunksWithColours
import dev.adamko.kafkatorio.processor.topology.playerUpdatesToWsServer
import dev.adamko.kafkatorio.processor.topology.saveMapTiles
import dev.adamko.kafkatorio.processor.topology.splitFactorioServerPacketStream
import dev.adamko.kafkatorio.processor.topology.tileProtoColourDictionary
import dev.adamko.kotka.extensions.consumedAs
import dev.adamko.kotka.extensions.producedAs
import dev.adamko.kotka.extensions.tables.toStream
import dev.adamko.kotka.kxs.serde
import java.time.Duration
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.StreamsConfig
import org.apache.kafka.streams.Topology
import org.apache.kafka.streams.kstream.KStream
import org.apache.kafka.streams.kstream.KTable


internal class KafkatorioTopology(
  private val websocketServer: WebsocketServer,
  private val appProps: ApplicationProperties = ApplicationProperties(),
) : CoroutineScope {

  override val coroutineContext: CoroutineContext =
    Dispatchers.Default + SupervisorJob() + CoroutineName("KafkatorioTopology")

  companion object {
    const val TOPIC_SRC_SERVER_LOG = "factorio-server-log"
    const val TOPIC_GROUPED_MAP_CHUNKS = "kafkatorio.MAP.grouped-map-chunks"
  }

  fun start() {
    groupTilesMapChunks()
    saveTiles()
    splitPackets()
    playerUpdates()
  }

  private fun groupTilesMapChunks() {
    val builder = StreamsBuilder()


    val protosStream: KStream<FactorioServerId, FactorioPrototypes> =
      builder.stream(
        "kafkatorio.${KafkatorioPacket.PacketType.PROTOTYPES}.all",
        consumedAs("consume.factorio-protos.all", jsonMapper.serde(), jsonMapper.serde())
      )
    val tileProtoColourDict = tileProtoColourDictionary(protosStream)


    val mapTilesStream: KStream<FactorioServerId, FactorioEvent> =
      builder.stream(
        "kafkatorio.${KafkatorioPacket.PacketType.EVENT}.${FactorioObjectData.ObjectName.LuaTiles}",
        consumedAs("consume.map-tiles", jsonMapper.serde(), jsonMapper.serde())
      )
    val mapChunksStream: KStream<FactorioServerId, FactorioEvent> =
      builder.stream(
        "kafkatorio.${KafkatorioPacket.PacketType.EVENT}.${FactorioObjectData.ObjectName.MapChunk}",
        consumedAs("consume.map-chunks", jsonMapper.serde(), jsonMapper.serde())
      )
    val groupedMapChunkTiles: KTable<ServerMapChunkId, ServerMapChunkTiles<ColourHex>> =
      groupTilesIntoChunksWithColours(
        mapTilesStream,
        mapChunksStream,
        tileProtoColourDict,
      )

    groupedMapChunkTiles
      .toStream("stream-grouped-map-tiles")
      .to(
        TOPIC_GROUPED_MAP_CHUNKS,
        producedAs("produce.grouped-map-chunks", kxsBinary.serde(), kxsBinary.serde())
      )


    val topology = builder.build()
    launchTopology("groupTilesMapChunks", topology)
  }

  private fun saveTiles() {
    val builder = StreamsBuilder()

    val groupedMapChunkTiles: KTable<ServerMapChunkId, ServerMapChunkTiles<ColourHex>> =
      builder.table(
        TOPIC_GROUPED_MAP_CHUNKS,
        consumedAs("consume.grouped-map-chunks", kxsBinary.serde(), kxsBinary.serde())
      )

    saveMapTiles(groupedMapChunkTiles)

    val topology = builder.build()
    launchTopology("saveTiles", topology)
  }

  private fun splitPackets() {

    val builder = StreamsBuilder()
    val packets = factorioServerPacketStream(builder)
    splitFactorioServerPacketStream(packets)

    val topology = builder.build()

    launchTopology("splitPackets", topology)
  }

  private fun playerUpdates() {
    val builder = StreamsBuilder()
    playerUpdatesToWsServer(websocketServer, builder)

    val topology = builder.build()

    launchTopology("playerUpdates", topology)
  }

  private fun launchTopology(id: String, topology: Topology) {

    val props = appProps.kafkaConfig

    val appId = props.compute(StreamsConfig.APPLICATION_ID_CONFIG) { _, v -> "$v.$id" } as? String
    props.setProperty(StreamsConfig.APPLICATION_ID_CONFIG, appId)


    val streams = KafkaStreams(topology, props)
    streams.setUncaughtExceptionHandler(StreamsExceptionHandler())

    coroutineContext.job.invokeOnCompletion {
      println("closing $id KafkaStreams")
      streams.close(Duration.ofSeconds(1))
    }

    launch { streams.start() }
  }

}
