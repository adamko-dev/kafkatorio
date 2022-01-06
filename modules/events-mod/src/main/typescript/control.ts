import {emitTiles} from "./prototypes/emitPrototypes";

require("events")

// on startup, push out prototypes
script.on_configuration_changed((e: ConfigurationChangedData) => {
  emitTiles()
})
