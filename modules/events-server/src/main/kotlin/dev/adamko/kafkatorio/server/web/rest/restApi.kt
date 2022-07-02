package dev.adamko.kafkatorio.server.web.rest

import dev.adamko.kafkatorio.server.config.ApplicationProperties
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.application.log
import io.ktor.server.plugins.autohead.AutoHeadResponse
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.plugins.compression.Compression
import io.ktor.server.plugins.compression.excludeContentType
import io.ktor.server.plugins.compression.minimumSize
import io.ktor.server.plugins.conditionalheaders.ConditionalHeaders
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.partialcontent.PartialContent
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.resources.Resources
import io.ktor.server.response.respondText
import io.ktor.server.routing.routing


fun Application.webmapTileServer(appProps: ApplicationProperties) {

  install(AutoHeadResponse)
  install(CallLogging)
  install(Compression) {
    default()
    minimumSize(1024)
    excludeContentType(ContentType.Video.Any)
  }
  install(ConditionalHeaders)
  install(ContentNegotiation) {
    json()
  }
  install(PartialContent)
  install(Resources)

  install(StatusPages) {
    exception<Throwable> { call, cause ->
      this@webmapTileServer.log.error("$call caused exception - returning 500", cause)
      call.respondText(text = "500: $cause", status = HttpStatusCode.InternalServerError)
    }
  }

  routing {
    factorioServersRoutes(appProps)
  }
}
