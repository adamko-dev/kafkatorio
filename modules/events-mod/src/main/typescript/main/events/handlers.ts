import {Converters} from "./converters"
import {emitEvent} from "./emitEvent";

export function handlePlayerUpdate(tick: uint, eventType: string, playerIndex: uint) {
  let player: LuaPlayer = game.players[playerIndex]
  let table = Converters.playerToTable(player)


  emitEvent(table, tick, eventType)

  handleCharactersEvent(tick, eventType, playerIndex)
}

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

const CHUNK_SIZE = 32

export function handleChunkUpdate(
    tick: uint,
    eventType: string,
    surfaceIndex: uint,
    position: ChunkPosition
) {

  let surface = game.get_surface(surfaceIndex)

  let x = (position as ChunkPositionTable).x * CHUNK_SIZE
  let y = (position as ChunkPositionTable).y * CHUNK_SIZE

  let tiles: LuaTile[] = surface?.find_tiles_filtered({
        position: {x: x, y: y},
        radius: CHUNK_SIZE,
        collision_mask: ["ground-tile", "water-tile"]
      }
  ) ?? []

  let convertedTiles: MapTile[] = tiles.map((tile: LuaTile) => {
    return Converters.convertTile(tile)
  })

  let mapChunk: MapChunk = {
    objectName: "MapChunk",
    tiles: convertedTiles,
  }

  emitEvent(mapChunk, tick, eventType)
}
