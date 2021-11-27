// import {JsonTable, JsonArray} from "../model/model";


export namespace Serdes {

  // export interface FactorioObjectData extends JsonTable {
  //   object_name: string
  // }

  export namespace Player {

    export function playerToTable(player: LuaPlayer): PlayerData {
      let charIds = Entity.entitiesToUnitNumbers(player.get_associated_characters())
      return {
        object_name: player.object_name,
        name: player.name,
        character_unit_number: player.character?.unit_number ?? null,
        associated_characters_unit_numbers: charIds,
        position: DataClasses.positionTableToTable(player.position),
      }
    }

  }

  export namespace Entity {

    export function entityToTable(entity: LuaEntity): EntityData {

      let player: LuaPlayer | null = entity.is_player() ? entity.player!! : null

      return {
        object_name: entity.object_name,
        name: entity.name,
        type: entity.type,
        active: entity.active,
        health: entity.health ?? null,
        health_ratio: entity.get_health_ratio(),
        surface_index: entity.surface.index,
        unit_number: entity.unit_number ?? null,
        position: DataClasses.positionTableToTable(entity.position),

        // player data:
        player_index: player?.index ?? null
      }
    }

    export function entitiesToUnitNumbers(entities: LuaEntity[]): uint[] {
      let result: (uint) [] = []
      for (let entity of entities) {
        if (entity.unit_number != null) {
          result[result.length] = entity.unit_number
        }
      }
      return result
    }

  }

  export namespace Surface {

    export function surfaceToTable(surface: LuaSurface): SurfaceData {
      return {
        object_name: surface.object_name,
        name: surface.name,
        index: surface.index,
        daytime: surface.daytime,
      }
    }

  }

  export namespace DataClasses {

    export function positionTableToTable(positionTable: PositionTable): PositionData {
      return {
        x: positionTable.x,
        y: positionTable.y,
      }
    }

  }
}
