local ____exports = {}
local ____global_2Dinit = require("main.global-init")
local initGlobal = ____global_2Dinit.initGlobal
local ____prototypeUpdates = require("main.events.prototypeUpdates")
local emitPrototypes = ____prototypeUpdates.emitPrototypes
local ____ConfigurationUpdateHandler = require("main.events.handlers.ConfigurationUpdateHandler")
local ConfigurationUpdates = ____ConfigurationUpdateHandler.default
script.on_configuration_changed(function(data)
    initGlobal(true)
    ConfigurationUpdates:emitConfigurationUpdate(data)
    emitPrototypes()
end)
return ____exports
