export type JsonPrimitiveType = number | string | boolean | undefined | null | void

// export type JsonArray<TValue extends JsonPrimitiveType> = TValue[]
// export class JsonArray extends Array<JsonPrimitiveTypes> {
// }

// export type JsonArray = Array<JsonPrimitiveTypes>
// declare type  JsonArrayConstructor = (new  () => JsonArray)
// declare const JsonArray: JsonArrayConstructor

export type JsonArray<T extends JsonPrimitiveType = void> = T[]

export type JsonTableValueType =
    JsonPrimitiveType
    | JsonTable
    | JsonArray<JsonPrimitiveType>
    | undefined
    | null

// export class JsonTable extends LuaTable<string, JsonTableValueType> {
// }
// export  type JsonTable = LuaTable<string, JsonTableValueType>
// const JsonTable = () => new LuaTable<string, JsonTableValueType>()
// export type JsonTableConstructor = (new  () => JsonTable)
// declare const JsonTable: JsonTableConstructor


export interface JsonTable {
  [key: string]: JsonTableValueType
}

export namespace Serdes {

  export namespace Player {

    export function playerToTable(player: LuaPlayer): JsonTable {
      let charIds = Entity.entitiesToUnitNumbers(player.get_associated_characters())
      return {
        "name": player.name,
        "characterId": player.character?.unit_number,
        "associatedCharacterIds": charIds,
        "position": DataClasses.positionTableToTable(player.position),
      }
    }

  }

  export namespace Entity {

    export function entityToTable(entity: LuaEntity): JsonTable {

      let player: JsonTable =
          entity.is_player() ? {
            "playerId": entity.player?.index,
          } : {}

      return {
        player,
        "name": entity.name,
        "type": entity.type,
        "active": entity.active,
        "health": entity.health,
        "surfaceId": entity.surface.index,
        "unit_number": entity.unit_number,
        "position": DataClasses.positionTableToTable(entity.position),
      }
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
        "name": surface.name,
        "index": surface.index,
        "daytime": surface.daytime,
      }
    }

  }

  export namespace DataClasses {

    export function positionTableToTable(positionTable: PositionTable): JsonTable {
      return {
        "x": positionTable.x,
        "y": positionTable.y,
      }
    }

  }
}
