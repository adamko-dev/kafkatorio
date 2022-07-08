local ____exports = {}
local ____EntityUpdateHandler = require("main.events.handlers.EntityUpdateHandler")
local EntityUpdates = ____EntityUpdateHandler.default
local ____MapChunkUpdateHandler = require("main.events.handlers.MapChunkUpdateHandler")
local MapChunkUpdate = ____MapChunkUpdateHandler.default
script.on_event(
    defines.events.on_chunk_generated,
    function(e)
        log("on_chunk_generated " .. tostring(e.tick))
        MapChunkUpdate:handleChunkGeneratedEvent(e)
        EntityUpdates:handleChunkGeneratedEvent(e)
    end
)
script.on_event(
    defines.events.script_raised_set_tiles,
    function(event)
        log("script_raised_set_tiles " .. tostring(event.tick))
        MapChunkUpdate:handleScriptSetTilesEvent(event)
    end
)
script.on_event(
    defines.events.on_player_built_tile,
    function(e)
        log("on_player_built_tile " .. tostring(e.tick))
        MapChunkUpdate:handleBuiltTileEvent(e)
    end
)
script.on_event(
    defines.events.on_robot_built_tile,
    function(e)
        log("on_robot_built_tile " .. tostring(e.tick))
        MapChunkUpdate:handleBuiltTileEvent(e)
    end
)
script.on_event(
    defines.events.on_player_mined_tile,
    function(e)
        log("on_player_mined_tile " .. tostring(e.tick))
        MapChunkUpdate:handleMinedTileEvent(e)
        EntityUpdates:handleMinedTileEvent(e)
    end
)
script.on_event(
    defines.events.on_robot_mined_tile,
    function(e)
        log("on_robot_mined_tile " .. tostring(e.tick))
        MapChunkUpdate:handleMinedTileEvent(e)
    end
)
script.on_event(
    defines.events.on_pre_chunk_deleted,
    function(e)
        log("on_pre_chunk_deleted " .. tostring(e.tick))
        MapChunkUpdate:handlePreChunkDeleted(e)
    end
)
return ____exports
