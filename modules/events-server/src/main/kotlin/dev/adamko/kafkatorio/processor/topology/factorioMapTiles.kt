package dev.adamko.kafkatorio.processor.topology

import dev.adamko.kafkatorio.events.schema.FactorioEvent
import dev.adamko.kafkatorio.events.schema.FactorioObjectData
import dev.adamko.kafkatorio.events.schema.KafkatorioPacket
import dev.adamko.kafkatorio.events.schema.MapChunk
import dev.adamko.kafkatorio.events.schema.MapTile
import dev.adamko.kafkatorio.events.schema.MapTilePosition
import dev.adamko.kafkatorio.events.schema.MapTiles
import dev.adamko.kafkatorio.processor.serdes.jsonMapper
import dev.adamko.kafkatorio.processor.serdes.kxsBinary
import dev.adamko.kotka.extensions.consumedAs
import dev.adamko.kotka.extensions.materializedWith
import dev.adamko.kotka.extensions.streams.filter
import dev.adamko.kotka.extensions.streams.flatMap
import dev.adamko.kotka.extensions.streams.mapValues
import dev.adamko.kotka.extensions.streams.toTable
import dev.adamko.kotka.kxs.serde
import java.time.Duration
import kotlinx.serialization.Serializable
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
): KTable<TileUpdateRecordKey, MapTile> {

  val luaTilesUpdatesStream: KStream<FactorioServerId, MapTiles> =
    builder.stream<FactorioServerId, FactorioEvent>(
      "kafkatorio.${KafkatorioPacket.PacketType.EVENT}.${FactorioObjectData.ObjectName.LuaTiles}",
      consumedAs("consume-map-tiles-packets", jsonMapper.serde(), jsonMapper.serde())
    )
      .filter("events.filter.map-tiles") { _: FactorioServerId, event: FactorioEvent ->
        event.data is MapTiles
      }
      .mapValues("events.extract-map-tiles") { _, event: FactorioEvent ->
        (event.data as? MapTiles)!!
      }

  val chunkTilesUpdateStream: KStream<FactorioServerId, MapTiles> =
    builder.stream<FactorioServerId, FactorioEvent>(
      "kafkatorio.${KafkatorioPacket.PacketType.EVENT}.${FactorioObjectData.ObjectName.MapChunk}",
      consumedAs("consume-map-chunk-packets", jsonMapper.serde(), jsonMapper.serde())
    )
      .filter("events.filter.map-chunks") { _: FactorioServerId, event: FactorioEvent ->
        event.data is MapChunk
      }
      .mapValues("events.extract-map-chunks") { _, event: FactorioEvent ->
        (event.data as? MapChunk)!!.tiles
      }

  return luaTilesUpdatesStream
    .merge(chunkTilesUpdateStream)
    .flatMap("flat-map-all-tiles") { _, value ->
      value.tiles.map { tile ->
        val key = TileUpdateRecordKey(
          SurfaceIndex(value.surfaceIndex),
          tile.position
        )
        key to tile
      }
    }
    .toTable(
      "all-tiles-keyed-on-position",
      materializedWith(kxsBinary.serde(), kxsBinary.serde())
    )

}
