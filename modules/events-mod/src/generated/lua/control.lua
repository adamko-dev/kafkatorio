local ____exports = {}
local ____global_2Dinit = require("main.global-init")
local initGlobal = ____global_2Dinit.initGlobal
local ____prototypeUpdates = require("main.events.prototypeUpdates")
local emitPrototypes = ____prototypeUpdates.emitPrototypes
require("main.events.registerEventListeners")
require("main.events.mapChunkUpdates")
require("main.events.playerUpdates")
require("main.events.entityUpdates")
require("main.commands.kafkatorioCommands")
script.on_init(function()
    initGlobal(true)
    emitPrototypes()
end)
return ____exports
