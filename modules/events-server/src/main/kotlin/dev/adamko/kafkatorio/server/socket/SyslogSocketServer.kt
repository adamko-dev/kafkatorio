package dev.adamko.kafkatorio.server.socket

import com.github.palindromicity.syslog.AllowableDeviations
import com.github.palindromicity.syslog.SyslogParser
import com.github.palindromicity.syslog.SyslogParserBuilder
import com.github.palindromicity.syslog.SyslogSpecification
import dev.adamko.kafkatorio.server.config.ApplicationProperties
import dev.adamko.kafkatorio.server.socket.SyslogMsg.Companion.toSyslogMsg
import io.ktor.network.selector.SelectorManager
import io.ktor.network.sockets.Socket
import io.ktor.network.sockets.aSocket
import io.ktor.network.sockets.openReadChannel
import io.ktor.utils.io.readUTF8Line
import java.util.EnumSet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield


class SyslogSocketServer(
  appProps: ApplicationProperties,
) {

  private val selectorManager: SelectorManager = SelectorManager(Dispatchers.IO)


  private val serverSocketTcp = aSocket(selectorManager)
    .tcp()
    .bind(
      appProps.socketServerHost,
      appProps.socketServerPort,
    )


  private val syslogParser: SyslogParser = SyslogParserBuilder()
    .withDeviations(EnumSet.of(AllowableDeviations.PRIORITY, AllowableDeviations.VERSION))
    .forSpecification(SyslogSpecification.RFC_5424)
    .build()


  private val _messages: MutableSharedFlow<SyslogMsg> = MutableSharedFlow()
  val messages: SharedFlow<SyslogMsg>
    get() = _messages.asSharedFlow()


  suspend fun start(): Unit = coroutineScope {
    log("started listening: ${serverSocketTcp.localAddress}")

    while (isActive) {
      val socket: Socket = serverSocketTcp.accept()
      log("new socket ${socket.description()}")

      launch {
        val channel = socket.openReadChannel()
        try {
          while (socket.socketContext.isActive) {
            val line = runCatching {
              channel.readUTF8Line()
            }.fold(
              onSuccess = { line ->
                log("received '$line' from $socket")
                line
              },
              onFailure = { e ->
                log("reading line from socket failed ${socket.description()}, cause: $e")
                null
              },
            )
            if (line != null) {

              val syslog = syslogParser.parseLine(line).toSyslogMsg()

              _messages.emit(syslog)
            }

            yield()
          }
        } catch (e: Exception) {
          log("closing socket ${socket.description()}, cause: $e")
          withContext(Dispatchers.IO) { socket.close() }
        }
      }

      yield()
    }
  }

  companion object {
    private fun log(msg: String) = println("[SyslogSocketServer] $msg")

    private fun Socket.description(): String = "Socket[local:$localAddress, remote:$remoteAddress]"
  }
}
