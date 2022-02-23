export namespace Queue {

  declare const global: {
    store: EventQueueStore

    head: Node | null
    tail: Node | null

    size: uint
  }


  export function init(force?: boolean) {

    const isAnythingUndefined = global.store == undefined ||
                                global.size == undefined ||
                                global.head == undefined ||
                                global.tail == undefined

    log(`Initialising EventQueue globals (force=${force}, isAnythingUndefined=${isAnythingUndefined})`)

    if (force == true || isAnythingUndefined) {
      global.store = {}
      global.head = null
      global.tail = null
      global.size = 0
    }

    log(`Finished initialising EventQueue`)
  }


  export function size(): uint | undefined {
    return global.size
  }


  export function enqueue(key: string, event: EventData, weight: int = 1) {
    if (key ! in global.store) {
      let node = new Node(key, weight)
      if (global.head == null) {
        global.head = node
      }
      if (global.tail != null) {
        global.tail.next = node
      }
      global.tail = node
      global.size++
    }

    global.store[key] = event
  }


  export function dequeue(): WeightedPacket | null {
    if (global.head == null) {
      return null
    } else {
      let weight = global.head.weight
      let storeKey = global.head.storeKey
      global.head = global.head.next
      global.size--

      let storedPacket = global.store[storeKey]
      global.store[storeKey] = null

      if (storedPacket == null) {
        return null
      } else {
        return {
          value: storedPacket,
          weight: weight,
        }
      }
    }
  }


  export function dequeueValues(targetWeight: uint = 50): EventData[] {
    let values: EventData[] = []
    let weight: uint = 0

    do {
      let packet = dequeue()

      if (packet == null) {
        break;
      } else {
        weight += packet.weight
        values[values.length] = packet.value
      }
    } while (weight <= targetWeight)

    return values
  }


  class Node {
    storeKey: string;
    next: Node | null = null;
    weight: uint

    constructor(storeKey: string, weight: uint) {
      this.storeKey = storeKey;
      this.weight = weight
    }
  }


  interface EventQueueStore {
    [key: string]: EventData | null;
  }


  interface WeightedPacket {
    value: EventData,
    weight: int,
  }

}
