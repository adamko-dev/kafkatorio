import {
  KafkatorioPacket,
  KafkatorioPacketData
} from "../generated/kafkatorio-schema";
import {KafkatorioKeyedPacketData} from "./types";


export class KafkatorioPacketEmitter {

  emitInstantPacket<DATA extends KafkatorioPacketData>(
      data: DATA,
  ) {
    KafkatorioPacketEmitter.emitPacket<KafkatorioPacket>({
      data: data,
      modVersion: global.MOD_VERSION,
      tick: game.tick,
    })
  }

  emitKeyedPacket<DATA extends KafkatorioKeyedPacketData>(
      data: DATA,
  ) {
    KafkatorioPacketEmitter.emitPacket<KafkatorioPacket>({
      data: data,
      modVersion: global.MOD_VERSION,
      tick: game.tick,
    })
  }

  /** Emit a serialised KafkatorioPacket */
  private static emitPacket<T extends KafkatorioPacket>(packet: T) {
    let data = game.table_to_json(packet)
    localised_print(`KafkatorioPacket: ${data}`)
    // rcon.print(`KafkatorioPacket: ${data}`)
  }

}


const PacketEmitter = new KafkatorioPacketEmitter()

export default PacketEmitter
