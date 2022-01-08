import {handleConfigurationUpdate} from "./main/config-update/handleConfigurationUpdate";

require("./main/events/registerHandlers")

script.on_configuration_changed(handleConfigurationUpdate)
