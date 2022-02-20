import {EventDataCache} from "./EventDataCache";
import {emitPacket} from "../emitKafkatorioPacket";

/** Every 0.1 seconds */
script.on_nth_tick(6, (nthTick: NthTickEventData) => {

  const cachedEvents: Array<EventDataCache.CacheData<any>> = EventDataCache.extractExpired()

  for (const event of cachedEvents) {

    const packet: FactorioEventUpdatePacket = {
      tick: nthTick.tick,
      modVersion: global.MOD_VERSION,
      packetType: "UPDATE",
      update: event,
    }

    emitPacket(packet)
  }
})

//
// function createEntityUpdate(cacheKey: EntityCacheKey, events: EventName[]): EntityUpdate | null {
//
//
//   return null
// }
//
// interface EntityUpdate {
//   name: string
//
//   unit_number: uint
//
//   unhandledEvents?: string[]
//
//   chunkPosition: MapChunkPosition
//   graphics_variation?: uint8
//   health?: float
//   isActive?: boolean
//   isRotatable?: boolean
//   last_user?: uint
//   localised_description?: string
//   localised_name?: string
//   prototype?: string
//
//   // ghost_localised_description?: string
//   // ghost_localised_name?: string
//   // ghost_name?: string
//   // ghost_prototype?: string
//   // ghost_unit_number?: uint
// }
