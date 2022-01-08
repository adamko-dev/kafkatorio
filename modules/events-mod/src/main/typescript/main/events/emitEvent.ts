// const SERVER_ID: uint = 0
// const EVENT_FILE_DIR: string = "events"

import {emitPacket} from "../emitKafkatorioPacket";

/** Emit a serialised event */
export function emitEvent<T extends FactorioObjectData>(eventData: T, tick: uint, eventType: string) {
  emitPacket({
    data: eventData,
    packetType: "EVENT",
    eventType: eventType,
    modVersion: global.MOD_VERSION,
    tick: tick
  })
}
