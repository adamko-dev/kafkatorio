package dev.adamko.ktrcon

import java.io.*
import java.net.Socket
import java.net.SocketException
import java.nio.BufferUnderflowException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.AsynchronousSocketChannel
import kotlin.coroutines.coroutineContext
import kotlin.coroutines.suspendCoroutine
import kotlin.random.Random
import kotlin.random.nextUInt
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.yield

class KtRconClient(
    private val host: String,
    private val port: Int,
    private val password: Password,
) : AutoCloseable {

  private val rconClientScope = CoroutineScope(SupervisorJob())

  private val socket: Socket = Socket(host, port)

  init {

    runBlocking {
      send(PacketType.AUTH_REQUEST, password())
    }

    require(socket.isConnected) { "Socket must connect" }

    println("socket.isConnected: ${socket.isConnected}")

//    val j = rconClientScope.launch {

    runCatching {
      val reader = socket.getInputStream()
          .bufferedReader()
          .lineSequence()
          .asFlow()
          .onEach {
            println("socket response: [$it]")
          }
          .launchIn(rconClientScope)

//        while (socket.isConnected) {
//          println("response: ${input.read().toByte().toInt().toChar()}")

    println("is listener active? ${reader.isActive}")

    reader.invokeOnCompletion {
      println("cancelled input stream job")
    }

    }
//        }
//        yield()
//      }
//    }


  }

  suspend fun send(type: PacketType, body: String) = supervisorScope {

    val p = Packet(Random.nextUInt(), type, body)

    println("Sending packet: $p - totalSize: ${p.totalSize}")
    println("p.size: ${p.size}")
    println("p.id: ${p.id}")
    println("p.type.value: ${p.type.value}")
//    println("p.bodyBytes: ${p.bodyBytes} / ${p.body}")
//    println("p.terminator: ${p.terminator} / ${p.terminator.decodeToString()}")

//    println("p.byteArray: ${p.toByteArray()}")

    val bb = p.toByteBuffer2()
//        ByteBuffer.wrap(p.toByteArray())
//    bb.order(ByteOrder.LITTLE_ENDIAN)

//    val bb = ByteBuffer.allocate(p.totalSize * 2).apply {
//      order(ByteOrder.LITTLE_ENDIAN)
//      putInt(p.size)
//      putInt(p.id.toInt())
//      putInt(p.type.value)
//      put(p.bodyBytes)
//      put(p.terminator)
//    }

    runCatching {
      socket.getOutputStream().use { os ->
        os.write(bb.array())
        os.flush()
      }
    }
//      socket.channel.finishConnect()
//      socket.getInputStream().use { input ->
//        println("running inputddd")
////        while (true) {
//          println("response: ${input.readAllBytes().decodeToString()}")
//
////          yield()
////        }
//      }
  }

  override fun close() {
    rconClientScope.cancel("Closing ${this::class}")
    socket.close()
  }


}