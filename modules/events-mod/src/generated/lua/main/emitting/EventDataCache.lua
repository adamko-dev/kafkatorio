local ____lualib = require("lualib_bundle")
local __TS__Class = ____lualib.__TS__Class
local __TS__New = ____lualib.__TS__New
local ____exports = {}
local CacheEntry
local ____KafkatorioSettings = require("main.settings.KafkatorioSettings")
local KafkatorioSettings = ____KafkatorioSettings.default
local ____PacketEmitter = require("main.emitting.PacketEmitter")
local PacketEmitter = ____PacketEmitter.default
____exports.EventUpdatesManager = __TS__Class()
local EventUpdatesManager = ____exports.EventUpdatesManager
EventUpdatesManager.name = "EventUpdatesManager"
function EventUpdatesManager.prototype.____constructor(self)
    self:init()
end
function EventUpdatesManager.prototype.init(self, force)
    local isAnythingUndefined = global.eventUpdatesManagerCache == nil
    log(((("Initialising EventDataCache (force=" .. tostring(force)) .. ", isAnythingUndefined=") .. tostring(isAnythingUndefined)) .. ")...")
    if force == true or isAnythingUndefined then
        log("Initialising global.cache")
        global.eventUpdatesManagerCache = {}
    end
    log("Finished initialising EventDataCache")
end
function EventUpdatesManager.prototype.throttle(self, key, ____type, mutate, expirationDurationTicks)
    self:update(
        key,
        ____type,
        mutate,
        false,
        expirationDurationTicks
    )
end
function EventUpdatesManager.prototype.debounce(self, key, ____type, mutate, expirationDurationTicks)
    self:update(
        key,
        ____type,
        mutate,
        true,
        expirationDurationTicks
    )
end
function EventUpdatesManager.prototype.update(self, key, ____type, mutate, resetLastUpdated, expirationDurationTicks)
    local entry = self:getCacheEntry(key, ____type) or ____exports.EventUpdatesManager:createCacheEntry(key, ____type)
    mutate(entry.packet)
    if resetLastUpdated then
        entry.lastUpdatedTick = game.tick
    end
    if expirationDurationTicks ~= nil then
        entry.expirationDurationTicks = expirationDurationTicks
    end
    if entry.expirationDurationTicks ~= nil and entry.expirationDurationTicks <= 0 then
        self:extractAndEmitExpiredPackets()
    end
end
function EventUpdatesManager.prototype.setExpiration(self, key, ____type, expirationDurationTicks)
    local entry = self:getCacheEntry(key, ____type)
    if entry ~= nil then
        entry.expirationDurationTicks = expirationDurationTicks
    end
end
function EventUpdatesManager.prototype.extractExpired(self)
    local countExpired = 0
    local countTotal = 0
    local expiredData = {}
    for key, entry in pairs(global.eventUpdatesManagerCache) do
        countTotal = countTotal + 1
        if self:isExpired(entry) then
            countExpired = countExpired + 1
            expiredData[#expiredData + 1] = entry.packet
            global.eventUpdatesManagerCache[key] = nil
        end
    end
    local hasExpiredItems = countExpired > 0
    local hasAnyItem = countTotal > 0
    if hasExpiredItems or game.tick % 30 == 0 and hasAnyItem then
        log((("[extractExpired] expired items: " .. tostring(countExpired)) .. ", total: ") .. tostring(countTotal))
    end
    return expiredData
end
function EventUpdatesManager.prototype.extractAndEmitExpiredPackets(self)
    local cachedEvents = self:extractExpired()
    if #cachedEvents > 0 then
        log((("[EventDataCache] emitting " .. tostring(#cachedEvents)) .. " events on tick ") .. tostring(game.tick))
        for ____, event in ipairs(cachedEvents) do
            PacketEmitter:emitKeyedPacket(event)
        end
    end
    return #cachedEvents
end
function EventUpdatesManager.prototype.getCacheEntry(self, key, ____type)
    local hash = ____exports.EventUpdatesManager:hashKey(key)
    if not (global.eventUpdatesManagerCache[hash] ~= nil) then
        return nil
    end
    local value = global.eventUpdatesManagerCache[hash]
    if self:isEntryInstanceOf(value, ____type) then
        return value
    else
        return nil
    end
end
function EventUpdatesManager.createCacheEntry(self, key, ____type)
    local data = {type = ____type, key = key}
    local entry = __TS__New(CacheEntry, data)
    local hash = ____exports.EventUpdatesManager:hashKey(key)
    global.eventUpdatesManagerCache[hash] = entry
    return entry
end
function EventUpdatesManager.prototype.isExpired(self, entry)
    local expiryDuration = entry.expirationDurationTicks or KafkatorioSettings:getEventCacheExpirationTicks(entry.packet)
    if expiryDuration == nil then
        return true
    else
        return game.tick - entry.lastUpdatedTick > expiryDuration
    end
end
function EventUpdatesManager.prototype.isEntryInstanceOf(self, entry, ____type)
    local ____self_isDataInstanceOf_2 = self.isDataInstanceOf
    local ____entry_packet_0 = entry
    if ____entry_packet_0 ~= nil then
        ____entry_packet_0 = ____entry_packet_0.packet
    end
    return ____self_isDataInstanceOf_2(self, ____entry_packet_0, ____type)
end
function EventUpdatesManager.prototype.isDataInstanceOf(self, packet, ____type)
    return packet ~= nil and packet.type == ____type
end
function EventUpdatesManager.hashKey(self, key)
    return game.encode_string(game.table_to_json(key))
end
local EventUpdates = __TS__New(____exports.EventUpdatesManager)
____exports.default = EventUpdates
CacheEntry = __TS__Class()
CacheEntry.name = "CacheEntry"
function CacheEntry.prototype.____constructor(self, packet, expirationDurationTicks)
    self.lastUpdatedTick = game.tick
    self.packet = packet
    self.expirationDurationTicks = expirationDurationTicks
end
return ____exports
