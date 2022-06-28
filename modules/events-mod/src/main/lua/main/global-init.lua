local ____exports = {}
local ____queue = require("main.queue.queue")
local Queue = ____queue.Queue
local ____EventDataCache = require("main.cache.EventDataCache")
local EventUpdatesManager = ____EventDataCache.default
function ____exports.initGlobal(force)
    if force == nil then
        force = false
    end
    log(("Initialising Kafkatorio Global variables (force=" .. tostring(force)) .. ")...")
    global.MOD_VERSION = script.active_mods[script.mod_name]
    global.FACTORIO_VERSION = script.active_mods.base
    Queue.init(true)
    EventUpdatesManager:init(true)
    log("Finished initialising Kafkatorio Global variables")
end
return ____exports
