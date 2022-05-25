package dev.adamko.kafkatorio.schema.common


import dev.adamko.kafkatorio.schema.common.MapTileDictionary.PrototypeKey
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

const val MAP_CHUNK_SIZE = 32


@Serializable
data class MapTile(
  val x: Int,
  val y: Int,
  val proto: PrototypeName,
)


/**
 * 2D map of x/y tile positions and prototype-name, optimised for JSON size.
 *
 * [protos] is a one-to-one map between a [PrototypeName] and [PrototypeKey].
 *
 * [PrototypeKey] is an arbitrary integer value, which maps to one [PrototypeName] only for a
 * specific instance of [MapTileDictionary].
 *
 * Each X/Y coordinate in [tilesXY] maps to a [PrototypeKey].
 */
@Serializable
data class MapTileDictionary(
  /** Map an X,Y coordinate a prototype name */
  val tilesXY: Map<String, Map<String, PrototypeKey>>,
  val protos: Map<PrototypeName, PrototypeKey>,
) {
  private val protosIndexToName: Map<PrototypeKey, PrototypeName> by lazy {
    protos.entries.associate { (name, key) -> key to name }
  }

  fun toMapTileList(): List<MapTile> = buildList {
    tilesXY.forEach { (xString, row) ->
      row.forEach { (yString, protoIndex) ->
        val x = xString.toIntOrNull()
        val y = yString.toIntOrNull()
        val protoName = protosIndexToName[protoIndex]
        if (x != null && y != null && protoName != null) {
          add(MapTile(x, y, protoName))
        } else if (protoName == null) {
          println("warning: no prototype name found for index:$protoIndex. $protosIndexToName")
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


@Serializable
@JvmInline
value class TilePngFilename(
  val value: String,
) {
  constructor(id: ServerMapChunkId) : this(buildString {
    append("s${id.surfaceIndex}")
    append("/z${id.chunkSize.zoomLevel}")
    append("/x${id.chunkPosition.x}")
    append("/y${id.chunkPosition.y}")
    append(".png")
  })
}
