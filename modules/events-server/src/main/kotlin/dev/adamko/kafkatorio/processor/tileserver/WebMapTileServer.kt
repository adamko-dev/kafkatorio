package dev.adamko.kafkatorio.processor.tileserver

import dev.adamko.kafkatorio.processor.config.ApplicationProperties
import kotlin.io.path.absolutePathString
import org.http4k.core.HttpHandler
import org.http4k.core.then
import org.http4k.filter.CachingFilters
import org.http4k.routing.ResourceLoader
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.routing.static

internal class WebMapTileServer(
  appProps: ApplicationProperties = ApplicationProperties()
) {

  private val routes: RoutingHttpHandler = routes(
    "/tiles" bind CachingFilters.Response.NoCache()
      .then(static(ResourceLoader.Directory(appProps.webmapTileDir.absolutePathString())))
  )

  fun build(): HttpHandler {
    return routes
  }
}
