local ____exports = {}
local playerUpdateThrottle, playerOnlineInfo, handleBannedEvent, Type
local ____converters = require("main.events.converters")
local Converters = ____converters.Converters
local ____EventDataCache = require("main.cache.EventDataCache")
local EventUpdatesManager = ____EventDataCache.default
local ____kafkatorio_2Dschema = require("generated.kafkatorio-schema")
local KafkatorioPacketData = ____kafkatorio_2Dschema.KafkatorioPacketData
local ____PacketEmitter = require("main.PacketEmitter")
local packetEmitter = ____PacketEmitter.default
function playerUpdateThrottle(event, mutate, expirationDurationTicks)
    if expirationDurationTicks == nil then
        expirationDurationTicks = nil
    end
    local playerIndex = event.player_index
    if playerIndex == nil then
        return
    end
    local eventName = Converters.eventNameString(event.name)
    EventUpdatesManager:throttle(
        {index = playerIndex},
        Type.PlayerUpdate,
        function(data)
            local player = game.players[playerIndex]
            if player ~= nil then
                mutate(player, data)
            end
            if data.events == nil then
                data.events = {}
            end
            local ____data_events_0, ____eventName_1 = data.events, eventName
            if ____data_events_0[____eventName_1] == nil then
                ____data_events_0[____eventName_1] = {}
            end
            local ____data_events_eventName_2 = data.events[eventName]
            ____data_events_eventName_2[#____data_events_eventName_2 + 1] = event.tick
        end,
        expirationDurationTicks
    )
end
function playerOnlineInfo(player, data)
    data.lastOnline = player.last_online
    data.onlineTime = player.online_time
    data.afkTime = player.afk_time
    data.isConnected = player.connected
end
function handleBannedEvent(event)
    playerUpdateThrottle(
        event,
        function(player, data)
            data.bannedReason = event.reason or nil
            playerOnlineInfo(player, data)
        end
    )
end
Type = KafkatorioPacketData.Type
local function playerUpdateImmediate(event, mutate)
    playerUpdateThrottle(event, mutate, 0)
end
script.on_event(
    defines.events.on_player_joined_game,
    function(e)
        playerUpdateThrottle(
            e,
            function(player, data)
                data.isAdmin = player.admin
                local ____data_5 = data
                local ____player_character_unit_number_3 = player.character
                if ____player_character_unit_number_3 ~= nil then
                    ____player_character_unit_number_3 = ____player_character_unit_number_3.unit_number
                end
                ____data_5.characterUnitNumber = ____player_character_unit_number_3 or nil
                data.chatColour = Converters.mapColour(player.chat_color)
                data.colour = Converters.mapColour(player.color)
                data.forceIndex = player.force.index
                data.name = player.name
                data.isShowOnMap = player.show_on_map
                data.isSpectator = player.spectator
                data.surfaceIndex = player.surface.index
                data.tag = player.tag
                playerOnlineInfo(player, data)
            end
        )
    end
)
script.on_event(
    defines.events.on_player_changed_position,
    function(e)
        playerUpdateThrottle(
            e,
            function(player, data)
                data.position = {player.position.x, player.position.y}
            end
        )
    end
)
script.on_event(
    defines.events.on_player_changed_surface,
    function(e)
        playerUpdateThrottle(
            e,
            function(player, data)
                data.position = {player.position.x, player.position.y}
                data.surfaceIndex = player.surface.index
            end
        )
    end
)
script.on_event(
    defines.events.on_player_died,
    function(e)
        playerUpdateThrottle(
            e,
            function(player, data)
                data.ticksToRespawn = player.ticks_to_respawn or nil
                playerOnlineInfo(player, data)
                if e.cause ~= nil then
                    data.diedCause = {
                        unitNumber = e.cause.unit_number or nil,
                        protoId = Converters.prototypeId(e.cause.type, e.cause.name)
                    }
                end
            end
        )
    end
)
script.on_event(defines.events.on_player_banned, handleBannedEvent)
script.on_event(defines.events.on_player_unbanned, handleBannedEvent)
script.on_event(
    defines.events.on_player_kicked,
    function(event)
        log((("on_player_kicked " .. tostring(event.tick)) .. " ") .. tostring(event.name))
        playerUpdateImmediate(
            event,
            function(player, data)
                data.kickedReason = event.reason or nil
                playerOnlineInfo(player, data)
            end
        )
    end
)
local disconnectReasons = {}
for name, disconnectId in pairs(defines.disconnect_reason) do
    disconnectReasons[disconnectId] = name
end
script.on_event(
    defines.events.on_pre_player_left_game,
    function(event)
        log((("on_pre_player_left_game " .. tostring(event.tick)) .. " ") .. tostring(event.name))
        local playerUpdateKey = {index = event.player_index}
        local playerUpdate = {type = KafkatorioPacketData.Type.PlayerUpdate, key = playerUpdateKey, disconnectReason = disconnectReasons[event.reason]}
        packetEmitter:emitKeyedPacket(playerUpdate)
    end
)
script.on_event(
    defines.events.on_player_removed,
    function(event)
        log((("on_player_removed " .. tostring(event.tick)) .. " ") .. tostring(event.name))
        playerUpdateImmediate(
            event,
            function(player, data)
                data.isRemoved = true
                playerOnlineInfo(player, data)
            end
        )
    end
)
return ____exports
