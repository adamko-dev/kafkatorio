package dev.adamko.kafkatorio.server

//import dev.adamko.kafkatorio.server.config.ApplicationProperties
//import dev.adamko.kafkatorio.server.processor.eventsProcessor
//import dev.adamko.kafkatorio.server.security.Authenticator
//import dev.adamko.kafkatorio.server.socket.SyslogSocketServer
//import dev.adamko.kafkatorio.server.web.startWebServer
//import dev.adamko.kafkatorio.server.web.websocket.WebmapWebsocketServer
//import kotlinx.coroutines.coroutineScope
//import kotlinx.coroutines.launch


//suspend fun main(): Unit = coroutineScope {
//
//  val appProps = ApplicationProperties.load()
//
//  val websocketServer = WebmapWebsocketServer()
//  val syslogSocketServer = SyslogSocketServer(appProps)
//  val authenticator = Authenticator(appProps)
//
//  launch {
//    startWebServer(appProps, websocketServer)
//  }
//
//  launch {
//    syslogSocketServer.start()
//  }
//
//  launch {
//    eventsProcessor(appProps, syslogSocketServer, websocketServer, authenticator)
//  }
//}
