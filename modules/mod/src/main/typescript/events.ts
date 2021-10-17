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

const SERVER_ID : uint = 0

function outputEvent(filename: string, event: table) {
  let json : string = game.table_to_json(event)
  // game.print(`Creating file ${filename}, content: ${json}`)
  game.write_file(filename, json, false, SERVER_ID)
}

script.on_event(
    [defines.events.on_pre_build, defines.events.on_player_dropped_item],
    (e: OnPreBuildEvent | OnPlayerDroppedItemEvent) => {
      game.print("player " + e.player_index + " dropped item, tick:" + e.tick)
    }
);

script.on_event(
    defines.events.on_player_joined_game,
    (e: OnPlayerJoinedGameEvent) => {
      let filename : string = `${e.name}/${e.tick}_${e.player_index}`
      outputEvent(filename, e)
    }
)

script.on_event(
    defines.events.on_player_changed_position,
    (e: OnPlayerJoinedGameEvent) => {
      let filename : string = `${e.name}/${e.tick}_${e.player_index}`
      outputEvent(filename, e)
    }
)

script.on_event(
    defines.events.on_tick,
    (e: OnTickEvent) => {
      if (e.tick % 60 == 0) {
        let msg = "test mod tick: " + e.tick + "\n"
        game.write_file("tick.log", msg, true, SERVER_ID)
        let filename : string = `${e.name}/${e.tick}`
        outputEvent(filename, e)
      }
    }
);


// script.on_load(() => {
//   game.print("loaded Factorio Web Map!")
// });
