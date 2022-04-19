import EventCacheService from "./EventDataCache";
import packetEmitter from "../PacketEmitter";
import {KafkatorioKeyedPacketData} from "../types";

/** Every 0.1 seconds */
script.on_nth_tick(6, (nthTick: NthTickEventData) => {
  const cachedEvents: Array<KafkatorioKeyedPacketData> = EventCacheService.extractExpired()

  if (cachedEvents.length > 0) {
    log(`nth tick ${nthTick.tick} has ${cachedEvents.length} events`)

    for (const event of cachedEvents) {
      packetEmitter.emitKeyedPacket(event)
    }
  }
})
