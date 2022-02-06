package dev.adamko.kafkatorio.processor.topology

import com.sksamuel.scrimage.ImmutableImage
import com.sksamuel.scrimage.ScaleMethod
import com.sksamuel.scrimage.nio.PngWriter
import dev.adamko.kafkatorio.events.schema.Colour
import dev.adamko.kafkatorio.events.schema.MapChunkPosition
import dev.adamko.kafkatorio.events.schema.MapTilePosition
import dev.adamko.kafkatorio.events.schema.converters.toMapChunkPosition
import java.awt.image.BufferedImage
import java.io.File
import kotlin.math.abs
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.apache.kafka.streams.kstream.KTable


private const val WEB_MAP_TILE_SIZE_PX = 256

private val cScope = CoroutineScope(
  Dispatchers.Default + SupervisorJob() + CoroutineName("f-server-map-updates")
)

/** A bridge between Kafka and Kotlin */
private val serverMapDataFlow: MutableStateFlow<FactorioServerMap?> = MutableStateFlow(null)


fun saveMapTiles(
  serverMapTable: KTable<FactorioServerId, FactorioServerMap>
) {

  serverMapTable
    .toStream()
    .foreach { _, value ->
      runBlocking(Dispatchers.Default) {
        serverMapDataFlow.emit(value)
      }
    }

  cScope.launch {
    println("launching serverMapDataFlow merging")


    serverMapDataFlow
      .filterNotNull()

    serverMapDataFlow
      .filterNotNull()
      .conflate()
//      .transform { serverMapData ->
      .flatMapMerge(10) { serverMapData ->
        println("flatMapMerge serverMapData ${serverMapData.serverId}, surfaces ${serverMapData.surfaces.keys}")

        val prototypes = serverMapData.tilePrototypes

        serverMapData
          .surfaces
          .values
//          .forEach { surfaceData ->
          .asFlow()
          .flatMapMerge(10) { surfaceData ->
            println("flatMapMerge surfaceData ${surfaceData.index}")

            // get all tile colours for the surface
            val surfaceTileColours = surfaceData.tiles
              .entries
              .mapNotNull { (tilePosition, prototypeName) ->
                prototypes[prototypeName]?.let { prototype ->
                  tilePosition to prototype.mapColour
                }
              }
            println("surfaceTileColours.size ${surfaceTileColours.size}")

            // split a surface into chunks, per zoom-level
            ZoomLevel.values
//              .forEach { zoom ->
              .asFlow()
              .flatMapMerge(10) { zoom ->
                println("flatMapMerge zoom $zoom")

                surfaceTileColours
                  .groupBy { (tilePosition, _) ->
                    tilePosition.toMapChunkPosition(zoom.chunkSize)
                  }
                  .entries
//                    .forEach { (chunkPosition, tileColours) ->
                  .asFlow()
                  .mapLatest { (chunkPosition, tileColours) ->
                    println("mapLatest chunkPosition $chunkPosition")

                    // mapLatest + delay = an alternative to debounce-per-key?
                    delay(30.seconds)

                    println("emitting ChunkData $chunkPosition")

                    ChunkData(
                      surfaceData.index,
                      zoom,
                      chunkPosition,
                      tileColours.toMap()
                    )
                  }
              }
          }
      }
      .onEach { chunkData ->

        println("starting to save chunkData ${chunkData.chunkPosition}...")

        val chunkImageFile = File(chunkData.filename.value)
        if (chunkImageFile.parentFile.mkdirs()) {
          println("created new map tile parentFile directory ${chunkImageFile.absolutePath}")
        }

        val image =
          createMapTileImage(chunkData.chunkPosition, chunkData.tileColours, chunkData.zoomLevel)

        println("saving map tile $chunkImageFile")
        val savedTile = image.output(PngWriter.NoCompression, chunkImageFile)
        println("savedTile: $savedTile")

      }
      .launchIn(this)
  }
}


@JvmInline
private value class TilePngFilename(
  val value: String,
) {

  constructor(
    surfaceIndex: SurfaceIndex,
    zoom: Int,
    chunkX: Int,
    chunkY: Int,
  ) : this("src/main/resources/kafkatorio-web-map/s${surfaceIndex.index}/z$zoom/x${chunkX}/y${chunkY}.png")

}


private data class ChunkData(
  val surfaceIndex: SurfaceIndex,
  val zoomLevel: ZoomLevel,
  val chunkPosition: MapChunkPosition,
  val tileColours: Map<MapTilePosition, Colour>,
) {
  val filename = TilePngFilename(surfaceIndex, zoomLevel.level, chunkPosition.x, chunkPosition.y)
}

private fun createMapTileImage(
  chunkPosition: MapChunkPosition,
  chunkColours: Map<MapTilePosition, Colour>,
  z: ZoomLevel
): ImmutableImage {


  val chunkImage = ImmutableImage.filled(
    z.chunkSize,
    z.chunkSize,
    COLOUR_TRANSPARENT.awt(),
    BufferedImage.TYPE_INT_ARGB
  )

  val chunkOriginX: Int = chunkPosition.x * z.chunkSize
  val chunkOriginY: Int = chunkPosition.y * z.chunkSize

  chunkColours.forEach { (tilePosition, colour) ->

    val rgbColour = colour.toRgbColor()

    val pixelX = abs(abs(tilePosition.x) - abs(chunkOriginX))
    val pixelY = abs(abs(tilePosition.y) - abs(chunkOriginY))

    chunkImage.setColor(
      pixelX,
      pixelY,
      rgbColour
    )
  }

  return chunkImage.scaleTo(WEB_MAP_TILE_SIZE_PX, WEB_MAP_TILE_SIZE_PX, ScaleMethod.Progressive)
}
