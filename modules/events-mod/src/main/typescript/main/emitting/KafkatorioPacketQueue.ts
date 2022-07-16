import {KafkatorioPacketData} from "../../generated/kafkatorio-schema";


/**
 * Simple ordered queue for {@link KafkatorioPacketData}.
 *
 * Implemented so printing multiple packets won't flood the output log, causing messages to be
 * missed or garbled.
 */
export class KafkatorioPacketQueueManager {

  private size: int = 0
  private first: Node<any> | null = null
  private last: Node<any> | null = null

  public get length() {
    return this.size
  }

  public enqueue<T extends KafkatorioPacketData>(packet: T) {
    const last = this.last
    this.last = {
      value: packet
    }
    if (last != null) {
      last.next = this.last
    }
    if (this.first == null) {
      this.first = this.last
    }
    this.size++
  }

  public dequeue<T extends KafkatorioPacketData>(): T | null {
    const first = this.first

    if (first == null) {
      return null
    }

    this.first = first.next ?? null
    this.size--
    return first.value
  }

}

const KafkatorioPacketQueue = new KafkatorioPacketQueueManager()

export default KafkatorioPacketQueue

interface Node<T extends KafkatorioPacketData> {
  value: T
  next?: Node<T>
}
