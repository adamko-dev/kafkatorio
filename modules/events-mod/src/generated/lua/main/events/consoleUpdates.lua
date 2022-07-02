local ____exports = {}
local ____PacketEmitter = require("main.PacketEmitter")
local PacketEmitter = ____PacketEmitter.default
local ____kafkatorio_2Dschema = require("generated.kafkatorio-schema")
local KafkatorioPacketData = ____kafkatorio_2Dschema.KafkatorioPacketData
local Type = KafkatorioPacketData.Type
script.on_event(
    defines.events.on_console_chat,
    function(event)
        local update = {type = Type.ConsoleChatUpdate, authorPlayerIndex = event.player_index or nil, content = event.message}
        PacketEmitter:emitInstantPacket(update)
    end
)
script.on_event(
    defines.events.on_console_command,
    function(event)
        local update = {type = Type.ConsoleCommandUpdate, authorPlayerIndex = event.player_index or nil, command = event.command, parameters = event.parameters}
        PacketEmitter:emitInstantPacket(update)
    end
)
return ____exports
