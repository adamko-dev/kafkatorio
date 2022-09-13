package dev.adamko.kafkatorio.server.processor

//import dev.adamko.kafkatorio.schema.common.FactorioServerId
//import dev.adamko.kafkatorio.schema.common.PrototypeHashCode
//import dev.adamko.kafkatorio.schema.common.ServerMapChunkId
//import dev.adamko.kafkatorio.schema.common.ServerMapTileLayer
//import dev.adamko.kafkatorio.schema.packets.MapChunkEntityUpdate
//import dev.adamko.kafkatorio.schema.packets.MapChunkResourceUpdate
//import dev.adamko.kafkatorio.schema.packets.PrototypesUpdate
//import dev.adamko.kafkatorio.server.config.ApplicationProperties
//import dev.adamko.kafkatorio.server.processor.topology.ServerMapChunkTiles
//import dev.adamko.kafkatorio.server.processor.topology.broadcastToWebsocket
//import dev.adamko.kafkatorio.server.processor.topology.colourEntityChunks
//import dev.adamko.kafkatorio.server.processor.topology.colourMapChunks
//import dev.adamko.kafkatorio.server.processor.topology.convertEntityUpdateToServerMapChunkTiles
//import dev.adamko.kafkatorio.server.processor.topology.convertResourceUpdateToServerMapChunkTiles
//import dev.adamko.kafkatorio.server.processor.topology.factorioServerPacketStream
//import dev.adamko.kafkatorio.server.processor.topology.saveMapTiles
//import dev.adamko.kafkatorio.server.processor.topology.splitFactorioServerPacketStream
//import dev.adamko.kafkatorio.server.processor.topology.streamPacketData
//import dev.adamko.kafkatorio.server.socket.SyslogSocketServer
//import dev.adamko.kafkatorio.server.web.websocket.WebmapWebsocketServer
//import java.util.Properties
//import kotlin.coroutines.resume
//import kotlin.coroutines.resumeWithException
//import kotlin.io.path.Path
//import kotlin.time.Duration.Companion.milliseconds
//import kotlin.time.Duration.Companion.seconds
//import kotlinx.coroutines.CoroutineName
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.cancel
//import kotlinx.coroutines.currentCoroutineContext
//import kotlinx.coroutines.job
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.suspendCancellableCoroutine
//import org.apache.kafka.clients.producer.ProducerConfig
//import org.apache.kafka.streams.KafkaStreams
//import org.apache.kafka.streams.StreamsBuilder
//import org.apache.kafka.streams.StreamsConfig
//import org.apache.kafka.streams.Topology
//import org.apache.kafka.streams.TopologyDescription
//import org.apache.kafka.streams.kstream.KStream


internal class KafkatorioTopology(
//  private val appProps: ApplicationProperties,
//  private val wsServer: WebmapWebsocketServer,
//  private val syslogServer: SyslogSocketServer,
//  private val kafkatorioPacketKafkaProducer: KafkatorioPacketKafkaProducer,
) {

//  private val builderScope = CoroutineScope(CoroutineName("KafkaTopology"))


//  suspend fun start() = builderScope.launch {
//    splitPackets()
//
//    groupTilesMapChunks()
//
////    groupEntitiesMapChunks(
////      layer = ServerMapTileLayer.BUILDING,
////      colouredUpdatesStreamTopic = TOPIC_MAP_CHUNK_BUILDING_COLOURED_032_UPDATES,
////      colouredChunkStateTopic = TOPIC_MAP_CHUNK_BUILDING_COLOURED_STATE,
////    )
//
////    groupEntitiesMapChunks(
////      layer = ServerMapTileLayer.RESOURCE,
////      colouredUpdatesStreamTopic = TOPIC_MAP_CHUNK_RESOURCE_COLOURED_032_UPDATES,
////      colouredChunkStateTopic = TOPIC_MAP_CHUNK_RESOURCE_COLOURED_STATE,
////    )
//
//    saveTiles()
//
//    websocketBroadcaster()
//
//    syslogProducer()
//  }


//  private suspend fun splitPackets() {
//    val builder = StreamsBuilder()
//    val packets = factorioServerPacketStream(builder)
//    splitFactorioServerPacketStream(packets)
//
//    val topology = builder.build()
//
//    launchTopology("splitPackets", topology)
//  }


//  /** Terrain tiles */
//  private suspend fun groupTilesMapChunks() {
//    val builder = StreamsBuilder()
//
//    val protosStream: KStream<FactorioServerId, PrototypesUpdate> =
//      builder.streamPacketData()
//
//    val topology = colourMapChunks(builder, protosStream)
//
//    launchTopology("groupTilesMapChunks.${ServerMapTileLayer.TERRAIN.dir}", topology)
//  }

//
//  /** Building and resource tiles */
//  private suspend fun groupEntitiesMapChunks(
//    layer: ServerMapTileLayer,
//    colouredUpdatesStreamTopic: String,
//    colouredChunkStateTopic: String,
//  ) {
//    val builder = StreamsBuilder()
//
//    val entityChunksStream: KStream<ServerMapChunkId, ServerMapChunkTiles<PrototypeHashCode>> =
//      when (layer) {
//        ServerMapTileLayer.TERRAIN  -> return
//
//        ServerMapTileLayer.RESOURCE ->
//          builder.streamPacketData<MapChunkResourceUpdate>()
//            .convertResourceUpdateToServerMapChunkTiles("groupEntitiesMapChunks.${layer.dir}")
//
//        ServerMapTileLayer.BUILDING ->
//          builder.streamPacketData<MapChunkEntityUpdate>()
//            .convertEntityUpdateToServerMapChunkTiles("groupEntitiesMapChunks.${layer.dir}")
//      }
//
//    val protosStream: KStream<FactorioServerId, PrototypesUpdate> =
//      builder.streamPacketData()
//
//    val topology = colourEntityChunks(
//      builder,
//      entityChunksStream,
//      protosStream,
//      layer,
//      colouredUpdatesStreamTopic,
//      colouredChunkStateTopic,
//    )
//
//    launchTopology("groupTilesMapChunks.${layer.dir}", topology)
//  }


//  private suspend fun saveTiles() {
//    val builder = StreamsBuilder()
//    val topology = saveMapTiles(builder, appProps.serverDataDir)
//    launchTopology("saveTiles", topology)
//  }


//  private suspend fun websocketBroadcaster() {
//    val builder = StreamsBuilder()
//    broadcastToWebsocket(wsServer, builder)
//
//    val topology = builder.build()
//
//    launchTopology(
//      "websocketBroadcaster", topology, mapOf(
//        StreamsConfig.COMMIT_INTERVAL_MS_CONFIG to "${1.seconds.inWholeMilliseconds}",
//        StreamsConfig.producerPrefix(ProducerConfig.LINGER_MS_CONFIG) to "${1.milliseconds.inWholeMilliseconds}"
//      )
//    )
//  }


//  /** Receive log messages from the syslog socket, and send them to a Kafka topic */
//  private fun syslogProducer() {
//    builderScope.launch {
//      kafkatorioPacketKafkaProducer.launch()
//    }
//  }


//  private suspend fun launchTopology(
//    id: String,
//    topology: Topology,
//    additionalProperties: Map<String, String> = mapOf(),
//  ) {
//
//    val props: Properties = appProps.kafkaStreamsConfig.toProperties()
//
//    additionalProperties.forEach { (k, v) -> props.setProperty(k, v) }
//
//    val appId = props.compute(StreamsConfig.APPLICATION_ID_CONFIG) { _, v -> "$v.$id" } as String
//    props.setProperty(StreamsConfig.APPLICATION_ID_CONFIG, appId)
//
//    builderScope.launch(CoroutineName(appId)) {
//
//      val streamsState: KafkaStreams.State = suspendCancellableCoroutine { cont ->
//
//        val streams = KafkaStreams(topology, props)
//        streams.setUncaughtExceptionHandler(StreamsExceptionHandler { e ->
//          cont.resumeWithException(e)
//        })
//
//        streams.setStateListener { newState: KafkaStreams.State, _ ->
//          when (newState) {
//            KafkaStreams.State.NOT_RUNNING,
//            KafkaStreams.State.ERROR -> if (cont.isActive) cont.resume(newState)
//
//            else                     -> Unit // do nothing
//          }
//        }
//
//        cont.invokeOnCancellation {
//          println("[$appId] coroutine cancelled -> closing. cause:$it")
//          streams.close()
//        }
//
//        printTopologyDescription(appId, topology)
//        streams.start()
//        println("launched Topology $appId")
//      }
//      println("exiting Topology $appId: $streamsState")
//      currentCoroutineContext().job.cancel("$appId: $streamsState")
//    }
//  }


//  private fun printTopologyDescription(appId: String, topology: Topology) {
//    val description: TopologyDescription = topology.describe()
//
//    val descFile = Path("./build/$appId.txt").toFile().apply {
//      parentFile.mkdirs()
//      if (!exists()) createNewFile()
//      writeText(description.toString())
//    }
//
//    println(
//      """
//        |----------------
//        |$appId - ${descFile.canonicalPath}
//        |$description
//        |----------------
//      """.trimMargin()
//    )
//  }


//  companion object {
//    private fun Map<String, String>.toProperties(): Properties =
//      entries.fold(Properties()) { props, (k, v) ->
//        props.apply { setProperty(k, v) }
//      }
//  }
}
