package dev.adamko.kafkatorio.processor.tileserver

import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TimeSource
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.withIndex
import kotlinx.coroutines.isActive
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.suspendCancellableCoroutine
import org.http4k.routing.bind
import org.http4k.routing.websockets
import org.http4k.websocket.Websocket
import org.http4k.websocket.WsHandler
import org.http4k.websocket.WsMessage
import org.http4k.websocket.WsStatus


class WebsocketServer {

  private val serverScope = CoroutineScope(
    CoroutineName("WsServer") +
        Dispatchers.IO +
        SupervisorJob() +
        CoroutineExceptionHandler { _, e ->
          println("[ws.WsServer] handling exception ${e.message}")
        }
  )


  private val clients: MutableSharedFlow<Websocket> = MutableSharedFlow(
    replay = 100,
    extraBufferCapacity = 100,
    BufferOverflow.DROP_OLDEST,
  )


  private val messages: MutableSharedFlow<String> = MutableSharedFlow(
    replay = 100,
    extraBufferCapacity = 100,
    BufferOverflow.DROP_OLDEST,
  )


  private fun bindClient(ws: Websocket) = serverScope.launch {
    println("[ws.bindClient] ${ws.upgradeRequest.toMessage()}")
    clients.emit(ws)
  }


  fun sendMessage(msg: String) = serverScope.launch {
    println("[ws.sendMessage] $msg")
    messages.emit(msg)
  }


  fun build(): WsHandler {
    val startTime = TimeSource.Monotonic.markNow()

    serverScope.launch {
      supervisorScope {
        clients
          .withIndex()
          .onEach { (id, ws) ->
            createClient(id, ws)
          }.launchIn(serverScope)
      }
    }

    serverScope.launch {
      while (isActive) {
        delay(10.seconds)
        sendMessage("elapsed ${startTime.elapsedNow()}")
      }
    }

    return websockets(
      "/ws/foo" bind { ws: Websocket ->
        serverScope.launch { bindClient(ws) }
      }
    )
  }


  private fun createClient(id: Int, ws: Websocket) {
    val clientJob = SupervisorJob(serverScope.coroutineContext.job)

    serverScope.launch(CoroutineName("ws-client-$id") + clientJob) {
      launch {
        suspendCancellableCoroutine<WsStatus> { cont ->
          println("[ws-client] entering suspendCancellableCoroutine")

          cont.invokeOnCancellation { e: Throwable? ->
            launch { sendMessage("[ws-client.invokeOnCompletion] closing client ws ${e?.message}") }
            when (e) {
              null -> ws.close(WsStatus.NORMAL)
              else -> ws.close(WsStatus.ABNORMAL_CLOSE)
            }
          }

          ws.onError { e: Throwable ->
            launch { sendMessage("[ws-client.onError] client $id error [${e.message}]") }
            if (!cont.isCompleted) {
              cont.resumeWithException(e)
            }
          }

          ws.onClose { status: WsStatus ->
            launch { sendMessage("[ws-client.onClose] client $id disconnected [${status}]") }
            if (!cont.isCompleted) {
              cont.resume(status)
            }
          }

          ws.onMessage { wsMsg ->
            launch { sendMessage("$id says: ${wsMsg.bodyString()}") }
          }
        }
      }

      println("[ws-client] $id listening to messages")
      messages
        .onEach { msg ->
          println("[ws-client] sending message to $id")
          ws.send(WsMessage(msg))
        }.launchIn(this)
    }
  }
}
