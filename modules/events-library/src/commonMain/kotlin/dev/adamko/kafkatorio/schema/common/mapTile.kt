package dev.adamko.kafkatorio.schema.common

import dev.adamko.kafkatorio.schema.common.MapTileDictionary.PrototypeKey
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName
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
 * @param[tilesXY] Map an X/Y coordinate to a [PrototypeKey].
 */
@Serializable
@SerialName("kafkatorio.packet.keyed.MapTileDictionary")
data class MapTileDictionary(
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
  value class PrototypeKey(private val index: Int) : Comparable<Int> by index

//  companion object {
//    fun MapTileDictionary?.isNullOrEmpty(): Boolean {
//      return this == null || (tilesXY.isEmpty() && tilesXY.all { it.value.isEmpty() })
//    }
//  }
}


@Serializable
data class ServerMapChunkId(
  val serverId: FactorioServerId,
  val layer: ServerMapTileLayer,
  val chunkPosition: MapChunkPosition,
  val surfaceIndex: SurfaceIndex,
) {
  val chunkSize: ChunkSize get() = chunkPosition.chunkSize
}


@Serializable
enum class ServerMapTileLayer(val dir: String) {
  @SerialName("terrain")
  TERRAIN("terrain"),
  @SerialName("resource")
  RESOURCE("resource"),
  @SerialName("building")
  BUILDING("building"),
}


/** `${serverDataDir}/servers/{serverId}/map/${layer}/s{surfaceIndex}/z{zoomLevel}/x{chunkX}/y{chunkY}.png` */
@Serializable
@JvmInline
value class ServerMapTilePngFilename(val value: String) {

  constructor(id: ServerMapChunkId) : this(
    serverId = id.serverId,
    layer = id.layer,
    surfaceIndex = id.surfaceIndex,
    zoomLevel = id.chunkSize.zoomLevel,
    chunkX = id.chunkPosition.x,
    chunkY = id.chunkPosition.y,
  )

  constructor(
    serverId: FactorioServerId,
    layer: ServerMapTileLayer,
    surfaceIndex: SurfaceIndex,
    zoomLevel: Int,
    chunkX: Int,
    chunkY: Int,
  ) : this(
    "servers/${serverId}" +
        "/map" +
        "/layers/${layer.dir}" +
        "/s${surfaceIndex}" +
        "/z${zoomLevel}" +
        "/x${chunkX}" +
        "/y${chunkY}" +
        ".png"
  )

}
