package dev.adamko.kafkatorio.server.web.rest

import dev.adamko.kafkatorio.schema.common.FactorioServerId
import dev.adamko.kafkatorio.schema.common.PlayerIndex
import dev.adamko.kafkatorio.schema.common.ServerMapTileLayer
import dev.adamko.kafkatorio.schema.common.ServerMapTilePngFilename
import dev.adamko.kafkatorio.schema.common.SurfaceIndex
import dev.adamko.kafkatorio.server.config.ApplicationProperties
import dev.adamko.kafkatorio.server.config.jsonMapper
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.EntityTagVersion
import io.ktor.http.content.versions
import io.ktor.resources.Resource
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.http.content.LastModifiedVersion
import io.ktor.server.resources.get
import io.ktor.server.response.respond
import io.ktor.server.response.respondFile
import io.ktor.server.routing.Routing
import io.ktor.util.combineSafe
import io.ktor.util.pipeline.PipelineContext
import kotlin.io.path.Path
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.encodeToJsonElement


fun Routing.factorioServersRoutes(
  appProps: ApplicationProperties,
) {

  get<FactorioServers> {
    val serverIds = appProps.kafkatorioServers.values.toSet().sortedBy { it.id }
    val serverIdsJson = jsonMapper.encodeToJsonElement(serverIds)
    call.respond(serverIdsJson)
  }

  get<FactorioServer.MapData.Tile> { tile ->
    handleTileRequest(tile, appProps)
  }
}


private suspend fun PipelineContext<Unit, ApplicationCall>.handleTileRequest(
  tile: FactorioServer.MapData.Tile,
  appProps: ApplicationProperties,
) {

  val tileFilename = ServerMapTilePngFilename(
    tile.serverId,
    tile.layer,
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
    // It's not a client or server error to request a non-existent tile, it just doesn't exist
    // (yet). So return 304.
    call.respond(HttpStatusCode.NotModified)
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
    @Resource("layers/{layer}/s{surfaceIndex}/z{zoomLevel}/x{chunkX}/y{chunkY}.png")
    data class Tile(
      val mapData: MapData,

      val layer: ServerMapTileLayer,
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
