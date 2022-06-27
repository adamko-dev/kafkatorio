package dev.adamko.kafkatorio.processor.topology

import dev.adamko.kafkatorio.processor.admin.TOPIC_BROADCAST_TO_WEBSOCKET
import dev.adamko.kafkatorio.processor.admin.topicName
import dev.adamko.kafkatorio.processor.tileserver.WebsocketServer
import dev.adamko.kafkatorio.schema.common.FactorioServerId
import dev.adamko.kafkatorio.schema.jsonMapperKafkatorio
import dev.adamko.kafkatorio.schema.packets.EventServerPacket
import dev.adamko.kafkatorio.schema.packets.KafkatorioPacket
import dev.adamko.kafkatorio.schema.packets.PlayerUpdate
import dev.adamko.kafkatorio.schema.packets.SurfaceUpdate
import dev.adamko.kotka.extensions.consumedAs
import dev.adamko.kotka.extensions.producedAs
import dev.adamko.kotka.extensions.stream
import dev.adamko.kotka.extensions.streams.forEach
import dev.adamko.kotka.extensions.streams.map
import dev.adamko.kotka.extensions.streams.to
import dev.adamko.kotka.kxs.serde
import kotlinx.coroutines.runBlocking
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

  builder.broadcastKafkatorioPackets()

  builder.stream<FactorioServerId, EventServerPacket>(
    TOPIC_BROADCAST_TO_WEBSOCKET,
    consumedAs("$pid.packets-to-broadcast.consume", jsonMapper.serde(), jsonMapper.serde()),
  ).forEach(
    "$pid.websocket-server.send-message"
  ) { _, esPacket ->
    runBlocking {
      websocketServer.sendMessage(jsonMapper.encodeToString(esPacket))
    }
  }
}


private fun StreamsBuilder.broadcastKafkatorioPackets() {
  val playerUpdatesStream = stream(
    consumedAs(
      "$pid.player-updates",
      jsonMapper.serde(FactorioServerId.serializer()),
      jsonMapper.serde(KafkatorioPacket.serializer()),
    ),
    PlayerUpdate::class.topicName,
    SurfaceUpdate::class.topicName,
  )

  playerUpdatesStream.map<FactorioServerId, KafkatorioPacket, FactorioServerId, EventServerPacket>(
    "$pid.packet-updates.convert"
  ) { serverId, packet ->
    serverId to EventServerPacket.Kafkatorio(packet)
  }.to(
    producedAs(
      "$pid.event-server-packets.produce",
      jsonMapper.serde(FactorioServerId.serializer()),
      jsonMapper.serde(EventServerPacket.serializer()),
    )
  ) { TOPIC_BROADCAST_TO_WEBSOCKET }
}


private fun StreamsBuilder.prepareServerUpdateBroadcast() {

  val surfaceUpdatesStream = stream<FactorioServerId, KafkatorioPacket>(
    SurfaceUpdate::class.topicName,
    consumedAs("$pid.surface-updates", jsonMapper.serde(), jsonMapper.serde()),
  )

  surfaceUpdatesStream.map<FactorioServerId, KafkatorioPacket, FactorioServerId, EventServerPacket>(
    "$pid.surface-updates.convert"
  ) { serverId, serverUpdatePacket ->
    serverId to EventServerPacket.Kafkatorio(serverUpdatePacket)
  }.to(
    producedAs(
      "$pid.player-updates.stream",
      jsonMapper.serde(FactorioServerId.serializer()),
      jsonMapper.serde(EventServerPacket.serializer()),
    )
  ) { TOPIC_BROADCAST_TO_WEBSOCKET }
}
