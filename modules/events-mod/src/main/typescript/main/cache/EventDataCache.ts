import KafkatorioSettings from "../settings/KafkatorioSettings";

export namespace EventUpdates {


  declare const global: {
    cache: LuaTable<string, CacheEntry<FactorioEventUpdateType>>,
  }


  export class Manager {


    init(force?: boolean) {

      const isAnythingUndefined = global.cache == undefined

      log(`Initialising EventDataCache (force=${force}, isAnythingUndefined=${isAnythingUndefined})...`)

      if (force == true || isAnythingUndefined) {
        log("Initialising global.cache")
        global.cache = new LuaTable<string, CacheEntry<FactorioEventUpdateType>>()
      }
      log(`Finished initialising EventDataCache`)
    }


    /**
     * Update the cache data without restarting the 'time-to-emit' countdown, so that the data won't
     * be emitted more than once per {@link CacheEntry.expirationDurationTicks}.
     */
    public throttle<TYPE extends FactorioEventUpdateType>(
        key: CacheKey<TYPE>,
        mutate: CacheDataMutator<TYPE>,
        expirationDurationTicks?: uint,
    ) {
      this.update(key, mutate, false, expirationDurationTicks)
    }


    /**
     * Update the cache data and reset the 'time-to-emit' countdown, so the data won't be emitted
     * until there's an inactivity gap of {@link CacheEntry.expirationDurationTicks}.
     */
    public debounce<TYPE extends FactorioEventUpdateType>(
        key: CacheKey<TYPE>,
        mutate: CacheDataMutator<TYPE>,
        expirationDurationTicks?: uint,
    ) {
      this.update(key, mutate, true, expirationDurationTicks)
    }


    private update<TYPE extends FactorioEventUpdateType>(
        key: CacheKey<TYPE>,
        mutate: CacheDataMutator<TYPE>,
        resetLastUpdated: boolean,
        expirationDurationTicks?: uint,
    ) {
      const entry: CacheEntry<TYPE> = this.getCacheEntry(key) ?? this.createCacheEntry(key, mutate)
      mutate(entry.data)
      if (resetLastUpdated) {
        entry.lastUpdatedTick = game.tick
      }
      if (expirationDurationTicks != undefined) {
        entry.expirationDurationTicks = expirationDurationTicks
      }
    }


    /**
     * Usage example: If an urgent update comes in, then the expiration can be reduced so that
     * it's transmitted sooner.
     */
    public setExpiration(
        key: CacheKey<FactorioEventUpdateType>,
        expirationDurationTicks: uint,
    ) {
      const entry = this.getCacheEntry(key)
      if (entry != undefined) {
        entry.expirationDurationTicks = expirationDurationTicks
      }
    }


    public extractExpired(): Array<FactorioEventUpdate> {
      let countExpired = 0
      let countTotal = 0
      const expiredData: Array<FactorioEventUpdate> = []
      for (let [key, entry] of global.cache) {
        countTotal++
        if (this.isExpired(entry)) {
          countExpired++
          expiredData.push(entry.data)
          global.cache.delete(key)
        }
      }
      if (countExpired + countTotal > 0) {
        log(`cache expired items count: ${countExpired}, total: ${countTotal}`)
      }
      return expiredData
    }


    private getCacheEntry<TYPE extends FactorioEventUpdateType>(
        key: CacheKey<TYPE>
    ): CacheEntry<TYPE> | undefined {
      const hash = game.encode_string(game.table_to_json(key))!!
      if (!global.cache.has(hash)) {
        return undefined
      }
      const value: CacheEntry<any> = global.cache.get(hash)
      if (this.isEntryInstanceOf(value, key.updateType)) {
        return value
      } else {
        // Type mismatch. This shouldn't happen...
        return undefined
      }
    }


    private createCacheEntry<TYPE extends FactorioEventUpdateType>(
        key: CacheKey<TYPE>,
        mutate: CacheDataMutator<TYPE>,
    ): CacheEntry<TYPE> {
      const data: CacheData<TYPE> = <CacheData<TYPE>>{...key}
      mutate(data)
      let entry: CacheEntry<TYPE> = new CacheEntry<TYPE>(data)
      const hash = game.encode_string(game.table_to_json(key))!!
      global.cache.set(hash, entry)
      return entry
    }


    isExpired<TYPE extends FactorioEventUpdateType>(entry: CacheEntry<TYPE>): boolean {
      const expiryDuration = entry.expirationDurationTicks
                             ??
                             KafkatorioSettings.getEventCacheExpirationTicks(entry.data.updateType)
      return game.tick - entry.lastUpdatedTick > expiryDuration
    }


    isEntryInstanceOf<TYPE extends FactorioEventUpdateType>(
        entry: CacheEntry<any> | undefined,
        updateType: TYPE,
    ): entry is CacheEntry<TYPE> {
      return this.isDataInstanceOf<TYPE>(entry?.data, updateType)
    }


    isDataInstanceOf<TYPE extends FactorioEventUpdateType>(
        data: CacheData<any> | undefined,
        updateType: TYPE,
    ): data is CacheData<TYPE> {
      return data != undefined && data.updateType == updateType
    }


    // function isKeyInstanceOf<TYPE extends FactorioEventUpdateType>(
    //     key: CacheKey<any> | undefined,
    //     updateType: TYPE,
    // ): key is CacheKey<TYPE> {
    //   return key != undefined && key.updateType == updateType
    // }

  }


  /** Map a {@link FactorioEventUpdateType} to a {@link FactorioEventUpdate} DTO */
  type ConvertToUpdate<TYPE extends FactorioEventUpdateType> =
      TYPE extends "PLAYER" ? PlayerUpdate :
      TYPE extends "MAP_CHUNK" ? MapChunkUpdate :
      TYPE extends "ENTITY" ? EntityUpdate :
      never


  /** Map a {@link FactorioEventUpdateType} to a {@link FactorioEventUpdateKey} */
  type ConvertToUpdateKey<TYPE extends FactorioEventUpdateType> =
      TYPE extends "PLAYER" ? PlayerUpdateKey :
      TYPE extends "MAP_CHUNK" ? MapChunkUpdateKey :
      TYPE extends "ENTITY" ? EntityUpdateKey :
      never


  /** Make `updateType` specific */
  export type CacheKey<TYPE extends FactorioEventUpdateType> =
      CacheTyped<TYPE>
      & Omit<ConvertToUpdateKey<TYPE>, "updateType">


  /** Make `updateType` specific */
  export type CacheData<TYPE extends FactorioEventUpdateType> =
      CacheTyped<TYPE>
      & Omit<ConvertToUpdate<TYPE>, "updateType">


  export type CacheTyped<TYPE extends FactorioEventUpdateType> = {
    readonly updateType: TYPE
  }


  export type CacheDataMutator<TYPE extends FactorioEventUpdateType> =
      (data: CacheData<TYPE>) => void


  class CacheEntry<TYPE extends FactorioEventUpdateType> {
    lastUpdatedTick: uint = game.tick
    expirationDurationTicks?: uint

    data: CacheData<TYPE>

    constructor(
        data: CacheData<TYPE>,
        expirationDurationTicks?: uint,
    ) {
      this.data = data
      this.expirationDurationTicks = expirationDurationTicks
    }
  }


}

const EventUpdatesManager = new EventUpdates.Manager()
export default EventUpdatesManager
