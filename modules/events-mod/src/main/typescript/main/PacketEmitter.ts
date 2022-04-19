import {
  KafkatorioPacket2,
  KafkatorioPacketData2
} from "../generated/kafkatorio-schema/kafkatorio-schema";
import {KafkatorioKeyedPacketData} from "./types";

export class KafkatorioPacketEmitter {

  emitInstantPacket<DATA extends KafkatorioPacketData2>(
      data: DATA,
  ) {
    KafkatorioPacketEmitter.emitPacket<KafkatorioPacket2>({
      data: data,
      modVersion: global.MOD_VERSION,
      tick: game.tick,
    })
  }

  emitKeyedPacket<DATA extends KafkatorioKeyedPacketData>(
      data: DATA,
  ) {
    KafkatorioPacketEmitter.emitPacket<KafkatorioPacket2>({
      data: data,
      modVersion: global.MOD_VERSION,
      tick: game.tick,
    })
  }

  /** Emit a serialised KafkatorioPacket */
  private static emitPacket<T extends KafkatorioPacket2>(packet: T) {
    let data = game.table_to_json(packet)
    localised_print(`KafkatorioPacket: ${data}`)
    // rcon.print(`KafkatorioPacket: ${data}`)
  }

}


const PacketEmitter = new KafkatorioPacketEmitter()

export default PacketEmitter
