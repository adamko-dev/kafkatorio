local ____exports = {}
local ____PacketEmitter = require("main.PacketEmitter")
local PacketEmitter = ____PacketEmitter.default
local ____kafkatorio_2Dschema = require("generated.kafkatorio-schema")
local KafkatorioPacketData = ____kafkatorio_2Dschema.KafkatorioPacketData
local Type = KafkatorioPacketData.Type
function ____exports.handleSurfaceUpdate(event, eventType, surface)
    local update = {type = Type.SurfaceUpdate, name = surface.name, index = surface.index, daytime = surface.daytime}
    PacketEmitter:emitInstantPacket(update)
end
return ____exports
