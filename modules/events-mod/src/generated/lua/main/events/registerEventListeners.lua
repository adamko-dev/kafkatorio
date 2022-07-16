local ____exports = {}
local ____eventTypeCheck = require("main.events.eventTypeCheck")
local isEventType = ____eventTypeCheck.isEventType
local ____converters = require("main.events.converters")
local Converters = ____converters.Converters
local ____surfaceUpdates = require("main.events.surfaceUpdates")
local handleSurfaceUpdate = ____surfaceUpdates.handleSurfaceUpdate
local ____EventDataCache = require("main.emitting.EventDataCache")
local EventCacheService = ____EventDataCache.default
local ____MapChunkUpdateHandler = require("main.events.handlers.MapChunkUpdateHandler")
local MapChunkUpdate = ____MapChunkUpdateHandler.default
local ____KafkatorioPacketQueue = require("main.emitting.KafkatorioPacketQueue")
local KafkatorioPacketQueue = ____KafkatorioPacketQueue.default
local ____PacketEmitter = require("main.emitting.PacketEmitter")
local PacketEmitter = ____PacketEmitter.default
local ____EventDataQueue = require("main.emitting.EventDataQueue")
local EventDataQueue = ____EventDataQueue.default
script.on_event(
    defines.events.on_tick,
    function(event)
        if event.tick % 1000 == 0 then
            for ____, surface in pairs(game.surfaces) do
                handleSurfaceUpdate(
                    event,
                    Converters.eventNameString(event.name),
                    surface
                )
            end
        end
        local kPacket = KafkatorioPacketQueue:dequeue()
        if kPacket ~= nil then
            PacketEmitter:emitInstantPacket(kPacket)
        end
        if event.tick % 6 == 0 then
            local emittedCount = EventCacheService:extractAndEmitExpiredPackets()
            if emittedCount > 0 then
                log(((("[on_tick:" .. tostring(event.tick)) .. "] emitted ") .. tostring(emittedCount)) .. " events")
            end
        end
        if event.tick % 7 == 0 then
            local events = EventDataQueue:dequeueValues(1)
            if #events > 0 then
                log((((("[on_tick:" .. tostring(event.tick)) .. "] dequeued ") .. tostring(#events)) .. " events, current size: ") .. tostring(EventDataQueue:size()))
                local i = 1
                for ____, event in ipairs(events) do
                    if isEventType(event, defines.events.on_chunk_generated) then
                        local eName = Converters.eventNameString(event.name)
                        log((((("[on_tick:" .. tostring(event.tick)) .. "] dequed event ") .. eName) .. ", delay ") .. tostring(i))
                        MapChunkUpdate:handleChunkGeneratedEvent(event, i)
                        i = i + 1
                    end
                end
            end
        end
    end
)
return ____exports
