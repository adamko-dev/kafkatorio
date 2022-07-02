package dev.adamko.kafkatorio.processor.tileserver

//import dev.adamko.kafkatorio.processor.config.ApplicationProperties
//import java.security.MessageDigest
//import org.http4k.core.Filter
//import org.http4k.core.HttpHandler
//import org.http4k.core.MemoryBody
//import org.http4k.core.Method.GET
//import org.http4k.core.Request
//import org.http4k.core.Response
//import org.http4k.core.Status
//import org.http4k.core.then
//import org.http4k.routing.ResourceLoader
//import org.http4k.routing.RoutingHttpHandler
//import org.http4k.routing.bind
//import org.http4k.routing.routes
//import org.http4k.routing.static
//
//internal class WebMapTileServer(
//  appProps: ApplicationProperties
//) {
//
//  private val tilesLoader = ResourceLoader.Directory(appProps.serverDataDir.toFile().canonicalPath)
//
//
//  private val filters = listOf(
//    ETagChecker(),
//  ).reduce(Filter::then)
//
//
//  private val routes: RoutingHttpHandler = routes(
//    "/tiles" bind GET to static(tilesLoader).withFilter(filters)
//  )
//
//
//  fun build(): HttpHandler {
//    return routes
//  }
//
//
//  private class ETagChecker : Filter {
//
//    override fun invoke(next: HttpHandler): HttpHandler = { request: Request ->
//      val requestedETag = request.header("If-None-Match")
//
//      var response = next(request)
//      val responseBytes = response.body.stream.use { it.readAllBytes() }
//      val currentETag = responseBytes.md5()
//
//      response = response.body(MemoryBody(responseBytes))
//
//      when {
//        currentETag == null          -> {
////            println("[${request.etagKey}] currentETag is null (requested: $requestedETag), executing request")
//          response
//            .header("Cache-Control", "no-cache, max-age=0, must-revalidate")
//        }
//        requestedETag == currentETag -> {
////            println("[${request.etagKey}] currentETag == requested, returning 304 $requestedETag")
//          Response(Status.NOT_MODIFIED)
//            .header("Cache-Control", "no-cache, max-age=0, must-revalidate")
//        }
//        else                         -> {
////            println("[${request.etagKey}] currentETag != requested, executing request and adding currentETag:$currentETag (requested:$requestedETag)")
//          response
//            .header("ETag", currentETag)
////              .header("Cache-Control", "no-store")
//            .header("Cache-Control", "no-cache, max-age=0, must-revalidate")
//        }
//      }
//    }
//  }
//
//
//  companion object {
//    private fun ByteArray.md5(): String? {
//      return if (isEmpty()) {
//        null
//      } else {
//        MessageDigest.getInstance("MD5")
//          .digest(this)
//          .joinToString("") { "%02x".format(it) }
//      }
//    }
//  }
//}
