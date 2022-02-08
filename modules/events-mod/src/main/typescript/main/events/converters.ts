export namespace Converters {

  export function playerToTable(player: LuaPlayer): PlayerData {
    let charIds = entitiesToUnitNumbers(player.get_associated_characters())
    return {
      objectName: player.object_name,

      name: player.name,
      characterUnitNumber: player.character?.unit_number ?? null,
      associatedCharactersUnitNumbers: charIds,
      position: mapEntityPosition(player.position),
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
      position: mapEntityPosition(entity.position),

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

  export function mapEntityPosition(mapPos: MapPositionTable): MapEntityPosition {
    return {
      x: mapPos.x,
      y: mapPos.y,
    }
  }

  export function mapTilePosition(mapPos: MapPositionTable): MapTilePosition {
    return {
      x: mapPos.x,
      y: mapPos.y,
    }
  }

  export function mapChunkPosition(mapPos: ChunkPositionTable): MapChunkPosition {
    return {
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

  export function convertTile(tile: LuaTile): MapTile {
    return {
      position: mapTilePosition(tile.position),
      prototypeName: tile.prototype.name,
    }
  }

  export function convertBoundingBox(bb: BoundingBoxRead): MapBoundingBox {
    return {
      topLeft: mapTilePosition(bb.left_top),
      bottomRight: mapTilePosition(bb.right_bottom),
    }
  }

}
