package dev.adamko.kafkatorio.server.processor


import dev.adamko.kafkatorio.server.config.ApplicationProperties
import dev.adamko.kafkatorio.server.processor.admin.KafkatorioKafkaAdmin
import dev.adamko.kafkatorio.server.security.Authenticator
import dev.adamko.kafkatorio.server.socket.SyslogSocketServer
import dev.adamko.kafkatorio.server.web.websocket.WebmapWebsocketServer
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


suspend fun eventsProcessor(
  appProps: ApplicationProperties,
  syslogServer: SyslogSocketServer,
  wsServer: WebmapWebsocketServer,
  authenticator: Authenticator,
): Unit = coroutineScope {

  val admin = KafkatorioKafkaAdmin(appProps)
  admin.createKafkatorioTopics()

  val kafkatorioPacketKafkaProducer = KafkatorioPacketKafkaProducer(appProps, syslogServer, authenticator)

  val topology = KafkatorioTopology(appProps, wsServer, syslogServer, kafkatorioPacketKafkaProducer)

  launch {
    topology.start()

    println("launched KafkatorioTopology")

    awaitCancellation()
  }
}
