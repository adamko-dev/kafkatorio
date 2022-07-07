package dev.adamko.kafkatorio.server.processor


import dev.adamko.kafkatorio.server.config.ApplicationProperties
import dev.adamko.kafkatorio.server.processor.admin.KafkatorioKafkaAdmin
import dev.adamko.kafkatorio.server.socket.SyslogSocketServer
import dev.adamko.kafkatorio.server.web.websocket.WebmapWebsocketServer
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


suspend fun eventsProcessor(
  appProps: ApplicationProperties,
  syslogServer: SyslogSocketServer,
  wsServer: WebmapWebsocketServer,
): Unit = coroutineScope {

  val admin = KafkatorioKafkaAdmin(appProps)
  admin.createKafkatorioTopics()

  val topology = KafkatorioTopology(appProps, wsServer, syslogServer)

  launch {
    topology.start()

    println("launched KafkatorioTopology")

    awaitCancellation()
  }
}
