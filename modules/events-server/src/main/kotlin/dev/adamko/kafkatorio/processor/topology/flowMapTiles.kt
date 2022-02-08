package dev.adamko.kafkatorio.processor.topology

import com.sksamuel.scrimage.ImmutableImage
import com.sksamuel.scrimage.ScaleMethod
import com.sksamuel.scrimage.nio.PngWriter
import dev.adamko.kafkatorio.events.schema.ColourHex
import dev.adamko.kafkatorio.events.schema.MapChunkPosition
import dev.adamko.kafkatorio.events.schema.MapTilePosition
import java.awt.image.BufferedImage
import java.io.File
import kotlin.math.abs
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.supervisorScope
import org.apache.kafka.streams.kstream.KTable


private const val WEB_MAP_TILE_SIZE_PX = 256

private val cScope = CoroutineScope(
  Dispatchers.Default + SupervisorJob() + CoroutineName("f-server-map-updates")
)

/** A bridge between Kafka and Kotlin */
private val chunkedTiles: MutableSharedFlow<ServerMapChunkTiles<ColourHex>> =
  MutableSharedFlow(
    0,
    1,
    BufferOverflow.SUSPEND
  )


fun saveMapTiles(
  serverMapTable: KTable<ServerMapChunkId, ServerMapChunkTiles<ColourHex>>
) {

  serverMapTable
    .toStream()
    .foreach { _, value ->
      if (value != null) {
        runBlocking(Dispatchers.Default) {
          println("emitting chunkedTiles ${value.chunkId} tiles: ${value.map.size}")
          chunkedTiles.emit(value)
        }
      }
    }

  cScope.launch {
    supervisorScope {
      chunkedTiles
        .conflate()
        .distinctUntilChanged()
        .debounce(5.seconds)
        .map {
          println("createMapTileImage ${it.chunkId}")
          createMapTileImage(it)
        }
        .collect()
    }
  }

//  cScope.launch {
//    println("launching serverMapDataFlow merging")
//
//    chunkedTiles
//      .runningFold(
//        mapOf<ServerMapChunkId, MutableSharedFlow<ServerMapChunkTiles<ColourHex>>>()
//      ) { accumulator, value ->
//
//        println("folding chunkedTiles ${value.chunkId}, current flows: ${accumulator.keys}")
//
//        val chunkFlow = accumulator.getOrElse(value.chunkId) {
//
//          println("creating new MutableSharedFlow for ${value.chunkId}")
//
//          val input = MutableSharedFlow<ServerMapChunkTiles<ColourHex>>(
//            replay = 0,
//            extraBufferCapacity = 1,
//            onBufferOverflow = BufferOverflow.DROP_OLDEST,
//          )
//
//          supervisorScope {
//            println("launching MutableSharedFlow for ${value.chunkId}")
//            input
//              .debounce(30.seconds)
//              .mapLatest {
//                println("createMapTileImage ${it.chunkId}")
//                createMapTileImage(it)
//              }.launchIn(this)
//          }
//
//          input
//        }
//
//        chunkFlow.emit(value)
//
//        accumulator + (value.chunkId to chunkFlow)
//      }
//      .collect()
//
//    println("end of cScope.launch")
//  }
}


@JvmInline
private value class TilePngFilename(
  val value: String,
) {

  constructor(
    chunkId: ServerMapChunkId
  ) : this(buildString {
    append("src/main/resources/kafkatorio-web-map/s")
    append(chunkId.surfaceIndex.index)
    append("/z")
    append(chunkId.zoomLevel.level)
    append("/x")
    append(chunkId.chunkPosition.x)
    append("/y")
    append(chunkId.chunkPosition.y)
    append(".png")
  })
}

private fun createMapTileImage(
  chunkTiles: ServerMapChunkTiles<ColourHex>
) {

  println("emitting ChunkData ${chunkTiles.chunkId.chunkPosition}")

  println("starting to save chunkData ${chunkTiles.chunkId.chunkPosition}...")

  val filename = TilePngFilename(chunkTiles.chunkId)

  val chunkImageFile = File(filename.value)
  if (chunkImageFile.parentFile.mkdirs()) {
    println("created new map tile parentFile directory ${chunkImageFile.absolutePath}")
  }

  val image =
    createMapTileImage(
      chunkTiles.chunkId.chunkPosition,
      chunkTiles.map,
      chunkTiles.chunkId.zoomLevel
    )

  println("saving map tile $chunkImageFile")
  val savedTile = image.output(PngWriter.NoCompression, chunkImageFile)
  println("savedTile: $savedTile")
}


private fun createMapTileImage(
  chunkPosition: MapChunkPosition,
  chunkColours: Map<MapTilePosition, ColourHex>,
  z: ZoomLevel
): ImmutableImage {

  val chunkImage = ImmutableImage.filled(
    z.chunkSize,
    z.chunkSize,
    ColourHex.TRANSPARENT.toRgbColor().awt(),
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
