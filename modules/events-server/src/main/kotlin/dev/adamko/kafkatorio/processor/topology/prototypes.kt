package dev.adamko.kafkatorio.processor.topology

import dev.adamko.kafkatorio.processor.serdes.kxsBinary
import dev.adamko.kafkatorio.schema.common.ColourHex
import dev.adamko.kafkatorio.schema.common.MapTile
import dev.adamko.kafkatorio.schema.common.toHex
import dev.adamko.kafkatorio.schema.packets.PrototypesUpdate
import dev.adamko.kafkatorio.schema.prototypes.FactorioPrototype
import dev.adamko.kotka.extensions.materializedAs
import dev.adamko.kotka.extensions.repartitionedAs
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
  constructor(tile: MapTile) : this(tile.proto.hashCode())
  constructor(tile: FactorioPrototype.MapTile) : this(tile.name.hashCode())
}


@JvmInline
@Serializable
value class TileColourDict(val map: Map<TileProtoHashCode, ColourHex>)


/** Get the latest map-tile colours per server. */
fun tileProtoColourDictionary(
  factorioServerPacketStream: KStream<FactorioServerId, PrototypesUpdate>
): KTable<FactorioServerId, TileColourDict> {
  val pid = "tileProtoColourDictionary"

  return factorioServerPacketStream.mapValues(
    "$pid.map-values"
  ) { _, protoPacket: PrototypesUpdate ->
    val map = protoPacket
      .prototypes
      .filterIsInstance<FactorioPrototype.MapTile>()
      .associate { TileProtoHashCode(it) to it.mapColour.toHex() }

    TileColourDict(map)
  }.filter("$pid.filterMapTileProtos") { _, dict: TileColourDict ->
    dict.map.isNotEmpty()
  }.peek { serverId, dict: TileColourDict ->
    println("$pid: server $serverId has TileColourDict[${dict.map.size}]: ${dict.map.entries.joinToString()}")
  }.repartition(
    repartitionedAs(
      "$pid.pre-table-repartition",
      kxsBinary.serde(),
      kxsBinary.serde(),
      // force, otherwise KTable-KTable FK join doesn't work
      numberOfPartitions = 1,
    )
  ).toTable(
    "$pid.create-table",
    materializedAs(
      "$pid.output-store",
      kxsBinary.serde(),
      kxsBinary.serde(),
    )
  )
}
