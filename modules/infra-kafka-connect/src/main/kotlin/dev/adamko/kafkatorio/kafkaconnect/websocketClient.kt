package dev.adamko.kafkatorio.kafkaconnect

import java.time.Duration
import org.http4k.client.WebsocketClient
import org.http4k.core.Uri
import org.http4k.websocket.WsMessage

fun main() {

//  val client = WebsocketClient.blocking(
//    uri = Uri.of("ws://localhost:8095/ws"),
////    autoReconnection = true,
////    timeout = Duration.ofSeconds(30)
//  )
////  client.send(WsMessage("server sent on connection"))
//
//  client.received()
//    .take(100)
//    .forEachIndexed { index, wsMessage ->
//      println("[$index] ${wsMessage.bodyString()}")
//    }


  val nonBlockingClient = WebsocketClient.nonBlocking(
    uri = Uri.of("ws://localhost:9292/lua-objects"),
    onError = { it.printStackTrace() },
    onConnect = { ws ->
      println("onConnect, ws.upgradeRequest ${ws.upgradeRequest}")
//      ws.send(WsMessage("client sent on connection"))
    }
  )

  nonBlockingClient.onMessage { msg -> print(msg.bodyString()) }


  nonBlockingClient.onClose {
    println("non-blocking client closing")
  }


  nonBlockingClient.onError { it.printStackTrace() }


  Thread.sleep(Duration.ofMinutes(1).toMillis())

}
