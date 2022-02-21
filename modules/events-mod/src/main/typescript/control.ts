import {emitConfigurationUpdate} from "./main/config-update/handleConfigurationUpdate";
import {emitPrototypes} from "./main/config-update/prototypes";
import {Queue} from "./main/queue/queue";
import {EventDataCache} from "./main/cache/EventDataCache";

require("./main/events/registerHandlers")
require("./main/commands/kafkatorioCommands")
require("./main/cache/cacheChecker")

script.on_configuration_changed((data: ConfigurationChangedData) => {
  // runs whenever the version of the base game, or a mod, changes

  reInitGlobal()
  emitConfigurationUpdate(data)
  emitPrototypes()
})


script.on_init(() => {
  // runs only once (when the mod is first added to the save)
  initGlobal()
})


function initGlobal() {
  log("Initialising Kafkatorio Global variables...")

  Queue.init()
  EventDataCache.init()

  log("Finished initialising Kafkatorio Global variables")
}

function reInitGlobal() {
  log("Re-initialising Kafkatorio Global variables...")

  global.MOD_VERSION = script.active_mods[script.mod_name]
  global.FACTORIO_VERSION = script.active_mods["base"]

  Queue.reset()
  EventDataCache.reset()

  log("Finished re-initialising Kafkatorio Global variables")
}
