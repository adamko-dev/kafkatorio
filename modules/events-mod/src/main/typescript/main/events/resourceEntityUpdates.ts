// import {isEventType} from "./eventTypeCheck";
// import {Converters} from "./converters";
// import EventUpdatesManager, {EventUpdates} from "../cache/EventDataCache";
// import {
//   KafkatorioPacketData,
//   MapChunkPosition,
//   PrototypeKey,
// } from "../../generated/kafkatorio-schema";
// import CacheKey = EventUpdates.PacketKey;
// import Type = KafkatorioPacketData.Type;
//
//
// export class ResourceEntityUpdatesHandler {
//
//
//   handleChunkGeneratedEvent(
//       event: OnChunkGeneratedEvent,
//       expirationDurationTicks?: uint,
//   ) {
//     const resourceEntities = this.getAreaResourceEntities(event.surface, event.area)
//
//     mapTilesUpdateDebounce(
//         event.surface,
//         Converters.convertMapTablePosition(event.position),
//         tiles,
//         event,
//         undefined,
//         expirationDurationTicks,
//     )
//   }
//
//
//    private resourceEntityUpdatesThrottle(
//       surface: LuaSurface | undefined,
//       chunkPosition: MapChunkPosition,
//       tiles: TileRead[],
//       event: MapTileUpdateEvent,
//       updater?: MapChunkUpdater,
//       expirationDurationTicks?: uint,
//   ) {
//
//     if (surface == undefined) {
//       return
//     }
//
//     const eventName = Converters.eventNameString(event.name)
//
//     const key: KafkatorioPacketData.ResourceEntitiesUpdate["key"] = {
//       surfaceIndex: surface.index as SurfaceIndex,
//       chunkPosition: chunkPosition,
//     }
//
//     EventUpdatesManager.throttle<KafkatorioPacketData.ResourceEntitiesUpdate>(
//         key,
//         Type.MapChunkUpdate,
//         data => {
//
//           // tile dictionary update
//           if (data.tileDictionary == undefined) {
//             data.tileDictionary = {
//               tilesXY: {},
//               protos: {},
//             }
//           }
//
//           let protosCount: uint = table_size(data.tileDictionary.protos)
//           for (const tile of tiles) {
//             const tileName = tile.name as PrototypeIdMapTile
//             const protoKey = protosCount++ as PrototypeKey
//             data.tileDictionary.protos[tileName] ??= protoKey
//
//             const xString = `${tile.position.x}`
//             const yString = `${tile.position.y}`
//
//             data.tileDictionary.tilesXY[xString] ??= {}
//             data.tileDictionary.tilesXY[xString][yString] = protoKey
//           }
//
//           // events count update
//           data.events ??= {}
//           data.events[eventName] ??= []
//           data.events[eventName].push(event.tick)
//
//           // apply mutator
//           if (updater != undefined) {
//             updater(data)
//           }
//         },
//         expirationDurationTicks,
//     )
//   }
//
// }
//
//
//
// const ResourceEntityUpdates = new ResourceEntityUpdatesHandler()
//
// export default ResourceEntityUpdates
