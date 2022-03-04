import {EventDataCache} from "./EventDataCache";
import {emitPacket} from "../emitKafkatorioPacket";

/** Every 0.1 seconds */
script.on_nth_tick(6, (nthTick: NthTickEventData) => {
  const cachedEvents: Array<FactorioEventUpdate> = EventDataCache.extractExpired()

  if (cachedEvents.length > 0) {
    log(`nth tick ${nthTick.tick} has ${cachedEvents.length} events`)

    for (const event of cachedEvents) {

      const packet: FactorioEventUpdatePacket = {
        tick: nthTick.tick,
        modVersion: global.MOD_VERSION,
        packetType: "UPDATE",
        update: event,
      }

      emitPacket(packet)
    }
  }
})
