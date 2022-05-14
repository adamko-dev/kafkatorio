package dev.adamko.kafkatorio.processor.topology

import com.sksamuel.scrimage.color.RGBColor
import dev.adamko.kafkatorio.schema.common.ChunkSize
import dev.adamko.kafkatorio.schema.common.ColourHex
import dev.adamko.kafkatorio.schema.common.MapChunkPosition
import dev.adamko.kafkatorio.schema.common.MapTile
import dev.adamko.kafkatorio.schema.common.MapTilePosition
import dev.adamko.kafkatorio.schema.common.SurfaceIndex
import java.awt.Color
import kotlinx.serialization.Serializable


@JvmInline
@Serializable
value class FactorioServerId(private val id: String) {
  override fun toString() = id
}


@Serializable
data class ServerMapChunkId(
  val serverId: FactorioServerId,
  val chunkPosition: MapChunkPosition,
  val surfaceIndex: SurfaceIndex,
  val chunkSize: ChunkSize,
)


@Serializable
data class ServerMapChunkTiles<Data>(
  val chunkId: ServerMapChunkId,
  val map: Map<MapTilePosition, Data>,
) {
  operator fun plus(other: ServerMapChunkTiles<Data>) = copy(map = map + other.map)
}


val MapTile.position
  get() = MapTilePosition(x, y)


val TRANSPARENT_AWT: Color = ColourHex.TRANSPARENT.toRgbColor().awt()


fun ColourHex.toRgbColor(): RGBColor {
  return RGBColor(
    red.toInt(),
    green.toInt(),
    blue.toInt(),
    alpha.toInt(),
  )
}
