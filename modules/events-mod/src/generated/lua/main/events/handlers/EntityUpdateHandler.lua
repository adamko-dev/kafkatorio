local ____lualib = require("lualib_bundle")
local __TS__Class = ____lualib.__TS__Class
local __TS__New = ____lualib.__TS__New
local ____exports = {}
local ____kafkatorio_2Dschema = require("generated.kafkatorio-schema")
local KafkatorioPacketData = ____kafkatorio_2Dschema.KafkatorioPacketData
local ____converters = require("main.events.converters")
local Converters = ____converters.Converters
local ____EventDataCache = require("main.emitting.EventDataCache")
local EventDataCache = ____EventDataCache.default
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
        if not (tilesByChunk[chunkPosition] ~= nil) then
            tilesByChunk[chunkPosition] = {}
        end
        local tiles = tilesByChunk[chunkPosition]
        tiles[#tiles + 1] = tile
        tilesByChunk[chunkPosition] = tiles
    end
    for chunkPos, tiles in pairs(tilesByChunk) do
        local entities = {}
        for ____, tile in ipairs(tiles) do
            local tileEntities = ____exports.EntityUpdatesHandler:getTileResourceEntities(surface, tile.position)
            for ____, resource in ipairs(tileEntities) do
                entities[#entities + 1] = resource
            end
        end
        ____exports.EntityUpdatesHandler:throttleResourcesUpdate(
            Converters.eventNameString(event.name),
            event.tick,
            event.surface_index,
            chunkPos,
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
    ____exports.EntityUpdatesHandler:throttleResourcesUpdate(
        Converters.eventNameString(event.name),
        event.tick,
        event.surface.index,
        Converters.chunkPosition(event.position),
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
    local ____entity_unit_number_2 = entity.unit_number
    local ____entity_graphics_variation_3 = entity.graphics_variation
    local ____entity_health_4 = entity.health
    local ____entity_active_5 = entity.active
    local ____entity_rotatable_6 = entity.rotatable
    local ____entity_last_user_index_0 = entity.last_user
    if ____entity_last_user_index_0 ~= nil then
        ____entity_last_user_index_0 = ____entity_last_user_index_0.index
    end
    local entityUpdate = {
        unitNumber = ____entity_unit_number_2,
        graphicsVariation = ____entity_graphics_variation_3,
        health = ____entity_health_4,
        isActive = ____entity_active_5,
        isRotatable = ____entity_rotatable_6,
        lastUser = ____entity_last_user_index_0,
        status = Converters.entityStatus(entity.status)
    }
    EventDataCache:throttle(
        entityUpdateKey,
        KafkatorioPacketData.Type.MapChunkEntityUpdate,
        function(data)
            if data.events == nil then
                data.events = {}
            end
            local ____data_events_7, ____eventName_8 = data.events, eventName
            if ____data_events_7[____eventName_8] == nil then
                ____data_events_7[____eventName_8] = {}
            end
            local ____data_events_eventName_9 = data.events[eventName]
            ____data_events_eventName_9[#____data_events_eventName_9 + 1] = event.tick
            if data.entitiesXY == nil then
                data.entitiesXY = {}
            end
            local ____data_entitiesXY_10, ____tostring_result_11 = data.entitiesXY, tostring(entity.position.x)
            if ____data_entitiesXY_10[____tostring_result_11] == nil then
                ____data_entitiesXY_10[____tostring_result_11] = {}
            end
            local ____data_entitiesXY_tostring_result_12, ____tostring_result_13 = data.entitiesXY[tostring(entity.position.x)], tostring(entity.position.y)
            if ____data_entitiesXY_tostring_result_12[____tostring_result_13] == nil then
                ____data_entitiesXY_tostring_result_12[____tostring_result_13] = entityUpdate
            end
        end
    )
end
function EntityUpdatesHandler.throttleResourcesUpdate(self, eventName, eventTick, surfaceIndex, chunkPosition, entities)
    local entitiesByProtoId = {}
    for ____, entity in ipairs(entities) do
        local protoId = Converters.prototypeIdEntity(entity)
        if entitiesByProtoId[protoId] == nil then
            entitiesByProtoId[protoId] = {}
        end
        local ____entitiesByProtoId_protoId_14 = entitiesByProtoId[protoId]
        ____entitiesByProtoId_protoId_14[#____entitiesByProtoId_protoId_14 + 1] = entity
    end
    for protoId, resourceEntities in pairs(entitiesByProtoId) do
        local entityUpdateKey = {protoId = protoId, surfaceIndex = surfaceIndex, chunkPosition = chunkPosition}
        EventDataCache:throttle(
            entityUpdateKey,
            KafkatorioPacketData.Type.MapChunkResourceUpdate,
            function(data)
                if data.events == nil then
                    data.events = {}
                end
                local ____data_events_15, ____eventName_16 = data.events, eventName
                if ____data_events_15[____eventName_16] == nil then
                    ____data_events_15[____eventName_16] = {}
                end
                local ____data_events_eventName_17 = data.events[eventName]
                ____data_events_eventName_17[#____data_events_eventName_17 + 1] = eventTick
                for ____, entity in ipairs(resourceEntities) do
                    if data.amounts == nil then
                        data.amounts = {}
                    end
                    local ____data_amounts_18, ____tostring_result_19 = data.amounts, tostring(entity.position.x)
                    if ____data_amounts_18[____tostring_result_19] == nil then
                        ____data_amounts_18[____tostring_result_19] = {}
                    end
                    data.amounts[tostring(entity.position.x)][tostring(entity.position.y)] = entity.amount
                    if entity.initial_amount ~= nil then
                        if data.initialAmounts == nil then
                            data.initialAmounts = {}
                        end
                        local ____data_initialAmounts_20, ____tostring_result_21 = data.initialAmounts, tostring(entity.position.x)
                        if ____data_initialAmounts_20[____tostring_result_21] == nil then
                            ____data_initialAmounts_20[____tostring_result_21] = {}
                        end
                        data.initialAmounts[tostring(entity.position.x)][tostring(entity.position.y)] = entity.initial_amount
                    end
                end
            end
        )
    end
end
function EntityUpdatesHandler.getTileResourceEntities(self, surface, mapPosition)
    return surface.find_entities_filtered({position = mapPosition, collision_mask = "resource-layer"})
end
function EntityUpdatesHandler.getAreaResourceEntities(self, surface, area)
    return surface.find_entities_filtered({area = area, collision_mask = "resource-layer"})
end
local EntityUpdates = __TS__New(____exports.EntityUpdatesHandler)
____exports.default = EntityUpdates
return ____exports
