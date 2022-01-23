package dev.adamko.kafkatorio.processor.topology

import dev.adamko.kafkatorio.events.schema.ConsoleChatMessage
import dev.adamko.kafkatorio.events.schema.EntityData
import dev.adamko.kafkatorio.events.schema.FactorioConfigurationUpdate
import dev.adamko.kafkatorio.events.schema.FactorioEvent
import dev.adamko.kafkatorio.events.schema.FactorioPrototypes
import dev.adamko.kafkatorio.events.schema.KafkatorioPacket
import dev.adamko.kafkatorio.events.schema.MapChunk
import dev.adamko.kafkatorio.events.schema.MapTile
import dev.adamko.kafkatorio.events.schema.MapTiles
import dev.adamko.kafkatorio.events.schema.PlayerData
import dev.adamko.kafkatorio.events.schema.SurfaceData
import dev.adamko.kafkatorio.events.schema.converters.toMapChunkPosition
import dev.adamko.kafkatorio.processor.KafkatorioTopology
import dev.adamko.kafkatorio.processor.serdes.jsonMapper
import dev.adamko.kotka.extensions.consumedAs
import dev.adamko.kotka.extensions.producedAs
import dev.adamko.kotka.extensions.streams.map
import dev.adamko.kotka.extensions.streams.to
import dev.adamko.kotka.kxs.serde
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.processor.RecordContext


@Serializable
data class FactorioPacketKey(
  val serverId: FactorioServerId,
  /** arbitrary distinctive key */
  val key: String,
)

fun splitFactorioServerLog(
  builder: StreamsBuilder = StreamsBuilder(),
) {

  builder.stream(
    KafkatorioTopology.sourceTopic,
    consumedAs(
      "read-raw-packets-from-server",
      Serdes.String(),
      Serdes.String(),
    )
  )
    .map("decode-packets") { serverId: String, value: String ->
//        println("Mapping $readOnlyKey:$value")
      val packet = jsonMapper.decodeFromString<KafkatorioPacket>(value)
      val key: String = createPacketKey(packet)
      FactorioPacketKey(FactorioServerId(serverId), key) to packet
    }
    .to(
      producedAs(
        "split-server-log",
        jsonMapper.serde(),
        jsonMapper.serde(),
      )
    ) { _: FactorioPacketKey, value: KafkatorioPacket, _: RecordContext ->
//        println("[$key] sending event:${value.eventType} to topic:${value.data.objectName()}")
      when (value) {
        is FactorioEvent               ->
          "kafkatorio.${value.packetType.name}.${value.data.objectName.name}"
        is FactorioConfigurationUpdate ->
          "kafkatorio.${value.packetType.name}.FactorioConfigurationUpdate"
        is FactorioPrototypes          ->
          "kafkatorio.${value.packetType.name}.all"
      }
    }

}

private fun createPacketKey(packet: KafkatorioPacket): String {

  return when (packet) {
    is FactorioEvent      -> factorioEventKey(packet)
    is FactorioConfigurationUpdate,
    is FactorioPrototypes -> packet.hashCode().toString()
  }
}

private fun factorioEventKey(event: FactorioEvent): String {
  val id: String = when (val data = event.data) {
    is ConsoleChatMessage -> data.authorPlayerIndex?.toString()
    is EntityData         -> data.type
    is MapChunk           -> data.position.toString()
    is MapTile            -> data.position.toString()
    is MapTiles           -> data.tiles.firstOrNull()?.position?.toMapChunkPosition()?.toString()
    is PlayerData         -> (data.characterUnitNumber?.toString() ?: data.name)
    is SurfaceData        -> data.index.toString()
  } ?: event.tick.toString()

  return id
}
