package dev.adamko.kafkatorio.processor.topology

import dev.adamko.kafkatorio.events.schema.FactorioEvent
import dev.adamko.kafkatorio.events.schema.FactorioObjectData
import dev.adamko.kafkatorio.events.schema.KafkatorioPacket
import dev.adamko.kafkatorio.events.schema.MapChunk
import dev.adamko.kafkatorio.events.schema.MapTile
import dev.adamko.kafkatorio.events.schema.MapTilePosition
import dev.adamko.kafkatorio.events.schema.MapTiles
import dev.adamko.kafkatorio.processor.jsonMapper
import dev.adamko.kotka.extensions.consumedAs
import dev.adamko.kotka.extensions.materializedWith
import dev.adamko.kotka.extensions.streams.filter
import dev.adamko.kotka.extensions.streams.flatMap
import dev.adamko.kotka.extensions.streams.mapValues
import dev.adamko.kotka.extensions.streams.toTable
import dev.adamko.kotka.kxs.serde
import java.time.Duration
import kotlinx.serialization.Serializable
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.kstream.KStream
import org.apache.kafka.streams.kstream.KTable
import org.apache.kafka.streams.kstream.Suppressed

@Serializable
data class TileUpdateRecordKey(
  val surfaceIndex: SurfaceIndex,
  val tilePosition: MapTilePosition,
)


fun allMapTilesTable(
  builder: StreamsBuilder
): KTable<MapTilePosition, MapTile> {

  val luaTilesUpdatesStream: KStream<String, MapTiles> = builder.stream(
    "kafkatorio.${KafkatorioPacket.PacketType.EVENT}.${FactorioObjectData.ObjectName.LuaTiles}",
    consumedAs("consume-map-tiles-packets", Serdes.String(), jsonMapper.serde<FactorioEvent>())
  )
    .filter("events.filter.map-tiles") { _: String, event: FactorioEvent -> event.data is MapTiles }
    .mapValues("events.extract-map-tiles") { _, event: FactorioEvent -> (event.data as? MapTiles)!! }

  val chunkTilesUpdateStream: KStream<String, MapTiles> = builder.stream(
    "kafkatorio.${KafkatorioPacket.PacketType.EVENT}.${FactorioObjectData.ObjectName.MapChunk}",
    consumedAs("consume-map-chunk-packets", Serdes.String(), jsonMapper.serde<FactorioEvent>())
  )
    .filter("events.filter.map-chunks") { _: String, event: FactorioEvent -> event.data is MapChunk }
    .mapValues("events.extract-map-chunks") { _, event: FactorioEvent -> (event.data as? MapChunk)!!.tiles }

  return luaTilesUpdatesStream
    .merge(chunkTilesUpdateStream)
    .flatMap("flat-map-all-tiles") { _, value ->
      value.tiles.map { it.position to it }
    }
    .toTable(
      "all-tiles-keyed-on-position",
      materializedWith(jsonMapper.serde(), jsonMapper.serde())
    )
    .suppress(
      Suppressed.untilTimeLimit(
        Duration.ofSeconds(30),
        Suppressed.BufferConfig
          .maxRecords(30)
          .emitEarlyWhenFull()
      )
    )
}

//fun buildTilesTable(
//  builder: StreamsBuilder
//): KTable<TileUpdateRecordKey, TileUpdateRecord> {
//
//  return luaTilesUpdatesStream
//    .merge("luaTilesUpdatesStream-and-chunkTilesUpdateStream", chunkTilesUpdateStream)
//    .flatMapTopicRecords("all-tiles-convert-to-TileUpdateRecord") { _, mapTiles: MapTiles ->
//      mapTiles.tiles.map { tile ->
//        TileUpdateRecord(
//          SurfaceIndex(mapTiles.surfaceIndex),
//          tile.position,
//          tile,
//        )
//      }
//    }
//    .toTable(materializedWith(jsonMapper.serde(), jsonMapper.serde()))
//}
