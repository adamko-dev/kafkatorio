import {
  handleChunkUpdate,
  handleConsoleChat,
  handleEntityUpdate,
  handlePlayerUpdate,
  handleSurfaceUpdate
} from "./handlers";

const mapEventIdToName = new LuaTable<defines.Events, keyof typeof defines.events>()
for (const [k, v] of pairs(defines.events)) {
  mapEventIdToName.set(v, k)
}

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
      handlePlayerUpdate(e.tick, e.player_index, eventName)
      handleEntityUpdate(e.tick, e.entity, eventName)
    }
)

script.on_event(
    defines.events.on_player_joined_game,
    (e: OnPlayerJoinedGameEvent) => {
      let eventName = mapEventIdToName.get(e.name)
      handlePlayerUpdate(e.tick, e.player_index, eventName)
    }
)

script.on_event(
    defines.events.on_player_changed_position,
    (e: OnPlayerChangedPositionEvent) => {
      let eventName = mapEventIdToName.get(e.name)
      handlePlayerUpdate(e.tick, e.player_index, eventName)
    }
)

script.on_event(
    defines.events.on_tick,
    (e: OnTickEvent) => {
      if (e.tick % 60 == 0) {
        for (const [_, surface] of pairs(game.surfaces)) {
          handleSurfaceUpdate(e.tick, surface, mapEventIdToName.get(e.name))
        }

        // for (const [index, _] of pairs(game.surfaces)) {
        //   let surface = game.get_surface(index) // TODO fix / report can't iterate over surfaces
        //   if (surface != undefined) {
        //     handleSurfaceUpdate(e.tick, surface, mapEventIdToName.get(e.name))
        //   }
        ////   let surface = game.surfaces[index]
        ////   surfaceEvent(e.tick, surface, mapEventIdToName.get(e.name))
        // }
      }
    }
)

script.on_event(
    defines.events.on_console_chat,
    (e: OnConsoleChatEvent) => {
      handleConsoleChat(e.tick, e.player_index, e.message, mapEventIdToName.get(e.name))
    }
)

script.on_event(
    defines.events.on_chunk_charted,
    (e: OnChunkChartedEvent) => {
      handleChunkUpdate(e.tick, mapEventIdToName.get(e.name), e.surface_index, e.position)
    }
)
