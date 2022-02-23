export namespace EventDataCache {


  declare const global: {
    cache: LuaTable<CacheKey<FactorioEventUpdateType>, CacheEntry<FactorioEventUpdateType>>,
    defaultCacheDurationTicks: Record<FactorioEventUpdateType | "default", uint>,
  }


  export function init(force?: boolean) {

    const isAnythingUndefined = global.cache == undefined ||
                                global.defaultCacheDurationTicks == undefined

    log(`Initialising EventDataCache (force=${force}, isAnythingUndefined=${isAnythingUndefined})...`)

    if (force == true || isAnythingUndefined) {
      log("Initialising global.cache")
      global.cache = new LuaTable<CacheKey<FactorioEventUpdateType>, CacheEntry<FactorioEventUpdateType>>()

      log("Initialising global.DEFAULT_CACHE_DURATION_TICKS")
      global.defaultCacheDurationTicks = {
        "PLAYER": 60, //              1 second
        "MAP_CHUNK": 60 * 30,  //    30 seconds
        "ENTITY": 60 * 2, //          2 seconds
        "default": 60 * 5, //         5 seconds
      }
    }
    log(`Finished initialising EventDataCache`)
  }


  /**
   * Update the cache data without restarting the 'time-to-emit' countdown, so that the data won't
   * be emitted more than once per {@link CacheEntry.expirationDurationTicks}.
   */
  export function throttle<TYPE extends FactorioEventUpdateType>(
      key: CacheKey<TYPE>,
      mutate: CacheDataMutator<TYPE>,
  ) {
    update(key, mutate, false)
  }


  /**
   * Update the cache data and reset the 'time-to-emit' countdown, so the data won't be emitted
   * until there's an inactivity gap of {@link CacheEntry.expirationDurationTicks}.
   */
  export function debounce<TYPE extends FactorioEventUpdateType>(
      key: CacheKey<TYPE>,
      mutate: CacheDataMutator<TYPE>,
  ) {
    update(key, mutate, true)
  }


  function update<TYPE extends FactorioEventUpdateType>(
      key: CacheKey<TYPE>,
      mutate: CacheDataMutator<TYPE>,
      resetLastUpdated: boolean,
  ) {
    const entry = getCacheEntry(key) ?? createCacheEntry(key, mutate)
    mutate(entry.data)
    if (resetLastUpdated) {
      entry.lastUpdatedTick = game.tick
    }
  }


  /**
   * Usage example: If an urgent update comes in, then the expiration can be reduced so that
   * it's transmitted sooner.
   */
  export function setExpiration(
      key: CacheKey<FactorioEventUpdateType>,
      expirationDurationTicks: uint,
  ) {
    const entry = getCacheEntry(key)
    if (entry != undefined) {
      entry.expirationDurationTicks = expirationDurationTicks
    }
  }


  export function extractExpired(): Array<FactorioEventUpdate> {
    const expiredData: Array<FactorioEventUpdate> = []
    for (let [key, entry] of global.cache) {
      if (isExpired(entry)) {
        expiredData.push(entry.data)
        global.cache.delete(key)
      }
    }
    return expiredData
  }


  function getCacheEntry<TYPE extends FactorioEventUpdateType>(
      key: CacheKey<TYPE>
  ): CacheEntry<TYPE> | undefined {
    if (!global.cache.has(key)) {
      return undefined
    }
    const value: CacheEntry<any> = global.cache.get(key)
    if (isEntryInstanceOf(value, key.updateType)) {
      return value
    } else {
      // Type mismatch. This shouldn't happen...
      return undefined
    }
  }


  function createCacheEntry<TYPE extends FactorioEventUpdateType>(
      key: CacheKey<TYPE>,
      mutate: CacheDataMutator<TYPE>,
  ): CacheEntry<TYPE> {
    const data: CacheData<TYPE> = <CacheData<TYPE>>{updateType: key.updateType}
    mutate(data)
    let entry: CacheEntry<TYPE> = new CacheEntry<TYPE>(data)
    global.cache.set(key, entry)
    return entry
  }


  // /** Map a {@link FactorioEventUpdateType} to a {@link FactorioEventUpdate} DTO */
  type ConvertToUpdate<TYPE extends FactorioEventUpdateType> =
      TYPE extends "PLAYER" ? PlayerUpdate :
      TYPE extends "MAP_CHUNK" ? MapChunkUpdate :
      TYPE extends "ENTITY" ? EntityUpdate :
      never

  type ConvertToUpdateKey<TYPE extends FactorioEventUpdateType> =
      TYPE extends "PLAYER" ? PlayerUpdateKey :
      TYPE extends "MAP_CHUNK" ? MapChunkUpdateKey :
      TYPE extends "ENTITY" ? EntityUpdateKey :
      never
  // type ConvertToUpdateData<TYPE extends FactorioEventUpdateType> =
  //     TYPE extends "PLAYER" ? PlayerUpdateData :
  //     TYPE extends "MAP_CHUNK" ? MapChunkUpdateData :
  //     TYPE extends "ENTITY" ? EntityUpdateData :
  //     never


  /** Make `updateType` specific */
  export type CacheKey<TYPE extends FactorioEventUpdateType> =
      CacheTyped<TYPE>
      & Omit<ConvertToUpdateKey<TYPE>, "updateType">


  /** Make nullable-fields optional, and `updateType` specific */
  export type CacheData<TYPE extends FactorioEventUpdateType> =
      CacheTyped<TYPE>
      & Omit<ConvertToUpdate<TYPE>, "updateType">
  // & NullablePartial<Omit<ConvertToUpdate<TYPE>, "updateType">>


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


  function isExpired<TYPE extends FactorioEventUpdateType>(entry: CacheEntry<TYPE>): boolean {
    const expiryDuration =
        entry.expirationDurationTicks ?? global.defaultCacheDurationTicks[entry.data.updateType]
    return game.tick - entry.lastUpdatedTick > expiryDuration
  }


  function isEntryInstanceOf<TYPE extends FactorioEventUpdateType>(
      data: CacheEntry<any>,
      updateType: TYPE,
  ): data is CacheEntry<TYPE> {
    return isDataInstanceOf<TYPE>(data.data, updateType)
  }


  function isDataInstanceOf<TYPE extends FactorioEventUpdateType>(
      data: CacheData<any>,
      updateType: TYPE,
  ): data is CacheData<TYPE> {
    return data.updateType == updateType
  }

}
