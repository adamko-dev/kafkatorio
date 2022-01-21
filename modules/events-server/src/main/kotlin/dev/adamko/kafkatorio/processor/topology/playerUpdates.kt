package dev.adamko.kafkatorio.processor.topology

import dev.adamko.kafkatorio.events.schema.FactorioObjectData
import dev.adamko.kafkatorio.events.schema.KafkatorioPacket
import dev.adamko.kafkatorio.processor.KafkatorioPacketSerde
import dev.adamko.kafkatorio.processor.WebsocketServer
import dev.adamko.kafkatorio.processor.jsonMapper
import kotlinx.serialization.encodeToString
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.kstream.Consumed


fun playerUpdatesToWsServer(
  websocketServer: WebsocketServer,
  builder: StreamsBuilder = StreamsBuilder(),
) {
  builder.stream(
    "kafkatorio.${KafkatorioPacket.PacketType.EVENT}.${FactorioObjectData.ObjectName.LuaPlayer}",
    Consumed.with(Serdes.String(), KafkatorioPacketSerde)
  )
    .foreach { _, value ->
//        println("sending ${value.packetType} packet to websocket")
      websocketServer.sendMessage(jsonMapper.encodeToString(value))
    }
}
