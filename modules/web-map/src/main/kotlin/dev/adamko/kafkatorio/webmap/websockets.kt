package dev.adamko.kafkatorio.webmap

import dev.adamko.kafkatorio.schema.packets.ConfigurationUpdate
import dev.adamko.kafkatorio.schema.packets.ConsoleChatUpdate
import dev.adamko.kafkatorio.schema.packets.ConsoleCommandUpdate
import dev.adamko.kafkatorio.schema.packets.EntityUpdate
import dev.adamko.kafkatorio.schema.packets.EventServerPacket
import dev.adamko.kafkatorio.schema.packets.KafkatorioPacketDataError
import dev.adamko.kafkatorio.schema.packets.MapChunkEntityUpdate
import dev.adamko.kafkatorio.schema.packets.MapChunkResourceUpdate
import dev.adamko.kafkatorio.schema.packets.MapChunkTileUpdate
import dev.adamko.kafkatorio.schema.packets.PlayerUpdate
import dev.adamko.kafkatorio.schema.packets.PrototypesUpdate
import dev.adamko.kafkatorio.schema.packets.SurfaceUpdate
import dev.adamko.kafkatorio.webmap.state.FactorioGameState
import dev.adamko.kafkatorio.webmap.state.FactorioUpdate
import io.kvision.redux.ReduxStore
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.w3c.dom.MessageEvent
import org.w3c.dom.WebSocket
import org.w3c.dom.events.Event


class WebsocketService(
  wsUrl: String = Props.websocketServerUrl,
  private val reduxStore: ReduxStore<FactorioGameState, FactorioUpdate>,
) : CoroutineScope {

  override val coroutineContext: CoroutineContext =
    CoroutineName("WebsocketService") + Job(rootJob)

  private val ws = WebSocket(wsUrl)
//  private val ws = WebSocket("ws://localhost:12080/ws/foo")

  private val _packetsFlow = MutableSharedFlow<EventServerPacket>()
  val packetsFlow: SharedFlow<EventServerPacket>
    get() = _packetsFlow.asSharedFlow()

  init {
    ws.onmessage = ::handleMessageEvent
    ws.onerror = ::handleError

    println("[WebsocketService] init ${ws.url}, ${ws.protocol}")
  }

  private fun handleMessageEvent(msg: MessageEvent) {

    val data = (msg.data as? String)?.trim()

    if (
      data != null
      && data.startsWith("{")
      && data.endsWith("}")
    ) {

      println("[WebsocketService.handleMessageEvent] ${data.replace('\n', ' ')}")

      val packet: EventServerPacket? = runCatching {
        jsonMapper.decodeFromString(EventServerPacket.serializer(), data)
      }.getOrElse {
        null
      }

      if (packet != null) {

        launch { _packetsFlow.emit(packet) }

        when (packet) {
          is EventServerPacket.ChunkTileSaved -> {}

          is EventServerPacket.Kafkatorio     ->
            when (val packetData = packet.packet.data) {
              is PlayerUpdate              ->
                reduxStore.dispatch(FactorioUpdate.Player(packet.packet.tick, packetData))

              is ConfigurationUpdate,
              is ConsoleChatUpdate,
              is ConsoleCommandUpdate,
              is EntityUpdate,
              is MapChunkEntityUpdate,
              is MapChunkResourceUpdate,
              is MapChunkTileUpdate,
              is PrototypesUpdate,
              is SurfaceUpdate,
              is KafkatorioPacketDataError -> {
                // to be continued...
              }
            }
        }
      } else {
        println("[WebsocketService.handleMessageEvent] unknown json message ${msg.data}")
      }
    } else {
      println("[WebsocketService.handleMessageEvent] non-json message ${msg.data}")
    }
  }

  private fun handleError(error: Event) {
    println("ws error: ${JSON.stringify(error)}")
  }
}
