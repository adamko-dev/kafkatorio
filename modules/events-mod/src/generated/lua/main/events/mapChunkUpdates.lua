local ____lualib = require("lualib_bundle")
local __TS__Class = ____lualib.__TS__Class
local Map = ____lualib.Map
local __TS__Iterator = ____lualib.__TS__Iterator
local __TS__New = ____lualib.__TS__New
local ____exports = {}
local ____eventTypeCheck = require("main.events.eventTypeCheck")
local isEventType = ____eventTypeCheck.isEventType
local ____converters = require("main.events.converters")
local Converters = ____converters.Converters
local ____EventDataCache = require("main.cache.EventDataCache")
local EventUpdatesManager = ____EventDataCache.default
local ____kafkatorio_2Dschema = require("generated.kafkatorio-schema")
local KafkatorioPacketData = ____kafkatorio_2Dschema.KafkatorioPacketData
local ____entityUpdates = require("main.events.entityUpdates")
local EntityUpdates = ____entityUpdates.default
local Type = KafkatorioPacketData.Type
local MapChunkUpdateHandler = __TS__Class()
MapChunkUpdateHandler.name = "MapChunkUpdateHandler"
function MapChunkUpdateHandler.prototype.____constructor(self)
end
function MapChunkUpdateHandler.prototype.handleChunkGeneratedEvent(self, event, expirationDurationTicks)
    local tiles = event.surface.find_tiles_filtered({area = event.area})
    MapChunkUpdateHandler:mapTilesUpdateDebounce(
        event.surface,
        Converters.convertMapTablePosition(event.position),
        tiles,
        event,
        nil,
        expirationDurationTicks
    )
end
function MapChunkUpdateHandler.prototype.handleBuiltTileEvent(self, event)
    local surface = game.get_surface(event.surface_index)
    if surface == nil then
        local eventName = Converters.eventNameString(event.name)
        log("[handleBuiltTileEvent] undefined surface " .. eventName)
        return
    end
    local tiles = Converters.convertPlacedTiles(event.tile, event.tiles)
    local groupedTiles = MapChunkUpdateHandler:groupTiles(tiles)
    for ____, ____value in __TS__Iterator(groupedTiles) do
        local chunkPos = ____value[1]
        local tiles = ____value[2]
        MapChunkUpdateHandler:mapTilesUpdateDebounce(
            surface,
            chunkPos,
            tiles,
            event,
            function(data)
                if isEventType(event, defines.events.on_player_built_tile) then
                    data.player = event.player_index
                    return
                end
                if isEventType(event, defines.events.on_robot_built_tile) then
                    local ____data_2 = data
                    local ____event_robot_unit_number_0 = event.robot
                    if ____event_robot_unit_number_0 ~= nil then
                        ____event_robot_unit_number_0 = ____event_robot_unit_number_0.unit_number
                    end
                    ____data_2.robot = {
                        unitNumber = ____event_robot_unit_number_0 or nil,
                        protoId = Converters.prototypeId(event.robot.type, event.robot.name)
                    }
                    return
                end
                local exhaustiveCheck = event
            end
        )
    end
end
function MapChunkUpdateHandler.prototype.handleMinedTileEvent(self, event)
    local surface = game.get_surface(event.surface_index)
    if surface == nil then
        local eventName = Converters.eventNameString(event.name)
        log("[handleMinedTileEvent] undefined surface " .. eventName)
        return
    end
    local tiles = Converters.convertRemovedTiles(surface, event.tiles)
    local groupedTiles = MapChunkUpdateHandler:groupTiles(tiles)
    for ____, ____value in __TS__Iterator(groupedTiles) do
        local chunkPos = ____value[1]
        local tiles = ____value[2]
        MapChunkUpdateHandler:mapTilesUpdateDebounce(
            surface,
            chunkPos,
            tiles,
            event,
            function(data)
                if isEventType(event, defines.events.on_player_mined_tile) then
                    data.player = event.player_index
                    return
                end
                if isEventType(event, defines.events.on_robot_mined_tile) then
                    local ____data_5 = data
                    local ____event_robot_unit_number_3 = event.robot
                    if ____event_robot_unit_number_3 ~= nil then
                        ____event_robot_unit_number_3 = ____event_robot_unit_number_3.unit_number
                    end
                    ____data_5.robot = {
                        unitNumber = ____event_robot_unit_number_3 or nil,
                        protoId = Converters.prototypeId(event.robot.type, event.robot.name)
                    }
                    return
                end
                local exhaustiveCheck = event
            end
        )
    end
end
function MapChunkUpdateHandler.prototype.handleScriptSetTilesEvent(self, event)
    local surface = game.get_surface(event.surface_index)
    if surface == nil then
        return
    end
    local groupedTiles = MapChunkUpdateHandler:groupTiles(event.tiles)
    for ____, ____value in __TS__Iterator(groupedTiles) do
        local chunkPos = ____value[1]
        local tiles = ____value[2]
        MapChunkUpdateHandler:mapTilesUpdateDebounce(surface, chunkPos, tiles, event)
    end
end
function MapChunkUpdateHandler.prototype.handlePreChunkDeleted(self, event)
    local surface = game.get_surface(event.surface_index)
    if surface == nil then
        return
    end
    for ____, position in ipairs(event.positions) do
        local key = {surfaceIndex = surface.index, chunkPosition = {position.x, position.y}}
        EventUpdatesManager:debounce(
            key,
            Type.MapChunkTileUpdate,
            function(data)
                data.isDeleted = true
                return true
            end,
            0
        )
    end
end
function MapChunkUpdateHandler.mapTilesUpdateDebounce(self, surface, chunkPosition, tiles, event, updater, expirationDurationTicks)
    if surface == nil then
        return
    end
    local eventName = Converters.eventNameString(event.name)
    local key = {surfaceIndex = surface.index, chunkPosition = chunkPosition}
    EventUpdatesManager:debounce(
        key,
        Type.MapChunkTileUpdate,
        function(data)
            if data.tileDictionary == nil then
                data.tileDictionary = {tilesXY = {}, protos = {}}
            end
            local protosCount = table_size(data.tileDictionary.protos)
            for ____, tile in ipairs(tiles) do
                local protoId = Converters.prototypeId("tile", tile.name)
                if data.tileDictionary.protos[protoId] == nil then
                end
                local ____data_tileDictionary_protos_7, ____protoId_8 = data.tileDictionary.protos, protoId
                if ____data_tileDictionary_protos_7[____protoId_8] == nil then
                    local ____protosCount_6 = protosCount
                    protosCount = ____protosCount_6 + 1
                    ____data_tileDictionary_protos_7[____protoId_8] = ____protosCount_6
                end
                local protoKey = data.tileDictionary.protos[protoId]
                local xString = tostring(tile.position.x)
                local yString = tostring(tile.position.y)
                local ____data_tileDictionary_tilesXY_9, ____xString_10 = data.tileDictionary.tilesXY, xString
                if ____data_tileDictionary_tilesXY_9[____xString_10] == nil then
                    ____data_tileDictionary_tilesXY_9[____xString_10] = {}
                end
                data.tileDictionary.tilesXY[xString][yString] = protoKey
            end
            if data.events == nil then
                data.events = {}
            end
            local ____data_events_11, ____eventName_12 = data.events, eventName
            if ____data_events_11[____eventName_12] == nil then
                ____data_events_11[____eventName_12] = {}
            end
            local ____data_events_eventName_13 = data.events[eventName]
            ____data_events_eventName_13[#____data_events_eventName_13 + 1] = event.tick
            if updater ~= nil then
                updater(data)
            end
        end,
        expirationDurationTicks
    )
end
function MapChunkUpdateHandler.groupTiles(self, tiles)
    local mapChunkPositionToTiles = __TS__New(Map)
    for ____, tile in ipairs(tiles) do
        local chunkPosition = {
            math.floor(tile.position.x / 32),
            math.floor(tile.position.y / 32)
        }
        if not mapChunkPositionToTiles:has(chunkPosition) then
            mapChunkPositionToTiles:set(chunkPosition, {})
        end
        local ____mapChunkPositionToTiles_get_result_push_result_14 = mapChunkPositionToTiles:get(chunkPosition)
        if ____mapChunkPositionToTiles_get_result_push_result_14 ~= nil then
            local ____temp_15 = mapChunkPositionToTiles:get(chunkPosition)
            ____temp_15[#____temp_15 + 1] = tile
            ____mapChunkPositionToTiles_get_result_push_result_14 = nil
        end
    end
    return mapChunkPositionToTiles
end
local MapChunkUpdate = __TS__New(MapChunkUpdateHandler)
____exports.default = MapChunkUpdate
script.on_event(
    defines.events.on_chunk_generated,
    function(e)
        log("on_chunk_generated " .. tostring(e.tick))
        MapChunkUpdate:handleChunkGeneratedEvent(e)
        EntityUpdates:handleChunkGeneratedEvent(e)
    end
)
script.on_event(
    defines.events.script_raised_set_tiles,
    function(event)
        log("script_raised_set_tiles " .. tostring(event.tick))
        MapChunkUpdate:handleScriptSetTilesEvent(event)
    end
)
script.on_event(
    defines.events.on_player_built_tile,
    function(e)
        log("on_player_built_tile " .. tostring(e.tick))
        MapChunkUpdate:handleBuiltTileEvent(e)
    end
)
script.on_event(
    defines.events.on_robot_built_tile,
    function(e)
        log("on_robot_built_tile " .. tostring(e.tick))
        MapChunkUpdate:handleBuiltTileEvent(e)
    end
)
script.on_event(
    defines.events.on_player_mined_tile,
    function(e)
        log("on_player_mined_tile " .. tostring(e.tick))
        MapChunkUpdate:handleMinedTileEvent(e)
        EntityUpdates:handleMinedTileEvent(e)
    end
)
script.on_event(
    defines.events.on_robot_mined_tile,
    function(e)
        log("on_robot_mined_tile " .. tostring(e.tick))
        MapChunkUpdate:handleMinedTileEvent(e)
    end
)
script.on_event(
    defines.events.on_pre_chunk_deleted,
    function(e)
        log("on_pre_chunk_deleted " .. tostring(e.tick))
        MapChunkUpdate:handlePreChunkDeleted(e)
    end
)
return ____exports
