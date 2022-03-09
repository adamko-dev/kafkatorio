import {Queue} from "./queue/queue";
import EventUpdatesManager from "./cache/EventDataCache";


export function initGlobal(force: boolean = false) {
  log(`Initialising Kafkatorio Global variables (force=${force})...`)

  global.MOD_VERSION = script.active_mods[script.mod_name]
  global.FACTORIO_VERSION = script.active_mods["base"]

  Queue.init(true)
  EventUpdatesManager.init(true)

  log("Finished initialising Kafkatorio Global variables")
}
