package dev.adamko.ktrcon

import java.nio.ByteBuffer
import java.nio.ByteOrder


/**
 * @param[size] The packet size field is a 32-bit little endian integer, representing the length of the
 * request in bytes. Note that the packet size field itself is not included when determining the
 * size of the packet, so the value of this field is always 4 less than the packet's actual
 * length.
 *
 * The minimum possible value for packet size is 10. The maximum possible value of packet size
 * is 4096. If the response is too large to fit into one packet, it will be split and sent as
 * multiple packets.
 *
 * @param[id] The packet id field is a 32-bit little endian integer chosen by the client for each
 * request. It may be set to any positive integer. When the server responds to the request, the
 * response packet will have the same packet id as the original request (unless it is a failed
 * [PacketType.AUTH_RESPONSE] packet.) It need not be unique, but if a unique packet id is
 * assigned, it can be used to match incoming responses to their corresponding requests.
 *
 * @param[type] See [PacketType]
 *
 * @param[body] The packet body field is a null-terminated string encoded in ASCII (i.e. ASCIIZ).
 * Depending on the packet type, it may contain either the RCON password for the server, the command
 * to be executed, or the server's response to a request.
 */
data class Packet(
  val id: UInt,
  val type: PacketType,
  val bodyRaw: String,
) {
  private val bodyTerminator: Byte = NULL_CHAR_BYTE

  private val body: ByteArray = bodyRaw.toByteArray(Charsets.UTF_8)

//  val bodyBytes = body.toByteArray(Charsets.UTF_8)
  /** The size of this packet (note that the size itself is excluded from the calculation.) */
  val size: Int = run {
    val idSize = 1 * Int.SIZE_BYTES
    val typeSize = 1 * Int.SIZE_BYTES
    val bodySize = body.size * Byte.SIZE_BYTES
    val bodyTerminatorSize = 1 * Byte.SIZE_BYTES
    val packetTerminatorSize = 1 * Byte.SIZE_BYTES
    val total = idSize + typeSize + bodySize + bodyTerminatorSize + packetTerminatorSize
    println(
      "packetSize: $total = $idSize + $typeSize + $bodySize + $bodyTerminatorSize + $packetTerminatorSize"
    )

    total
  }

  val totalSize: Int = size + Int.SIZE_BYTES

  init {
    println("totalSize: $totalSize")
    println("body: ${body.joinToString { it.toString() }}")
  }

//  fun toByteArray(): ByteArray {
//    val array = buildList {
//      add(this@Packet.size.toByte())
//      add(id.toByte())
//      add(type.value.toByte())
//      addAll(body.toByteArray(Charsets.UTF_8).asIterable())
//      add(bodyTerminator)
//      add(RCON_PACKET_TERMINATOR)
//    }.toByteArray()
//
//    println(array.joinToString { it.toInt().toChar().toString().padStart(3, ' ') })
//    println(array.joinToString { it.toInt().toString().padStart(3, ' ') })
//    println(array.joinToString { it.toUInt().toString().padStart(3, ' ') })
//    println(array.joinToString { it.toString().padStart(3, ' ') })
//
////    require(array.size == totalSize) { "${array.size} == $totalSize" }
//    return array
//  }

  fun toByteBuffer2(): ByteBuffer {

    return ByteBuffer
      .allocate(totalSize)
      .order(ByteOrder.LITTLE_ENDIAN)
      // size
      .putInt(size)
      // ID
      .putInt(id.toInt())
      // type
      .putInt(type.value)
      // body
      .put(body)
      // body terminator
      .put(bodyTerminator)
      // packet terminator
      .put(RCON_PACKET_TERMINATOR)
//        .asReadOnlyBuffer()


  }

//  fun toByteBuffer(): ByteBuffer {
//
//
//    val bodyBA = buildList {
//      add(id.toByte())
//      add(type.value.toByte())
//      addAll(bodyBytes.asIterable())
//      add(bodyTerminator)
//      add(RCON_PACKET_TERMINATOR)
//    }.toByteArray()
////
////
////    val body = ByteBuffer.allocate(totalSize + 15).apply {
////      order(ByteOrder.LITTLE_ENDIAN)
//////      putInt(this@Packet.size)
////      putInt(this@Packet.id.toInt())
////      putInt(this@Packet.type.value)
////      put(this@Packet.bodyBytes)
////      put(this@Packet.terminator)
////    }
//
//    val bodySize = bodyBA.size //* Byte.SIZE_BYTES
//    println("bodySize: $bodySize")
//
//    val bb = ByteBuffer.allocate(bodySize + Int.SIZE_BYTES).apply {
//      order(ByteOrder.LITTLE_ENDIAN)
//      putInt(bodySize)
////      put(body.array())
//      put(bodyBA)
//    }
//
//    require(
//        bb.array().size == totalSize) { "${bb.array().size} == $totalSize ($size + ${Int.SIZE_BYTES})" }
//
//    return bb
//  }

  companion object {
    /** [Char.MIN_VALUE] = null char */
    const val NULL_CHAR_BYTE = Char.MIN_VALUE.code.toByte()

    /**
     * The end of a Source RCON Protocol packet is marked by an empty ASCIIZ string -
     * essentially just 8 bits of 0. In languages that do not null-terminate strings automatically,
     * you can simply write 16 bits of 0 immediately after the packet body (8 to terminate the
     * packet body and 8 for the empty string afterwards).
     */
    const val RCON_PACKET_TERMINATOR = NULL_CHAR_BYTE
  }
}
