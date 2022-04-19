package dev.adamko.kafkatorio.webmap

import dev.adamko.kafkatorio.schema2.ConfigurationUpdate
import dev.adamko.kafkatorio.schema2.ConsoleChatUpdate
import dev.adamko.kafkatorio.schema2.ConsoleCommandUpdate
import dev.adamko.kafkatorio.schema2.EntityUpdate
import dev.adamko.kafkatorio.schema2.KafkatorioPacket2
import dev.adamko.kafkatorio.schema2.MapChunkUpdate
import dev.adamko.kafkatorio.schema2.PlayerUpdate
import dev.adamko.kafkatorio.schema2.PrototypesUpdate
import dev.adamko.kafkatorio.schema2.SurfaceUpdate
import io.kvision.redux.ReduxStore
import kotlinx.serialization.decodeFromString
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

      val packet: KafkatorioPacket2 = jsonMapper.decodeFromString(data)

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
