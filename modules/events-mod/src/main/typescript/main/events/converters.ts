import {EventName} from "../types";
import {Colour, MapChunkPosition} from "../../generated/kafkatorio-schema/kafkatorio-schema";

export namespace Converters {

  const mapEventIdToName = new LuaTable<defines.Events, EventName>()
  for (const [eventName, eventId] of pairs(defines.events)) {
    mapEventIdToName.set(eventId, eventName)
  }


  export function eventNameString(event: defines.Events): EventName {
    return mapEventIdToName.get(event)
  }


  export function mapColour(color: ColorTable): Colour {
    return [
      color.r ?? 0,
      color.g ?? 0,
      color.b ?? 0,
      color.a ?? 0,
    ]
  }


  export function convertPlacedTiles(
      placedTile: LuaTilePrototype,
      oldTiles: OldTileAndPosition[],
  ): TileRead[] {
    const converted: TileRead[] = []
    for (const [, tile] of ipairs(oldTiles)) {
      converted[converted.length] = {
        position: {x: tile.position.x, y: tile.position.y},
        name: placedTile.name,
      }
    }
    return converted
  }


  export function convertRemovedTiles(
      surface: LuaSurface,
      oldTiles: OldTileAndPosition[],
  ): TileRead[] {
    const converted: TileRead[] = []
    for (const [, tile] of ipairs(oldTiles)) {
      converted[converted.length] = surface.get_tile(tile.position.x, tile.position.y)
    }
    return converted
  }


  export function convertMapTablePosition(
      position: MapPositionTable,
  ): MapChunkPosition {
    return [position.x, position.y]
  }

}
