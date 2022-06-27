package dev.adamko.kafkatorio.processor


import dev.adamko.kafkatorio.processor.admin.KafkatorioKafkaAdmin
import dev.adamko.kafkatorio.processor.config.ApplicationProperties
import dev.adamko.kafkatorio.processor.tileserver.SyslogSocketServer
import dev.adamko.kafkatorio.processor.tileserver.WebMapTileServer
import dev.adamko.kafkatorio.processor.tileserver.WebsocketServer
import dev.adamko.kafkatorio.processor.tileserver.webServer
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import org.http4k.server.Http4kServer


suspend fun main() {

  val appProps = ApplicationProperties.load()

  val admin = KafkatorioKafkaAdmin(appProps)
  admin.createKafkatorioTopics()


  val syslogServer = SyslogSocketServer(appProps)
  val wsServer = WebsocketServer()
  val tileServer = WebMapTileServer(appProps)
  val topology = KafkatorioTopology(appProps, wsServer, syslogServer)

  coroutineScope {
    launch {
      syslogServer.start()
    }

    launch {
      topology.start()

      println("launched KafkatorioTopology")

      awaitCancellation()
    }

    launch {
      val webServer: Http4kServer = webServer(tileServer, wsServer, appProps)

      currentCoroutineContext().job.invokeOnCompletion {
        println("webserver stopping. Cause: $it")
        webServer.stop()
      }

      webServer.start()
      println("launched webserver port:${webServer.port()}")

      awaitCancellation()
    }
  }
}
