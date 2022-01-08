export namespace Converters {

  export function playerToTable(player: LuaPlayer): PlayerData {
    let charIds = entitiesToUnitNumbers(player.get_associated_characters())
    return {
      objectName: player.object_name,

      name: player.name,
      characterUnitNumber: player.character?.unit_number ?? null,
      associatedCharactersUnitNumbers: charIds,
      position: convertMapPosition(player.position),
      colour: mapColour(player.color),
      chatColour: mapColour(player.chat_color),
      lastOnline: player.last_online
    }
  }

  export function entityToTable(entity: LuaEntity): EntityData {

    let player: LuaPlayer | null = entity.is_player() ? entity.player!! : null

    return {
      objectName: entity.object_name,

      // entity data
      name: entity.name,
      type: entity.type,
      active: entity.active,
      health: entity.health ?? null,
      healthRatio: entity.get_health_ratio(),
      surfaceIndex: entity.surface.index,
      unitNumber: entity.unit_number ?? null,
      position: convertMapPosition(entity.position),

      // player data
      playerIndex: player?.index ?? null
    }
  }

  function entitiesToUnitNumbers(entities: LuaEntity[]): uint[] {
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
      objectName: surface.object_name,

      name: surface.name,
      index: surface.index,
      daytime: surface.daytime,
    }
  }

  export function convertMapPosition(mapPos: MapPositionTable): PositionData {
    return {
      type: "MAP",
      x: mapPos.x,
      y: mapPos.y,
    }
  }

  export function mapColour(color: ColorTable): Colour {
    return {
      red: color.r ?? 0,
      green: color.g ?? 0,
      blue: color.b ?? 0,
      alpha: color.a ?? 0,
    }
  }

  export function consoleChat(content: string, playerIndex?: uint): ConsoleChatMessage {
    return {
      objectName: "ConsoleChatMessage",

      authorPlayerIndex: playerIndex ?? null,
      content: content
    }
  }

  export function convertTile(tile: LuaTile): FactorioTile {
    return {
      objectName: tile.object_name,

      position: convertMapPosition(tile.position),
      prototypeName: tile.prototype.name,
      surfaceIndex: tile.surface.index
    }
  }

}
