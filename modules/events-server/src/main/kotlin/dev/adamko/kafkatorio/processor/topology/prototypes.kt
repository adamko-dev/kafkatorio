package dev.adamko.kafkatorio.processor.topology

import com.sksamuel.scrimage.color.RGBColor
import dev.adamko.kafkatorio.events.schema.Colour
import dev.adamko.kafkatorio.events.schema.FactorioPrototypes
import dev.adamko.kafkatorio.events.schema.KafkatorioPacket
import dev.adamko.kafkatorio.events.schema.MapTilePrototype
import dev.adamko.kafkatorio.processor.serdes.jsonMapper
import dev.adamko.kafkatorio.processor.serdes.kxsBinary
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


//fun prototypesTable(builder: StreamsBuilder): KTable<PrototypeName, MapTilePrototype> {
//  return builder.stream<FactorioPacketKey, FactorioPrototypes>(
//    "kafkatorio.${KafkatorioPacket.PacketType.PROTOTYPES}.all",
//    consumedAs(
//      "consume-all-prototypes-packets",
//      jsonMapper.serde(),
//      jsonMapper.serde()
//    )
//  )
//    .flatMap("map-MapTilePrototype") { _, prototypes: FactorioPrototypes ->
//      prototypes.prototypes
//        .filterIsInstance<MapTilePrototype>()
//        .map { PrototypeName(it.name) to it }
//    }
//    .toTable(
//      materializedWith(
//        kxsBinary.serde(),
//        kxsBinary.serde()
//      )
//    )
//}


/** Get the [Colour] of each [MapTilePrototype] */
fun tilePrototypeColourTable(builder: StreamsBuilder): KTable<PrototypeName, Colour> {
  return builder.stream<FactorioPacketKey, FactorioPrototypes>(
    "kafkatorio.${KafkatorioPacket.PacketType.PROTOTYPES}.all",
    consumedAs(
      "consume-all-prototypes-packets",
      jsonMapper.serde(),
      jsonMapper.serde()
    )
  )
    .flatMap("map-MapTilePrototype") { _, prototypes: FactorioPrototypes ->
      prototypes.prototypes
        .filterIsInstance<MapTilePrototype>()
        .map {
          PrototypeName(it.name) to  it.mapColour
        }
    }
    .toTable(
      materializedWith(
        kxsBinary.serde(),
        kxsBinary.serde()
      )
    )
}
