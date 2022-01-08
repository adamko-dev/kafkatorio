import {handleConfigurationUpdate} from "./main/config-update/handleConfigurationUpdate";

require("./main/events")

script.on_configuration_changed(handleConfigurationUpdate)
