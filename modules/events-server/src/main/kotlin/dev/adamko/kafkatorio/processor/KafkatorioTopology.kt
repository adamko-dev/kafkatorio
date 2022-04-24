package dev.adamko.kafkatorio.processor

import dev.adamko.kafkatorio.processor.config.ApplicationProperties
import dev.adamko.kafkatorio.processor.topology.factorioServerPacketStream
import dev.adamko.kafkatorio.processor.topology.groupMapChunks
import dev.adamko.kafkatorio.processor.topology.playerUpdatesToWsServer
import dev.adamko.kafkatorio.processor.topology.saveMapTiles2
import dev.adamko.kafkatorio.processor.topology.splitFactorioServerPacketStream
import java.time.Duration
import java.util.Properties
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.cancellation.CancellationException
import kotlin.io.path.Path
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

  fun start() {
    splitPackets()

    groupTilesMapChunks()
    saveTiles()

    playerUpdates()
  }

  private fun splitPackets() {
    val builder = StreamsBuilder()
    val packets = factorioServerPacketStream(builder)
    splitFactorioServerPacketStream(packets)

    val topology = builder.build()

    launchTopology("splitPackets", topology)
  }

  private fun groupTilesMapChunks() {
    val builder = StreamsBuilder()
    val topology = groupMapChunks(builder)
    launchTopology("groupTilesMapChunks", topology)
  }

  private fun saveTiles() {
    val builder = StreamsBuilder()
//    val topology = saveMapTiles(builder)
    val topology = saveMapTiles2(builder)
    launchTopology("saveTiles", topology)
  }

  private fun playerUpdates() {
    val builder = StreamsBuilder()
    playerUpdatesToWsServer(websocketServer, builder)

    val topology = builder.build()

    launchTopology("playerUpdates", topology)
  }

  private fun launchTopology(
    id: String,
    topology: Topology,
    config: Map<String, String> = mapOf()
  ) {

    val props: Properties = appProps.kafkaConfig

    config.forEach { (k, v) -> props.setProperty(k, v) }

    val appId = props.compute(StreamsConfig.APPLICATION_ID_CONFIG) { _, v -> "$v.$id" } as? String
    props.setProperty(StreamsConfig.APPLICATION_ID_CONFIG, appId)

//    props.setProperty(StreamsConfig.MAX_TASK_IDLE_MS_CONFIG, "${30.minutes.inWholeMilliseconds}")

    val streams = KafkaStreams(topology, props)
    streams.setUncaughtExceptionHandler(StreamsExceptionHandler {
      coroutineContext.job.cancel(CancellationException(cause = it))
    })

    val description: TopologyDescription = topology.describe()

    val descFile = Path("./build/$appId.txt").toFile().apply {
      parentFile.mkdirs()
      if (!exists()) createNewFile()
      writeText(description.toString())
    }

    println(
      """
        |----------------
        |$appId - ${descFile.canonicalPath}
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
