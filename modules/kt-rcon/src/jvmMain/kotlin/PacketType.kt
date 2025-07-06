package dev.adamko.ktrcon
/**
 * The packet type field is a 32-bit little endian integer, which indicates the purpose of the
 * packet. Its value will always be either 0, 2, or 3, depending on which of the request/response
 * types the packet represents.
 */
enum class PacketType(
    val value: Int,
    val descriptor: String,
) {

  /**
   * Typically, the first packet sent by the client will be a [AUTH_RESPONSE] packet, which is
   * used to authenticate the connection with the server.
   *
   * The value of the packet's fields are as follows:
   *
   * * **ID** - any positive integer, chosen by the client
   *     (will be mirrored back in the server's response)
   * * **Type** - 3
   * * **Body** - the RCON password of the server (if this matches the server's *rcon_password*
   *     cvar, the auth will succeed)
   *
   *  If the rcon_password cvar is not set, or if it is set to empty string, all
   *  [AUTH_RESPONSE] requests will be refused.
   */
  AUTH_REQUEST(3, "SERVERDATA_AUTH"),
  AUTH_RESPONSE(2, "SERVERDATA_AUTH_RESPONSE"),
  EXEC_COMMAND(2, "SERVERDATA_EXECCOMMAND"),
  RESPONSE_VALUE(0, "SERVERDATA_RESPONSE_VALUE"),
}
