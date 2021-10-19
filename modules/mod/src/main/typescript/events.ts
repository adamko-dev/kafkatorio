// import {Kafka, Producer} from "kafkajs";

// const {Kafka} = require("kafkajs")
// import {Kafka, Producer} from "kafkajs/types"
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

function playerEvent(tick: uint, player_index: uint, eventType: string) {
  let player: LuaPlayer = game.players[player_index]

  let filename: string = table.concat([player.index, eventType, tick], "_")
  let data = Serdes.Player.playerToTable(player)
  outputEventFile(player.object_name + "/" + filename, data)

  if (player.character != undefined) {
    entityEvent(tick, player.character, eventType)

    for (const char of player.get_associated_characters()) {
      if (char != undefined) {
        entityEvent(tick, char, eventType)
      }
    }

  }
}

function entityEvent(tick: uint, entity: LuaEntity, eventType: string) {
  if (entity.unit_number != undefined && entity.unit_number != 0) {
    let filename: string = table.concat([entity.unit_number, eventType, tick], "_")
    let data = Serdes.Entity.entityToTable(entity)
    outputEventFile(entity.object_name + "/" + filename, data)
  }
}

function surfaceEvent(tick: uint, surface: LuaSurface, eventType: string) {
  let filename: string =
      surface.object_name + "/" + table.concat([surface.index, eventType, tick], "_")
  let data = Serdes.Surface.surfaceToTable(surface)
  outputEventFile(filename, data)
}

function outputEventFile(filename: string, obj: JsonTable) {
  let data = game.table_to_json(obj)
  // game.write_file(EVENT_FILE_DIR + "/" + filename, data, false, SERVER_ID)
  localised_print(`!===== ${filename} | ${data} =====!`)
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
      entityEvent(e.tick, e.entity, "on_player_mined_entity")
      playerEvent(e.tick, e.player_index, "on_player_mined_entity")
    }
)
script.on_event(
    defines.events.on_player_joined_game,
    (e: OnPlayerJoinedGameEvent) => {
      playerEvent(e.tick, e.player_index, "on_player_joined_game")
    }
)

script.on_event(
    defines.events.on_player_changed_position,
    (e: OnPlayerChangedPositionEvent) => {
      playerEvent(e.tick, e.player_index, "on_player_changed_position")
    }
)

script.on_event(
    defines.events.on_tick,
    (e: OnTickEvent) => {
      if (e.tick % 60 == 0) {

        game.surfaces

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
