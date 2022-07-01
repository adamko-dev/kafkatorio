package dev.adamko.kafkatorio.server

import dev.adamko.kafkatorio.processor.config.ApplicationProperties
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respondText


fun main() {
  val appProps = ApplicationProperties.load()

  embeddedServer(CIO, port = appProps.webPort.value) {

    common()

    webmapWebsocketServer()

    webmapTileServer(appProps)

    syslogSocketServer(appProps)

  }.start(wait = true)
}


fun Application.common() {
  install(StatusPages) {
    exception<Throwable> { call, cause ->
      call.respondText(text = "500: $cause", status = HttpStatusCode.InternalServerError)
    }
  }
  install(CallLogging)
}
