local ____exports = {}
local handleBuiltEntityEvent, handleMinedEntityEvent
local ____converters = require("main.events.converters")
local Converters = ____converters.Converters
local ____EventDataCache = require("main.cache.EventDataCache")
local EventDataCache = ____EventDataCache.default
local ____kafkatorio_2Dschema = require("generated.kafkatorio-schema")
local KafkatorioPacketData = ____kafkatorio_2Dschema.KafkatorioPacketData
function handleBuiltEntityEvent(event)
    local eventName = Converters.eventNameString(event.name)
    if event.created_entity.unit_number == nil then
        return
    end
    local key = {name = event.created_entity.name, protoType = event.created_entity.prototype.name, unitNumber = event.created_entity.unit_number}
    EventDataCache:debounce(
        key,
        KafkatorioPacketData.Type.EntityUpdate,
        function(data)
            if data.events == nil then
                data.events = {}
            end
            local ____data_events_0, ____eventName_1 = data.events, eventName
            if ____data_events_0[____eventName_1] == nil then
                ____data_events_0[____eventName_1] = {}
            end
            local ____data_events_eventName_2 = data.events[eventName]
            ____data_events_eventName_2[#____data_events_eventName_2 + 1] = event.tick
            data.chunkPosition = Converters.convertMapTablePosition(event.created_entity.position)
            data.graphicsVariation = event.created_entity.graphics_variation
            data.health = event.created_entity.health
            data.isActive = event.created_entity.active
            data.isRotatable = event.created_entity.rotatable
            local ____data_5 = data
            local ____event_created_entity_last_user_index_3 = event.created_entity.last_user
            if ____event_created_entity_last_user_index_3 ~= nil then
                ____event_created_entity_last_user_index_3 = ____event_created_entity_last_user_index_3.index
            end
            ____data_5.lastUser = ____event_created_entity_last_user_index_3
            data.prototype = event.created_entity.prototype.name
        end
    )
end
function handleMinedEntityEvent(event)
    local eventName = Converters.eventNameString(event.name)
    if event.entity.unit_number == nil then
        return
    end
    local key = {name = event.entity.name, protoType = event.entity.prototype.name, unitNumber = event.entity.unit_number}
    EventDataCache:debounce(
        key,
        KafkatorioPacketData.Type.EntityUpdate,
        function(data)
            if data.events == nil then
                data.events = {}
            end
            local ____data_events_6, ____eventName_7 = data.events, eventName
            if ____data_events_6[____eventName_7] == nil then
                ____data_events_6[____eventName_7] = {}
            end
            local ____data_events_eventName_8 = data.events[eventName]
            ____data_events_eventName_8[#____data_events_eventName_8 + 1] = event.tick
            data.chunkPosition = Converters.convertMapTablePosition(event.entity.position)
            data.graphicsVariation = event.entity.graphics_variation
            data.health = event.entity.health
            data.isActive = event.entity.active
            data.isRotatable = event.entity.rotatable
            local ____data_11 = data
            local ____event_entity_last_user_index_9 = event.entity.last_user
            if ____event_entity_last_user_index_9 ~= nil then
                ____event_entity_last_user_index_9 = ____event_entity_last_user_index_9.index
            end
            ____data_11.lastUser = ____event_entity_last_user_index_9
            data.prototype = event.entity.prototype.name
        end
    )
end
script.on_event(
    defines.events.on_built_entity,
    function(e)
        log("on_built_entity " .. tostring(e.tick))
        handleBuiltEntityEvent(e)
    end
)
script.on_event(
    defines.events.on_robot_built_entity,
    function(e)
        log("on_robot_built_entity " .. tostring(e.tick))
        handleBuiltEntityEvent(e)
    end
)
script.on_event(
    defines.events.on_player_mined_entity,
    function(e)
        log("on_player_mined_entity " .. tostring(e.tick))
        handleMinedEntityEvent(e)
    end
)
script.on_event(
    defines.events.on_robot_mined_entity,
    function(e)
        log("on_robot_mined_entity " .. tostring(e.tick))
        handleMinedEntityEvent(e)
    end
)
return ____exports
