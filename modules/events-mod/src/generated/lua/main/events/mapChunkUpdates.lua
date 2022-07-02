local ____lualib = require("lualib_bundle")
local Map = ____lualib.Map
local __TS__Iterator = ____lualib.__TS__Iterator
local __TS__New = ____lualib.__TS__New
local ____exports = {}
local mapTilesUpdateDebounce, getTiles, getSurface, groupTiles, handleBuiltTileEvent, handleMinedTileEvent, Type
local ____eventTypeCheck = require("main.events.eventTypeCheck")
local isEventType = ____eventTypeCheck.isEventType
local ____converters = require("main.events.converters")
local Converters = ____converters.Converters
local ____EventDataCache = require("main.cache.EventDataCache")
local EventUpdatesManager = ____EventDataCache.default
local ____kafkatorio_2Dschema = require("generated.kafkatorio-schema")
local KafkatorioPacketData = ____kafkatorio_2Dschema.KafkatorioPacketData
function mapTilesUpdateDebounce(surface, chunkPosition, tiles, event, updater, expirationDurationTicks)
    if surface == nil then
        return
    end
    local eventName = Converters.eventNameString(event.name)
    local key = {surfaceIndex = surface.index, chunkPosition = chunkPosition}
    EventUpdatesManager:debounce(
        key,
        Type.MapChunkUpdate,
        function(data)
            if data.tileDictionary == nil then
                data.tileDictionary = {tilesXY = {}, protos = {}}
            end
            local protosCount = table_size(data.tileDictionary.protos)
            for ____, tile in ipairs(tiles) do
                local ____data_tileDictionary_protos_1, ____tile_name_2 = data.tileDictionary.protos, tile.name
                if ____data_tileDictionary_protos_1[____tile_name_2] == nil then
                    local ____protosCount_0 = protosCount
                    protosCount = ____protosCount_0 + 1
                    ____data_tileDictionary_protos_1[____tile_name_2] = ____protosCount_0
                end
                local protoKey = data.tileDictionary.protos[tile.name]
                local xString = tostring(tile.position.x)
                local yString = tostring(tile.position.y)
                local ____data_tileDictionary_tilesXY_3, ____xString_4 = data.tileDictionary.tilesXY, xString
                if ____data_tileDictionary_tilesXY_3[____xString_4] == nil then
                    ____data_tileDictionary_tilesXY_3[____xString_4] = {}
                end
                data.tileDictionary.tilesXY[xString][yString] = protoKey
            end
            if data.events == nil then
                data.events = {}
            end
            local ____data_events_5, ____eventName_6 = data.events, eventName
            if ____data_events_5[____eventName_6] == nil then
                ____data_events_5[____eventName_6] = {}
            end
            local ____data_events_eventName_7 = data.events[eventName]
            ____data_events_eventName_7[#____data_events_eventName_7 + 1] = event.tick
            if updater ~= nil then
                updater(data)
            end
        end,
        expirationDurationTicks
    )
end
function getTiles(surface, area)
    local ____surface_find_tiles_filtered_result_8 = surface
    if ____surface_find_tiles_filtered_result_8 ~= nil then
        ____surface_find_tiles_filtered_result_8 = ____surface_find_tiles_filtered_result_8.find_tiles_filtered({area = area})
    end
    return ____surface_find_tiles_filtered_result_8 or ({})
end
function getSurface(surfaceIndex)
    return game.surfaces[surfaceIndex]
end
function ____exports.handleChunkGeneratedEvent(event, expirationDurationTicks)
    local tiles = getTiles(event.surface, event.area)
    mapTilesUpdateDebounce(
        event.surface,
        Converters.convertMapTablePosition(event.position),
        tiles,
        event,
        nil,
        expirationDurationTicks
    )
end
function groupTiles(tiles)
    local mapChunkPositionToTiles = __TS__New(Map)
    for ____, tile in ipairs(tiles) do
        local chunkPosition = {
            math.floor(tile.position.x / 32),
            math.floor(tile.position.y / 32)
        }
        if not mapChunkPositionToTiles:has(chunkPosition) then
            mapChunkPositionToTiles:set(chunkPosition, {})
        end
        local ____mapChunkPositionToTiles_get_result_push_result_10 = mapChunkPositionToTiles:get(chunkPosition)
        if ____mapChunkPositionToTiles_get_result_push_result_10 ~= nil then
            local ____temp_11 = mapChunkPositionToTiles:get(chunkPosition)
            ____temp_11[#____temp_11 + 1] = tile
            ____mapChunkPositionToTiles_get_result_push_result_10 = nil
        end
    end
    return mapChunkPositionToTiles
end
function handleBuiltTileEvent(event)
    local surface = getSurface(event.surface_index)
    if surface == nil then
        local eventName = Converters.eventNameString(event.name)
        log("[handleBuiltTileEvent] undefined surface " .. eventName)
        return
    end
    local tiles = Converters.convertPlacedTiles(event.tile, event.tiles)
    local groupedTiles = groupTiles(tiles)
    for ____, ____value in __TS__Iterator(groupedTiles) do
        local chunkPos = ____value[1]
        local tiles = ____value[2]
        mapTilesUpdateDebounce(
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
                    local ____data_15 = data
                    local ____event_robot_unit_number_13 = event.robot
                    if ____event_robot_unit_number_13 ~= nil then
                        ____event_robot_unit_number_13 = ____event_robot_unit_number_13.unit_number
                    end
                    ____data_15.robot = {unitNumber = ____event_robot_unit_number_13 or nil, name = event.robot.name, protoType = event.robot.type}
                    return
                end
                local exhaustiveCheck = event
            end
        )
    end
end
function handleMinedTileEvent(event)
    local surface = getSurface(event.surface_index)
    if surface == nil then
        local eventName = Converters.eventNameString(event.name)
        log("[handleMinedTileEvent] undefined surface " .. eventName)
        return
    end
    local tiles = Converters.convertRemovedTiles(surface, event.tiles)
    local groupedTiles = groupTiles(tiles)
    for ____, ____value in __TS__Iterator(groupedTiles) do
        local chunkPos = ____value[1]
        local tiles = ____value[2]
        mapTilesUpdateDebounce(
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
                    local ____data_18 = data
                    local ____event_robot_unit_number_16 = event.robot
                    if ____event_robot_unit_number_16 ~= nil then
                        ____event_robot_unit_number_16 = ____event_robot_unit_number_16.unit_number
                    end
                    ____data_18.robot = {unitNumber = ____event_robot_unit_number_16 or nil, name = event.robot.name, protoType = event.robot.type}
                    return
                end
                local exhaustiveCheck = event
            end
        )
    end
end
Type = KafkatorioPacketData.Type
script.on_event(
    defines.events.on_chunk_generated,
    function(e)
        log("on_chunk_generated " .. tostring(e.tick))
        ____exports.handleChunkGeneratedEvent(e)
    end
)
script.on_event(
    defines.events.script_raised_set_tiles,
    function(event)
        log("script_raised_set_tiles " .. tostring(event.tick))
        local surface = getSurface(event.surface_index)
        if surface == nil then
            return
        end
        local groupedTiles = groupTiles(event.tiles)
        for ____, ____value in __TS__Iterator(groupedTiles) do
            local chunkPos = ____value[1]
            local tiles = ____value[2]
            mapTilesUpdateDebounce(surface, chunkPos, tiles, event)
        end
    end
)
script.on_event(
    defines.events.on_player_built_tile,
    function(e)
        log("on_player_built_tile " .. tostring(e.tick))
        handleBuiltTileEvent(e)
    end
)
script.on_event(
    defines.events.on_robot_built_tile,
    function(e)
        log("on_robot_built_tile " .. tostring(e.tick))
        handleBuiltTileEvent(e)
    end
)
script.on_event(
    defines.events.on_player_mined_tile,
    function(e)
        log("on_player_mined_tile " .. tostring(e.tick))
        handleMinedTileEvent(e)
    end
)
script.on_event(
    defines.events.on_robot_mined_tile,
    function(e)
        log("on_robot_mined_tile " .. tostring(e.tick))
        handleMinedTileEvent(e)
    end
)
script.on_event(
    defines.events.on_pre_chunk_deleted,
    function(e)
        log("on_pre_chunk_deleted " .. tostring(e.tick))
        local surface = getSurface(e.surface_index)
        if surface == nil then
            return
        end
        for ____, position in ipairs(e.positions) do
            local key = {surfaceIndex = surface.index, chunkPosition = {position.x, position.y}}
            EventUpdatesManager:debounce(
                key,
                Type.MapChunkUpdate,
                function(data)
                    data.isDeleted = true
                    return true
                end,
                0
            )
        end
    end
)
return ____exports
