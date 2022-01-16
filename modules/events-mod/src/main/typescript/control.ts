import {handleConfigurationUpdate} from "./main/config-update/handleConfigurationUpdate";

require("./main/events/registerHandlers")
require("./main/commands/kafkatorioCommands")

script.on_configuration_changed(handleConfigurationUpdate)
