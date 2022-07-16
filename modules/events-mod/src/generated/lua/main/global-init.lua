local ____exports = {}
local ____EventDataCache = require("main.emitting.EventDataCache")
local EventUpdates = ____EventDataCache.default
local ____EventDataQueue = require("main.emitting.EventDataQueue")
local EventDataQueueManager = ____EventDataQueue.EventDataQueueManager
function ____exports.initGlobal(force)
    if force == nil then
        force = false
    end
    log(("Initialising Kafkatorio Global variables (force=" .. tostring(force)) .. ")...")
    global.MOD_VERSION = script.active_mods[script.mod_name]
    global.FACTORIO_VERSION = script.active_mods.base
    EventDataQueueManager:init(true)
    EventUpdates:init(true)
    log("Finished initialising Kafkatorio Global variables")
end
return ____exports
