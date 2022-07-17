local ____lualib = require("lualib_bundle")
local __TS__Class = ____lualib.__TS__Class
local __TS__New = ____lualib.__TS__New
local ____exports = {}
____exports.KafkatorioPacketEmitter = __TS__Class()
local KafkatorioPacketEmitter = ____exports.KafkatorioPacketEmitter
KafkatorioPacketEmitter.name = "KafkatorioPacketEmitter"
function KafkatorioPacketEmitter.prototype.____constructor(self)
end
function KafkatorioPacketEmitter.prototype.emitInstantPacket(self, data)
    ____exports.KafkatorioPacketEmitter:emitPacket({data = data, modVersion = global.MOD_VERSION, tick = game.tick})
end
function KafkatorioPacketEmitter.prototype.emitKeyedPacket(self, data)
    ____exports.KafkatorioPacketEmitter:emitPacket({data = data, modVersion = global.MOD_VERSION, tick = game.tick})
end
function KafkatorioPacketEmitter.emitPacket(self, packet)
    local data = game.table_to_json(packet)
    local encoded = game.encode_string(data)
    print("KafkatorioPacket encoded::: " .. tostring(encoded))
end
local PacketEmitter = __TS__New(____exports.KafkatorioPacketEmitter)
____exports.default = PacketEmitter
return ____exports
