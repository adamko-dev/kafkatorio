script.on_event(
    defines.events.on_runtime_mod_setting_changed,
    function(e)
        print((((((("Settings changed " .. e.setting_type) .. ", ") .. tostring(e.player_index)) .. ", ") .. e.setting) .. ", ") .. tostring(e.mod_name))
    end
)
