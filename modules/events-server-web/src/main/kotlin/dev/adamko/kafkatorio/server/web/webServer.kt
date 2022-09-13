package dev.adamko.kafkatorio.server.web


import dev.adamko.kafkatorio.processor.config.ApplicationProperties
import dev.adamko.kafkatorio.server.web.rest.webmapTileServer
import dev.adamko.kafkatorio.server.web.websocket.WebmapWebsocketServer
import dev.adamko.kafkatorio.server.web.websocket.webmapWebsocketServer
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import kotlinx.coroutines.coroutineScope


internal suspend fun startWebServer(
  appProps: ApplicationProperties,
  wsServer: WebmapWebsocketServer,
): Unit = coroutineScope {

  embeddedServer(
    factory = CIO,
    port = appProps.webPort.value,
  ) {

    webmapWebsocketServer(wsServer)

    webmapTileServer(appProps)

  }.start(wait = true)
}
