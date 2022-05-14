package dev.adamko.kafkatorio.processor.topology

import dev.adamko.kafkatorio.processor.WebsocketServer
import dev.adamko.kafkatorio.processor.admin.topicName
import dev.adamko.kafkatorio.schema.jsonMapperKafkatorio
import dev.adamko.kafkatorio.schema.packets.KafkatorioPacket
import dev.adamko.kafkatorio.schema.packets.PlayerUpdate
import dev.adamko.kotka.extensions.consumedAs
import dev.adamko.kotka.kxs.serde
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.apache.kafka.streams.StreamsBuilder


private val jsonMapper: Json = Json {
  prettyPrint = false
  serializersModule = jsonMapperKafkatorio.serializersModule
}


fun playerUpdatesToWsServer(
  websocketServer: WebsocketServer,
  builder: StreamsBuilder = StreamsBuilder(),
) {
  builder.stream<FactorioServerId, KafkatorioPacket>(
    PlayerUpdate::class.topicName,
    consumedAs("EventUpdate.Player-updates-for-ws-server", jsonMapper.serde(), jsonMapper.serde()),
  ).foreach { _, value ->
//      println("sending ${value.packetType} packet to websocket")
    websocketServer.sendMessage(jsonMapper.encodeToString(value))
  }
}
