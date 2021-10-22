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


import {JsonTable, JsonArray} from "../model/model";

export namespace Serdes {

  export namespace Player {

    export function playerToTable(player: LuaPlayer): JsonTable {
      let charIds = Entity.entitiesToUnitNumbers(player.get_associated_characters())
      return {
        name: player.name,
        character_unit_number: player.character?.unit_number,
        associated_characters_unit_numbers: charIds,
        position: DataClasses.positionTableToTable(player.position),
      }
    }

  }

  export namespace Entity {

    export function entityToTable(entity: LuaEntity): JsonTable {

      let data: JsonTable = {
        name: entity.name,
        type: entity.type,
        active: entity.active,
        health: entity.health,
        surface_index: entity.surface.index,
        unit_number: entity.unit_number,
        position: DataClasses.positionTableToTable(entity.position),
      }

      if (entity.is_player()) {
        let player = entity.player!!
        data.player_index = player.index
      }

      return data
    }

    export function entitiesToUnitNumbers(entities: LuaEntity[]): JsonArray<number> {
      let result: (number) [] = []
      for (let entity of entities) {
        if (entity.unit_number != null) {
          result[result.length] = entity.unit_number
        }
      }
      return result
    }

  }

  export namespace Surface {

    export function surfaceToTable(surface: LuaSurface): JsonTable {
      return {
        name: surface.name,
        index: surface.index,
        daytime: surface.daytime,
      }
    }

  }

  export namespace DataClasses {

    export function positionTableToTable(positionTable: PositionTable): JsonTable {
      return {
        x: positionTable.x,
        y: positionTable.y,
      }
    }

  }
}
