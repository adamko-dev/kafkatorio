package dev.adamko.kafkatorio.processor


import dev.adamko.kafkatorio.processor.config.ApplicationProperties
import dev.adamko.kafkatorio.processor.tileserver.WebMapTileServer
import kotlinx.coroutines.runBlocking
import org.http4k.server.PolyHandler
import org.http4k.server.Undertow
import org.http4k.server.asServer

val appProps = ApplicationProperties()

fun main() = runBlocking {

  val wsServer = WebsocketServer()
  val tileServer = WebMapTileServer()

  val topology = KafkatorioTopology(wsServer)
  topology.build()

  PolyHandler(
    tileServer.build(),
    wsServer.build(),
  ).asServer(Undertow(9073))
    .start()
    .block()

}
