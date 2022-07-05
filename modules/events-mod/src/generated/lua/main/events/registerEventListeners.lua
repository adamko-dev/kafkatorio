local ____exports = {}
local ____queue = require("main.queue.queue")
local Queue = ____queue.Queue
local ____eventTypeCheck = require("main.events.eventTypeCheck")
local isEventType = ____eventTypeCheck.isEventType
local ____converters = require("main.events.converters")
local Converters = ____converters.Converters
local ____surfaceUpdates = require("main.events.surfaceUpdates")
local handleSurfaceUpdate = ____surfaceUpdates.handleSurfaceUpdate
local ____EventDataCache = require("main.cache.EventDataCache")
local EventCacheService = ____EventDataCache.default
local ____mapChunkUpdates = require("main.events.mapChunkUpdates")
local MapChunkUpdate = ____mapChunkUpdates.default
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
        if event.tick % 6 == 0 then
            local emittedCount = EventCacheService:extractAndEmitExpiredPackets()
            if emittedCount > 0 then
                log(((("[on_tick:" .. tostring(event.tick)) .. "] emitted ") .. tostring(emittedCount)) .. " events")
            end
        end
        if event.tick % 7 == 0 then
            local events = Queue.dequeueValues(1)
            if #events > 0 then
                log((((("[on_tick:" .. tostring(event.tick)) .. "] dequeued ") .. tostring(#events)) .. " events, current size: ") .. tostring(Queue.size()))
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
