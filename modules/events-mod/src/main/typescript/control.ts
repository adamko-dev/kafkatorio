import {handleConfigurationUpdate} from "./main/config-update/handleConfigurationUpdate";

require("events")

script.on_configuration_changed(handleConfigurationUpdate)
