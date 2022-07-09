local ____exports = {}
local ____kafkatorio_2Dschema = require("generated.kafkatorio-schema")
local KafkatorioPacketData = ____kafkatorio_2Dschema.KafkatorioPacketData
local ____KafkatorioPacketQueue = require("main.emitting.KafkatorioPacketQueue")
local KafkatorioPacketQueue = ____KafkatorioPacketQueue.default
script.on_event(
    defines.events.on_console_chat,
    function(event)
        local update = {type = KafkatorioPacketData.Type.ConsoleChatUpdate, authorPlayerIndex = event.player_index or nil, content = event.message}
        KafkatorioPacketQueue:enqueue(update)
    end
)
script.on_event(
    defines.events.on_console_command,
    function(event)
        local update = {type = KafkatorioPacketData.Type.ConsoleCommandUpdate, authorPlayerIndex = event.player_index or nil, command = event.command, parameters = event.parameters}
        KafkatorioPacketQueue:enqueue(update)
    end
)
return ____exports
