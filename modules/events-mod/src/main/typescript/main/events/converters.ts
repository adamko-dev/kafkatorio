import {EventName} from "../types";
import {Colour} from "../../generated/kafkatorio-schema/kafkatorio-schema";

export namespace Converters {

  // export function playerToTable(player: LuaPlayer): PlayerData {
  //   let charIds = entitiesToUnitNumbers(player.get_associated_characters())
  //   return {
  //     objectName: player.object_name,
  //
  //     name: player.name,
  //     characterUnitNumber: player.character?.unit_number ?? null,
  //     associatedCharactersUnitNumbers: charIds,
  //     position: mapEntityPosition(player.position),
  //     colour: mapColour(player.color),
  //     chatColour: mapColour(player.chat_color),
  //     lastOnline: player.last_online
  //   }
  // }


  const mapEventIdToName = new LuaTable<defines.Events, EventName>()
  for (const [eventName, eventId] of pairs(defines.events)) {
    mapEventIdToName.set(eventId, eventName)
  }


  export function eventNameString(event: defines.Events): EventName {
    return mapEventIdToName.get(event)
  }


  // export function entityToTable(entity: LuaEntity): EntityData {
  //
  //   let player: LuaPlayer | null = entity.is_player() ? entity.player!! : null
  //
  //   return {
  //     objectName: entity.object_name,
  //
  //     // entity data
  //     name: entity.name,
  //     type: entity.type,
  //     active: entity.active,
  //     health: entity.health ?? null,
  //     healthRatio: entity.get_health_ratio() ?? null,
  //     surface: entity.surface.index,
  //     unitNumber: entity.unit_number ?? null,
  //     position: mapEntityPosition(entity.position),
  //     force: entity.force.index,
  //
  //     // player data
  //     playerIndex: player?.index ?? null
  //   }
  // }
  //
  // function entitiesToUnitNumbers(entities: LuaEntity[]): uint[] {
  //   let result: (uint) [] = []
  //   for (let entity of entities) {
  //     if (entity.unit_number != null) {
  //       result[result.length] = entity.unit_number
  //     }
  //   }
  //   return result
  // }


  // export function mapEntityPosition(mapPos: MapPositionTable): MapEntityPosition {
  //   return {
  //     x: mapPos.x,
  //     y: mapPos.y,
  //   }
  // }

  // export function mapTilePosition(mapPos: MapPositionTable): MapTilePosition {
  //   return {
  //     x: mapPos.x,
  //     y: mapPos.y,
  //   }
  // }

  // export function mapChunkPosition(mapPos: ChunkPositionTable): MapChunkPosition {
  //   return {
  //     x: mapPos.x,
  //     y: mapPos.y,
  //   }
  // }

  export function mapColour(color: ColorTable): Colour {
    return [
      color.r ?? 0,
      color.g ?? 0,
      color.b ?? 0,
      color.a ?? 0,
    ]
  }


  // export function convertTile(tile: LuaTile): MapTile {
  //   return {
  //     x: tile.position.x,
  //     y: tile.position.y,
  //     proto: tile.prototype.name,
  //   }
  // }
  //
  // export function convertTiles(tiles: LuaTile[]): MapTile[] {
  //   return tiles.map((tile: LuaTile) => convertTile(tile))
  // }

  // export function convertBoundingBox(bb: BoundingBoxRead): MapBoundingBox {
  //   return {
  //     topLeft: mapTilePosition(bb.left_top),
  //     bottomRight: mapTilePosition(bb.right_bottom),
  //   }
  // }

}
