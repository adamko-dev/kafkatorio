package dev.adamko.kafkatorio.server.socket

import com.github.palindromicity.syslog.AllowableDeviations
import com.github.palindromicity.syslog.DefaultKeyProvider
import com.github.palindromicity.syslog.Flat5424MessageHandler
import com.github.palindromicity.syslog.NilPolicy
import com.github.palindromicity.syslog.SyslogParser
import com.github.palindromicity.syslog.SyslogParserBuilder
import com.github.palindromicity.syslog.SyslogSpecification
import dev.adamko.kafkatorio.server.config.ApplicationProperties
import io.ktor.network.selector.SelectorManager
import io.ktor.network.sockets.Socket
import io.ktor.network.sockets.aSocket
import io.ktor.network.sockets.openReadChannel
import io.ktor.utils.io.readUTF8Line
import java.util.EnumSet
import kotlinx.coroutines.CancellationException
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

  /** Incoming messages */
  private val serverSocketTcp = aSocket(SelectorManager(Dispatchers.IO))
    .tcp()
    .bind(
      appProps.socketServerHost,
      appProps.socketServerPort,
    )


  private val syslogParser: SyslogParser<Map<String, String>> =
    SyslogParserBuilder<Map<String, String>>()
      .forSpecification(SyslogSpecification.RFC_5424)
      .withSyslogBuilder(
        Flat5424MessageHandler(
          DefaultKeyProvider(),
          NilPolicy.OMIT,
          EnumSet.of(AllowableDeviations.PRIORITY, AllowableDeviations.VERSION),
        )
      ).build()


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
          while (socket.socketContext.isActive && !channel.isClosedForRead) {
            val line = runCatching {
              channel.readUTF8Line()
            }.fold(
              onSuccess = { line ->
                if (line == null) {
                  log("received 'null' from ${socket.description()} - closing connection")
                  socket.close()
                }
                // log("received '$line' from ${socket.description()}")
                line
              },
              onFailure = { ex ->
                if (ex is CancellationException) throw ex

                log("reading line from socket failed ${socket.description()}, cause: $ex")
                null
              },
            )
            if (line != null) {
              val syslog = SyslogMsg(syslogParser.parseLine(line))
              _messages.emit(syslog)
            }
            yield()
          }
        } catch (e: Exception) {
          if (e is CancellationException) throw e
          log("closing socket ${socket.description()}, cause: ${e::class.java.name} ${e.message}")
          e.printStackTrace()
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
