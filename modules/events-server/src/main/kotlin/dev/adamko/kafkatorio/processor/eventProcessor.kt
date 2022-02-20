package dev.adamko.kafkatorio.processor


import dev.adamko.kafkatorio.processor.admin.KafkatorioKafkaAdmin
import dev.adamko.kafkatorio.processor.tileserver.WebMapTileServer
import kotlinx.coroutines.runBlocking
import org.http4k.server.PolyHandler
import org.http4k.server.Undertow
import org.http4k.server.asServer


//private val appProps = ApplicationProperties()


fun main(): Unit = runBlocking {

  KafkatorioKafkaAdmin.createKafkatorioTopics()

  val wsServer = WebsocketServer()
  val tileServer = WebMapTileServer()

  val topology = KafkatorioTopology(wsServer)
  topology.start()

  val webServer = PolyHandler(
    tileServer.build(),
    wsServer.build(),
  ).asServer(Undertow(9073))

  Runtime.getRuntime().addShutdownHook(Thread {
    webServer.stop()
  })
//  launch {
  webServer.start().block()
//  }

}
