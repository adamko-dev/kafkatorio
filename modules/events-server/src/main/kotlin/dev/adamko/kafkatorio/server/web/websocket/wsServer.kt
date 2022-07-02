package dev.adamko.kafkatorio.server.web.websocket

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.routing.routing
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.WebSocketDeflateExtension
import java.util.zip.Deflater


fun Application.webmapWebsocketServer(
  wsServer: WebmapWebsocketServer,
) {
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

  routing {
    // TODO only send a ws client message for a single Factorio-sever. Make the URL server specific.
    //      /kafkatorio/data/servers/{serverId}/ws
    webSocket("kafkatorio/ws") {
      wsServer.bindClient(this)
    }
  }
}
