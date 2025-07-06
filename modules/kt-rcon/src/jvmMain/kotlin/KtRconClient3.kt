package dev.adamko.ktrcon

import io.ktor.network.selector.ActorSelectorManager
import io.ktor.network.sockets.InetSocketAddress
import io.ktor.network.sockets.aSocket
import io.ktor.network.sockets.openReadChannel
import io.ktor.network.sockets.openWriteChannel
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.ByteWriteChannel
import io.ktor.utils.io.core.isNotEmpty
import io.ktor.utils.io.core.readBytes
import kotlin.random.Random
import kotlin.random.nextUInt
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.yield

class KtRconClient3(
  private val host: String,
  private val port: Int,
  private val password: Password,
) : AutoCloseable {

  private val rconClientJob = SupervisorJob()
  private val rconClientScope = CoroutineScope(rconClientJob)

  private val input: ByteReadChannel
  private val output: ByteWriteChannel

  init {
    val socket = runBlocking {
      val selector = ActorSelectorManager(rconClientJob)
      aSocket(selector)
        .tcp()
        .connect(InetSocketAddress(host, port))
    }
    input = socket.openReadChannel()
    output = socket.openWriteChannel()

//    runBlocking {
//      output.writeStringUtf8("test")
//      output.flush()
//      delay(1000)
//      println("availableForRead: ${input.availableForRead}")
//
//    }

    rconClientScope.launch {
      while (!input.isClosedForRead) {
        val packet = input.readRemaining(1)
        while (packet.isNotEmpty) {
          val bytes = packet.readBytes()
          println(
            "received ${bytes.size} bytes: ${bytes.joinToString { "$it" }} - ${bytes.decodeToString()}"
          )
          yield()
        }
        yield()
      }
    }
  }

  suspend fun send(type: PacketType, body: String) = supervisorScope {

    val p = Packet(Random.nextUInt(1u..100u), type, body)

    println("Sending packet: $p - totalSize: ${p.totalSize}")
    println("p.size: ${p.size}")
    println("p.totalSize: ${p.totalSize}")
    println("p.id: ${p.id}")
    println("p.type.value: ${p.type.value}")
//    println("p.bodyBytes: ${p.bodyBytes} / ${p.body}")
//    println("p.terminator: ${p.terminator} / ${p.terminator.decodeToString()}")

//    println("p.byteArray: ${p.toByteArray()}")
    println("p.byteArray: ${p.toByteBuffer2().array().joinToString { it.toString() }}")


    val bb = p.toByteBuffer2()
    println("bb: ${bb.position()} - ${bb.toString()}")

    bb.array().forEach {
      print("-$it")
      output.writeByte(it)
    }
//    output.writeFully(bb)
//    output.writeFully(bb)
    output.flush()
    println("finished writing")

//    if (type == PacketType.EXEC_COMMAND) {
//      println("awaiting response from server")
//      input.read(0) {
//        println("server response: ${it.array()}")
//      }
////    val response = input.readUTF8Line()
////    println("Server said $response")
//    }

  }

  override fun close() {
    rconClientJob.complete()
    rconClientScope.cancel("Closing ${this::class}")
  }

}
