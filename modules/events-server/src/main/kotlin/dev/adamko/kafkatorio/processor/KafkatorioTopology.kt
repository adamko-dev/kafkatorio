package dev.adamko.kafkatorio.processor

import dev.adamko.kafkatorio.processor.topology.factorioServerPacketStream
import dev.adamko.kafkatorio.processor.topology.groupTilesIntoChunksWithColours
import dev.adamko.kafkatorio.processor.topology.playerUpdatesToWsServer
import dev.adamko.kafkatorio.processor.topology.saveMapTiles
import dev.adamko.kafkatorio.processor.topology.splitFactorioServerPacketStream
import dev.adamko.kafkatorio.processor.topology.tileProtoColourDictionary
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

class KafkatorioTopology(
  private val websocketServer: WebsocketServer,
) : CoroutineScope {

  override val coroutineContext: CoroutineContext =
    Dispatchers.Default + SupervisorJob() + CoroutineName("KafkatorioTopology")

  companion object {
    const val sourceTopic = "factorio-server-log"
  }

  fun start() {
    saveTiles()
    splitPackets()
    playerUpdates()
  }

  private fun saveTiles() {
    val builder = StreamsBuilder()
    val packets = factorioServerPacketStream(builder)
    val tileProtoColourDict = tileProtoColourDictionary(packets)

    val tilePrototypesTable = groupTilesIntoChunksWithColours(
      packets,
      tileProtoColourDict,
    )
    saveMapTiles(tilePrototypesTable)

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

    val props =  appProps.kafkaConfig

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
