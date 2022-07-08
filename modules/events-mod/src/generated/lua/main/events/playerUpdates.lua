local ____exports = {}
local disconnectReasons
local ____converters = require("main.events.converters")
local Converters = ____converters.Converters
local ____kafkatorio_2Dschema = require("generated.kafkatorio-schema")
local KafkatorioPacketData = ____kafkatorio_2Dschema.KafkatorioPacketData
local ____PacketEmitter = require("main.PacketEmitter")
local packetEmitter = ____PacketEmitter.default
local ____PlayerUpdateHandler = require("main.events.handlers.PlayerUpdateHandler")
local PlayerUpdates = ____PlayerUpdateHandler.default
script.on_event(
    defines.events.on_player_joined_game,
    function(e)
        PlayerUpdates:playerUpdateThrottle(
            e,
            function(player, data)
                data.isAdmin = player.admin
                local ____data_2 = data
                local ____player_character_unit_number_0 = player.character
                if ____player_character_unit_number_0 ~= nil then
                    ____player_character_unit_number_0 = ____player_character_unit_number_0.unit_number
                end
                ____data_2.characterUnitNumber = ____player_character_unit_number_0 or nil
                data.chatColour = Converters.mapColour(player.chat_color)
                data.colour = Converters.mapColour(player.color)
                data.forceIndex = player.force.index
                data.name = player.name
                data.isShowOnMap = player.show_on_map
                data.isSpectator = player.spectator
                data.surfaceIndex = player.surface.index
                data.tag = player.tag
                Converters.playerOnlineInfo(player, data)
            end
        )
    end
)
script.on_event(
    defines.events.on_player_changed_position,
    function(e)
        PlayerUpdates:playerUpdateThrottle(
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
        PlayerUpdates:playerUpdateThrottle(
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
        PlayerUpdates:playerUpdateThrottle(
            e,
            function(player, data)
                data.ticksToRespawn = player.ticks_to_respawn or nil
                Converters.playerOnlineInfo(player, data)
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
script.on_event(
    defines.events.on_player_banned,
    function(event)
        PlayerUpdates:handleBannedEvent(event)
    end
)
script.on_event(
    defines.events.on_player_unbanned,
    function(event)
        PlayerUpdates:handleBannedEvent(event)
    end
)
script.on_event(
    defines.events.on_player_kicked,
    function(event)
        log((("on_player_kicked " .. tostring(event.tick)) .. " ") .. tostring(event.name))
        PlayerUpdates:playerUpdateImmediate(
            event,
            function(player, data)
                data.kickedReason = event.reason or nil
                Converters.playerOnlineInfo(player, data)
            end
        )
    end
)
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
        PlayerUpdates:playerUpdateImmediate(
            event,
            function(player, data)
                data.isRemoved = true
                Converters.playerOnlineInfo(player, data)
            end
        )
    end
)
disconnectReasons = {}
for name, disconnectId in pairs(defines.disconnect_reason) do
    disconnectReasons[disconnectId] = name
end
return ____exports
