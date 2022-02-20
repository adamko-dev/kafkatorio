import {EventDataCache} from "../cache/EventDataCache";
import CacheKey = EventDataCache.CacheKey;
import CacheData = EventDataCache.CacheData;


type MapChunkUpdater = (data: CacheData<"MAP_CHUNK">) => void


function mapTilesUpdateDebounce(
    surface: LuaSurface | undefined,
    chunkPosition: ChunkPositionTable,
    tiles: TileRead[],
    updater?: MapChunkUpdater
) {
  if (surface == undefined) {
    return
  }

  EventDataCache.debounce<"MAP_CHUNK">(
      {
        surfaceIndex: surface.index,
        chunkPosition: chunkPosition,
        updateType: "MAP_CHUNK",
      },
      data => {
        if (data.tiles == undefined) {
          data.tiles = new Map<MapTilePosition, string>()
        }

        for (const tile of tiles) {
          const position: MapTilePosition = {x: tile.position.x, y: tile.position.y}
          data.tiles.set(position, tile.name)
        }

        if (updater != undefined) {
          updater(data)
        }
      }
  )
}


function getTiles(
    surface: LuaSurface | undefined,
    area: BoundingBoxTable
): LuaTile[] {
  return surface?.find_tiles_filtered({
        area: area,
        // collision_mask: ["ground-tile", "water-tile"]
      }
  ) ?? []
}

function getSurface(surfaceIndex: uint): LuaSurface | undefined {
  return game.surfaces[surfaceIndex]
}

script.on_event(
    defines.events.on_chunk_generated,
    (e: OnChunkGeneratedEvent) => {
      let tiles = getTiles(e.surface, e.area)
      mapTilesUpdateDebounce(e.surface, e.position, tiles)
    }
)

script.on_event(
    defines.events.on_chunk_charted,
    (e: OnChunkChartedEvent) => {

      const surface = getSurface(e.surface_index)
      if (surface == undefined) {
        return
      }

      let tiles = getTiles(surface, e.area)
      mapTilesUpdateDebounce(surface, e.position, tiles, (data => {
        data.force = e.force.index
      }))
    }
)

script.on_event(
    defines.events.script_raised_set_tiles,
    (e: ScriptRaisedSetTilesEvent) => {

      const surface = getSurface(e.surface_index)
      if (surface == undefined) {
        return
      }

      const groupedTiles = groupTiles(e.tiles)

      for (const [chunkPos, tiles] of groupedTiles) {
        mapTilesUpdateDebounce(surface, chunkPos, tiles)
      }
    }
)


function groupTiles(tiles: TileRead[]): Map<MapChunkPosition, TileRead[]> {

  const mapChunkPositionToTiles = new Map<MapChunkPosition, TileRead[]>()
  for (const tile of tiles) {
    const chunkPosition = {
      x: math.floor((tile.position.x / 32)),
      y: math.floor((tile.position.y / 32)),
    }
    if (!mapChunkPositionToTiles.has(chunkPosition)) {
      mapChunkPositionToTiles.set(chunkPosition, [])
    }
    mapChunkPositionToTiles.get(chunkPosition)?.push(tile)
  }
  return mapChunkPositionToTiles
}

function convertOldPosition(
    oldTiles: OldTileAndPosition[],
    placedTile: LuaTilePrototype,
): TileRead[] {

  return oldTiles.map((tile) => {
        return {
          position: {x: tile.position.x, y: tile.position.y},
          name: placedTile.name,
        }
      }
  )
}

function onBuildTileEvent(event: OnPlayerBuiltTileEvent | OnRobotBuiltTileEvent) {

  const surface = getSurface(event.surface_index)
  if (surface == undefined) {
    return
  }

  const tiles: TileRead[] = convertOldPosition(event.tiles, event.tile)
  const groupedTiles = groupTiles(tiles)

  for (const [chunkPos, tiles] of groupedTiles) {
    mapTilesUpdateDebounce(
        surface,
        chunkPos,
        tiles,
        (data => {
              if (isEventType(event, defines.events.on_player_built_tile)) {
                data.player = event.player_index
                return
              }
              if (isEventType(event, defines.events.on_robot_built_tile)) {
                data.robot = {
                  unitNumber: event.robot?.unit_number ?? null,
                  name: event.robot.name,
                  type: event.robot.type,
                }
                return
              }
              // this line will not compile if the previous 'if' statements are not exhaustive
              // noinspection UnnecessaryLocalVariableJS,JSUnusedLocalSymbols
              const exhaustiveCheck: never = event
            }
        )
    )
  }
}

script.on_event(defines.events.on_player_built_tile, (e: OnPlayerBuiltTileEvent) => onBuildTileEvent(e))
script.on_event(defines.events.on_robot_built_tile, (e: OnRobotBuiltTileEvent) => onBuildTileEvent(e))

// script.on_event(
//     defines.events.on_player_mined_tile,
//     (e: OnPlayerMinedTileEvent) => {
//       // need to group tiles by ChunkPosition...
//
//     }
// )

// script.on_event(
//     defines.events.on_robot_mined_tile,
//     (e: OnRobotMinedTileEvent) => {
//       // need to group tiles by ChunkPosition...
//
//     }
// )


script.on_event(
    defines.events.on_pre_chunk_deleted,
    (e: OnPreChunkDeletedEvent) => {

      const surface = getSurface(e.surface_index)
      if (surface == undefined) {
        return
      }

      for (const position of e.positions) {
        const key: CacheKey<"MAP_CHUNK"> = {
          surfaceIndex: surface.index,
          chunkPosition: position,
          updateType: "MAP_CHUNK",
        }

        EventDataCache.debounce<"MAP_CHUNK">(
            key,
            data => {
              data.isDeleted = true
            }
        )
        // deletion is important - emit ASAP
        EventDataCache.setExpiration(key, 0)
      }
    }
)
