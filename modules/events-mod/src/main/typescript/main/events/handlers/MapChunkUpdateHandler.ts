import {Converters} from "../converters";
import {isEventType} from "../eventTypeCheck";
import {
  KafkatorioPacketData,
  MapChunkPosition,
  PrototypeKey
} from "../../../generated/kafkatorio-schema";
import EventUpdates, {PacketKey} from "../../emitting/EventDataCache";
import {MapChunkUpdateEvent, MapTileChangeEvent} from "../mapChunkUpdates";


type MapChunkTileUpdater = (data: KafkatorioPacketData.MapChunkTileUpdate) => void


class MapChunkUpdateHandler {

  handleChunkGeneratedEvent(
      event: OnChunkGeneratedEvent,
      expirationDurationTicks?: uint,
  ) {
    const tiles = event.surface.find_tiles_filtered({area: event.area})

    MapChunkUpdateHandler.mapTilesUpdateDebounce(
        event.surface,
        Converters.convertMapTablePosition(event.position),
        tiles,
        event,
        undefined,
        expirationDurationTicks,
    )
  }


  handleBuiltTileEvent(event: OnPlayerBuiltTileEvent | OnRobotBuiltTileEvent) {

    const surface = game.get_surface(event.surface_index)
    if (surface == undefined) {
      const eventName = Converters.eventNameString(event.name)
      log(`[handleBuiltTileEvent] undefined surface ${eventName}`)
      return
    }

    const tiles: TileRead[] = Converters.convertPlacedTiles(event.tile, event.tiles)
    const groupedTiles = MapChunkUpdateHandler.groupTiles(tiles)

    for (const [chunkPos, tiles] of groupedTiles) {
      MapChunkUpdateHandler.mapTilesUpdateDebounce(
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
                    protoId: Converters.prototypeId(event.robot.type, event.robot.name),
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


  handleMinedTileEvent(event: OnPlayerMinedTileEvent | OnRobotMinedTileEvent) {
    const surface = game.get_surface(event.surface_index)
    if (surface == undefined) {
      const eventName = Converters.eventNameString(event.name)
      log(`[handleMinedTileEvent] undefined surface ${eventName}`)
      return
    }

    const tiles: TileRead[] = Converters.convertRemovedTiles(surface, event.tiles)
    const groupedTiles = MapChunkUpdateHandler.groupTiles(tiles)

    for (const [chunkPos, tiles] of groupedTiles) {

      MapChunkUpdateHandler.mapTilesUpdateDebounce(
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
                    protoId: Converters.prototypeId(event.robot.type, event.robot.name),
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


  handleScriptSetTilesEvent(event: ScriptRaisedSetTilesEvent) {

    const surface = game.get_surface(event.surface_index)
    if (surface == undefined) {
      return
    }

    const groupedTiles = MapChunkUpdateHandler.groupTiles(event.tiles)

    for (const [chunkPos, tiles] of groupedTiles) {
      MapChunkUpdateHandler.mapTilesUpdateDebounce(surface, chunkPos, tiles, event)
    }
  }


  handlePreChunkDeleted(event: OnPreChunkDeletedEvent) {

    const surface = game.get_surface(event.surface_index)
    if (surface == undefined) {
      return
    }

    for (const position of event.positions) {
      const key: PacketKey<KafkatorioPacketData.MapChunkTileUpdate> = {
        surfaceIndex: surface.index,
        chunkPosition: [position.x, position.y],
      }

      EventUpdates.debounce<KafkatorioPacketData.MapChunkTileUpdate>(
          key,
          KafkatorioPacketData.Type.MapChunkTileUpdate,
          data => data.isDeleted = true,
          0, // deletion is important - emit ASAP
      )
    }
  }


  private static mapTilesUpdateDebounce(
      surface: LuaSurface | undefined,
      chunkPosition: MapChunkPosition,
      tiles: TileRead[],
      event: MapChunkUpdateEvent | MapTileChangeEvent,
      updater?: MapChunkTileUpdater,
      expirationDurationTicks?: uint,
  ) {

    if (surface == undefined) {
      return
    }

    const eventName = Converters.eventNameString(event.name)

    const key: KafkatorioPacketData.MapChunkTileUpdate["key"] = {
      surfaceIndex: surface.index,
      chunkPosition: chunkPosition,
    }

    EventUpdates.debounce<KafkatorioPacketData.MapChunkTileUpdate>(
        key,
        KafkatorioPacketData.Type.MapChunkTileUpdate,
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
            const protoId = Converters.prototypeId("tile", tile.name)

            if (data.tileDictionary.protos[protoId] == null) {

            }
            data.tileDictionary.protos[protoId] ??= protosCount++ as PrototypeKey

            const protoKey = data.tileDictionary.protos[protoId]

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


  private static groupTiles(tiles: TileRead[]): Map<MapChunkPosition, TileRead[]> {

    const mapChunkPositionToTiles = new Map<MapChunkPosition, TileRead[]>()
    for (const tile of tiles) {
      const chunkPosition: MapChunkPosition = [
        math.floor((tile.position.x / 32)),
        math.floor((tile.position.y / 32)),
      ]
      if (!mapChunkPositionToTiles.has(chunkPosition)) {
        mapChunkPositionToTiles.set(chunkPosition, [])
      }
      mapChunkPositionToTiles.get(chunkPosition)?.push(tile)
    }
    return mapChunkPositionToTiles
  }


}


const MapChunkUpdate = new MapChunkUpdateHandler()

export default MapChunkUpdate
