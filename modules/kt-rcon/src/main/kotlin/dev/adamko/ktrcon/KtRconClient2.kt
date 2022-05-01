package dev.adamko.ktrcon

import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.AsynchronousSocketChannel
import kotlin.random.Random
import kotlin.random.nextUInt
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.supervisorScope

class KtRconClient2(
  private val host: String,
  private val port: Int,
  private val password: Password,
) : AutoCloseable {

  private val rconClientScope = CoroutineScope(SupervisorJob())

  private val client: AsynchronousSocketChannel by lazy {
    println("Initialising client")

    val client = AsynchronousSocketChannel.open()
    val address = InetSocketAddress(host, port)
    val result = client.connect(address)
    result.get() //.get(2, TimeUnit.SECONDS)

    println("connected to async - $address")
    require(client.isOpen) { "Require client is open" }

    client
  }

  init {
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
//    println("p.byteArray: ${p.toByteBuffer().array()}")


    runCatching {
      val bb = p.toByteBuffer2()
//          ByteBuffer.wrap(p.toByteArray())
//      bb.order(ByteOrder.LITTLE_ENDIAN)

      println("writing bb to client")

      val future = client.write(bb)

      delay(2000)

      val writeResult = future.get() //.get(2, TimeUnit.SECONDS)
      delay(2000)

      println("write result: $writeResult")


      val response = ByteBuffer.allocate(Int.SIZE_BYTES)
      val responseStatus = runCatching {
        client.read(response).get() //.get(2, TimeUnit.SECONDS)
      }
      println("responseStatus: $responseStatus")
      println("response from server: ${response.array().decodeToString()}")
      delay(2000)
    }

  }

  override fun close() {
    rconClientScope.cancel("Closing ${this::class}")
    client.shutdownInput()
    client.close()
  }

}
