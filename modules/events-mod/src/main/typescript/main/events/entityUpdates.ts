import EntityUpdates from "./handlers/EntityUpdateHandler";

script.on_event(defines.events.on_built_entity, (e: OnBuiltEntityEvent) => {
  log(`on_built_entity ${e.tick}`)
  EntityUpdates.handleBuiltEntityEvent(e)
})


script.on_event(defines.events.on_robot_built_entity, (e: OnRobotBuiltEntityEvent) => {
  log(`on_robot_built_entity ${e.tick}`)
  EntityUpdates.handleBuiltEntityEvent(e)
})


script.on_event(defines.events.on_player_mined_entity, (e: OnPlayerMinedEntityEvent) => {
  log(`on_player_mined_entity ${e.tick}`)
  EntityUpdates.handleMinedEntityEvent(e)
})


script.on_event(defines.events.on_robot_mined_entity, (e: OnRobotMinedEntityEvent) => {
  log(`on_robot_mined_entity ${e.tick}`)
  EntityUpdates.handleMinedEntityEvent(e)
})
