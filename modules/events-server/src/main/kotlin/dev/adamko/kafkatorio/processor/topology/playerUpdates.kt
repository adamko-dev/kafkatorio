package dev.adamko.kafkatorio.processor.topology

import dev.adamko.kafkatorio.processor.WebsocketServer
import dev.adamko.kafkatorio.processor.admin.TOPIC_BROADCAST_TO_WEBSOCKET
import dev.adamko.kafkatorio.processor.admin.topicName
import dev.adamko.kafkatorio.schema.common.FactorioServerId
import dev.adamko.kafkatorio.schema.jsonMapperKafkatorio
import dev.adamko.kafkatorio.schema.packets.EventServerPacket
import dev.adamko.kafkatorio.schema.packets.KafkatorioPacket
import dev.adamko.kafkatorio.schema.packets.PlayerUpdate
import dev.adamko.kotka.extensions.consumedAs
import dev.adamko.kotka.extensions.producedAs
import dev.adamko.kotka.extensions.streams.forEach
import dev.adamko.kotka.extensions.streams.map
import dev.adamko.kotka.kxs.serde
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.apache.kafka.streams.StreamsBuilder


private val jsonMapper: Json = Json {
  prettyPrint = false
  serializersModule = jsonMapperKafkatorio.serializersModule
}


private const val pid = "websocketBroadcast"


fun broadcastToWebsocket(
  websocketServer: WebsocketServer,
  builder: StreamsBuilder = StreamsBuilder(),
) {

  builder.preparePlayerUpdatesBroadcast()

  builder.stream<FactorioServerId, EventServerPacket>(
    TOPIC_BROADCAST_TO_WEBSOCKET,
    consumedAs("$pid.packets-to-broadcast.consume", jsonMapper.serde(), jsonMapper.serde()),
  ).forEach(
    "$pid.websocket-server.send-message"
  ) { _, esPacket ->
    websocketServer.sendMessage(jsonMapper.encodeToString(esPacket))
  }
}


private fun StreamsBuilder.preparePlayerUpdatesBroadcast() {
  stream<FactorioServerId, KafkatorioPacket>(
    PlayerUpdate::class.topicName,
    consumedAs("$pid.player-updates", jsonMapper.serde(), jsonMapper.serde()),
  ).map<FactorioServerId, KafkatorioPacket, FactorioServerId, EventServerPacket>(
    "$pid.player-updates.convert"
  ) { serverId, playerPacket ->
    serverId to EventServerPacket.Kafkatorio(playerPacket)
  }.to(
    TOPIC_BROADCAST_TO_WEBSOCKET,
    producedAs(
      "$pid.player-updates.stream",
      jsonMapper.serde(FactorioServerId.serializer()),
      jsonMapper.serde(EventServerPacket.serializer()),
    )
  )
}
