import {isEventType} from "./eventTypeCheck";
import {Converters} from "./converters";
import EventUpdatesManager, {EventUpdates} from "../cache/EventDataCache";
import {
  KafkatorioPacketData,
  MapChunkPosition
} from "../../generated/kafkatorio-schema";
import CacheKey = EventUpdates.PacketKey;
import Type = KafkatorioPacketData.Type;


type MapChunkUpdater = (data: KafkatorioPacketData.MapChunkUpdate) => void


type MapTileUpdateEvent =
    | OnChunkGeneratedEvent
    | OnPlayerBuiltTileEvent
    | OnPlayerMinedEntityEvent
    | OnPlayerMinedTileEvent
    | OnRobotBuiltTileEvent
    | OnRobotMinedEntityEvent
    | OnRobotMinedTileEvent
    | ScriptRaisedSetTilesEvent


function mapTilesUpdateDebounce(
    surface: LuaSurface | undefined,
    chunkPosition: MapChunkPosition,
    tiles: TileRead[],
    event: MapTileUpdateEvent,
    updater?: MapChunkUpdater,
    expirationDurationTicks?: uint,
) {

  if (surface == undefined) {
    return
  }

  const eventName = Converters.eventNameString(event.name)

  const key: KafkatorioPacketData.MapChunkUpdate["key"] = {
    surfaceIndex: surface.index,
    chunkPosition: chunkPosition,
  }

  EventUpdatesManager.debounce<KafkatorioPacketData.MapChunkUpdate>(
      key,
      Type.MapChunkUpdate,
      data => {

        // tile dictionary update
        if (data.tileDictionary == undefined) {
          data.tileDictionary = {
            tilesXY: {},
            protos: {},
          }
        }

        let protosCount: uint = table_size(data.tileDictionary.protos)
        for (const tile of tiles) {
          data.tileDictionary.protos[tile.name] ??= protosCount++

          const protoKey = data.tileDictionary.protos[tile.name]

          const xString = `${tile.position.x}`
          const yString = `${tile.position.y}`

          data.tileDictionary.tilesXY[xString] ??= {}
          data.tileDictionary.tilesXY[xString][yString] = protoKey
        }

        // events count update
        data.events ??= {}
        data.events[eventName] ??= []
        data.events[eventName].push(event.tick)

        // apply mutator
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
    event: OnChunkGeneratedEvent,
    expirationDurationTicks?: uint,
) {
  let tiles = getTiles(event.surface, event.area)
  mapTilesUpdateDebounce(
      event.surface,
      Converters.convertMapTablePosition(event.position),
      tiles,
      event,
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
    (event: ScriptRaisedSetTilesEvent) => {
      log(`script_raised_set_tiles ${event.tick}`)

      const surface = getSurface(event.surface_index)
      if (surface == undefined) {
        return
      }

      const groupedTiles = groupTiles(event.tiles)

      for (const [chunkPos, tiles] of groupedTiles) {
        mapTilesUpdateDebounce(surface, chunkPos, tiles, event)
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
    mapChunkPositionToTiles.get(chunkPosition)?.push(tile)
    // const tiles = mapChunkPositionToTiles.get(chunkPosition)
    // if (tiles != null) {
    //   tiles.push(tile)
    // }
  }
  return mapChunkPositionToTiles
}


script.on_event(defines.events.on_player_built_tile, (e: OnPlayerBuiltTileEvent) => {
  log(`on_player_built_tile ${e.tick}`)
  handleBuiltTileEvent(e)
})
script.on_event(defines.events.on_robot_built_tile, (e: OnRobotBuiltTileEvent) => {
  log(`on_robot_built_tile ${e.tick}`)
  handleBuiltTileEvent(e)
})

function handleBuiltTileEvent(event: OnPlayerBuiltTileEvent | OnRobotBuiltTileEvent) {

  const surface = getSurface(event.surface_index)
  if (surface == undefined) {
    const eventName = Converters.eventNameString(event.name)
    log(`[handleBuiltTileEvent] undefined surface ${eventName}`)
    return
  }

  const tiles: TileRead[] = Converters.convertPlacedTiles(event.tile, event.tiles)
  const groupedTiles = groupTiles(tiles)

  for (const [chunkPos, tiles] of groupedTiles) {
    mapTilesUpdateDebounce(
        surface,
        chunkPos,
        tiles,
        event,
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


script.on_event(defines.events.on_player_mined_tile, (e: OnPlayerMinedTileEvent) => {
  log(`on_player_mined_tile ${e.tick}`)
  handleMinedTileEvent(e)
})

script.on_event(defines.events.on_robot_mined_tile, (e: OnRobotMinedTileEvent) => {
  log(`on_robot_mined_tile ${e.tick}`)
  handleMinedTileEvent(e)
})

function handleMinedTileEvent(event: OnPlayerMinedTileEvent | OnRobotMinedTileEvent) {
  const surface = getSurface(event.surface_index)
  if (surface == undefined) {
    const eventName = Converters.eventNameString(event.name)
    log(`[handleMinedTileEvent] undefined surface ${eventName}`)
    return
  }

  const tiles: TileRead[] = Converters.convertRemovedTiles(surface, event.tiles)
  const groupedTiles = groupTiles(tiles)

  for (const [chunkPos, tiles] of groupedTiles) {
    mapTilesUpdateDebounce(
        surface,
        chunkPos,
        tiles,
        event,
        (data => {
              if (isEventType(event, defines.events.on_player_mined_tile)) {
                data.player = event.player_index
                return
              }
              if (isEventType(event, defines.events.on_robot_mined_tile)) {
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


script.on_event(
    defines.events.on_pre_chunk_deleted,
    (e: OnPreChunkDeletedEvent) => {
      log(`on_pre_chunk_deleted ${e.tick}`)

      const surface = getSurface(e.surface_index)
      if (surface == undefined) {
        return
      }

      for (const position of e.positions) {
        const key: CacheKey<KafkatorioPacketData.MapChunkUpdate> = {
          surfaceIndex: surface.index,
          chunkPosition: [position.x, position.y],
        }

        EventUpdatesManager.debounce<KafkatorioPacketData.MapChunkUpdate>(
            key,
            Type.MapChunkUpdate,
            data => data.isDeleted = true,
            0, // deletion is important - emit ASAP
        )
      }
    }
)
