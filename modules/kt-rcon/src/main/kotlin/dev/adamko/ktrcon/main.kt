package dev.adamko.ktrcon

import kotlin.time.Duration
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
    delay(Duration.seconds(10))

    client.send(PacketType.EXEC_COMMAND, "/help")


    delay(Duration.seconds(10))

  }
}
