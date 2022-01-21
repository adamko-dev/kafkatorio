package dev.adamko.kafkatorio.processor.topology

import dev.adamko.kafkatorio.events.schema.FactorioPrototypes
import dev.adamko.kafkatorio.events.schema.KafkatorioPacket
import dev.adamko.kafkatorio.events.schema.MapTilePrototype
import dev.adamko.kafkatorio.processor.serdes.jsonMapper
import dev.adamko.kotka.extensions.consumedAs
import dev.adamko.kotka.extensions.materializedWith
import dev.adamko.kotka.extensions.streams.flatMap
import dev.adamko.kotka.kxs.serde
import kotlinx.serialization.Serializable
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.kstream.KTable


@Serializable
@JvmInline
value class PrototypeName(val name: String) {
  override fun toString() = name
}


fun prototypesTable(builder: StreamsBuilder): KTable<PrototypeName, MapTilePrototype> {
  return builder.stream(
    "kafkatorio.${KafkatorioPacket.PacketType.PROTOTYPES}.all",
    consumedAs(
      "consume-all-prototypes-packets",
      Serdes.String(),
      jsonMapper.serde<FactorioPrototypes>()
    )
  )
    .flatMap("map-MapTilePrototype") { _, prototypes: FactorioPrototypes ->
      prototypes.prototypes
        .filterIsInstance<MapTilePrototype>()
        .map { PrototypeName(it.name) to it }
    }
    .toTable(
      materializedWith(
        jsonMapper.serde(),
        jsonMapper.serde()
      )
    )
}


//@Serializable
//data class FactorioServerPrototypesRecord(
//  val serverId: FactorioServerId,
//  val mapTilePrototypes: Map<PrototypeName, MapTilePrototype>,
//)
//
//fun serverPrototypesTable(builder: StreamsBuilder): KTable<FactorioServerId, FactorioServerPrototypesRecord> {
//
//  return builder.stream(
//    "kafkatorio.${KafkatorioPacket.PacketType.PROTOTYPES}.all",
//    consumedAs(
//      "consume-all-prototypes-packets",
//      Serdes.String(),
//      jsonMapper.serde<FactorioPrototypes>()
//    )
//  )
//    .map("build-FactorioServerPrototypesRecord", ::buildPrototypesRecord)
//    .peek { _, value ->
//      println("tile prototypes: ${value.mapTilePrototypes.keys.joinToString()}")
//    }
//    .groupByKey(
//      groupedAs(
//        "grouping-tile-prototypes-by-surface-index",
//        jsonMapper.serde(),
//        jsonMapper.serde()
//      )
//    )
//    .reduce(
//      "group-surface-prototypes",
//      materializedWith(
////          "kafkatorio.surface-prototypes",
//        jsonMapper.serde(),
//        jsonMapper.serde()
//      )
//    ) { value1: FactorioServerPrototypesRecord, value2: FactorioServerPrototypesRecord ->
//      val allTiles = value1.mapTilePrototypes + value2.mapTilePrototypes
//      value1.copy(mapTilePrototypes = allTiles)
//    }
//}
//
//private fun buildPrototypesRecord(
//  serverId: String,
//  prototypes: FactorioPrototypes
//): Pair<FactorioServerId, FactorioServerPrototypesRecord> {
//  val tilePrototypes = prototypes.prototypes
//    .filterIsInstance<MapTilePrototype>()
//    .associateBy { PrototypeName(it.name) }
//
//  val key = FactorioServerId(serverId)
//  val data = FactorioServerPrototypesRecord(key, tilePrototypes)
//  return key to data
//}
