import {Converters} from "./converters"
import {emitEvent} from "./emitEvent";

// export function handlePlayerUpdate(tick: uint, eventType: string, playerIndex: uint) {
//   let player: LuaPlayer = game.players[playerIndex]
//   let table = Converters.playerToTable(player)
//
//   emitEvent(table, tick, eventType)
//
//   handleCharactersEvent(tick, eventType, playerIndex)
// }

export function handleCharactersEvent(tick: uint, eventType: string, playerIndex: uint) {

  let player: LuaPlayer = game.players[playerIndex]

  if (player.character != undefined) {
    handleEntityUpdate(tick, eventType, player.character)
  }
  for (const character of player.get_associated_characters()) {
    if (character != undefined) {
      handleEntityUpdate(tick, eventType, character)
    }
  }
}

export function handleEntityUpdate(tick: uint, eventType: string, entity: LuaEntity) {
  let table = Converters.entityToTable(entity)
  emitEvent(table, tick, eventType)
}

export function handleSurfaceUpdate(
    tick: uint,
    eventType: string,
    surface: LuaSurface,
) {
  let table = Converters.surfaceToTable(surface)
  emitEvent(table, tick, eventType)
}

export function handleConsoleChat(
    tick: uint,
    eventType: string,
    playerIndex: uint | undefined,
    message: string,
) {
  let table = Converters.consoleChat(message, playerIndex)
  emitEvent(table, tick, eventType)
}

export function handleChunkUpdate(
    tick: uint,
    eventType: string,
    surfaceIndex: uint,
    position: ChunkPositionTable,
    area: BoundingBoxRead,
) {

  let surface = game.get_surface(surfaceIndex)

  let tiles: LuaTile[] = surface?.find_tiles_filtered({
        area: area,
        collision_mask: ["ground-tile", "water-tile"]
      }
  ) ?? []

  let convertedTiles: MapTile[] = tiles.map((tile: LuaTile) => {
    return Converters.convertTile(tile)
  })

  let mapChunk: MapChunk = {
    objectName: "MapChunk",
    tiles: createMapTiles(surfaceIndex, convertedTiles),
    position: Converters.mapChunkPosition(position),
    area: Converters.convertBoundingBox(area),
  }

  emitEvent(mapChunk, tick, eventType)
}

// export function handleTilesUpdate(
//     tick: uint,
//     eventType: string,
//     surfaceIndex: uint,
//     newTileType: LuaTilePrototype,
//     updatedTiles: OldTileAndPosition[],
// ) {
//
//   if (!newTileType.collision_mask["ground-tile"] && !newTileType.collision_mask["water-tile"]) {
//     return
//   } else {
//
//     let convertedTiles: MapTile[] = updatedTiles.map((it) => {
//       return {
//         x: it.position.x,
//         y: it.position.y,
//         proto: newTileType.name
//       }
//     })
//
//     let mapTiles: MapTiles = createMapTiles(surfaceIndex, convertedTiles)
//
//     emitEvent(mapTiles, tick, eventType)
//   }
// }

function createMapTiles(
    surfaceIndex: uint,
    tiles: MapTile[],
): MapTiles {
  return {
    objectName: "LuaTiles",
    surfaceIndex: surfaceIndex,
    tiles: tiles,
  }
}
