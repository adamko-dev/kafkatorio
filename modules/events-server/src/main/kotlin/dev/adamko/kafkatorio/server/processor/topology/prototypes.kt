package dev.adamko.kafkatorio.server.processor.topology

import dev.adamko.kafkatorio.library.kxsBinary
import dev.adamko.kafkatorio.schema.common.ColourHex
import dev.adamko.kafkatorio.schema.common.FactorioEntityData
import dev.adamko.kafkatorio.schema.common.FactorioPrototype
import dev.adamko.kafkatorio.schema.common.FactorioServerId
import dev.adamko.kafkatorio.schema.common.PrototypeHashCode
import dev.adamko.kafkatorio.schema.common.PrototypeId
import dev.adamko.kafkatorio.schema.common.toHex
import dev.adamko.kafkatorio.schema.packets.PrototypesUpdate
import dev.adamko.kotka.extensions.groupedAs
import dev.adamko.kotka.extensions.materializedAs
import dev.adamko.kotka.extensions.materializedWith
import dev.adamko.kotka.extensions.*
import dev.adamko.kotka.extensions.streams.*
import dev.adamko.kotka.extensions.streams.mapValues
import dev.adamko.kotka.extensions.streams.toTable
import dev.adamko.kotka.extensions.tables.*
import dev.adamko.kotka.kxs.serde
import kotlinx.serialization.Serializable
import org.apache.kafka.streams.kstream.KStream
import org.apache.kafka.streams.kstream.KTable


@JvmInline
@Serializable
value class TileColourDict(
  private val map: Map<PrototypeHashCode, ColourHex>
) : Map<PrototypeHashCode, ColourHex> by map {
  operator fun plus(other: TileColourDict): TileColourDict = TileColourDict(this.map + other.map)
}


/** Get the latest map-tile colours per server. */
fun KStream<FactorioServerId, PrototypesUpdate>.createTilePrototypeTable()
    : KTable<FactorioServerId, TileColourDict> {

  val pid = "createTilePrototypeTable"

  return mapValues(
    "$pid.map-values"
  ) { _, protoPacket: PrototypesUpdate ->
    val map = protoPacket
      .prototypes
      .filterIsInstance<FactorioPrototype.MapTile>()
      .associate { PrototypeHashCode(it) to it.mapColour.toHex() }

    TileColourDict(map)
  }.filter("$pid.filterMapTileProtos") { _, dict: TileColourDict ->
    dict.isNotEmpty()
  }.peek { serverId, dict: TileColourDict ->
    println("$pid: server $serverId has TileColourDict[${dict.size}]: ${dict.entries.joinToString()}")
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


@JvmInline
@Serializable
value class EntityPrototypeMap(
  private val map: Map<PrototypeHashCode, FactorioPrototype.Entity>
) : Map<PrototypeHashCode, FactorioPrototype.Entity> by map {
  operator fun plus(other: EntityPrototypeMap): EntityPrototypeMap =
    EntityPrototypeMap(this.map + other.map)

  operator fun get(protoId: PrototypeId): FactorioPrototype.Entity? =
    map[PrototypeHashCode(protoId)]

  operator fun get(entity: FactorioEntityData): FactorioPrototype.Entity? =
    map[PrototypeHashCode(entity.protoId)]
}


/** Get the latest entity colours per server. */
fun KStream<FactorioServerId, PrototypesUpdate>.createEntityPrototypeMap(
  callerPid: String
): KTable<FactorioServerId, EntityPrototypeMap> {

  val pid = "$callerPid.createEntityPrototypeMap"

  return mapValues("$pid.colour-dict.build") { _, protoPacket: PrototypesUpdate ->
    val map = protoPacket
      .prototypes
      .filterIsInstance<FactorioPrototype.Entity>()
      .associateBy { entityProto ->
        PrototypeHashCode(entityProto)
      }

    EntityPrototypeMap(map)
  }.filter("$pid.filterEntityProtos") { _, dict: EntityPrototypeMap ->
    dict.isNotEmpty()
  }.groupByKey(groupedAs("$pid.group-by-key"))
    .reduce(
      "$pid.merge",
      materializedWith(
        kxsBinary.serde(),
        kxsBinary.serde(),
      )
    ) { dict1: EntityPrototypeMap, dict2: EntityPrototypeMap ->
      dict1 + dict2
    }.toStream("$pid.")
    .filter("$pid.not-null.filter") { _, v -> v != null }
    .mapValues("$pid.not-null.require") { _, value ->
      requireNotNull(value)
    }.peek { serverId, dict: EntityPrototypeMap ->
      println("$pid: server $serverId has EntityPrototypeMap (size:${dict.size}): ${dict.entries.joinToString()}")
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
