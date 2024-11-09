import {OnObjectDestroyedEvent} from "factorio:runtime";
import ObjectUpdates from "./handlers/ObjectUpdateHandler";

script.on_event(defines.events.on_object_destroyed, (e: OnObjectDestroyedEvent) => {
  log(`on_object_destroyed ${e.tick}`)
  ObjectUpdates.handleObjectDestroyed(e)
})
