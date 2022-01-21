package dev.adamko.kafkatorio.processor.topology

//import dev.adamko.kafkatorio.events.schema.KafkatorioPacket
//import dev.adamko.kafkatorio.events.schema.MapChunkPosition
//import dev.adamko.kafkatorio.events.schema.MapTilePosition
//import dev.adamko.kafkatorio.events.schema.MapTilePrototype
//import dev.adamko.kafkatorio.processor.serdes.jsonMapper
import kotlinx.serialization.Serializable
//import kotlinx.serialization.decodeFromString
//import org.apache.kafka.common.serialization.Serdes
//import org.apache.kafka.streams.StreamsBuilder
//import org.apache.kafka.streams.kstream.Consumed
//import org.apache.kafka.streams.kstream.KGroupedStream

private const val sourceTopic = "factorio-server-log"

@JvmInline
@Serializable
value class FactorioServerId(val id: String) {
  override fun toString() = id
}

//@Serializable
//data class FactorioServerState(
//  val serverId: FactorioServerId,
//  val mapTilePrototypes: Set<MapTilePrototype>,
//  val surfaceIndexes: Set<SurfaceIndex>,
//)
//
//
//private fun gatherGameState(builder: StreamsBuilder = StreamsBuilder()) {
//
//  val serverPackets: KGroupedStream<String, KafkatorioPacket> =
//    builder.stream(
//      sourceTopic,
//      Consumed.with(Serdes.String(), Serdes.String())
//    )
//      .mapValues { readOnlyKey, value ->
//        println("Mapping $readOnlyKey:$value")
//        jsonMapper.decodeFromString<KafkatorioPacket>(value)
//      }
//      .groupByKey()
//
//  @Serializable
//  data class TileSurfaceData(
//    val surfaceIndex: Int,
//    val position: MapTilePosition,
//    val prototype: MapTilePrototype,
//  )
//
//  @Serializable
//  data class MapChunkData(
//    val position: MapChunkPosition,
//    val tiles: List<TileSurfaceData>,
//  )
//
//  @Serializable
//  data class FactorioSurfaceData(
//    val surfaceIndex: Int,
//    val chunks: List<MapChunkData>,
//  )
//
//  @Serializable
//  data class FactorioServerState(
//    val surfaces: Map<Int, FactorioSurfaceData>,
//    val tilePrototypes: List<MapTilePrototype>,
//  )
//
//
////    serverPackets
////      .aggregate<FactorioServerState>(
////        Initializer { FactorioServerState("", emptyMap(), emptyList()) }
////      )
//}
