import EntityUpdates from "./handlers/EntityUpdateHandler";
import MapChunkUpdate from "./handlers/MapChunkUpdateHandler";


export type MapTileChangeEvent =
    | OnPlayerBuiltTileEvent
    | OnPlayerMinedTileEvent
    | OnRobotBuiltTileEvent
    | OnRobotMinedTileEvent
    | ScriptRaisedSetTilesEvent


export type MapChunkUpdateEvent =
    | OnChunkChartedEvent
    | OnChunkDeletedEvent
    | OnChunkGeneratedEvent
    | OnPreChunkDeletedEvent


script.on_event(defines.events.on_chunk_generated, (e: OnChunkGeneratedEvent) => {
  log(`on_chunk_generated ${e.tick}`)
  MapChunkUpdate.handleChunkGeneratedEvent(e)
  EntityUpdates.handleChunkGeneratedEvent(e)
})


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


script.on_event(defines.events.script_raised_set_tiles, (event: ScriptRaisedSetTilesEvent) => {
  log(`script_raised_set_tiles ${event.tick}`)
  MapChunkUpdate.handleScriptSetTilesEvent(event)
})


script.on_event(defines.events.on_player_built_tile, (e: OnPlayerBuiltTileEvent) => {
  log(`on_player_built_tile ${e.tick}`)
  MapChunkUpdate.handleBuiltTileEvent(e)
})


script.on_event(defines.events.on_robot_built_tile, (e: OnRobotBuiltTileEvent) => {
  log(`on_robot_built_tile ${e.tick}`)
  MapChunkUpdate.handleBuiltTileEvent(e)
})


script.on_event(defines.events.on_player_mined_tile, (e: OnPlayerMinedTileEvent) => {
  log(`on_player_mined_tile ${e.tick}`)
  MapChunkUpdate.handleMinedTileEvent(e)
  EntityUpdates.handleMinedTileEvent(e)
})


script.on_event(defines.events.on_robot_mined_tile, (e: OnRobotMinedTileEvent) => {
  log(`on_robot_mined_tile ${e.tick}`)
  MapChunkUpdate.handleMinedTileEvent(e)
})


script.on_event(defines.events.on_pre_chunk_deleted, (e: OnPreChunkDeletedEvent) => {
  log(`on_pre_chunk_deleted ${e.tick}`)
  MapChunkUpdate.handlePreChunkDeleted(e)
})
