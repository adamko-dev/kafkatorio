package dev.adamko.kafkatorio.server

import dev.adamko.kafkatorio.processor.config.ApplicationProperties
import dev.adamko.kafkatorio.schema.common.FactorioServerId
import dev.adamko.kafkatorio.schema.common.PlayerIndex
import dev.adamko.kafkatorio.schema.common.SurfaceIndex
import dev.adamko.kafkatorio.schema.common.TilePngFilename
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.EntityTagVersion
import io.ktor.http.content.versions
import io.ktor.resources.Resource
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.http.content.LastModifiedVersion
import io.ktor.server.plugins.autohead.AutoHeadResponse
import io.ktor.server.plugins.compression.Compression
import io.ktor.server.plugins.compression.excludeContentType
import io.ktor.server.plugins.compression.minimumSize
import io.ktor.server.plugins.conditionalheaders.ConditionalHeaders
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.partialcontent.PartialContent
import io.ktor.server.resources.Resources
import io.ktor.server.resources.get
import io.ktor.server.response.respond
import io.ktor.server.response.respondFile
import io.ktor.server.routing.routing
import io.ktor.util.combineSafe
import io.ktor.util.pipeline.PipelineContext
import kotlin.io.path.Path
import kotlinx.serialization.Serializable


fun Application.webmapTileServer(appProps: ApplicationProperties) {

  install(AutoHeadResponse)
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

  val server = WebmapTileServer(appProps)

  routing {
    with(server) {

      get<FactorioServers.FactorioServer.MapData.Tile> { tile ->
        handleTileRequest(tile)
      }

      get<FactorioServers> {

      }
    }
  }
}


private class WebmapTileServer(
  private val appProps: ApplicationProperties
) {
  private val serverDataDir by appProps::serverDataDir

  suspend fun PipelineContext<Unit, ApplicationCall>.handleTileRequest(
    tile: FactorioServers.FactorioServer.MapData.Tile
  ) {

    val tileFilename = TilePngFilename(
      tile.serverId,
      tile.surfaceIndex,
      tile.zoomLevel,
      tile.chunkX,
      tile.chunkY,
    )

    val tileFile = serverDataDir.combineSafe(Path(tileFilename.value))

    if (tileFile.exists()) {
      call.respondFile(tileFile) {
        versions = versions + listOf(
          EntityTagVersion(tileFile.lastModified().hashCode().toString()),
          LastModifiedVersion(tileFile.lastModified()),
        )
      }
    } else {
      call.respond(HttpStatusCode.NotFound)
    }
  }
}


@Serializable
@Resource("servers")
private class FactorioServers {

  @Serializable
  @Resource("{factorioServerId}")
  data class FactorioServer(
    val factorioServerId: FactorioServerId,
  ) {

    @Serializable
    @Resource("map")
    class MapData {

      @Serializable
      @Resource("tiles/s{surfaceIndex}/z{zoomLevel}/x{chunkX}/y{chunkY}.png")
      data class Tile(
        val parent: FactorioServer,
        val surfaceIndex: SurfaceIndex,
        val zoomLevel: Int,
        val chunkX: Int,
        val chunkY: Int,
      ) {
        val serverId: FactorioServerId by parent::factorioServerId
      }

      @Serializable
      @Resource("buildings/s{surfaceIndex}")
      data class Buildings(
        val surfaceIndex: SurfaceIndex,
        // a list of buildings... (Sealed interface, different types?)
      )

    }

    @Serializable
    @Resource("players")
    class Players {

      @Serializable
      @Resource("{playerId}")
      data class Id(
        val playerIndex: PlayerIndex,
      )
    }
  }
}
