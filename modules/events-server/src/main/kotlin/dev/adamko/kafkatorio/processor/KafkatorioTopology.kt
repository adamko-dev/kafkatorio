package dev.adamko.kafkatorio.processor

import dev.adamko.kafkatorio.processor.config.ApplicationProperties
import dev.adamko.kafkatorio.processor.topology.factorioServerPacketStream
import dev.adamko.kafkatorio.processor.topology.groupMapChunks
import dev.adamko.kafkatorio.processor.topology.playerUpdatesToWsServer
import dev.adamko.kafkatorio.processor.topology.saveMapTiles
import dev.adamko.kafkatorio.processor.topology.splitFactorioServerPacketStream
import java.time.Duration
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Duration.Companion.minutes
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
import org.apache.kafka.streams.TopologyDescription


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
    val topology = groupMapChunks(builder)
    launchTopology("groupTilesMapChunks", topology)
  }

  private fun saveTiles() {
    val builder = StreamsBuilder()
    val topology = saveMapTiles(builder)
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
    props.setProperty(StreamsConfig.MAX_TASK_IDLE_MS_CONFIG, "${1.minutes.inWholeMilliseconds}")

    val streams = KafkaStreams(topology, props)
    streams.setUncaughtExceptionHandler(StreamsExceptionHandler {
      coroutineContext.job.cancel(CancellationException(cause = it))
    })

    val description: TopologyDescription = topology.describe()
    println(
      """
        |----------------
        |$appId
        |$description
        |----------------
      """.trimMargin()
    )

    coroutineContext.job.invokeOnCompletion {
      println("closing $id KafkaStreams")
      streams.close(Duration.ofSeconds(1))
    }

    launch { streams.start() }
  }

}
