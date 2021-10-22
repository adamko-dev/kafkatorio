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

export interface FactorioEvent extends JsonTable {
  tick: uint
  object_name: string,
  event_type: string,
  data: JsonTable,
}
