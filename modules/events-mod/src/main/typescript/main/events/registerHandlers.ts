import {Queue} from "../queue/queue";
import {isEventType} from "./eventTypeCheck";
import {Converters} from "./converters";
import KafkatorioSettings from "../settings/KafkatorioSettings";
import {handleChunkGeneratedEvent} from "./onMapChunkEventListeners";
import {handleSurfaceUpdate} from "./handlers";


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
    (event: OnTickEvent) => {
      if (event.tick % 1000 == 0) {
        for (const [, surface] of game.surfaces) {
          handleSurfaceUpdate(event, Converters.eventNameString(event.name), surface)
        }
      }

      if (event.tick % 7 == 0) {
        const events: EventData[] = Queue.dequeueValues(1)

        if (events.length > 0) {
          log(`[${event.tick}] dequed ${events.length} events, current size: ${Queue.size()}`)

          let i = 100
          for (const event of events) {
            if (isEventType(event, defines.events.on_chunk_generated)) {
              let eName = Converters.eventNameString(event.name)

              log(`[${event.tick}] dequed event ${eName}`)
              handleChunkGeneratedEvent(event, i)
              i += 10
            }
          }
        }
      }
    }
)


script.on_event(
    defines.events.on_runtime_mod_setting_changed,
    (e: OnRuntimeModSettingChangedEvent) => {
      print(`Settings changed ${e.setting_type}, ${e.player_index}, ${e.setting}, ${e.mod_name}`)
      KafkatorioSettings.loadSettings()
    }
)
