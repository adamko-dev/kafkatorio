package dev.adamko.kafkatorio.processor.topology

import com.sksamuel.scrimage.color.RGBColor
import dev.adamko.kafkatorio.events.schema.Colour
import dev.adamko.kafkatorio.events.schema.MapTile
import dev.adamko.kafkatorio.events.schema.MapTilePosition
import dev.adamko.kafkatorio.events.schema.converters.toMapChunkPosition
import dev.adamko.kafkatorio.processor.serdes.kxsBinary
import dev.adamko.kotka.extensions.groupedAs
import dev.adamko.kotka.extensions.materializedAs
import dev.adamko.kotka.extensions.materializedWith
import dev.adamko.kotka.extensions.namedAs
import dev.adamko.kotka.extensions.streams.groupBy
import dev.adamko.kotka.extensions.tables.groupBy
import dev.adamko.kotka.extensions.tables.join
import dev.adamko.kotka.extensions.tables.mapValues
import dev.adamko.kotka.extensions.tables.toStream
import dev.adamko.kotka.extensions.toKeyValue
import dev.adamko.kotka.kxs.serde
import java.awt.Color
import java.time.Duration
import kotlinx.serialization.Serializable
import org.apache.kafka.streams.kstream.KTable
import org.apache.kafka.streams.kstream.SessionWindows
import org.apache.kafka.streams.kstream.Suppressed
import org.apache.kafka.streams.kstream.Windowed


/** The tiles of the webmap will be this many pixels high/wide */
const val WEB_MAP_IMAGE_TILE_SIZE = 256

/** Debounce duration */
private val WINDOW_INACTIVITY_DURATION = Duration.ofSeconds(60)

@Serializable
data class WebMapTileChunkPosition(
  val chunkSize: Int,
  val x: Int,
  val y: Int,
  val surfaceIndex: SurfaceIndex,
)

@Serializable
private data class WebMapTilePixel(
  val mapColour: Colour,
  val tilePosition: MapTilePosition,
)

@Serializable
@JvmInline
value class WebMapTileChunkPixels(
  val pixels: Map<MapTilePosition, Colour>,
)

/** Temporary mutable accumulator */
@Serializable
@JvmInline
private value class WebMapTileChunkPixelsAcc(
  val pixels: MutableMap<MapTilePosition, Colour> = mutableMapOf(),
) {

  operator fun plus(pixel: WebMapTilePixel): WebMapTileChunkPixelsAcc {
    pixels[pixel.tilePosition] = pixel.mapColour
    return this
  }

  operator fun plus(other: WebMapTileChunkPixelsAcc): WebMapTileChunkPixelsAcc {
    this.pixels += other.pixels
    return this
  }

  operator fun minus(other: WebMapTileChunkPixelsAcc): WebMapTileChunkPixelsAcc {
    this.pixels -= other.pixels.keys
    return this
  }

}


fun aggregateWebMapTiles(
  allMapTilesTable: KTable<TileUpdateRecordKey, MapTile>,
  tilePrototypeColourTable: KTable<PrototypeName, Colour>,
): KTable<WebMapTileChunkPosition, WebMapTileChunkPixels> {


  val pixels: KTable<TileUpdateRecordKey, WebMapTilePixel> =
    allMapTilesTable
      .join(
        tilePrototypeColourTable,
        "convert-tiles-to-colours",
        materializedAs(
          "store-web-map-tile-pixels",
          kxsBinary.serde(),
          kxsBinary.serde(),
        ),
        { PrototypeName(it.prototypeName) },
      ) { tile: MapTile, colour: Colour ->
        WebMapTilePixel(colour, tile.position)
      }


  val chunkedPixelsWindowed: KTable<Windowed<WebMapTileChunkPosition>, WebMapTileChunkPixelsAcc> =
    pixels
      .toStream("stream-webmap-tile-pixels")
      .groupBy(
        groupedAs(
          "group-webmap-tile-colours-into-chunks",
          kxsBinary.serde(),
          kxsBinary.serde(),
        )
      ) { pos: TileUpdateRecordKey, px: WebMapTilePixel ->
        val (chunkX, chunkY) = pos.tilePosition.toMapChunkPosition(WEB_MAP_IMAGE_TILE_SIZE)
        val chunkPos = WebMapTileChunkPosition(
          WEB_MAP_IMAGE_TILE_SIZE,
          chunkX,
          chunkY,
          pos.surfaceIndex
        )
        chunkPos
      }
      .windowedBy(
        SessionWindows.ofInactivityGapAndGrace(
          WINDOW_INACTIVITY_DURATION,
          WINDOW_INACTIVITY_DURATION,
        )
      )
      .aggregate(
        { WebMapTileChunkPixelsAcc() },
        { _: WebMapTileChunkPosition, newValue: WebMapTilePixel, acc: WebMapTileChunkPixelsAcc ->
          acc + newValue
        },
        { _, aggOne: WebMapTileChunkPixelsAcc, aggTwo: WebMapTileChunkPixelsAcc ->
          aggOne + aggTwo
        },
        namedAs("web-map-tile-colours-into-$WEB_MAP_IMAGE_TILE_SIZE-chunks"),
        materializedAs(
          "web-map-tile-colour-chunks-aggregate",
          kxsBinary.serde(),
          kxsBinary.serde()
        )
      )
      .suppress(
        Suppressed.untilWindowCloses(
          Suppressed.BufferConfig.unbounded()
        ).withName("chunked-pixels-debounce")
      )


  // merge windows
  val chunkedPixels: KTable<WebMapTileChunkPosition, WebMapTileChunkPixelsAcc> =
    chunkedPixelsWindowed
      .groupBy(
        groupedAs("merge-chunked-pixels.group-keys", kxsBinary.serde(), kxsBinary.serde())
      ) { a: Windowed<WebMapTileChunkPosition>, b: WebMapTileChunkPixelsAcc ->
        (a.key() to b).toKeyValue()
      }
      .reduce(
        /* adder */
        { pixels1, pixels2 -> pixels1 + pixels2 },
        /* subtractor */
        { pixels1, pixels2 -> pixels1 - pixels2 },
        namedAs("merge-chunked-pixels.reduce-values"),
        materializedAs("merge-chunked-pixels.reduce-values.store", kxsBinary.serde(), kxsBinary.serde())
      )

  return chunkedPixels
    .mapValues(
      "finalise-web-map-tile-colour-chunk-aggregation",
      materializedAs("web-map-tile-colour-chunks", kxsBinary.serde(), kxsBinary.serde())
    ) { _: WebMapTileChunkPosition, v: WebMapTileChunkPixelsAcc ->
      WebMapTileChunkPixels(v.pixels)
    }
}
