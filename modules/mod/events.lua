--[[ Generated with https://github.com/TypeScriptToLua/TypeScriptToLua ]]
script.on_event(
    {defines.events.on_pre_build, defines.events.on_player_dropped_item},
    function(e)
        log(
            (("player " .. tostring(e.player_index)) .. " dropped item, tick:") .. tostring(e.tick)
        )
    end
)
