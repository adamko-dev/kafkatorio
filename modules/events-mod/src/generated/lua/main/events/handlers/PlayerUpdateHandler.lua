local ____lualib = require("lualib_bundle")
local __TS__Class = ____lualib.__TS__Class
local __TS__New = ____lualib.__TS__New
local ____exports = {}
local ____kafkatorio_2Dschema = require("generated.kafkatorio-schema")
local KafkatorioPacketData = ____kafkatorio_2Dschema.KafkatorioPacketData
local ____converters = require("main.events.converters")
local Converters = ____converters.Converters
local ____EventDataCache = require("main.emitting.EventDataCache")
local EventUpdates = ____EventDataCache.default
____exports.PlayerUpdateHandler = __TS__Class()
local PlayerUpdateHandler = ____exports.PlayerUpdateHandler
PlayerUpdateHandler.name = "PlayerUpdateHandler"
function PlayerUpdateHandler.prototype.____constructor(self)
end
function PlayerUpdateHandler.prototype.handleBannedEvent(self, event)
    self:playerUpdateThrottle(
        event,
        function(player, data)
            data.bannedReason = event.reason or nil
            Converters.playerOnlineInfo(player, data)
        end
    )
end
function PlayerUpdateHandler.prototype.playerUpdateThrottle(self, event, mutate, expirationDurationTicks)
    if expirationDurationTicks == nil then
        expirationDurationTicks = nil
    end
    local playerIndex = event.player_index
    if playerIndex == nil then
        return
    end
    local eventName = Converters.eventNameString(event.name)
    EventUpdates:throttle(
        {index = playerIndex},
        KafkatorioPacketData.Type.PlayerUpdate,
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
function PlayerUpdateHandler.prototype.playerUpdateImmediate(self, event, mutate)
    self:playerUpdateThrottle(event, mutate, 0)
end
local PlayerUpdates = __TS__New(____exports.PlayerUpdateHandler)
____exports.default = PlayerUpdates
return ____exports
