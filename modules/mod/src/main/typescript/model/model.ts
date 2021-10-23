export type JsonPrimitiveType = number | string | boolean | undefined | null | void

export type JsonArray<T extends JsonPrimitiveType = void> = T[]

export type JsonTableValueType =
    JsonPrimitiveType
    | JsonTable
    | JsonArray<JsonPrimitiveType>
    | undefined
    | null

export interface JsonTable {
  [key: string]: JsonTableValueType
}

const MOD_VERSION = script.active_mods["factorio_web_map"]

export interface FactorioEvent extends JsonTable {
  /** Schema versioning */
  mod_version: string,
  /** game time */
  tick: uint,
  /** the initial Factorio event ({defines.events}) trigger */
  event_type: string,
  /** Defines the structure of {FactorioEvent.data} */
  object_name: string,
  data: JsonTable,
}

export const FactorioEvent = ((tick: uint, objectName: string, eventType: string, data: JsonTable) => {
      return {
        mod_version: MOD_VERSION,
        tick: tick,
        object_name: objectName,
        event_type: eventType,
        data: data,
      } as FactorioEvent
    }
)
