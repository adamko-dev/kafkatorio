import EventCacheService from "./EventDataCache";


/** Every 0.1 seconds */
script.on_nth_tick(6, (nthTick: NthTickEventData) => {
  const emittedCount = EventCacheService.extractAndEmitExpiredPackets()
  if (emittedCount > 0) {
    log(`[on_nth_tick:${nthTick.tick}] emitted ${emittedCount} events`)
  }
})
