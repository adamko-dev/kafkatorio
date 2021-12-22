import {Serdes} from "./serdes/serdes"

const MOD_VERSION = script.active_mods["@mod.name@"] ?? "UNKNOWN"
const FACTORIO_VERSION = script.active_mods["base"] ?? "UNKNOWN"

const mapEventIdToName = new LuaTable<defines.Events, keyof typeof defines.events>()
for (const [k, v] of pairs(defines.events)) {
  mapEventIdToName.set(v, k)
}

function handlePlayerUpdate(tick: uint, playerIndex: uint, eventType: string) {
  let player: LuaPlayer = game.players[playerIndex]
  let table = Serdes.Player.playerToTable(player)
  emitEvent(table, tick, eventType)

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
  emitEvent(table, tick, eventType)
}

function surfaceEvent(tick: uint, surface: LuaSurface, eventType: string) {
  let table = Serdes.Surface.surfaceToTable(surface)
  emitEvent(table, tick, eventType)
}

// const SERVER_ID: uint = 0
// const EVENT_FILE_DIR: string = "events"
/** Emit a serialised event */
function emitEvent<T extends FactorioObjectData>(eventData: T, tick: uint, eventType: string) {

  let event: FactorioEvent<T> = {
    data: eventData,
    event_type: eventType,
    mod_version: MOD_VERSION,
    factorio_version: FACTORIO_VERSION,
    tick: tick
  }

  let data = game.table_to_json(event)

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

        for (const [index, _] of pairs(game.surfaces)) {
          let surface = game.get_surface(index) // TODO fix / report can't iterate over surfaces
          if (surface != undefined) {
            surfaceEvent(e.tick, surface, mapEventIdToName.get(e.name))
          }
          // let surface = game.surfaces[index]
          // surfaceEvent(e.tick, surface, mapEventIdToName.get(e.name))
        }
      }
    }
);


// script.on_load(() => {
//   game.print("loaded Factorio Web Map!")
// });
