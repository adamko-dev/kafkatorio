package dev.adamko.kafkatorio.webmap

import dev.adamko.kafkatorio.schema.packets.ConfigurationUpdate
import dev.adamko.kafkatorio.schema.packets.ConsoleChatUpdate
import dev.adamko.kafkatorio.schema.packets.ConsoleCommandUpdate
import dev.adamko.kafkatorio.schema.packets.EntityUpdate
import dev.adamko.kafkatorio.schema.packets.KafkatorioPacket
import dev.adamko.kafkatorio.schema.packets.MapChunkUpdate
import dev.adamko.kafkatorio.schema.packets.PlayerUpdate
import dev.adamko.kafkatorio.schema.packets.PrototypesUpdate
import dev.adamko.kafkatorio.schema.packets.SurfaceUpdate
import dev.adamko.kafkatorio.webmap.state.FactorioGameState
import dev.adamko.kafkatorio.webmap.state.FactorioUpdate
import io.kvision.redux.ReduxStore
import org.w3c.dom.MessageEvent
import org.w3c.dom.WebSocket


class WebsocketService(
  private val wsUrl: String,
  private val reduxStore: ReduxStore<FactorioGameState, FactorioUpdate>,
) {
  private val ws = WebSocket(wsUrl)

  init {
    ws.onmessage = ::handleMessageEvent
  }

  private fun handleMessageEvent(msg: MessageEvent) {

    val data = msg.data as? String

    if (
      data != null
      && data.trim().run { startsWith("{") && endsWith("}") }
    ) {

      println(data.replace('\n', ' '))

      val packet = jsonMapper.decodeFromString(KafkatorioPacket.serializer(), data)

      when (val packetData = packet.data) {

        is PlayerUpdate   -> reduxStore.dispatch(FactorioUpdate.Player(packet.tick, packetData))
        is ConfigurationUpdate,
        is ConsoleChatUpdate,
        is ConsoleCommandUpdate,
        is PrototypesUpdate,
        is SurfaceUpdate,
        is EntityUpdate,
        is MapChunkUpdate -> {
          // to be continued...
        }
      }
    } else {
      println(data)
    }
  }
}
