import {OnObjectDestroyedEvent} from "factorio:runtime";


export class ObjectUpdateHandler {
  handleObjectDestroyed(
    event: ObjectUpdateEvent
  ) {
    // TODO handle object events
  }
}

const ObjectUpdates = new ObjectUpdateHandler()
export default ObjectUpdates

type ObjectUpdateEvent =
  | OnObjectDestroyedEvent
