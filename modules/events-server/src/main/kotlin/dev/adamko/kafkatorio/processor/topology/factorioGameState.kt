package dev.adamko.kafkatorio.processor.topology

import dev.adamko.kafkatorio.events.schema.FactorioConfigurationUpdate
import dev.adamko.kafkatorio.events.schema.FactorioEvent
import dev.adamko.kafkatorio.events.schema.FactorioPrototypes
import dev.adamko.kafkatorio.events.schema.KafkatorioPacket
import dev.adamko.kafkatorio.events.schema.MapChunk
import dev.adamko.kafkatorio.events.schema.MapTilePosition
import dev.adamko.kafkatorio.events.schema.MapTilePrototype
import dev.adamko.kafkatorio.events.schema.MapTiles
import dev.adamko.kafkatorio.processor.serdes.jsonMapper
import dev.adamko.kafkatorio.processor.serdes.kxsBinary
import dev.adamko.kotka.extensions.groupedAs
import dev.adamko.kotka.extensions.materializedAs
import dev.adamko.kotka.extensions.materializedWith
import dev.adamko.kotka.extensions.streams.aggregate
import dev.adamko.kotka.extensions.tables.mapValues
import dev.adamko.kotka.kxs.serde
import kotlinx.serialization.Serializable
import org.apache.kafka.streams.kstream.KStream
import org.apache.kafka.streams.kstream.KTable


@Serializable
data class FactorioServerMap(
  val serverId: FactorioServerId,
  val tilePrototypes: Map<PrototypeName, MapTilePrototype>,
  val surfaces: Map<SurfaceIndex, FactorioMapSurface>,
)


@Serializable
data class FactorioMapSurface(
  val index: SurfaceIndex,
  val tiles: Map<MapTilePosition, PrototypeName>,
)

/** Aggregate [KafkatorioPacket]s into a single [FactorioServerMap] (per [serverId][FactorioServerId]). */
fun buildFactorioServerMap(
  factorioServerPacketStream: KStream<FactorioServerId, KafkatorioPacket>
): KTable<FactorioServerId, FactorioServerMap> {

  return factorioServerPacketStream
    .groupByKey(
      groupedAs("factorio-server-map-data-grouping", jsonMapper.serde(), jsonMapper.serde())
    )
    .aggregate<FactorioServerId, KafkatorioPacket, FactorioServerMapAggregator>(
      "factorio-server-map-data-aggregate",
      materializedAs("factorio-server-map-aggregate-store", kxsBinary.serde(), kxsBinary.serde()),
      { FactorioServerMapAggregator() },
    ) { _: FactorioServerId, packet: KafkatorioPacket, aggregate: FactorioServerMapAggregator ->
      aggregate + packet
    }
    .mapValues(
      "finalise-factorio-server-map-aggregate",
      materializedWith(kxsBinary.serde(), kxsBinary.serde())
    ) { readOnlyKey, aggregate ->

      val surfaces = aggregate.surfaces.mapValues { (_, surface) ->
        FactorioMapSurface(surface.index, surface.tiles)
      }

      FactorioServerMap(readOnlyKey, aggregate.tilePrototypes, surfaces)
    }

}


@Serializable
private data class FactorioServerMapAggregator(
  val tilePrototypes: MutableMap<PrototypeName, MapTilePrototype> = mutableMapOf(),
  val surfaces: MutableMap<SurfaceIndex, FactorioMapSurfaceAggregator> = mutableMapOf(),
) {

  operator fun plus(packet: KafkatorioPacket): FactorioServerMapAggregator {
    return when (packet) {
      is FactorioEvent               -> {
        when (val data = packet.data) {
          is MapChunk -> plus(data)
          is MapTiles -> plus(data)
//        is MapTile  -> {} // hmmm... no surface index
          else        -> this
        }
      }
      is FactorioPrototypes          -> plus(packet)
      is FactorioConfigurationUpdate -> this
    }
  }

  operator fun plus(mapTiles: MapTiles): FactorioServerMapAggregator {
//    println("FactorioServerMapAggregator: adding ${mapTiles.tiles.size} MapTiles from surface ${mapTiles.surfaceIndex}")

    val surfaceIndex = SurfaceIndex(mapTiles.surfaceIndex)

    val surface = surfaces.getOrPut(surfaceIndex) { FactorioMapSurfaceAggregator(surfaceIndex) }

    val surfaceTiles = mapTiles.tiles.associate { it.position to PrototypeName(it.prototypeName) }
    surface.tiles.putAll(surfaceTiles)

    return this
  }

  operator fun plus(mapChunk: MapChunk): FactorioServerMapAggregator {
//    println("FactorioServerMapAggregator: adding mapChunk ${mapChunk.position}")
    return plus(mapChunk.tiles)
  }

  operator fun plus(packet: FactorioPrototypes): FactorioServerMapAggregator {
//    println("FactorioServerMapAggregator: adding FactorioPrototypes")

    val packetPrototypes = packet.prototypes
      .filterIsInstance<MapTilePrototype>()
      .associateBy { PrototypeName(it.name) }

    tilePrototypes.putAll(packetPrototypes)

    return this
  }

}


@Serializable
private data class FactorioMapSurfaceAggregator(
  val index: SurfaceIndex,
  val tiles: MutableMap<MapTilePosition, PrototypeName> = mutableMapOf(),
)
