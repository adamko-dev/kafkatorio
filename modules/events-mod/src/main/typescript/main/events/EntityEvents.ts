import {Converters} from "./converters";
import EventDataCache from "../cache/EventDataCache";
import {
  EntityUpdateKey,
  KafkatorioPacketData
} from "../../generated/kafkatorio-schema/kafkatorio-schema";


script.on_event(defines.events.on_built_entity, (e: OnBuiltEntityEvent) => {
  log(`on_built_entity ${e.tick}`)
  handleBuiltEntityEvent(e)
})


script.on_event(defines.events.on_robot_built_entity, (e: OnRobotBuiltEntityEvent) => {
  log(`on_robot_built_entity ${e.tick}`)
  handleBuiltEntityEvent(e)
})


function handleBuiltEntityEvent(event: OnBuiltEntityEvent | OnRobotBuiltEntityEvent) {
  const eventName = Converters.eventNameString(event.name)

  if (event.created_entity.unit_number == undefined) {
    return
  }

  const key: EntityUpdateKey = {
    name: event.created_entity.name,
    protoType: event.created_entity.prototype.name,
    unitNumber: event.created_entity.unit_number
  }

  EventDataCache.debounce<KafkatorioPacketData.EntityUpdate>(
      key,
      KafkatorioPacketData.Type.EntityUpdate,
      data => {

        data.events ??= {}
        data.events[eventName] ??= []
        data.events[eventName].push(event.tick)

        data.chunkPosition = Converters.convertMapTablePosition(event.created_entity.position)
        data.graphicsVariation = event.created_entity.graphics_variation
        data.health = event.created_entity.health
        data.isActive = event.created_entity.active
        data.isRotatable = event.created_entity.rotatable
        data.lastUser = event.created_entity.last_user?.index
        data.prototype = event.created_entity.prototype.name
      }
  )
}

script.on_event(defines.events.on_player_mined_entity, (e: OnPlayerMinedEntityEvent) => {
  log(`on_player_mined_entity ${e.tick}`)
  handleMinedEntityEvent(e)
})


script.on_event(defines.events.on_robot_mined_entity, (e: OnRobotMinedEntityEvent) => {
  log(`on_robot_mined_entity ${e.tick}`)
  handleMinedEntityEvent(e)
})


function handleMinedEntityEvent(event: OnPlayerMinedEntityEvent | OnRobotMinedEntityEvent) {
  const eventName = Converters.eventNameString(event.name)

  if (event.entity.unit_number == undefined) {
    return
  }

  const key: EntityUpdateKey = {
    name: event.entity.name,
    protoType: event.entity.prototype.name,
    unitNumber: event.entity.unit_number
  }

  EventDataCache.debounce<KafkatorioPacketData.EntityUpdate>(
      key,
      KafkatorioPacketData.Type.EntityUpdate,
      data => {

        data.events ??= {}
        data.events[eventName] ??= []
        data.events[eventName].push(event.tick)

        data.chunkPosition = Converters.convertMapTablePosition(event.entity.position)
        data.graphicsVariation = event.entity.graphics_variation
        data.health = event.entity.health
        data.isActive = event.entity.active
        data.isRotatable = event.entity.rotatable
        data.lastUser = event.entity.last_user?.index
        data.prototype = event.entity.prototype.name
      }
  )
}
