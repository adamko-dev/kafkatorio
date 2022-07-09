local ____lualib = require("lualib_bundle")
local __TS__Class = ____lualib.__TS__Class
local __TS__SetDescriptor = ____lualib.__TS__SetDescriptor
local __TS__New = ____lualib.__TS__New
local ____exports = {}
____exports.KafkatorioPacketQueueManager = __TS__Class()
local KafkatorioPacketQueueManager = ____exports.KafkatorioPacketQueueManager
KafkatorioPacketQueueManager.name = "KafkatorioPacketQueueManager"
function KafkatorioPacketQueueManager.prototype.____constructor(self)
    self.size = 0
    self.first = nil
    self.last = nil
end
__TS__SetDescriptor(
    KafkatorioPacketQueueManager.prototype,
    "length",
    {get = function(self)
        return self.size
    end},
    true
)
function KafkatorioPacketQueueManager.prototype.enqueue(self, packet)
    local last = self.last
    self.last = {value = packet}
    if last ~= nil then
        last.next = self.last
    end
    if self.first == nil then
        self.first = self.last
    end
    self.size = self.size + 1
end
function KafkatorioPacketQueueManager.prototype.dequeue(self)
    local first = self.first
    if first == nil then
        return nil
    end
    self.first = first.next or nil
    self.size = self.size - 1
    return first.value
end
local KafkatorioPacketQueue = __TS__New(____exports.KafkatorioPacketQueueManager)
____exports.default = KafkatorioPacketQueue
return ____exports
