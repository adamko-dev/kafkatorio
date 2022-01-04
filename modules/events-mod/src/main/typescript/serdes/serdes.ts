export namespace Serdes {


  export function playerToTable(player: LuaPlayer): PlayerData {
    let charIds = entitiesToUnitNumbers(player.get_associated_characters())
    return {
      object_name: player.object_name,

      name: player.name,
      character_unit_number: player.character?.unit_number ?? null,
      associated_characters_unit_numbers: charIds,
      position: positionTableToTable(player.position),
      colour: mapColour(player.color),
      chat_colour: mapColour(player.chat_color),
      last_online: player.last_online
    }
  }

  export function entityToTable(entity: LuaEntity): EntityData {

    let player: LuaPlayer | null = entity.is_player() ? entity.player!! : null

    return {
      object_name: entity.object_name,

      // entity data
      name: entity.name,
      type: entity.type,
      active: entity.active,
      health: entity.health ?? null,
      health_ratio: entity.get_health_ratio(),
      surface_index: entity.surface.index,
      unit_number: entity.unit_number ?? null,
      position: positionTableToTable(entity.position),

      // player data
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

  export function surfaceToTable(surface: LuaSurface): SurfaceData {
    return {
      object_name: surface.object_name,

      name: surface.name,
      index: surface.index,
      daytime: surface.daytime,
    }
  }

  export function positionTableToTable(positionTable: PositionTable): PositionData {
    return {
      x: positionTable.x,
      y: positionTable.y,
    }
  }

  export function mapColour(color: Color): Colour {
    color = (color as ColorTable)
    return {
      red: color.r ?? 0,
      green: color.g ?? 0,
      blue: color.b ?? 0,
      alpha: color.a ?? 0,
    }
  }

}
