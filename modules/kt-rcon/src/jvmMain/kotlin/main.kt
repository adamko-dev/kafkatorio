package dev.adamko.ktrcon

import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking {
  KtRconClient3(
    "localhost",
    27015,
    Password("chofeengook8Shu")
  ).use { client ->

//    client.send(PacketType.AUTH_REQUEST, "passwrd")
    client.send(PacketType.AUTH_REQUEST, "chofeengook8Shu")
    delay(10.seconds)

    client.send(PacketType.EXEC_COMMAND, "/help")

    delay(10.seconds)
  }
}
