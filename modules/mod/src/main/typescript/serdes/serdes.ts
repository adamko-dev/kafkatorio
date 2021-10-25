import {JsonTable, JsonArray} from "../model/model";

import PositionData = Serdes.DataClasses.PositionData;

export namespace Serdes {

  export interface FactorioObjectData extends JsonTable {
    object_name: string
  }

  export namespace Player {

    export interface PlayerData extends FactorioObjectData {
      name: string
      character_unit_number?: uint
      associated_characters_unit_numbers: JsonArray<number>
      position: PositionData
    }

    export function playerToTable(player: LuaPlayer): PlayerData {
      let charIds = Entity.entitiesToUnitNumbers(player.get_associated_characters())
      return {
        object_name: player.object_name,
        name: player.name,
        character_unit_number: player.character?.unit_number,
        associated_characters_unit_numbers: charIds,
        position: DataClasses.positionTableToTable(player.position),
      }
    }

  }

  export namespace Entity {

    interface EntityData extends FactorioObjectData {
      name: string
      type: string
      active: boolean
      health?: number
      surface_index: int
      unit_number?: number
      position: PositionData
      player_index?: number
    }

    export function entityToTable(entity: LuaEntity): EntityData {

      let data: EntityData = {
        object_name: entity.object_name,
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

    interface SurfaceData extends FactorioObjectData {
      name: string
      index: uint
      daytime: number
    }

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

    export interface PositionData extends JsonTable {
      x: int
      y: int
    }

    export function positionTableToTable(positionTable: PositionTable): PositionData {
      return {
        x: positionTable.x,
        y: positionTable.y,
      }
    }

  }
}
