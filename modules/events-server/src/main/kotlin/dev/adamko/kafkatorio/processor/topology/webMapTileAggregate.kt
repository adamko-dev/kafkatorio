package dev.adamko.kafkatorio.processor.topology

import dev.adamko.kafkatorio.events.schema.Colour
import dev.adamko.kafkatorio.events.schema.MapTile
import dev.adamko.kafkatorio.events.schema.MapTilePosition
import dev.adamko.kafkatorio.events.schema.MapTilePrototype
import dev.adamko.kafkatorio.events.schema.converters.toMapChunkPosition
import dev.adamko.kafkatorio.processor.serdes.kxsBinary
import dev.adamko.kafkatorio.processor.serdes.serde
import dev.adamko.kotka.extensions.groupedAs
import dev.adamko.kotka.extensions.materializedAs
import dev.adamko.kotka.extensions.namedAs
import dev.adamko.kotka.extensions.tables.groupBy
import dev.adamko.kotka.extensions.tables.join
import dev.adamko.kotka.extensions.tables.mapValues
import dev.adamko.kotka.extensions.toKeyValue
import java.time.Duration
import kotlinx.serialization.Serializable
import org.apache.kafka.streams.kstream.KTable
import org.apache.kafka.streams.kstream.Suppressed


/** The tiles of the webmap will be this many pixels high/wide */
const val WEB_MAP_IMAGE_TILE_SIZE = 256

@Serializable
data class WebMapTileChunkPosition(
  val chunkSize: Int,
  val x: Int,
  val y: Int,
  val surfaceIndex: SurfaceIndex,
)

@Serializable
data class WebMapTilePixel(
  val mapColour: Colour,
  val tilePosition: MapTilePosition,
)

@Serializable
data class WebMapTileChunkPixels(
  val pixels: Set<WebMapTilePixel> = mutableSetOf(),
)

/** Temporary mutable aggregator */
@Serializable
private data class WebMapTileChunkPixelsAcc(
  val pixels: MutableSet<WebMapTilePixel> = mutableSetOf(),
)

fun aggregateWebMapTiles(
  allMapTilesTable: KTable<TileUpdateRecordKey, MapTile>,
  tilePrototypesTable: KTable<PrototypeName, MapTilePrototype>,
): KTable<WebMapTileChunkPosition, WebMapTileChunkPixels> {

  val webmapTileColours: KTable<TileUpdateRecordKey, WebMapTilePixel> =
    allMapTilesTable
      .join(
        tilePrototypesTable,
        "convert-tiles-to-colours",
        materializedAs(
          "store-web-map-tile-pixels",
          kxsBinary.serde(),
          kxsBinary.serde(),
        ),
        { PrototypeName(it.prototypeName) },
      ) { tile: MapTile, proto: MapTilePrototype ->
        WebMapTilePixel(proto.mapColour, tile.position)
      }


  val webMapTileChunkAggregate: KTable<WebMapTileChunkPosition, WebMapTileChunkPixelsAcc> =
    webmapTileColours
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
        (chunkPos to px).toKeyValue()
      }
      .aggregate(
        { WebMapTileChunkPixelsAcc() },
        /* adder */
        { _: WebMapTileChunkPosition, newValue: WebMapTilePixel, acc: WebMapTileChunkPixelsAcc ->
          acc.apply { pixels.add(newValue) }
        },
        /* subtractor */
        { _: WebMapTileChunkPosition, oldValue: WebMapTilePixel, acc: WebMapTileChunkPixelsAcc ->
          acc.apply { pixels.minus(oldValue) }
        },
        namedAs("web-map-tile-colours-into-$WEB_MAP_IMAGE_TILE_SIZE-chunks"),
        materializedAs(
          "web-map-tile-colour-chunks-aggregate",
          kxsBinary.serde(),
          kxsBinary.serde()
        )
      )
      .suppress(
        Suppressed.untilTimeLimit<WebMapTileChunkPosition>(
          Duration.ofSeconds(30),
          Suppressed.BufferConfig
            .maxRecords(30)
//            .withMaxBytes(31457280  ) // 30MB
//            .emitEarlyWhenFull()
        ).withName("web-map-tile-aggregate-debounce")
      )

  return webMapTileChunkAggregate
    .mapValues(
      "finalise-web-map-tile-colour-chunk-aggregation",
      materializedAs("web-map-tile-colour-chunks", kxsBinary.serde(), kxsBinary.serde())
    ) { _, v ->
      WebMapTileChunkPixels(v.pixels)
    }
}
