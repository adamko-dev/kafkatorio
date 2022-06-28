local ____lualib = require("lualib_bundle")
local __TS__Class = ____lualib.__TS__Class
local __TS__New = ____lualib.__TS__New
local ____exports = {}
local ____KafkatorioSettings = require("main.settings.KafkatorioSettings")
local KafkatorioSettings = ____KafkatorioSettings.default
local ____PacketEmitter = require("main.PacketEmitter")
local packetEmitter = ____PacketEmitter.default
____exports.EventUpdates = {}
local EventUpdates = ____exports.EventUpdates
do
    local CacheEntry
    EventUpdates.Manager = __TS__Class()
    local Manager = EventUpdates.Manager
    Manager.name = "Manager"
    function Manager.prototype.____constructor(self)
    end
    function Manager.prototype.init(self, force)
        local isAnythingUndefined = global.cache == nil
        log(((("Initialising EventDataCache (force=" .. tostring(force)) .. ", isAnythingUndefined=") .. tostring(isAnythingUndefined)) .. ")...")
        if force == true or isAnythingUndefined then
            log("Initialising global.cache")
            global.cache = {}
        end
        log("Finished initialising EventDataCache")
    end
    function Manager.prototype.throttle(self, key, ____type, mutate, expirationDurationTicks)
        self:update(
            key,
            ____type,
            mutate,
            false,
            expirationDurationTicks
        )
    end
    function Manager.prototype.debounce(self, key, ____type, mutate, expirationDurationTicks)
        self:update(
            key,
            ____type,
            mutate,
            true,
            expirationDurationTicks
        )
    end
    function Manager.prototype.update(self, key, ____type, mutate, resetLastUpdated, expirationDurationTicks)
        local entry = self:getCacheEntry(key, ____type) or EventUpdates.Manager:createCacheEntry(key, ____type)
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
    function Manager.prototype.setExpiration(self, key, ____type, expirationDurationTicks)
        local entry = self:getCacheEntry(key, ____type)
        if entry ~= nil then
            entry.expirationDurationTicks = expirationDurationTicks
        end
    end
    function Manager.prototype.extractExpired(self)
        local countExpired = 0
        local countTotal = 0
        local expiredData = {}
        for key, entry in pairs(global.cache) do
            countTotal = countTotal + 1
            if self:isExpired(entry) then
                countExpired = countExpired + 1
                expiredData[#expiredData + 1] = entry.packet
                global.cache[key] = nil
            end
        end
        local hasExpiredItems = countExpired > 0
        local hasAnyItem = countTotal > 0
        if hasExpiredItems or game.tick % 30 == 0 and hasAnyItem then
            log((("[extractExpired] expired items: " .. tostring(countExpired)) .. ", total: ") .. tostring(countTotal))
        end
        return expiredData
    end
    function Manager.prototype.extractAndEmitExpiredPackets(self)
        local cachedEvents = self:extractExpired()
        if #cachedEvents > 0 then
            log((("[EventDataCache] emitting " .. tostring(#cachedEvents)) .. " events on tick ") .. tostring(game.tick))
            for ____, event in ipairs(cachedEvents) do
                packetEmitter:emitKeyedPacket(event)
            end
        end
        return #cachedEvents
    end
    function Manager.prototype.getCacheEntry(self, key, ____type)
        local hash = EventUpdates.Manager:hashKey(key)
        if not (global.cache[hash] ~= nil) then
            return nil
        end
        local value = global.cache[hash]
        if self:isEntryInstanceOf(value, ____type) then
            return value
        else
            return nil
        end
    end
    function Manager.createCacheEntry(self, key, ____type)
        local data = {type = ____type, key = key}
        local entry = __TS__New(CacheEntry, data)
        local hash = EventUpdates.Manager:hashKey(key)
        global.cache[hash] = entry
        return entry
    end
    function Manager.prototype.isExpired(self, entry)
        local expiryDuration = entry.expirationDurationTicks or KafkatorioSettings:getEventCacheExpirationTicks(entry.packet)
        if expiryDuration == nil then
            return true
        else
            return game.tick - entry.lastUpdatedTick > expiryDuration
        end
    end
    function Manager.prototype.isEntryInstanceOf(self, entry, ____type)
        local ____self_2 = self
        local ____self_isDataInstanceOf_3 = self.isDataInstanceOf
        local ____entry_packet_0 = entry
        if ____entry_packet_0 ~= nil then
            ____entry_packet_0 = ____entry_packet_0.packet
        end
        return ____self_isDataInstanceOf_3(____self_2, ____entry_packet_0, ____type)
    end
    function Manager.prototype.isDataInstanceOf(self, packet, ____type)
        return packet ~= nil and packet.type == ____type
    end
    function Manager.hashKey(self, key)
        return game.encode_string(game.table_to_json(key))
    end
    CacheEntry = __TS__Class()
    CacheEntry.name = "CacheEntry"
    function CacheEntry.prototype.____constructor(self, packet, expirationDurationTicks)
        self.lastUpdatedTick = game.tick
        self.packet = packet
        self.expirationDurationTicks = expirationDurationTicks
    end
end
local EventUpdatesManager = __TS__New(____exports.EventUpdates.Manager)
____exports.default = EventUpdatesManager
return ____exports
