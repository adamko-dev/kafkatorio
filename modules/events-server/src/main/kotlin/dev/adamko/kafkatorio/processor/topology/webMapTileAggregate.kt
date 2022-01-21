package dev.adamko.kafkatorio.processor.topology

import dev.adamko.kafkatorio.events.schema.Colour
import dev.adamko.kafkatorio.events.schema.MapTile
import dev.adamko.kafkatorio.events.schema.MapTilePosition
import dev.adamko.kafkatorio.events.schema.MapTilePrototype
import dev.adamko.kafkatorio.events.schema.converters.toMapChunkPosition
import dev.adamko.kafkatorio.processor.jsonMapper
import dev.adamko.kotka.extensions.groupedAs
import dev.adamko.kotka.extensions.materializedAs
import dev.adamko.kotka.extensions.namedAs
import dev.adamko.kotka.extensions.tables.groupBy
import dev.adamko.kotka.extensions.tables.join
import dev.adamko.kotka.extensions.tables.mapValues
import dev.adamko.kotka.extensions.toKeyValue
import dev.adamko.kotka.kxs.serde
import java.time.Duration
import kotlinx.serialization.Serializable
import org.apache.kafka.streams.kstream.KTable
import org.apache.kafka.streams.kstream.Suppressed

/** The tiles of the webmap will be this many pixels high/wide */
const val WEB_MAP_IMAGE_TILE_SIZE = 32

//@Serializable
//data class MapChunkDataPosition(
//  val position: MapChunkPosition,
//  val surfaceIndex: SurfaceIndex,
//)
//
//@Serializable
//data class MapChunkData(
//  val chunkPosition: MapChunkDataPosition,
//  val tiles: Set<MapTile>,
//)

@Serializable
data class WebMapTileChunkPosition(
  val chunkSize: Int,
  val x: Int,
  val y: Int,
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
  allMapTilesTable: KTable<MapTilePosition, MapTile>,
  tilePrototypesTable: KTable<PrototypeName, MapTilePrototype>,
): KTable<WebMapTileChunkPosition, WebMapTileChunkPixels> {

  val webmapTileColours: KTable<MapTilePosition, WebMapTilePixel> =
    allMapTilesTable
      .join(
        tilePrototypesTable,
        "convert-tiles-to-colours",
        materializedAs(
          "store-web-map-tile-pixels",
          jsonMapper.serde(),
          jsonMapper.serde(),
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
          jsonMapper.serde(),
          jsonMapper.serde(),
        )
      ) { pos: MapTilePosition, px: WebMapTilePixel ->
        val (chunkX, chunkY) = pos.toMapChunkPosition(WEB_MAP_IMAGE_TILE_SIZE)
        val chunkPos = WebMapTileChunkPosition(WEB_MAP_IMAGE_TILE_SIZE, chunkX, chunkY)
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
        materializedAs("web-map-tile-colour-chunks-aggregate", jsonMapper.serde(), jsonMapper.serde())
      )
      .suppress(
        Suppressed.untilTimeLimit<WebMapTileChunkPosition>(
          Duration.ofSeconds(30),
          Suppressed.BufferConfig
            .maxRecords(2)
//            .withMaxBytes(31457280  ) // 30MB
            .emitEarlyWhenFull()
        ).withName("web-map-tile-aggregate-debounce")
      )

  return webMapTileChunkAggregate
    .mapValues(
      "finalise-web-map-tile-colour-chunk-aggregation",
      materializedAs("web-map-tile-colour-chunks", jsonMapper.serde(), jsonMapper.serde())
    ) { _, v ->
      WebMapTileChunkPixels(v.pixels)
    }
}
