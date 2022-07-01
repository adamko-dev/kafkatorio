package dev.adamko.kafkatorio.server

import com.github.palindromicity.syslog.AllowableDeviations
import com.github.palindromicity.syslog.SyslogParser
import com.github.palindromicity.syslog.SyslogParserBuilder
import com.github.palindromicity.syslog.SyslogSpecification
import com.github.palindromicity.syslog.dsl.SyslogFieldKeys
import dev.adamko.kafkatorio.processor.config.ApplicationProperties
import dev.adamko.kafkatorio.server.SyslogMsg.Companion.toSyslogMsg
import io.ktor.network.selector.SelectorManager
import io.ktor.network.sockets.aSocket
import io.ktor.network.sockets.awaitClosed
import io.ktor.network.sockets.openReadChannel
import io.ktor.server.application.Application
import io.ktor.utils.io.readUTF8Line
import java.util.EnumSet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield


fun Application.syslogSocketServer(appProps: ApplicationProperties) {
  val server = SyslogSocketServer(appProps)

  launch {
    server.start()
  }
}


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
    println("[SyslogSocketServer] started listening: ${serverSocketTcp.localAddress}")

    while (isActive) {
      val socket = serverSocketTcp.accept()

      launch {
        val channel = socket.openReadChannel()
        try {
          while (socket.socketContext.isActive) {
            val line = runCatching { channel.readUTF8Line() }.getOrNull()
            if (line != null) {

              println("[SyslogSocketServer] received: $line")

              val syslog = syslogParser.parseLine(line).toSyslogMsg()

              _messages.emit(syslog)
            }

            yield()
          }
        } catch (e: Exception) {
          socket.awaitClosed()
        }
      }

      yield()
    }
  }
}


data class SyslogMsg(
  val src: Map<String, String?>
) {

  val message: String?
    get() = src[SyslogFieldKeys.MESSAGE.field]

  val headerAppName: String?
    get() = src[SyslogFieldKeys.HEADER_APPNAME.field]

  val headerHostName: String?
    get() = src[SyslogFieldKeys.HEADER_HOSTNAME.field]

  val headerPri: String?
    get() = src[SyslogFieldKeys.HEADER_PRI.field]

  val headerPriSeverity: String?
    get() = src[SyslogFieldKeys.HEADER_PRI_SEVERITY.field]

  val headerPriFacility: String?
    get() = src[SyslogFieldKeys.HEADER_PRI_FACILITY.field]

  val headerProcId: String?
    get() = src[SyslogFieldKeys.HEADER_PROCID.field]

  val headerTimestamp: String?
    get() = src[SyslogFieldKeys.HEADER_TIMESTAMP.field]

  val headerMsgId: String?
    get() = src[SyslogFieldKeys.HEADER_MSGID.field]

  val headerVersion: String?
    get() = src[SyslogFieldKeys.HEADER_VERSION.field]

  val structuredBase: String?
    get() = src[SyslogFieldKeys.STRUCTURED_BASE.field]

  val structuredElementIdFmt: String?
    get() = src[SyslogFieldKeys.STRUCTURED_ELEMENT_ID_FMT.field]

  val structuredElementIdPnameFmt: String?
    get() = src[SyslogFieldKeys.STRUCTURED_ELEMENT_ID_PNAME_FMT.field]

  val structuredElementIdPnamePattern: String?
    get() = src[SyslogFieldKeys.STRUCTURED_ELEMENT_ID_PNAME_PATTERN.field]

  companion object {
    fun Map<String, Any?>.toSyslogMsg(): SyslogMsg {
      val map = entries.mapNotNull { (k, v) ->
        when (v) {
          is String -> k to v
          else      -> null
        }
      }.toMap()
      return SyslogMsg(map)
    }
  }
}
