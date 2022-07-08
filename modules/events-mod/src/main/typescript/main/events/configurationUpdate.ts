import {initGlobal} from "../global-init";
import {emitPrototypes} from "./prototypeUpdates";
import ConfigurationUpdates from "./handlers/ConfigurationUpdateHandler";


script.on_configuration_changed((data: ConfigurationChangedData) => {
  // runs whenever the version of the base game, or a mod, changes
  initGlobal(true)
  // KafkatorioSettings.loadSettings()
  ConfigurationUpdates.emitConfigurationUpdate(data)
  emitPrototypes()
})
