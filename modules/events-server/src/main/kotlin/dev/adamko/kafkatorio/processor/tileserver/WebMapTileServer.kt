package dev.adamko.kafkatorio.processor.tileserver

import org.http4k.core.HttpHandler
import org.http4k.routing.ResourceLoader
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.routing.static

class WebMapTileServer {

  private val routes: RoutingHttpHandler = routes(
    "/tiles" bind static(ResourceLoader.Classpath("/kafkatorio-web-map"))
  )

  fun build(): HttpHandler {
    return routes
  }
}
