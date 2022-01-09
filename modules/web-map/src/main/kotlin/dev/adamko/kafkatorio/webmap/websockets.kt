package dev.adamko.kafkatorio.webmap

import dev.adamko.kafkatorio.events.schema.ConsoleChatMessage
import dev.adamko.kafkatorio.events.schema.EntityData
import dev.adamko.kafkatorio.events.schema.FactorioConfigurationUpdate
import dev.adamko.kafkatorio.events.schema.FactorioEvent
import dev.adamko.kafkatorio.events.schema.MapChunk
import dev.adamko.kafkatorio.events.schema.MapTile
import dev.adamko.kafkatorio.events.schema.KafkatorioPacket
import dev.adamko.kafkatorio.events.schema.PlayerData
import dev.adamko.kafkatorio.events.schema.SurfaceData
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

      when (val event: KafkatorioPacket = jsonMapper.decodeFromString(data)) {
        is FactorioEvent<*>            -> {
          when (val eventData = event.data) {
            is PlayerData         ->
              reduxStore.dispatch(FactorioUpdate.PlayerUpdate(event, eventData))
            is ConsoleChatMessage -> {}
            is EntityData -> {}
            is MapChunk   -> {}
            is MapTile    -> {}
            is SurfaceData      -> {}
          }
        }
        is FactorioConfigurationUpdate -> {}
      }

    } else {
      println(data)
    }

  }
}
