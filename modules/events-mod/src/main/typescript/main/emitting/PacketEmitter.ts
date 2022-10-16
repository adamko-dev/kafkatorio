import {KafkatorioPacket, KafkatorioPacketData} from "../../generated/kafkatorio-schema";
import {KafkatorioKeyedPacketData} from "../types";


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
    const data = game.table_to_json(packet)
    // print(`KafkatorioPacket::: ${data}`)
    const encodedData = game.encode_string(data)
    if (encodedData != null) {
      print(`KafkatorioPacket encoded::: ${encodedData}`)
      // rcon.print(`KafkatorioPacket: ${data}`)
      // this.appendToFile(encodedData)
    }
  }

  // private static appendToFile(encodedData: string) {
  //   // change log files every hour:
  //   const perHour = game.tick / (60 * 60 * 60)
  //   game.write_file(
  //       `kafkatorio/packets_${perHour}.txt`,
  //       `${encodedData}\n`,
  //       true,
  //       0, // server only
  //   )
  // }

}


const PacketEmitter = new KafkatorioPacketEmitter()

export default PacketEmitter
