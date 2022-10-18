local ____lualib = require("lualib_bundle")
local __TS__Class = ____lualib.__TS__Class
local __TS__StringTrim = ____lualib.__TS__StringTrim
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
    if #__TS__StringTrim(data) <= 0 then
        print("[error] table_to_json returned empty string for packet:" .. tostring(packet))
        return
    end
    local encodedData = game.encode_string(data)
    if encodedData == nil or #__TS__StringTrim(encodedData) <= 0 then
        print("[error] could not encode packet")
        print("KafkatorioPacket:::" .. data)
    else
        print("KafkatorioPacket:::encoded:" .. encodedData)
    end
end
local PacketEmitter = __TS__New(____exports.KafkatorioPacketEmitter)
____exports.default = PacketEmitter
return ____exports
