local ____lualib = require("lualib_bundle")
local __TS__StringStartsWith = ____lualib.__TS__StringStartsWith
local __TS__StringSplit = ____lualib.__TS__StringSplit
local __TS__ParseInt = ____lualib.__TS__ParseInt
local ____exports = {}
local ____global_2Dinit = require("main.global-init")
local initGlobal = ____global_2Dinit.initGlobal
local ____prototypeUpdates = require("main.events.prototypeUpdates")
local emitPrototypes = ____prototypeUpdates.emitPrototypes
local ____EventDataQueue = require("main.emitting.EventDataQueue")
local EventDataQueue = ____EventDataQueue.default
commands.add_command(
    "kafkatorio",
    "kafkatorio innit bruv",
    function(e)
        local ____temp_0
        if e.player_index ~= nil then
            ____temp_0 = game.get_player(e.player_index)
        else
            ____temp_0 = nil
        end
        local player = ____temp_0
        if e.parameter == nil then
            if player ~= nil then
                player.print("no parameter")
            end
        else
            local paramUppercase = string.upper(e.parameter)
            if "PROTOTYPES" == paramUppercase then
                emitPrototypes()
            elseif __TS__StringStartsWith(paramUppercase, "CHUNKS") then
                local ____paramUppercase_split_result__1_1 = __TS__StringSplit(paramUppercase, " ")
                if ____paramUppercase_split_result__1_1 ~= nil then
                    ____paramUppercase_split_result__1_1 = ____paramUppercase_split_result__1_1[2]
                end
                local size = ____paramUppercase_split_result__1_1 or nil
                local radius = size ~= nil and __TS__ParseInt(size) or 1
                if e.player_index ~= nil then
                    local player = game.players[e.player_index]
                    local chunkPosition = {
                        x = math.floor(player.position.x / 32),
                        y = math.floor(player.position.y / 32)
                    }
                    local chunkXMin = chunkPosition.x - radius
                    local chunkXMax = chunkPosition.x + radius
                    local chunkYMin = chunkPosition.y - radius
                    local chunkYMax = chunkPosition.y + radius
                    local chunkCount = 0
                    for ____, surface in pairs(game.surfaces) do
                        for chunk in surface.get_chunks() do
                            if chunk.x >= chunkXMin and chunk.x <= chunkXMax and (chunk.y >= chunkYMin and chunk.y <= chunkYMax) then
                                local data = {
                                    name = defines.events.on_chunk_generated,
                                    position = {x = chunk.x, y = chunk.y},
                                    area = chunk.area,
                                    surface = surface,
                                    tick = e.tick
                                }
                                EventDataQueue:enqueue(
                                    (tostring(surface.index) .. tostring(chunk.x)) .. tostring(chunk.y),
                                    data,
                                    50
                                )
                                chunkCount = chunkCount + 1
                            end
                        end
                    end
                    player.print(((((((((("enqueued " .. tostring(chunkCount)) .. " chunks from [x:") .. tostring(chunkXMin)) .. ", y:") .. tostring(chunkYMin)) .. "] to [x:") .. tostring(chunkXMax)) .. ", y:") .. tostring(chunkYMax)) .. "]")
                end
            elseif __TS__StringStartsWith(paramUppercase, "INIT_GLOBAL") then
                initGlobal(__TS__StringStartsWith(paramUppercase, "INIT_GLOBAL FORCE"))
            end
        end
    end
)
return ____exports
