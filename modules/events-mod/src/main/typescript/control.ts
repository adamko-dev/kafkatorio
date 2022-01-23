import {handleConfigurationUpdate} from "./main/config-update/handleConfigurationUpdate";
import {emitPrototypes} from "./main/config-update/prototypes";
import {Queue} from "./main/queue/queue";

require("./main/events/registerHandlers")
require("./main/commands/kafkatorioCommands")

script.on_configuration_changed(handleConfigurationUpdate)

script.on_init(() => {
  Queue.reset()
  emitPrototypes()
})
