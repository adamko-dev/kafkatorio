package dev.adamko.kafkatorio.processor

import dev.adamko.kafkatorio.processor.config.ApplicationProperties
import dev.adamko.kafkatorio.processor.tileserver.WebsocketServer
import dev.adamko.kafkatorio.processor.topology.broadcastToWebsocket
import dev.adamko.kafkatorio.processor.topology.colourMapChunks
import dev.adamko.kafkatorio.processor.topology.factorioServerPacketStream
import dev.adamko.kafkatorio.processor.topology.saveMapTiles
import dev.adamko.kafkatorio.processor.topology.splitFactorioServerPacketStream
import java.util.Properties
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.io.path.Path
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.StreamsConfig
import org.apache.kafka.streams.Topology
import org.apache.kafka.streams.TopologyDescription


internal class KafkatorioTopology(
  private val websocketServer: WebsocketServer,
  private val appProps: ApplicationProperties,
) {

  private val builderScope = CoroutineScope(CoroutineName("KafkaTopology"))

  suspend fun start() = builderScope.launch {
    splitPackets()

    groupTilesMapChunks()
    saveTiles()

    websocketBroadcaster()
  }

  private suspend fun splitPackets() {
    val builder = StreamsBuilder()
    val packets = factorioServerPacketStream(builder)
    splitFactorioServerPacketStream(packets)

    val topology = builder.build()

    launchTopology("splitPackets", topology)
  }

  private suspend fun groupTilesMapChunks() {
    val builder = StreamsBuilder()
    val topology = colourMapChunks(builder)
    launchTopology("groupTilesMapChunks", topology)
  }

  private suspend fun saveTiles() {
    val builder = StreamsBuilder()
    val topology = saveMapTiles(builder, appProps.tileDir)
    launchTopology("saveTiles", topology)
  }

  private suspend fun websocketBroadcaster() {
    val builder = StreamsBuilder()
    broadcastToWebsocket(websocketServer, builder)

    val topology = builder.build()

    launchTopology(
      "websocketBroadcaster", topology, mapOf(
        StreamsConfig.COMMIT_INTERVAL_MS_CONFIG to "${1.seconds.inWholeMilliseconds}",
        StreamsConfig.producerPrefix(ProducerConfig.LINGER_MS_CONFIG) to "${10.milliseconds.inWholeMilliseconds}"
      )
    )
  }

  private suspend fun launchTopology(
    id: String,
    topology: Topology,
    additionalProperties: Map<String, String> = mapOf(),
  ) {

    val props: Properties = appProps.kafkaStreamsConfig.toProperties()

    additionalProperties.forEach { (k, v) -> props.setProperty(k, v) }

    val appId = props.compute(StreamsConfig.APPLICATION_ID_CONFIG) { _, v -> "$v.$id" } as String
    props.setProperty(StreamsConfig.APPLICATION_ID_CONFIG, appId)

    builderScope.launch(CoroutineName(appId)) {

      val streamsState: KafkaStreams.State = suspendCancellableCoroutine { cont ->

        val streams = KafkaStreams(topology, props)
        streams.setUncaughtExceptionHandler(StreamsExceptionHandler { e ->
          cont.resumeWithException(e)
        })

        streams.setStateListener { newState: KafkaStreams.State, _ ->
          when (newState) {
            KafkaStreams.State.NOT_RUNNING,
//            KafkaStreams.State.ERROR,
                 -> if (cont.isActive) {
              cont.resume(newState)
            }
            else -> Unit // do nothing
          }
        }

        cont.invokeOnCancellation {
          println("[$appId] coroutine cancelled -> closing. cause:$it")
          streams.close()
        }

        printTopologyDescription(appId, topology)
        streams.start()
        println("launched Topology $appId")
      }
      println("exiting Topology $appId: $streamsState")
      currentCoroutineContext().job.cancel("$appId: $streamsState")
    }
  }

  private fun printTopologyDescription(appId: String, topology: Topology) {
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
  }

  companion object {
    private fun Map<String, String>.toProperties(): Properties =
      entries.fold(Properties()) { props, (k, v) ->
        props.apply { setProperty(k, v) }
      }
  }
}
