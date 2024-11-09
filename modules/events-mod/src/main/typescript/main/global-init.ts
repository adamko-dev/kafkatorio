import EventUpdates from "./emitting/EventDataCache";
import {EventDataQueueManager} from "./emitting/EventDataQueue";


export function initGlobal(force: boolean = false) {
  log(`Initialising Kafkatorio Global variables (force=${force})...`)

  global.MOD_VERSION = script.active_mods[script.mod_name] ?? "unknown"
  global.FACTORIO_VERSION = script.active_mods["base"] ?? "unknown"

  EventDataQueueManager.init(true)
  EventUpdates.init(true)

  log("Finished initialising Kafkatorio Global variables")
}
