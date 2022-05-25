package dev.adamko.kafkatorio.processor.topology

import com.sksamuel.scrimage.color.RGBColor
import dev.adamko.kafkatorio.schema.common.ColourHex
import dev.adamko.kafkatorio.schema.common.MapTile
import dev.adamko.kafkatorio.schema.common.MapTilePosition
import dev.adamko.kafkatorio.schema.common.ServerMapChunkId
import java.awt.Color
import kotlinx.serialization.Serializable


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
