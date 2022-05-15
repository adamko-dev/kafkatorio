package dev.adamko.kafkatorio.processor.tileserver

import dev.adamko.kafkatorio.processor.config.ApplicationProperties
import java.security.MessageDigest
import kotlin.io.path.absolutePathString
import org.http4k.core.Filter
import org.http4k.core.HttpHandler
import org.http4k.core.MemoryBody
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.then
import org.http4k.routing.ResourceLoader
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.routing.static

internal class WebMapTileServer(
  private val appProps: ApplicationProperties = ApplicationProperties()
) {

  private val tilesLoader = ResourceLoader.Directory(appProps.webmapTileDir.absolutePathString())

  private val filters = listOf(
    ETagChecker()(),
  ).reduce(Filter::then)

  private val routes: RoutingHttpHandler = routes(
    "/tiles" bind GET to
        static(tilesLoader).withFilter(filters)
  )

  fun build(): HttpHandler {
    return routes
  }


  inner class ETagChecker
//    : CoroutineScope
  {

//    private val tagState: MutableStateFlow<MutableMap<String, String>> =
//      MutableStateFlow(mutableMapOf())
//
//    override val coroutineContext: CoroutineContext =
//      CoroutineName("ETagChecker") +
//          Dispatchers.Unconfined +
//          KafkatorioTopology.rootJob

//    init {
//      launch {
//        appProps.webmapTileDir
//          .fileEventsFlow()
//          .filterIsInstance<KWatchEvent.PathEvent>()
//          .filter { it.path.isRegularFile() }
////          .filter { it.path.extension == "png" }
//          .runningFold(mutableMapOf<String, String>()) { tags, event ->
////            println("[WebMapTileServer] handling file update ${event.path}")
//
//            val relativePath = event.path
//              .relativeTo(appProps.webmapTileDir)
//              .invariantSeparatorsPathString
//
//            when (event) {
//              is KWatchEvent.Deleted  -> tags.remove(relativePath)
//              is KWatchEvent.Created,
//              is KWatchEvent.Modified -> {
//                val md5 = event.path.md5()
//                if (md5 != null) {
//                  tags[relativePath] = md5
//                } else {
//                  println("null md5 for ${event.path}")
//                }
//              }
//            }
//
//            tags
//          }.cancellable()
//          .collect(tagState)
//      }
//    }


    private val Request.etagKey: String get() = uri.path.removePrefix("/tiles/")


    operator fun invoke() = Filter { next ->
      { request ->
        val requestedETag = request.header("If-None-Match")

        var response = next(request)
        val responseBytes = response.body.stream.use { it.readAllBytes() }
        val currentETag = responseBytes.md5()

        response = response.body(MemoryBody(responseBytes))

        when {
          currentETag == null          -> {
//            println("[${request.etagKey}] currentETag is null (requested: $requestedETag), executing request")
            response
              .header("Cache-Control", "no-cache")
          }
          requestedETag == currentETag -> {
            println("[${request.etagKey}] currentETag == requested, returning 302 $requestedETag")
            Response(Status.NOT_MODIFIED)
              .header("Cache-Control", "no-cache")
          }
          else                         -> {
            println("[${request.etagKey}] currentETag != requested, executing request and adding currentETag:$currentETag (requested:$requestedETag)")
            response
              .header("ETag", currentETag)
              .header("Cache-Control", "public, max-age=1, stale-while-revalidate=60, stale-if-error=300")
          }
        }
      }
    }

    private fun ByteArray.md5(): String? {
      return if (isEmpty()) {
        null
      } else {
        MessageDigest.getInstance("MD5")
          .digest(this)
          .joinToString("") { "%02x".format(it) }
      }
    }
  }
}
