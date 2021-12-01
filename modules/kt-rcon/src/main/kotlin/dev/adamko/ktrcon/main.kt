package dev.adamko.ktrcon

import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking {
  KtRconClient3(
    "localhost",
    27715,
    Password("coh9oongoojahX4")
  ).use { client ->

//    client.send(PacketType.AUTH_REQUEST, "passwrd")
    client.send(PacketType.AUTH_REQUEST, "coh9oongoojahX4")
    delay(10.seconds)

    client.send(PacketType.EXEC_COMMAND, "/help")

    delay(10.seconds)
  }
}
