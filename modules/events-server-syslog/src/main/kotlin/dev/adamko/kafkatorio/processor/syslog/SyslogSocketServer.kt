package dev.adamko.kafkatorio.processor.syslog

import com.github.palindromicity.syslog.AllowableDeviations
import com.github.palindromicity.syslog.DefaultKeyProvider
import com.github.palindromicity.syslog.Flat5424MessageHandler
import com.github.palindromicity.syslog.NilPolicy
import com.github.palindromicity.syslog.SyslogParser
import com.github.palindromicity.syslog.SyslogParserBuilder
import com.github.palindromicity.syslog.SyslogSpecification
import com.github.palindromicity.syslog.dsl.ParseException
import dev.adamko.kafkatorio.processor.config.ApplicationProperties
import io.ktor.network.selector.SelectorManager
import io.ktor.network.sockets.Socket
import io.ktor.network.sockets.aSocket
import io.ktor.network.sockets.openReadChannel
import io.ktor.utils.io.readUTF8Line
import java.util.EnumSet
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
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


  suspend fun start(): Unit = withContext(Dispatchers.IO) {
    log("started listening: ${serverSocketTcp.localAddress}")

    while (isActive) {
      val socket: Socket = serverSocketTcp.accept()
      launch {
        log("new socket ${socket.description()}")
        handleConnection(socket)
      }
      yield()
    }
  }


  private suspend fun handleConnection(
    socket: Socket
  ): Unit = supervisorScope {
    try {
      val channel = socket.openReadChannel()
      while (socket.socketContext.isActive && !channel.isClosedForRead) {

        val line = channel.readUTF8Line()

        requireNotNull(line) { "received 'null' line from ${socket.description()}" }

        try {
          val messageContents = syslogParser.parseLine(line)
          val syslog = SyslogMsg(messageContents)
          _messages.emit(syslog)
        } catch (ex: ParseException) {
          log("ignoring non-syslog message: $line")
        }

        yield()
      }
    } catch (ex: Throwable) {
      withContext(Dispatchers.IO) { socket.close() }
      if (ex is CancellationException) {
        throw ex
      } else {
        log("closing socket ${socket.description()}, cause: ${ex::class.qualifiedName} ${ex.message}")
        ex.printStackTrace()
      }
    }
  }


  companion object {
    private fun log(msg: String) = println("[SyslogSocketServer] $msg")

    private fun Socket.description(): String = "Socket[local:$localAddress, remote:$remoteAddress]"
  }
}
