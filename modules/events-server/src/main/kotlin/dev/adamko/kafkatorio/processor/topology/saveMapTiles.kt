package dev.adamko.kafkatorio.processor.topology

import com.sksamuel.scrimage.ImmutableImage
import com.sksamuel.scrimage.ScaleMethod
import com.sksamuel.scrimage.nio.PngWriter
import dev.adamko.kafkatorio.processor.admin.TOPIC_BROADCAST_TO_WEBSOCKET
import dev.adamko.kafkatorio.processor.admin.TOPIC_MAP_CHUNK_COLOURED_STATE
import dev.adamko.kafkatorio.processor.serdes.jsonMapper
import dev.adamko.kafkatorio.processor.serdes.kxsBinary
import dev.adamko.kafkatorio.schema.common.ColourHex
import dev.adamko.kafkatorio.schema.common.FactorioServerId
import dev.adamko.kafkatorio.schema.common.ServerMapChunkId
import dev.adamko.kafkatorio.schema.common.TilePngFilename
import dev.adamko.kafkatorio.schema.packets.EventServerPacket
import dev.adamko.kotka.extensions.consumedAs
import dev.adamko.kotka.extensions.producedAs
import dev.adamko.kotka.extensions.streams.map
import dev.adamko.kotka.extensions.streams.mapValues
import dev.adamko.kotka.kxs.serde
import java.awt.image.BufferedImage
import java.nio.file.Path
import kotlin.io.path.fileSize
import kotlin.math.abs
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.Topology
import org.apache.kafka.streams.kstream.KStream


private const val pid = "saveMapTiles"

private const val WEB_MAP_TILE_SIZE_PX = 256


/**
 * @param[serverDataDir] [dev.adamko.kafkatorio.processor.config.ApplicationProperties.serverDataDir]
 */
fun saveMapTiles(
  builder: StreamsBuilder,
  serverDataDir: Path,
): Topology {

  val subdividedMapChunkTilesDebounced: KStream<ServerMapChunkId, ServerMapChunkTiles<ColourHex>> =
    builder.stream(
      TOPIC_MAP_CHUNK_COLOURED_STATE,
      consumedAs(
        "$pid.consume.grouped-map-chunks",
        kxsBinary.serde(),
        kxsBinary.serde(),
      ),
    )

  val savedChunkFilenames: KStream<ServerMapChunkId, TilePngFilename> =
    subdividedMapChunkTilesDebounced.saveChunkAsImage(serverDataDir)

  savedChunkFilenames.broadcastSavedTileUpdates()

  return builder.build()
}


private fun KStream<ServerMapChunkId, ServerMapChunkTiles<ColourHex>>.saveChunkAsImage(
  tileDirectory: Path,
): KStream<ServerMapChunkId, TilePngFilename> = mapValues(
  "$pid.save-tiles"
) { chunkId: ServerMapChunkId, tileColours: ServerMapChunkTiles<ColourHex> ->

  val chunkImage = ImmutableImage.filled(
    chunkId.chunkSize.lengthInTiles,
    chunkId.chunkSize.lengthInTiles,
    TRANSPARENT_AWT,
    BufferedImage.TYPE_INT_ARGB
  )

  val chunkOriginX: Int = chunkId.chunkPosition.x * chunkId.chunkSize.lengthInTiles
  val chunkOriginY: Int = chunkId.chunkPosition.y * chunkId.chunkSize.lengthInTiles

  tileColours.map.forEach { (tilePosition, colour) ->
    val rgbColour = colour.toRgbColor()

    val pixelX = abs(abs(tilePosition.x) - abs(chunkOriginX))
    val pixelY = abs(abs(tilePosition.y) - abs(chunkOriginY))

    chunkImage.setColor(pixelX, pixelY, rgbColour)
  }

  val scaledImage =
    chunkImage.scaleTo(WEB_MAP_TILE_SIZE_PX, WEB_MAP_TILE_SIZE_PX, ScaleMethod.FastScale)

  val filename = TilePngFilename(chunkId)
  saveTile(filename, tileDirectory, scaledImage)
  filename
}


@Synchronized
private fun saveTile(
  filename: TilePngFilename,
  serverDataDir: Path,
  img: ImmutableImage,
) {
  val chunkImageFile = serverDataDir.resolve(filename.value).toFile()
  if (chunkImageFile.parentFile.mkdirs()) {
    println("created new map tile parentFile directory ${chunkImageFile.absolutePath}")
  }
  val sizeBefore = chunkImageFile.takeIf { it.exists() }?.toPath()?.fileSize()

  val savedTile = img.output(PngWriter.NoCompression, chunkImageFile)

  val sizeAfter = savedTile.takeIf { it.exists() }?.toPath()?.fileSize()

  val sizeDescription = if (sizeBefore != sizeAfter && sizeBefore != null) {
    "$sizeBefore/$sizeAfter"
  } else {
    "$sizeAfter"
  }
  println("savedTile $sizeDescription: $savedTile")
}


private fun KStream<ServerMapChunkId, TilePngFilename>.broadcastSavedTileUpdates() {
  map<ServerMapChunkId, TilePngFilename, FactorioServerId, EventServerPacket>(
    "$pid.broadcast.filename-update.convert",
  ) { id, filename ->
    id.serverId to EventServerPacket.ChunkTileSaved(id, filename)
  }.to(
    TOPIC_BROADCAST_TO_WEBSOCKET,
    producedAs(
      "$pid.broadcast.filename-update.send",
      jsonMapper.serde(FactorioServerId.serializer()),
      jsonMapper.serde(EventServerPacket.serializer()),
    )
  )
}
