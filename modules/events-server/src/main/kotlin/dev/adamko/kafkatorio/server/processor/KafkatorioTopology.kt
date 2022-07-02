package dev.adamko.kafkatorio.server.processor

import com.github.palindromicity.syslog.dsl.SyslogFieldKeys
import dev.adamko.kafkatorio.server.config.ApplicationProperties
import dev.adamko.kafkatorio.server.processor.topology.colourMapChunks
import dev.adamko.kafkatorio.server.processor.topology.*
import dev.adamko.kafkatorio.server.socket.SyslogSocketServer
import dev.adamko.kafkatorio.server.web.websocket.WebmapWebsocketServer
import dev.adamko.kafkatorio.server.web.websocket.broadcastToWebsocket
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
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.header.internals.RecordHeader
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.StreamsConfig
import org.apache.kafka.streams.Topology
import org.apache.kafka.streams.TopologyDescription


internal class KafkatorioTopology(
  private val appProps: ApplicationProperties,
  private val wsServer: WebmapWebsocketServer,
  private val syslogServer: SyslogSocketServer,
) {

  private val builderScope = CoroutineScope(CoroutineName("KafkaTopology"))


  suspend fun start() = builderScope.launch {
    splitPackets()

    groupTilesMapChunks()
    saveTiles()

    websocketBroadcaster()

    syslogProducer()
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
    val topology = saveMapTiles(builder, appProps.serverDataDir)
    launchTopology("saveTiles", topology)
  }


  private suspend fun websocketBroadcaster() {
    val builder = StreamsBuilder()
    broadcastToWebsocket(wsServer, builder)

    val topology = builder.build()

    launchTopology(
      "websocketBroadcaster", topology, mapOf(
        StreamsConfig.COMMIT_INTERVAL_MS_CONFIG to "${1.seconds.inWholeMilliseconds}",
        StreamsConfig.producerPrefix(ProducerConfig.LINGER_MS_CONFIG) to "${1.milliseconds.inWholeMilliseconds}"
      )
    )
  }


  private fun syslogProducer(
    id: String = "syslog-producer"
  ) {
    val props: Properties = appProps.kafkaStreamsConfig.toProperties()

    val appId = props.compute(StreamsConfig.APPLICATION_ID_CONFIG) { _, v -> "$v.$id" } as String
    props.setProperty(StreamsConfig.APPLICATION_ID_CONFIG, appId)


    val producer: KafkaProducer<String, String> = KafkaProducer(
      props,
      Serdes.String().serializer(),
      Serdes.String().serializer(),
    )

    builderScope.coroutineContext.job.invokeOnCompletion {
      producer.close()
    }

    syslogServer.messages
      .filter { it.message?.startsWith("KafkatorioPacket:") == true }
      .onEach { syslogMsg ->

        val kafkatorioMessage = syslogMsg.message?.substringAfter("KafkatorioPacket:")

        val record = ProducerRecord<String, String>(
          TOPIC_SRC_SERVER_LOG,
          "syslog-test",
          kafkatorioMessage,
        )

        val headers = SyslogFieldKeys.values()
          .filter { "HEADER_" in it.name }
          .mapNotNull { headerKey ->
            val headerValue = syslogMsg.src[headerKey.field]
            when {
              headerValue.isNullOrBlank() -> null
              else                        -> RecordHeader(
                headerKey.field,
                headerValue.toByteArray()
              )
            }
          }

        headers.forEach { header ->
          record.headers().add(header)
        }

        producer.send(record)
      }.launchIn(builderScope)
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
            KafkaStreams.State.ERROR -> if (cont.isActive) cont.resume(newState)
            else                     -> Unit // do nothing
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
