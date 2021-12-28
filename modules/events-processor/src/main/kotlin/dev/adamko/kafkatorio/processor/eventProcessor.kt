package dev.adamko.kafkatorio.processor


import dev.adamko.kafkatorio.processor.config.ApplicationProperties
import kotlinx.coroutines.runBlocking
import org.http4k.server.Undertow
import org.http4k.server.asServer

val appProps = ApplicationProperties()

fun main() = runBlocking {

  val wsServer = WebsocketServer()

  val topology = FactorioEventsTopology(wsServer)
  topology.build()

  wsServer.build().asServer(Undertow(9073)).start().block()
}
