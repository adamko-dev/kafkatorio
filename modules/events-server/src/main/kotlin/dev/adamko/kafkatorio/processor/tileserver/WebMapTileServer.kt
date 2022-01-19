package dev.adamko.kafkatorio.processor.tileserver

import org.http4k.core.HttpHandler
import org.http4k.core.then
import org.http4k.filter.CachingFilters
import org.http4k.routing.ResourceLoader
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.routing.static

class WebMapTileServer {

  private val routes: RoutingHttpHandler = routes(
    "/tiles" bind CachingFilters.Response.NoCache()
      .then(
        static(
          ResourceLoader.Directory(
            """D:\Users\Adam\Projects\games\kafkatorio\modules\events-server\src\main\resources\kafkatorio-web-map\"""
          )
        )
      )
  )

  fun build(): HttpHandler {
    return routes
  }
}
