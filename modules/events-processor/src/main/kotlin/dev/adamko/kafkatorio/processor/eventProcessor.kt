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

  wsServer.start().asServer(Undertow(9073)).start().block()
}

//
//
//// a standard websocket app
//val websocketServer = websockets(
//  "/lua-objects" bind { ws: Websocket ->
//    ws.send(WsMessage("bob"))
//    ws.onMessage {
//      println("server received: $it")
//      ws.send(it)
//    }
//  }
//).asServer(Undertow(9097)).start()
