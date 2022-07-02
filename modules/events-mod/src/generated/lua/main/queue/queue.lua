local ____lualib = require("lualib_bundle")
local __TS__New = ____lualib.__TS__New
local __TS__Class = ____lualib.__TS__Class
local ____exports = {}
____exports.Queue = {}
local Queue = ____exports.Queue
do
    local Node
    function Queue.init(force)
        local isAnythingUndefined = global.store == nil or global.size == nil or global.head == nil or global.tail == nil
        log(((("Initialising EventQueue globals (force=" .. tostring(force)) .. ", isAnythingUndefined=") .. tostring(isAnythingUndefined)) .. ")")
        if force == true or isAnythingUndefined then
            global.store = {}
            global.head = nil
            global.tail = nil
            global.size = 0
        end
        log("Finished initialising EventQueue")
    end
    function Queue.size()
        return global.size
    end
    function Queue.enqueue(key, event, weight)
        if weight == nil then
            weight = 1
        end
        if global.store[key] ~= nil then
            local node = __TS__New(Node, key, weight)
            if global.head == nil then
                global.head = node
            end
            if global.tail ~= nil then
                global.tail.next = node
            end
            global.tail = node
            global.size = global.size + 1
        end
        global.store[key] = event
    end
    function Queue.dequeue()
        if global.head == nil then
            return nil
        else
            local weight = global.head.weight
            local storeKey = global.head.storeKey
            global.head = global.head.next
            global.size = global.size - 1
            local storedPacket = global.store[storeKey]
            global.store[storeKey] = nil
            if storedPacket == nil then
                return nil
            else
                return {value = storedPacket, weight = weight}
            end
        end
    end
    function Queue.dequeueValues(targetWeight)
        if targetWeight == nil then
            targetWeight = 50
        end
        local values = {}
        local weight = 0
        repeat
            do
                local packet = Queue.dequeue()
                if packet == nil then
                    break
                else
                    weight = weight + packet.weight
                    values[#values + 1] = packet.value
                end
            end
        until not (weight <= targetWeight)
        return values
    end
    Node = __TS__Class()
    Node.name = "Node"
    function Node.prototype.____constructor(self, storeKey, weight)
        self.next = nil
        self.storeKey = storeKey
        self.weight = weight
    end
end
return ____exports
