package dev.adamko.kafkatorio.server.web.rest

import dev.adamko.kafkatorio.schema.common.FactorioServerId
import dev.adamko.kafkatorio.schema.common.PlayerIndex
import dev.adamko.kafkatorio.schema.common.SurfaceIndex
import dev.adamko.kafkatorio.schema.common.TilePngFilename
import dev.adamko.kafkatorio.server.config.ApplicationProperties
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.EntityTagVersion
import io.ktor.http.content.versions
import io.ktor.resources.Resource
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.application
import io.ktor.server.application.call
import io.ktor.server.application.log
import io.ktor.server.http.content.LastModifiedVersion
import io.ktor.server.resources.get
import io.ktor.server.response.respond
import io.ktor.server.response.respondFile
import io.ktor.server.routing.Routing
import io.ktor.util.combineSafe
import io.ktor.util.pipeline.PipelineContext
import kotlin.io.path.Path
import kotlinx.serialization.Serializable


fun Routing.factorioServersRoutes(
  appProps: ApplicationProperties,
) {

  get<FactorioServers> {
    call.respond(HttpStatusCode.NotImplemented)
  }

  get<FactorioServer.MapData.Tile> { tile ->
    handleTileRequest(tile, appProps)
  }
}


private suspend fun PipelineContext<Unit, ApplicationCall>.handleTileRequest(
  tile: FactorioServer.MapData.Tile,
  appProps: ApplicationProperties,
) {

  val tileFilename = TilePngFilename(
    tile.serverId,
    tile.surfaceIndex,
    tile.zoomLevel,
    tile.chunkX,
    tile.chunkY,
  )

  val tileFile = appProps.serverDataDir.combineSafe(Path(tileFilename.value))

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


@Serializable
@Resource("kafkatorio/data/servers")
private class FactorioServers


@Serializable
@Resource("kafkatorio/data/servers/{factorioServerId}")
data class FactorioServer(
  val factorioServerId: FactorioServerId,
) {

  @Serializable
  @Resource("map")
  data class MapData(
    val server: FactorioServer,
  ) {

    @Serializable
    @Resource("tiles/s{surfaceIndex}/z{zoomLevel}/x{chunkX}/y{chunkY}.png")
    data class Tile(
      val mapData: MapData,
      val surfaceIndex: SurfaceIndex,
      val zoomLevel: Int,
      val chunkX: Int,
      val chunkY: Int,
    )

    @Serializable
    @Resource("buildings/s{surfaceIndex}")
    data class Buildings(
      val mapData: MapData,
      val surfaceIndex: SurfaceIndex,
      // a list of buildings... (Sealed interface, different types?)
    )
  }

  @Serializable
  @Resource("players")
  data class Players(
    val server: FactorioServer,
  )

  @Serializable
  @Resource("{playerId}")
  data class Player(
    val server: FactorioServer,
    val playerIndex: PlayerIndex,
  )
}


private val FactorioServer.MapData.Tile.serverId: FactorioServerId
  get() = mapData.server.factorioServerId
