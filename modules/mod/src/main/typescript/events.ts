// import {Data, Mods} from "typed-factorio/settings/types"
// import { events } from "typed-factorio/generated"

// declare const factorioData: Data
// declare const factorioMods: Mods

// const eventEnum = {
//   thingType: Object.values(defines.events)
// }

import {Serdes} from "./serdes/serdes"
import {FactorioEvent, JsonTable} from "./model/model";

const mapEventIdToName = new LuaTable<defines.events, keyof typeof defines.events>()
for (const [k, v] of pairs(defines.events)) {
  mapEventIdToName.set(v, k)
}

// function createFactorioEvent(tick: uint, object_name: string, eventType: string, data:
// JsonTable): FactorioEvent { return { tick, object_name, eventType, data, } }

function handlePlayerUpdate(tick: uint, playerIndex: uint, eventType: string) {
  let player: LuaPlayer = game.players[playerIndex]
  let table = Serdes.Player.playerToTable(player)
  let event = FactorioEvent(tick, player.object_name, eventType, table)
  emitEvent(event)

  handleCharactersEvent(tick, playerIndex, eventType)
}

function handleCharactersEvent(tick: uint, playerIndex: uint, eventType: string) {

  let player: LuaPlayer = game.players[playerIndex]

  if (player.character != undefined) {
    handleEntityUpdate(tick, player.character, eventType)
  }
  for (const character of player.get_associated_characters()) {
    if (character != undefined) {
      handleEntityUpdate(tick, character, eventType)
    }
  }
}

function handleEntityUpdate(tick: uint, entity: LuaEntity, eventType: string) {
  let table = Serdes.Entity.entityToTable(entity)
  let event = FactorioEvent(tick, entity.object_name, eventType, table)
  emitEvent(event)
}

function surfaceEvent(tick: uint, surface: LuaSurface, eventType: string) {
  let table = Serdes.Surface.surfaceToTable(surface)
  let event = FactorioEvent(tick, surface.object_name, eventType, table)
  emitEvent(event)
}

// const SERVER_ID: uint = 0
// const EVENT_FILE_DIR: string = "events"
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
          surfaceEvent(e.tick, surface, mapEventIdToName.get(e.name))
          // surfaceEvent(e.tick, surface, "on_tick")
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
