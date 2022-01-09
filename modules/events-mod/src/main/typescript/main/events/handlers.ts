import {Converters} from "./converters"
import {emitEvent} from "./emitEvent";

export function handlePlayerUpdate(tick: uint, playerIndex: uint, eventType: string) {
  let player: LuaPlayer = game.players[playerIndex]
  let table = Converters.playerToTable(player)


  emitEvent(table, tick, eventType)

  handleCharactersEvent(tick, playerIndex, eventType)
}

export function handleCharactersEvent(tick: uint, playerIndex: uint, eventType: string) {

  let player: LuaPlayer = game.players[playerIndex]

  if (player.character != undefined) {
    handleEntityUpdate(tick, player.character, eventType)
  }
  for (const character of player.get_associated_characters()) {
    if (character != undefined) {
      handleEntityUpdate(tick, character, eventType)
    }
  }
}

export function handleEntityUpdate(tick: uint, entity: LuaEntity, eventType: string) {
  let table = Converters.entityToTable(entity)
  emitEvent(table, tick, eventType)
}

export function handleSurfaceUpdate(
    tick: uint,
    surface: LuaSurface,
    eventType: string
) {
  let table = Converters.surfaceToTable(surface)
  emitEvent(table, tick, eventType)
}

export function handleConsoleChat(
    tick: uint,
    playerIndex: uint | undefined,
    message: string,
    eventType: string,
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

  let convertedTiles: FactorioMapTile[] = tiles.map((tile: LuaTile) => {
    return Converters.convertTile(tile)
  })

  let mapChunk: FactorioMapChunk = {
    objectName: "FactorioMapChunk",
    tiles: convertedTiles,
  }

  emitEvent(mapChunk, tick, eventType)
}