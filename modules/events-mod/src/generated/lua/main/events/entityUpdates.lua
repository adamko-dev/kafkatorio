local ____exports = {}
local ____EntityUpdateHandler = require("main.events.handlers.EntityUpdateHandler")
local EntityUpdates = ____EntityUpdateHandler.default
script.on_event(
    defines.events.on_built_entity,
    function(e)
        log("on_built_entity " .. tostring(e.tick))
        EntityUpdates:handleBuiltEntityEvent(e)
    end
)
script.on_event(
    defines.events.on_robot_built_entity,
    function(e)
        log("on_robot_built_entity " .. tostring(e.tick))
        EntityUpdates:handleBuiltEntityEvent(e)
    end
)
script.on_event(
    defines.events.on_player_mined_entity,
    function(e)
        log("on_player_mined_entity " .. tostring(e.tick))
        EntityUpdates:handleMinedEntityEvent(e)
    end
)
script.on_event(
    defines.events.on_robot_mined_entity,
    function(e)
        log("on_robot_mined_entity " .. tostring(e.tick))
        EntityUpdates:handleMinedEntityEvent(e)
    end
)
return ____exports
