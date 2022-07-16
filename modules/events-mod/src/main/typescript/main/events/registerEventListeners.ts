import {isEventType} from "./eventTypeCheck";
import {Converters} from "./converters";
import {handleSurfaceUpdate} from "./surfaceUpdates";
import EventCacheService from "../emitting/EventDataCache";
import MapChunkUpdate from "./handlers/MapChunkUpdateHandler";
import KafkatorioPacketQueue from "../emitting/KafkatorioPacketQueue";
import PacketEmitter from "../emitting/PacketEmitter";
import {KafkatorioPacketData} from "../../generated/kafkatorio-schema";
import EventDataQueue from "../emitting/EventDataQueue";


script.on_event(defines.events.on_tick, (event: OnTickEvent) => {
  if (event.tick % 1000 == 0) {
    for (const [, surface] of game.surfaces) {
      handleSurfaceUpdate(event, Converters.eventNameString(event.name), surface)
    }
  }

  const kPacket: KafkatorioPacketData | null = KafkatorioPacketQueue.dequeue()
  if (kPacket != null) {
    PacketEmitter.emitInstantPacket(kPacket)
  }

  if (event.tick % 6 == 0) {
    // Every 0.1 seconds
    const emittedCount = EventCacheService.extractAndEmitExpiredPackets()
    if (emittedCount > 0) {
      log(`[on_tick:${event.tick}] emitted ${emittedCount} events`)
    }
  }

  if (event.tick % 7 == 0) {
    const events: EventData[] = EventDataQueue.dequeueValues(1)

    if (events.length > 0) {
      log(`[on_tick:${event.tick}] dequeued ${events.length} events, current size: ${EventDataQueue.size()}`)

      let i = 1
      for (const event of events) {
        if (isEventType(event, defines.events.on_chunk_generated)) {
          let eName = Converters.eventNameString(event.name)

          log(`[on_tick:${event.tick}] dequed event ${eName}, delay ${i}`)
          MapChunkUpdate.handleChunkGeneratedEvent(event, i)
          i += 1
        }
      }
    }
  }
})
