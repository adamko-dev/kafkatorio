export const MOD_VERSION = script.active_mods["@mod.name@"] ?? "UNKNOWN"
export const FACTORIO_VERSION = script.active_mods["base"] ?? "UNKNOWN"

// const SERVER_ID: uint = 0
// const EVENT_FILE_DIR: string = "events"

/** Emit a serialised event */
export function emitEvent<T extends FactorioObjectData>(eventData: T, tick: uint, eventType: string) {

  let event: FactorioEvent<T> = {
    data: eventData,
    eventType: eventType,
    modVersion: MOD_VERSION,
    tick: tick
  }

  let data = game.table_to_json(event)

  localised_print(`FactorioEvent: ${data}`)
}
