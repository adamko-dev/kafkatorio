package dev.adamko.kafkatorio.processor.topology

import dev.adamko.kafkatorio.events.schema.ColourHex
import dev.adamko.kafkatorio.events.schema.FactorioPrototypes
import dev.adamko.kafkatorio.events.schema.KafkatorioPacket
import dev.adamko.kafkatorio.events.schema.MapTile
import dev.adamko.kafkatorio.events.schema.MapTilePrototype
import dev.adamko.kafkatorio.events.schema.converters.toHex
import dev.adamko.kafkatorio.processor.serdes.jsonMapper
import dev.adamko.kotka.extensions.materializedAs
import dev.adamko.kotka.extensions.streams.filter
import dev.adamko.kotka.extensions.streams.mapValues
import dev.adamko.kotka.extensions.streams.toTable
import dev.adamko.kotka.kxs.serde
import kotlinx.serialization.Serializable
import org.apache.kafka.streams.kstream.KStream
import org.apache.kafka.streams.kstream.KTable


@Serializable
@JvmInline
value class PrototypeName(val name: String) {
  override fun toString() = name
}


/** Use the hashcode of Prototypes names as keys - more efficient storage */
@JvmInline
@Serializable
value class TileProtoHashCode private constructor(val code: Int) {
  constructor(prototype: MapTilePrototype) : this(prototype.name.hashCode())
  constructor(tile: MapTile) : this(tile.prototypeName.hashCode())
}


@JvmInline
@Serializable
value class TileColourDict(val map: Map<TileProtoHashCode, ColourHex>)


/** Get the latest map-tile colours per server. */
fun tileProtoColourDictionary(
  factorioServerPacketStream: KStream<FactorioServerId, FactorioPrototypes>
): KTable<FactorioServerId, TileColourDict> {

  // get all the prototypes
  return factorioServerPacketStream
    .mapValues("server-map-data.tile-prototypes.mapValues") { _, packet: KafkatorioPacket ->
      val map = when (packet) {
        is FactorioPrototypes -> packet
          .prototypes
          .filterIsInstance<MapTilePrototype>()
          .associate { TileProtoHashCode(it) to it.mapColour.toHex() }
        else                  -> mapOf()
      }
      TileColourDict(map)
    }.filter("server-map-data.tile-prototypes.filterMapTileProtos") { _, dict: TileColourDict ->
      dict.map.isNotEmpty()
    }.peek { serverId, dict: TileColourDict ->
      println("server $serverId has TileColourDict[${dict.map.size}]: ${dict.map.entries.joinToString()}")
    }
    .toTable(
      "server-map-data.tile-prototypes",
      materializedAs(
        "server-map-data.tile-prototypes.store",
        jsonMapper.serde(),
        jsonMapper.serde(),
      )
    )

}
