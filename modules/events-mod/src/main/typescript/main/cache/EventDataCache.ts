import {ExcludeNullKeys, NonNullableKeys} from "../types";

export namespace EventDataCache {


  declare const global: {
    cache: LuaTable<CacheKey<FactorioEventUpdateType>, CacheEntry<FactorioEventUpdateType>>,
    DEFAULT_CACHE_DURATION_TICKS2: Record<FactorioEventUpdateType | "default", uint>,
  }


  export function reset() {
    log("Resetting EventDataCache")
    global.cache = new LuaTable<CacheKey<FactorioEventUpdateType>, CacheEntry<FactorioEventUpdateType>>()
  }


  export function init() {
    if (typeof global.cache == undefined) {
      log("Initialising EventDataCache")
      reset()

      global.DEFAULT_CACHE_DURATION_TICKS2 = {
        "PLAYER": 30, //  0.5 seconds
        "MAP_CHUNK": 60 * 15,  // ~ 15 seconds
        "ENTITY": 60 * 2, //   ~2 seconds
        "default": 60
      }
    }
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
      expirationDurationTicks?: uint,
  ) {
    const entry = getCacheEntry(key)
    if (entry != undefined) {
      entry.expirationDurationTicks = expirationDurationTicks
    }
  }


  export function extractExpired(): Array<CacheData<FactorioEventUpdateType>> {
    const expiredData: Array<CacheData<FactorioEventUpdateType>> = []
    for (let [key, entry] of global.cache) {
      if (entry.isExpired()) {
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
    if (isType(value, key.updateType)) {
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
    // @ts-ignore // TODO remove ts-ignore for "not assignable to type"
    const data: CacheData<TYPE> = {updateType: key.updateType}
    mutate(data)
    let entry: CacheEntry<TYPE> = new CacheEntry<TYPE>(data)
    global.cache.set(key, entry)
    return entry
  }


  /** Map a {@link FactorioEventUpdateType} to a {@link FactorioEventUpdate} DTO */
  type ConvertUpdateType<TYPE extends FactorioEventUpdateType> =
      TYPE extends "PLAYER" ? PlayerUpdate :
      TYPE extends "MAP_CHUNK" ? MapChunkUpdate :
      TYPE extends "ENTITY" ? EntityUpdate :
      never


  /** Only include the non-null properties, and make `updateType` specific */
  export type CacheKey<TYPE extends FactorioEventUpdateType> =
      CacheTyped<TYPE>
      & Omit<ExcludeNullKeys<ConvertUpdateType<TYPE>>, "updateType">


  /** Exclude {@link CacheKey} fields, and make `updateType` specific */
  export type CacheData<TYPE extends FactorioEventUpdateType> =
      CacheTyped<TYPE>
      & NonNullableKeys<Partial<Omit<ConvertUpdateType<TYPE>, keyof CacheKey<TYPE> | "updateType">>>


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
      if (expirationDurationTicks != undefined) {
        this.expirationDurationTicks = expirationDurationTicks
      }
    }

    isExpired(): boolean {
      const cacheDuration =
          this.expirationDurationTicks ?? global.DEFAULT_CACHE_DURATION_TICKS2[this.data.updateType]

      return game.tick - this.lastUpdatedTick > cacheDuration
    }
  }


  function isType<TYPE extends FactorioEventUpdateType>(
      data: CacheEntry<TYPE>,
      updateType: TYPE,
  ): data is CacheEntry<TYPE> {
    return data.data.updateType == updateType
  }

}

// interface FactorioEventUpdate {
//     type: string
// }
//
// interface Update<TYPE extends FactorioEventUpdateTypeType> {
//   type: T
// }
//
// export interface PlayerUpdate extends Update<"PLAYER"> {
//   index: uint
//
//   unhandledEvents?: string[]
//
//   characterUnitNumber?: uint
//   chatColour?: Colour
//   colour?: Colour
//   disconnectReason?: string
//   forceIndex?: uint
//   isAdmin?: boolean
//   isConnected?: boolean
//   isShowOnMap?: boolean
//   isSpectator?: boolean
//   lastOnline?: uint
//   afkTime?: uint
//   name?: string
//   onlineTime?: uint
//   position?: MapEntityPosition
//   surfaceIndex?: uint
//   ticksToRespawn?: uint
//   tag?: string
// }

// export type CacheKey<Type> = MapChunkCacheKey | EntityCacheKey | PlayerCacheKey & {
//   type: Type
// }
//
// export function isCacheType<CK extends CacheKey>(
//     cacheKey: CacheKey,
//     type: CacheType
// ): cacheKey is CK {
//   return cacheKey.type == type
// }
