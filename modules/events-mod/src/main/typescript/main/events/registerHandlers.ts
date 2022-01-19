import {
  handleChunkUpdate,
  handleConsoleChat,
  handleEntityUpdate,
  handlePlayerUpdate,
  handleSurfaceUpdate,
  handleTilesUpdate
} from "./handlers";
import on_player_built_tile = defines.events.on_player_built_tile;
import on_robot_built_tile = defines.events.on_robot_built_tile;


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
      handlePlayerUpdate(e.tick, eventName, e.player_index)
      handleEntityUpdate(e.tick, eventName, e.entity)
    }
)

script.on_event(
    defines.events.on_player_joined_game,
    (e: OnPlayerJoinedGameEvent) => {
      handlePlayerUpdate(e.tick, mapEventIdToName.get(e.name), e.player_index)
    }
)

script.on_event(
    defines.events.on_player_changed_position,
    (e: OnPlayerChangedPositionEvent) => {
      handlePlayerUpdate(e.tick, mapEventIdToName.get(e.name), e.player_index)
    }
)

script.on_event(
    defines.events.on_tick,
    (e: OnTickEvent) => {
      if (e.tick % 60 == 0) {
        for (const [, surface] of pairs(game.surfaces)) {
          handleSurfaceUpdate(e.tick, mapEventIdToName.get(e.name), surface)
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
    [on_player_built_tile, on_robot_built_tile],
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
