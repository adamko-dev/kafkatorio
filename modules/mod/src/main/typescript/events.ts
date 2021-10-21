// import {Data, Mods} from "typed-factorio/settings/types"
// import { events } from "typed-factorio/generated"

// declare const factorioData: Data
// declare const factorioMods: Mods

// const eventEnum = {
//   thingType: Object.values(defines.events)
// }

import {JsonTable, Serdes} from "./serdes/serdes"

// const SERVER_ID: uint = 0
// const EVENT_FILE_DIR: string = "events"

function createFactorioEvent(tick: uint, object_name: string, eventType: string, data: JsonTable): JsonTable {
  return {
    tick,
    object_name,
    eventType,
    data,
  }
}

function handlePlayerUpdate(tick: uint, player_index: uint, eventType: string) {
  let player: LuaPlayer = game.players[player_index]
  let table = Serdes.Player.playerToTable(player)
  let event = createFactorioEvent(tick, player.object_name, eventType, table)
  emitEvent(event)

  handleCharactersEvent(tick, player_index, eventType)
}

function handleCharactersEvent(tick: uint, player_index: uint, eventType: string) {

  let player: LuaPlayer = game.players[player_index]

  if (player.character != undefined) {
    handleEntityUpdate(tick, player.character, eventType)

    for (const char of player.get_associated_characters()) {
      if (char != undefined) {
        handleEntityUpdate(tick, char, eventType)
      }
    }
  }
}

function handleEntityUpdate(tick: uint, entity: LuaEntity, eventType: string) {
  let table = Serdes.Entity.entityToTable(entity)
  let event = createFactorioEvent(tick, entity.object_name, eventType, table)
  emitEvent(event)
}

function surfaceEvent(tick: uint, surface: LuaSurface, eventType: string) {
  let table = Serdes.Surface.surfaceToTable(surface)
  let event = createFactorioEvent(tick, surface.object_name, eventType, table)
  emitEvent(event)
}

/** Emit a serialised event */
function emitEvent(event: JsonTable) {
  let data = game.table_to_json(event)
  // game.write_file(EVENT_FILE_DIR + "/" + filename, data, false, SERVER_ID)
  localised_print(`FactorioEvent: ${data}`)
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
      handlePlayerUpdate(e.tick, e.player_index, "on_player_mined_entity")
      handleEntityUpdate(e.tick, e.entity, "on_player_mined_entity")
    }
)
script.on_event(
    defines.events.on_player_joined_game,
    (e: OnPlayerJoinedGameEvent) => {
      handlePlayerUpdate(e.tick, e.player_index, "on_player_joined_game")
    }
)

script.on_event(
    defines.events.on_player_changed_position,
    // this is actually a character update, not a player update?
    (e: OnPlayerChangedPositionEvent) => {
      handlePlayerUpdate(e.tick, e.player_index, "on_player_changed_position")
    }
)

script.on_event(
    defines.events.on_tick,
    (e: OnTickEvent) => {
      if (e.tick % 60 == 0) {

        for (const [index,] of pairs(game.surfaces)) {
          let surface = game.surfaces[index]
          surfaceEvent(e.tick, surface, "on_tick")
        }

        // let filename: string = table.concat([entity.unit_number, eventType, tick], "_")
        // let msg = "test mod tick: " + e.tick + "\n"
        // game.write_file("tick.log", msg, true, SERVER_ID)
        // let filename: string = `OnTickEvent/${e.tick}`
        // outputEvent(filename, e)
      }
    }
);


// script.on_load(() => {
//   game.print("loaded Factorio Web Map!")
// });
