// import {Serdes} from "../serdes/serdes";
// import FactorioObjectData = Serdes.FactorioObjectData;
//
// export type JsonPrimitiveType = number | string | boolean | undefined | null | void
//
// export type JsonArray<T extends JsonPrimitiveType = void> = T[]
//
// export type JsonTableValueType =
//     JsonPrimitiveType
//     | JsonTable
//     | JsonArray<JsonPrimitiveType>
//     | undefined
//     | null
//
// export interface JsonTable {
//   [key: string]: JsonTableValueType
// }


// export interface FactorioEvent extends JsonTable {
//   /** Schema versioning */
//   mod_version: string,
//   /** game time */
//   tick: uint,
//   /** the initial Factorio event ({defines.events}) trigger */
//   event_type: string,
//   data: FactorioObjectData,
// }
//
// export const FactorioEvent =
//     (<DataType extends FactorioObjectData>(
//             tick: uint,
//             eventType: string,
//             data: DataType
//         ) => {
//           return {
//             mod_version: MOD_VERSION,
//             tick: tick,
//             event_type: eventType,
//             data: data,
//           } as FactorioEvent
//         }
//     )


// export type JsonArray<TValue extends JsonPrimitiveType> = TValue[]
// export class JsonArray extends Array<JsonPrimitiveTypes> {
// }

// export type JsonArray = Array<JsonPrimitiveTypes>
// declare type  JsonArrayConstructor = (new  () => JsonArray)
// declare const JsonArray: JsonArrayConstructor

// export class JsonTable extends LuaTable<string, JsonTableValueType> {
// }
// export  type JsonTable = LuaTable<string, JsonTableValueType>
// const JsonTable = () => new LuaTable<string, JsonTableValueType>()
// export type JsonTableConstructor = (new  () => JsonTable)
// declare const JsonTable: JsonTableConstructor

