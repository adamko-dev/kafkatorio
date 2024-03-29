import KafkatorioSettings from "../settings/KafkatorioSettings";
import {KafkatorioKeyedPacketData} from "../types";
import PacketEmitter from "./PacketEmitter";


declare const global: {
  /** Map the hash of the key, as a string, a cache entry */
  eventUpdatesManagerCache: LuaTable<string, CacheEntry<KafkatorioKeyedPacketData>>,
}


export class EventUpdatesManager {

  constructor() {
    this.init()
  }


  init(force?: boolean) {

    const isAnythingUndefined = global.eventUpdatesManagerCache == undefined

    log(`Initialising EventDataCache (force=${force}, isAnythingUndefined=${isAnythingUndefined})...`)

    if (force == true || isAnythingUndefined) {
      log("Initialising global.cache")
      global.eventUpdatesManagerCache = new LuaTable<string, CacheEntry<KafkatorioKeyedPacketData>>()
    }
    log(`Finished initialising EventDataCache`)
  }


  /**
   * Update the cache data without restarting the 'time-to-emit' countdown, so that the data won't
   * be emitted more than once per {@link CacheEntry.expirationDurationTicks}.
   */
  public throttle<PACKET extends KafkatorioKeyedPacketData>(
      key: PacketKey<PACKET>,
      type: PacketType<PACKET>,
      mutate: CacheDataMutator<PACKET>,
      expirationDurationTicks?: uint,
  ) {
    this.update(key, type, mutate, false, expirationDurationTicks)
  }


  /**
   * Update the cache data and reset the 'time-to-emit' countdown, so the data won't be emitted
   * until there's an inactivity gap of {@link CacheEntry.expirationDurationTicks}.
   */
  public debounce<PACKET extends KafkatorioKeyedPacketData>(
      key: PacketKey<PACKET>,
      type: PacketType<PACKET>,
      mutate: CacheDataMutator<PACKET>,
      expirationDurationTicks?: uint,
  ) {
    this.update(key, type, mutate, true, expirationDurationTicks)
  }


  private update<PACKET extends KafkatorioKeyedPacketData>(
      key: PacketKey<PACKET>,
      type: PacketType<PACKET>,
      mutate: CacheDataMutator<PACKET>,
      resetLastUpdated: boolean,
      expirationDurationTicks?: uint,
  ) {
    const entry: CacheEntry<PACKET> = this.getCacheEntry(key, type) ??
                                      EventUpdatesManager.createCacheEntry(key, type)
    mutate(entry.packet)
    if (resetLastUpdated) {
      entry.lastUpdatedTick = game.tick
    }
    if (expirationDurationTicks != undefined) {
      entry.expirationDurationTicks = expirationDurationTicks
    }

    if (entry.expirationDurationTicks != undefined && entry.expirationDurationTicks <= 0) {
      this.extractAndEmitExpiredPackets()
    }
  }


  /**
   * Usage example: If an urgent update comes in, then the expiration can be reduced so that
   * it's transmitted sooner.
   */
  public setExpiration<PACKET extends KafkatorioKeyedPacketData>(
      key: PacketKey<PACKET>,
      type: PacketType<PACKET>,
      expirationDurationTicks: uint,
  ) {
    const entry = this.getCacheEntry(key, type)
    if (entry != undefined) {
      entry.expirationDurationTicks = expirationDurationTicks
    }
  }


  /** Removes all expired entries from the cache, and return them. */
  // use in/out v4.7.0 https://github.com/microsoft/TypeScript/pull/48240
  private extractExpired(): Array<KafkatorioKeyedPacketData> {
    let countExpired = 0
    let countTotal = 0
    const expiredData: Array<KafkatorioKeyedPacketData> = []
    for (const [key, entry] of global.eventUpdatesManagerCache) {
      countTotal++
      if (this.isExpired(entry)) {
        countExpired++
        expiredData.push(entry.packet)
        global.eventUpdatesManagerCache.delete(key)
      }
    }

    const hasExpiredItems = countExpired > 0
    const hasAnyItem = countTotal > 0

    if (hasExpiredItems || (game.tick % 30 == 0 && hasAnyItem)) {
      log(`[extractExpired] expired items: ${countExpired}, total: ${countTotal}`)
    }
    return expiredData
  }


  /**
   * Removes and emits all expired packets.
   *
   * @return the number of emitted packets
   */
  public extractAndEmitExpiredPackets() {
    const cachedEvents: Array<KafkatorioKeyedPacketData> = this.extractExpired()

    if (cachedEvents.length > 0) {
      log(`[EventDataCache] emitting ${cachedEvents.length} events on tick ${game.tick}`)
      for (const event of cachedEvents) {
        PacketEmitter.emitKeyedPacket(event)
      }
    }

    return cachedEvents.length
  }


  private getCacheEntry<PACKET extends KafkatorioKeyedPacketData>(
      key: PacketKey<PACKET>,
      type: PacketType<PACKET>,
  ): CacheEntry<PACKET> | undefined {
    const hash = EventUpdatesManager.hashKey(key)
    if (!global.eventUpdatesManagerCache.has(hash)) {
      return undefined
    }
    const value: CacheEntry<any> = global.eventUpdatesManagerCache.get(hash)
    if (this.isEntryInstanceOf(value, type)) {
      return value
    } else {
      // Type mismatch. This shouldn't happen...
      return undefined
    }
  }


  private static createCacheEntry<PACKET extends KafkatorioKeyedPacketData>(
      key: PacketKey<PACKET>,
      type: PacketType<PACKET>,
  ): CacheEntry<PACKET> {
    const data: PACKET = <PACKET>{
      type: type,
      key: key,
    }
    const entry: CacheEntry<PACKET> = new CacheEntry<PACKET>(data)
    const hash = EventUpdatesManager.hashKey(key)
    global.eventUpdatesManagerCache.set(hash, entry)
    return entry
  }


  isExpired<TYPE extends KafkatorioKeyedPacketData>(entry: CacheEntry<TYPE>): boolean {
    const expiryDuration =
        entry.expirationDurationTicks
        ?? KafkatorioSettings.getEventCacheExpirationTicks(entry.packet)

    if (expiryDuration == undefined) {
      return true
    } else {
      return game.tick - entry.lastUpdatedTick > expiryDuration
    }
  }


  isEntryInstanceOf<PACKET extends KafkatorioKeyedPacketData>(
      entry: CacheEntry<any> | undefined,
      type: PacketType<PACKET>,
  ): entry is CacheEntry<PACKET> {
    return this.isDataInstanceOf(entry?.packet, type)
  }


  isDataInstanceOf<PACKET extends KafkatorioKeyedPacketData>(
      packet: PACKET | undefined,
      type: PacketType<PACKET>,
  ): packet is PACKET {
    return packet != undefined && packet.type == type
  }


  private static hashKey<PACKET extends KafkatorioKeyedPacketData>(
      key: PacketKey<PACKET>,
  ): string {
    return game.encode_string(game.table_to_json(key))!!
  }

}


const EventUpdates = new EventUpdatesManager()

export default EventUpdates


export type PacketKey<PACKET extends KafkatorioKeyedPacketData> = PACKET["key"]
export type PacketType<PACKET extends KafkatorioKeyedPacketData> = PACKET["type"]

export type CacheDataMutator<PACKET extends KafkatorioKeyedPacketData> = (packet: PACKET) => void


class CacheEntry<PACKET extends KafkatorioKeyedPacketData> {
  lastUpdatedTick: uint = game.tick
  expirationDurationTicks?: uint

  packet: PACKET

  constructor(
      packet: PACKET,
      expirationDurationTicks?: uint,
  ) {

    this.packet = packet
    this.expirationDurationTicks = expirationDurationTicks
  }
}
