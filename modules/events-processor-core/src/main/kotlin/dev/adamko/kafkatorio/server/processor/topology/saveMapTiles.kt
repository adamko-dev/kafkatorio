package dev.adamko.kafkatorio.server.processor.topology

import com.sksamuel.scrimage.ImmutableImage
import com.sksamuel.scrimage.ScaleMethod
import com.sksamuel.scrimage.nio.PngWriter
import dev.adamko.kafkatorio.library.kxsBinary
import dev.adamko.kafkatorio.schema.common.ChunkSize
import dev.adamko.kafkatorio.schema.common.ColourHex
import dev.adamko.kafkatorio.schema.common.FactorioServerId
import dev.adamko.kafkatorio.schema.common.ServerMapChunkId
import dev.adamko.kafkatorio.schema.common.ServerMapTilePngFilename
import dev.adamko.kafkatorio.schema.packets.EventServerPacket
import dev.adamko.kafkatorio.processor.config.TOPIC_BROADCAST_TO_WEBSOCKET
import dev.adamko.kafkatorio.processor.config.TOPIC_MAP_CHUNK_BUILDING_COLOURED_STATE
import dev.adamko.kafkatorio.processor.config.TOPIC_MAP_CHUNK_RESOURCE_COLOURED_STATE
import dev.adamko.kafkatorio.processor.config.TOPIC_MAP_CHUNK_TERRAIN_COLOURED_STATE
import dev.adamko.kafkatorio.processor.config.jsonMapper
import dev.adamko.kotka.extensions.consumedAs
import dev.adamko.kotka.extensions.producedAs
import dev.adamko.kotka.extensions.stream
import dev.adamko.kotka.extensions.streams.filter
import dev.adamko.kotka.extensions.streams.map
import dev.adamko.kotka.extensions.streams.mapValues
import dev.adamko.kotka.kxs.serde
import java.awt.image.BufferedImage
import java.nio.file.Path
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.io.path.fileSize
import kotlin.math.abs
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.Topology
import org.apache.kafka.streams.kstream.KStream


private const val pid = "saveMapTiles"

private const val WEB_MAP_TILE_SIZE_PX = 256


/**
 * @param[serverDataDir] [dev.adamko.kafkatorio.server.config.ApplicationProperties.serverDataDir]
 */
fun saveMapTiles(
  builder: StreamsBuilder,
  serverDataDir: Path,
) {

  val subdividedMapChunkTilesDebounced: KStream<ServerMapChunkId, ServerMapChunkTiles<ColourHex>> =
    builder.stream(
      consumedAs(
        "$pid.consume.grouped-map-chunks",
        kxsBinary.serde(ServerMapChunkId.serializer()),
        kxsBinary.serde(ServerMapChunkTiles.serializer(ColourHex.serializer())),
      ),
      TOPIC_MAP_CHUNK_TERRAIN_COLOURED_STATE,
      TOPIC_MAP_CHUNK_RESOURCE_COLOURED_STATE,
      TOPIC_MAP_CHUNK_BUILDING_COLOURED_STATE,
    ).filter("$pid.consume.grouped-map-chunks.filter-size") { _, y ->
      y.chunkId.chunkSize.lengthInTiles >= ChunkSize.CHUNK_256.lengthInTiles
    }

  val savedChunkFilenames: KStream<ServerMapChunkId, ServerMapTilePngFilename> =
    subdividedMapChunkTilesDebounced.saveChunkAsImage(serverDataDir)

  savedChunkFilenames.broadcastSavedTileUpdates()
}


private fun KStream<ServerMapChunkId, ServerMapChunkTiles<ColourHex>>.saveChunkAsImage(
  tileDirectory: Path,
): KStream<ServerMapChunkId, ServerMapTilePngFilename> = mapValues(
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

  val validPixelRangeX = 0 until chunkImage.width
  val validPixelRangeY = 0 until chunkImage.height

  tileColours.map.forEach { (tilePosition, colour) ->
    val rgbColour = colour.toRgbColor()

    val pixelX = abs(abs(tilePosition.x) - abs(chunkOriginX))
    val pixelY = abs(abs(tilePosition.y) - abs(chunkOriginY))

    if (!(pixelX in validPixelRangeX && pixelY in validPixelRangeY)) {
      // TODO figure out why buildings on chunk boundaries aren't re-chunked, and so cause this error
      println(
        "ERROR - pixel XY:[$pixelX,$pixelY] was out of bounds (x:$validPixelRangeX, y:$validPixelRangeY). $tilePosition, $colour, chunkOriginXY[$chunkOriginX,$chunkOriginY], $chunkId"
      )
    } else {
      try {
        chunkImage.setColor(pixelX, pixelY, rgbColour)
      } catch (e: Exception) {
        println(
          "ERROR $e - pixel XY:[$pixelX,$pixelY] (x:$validPixelRangeX, y:$validPixelRangeY). $tilePosition, $colour, chunkOriginXY[$chunkOriginX,$chunkOriginY], $chunkId"
        )
        throw e
      }
    }
  }

  val scaledImage =
    chunkImage.scaleTo(WEB_MAP_TILE_SIZE_PX, WEB_MAP_TILE_SIZE_PX, ScaleMethod.FastScale)

  val filename = ServerMapTilePngFilename(chunkId)
  saveTile(filename, tileDirectory, scaledImage)
  filename
}


@Synchronized
private fun saveTile(
  filename: ServerMapTilePngFilename,
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


private fun KStream<ServerMapChunkId, ServerMapTilePngFilename>.broadcastSavedTileUpdates() {
  map<ServerMapChunkId, ServerMapTilePngFilename, FactorioServerId, EventServerPacket>(
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
