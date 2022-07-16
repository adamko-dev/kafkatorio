package dev.adamko.kafkatorio.server.web.websocket

import io.ktor.websocket.CloseReason
import io.ktor.websocket.DefaultWebSocketSession
import io.ktor.websocket.Frame
import io.ktor.websocket.send
import java.util.concurrent.atomic.AtomicInteger
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TimeSource
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield


class WebmapWebsocketServer {

  private val startTime = TimeSource.Monotonic.markNow()

  private val clients: MutableStateFlow<List<WSConnection>> = MutableStateFlow(emptyList())

  private val messagesToClients: MutableSharedFlow<String> = MutableSharedFlow(
    extraBufferCapacity = Int.MAX_VALUE,
    onBufferOverflow = BufferOverflow.DROP_OLDEST
  )

  private val messagesFromClients: MutableSharedFlow<Frame> = MutableSharedFlow(
    extraBufferCapacity = Int.MAX_VALUE,
    onBufferOverflow = BufferOverflow.DROP_OLDEST
  )


  suspend fun bindClient(session: DefaultWebSocketSession): Unit = coroutineScope {
    val client = WSConnection(session)

    clients.update { clients -> clients + client }

    client.coroutineContext.job.invokeOnCompletion { e ->
      log("client ${client.name} completed ${e ?: "successfully"}")
      clients.update { clients ->
        (clients - client).filter { it.isActive }
      }
    }

    messagesToClients.onEach { msg ->
      runCatching {
        if (client.isActive) {
          client.outgoing.send(Frame.Text(msg))
        }
      }
    }.launchIn(client)

    client.launch {
      client.incoming.receiveAsFlow().collect(messagesFromClients)
    }

    client.launch {
      while (isActive) {
        runCatching {
          client.send("elapsed ${startTime.elapsedNow()}")
        }
        delay(10.seconds)
        yield()
      }
    }

    log("client ${client.name} is up and running...")
    val closedReason: CloseReason? = client.closeReason.await()

    log("client ${client.name} closed ${closedReason ?: "successfully"}")

    coroutineContext.cancel()
  }


  suspend fun sendMessageToClients(msg: String) {
    log("sending message to clients: $msg")
    messagesToClients.emit(msg)
  }


  private data class WSConnection(
    private val session: DefaultWebSocketSession,
  ) : DefaultWebSocketSession by session {
    val name: String = "user${lastId.getAndIncrement()}"

    override suspend fun send(frame: Frame): Unit = runCatching {
      super.send(frame)
    }.fold(
      onSuccess = {
//        log("sent message to client $name: $frame")
      },
      onFailure = { e -> log("failed to send message to client $name: $e") }
    )

    override fun equals(other: Any?): Boolean = name == other
    override fun hashCode(): Int = name.hashCode()

    companion object {
      private val lastId = AtomicInteger(0)
    }
  }


  companion object {

    private fun log(msg: String) = println("[WebmapWebsocketServer] $msg")

  }
}
