package dev.adamko.kafkatorio.processor.topology

import dev.adamko.kafkatorio.processor.serdes.kxsBinary
import dev.adamko.kafkatorio.schema.common.ColourHex
import dev.adamko.kafkatorio.schema.common.MapTile
import dev.adamko.kafkatorio.schema.common.toHex
import dev.adamko.kafkatorio.schema.prototypes.FactorioPrototype2
import dev.adamko.kafkatorio.schema.prototypes.MapTilePrototype
import dev.adamko.kafkatorio.schema2.PrototypesUpdate
import dev.adamko.kotka.extensions.materializedAs
import dev.adamko.kotka.extensions.streams.filter
import dev.adamko.kotka.extensions.streams.mapValues
import dev.adamko.kotka.extensions.streams.toTable
import dev.adamko.kotka.kxs.serde
import kotlinx.serialization.Serializable
import org.apache.kafka.streams.kstream.KStream
import org.apache.kafka.streams.kstream.KTable


/** Use the hashcode of Prototypes names as keys - more efficient storage */
@JvmInline
@Serializable
value class TileProtoHashCode private constructor(val code: Int) {
  constructor(prototype: MapTilePrototype) : this(prototype.name.hashCode())
  constructor(tile: MapTile) : this(tile.proto.hashCode())
  constructor(tile: FactorioPrototype2.MapTile) : this(tile.name.hashCode())
}


@JvmInline
@Serializable
value class TileColourDict(val map: Map<TileProtoHashCode, ColourHex>)


/** Get the latest map-tile colours per server. */
fun tileProtoColourDictionary(
  factorioServerPacketStream: KStream<FactorioServerId, PrototypesUpdate>
): KTable<FactorioServerId, TileColourDict> {

  return factorioServerPacketStream.mapValues("server-map-data.tile-prototypes.map-values") { _, protoPacket: PrototypesUpdate ->
    val map = protoPacket
      .prototypes
      .filterIsInstance<FactorioPrototype2.MapTile>()
      .associate { TileProtoHashCode(it) to it.mapColour.toHex() }

    TileColourDict(map)
  }.filter("server-map-data.tile-prototypes.filterMapTileProtos") { _, dict: TileColourDict ->
    dict.map.isNotEmpty()
  }.peek { serverId, dict: TileColourDict ->
    println("server $serverId has TileColourDict[${dict.map.size}]: ${dict.map.entries.joinToString()}")
  }.toTable(
    "server-map-data.tile-prototypes",
    materializedAs(
      "server-map-data.tile-prototypes.store",
      kxsBinary.serde(),
      kxsBinary.serde(),
    )
  )
}

// get all the prototypes
//  return factorioServerPacketStream
//    .mapValues("server-map-data.tile-prototypes.mapValues") { _, packet: KafkatorioPacket ->
//      val map = when (packet) {
//        is FactorioPrototype -> packet
//          .prototypes
//          .filterIsInstance<MapTilePrototype>()
//          .associate { TileProtoHashCode(it) to it.mapColour.toHex() }
//        else                 -> mapOf()
//      }
//      TileColourDict(map)
//    }.filter("server-map-data.tile-prototypes.filterMapTileProtos") { _, dict: TileColourDict ->
//      dict.map.isNotEmpty()
//    }.peek { serverId, dict: TileColourDict ->
//      println("server $serverId has TileColourDict[${dict.map.size}]: ${dict.map.entries.joinToString()}")
//    }
//    .toTable(
//      "server-map-data.tile-prototypes",
//      materializedAs(
//        "server-map-data.tile-prototypes.store",
//        kxsBinary.serde(),
//        kxsBinary.serde(),
//      )
//    )
//
//}
