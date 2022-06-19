package dev.adamko.kafkatorio.processor.tileserver

import dev.adamko.kafkatorio.processor.config.ApplicationProperties
import org.http4k.server.Http4kServer
import org.http4k.server.PolyHandler
import org.http4k.server.Undertow
import org.http4k.server.asServer

internal suspend fun webServer(
  tileServer: WebMapTileServer,
  wsServer: WebsocketServer,
  appProps: ApplicationProperties,
): Http4kServer = PolyHandler(
  tileServer.build(),
  wsServer.build(),
).asServer(Undertow(appProps.webPort()))
