package dev.adamko.kafkatorio.webmap

import dev.adamko.kafkatorio.events.schema.FactorioEvent
import dev.adamko.kafkatorio.events.schema.FactorioObjectData
import dev.adamko.kafkatorio.events.schema.PlayerData
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
      && data.trim().startsWith("{")
      && data.trim().endsWith("}")
    ) {

      println(data.replace('\n', ' '))
      val event: FactorioEvent<FactorioObjectData> = jsonMapper.decodeFromString(data)

      when (val eventData = event.data) {
        is PlayerData -> {
          reduxStore.dispatch(FactorioUpdate.PlayerUpdate(event, eventData))
        }
        else          -> {}
      }
    } else {
      println(data)

    }

  }
}
