import {
  handleChunkUpdate,
  handleConsoleChat,
  handleEntityUpdate,
  // handlePlayerUpdate,
  handleSurfaceUpdate,
  handleTilesUpdate
} from "./handlers";
import {Queue} from "../queue/queue";
import {EventName} from "../types";


const mapEventIdToName = new LuaTable<defines.Events, EventName>()
for (const [eventName, eventId] of pairs(defines.events)) {
  mapEventIdToName.set(eventId, eventName)
}

// const mapEventIdToDefinedType = new LuaTable<EventId<EventData>, EventId<EventData>>()
// for (const [eventName, eventId] of pairs(defines.events)) {
//   let definedType = defines.events[eventName]
//   mapEventIdToDefinedType.set(eventId, definedType)
// }

// script.on_event(
//     [defines.events.on_pre_build, defines.events.on_player_dropped_item],
//     (e: OnPreBuildEvent | OnPlayerDroppedItemEvent) => {
//       game.print("player " + e.player_index + " dropped item, tick:" + e.tick)
//     }
// );

script.on_event(
    defines.events.on_player_mined_entity,
    (e: OnPlayerMinedEntityEvent) => {
      let eventName = mapEventIdToName.get(e.name)
      // handlePlayerUpdate(e.tick, eventName, e.player_index)
      handleEntityUpdate(e.tick, eventName, e.entity)
    }
)

// script.on_event(
//     defines.events.on_player_joined_game,
//     (e: OnPlayerJoinedGameEvent) => {
//       handlePlayerUpdate(e.tick, mapEventIdToName.get(e.name), e.player_index)
//     }
// )

// script.on_event(
//     defines.events.on_player_changed_position,
//     (e: OnPlayerChangedPositionEvent) => {
//       handlePlayerUpdate(e.tick, mapEventIdToName.get(e.name), e.player_index)
//     }
// )

script.on_event(
    defines.events.on_tick,
    (e: OnTickEvent) => {
      if (e.tick % 1000 == 0) {
        for (const [, surface] of game.surfaces) {
          handleSurfaceUpdate(e.tick, mapEventIdToName.get(e.name), surface)
        }

        // let packets = new KafkatorioPacketQueue().dequeueValues(1)
        // for (const packet of packets) {
        //   emitPacket(packet)
        // }
      }

      if (e.tick % 30 == 0) {
        let events: EventData[] = Queue.dequeueValues(1)

        if (events.length > 0) {
          log(`[${e.tick}] dequed ${events.length} events, current size: ${Queue.size()}`)

          for (const event of events) {
            if (isEventType(event, defines.events.on_chunk_generated)) {
              let eName = mapEventIdToName.get(event.name)

              log(`[${e.tick}] dequed event ${eName}`)
              handleChunkUpdate(e.tick, eName, event.surface.index, event.position, event.area)
            }
          }
        }
      }
    }
)

script.on_event(
    defines.events.on_console_chat,
    (e: OnConsoleChatEvent) => {
      handleConsoleChat(e.tick, mapEventIdToName.get(e.name), e.player_index, e.message)
    }
)

script.on_event(
    defines.events.on_chunk_generated,
    (e: OnChunkGeneratedEvent) => {
      handleChunkUpdate(e.tick, mapEventIdToName.get(e.name), e.surface.index, e.position, e.area)
    }
)

script.on_event(
    [
      defines.events.on_player_built_tile,
      defines.events.on_robot_built_tile,
    ],
    (builtTilesEvent) => {
      handleTilesUpdate(
          builtTilesEvent.tick,
          mapEventIdToName.get(builtTilesEvent.name),
          builtTilesEvent.surface_index,
          builtTilesEvent.tile,
          builtTilesEvent.tiles,
      )
    }
)
