package dev.adamko.kafkatorio.processor

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.withIndex
import kotlinx.coroutines.job
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.supervisorScope
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.routing.bind
import org.http4k.routing.websockets
import org.http4k.server.PolyHandler
import org.http4k.websocket.Websocket
import org.http4k.websocket.WsMessage
import org.http4k.websocket.WsStatus


class WebsocketServer {

  // create a scope+job for the server
  private val serverJob = SupervisorJob()
  private val serverScope = CoroutineScope(serverJob)

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

  private fun bindClient(ws: Websocket) = runBlocking(serverScope.coroutineContext) {
    clients.emit(ws)
  }

  fun sendMessage(msg: String) = runBlocking(serverScope.coroutineContext) {
    messages.emit(msg)
  }

  suspend fun start(): PolyHandler = supervisorScope builder@{

    coroutineContext.job.invokeOnCompletion { e ->
      e?.let { serverJob.completeExceptionally(it) } ?: serverJob.complete()
    }

    clients
      .withIndex()
      .onEach { (id, ws) ->
        // create a scope+job for each client
        val clientJob = SupervisorJob(serverJob)
        val clientScope = CoroutineScope(clientJob)

        clientJob.invokeOnCompletion { e ->
          println("[clientJob.invokeOnCompletion] closing client ws $e")
          when (e) {
            null -> ws.close(WsStatus.NORMAL)
            else -> ws.close(WsStatus.ABNORMAL_CLOSE)
          }
        }

        ws.onError { e ->
          sendMessage("[ws.onError] client $id error [${e}]")
          clientJob.completeExceptionally(e)
        }
        ws.onClose { status ->
          sendMessage("[ws.onClose] client $id disconnected [${status}]")
          clientJob.complete()
        }
        ws.onMessage { wsMsg ->
          sendMessage("$id says: ${wsMsg.bodyString()}")
        }

        sendMessage("new client $id!")

        messages
          .onEach { msg -> ws.send(WsMessage(msg)) }
          .launchIn(clientScope)

      }
      .launchIn(serverScope)

    val http = { _: Request -> Response(OK) }
    val ws = websockets("/ws" bind ::bindClient)

    return@builder PolyHandler(http, ws)

  }
}
