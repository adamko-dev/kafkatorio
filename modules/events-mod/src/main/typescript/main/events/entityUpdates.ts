import {Converters} from "./converters";
import EventDataCache from "../cache/EventDataCache";
import {
  EventName,
  FactorioEntityData,
  KafkatorioPacketData,
  MapChunkPosition,
  PrototypeId,
  Tick,
} from "../../generated/kafkatorio-schema";


export class EntityUpdatesHandler {


  handleMinedTileEvent(event: OnPlayerMinedTileEvent | OnRobotMinedTileEvent) {

    const surface = game.get_surface(event.surface_index)
    if (surface == null) {
      return
    }

    const tilesByChunk: LuaTable<MapChunkPosition, OldTileAndPosition> = new LuaTable<MapChunkPosition, OldTileAndPosition>()

    for (const tile of event.tiles) {
      const chunkPosition = Converters.tilePositionToChunkPosition(tile.position)
      tilesByChunk.set(chunkPosition, tile)
    }

    for (const [chunkPos, tiles] of pairs(tilesByChunk)) {

      const entities: FactorioEntityData.Resource[] = []

      for (const tile in tiles) {
        const resources: FactorioEntityData.Resource[] = EntityUpdatesHandler.getTileResourceEntities(surface, chunkPos)
        for (const resource of resources) {
          entities[entities.length] = resource
        }
      }

      EntityUpdatesHandler.throttleResourceEntitiesUpdate(
          Converters.eventNameString(event.name),
          event.tick as Tick,
          event.surface_index,
          [chunkPos[0], chunkPos[1]],
          entities,
      )
    }
  }


  handleMinedEntityEvent(
      event: OnPlayerMinedEntityEvent | OnRobotMinedEntityEvent,
  ) {
    EntityUpdatesHandler.throttleEntityUpdate(event, event.entity)
  }


  handleBuiltEntityEvent(
      event: OnBuiltEntityEvent | OnRobotBuiltEntityEvent,
  ) {
    EntityUpdatesHandler.throttleEntityUpdate(event, event.created_entity)
  }


  handleChunkGeneratedEvent(event: OnChunkGeneratedEvent) {
    const entities = EntityUpdatesHandler.getAreaResourceEntities(event.surface, event.area)
    EntityUpdatesHandler.throttleResourceEntitiesUpdate(
        Converters.eventNameString(event.name),
        event.tick as Tick,
        event.surface.index,
        [event.position.x, event.position.y],
        entities,
    )
  }


  private static throttleEntityUpdate(event: EntityUpdateEvent, entity: LuaEntity) {

    if (entity.unit_number == undefined) {
      // ignore for now - I'm not sure how to track entities without a unit_number
      return
    }
    const eventName = Converters.eventNameString(event.name)

    const entityUpdateKey: KafkatorioPacketData.MapChunkEntityUpdate["key"] = {
      protoId: Converters.prototypeIdEntity(entity),
      surfaceIndex: entity.surface.index,
      chunkPosition: Converters.mapPositionToChunkPosition(entity.position)
    }

    const entityUpdate: FactorioEntityData.Standard = {
      type: FactorioEntityData.Type.Standard,
      protoId: Converters.prototypeIdEntity(entity),
      graphicsVariation: entity.graphics_variation,
      health: entity.health,
      isActive: entity.active,
      isRotatable: entity.rotatable,
      lastUser: entity.last_user?.index,
      localisedDescription: null,
      localisedName: null,
      position: [entity.position.x, entity.position.y],
      status: Converters.entityStatus(entity.status),
    }

    const entityKey: string = `${entity.unit_number}`

    EventDataCache.throttle<KafkatorioPacketData.MapChunkEntityUpdate>(
        entityUpdateKey,
        KafkatorioPacketData.Type.MapChunkEntityUpdate,
        data => {

          data.events ??= {}
          data.events[eventName] ??= []
          data.events[eventName].push(event.tick)

          data.distinctEntities ??= {}
          data.distinctEntities[entityKey] = entityUpdate
        }
    )
  }


  private static throttleResourceEntitiesUpdate(
      eventName: EventName,
      eventTick: Tick,
      surfaceIndex: SurfaceIndex,
      chunkPosition: MapChunkPosition,
      entities: FactorioEntityData.Resource[],
  ) {

    // first group by prototype ID
    const entitiesByProtoId: { [protoId: PrototypeId]: FactorioEntityData.Resource[] } = {}

    for (const entity of entities) {
      entitiesByProtoId[entity.protoId] ??= []
      entitiesByProtoId[entity.protoId].push(entity)
    }

    for (const [protoId, resourceEntities] of pairs(entitiesByProtoId)) {

      // create an update for each prototype

      const entityUpdateKey: KafkatorioPacketData.MapChunkEntityUpdate["key"] = {
        protoId: protoId,
        surfaceIndex: surfaceIndex,
        chunkPosition: [chunkPosition[0], chunkPosition[1]],
      }

      EventDataCache.throttle<KafkatorioPacketData.MapChunkEntityUpdate>(
          entityUpdateKey,
          KafkatorioPacketData.Type.MapChunkEntityUpdate,
          data => {

            data.events ??= {}
            data.events[eventName] ??= []
            data.events[eventName].push(eventTick)

            for (const entity of resourceEntities) {
              const [x, y] = entity.position
              const entityKey: string = `${x},${y}`

              data.distinctEntities ??= {}
              data.distinctEntities[entityKey] = entity
            }
          }
      )
    }

  }


  /** Get all resources on a tile-position */
  private static getTileResourceEntities(
      surface: LuaSurface,
      mapPosition: MapPosition,
  ): FactorioEntityData.Resource[] {

    const rawEntities = surface.find_entities_filtered({
          position: mapPosition,
          collision_mask: "resource-layer",
        }
    )

    const resources: FactorioEntityData.Resource[] = []

    for (const rawEntity of rawEntities) {
      const entity = Converters.convertResourceEntity(rawEntity)
      if (entity != null) {
        resources[resources.length] = entity
      }
    }

    return resources
  }


  /** Get all resources within a map-area */
  private static getAreaResourceEntities(
      surface: LuaSurface,
      area: BoundingBoxTable,
  ): FactorioEntityData.Resource[] {

    const rawEntities = surface.find_entities_filtered({
          area: area,
          collision_mask: "resource-layer",
        }
    )

    const resources: FactorioEntityData.Resource[] = []

    for (const rawEntity of rawEntities) {
      const entity = Converters.convertResourceEntity(rawEntity)
      if (entity != null) {
        resources[resources.length] = entity
      }
    }

    return resources
  }
}


const EntityUpdates = new EntityUpdatesHandler()

export default EntityUpdates


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


type EntityUpdateEvent =
    | OnBuiltEntityEvent

    | OnEntityClonedEvent
    | OnEntityDamagedEvent
    | OnEntityDestroyedEvent
    | OnEntityDiedEvent
    | OnEntityLogisticSlotChangedEvent
    | OnEntityRenamedEvent
    | OnEntitySettingsPastedEvent
    | OnEntitySpawnedEvent

    | OnPlayerMinedEntityEvent
    | OnPlayerRepairedEntityEvent
    | OnPlayerRotatedEntityEvent

    | OnRobotMinedEntityEvent
    | OnRobotBuiltEntityEvent

    | OnPostEntityDiedEvent
    | OnPreEntitySettingsPastedEvent

    | OnSelectedEntityChangedEvent
    | OnTriggerCreatedEntityEvent
