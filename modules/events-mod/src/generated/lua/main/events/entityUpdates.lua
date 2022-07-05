local ____lualib = require("lualib_bundle")
local __TS__Class = ____lualib.__TS__Class
local __TS__New = ____lualib.__TS__New
local ____exports = {}
local ____converters = require("main.events.converters")
local Converters = ____converters.Converters
local ____EventDataCache = require("main.cache.EventDataCache")
local EventDataCache = ____EventDataCache.default
local ____kafkatorio_2Dschema = require("generated.kafkatorio-schema")
local FactorioEntityData = ____kafkatorio_2Dschema.FactorioEntityData
local KafkatorioPacketData = ____kafkatorio_2Dschema.KafkatorioPacketData
____exports.EntityUpdatesHandler = __TS__Class()
local EntityUpdatesHandler = ____exports.EntityUpdatesHandler
EntityUpdatesHandler.name = "EntityUpdatesHandler"
function EntityUpdatesHandler.prototype.____constructor(self)
end
function EntityUpdatesHandler.prototype.handleMinedTileEvent(self, event)
    local surface = game.get_surface(event.surface_index)
    if surface == nil then
        return
    end
    local tilesByChunk = {}
    for ____, tile in ipairs(event.tiles) do
        local chunkPosition = Converters.tilePositionToChunkPosition(tile.position)
        tilesByChunk[chunkPosition] = tile
    end
    for chunkPos, tiles in pairs(tilesByChunk) do
        local entities = {}
        for tile in pairs(tiles) do
            local resources = ____exports.EntityUpdatesHandler:getTileResourceEntities(surface, chunkPos)
            for ____, resource in ipairs(resources) do
                entities[#entities + 1] = resource
            end
        end
        ____exports.EntityUpdatesHandler:throttleResourceEntitiesUpdate(
            Converters.eventNameString(event.name),
            event.tick,
            event.surface_index,
            {chunkPos[1], chunkPos[2]},
            entities
        )
    end
end
function EntityUpdatesHandler.prototype.handleMinedEntityEvent(self, event)
    ____exports.EntityUpdatesHandler:throttleEntityUpdate(event, event.entity)
end
function EntityUpdatesHandler.prototype.handleBuiltEntityEvent(self, event)
    ____exports.EntityUpdatesHandler:throttleEntityUpdate(event, event.created_entity)
end
function EntityUpdatesHandler.prototype.handleChunkGeneratedEvent(self, event)
    local entities = ____exports.EntityUpdatesHandler:getAreaResourceEntities(event.surface, event.area)
    ____exports.EntityUpdatesHandler:throttleResourceEntitiesUpdate(
        Converters.eventNameString(event.name),
        event.tick,
        event.surface.index,
        {event.position.x, event.position.y},
        entities
    )
end
function EntityUpdatesHandler.throttleEntityUpdate(self, event, entity)
    if entity.unit_number == nil then
        return
    end
    local eventName = Converters.eventNameString(event.name)
    local entityUpdateKey = {
        protoId = Converters.prototypeIdEntity(entity),
        surfaceIndex = entity.surface.index,
        chunkPosition = Converters.mapPositionToChunkPosition(entity.position)
    }
    local ____FactorioEntityData_Type_Standard_2 = FactorioEntityData.Type.Standard
    local ____Converters_prototypeIdEntity_result_3 = Converters.prototypeIdEntity(entity)
    local ____entity_graphics_variation_4 = entity.graphics_variation
    local ____entity_health_5 = entity.health
    local ____entity_active_6 = entity.active
    local ____entity_rotatable_7 = entity.rotatable
    local ____entity_last_user_index_0 = entity.last_user
    if ____entity_last_user_index_0 ~= nil then
        ____entity_last_user_index_0 = ____entity_last_user_index_0.index
    end
    local entityUpdate = {
        type = ____FactorioEntityData_Type_Standard_2,
        protoId = ____Converters_prototypeIdEntity_result_3,
        graphicsVariation = ____entity_graphics_variation_4,
        health = ____entity_health_5,
        isActive = ____entity_active_6,
        isRotatable = ____entity_rotatable_7,
        lastUser = ____entity_last_user_index_0,
        localisedDescription = nil,
        localisedName = nil,
        position = {entity.position.x, entity.position.y},
        status = Converters.entityStatus(entity.status)
    }
    local entityKey = tostring(entity.unit_number)
    EventDataCache:throttle(
        entityUpdateKey,
        KafkatorioPacketData.Type.MapChunkEntityUpdate,
        function(data)
            if data.events == nil then
                data.events = {}
            end
            local ____data_events_8, ____eventName_9 = data.events, eventName
            if ____data_events_8[____eventName_9] == nil then
                ____data_events_8[____eventName_9] = {}
            end
            local ____data_events_eventName_10 = data.events[eventName]
            ____data_events_eventName_10[#____data_events_eventName_10 + 1] = event.tick
            if data.distinctEntities == nil then
                data.distinctEntities = {}
            end
            data.distinctEntities[entityKey] = entityUpdate
        end
    )
end
function EntityUpdatesHandler.throttleResourceEntitiesUpdate(self, eventName, eventTick, surfaceIndex, chunkPosition, entities)
    local entitiesByProtoId = {}
    for ____, entity in ipairs(entities) do
        local ____entitiesByProtoId_11, ____entity_protoId_12 = entitiesByProtoId, entity.protoId
        if ____entitiesByProtoId_11[____entity_protoId_12] == nil then
            ____entitiesByProtoId_11[____entity_protoId_12] = {}
        end
        local ____entitiesByProtoId_entity_protoId_13 = entitiesByProtoId[entity.protoId]
        ____entitiesByProtoId_entity_protoId_13[#____entitiesByProtoId_entity_protoId_13 + 1] = entity
    end
    for protoId, resourceEntities in pairs(entitiesByProtoId) do
        local entityUpdateKey = {protoId = protoId, surfaceIndex = surfaceIndex, chunkPosition = {chunkPosition[1], chunkPosition[2]}}
        EventDataCache:throttle(
            entityUpdateKey,
            KafkatorioPacketData.Type.MapChunkEntityUpdate,
            function(data)
                if data.events == nil then
                    data.events = {}
                end
                local ____data_events_14, ____eventName_15 = data.events, eventName
                if ____data_events_14[____eventName_15] == nil then
                    ____data_events_14[____eventName_15] = {}
                end
                local ____data_events_eventName_16 = data.events[eventName]
                ____data_events_eventName_16[#____data_events_eventName_16 + 1] = eventTick
                for ____, entity in ipairs(resourceEntities) do
                    local x, y = table.unpack(entity.position)
                    local entityKey = (tostring(x) .. ",") .. tostring(y)
                    if data.distinctEntities == nil then
                        data.distinctEntities = {}
                    end
                    data.distinctEntities[entityKey] = entity
                end
            end
        )
    end
end
function EntityUpdatesHandler.getTileResourceEntities(self, surface, mapPosition)
    local rawEntities = surface.find_entities_filtered({position = mapPosition, collision_mask = "resource-layer"})
    local resources = {}
    for ____, rawEntity in ipairs(rawEntities) do
        local entity = Converters.convertResourceEntity(rawEntity)
        if entity ~= nil then
            resources[#resources + 1] = entity
        end
    end
    return resources
end
function EntityUpdatesHandler.getAreaResourceEntities(self, surface, area)
    local rawEntities = surface.find_entities_filtered({area = area, collision_mask = "resource-layer"})
    local resources = {}
    for ____, rawEntity in ipairs(rawEntities) do
        local entity = Converters.convertResourceEntity(rawEntity)
        if entity ~= nil then
            resources[#resources + 1] = entity
        end
    end
    return resources
end
local EntityUpdates = __TS__New(____exports.EntityUpdatesHandler)
____exports.default = EntityUpdates
script.on_event(
    defines.events.on_built_entity,
    function(e)
        log("on_built_entity " .. tostring(e.tick))
        EntityUpdates:handleBuiltEntityEvent(e)
    end
)
script.on_event(
    defines.events.on_robot_built_entity,
    function(e)
        log("on_robot_built_entity " .. tostring(e.tick))
        EntityUpdates:handleBuiltEntityEvent(e)
    end
)
script.on_event(
    defines.events.on_player_mined_entity,
    function(e)
        log("on_player_mined_entity " .. tostring(e.tick))
        EntityUpdates:handleMinedEntityEvent(e)
    end
)
script.on_event(
    defines.events.on_robot_mined_entity,
    function(e)
        log("on_robot_mined_entity " .. tostring(e.tick))
        EntityUpdates:handleMinedEntityEvent(e)
    end
)
return ____exports
