package dev.adamko.dokalo

import com.typesafe.config.ConfigFactory
import io.ktor.server.application.install
import io.ktor.server.config.HoconApplicationConfig
import io.ktor.server.engine.applicationEngineEnvironment
import io.ktor.server.engine.connector
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.resources.Resources


fun main() {

  embeddedServer(Netty, port = 8080) {

    install(Resources)

    configureRouting()
  }.start(wait = true)


//  embeddedServer(Netty, environment = applicationEngineEnvironment {
//
//    config = HoconApplicationConfig(ConfigFactory.load())
//
//    module {
//      main()
//    }
//
//    connector {
//      port = 8080
//      host = "127.0.0.1"
//    }
//  }).start(wait = true)

}
