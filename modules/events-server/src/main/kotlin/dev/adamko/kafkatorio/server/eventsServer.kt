package dev.adamko.kafkatorio.server

import dev.adamko.kafkatorio.server.config.ApplicationProperties
import dev.adamko.kafkatorio.server.socket.SyslogSocketServer
import dev.adamko.kafkatorio.server.web.startWebServer
import dev.adamko.kafkatorio.server.web.websocket.WebmapWebsocketServer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


suspend fun main(): Unit = coroutineScope {

  val appProps = ApplicationProperties.load()

  val websocketServer = WebmapWebsocketServer()
  val syslogSocketServer = SyslogSocketServer(appProps)

  launch {
    startWebServer(appProps, websocketServer)
  }


  launch {
    syslogSocketServer.start()
  }


//  launch(Dispatchers.Default.limitedParallelism(2)) {
//    eventsProcessor(appProps, syslogSocketServer, websocketServer)
//  }
}
