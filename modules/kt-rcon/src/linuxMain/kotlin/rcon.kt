import PacketType.*
import kotlin.math.absoluteValue
import kotlin.random.Random
import kotlinx.cinterop.*
import platform.linux.*
import platform.posix.*

private const val MIN_PACKET = 10
private const val MAX_PACKET = 4096
private val MAX_BODY: Int = MAX_PACKET - (3 * sizeOf<UIntVar>().toInt()) - 2
private const val RCON_HOST = "127.0.0.1"


fun main(args: Array<String>) {
  if (args.size < 2) {
    error("error: missing command argument")
  }

  val port = getenv("RCON_PORT")?.toKString()
    ?: error("error: missing \$RCON_PORT env")

  val confDir = getenv("CONFIG")?.toKString()
    ?: error("error: missing \$CONFIG env")

  val rconSocket = rconOpen(port)
    ?: error("error: could not connect")

  val password = readPassword(confDir)
  if (password == null || !rconAuth(rconSocket, password)) {
    error("error: login failed")
  }

  val pkt = rconCreate(EXEC_COMMAND, combineArgs(args))
  if (!rconSocket.send(pkt)) {
    error("error: send command failed")
  }

  if (rconSocket.receive(pkt, RESPONSE) && pkt.length > 0u) {
    println(pkt.body.toKString())
  }
}


private enum class PacketType(val value: UInt) {
  RESPONSE(0u),
  EXEC_COMMAND(2u),
  AUTH_RESPONSE(2u),
  AUTH(3u),
}

private class Packet(
  val length: UInt = 0u,
  val id: UInt = 0u,
  val type: PacketType = RESPONSE,
  val body: ByteArray = ByteArray(MAX_BODY),
)

private class RconSocket(
  val id: Int
)


private fun rconOpen(port: String): RconSocket? {
  memScoped {
    val address = alloc<sockaddr_in> {
      sin_family = AF_INET.convert()
      sin_port = htons(port.toShort().convert())
    }
    inet_aton(RCON_HOST, address.sin_addr.ptr)

    val rconSocket = socket(AF_INET, SOCK_STREAM, 0)
    if (connect(rconSocket, address.ptr.reinterpret(), sizeOf<sockaddr_in>().convert()) < 0) {
      return null
    }
    return RconSocket(rconSocket)
  }
}

private fun rconCreate(type: PacketType, body: String): Packet {
  val bodyLength = body.length
  if (bodyLength >= MAX_BODY - 2) {
    error("error: command too long\n")
  }

  return Packet(
    id = Random.nextInt().absoluteValue.toUInt(),
    type = type,
    length = (sizeOf<UIntVar>() + sizeOf<UIntVar>() + bodyLength + 2).toUInt(),
    body = body.encodeToByteArray(),
  )
}

private fun RconSocket.send(pkt: Packet): Boolean {
  val length = pkt.length.toInt() + sizeOf<UIntVar>()
  val ptr = nativeHeap.allocArray<ByteVar>(length)
  memcpy(ptr, pkt.ptr, sizeOf<UIntVar>().toULong())
  var remaining = length

  while (remaining > 0) {
    val ret = send(id, ptr, remaining.convert(), 0)
    if (ret == -1L) return false
    remaining -= ret
  }
  return true
}

private fun RconSocket.receive(pkt: Packet, expectedType: PacketType): Boolean {

  val ptr = nativeHeap.allocArray<ByteVar>(sizeOf<Packet>().convert())

  var received = 0L

  while (received < pkt.length.toInt()) {
    val rxBytes = recv(id, ptr + received, (pkt.length.toInt() - received).convert(), 0)
    if (rxBytes < 0) {
      perror("error: socket error\n")
      return false
    } else if (rxBytes == 0L) {
      fprintf(stderr, "error: connection lost\n")
      return false
    }
    received += rxBytes
  }

  return pkt.type == expectedType
}

private fun rconAuth(rconSocket: RconSocket, password: String): Boolean {
  val pkt = rconCreate(AUTH, password)
  if (!rconSocket.send(pkt)) return false
  if (!rconSocket.receive(pkt, AUTH_RESPONSE)) return false
  return true
}

private fun combineArgs(args: Array<String>): String {
  return args.slice(1 until args.size).joinToString(" ")
}

private fun readPassword(confDir: String): String? {
  val path = "$confDir/rconpw"
  val file = fopen(path, "r") ?: return null
  fseek(file, 0, SEEK_END)
  val fsize = ftell(file).toInt()
  fseek(file, 0, SEEK_SET)
  val buffer = nativeHeap.allocArray<ByteVar>(fsize + 1)
  fread(buffer, fsize.toULong(), 1.convert(), file)
  fclose(file)
  return buffer.toKString()
}
