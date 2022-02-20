import {ExcludeNullKeys} from "../global";

export namespace EventDataCache {


  declare const global: {
    cache: LuaTable<CacheKey<FactorioEventUpdate>, CacheEntry<FactorioEventUpdate>>,
    DEFAULT_EXPIRATION_DURATION_TICKS: uint,
  }


  export function init() {
    if (typeof global.cache == undefined) {
      global.cache = new LuaTable<CacheKey<FactorioEventUpdate>, CacheEntry<FactorioEventUpdate>>()

      global.DEFAULT_EXPIRATION_DURATION_TICKS = 60 * 60 // ~60 seconds
    }
  }

  /**
   * Update the cache data without restarting the 'time-to-emit' countdown, so that the data won't
   * be emitted more than once per {@link CacheEntry.expirationDurationTicks}.
   */
  export function throttle<T extends FactorioEventUpdate>(
      key: CacheKey<T>,
      mutate: (data: CacheData<T> | undefined) => void,
  ) {
    update(key, mutate, false)
  }

  /**
   * Update the cache data and reset the 'time-to-emit' countdown, so the data won't be emitted
   * until there's an inactivity gap of {@link CacheEntry.expirationDurationTicks}.
   */
  export function debounce<T extends FactorioEventUpdate>(
      key: CacheKey<T>,
      mutate: (data: CacheData<T> | undefined) => void,
  ) {
    update(key, mutate, true)
  }

  function update<T extends FactorioEventUpdate>(
      key: CacheKey<T>,
      mutate: (data: CacheData<T> | undefined) => void,
      resetLastUpdated: boolean,
  ) {
    const entry = getCacheEntry(key)
    mutate(entry?.data)
    if (entry != undefined && resetLastUpdated) {
      entry.lastUpdatedTick = game.tick
    }
  }

  /**
   * Usage example: If an urgent update comes in, then the expiration can be reduced so that
   * it's transmitted sooner.
   */
  export function setExpiration(
      key: CacheKey<any>,
      expirationDurationTicks?: uint,
  ) {
    const entry = getCacheEntry(key)
    if (entry != undefined) {
      entry.expirationDurationTicks = expirationDurationTicks
    }
  }

  function getCacheEntry<T extends FactorioEventUpdate>(
      key: CacheKey<T>
  ): CacheEntry<T> | undefined {
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

  type UpdateType<T extends FactorioEventUpdate> = T["updateType"]

  /** Only include the non-null properties */
  export type CacheKey<T extends FactorioEventUpdate> = ExcludeNullKeys<T> & {
    updateType: UpdateType<T>
  }

  /** Make the generated schema objects generic closed-polymorphic */
  export type CacheData<T extends FactorioEventUpdate> = Omit<T, "type"> & {
    updateType: UpdateType<T>
  }


  class CacheEntry<T extends FactorioEventUpdate> {
    lastUpdatedTick: uint = game.tick
    expirationDurationTicks?: uint

    data: CacheData<T>

    constructor(
        data: CacheData<T>,
        expirationDurationTicks?: uint,
    ) {
      this.data = data
      if (expirationDurationTicks != undefined) {
        this.expirationDurationTicks = expirationDurationTicks
      }
    }

    isExpired(): boolean {
      return game.tick -
             this.lastUpdatedTick >
             (this.expirationDurationTicks ?? global.DEFAULT_EXPIRATION_DURATION_TICKS)
    }
  }

  function isType<T extends FactorioEventUpdate>(
      data: CacheEntry<T>,
      updateType: UpdateType<T>,
  ): data is CacheEntry<T> {
    return data.data.updateType == updateType
  }

}

// interface FactorioEventUpdate {
//     type: string
// }
//
// interface Update<T extends FactorioEventUpdateType> {
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
