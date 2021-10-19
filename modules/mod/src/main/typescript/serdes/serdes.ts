export type PrimitiveType = number | string | boolean
export type JsonArray<TValue extends PrimitiveType> = TValue[]
export type JsonTableValueType =
    PrimitiveType
    | JsonTable
    | JsonArray<PrimitiveType>
export type JsonTable = LuaTable<string, JsonTableValueType>  // & table
// export type JsonArray = LuaArray<PrimitiveType>               // & table


export namespace Serdes {

  export namespace Player {

    export function playerToTable(player: LuaPlayer): JsonTable {
      let table = new LuaTable<string, JsonTableValueType>()

      table.set("name", player.name)
      let charIds: JsonArray<number> = Entity.entitiesToUnitNumbers(player.get_associated_characters())
      table.set("characterIds", charIds)

      return table
    }

  }

  export namespace Entity {

    export function entityToTable(entity: LuaEntity): JsonTable {
      let table = new LuaTable<string, JsonTableValueType>()

      if (entity.player?.index != null) {
        table.set("playerId", entity.player?.index)
      }
      table.set("type", entity.type)
      table.set("active", entity.active)

      if (entity.health != null) {
        table.set("health", entity.health)
      }

      table.set("surfaceId", entity.surface.index)
      table.set("position", DataClasses.positionTableToTable(entity.position))

      return table
    }

    export function entitiesToUnitNumbers(entities: LuaEntity[]): JsonArray<number> {
      let array: number[] = []
      let j: number = 0
      for (let i = 0; i < entities.length; i++) {
        let unitNum = entities[i].unit_number
        if (unitNum != null) {
          array[j++] = unitNum
        }
      }
      return array
    }

  }

  export namespace Surface {

    export function surfaceToTable(surface: LuaSurface): JsonTable {
      let table = new LuaTable<string, JsonTableValueType>()

      table.set("name", surface.name)
      table.set("index", surface.index)
      table.set("daytime", surface.daytime)

      return table
    }

  }

  export namespace DataClasses {

    export function positionTableToTable(positionTable: PositionTable): JsonTable {
      let table = new LuaTable<string, JsonTableValueType>()
      table.set("x", positionTable.x)
      table.set("y", positionTable.y)
      return table
    }

  }
}
