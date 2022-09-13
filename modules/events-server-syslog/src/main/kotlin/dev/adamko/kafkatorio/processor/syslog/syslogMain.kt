package dev.adamko.kafkatorio.processor.syslog

import dev.adamko.kafkatorio.processor.config.ApplicationProperties
import dev.adamko.kafkatorio.processor.core.Authenticator
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


/** Receive log messages from the syslog socket, and send them to a Kafka topic */
suspend fun main(): Unit = coroutineScope {

  val appProps = ApplicationProperties.load()
  val syslogServer = SyslogSocketServer(appProps)
  val authenticator = Authenticator(appProps)

  val producer = KafkatorioPacketKafkaProducer(appProps, syslogServer, authenticator)

  launch { syslogServer.start() }
  launch { producer.launch() }
}
