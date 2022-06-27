script.on_event(
    defines.events.on_runtime_mod_setting_changed,
    (e: OnRuntimeModSettingChangedEvent) => {
      print(`Settings changed ${e.setting_type}, ${e.player_index}, ${e.setting}, ${e.mod_name}`)
      // KafkatorioSettings.loadSettings()
    }
)
