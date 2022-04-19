import {isEventType} from "./eventTypeCheck";
import {Converters} from "./converters";
import EventUpdatesManager, {EventUpdates} from "../cache/EventDataCache";
import {
  KafkatorioPacketData2,
  MapChunkPosition,
  MapTileDictionary
} from "../../generated/kafkatorio-schema/kafkatorio-schema";
import CacheKey = EventUpdates.PacketKey;
import Type = KafkatorioPacketData2.Type;


type MapChunkUpdater = (data: KafkatorioPacketData2.MapChunkUpdate) => void


const mapProtoNameToKey: { [key: string]: uint } = {}
let protoIndex: uint = 0


function getProtoKey(protoName: string): uint {
  if (mapProtoNameToKey[protoName] == undefined) {
    mapProtoNameToKey[protoName] = protoIndex++
  }
  return mapProtoNameToKey[protoName]
}


function mapTilesUpdateDebounce(
    surface: LuaSurface | undefined,
    chunkPosition: MapChunkPosition,
    tiles: TileRead[],
    eventName: string,
    updater?: MapChunkUpdater,
    expirationDurationTicks?: uint,
) {

  if (surface == undefined) {
    return
  }

  const key: KafkatorioPacketData2.MapChunkUpdate["key"] = {
    surfaceIndex: surface.index,
    chunkPosition: chunkPosition,
  }

  EventUpdatesManager.debounce<KafkatorioPacketData2.MapChunkUpdate>(
      key,
      Type.MapChunkUpdate,
      data => {
        data.tileDictionary ??= <MapTileDictionary>{
          tilesXY: {},
          protos: {},
        }

        for (const tile of tiles) {

          const xString = `${tile.position.x}`
          const yString = `${tile.position.y}`
          const protoKey = getProtoKey(tile.name)

          data.tileDictionary.protos[tile.name] = protoIndex

          data.tileDictionary.tilesXY[xString] ??= {}
          data.tileDictionary.tilesXY[xString][yString] = protoKey
        }

        data.eventCounts ??= {}
        data.eventCounts[eventName] ??= 0
        data.eventCounts[eventName]++

        if (updater != undefined) {
          updater(data)
        }
      },
      expirationDurationTicks,
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
      log(`on_chunk_generated ${e.tick}`)
      handleChunkGeneratedEvent(e)
    }
)


export function handleChunkGeneratedEvent(
    e: OnChunkGeneratedEvent,
    expirationDurationTicks?: uint,
) {
  let tiles = getTiles(e.surface, e.area)
  mapTilesUpdateDebounce(
      e.surface,
      [e.position.x, e.position.y],
      tiles,
      Converters.eventNameString(e.name),
      undefined,
      expirationDurationTicks,
  )
}


// script.on_event(
//     defines.events.on_chunk_charted,
//     (e: OnChunkChartedEvent) => {
//       log(`on_chunk_charted ${e.tick}`)
//
//       // const surface = getSurface(e.surface_index)
//       // if (surface == undefined) {
//       //   return
//       // }
//       //
//       // let tiles = getTiles(surface, e.area)
//       // mapTilesUpdateDebounce(surface, e.position, tiles, (data => {
//       //   data.force = e.force.index
//       // }))
//     }
// )


script.on_event(
    defines.events.script_raised_set_tiles,
    (e: ScriptRaisedSetTilesEvent) => {
      log(`script_raised_set_tiles ${e.tick}`)

      const surface = getSurface(e.surface_index)
      if (surface == undefined) {
        return
      }

      const groupedTiles = groupTiles(e.tiles)

      for (const [chunkPos, tiles] of groupedTiles) {
        mapTilesUpdateDebounce(surface, chunkPos, tiles, Converters.eventNameString(e.name))
      }
    }
)


function groupTiles(tiles: TileRead[]): Map<MapChunkPosition, TileRead[]> {

  const mapChunkPositionToTiles = new Map<MapChunkPosition, TileRead[]>()
  for (const tile of tiles) {
    const chunkPosition: MapChunkPosition = [
      math.floor((tile.position.x / 32)),
      math.floor((tile.position.y / 32)),
    ]
    if (!mapChunkPositionToTiles.has(chunkPosition)) {
      mapChunkPositionToTiles.set(chunkPosition, [])
    }
    // https://github.com/TypeScriptToLua/TypeScriptToLua/issues/1256
    // mapChunkPositionToTiles.get(chunkPosition)?.push(tile)
    const tiles = mapChunkPositionToTiles.get(chunkPosition)
    if (tiles != null) {
      tiles.push(tile)
    }
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


function handleBuiltTileEvent(event: OnPlayerBuiltTileEvent | OnRobotBuiltTileEvent) {

  const surface = getSurface(event.surface_index)
  if (surface == undefined) {
    return
  }

  const tiles: TileRead[] = convertOldPosition(event.tiles, event.tile)
  const groupedTiles = groupTiles(tiles)
  const eventName = Converters.eventNameString(event.name)

  for (const [chunkPos, tiles] of groupedTiles) {
    mapTilesUpdateDebounce(
        surface,
        chunkPos,
        tiles,
        eventName,
        (data => {
              if (isEventType(event, defines.events.on_player_built_tile)) {
                data.player = event.player_index
                return
              }
              if (isEventType(event, defines.events.on_robot_built_tile)) {
                data.robot = {
                  unitNumber: event.robot?.unit_number ?? null,
                  name: event.robot.name,
                  protoType: event.robot.type,
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


script.on_event(defines.events.on_player_built_tile, (e: OnPlayerBuiltTileEvent) => {
  log(`on_player_built_tile ${e.tick}`)
  handleBuiltTileEvent(e)
})
script.on_event(defines.events.on_robot_built_tile, (e: OnRobotBuiltTileEvent) => {
  log(`on_robot_built_tile ${e.tick}`)
  handleBuiltTileEvent(e)
})


// function handleMinedTileEvent(event: OnPlayerMinedTileEvent | OnRobotMinedTileEvent) {
// }
//
// script.on_event(defines.events.on_player_mined_tile, (e: OnPlayerMinedTileEvent) => {
//   log(`on_player_mined_tile ${e.tick}`)
//   handleMinedTileEvent(e)
// })
// script.on_event(defines.events.on_robot_mined_tile, (e: OnRobotMinedTileEvent) => {
//   log(`on_robot_mined_tile ${e.tick}`)
//   handleMinedTileEvent(e)
// })


script.on_event(
    defines.events.on_pre_chunk_deleted,
    (e: OnPreChunkDeletedEvent) => {
      log(`on_pre_chunk_deleted ${e.tick}`)

      const surface = getSurface(e.surface_index)
      if (surface == undefined) {
        return
      }

      for (const position of e.positions) {
        const key: CacheKey<KafkatorioPacketData2.MapChunkUpdate> = {
          surfaceIndex: surface.index,
          chunkPosition: [position.x, position.y],
        }

        EventUpdatesManager.debounce<KafkatorioPacketData2.MapChunkUpdate>(
            key,
            Type.MapChunkUpdate,
            data => data.isDeleted = true,
            0, // deletion is important - emit ASAP
        )
      }
    }
)
