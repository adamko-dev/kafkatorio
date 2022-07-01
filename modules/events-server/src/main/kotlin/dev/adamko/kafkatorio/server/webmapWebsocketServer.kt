package dev.adamko.kafkatorio.server

import dev.adamko.kafkatorio.processor.config.ApplicationProperties
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.routing.routing
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.DefaultWebSocketSession
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketDeflateExtension
import io.ktor.websocket.send
import java.util.concurrent.atomic.AtomicInteger
import java.util.zip.Deflater
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TimeSource
import kotlinx.coroutines.channels.BufferOverflow
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


fun Application.webmapWebsocketServer() {
  install(WebSockets) {
    extensions {
      install(WebSocketDeflateExtension) {
        // Compression level to use for java.util.zip.Deflater
        compressionLevel = Deflater.DEFAULT_COMPRESSION

        // Prevent compressing small outgoing frames
        compressIfBiggerThan(bytes = 4 * 1024)
      }
    }
  }

  val wsServer = WebmapWebsocketServer()

  routing {

    webSocket("/ws") {
      wsServer.bindClient(this)
    }
  }
}


private class WebmapWebsocketServer {

  private val startTime = TimeSource.Monotonic.markNow()

  private val clients: MutableStateFlow<Set<WSConnection>> = MutableStateFlow(emptySet())

  private val messagesToClients: MutableSharedFlow<String> = MutableSharedFlow(
    extraBufferCapacity = Int.MAX_VALUE,
    onBufferOverflow = BufferOverflow.DROP_OLDEST
  )

  private val messagesFromClients: MutableSharedFlow<Frame> = MutableSharedFlow(
    extraBufferCapacity = Int.MAX_VALUE,
    onBufferOverflow = BufferOverflow.DROP_OLDEST
  )

  suspend fun bindClient(session: DefaultWebSocketSession) {
    val client = WSConnection(session)

    clients.update { clients -> clients + client }

    messagesToClients.onEach { msg ->
      runCatching {
        client.send(msg)
      }
    }.launchIn(client)

    client.launch {
      client.incoming.receiveAsFlow()
        .collect(messagesFromClients)
    }

    client.launch {
      while (isActive) {
        runCatching {
          client.send("elapsed ${startTime.elapsedNow()}")
        }
        delay(10.seconds)
      }
    }

    client.coroutineContext.job.invokeOnCompletion {
      log("client ${client.name} completed")
      clients.update { clients -> clients - client }
    }
  }


  suspend fun sendMessageToClients(msg: String) {
    log("sending message to clients: $msg")
    messagesToClients.emit(msg)
  }
}


private data class WSConnection(
  private val session: DefaultWebSocketSession,
) : DefaultWebSocketSession by session {
  val name: String = "user${lastId.getAndIncrement()}"

  override suspend fun send(frame: Frame): Unit = runCatching {
    super.send(frame)
  }.fold(
    onSuccess = { log("sent message to client $name: $frame") },
    onFailure = { log("failed to send message to client $name: $frame") }
  )

  override fun equals(other: Any?): Boolean = name == other
  override fun hashCode(): Int = name.hashCode()

  companion object {
    private val lastId = AtomicInteger(0)
  }
}


private fun log(msg: String) = println("[WebmapWebsocketServer] $msg")
