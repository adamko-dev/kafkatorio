import {initGlobal} from "./main/global-init";
import {emitPrototypes} from "./main/events/prototypeUpdates";

require("./main/events/registerHandlers")
require("./main/events/mapChunkUpdates")
require("./main/events/playerUpdates")
require("./main/events/entityUpdates")
require("./main/commands/kafkatorioCommands")


script.on_init(() => {
  // runs only once (when the mod is first added to the save)
  initGlobal(true)
  // KafkatorioSettings.loadSettings()
  emitPrototypes()
})
