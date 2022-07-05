package dev.adamko.kafkatorio.schema.common


import dev.adamko.kafkatorio.schema.common.MapTileDictionary.PrototypeKey
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

const val MAP_CHUNK_SIZE = 32


@Serializable
data class MapTile(
  val x: Int,
  val y: Int,
  val protoId: PrototypeId,
)


/**
 * 2D map of x/y tile positions and prototype-name, optimised for JSON size.
 *
 * [protos] is a one-to-one map between a prototype[PrototypeId] and [PrototypeKey].
 *
 * [PrototypeKey] is an arbitrary integer value, which maps to one [PrototypeId] only for a
 * specific instance of [MapTileDictionary].
 *
 * Each X/Y coordinate in [tilesXY] maps to a [PrototypeKey].
 */
@Serializable
data class MapTileDictionary(
  /** Map an X,Y coordinate a prototype name */
  val tilesXY: Map<String, Map<String, PrototypeKey>>,
  val protos: Map<PrototypeId, PrototypeKey>,
) {

  private val protosIndexToId: Map<PrototypeKey, PrototypeId> by lazy {
    protos.entries.associate { (name, key) -> key to name }
  }

  fun toMapTileList(): List<MapTile> = buildList {
    tilesXY.forEach { (xString, row) ->
      row.forEach { (yString, protoIndex) ->
        val x = xString.toIntOrNull()
        val y = yString.toIntOrNull()
        val protoName = protosIndexToId[protoIndex]
        if (x != null && y != null && protoName != null) {
          add(MapTile(x, y, protoName))
        } else if (protoName == null) {
          println("warning: no prototype name found for index:$protoIndex. $protosIndexToId")
        }
      }
    }
  }

  /** An arbitrary ID for a prototype in [MapTileDictionary.protos]. */
  @Serializable
  @JvmInline
  value class PrototypeKey(private val index: Int)

  companion object {
    fun MapTileDictionary?.isNullOrEmpty(): Boolean {
      return this == null || (tilesXY.isEmpty() && tilesXY.all { it.value.isEmpty() })
    }
  }
}


@Serializable
data class MapBoundingBox(
  val topLeft: MapTilePosition,
  val bottomRight: MapTilePosition,
)


@Serializable
data class ServerMapChunkId(
  val serverId: FactorioServerId,
  val chunkPosition: MapChunkPosition,
  val surfaceIndex: SurfaceIndex,
  val chunkSize: ChunkSize,
)


/** `${serverDataDir}/servers/{serverId}/map/tiles/s{surfaceIndex}/z{zoomLevel}/x{chunkX}/y{chunkY}.png` */
@Serializable
@JvmInline
value class TilePngFilename(
  val value: String,
) {
  constructor(id: ServerMapChunkId) : this(
    id.serverId,
    id.surfaceIndex,
    id.chunkSize.zoomLevel,
    id.chunkPosition.x,
    id.chunkPosition.y,
  )

  constructor(
    serverId: FactorioServerId,
    surfaceIndex: SurfaceIndex,
    zoomLevel: Int,
    chunkX: Int,
    chunkY: Int,
  ) : this(buildString {
    append("servers/${serverId}")
    append("/map/tiles")
    append("/s${surfaceIndex}")
    append("/z${zoomLevel}")
    append("/x${chunkX}")
    append("/y${chunkY}")
    append(".png")
  })
}
