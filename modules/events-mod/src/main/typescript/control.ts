import {emitConfigurationUpdate} from "./main/config-update/handleConfigurationUpdate";
import {emitPrototypes} from "./main/config-update/prototypes";
import {initGlobal} from "./main/global-init";

require("./main/events/registerHandlers")
require("./main/events/playerEvents")
require("./main/events/mapEvents")
require("./main/commands/kafkatorioCommands")
require("./main/cache/cacheChecker")

script.on_configuration_changed((data: ConfigurationChangedData) => {
  // runs whenever the version of the base game, or a mod, changes

  initGlobal(true)
  emitConfigurationUpdate(data)
  emitPrototypes()
})


script.on_init(() => {
  // runs only once (when the mod is first added to the save)
  initGlobal(true)
})
