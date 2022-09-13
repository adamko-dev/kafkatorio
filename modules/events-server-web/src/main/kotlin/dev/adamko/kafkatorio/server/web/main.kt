package dev.adamko.kafkatorio.server.web

import dev.adamko.kafkatorio.processor.config.ApplicationProperties
import dev.adamko.kafkatorio.processor.core.launchTopology
import dev.adamko.kafkatorio.server.processor.topology.broadcastToWebsocket
import dev.adamko.kafkatorio.server.processor.topology.saveMapTiles
import dev.adamko.kafkatorio.server.web.websocket.WebmapWebsocketServer
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.StreamsConfig

suspend fun main(): Unit = coroutineScope {
  val appProps = ApplicationProperties.load()
  val wsServer = WebmapWebsocketServer()

  launch {
    websocketBroadcaster(wsServer)
  }

//  launch {
//    broadcastToWebsocket(wsServer, StreamsBuilder())
//  }

  launch {
    saveTiles(appProps)
  }

  launch {
    startWebServer(
      appProps = appProps,
      wsServer = wsServer,
    )
  }
}

private suspend fun websocketBroadcaster(wsServer: WebmapWebsocketServer) {
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

private suspend fun saveTiles(
  appProps: ApplicationProperties
) {
  val builder = StreamsBuilder()
  saveMapTiles(builder, appProps.serverDataDir)
  launchTopology("saveTiles", builder.build())
}
