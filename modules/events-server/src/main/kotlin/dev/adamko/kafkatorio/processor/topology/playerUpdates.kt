package dev.adamko.kafkatorio.processor.topology

import dev.adamko.kafkatorio.events.schema.FactorioObjectData
import dev.adamko.kafkatorio.events.schema.KafkatorioPacket
import dev.adamko.kafkatorio.processor.WebsocketServer
import dev.adamko.kafkatorio.processor.serdes.jsonMapper
import dev.adamko.kotka.extensions.consumedAs
import dev.adamko.kotka.kxs.serde
import kotlinx.serialization.encodeToString
import org.apache.kafka.streams.StreamsBuilder


fun playerUpdatesToWsServer(
  websocketServer: WebsocketServer,
  builder: StreamsBuilder = StreamsBuilder(),
) {
  builder.stream<FactorioPacketKey, KafkatorioPacket>(
    "kafkatorio.${KafkatorioPacket.PacketType.EVENT}.${FactorioObjectData.ObjectName.LuaPlayer}",
    consumedAs("stream-player-updates-for-ws-server", jsonMapper.serde(), jsonMapper.serde())
  )
    .foreach { _, value ->
//      println("sending ${value.packetType} packet to websocket")
      websocketServer.sendMessage(jsonMapper.encodeToString(value))
    }
}
