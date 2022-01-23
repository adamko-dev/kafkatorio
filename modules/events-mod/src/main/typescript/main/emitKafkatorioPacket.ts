/** Emit a serialised KafkatorioPacket */
export function emitPacket<T extends KafkatorioPacket>(packet: T) {
  let data = game.table_to_json(packet)
  // localised_print(`KafkatorioPacket: ${packet.packetType}`)
  localised_print(`KafkatorioPacket: ${data}`)
  // rcon.print(`KafkatorioPacket: ${data}`)
}
