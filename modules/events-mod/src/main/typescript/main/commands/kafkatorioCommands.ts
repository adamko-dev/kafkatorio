import {emitPrototypes} from "../config-update/prototypes";

commands.add_command(
    "kafkatorio",
    "kafkatorio innit bruv",
    (e: CustomCommandData) => {
      if (e.parameter == undefined) {
        // do nothing
      } else  if ("PROTOTYPES" == e.parameter.toUpperCase()) {
        emitPrototypes()
      }
    })
