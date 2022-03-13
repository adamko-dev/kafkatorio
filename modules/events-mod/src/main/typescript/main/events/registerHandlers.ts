import {handleConsoleChat, handleSurfaceUpdate,} from "./handlers";
import {Queue} from "../queue/queue";
import {isEventType} from "./eventTypeCheck";
import {Converters} from "./converters";
import KafkatorioSettings from "../settings/KafkatorioSettings";
import {handleChunkGeneratedEvent} from "./onMapChunkEventListeners";


// script.on_event(
//     defines.events.on_player_mined_entity,
//     (e: OnPlayerMinedEntityEvent) => {
//       let eventName = Converters.eventNameString(e.name)
//       // handlePlayerUpdate(e.tick, eventName, e.player_index)
//       handleEntityUpdate(e.tick, eventName, e.entity)
//     }
// )

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
          handleSurfaceUpdate(e.tick, Converters.eventNameString(e.name), surface)
        }

        // let packets = new KafkatorioPacketQueue().dequeueValues(1)
        // for (const packet of packets) {
        //   emitPacket(packet)
        // }
      }

      if (e.tick % 7 == 0) {
        const events: EventData[] = Queue.dequeueValues(1)

        if (events.length > 0) {
          log(`[${e.tick}] dequed ${events.length} events, current size: ${Queue.size()}`)

          let i = 100
          for (const event of events) {
            if (isEventType(event, defines.events.on_chunk_generated)) {
              let eName = Converters.eventNameString(event.name)

              log(`[${e.tick}] dequed event ${eName}`)
              handleChunkGeneratedEvent(event, i)
              i += 10
            }
          }
        }
      }
    }
)

script.on_event(
    defines.events.on_console_chat,
    (e: OnConsoleChatEvent) => {
      handleConsoleChat(e.tick, Converters.eventNameString(e.name), e.player_index, e.message)
    }
)

// script.on_event(
//     defines.events.on_chunk_generated,
//     (e: OnChunkGeneratedEvent) => {
//       handleChunkUpdate(e.tick, mapEventIdToName.get(e.name), e.surface.index, e.position,
// e.area) } )  script.on_event( [ defines.events.on_player_built_tile,
// defines.events.on_robot_built_tile, ], (builtTilesEvent) => { handleTilesUpdate(
// builtTilesEvent.tick, mapEventIdToName.get(builtTilesEvent.name), builtTilesEvent.surface_index,
// builtTilesEvent.tile, builtTilesEvent.tiles, ) } )

script.on_event(
    defines.events.on_runtime_mod_setting_changed,
    (e: OnRuntimeModSettingChangedEvent) => {
      print(`Settings changed ${e.setting_type}, ${e.player_index}, ${e.setting}, ${e.mod_name}`)
      KafkatorioSettings.loadSettings()
    }
)
