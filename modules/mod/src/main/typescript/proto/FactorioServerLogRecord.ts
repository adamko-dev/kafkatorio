/* eslint-disable */



export interface FactorioServerLogRecord {
  modVersion: string;
  tick: number;
  eventType: string;
  data?: FactorioLuaObject;
}

export interface FactorioLuaObject {
  objectName: string;
  luaControl?: LuaControl | undefined;
}

/** https://lua-api.factorio.com/next/LuaControl.html */
export interface LuaControl {
  isPlayer: boolean;
  surfaceIndex: number;
  position?: Position;
  forceIndex: number;
  player?: LuaPlayer | undefined;
  entity?: LuaEntity | undefined;
}

/** https://lua-api.factorio.com/next/LuaPlayer.html */
export interface LuaPlayer {
  /** unique ID */
  index: number;
  name: string;
  data: number[];
  colour?: Colour;
  isConnected: boolean;
  isAdmin: boolean;
  afkTimeTicks: number;
  onlineTimeTicks: number;
  lastOnlineTicks: number;
}

/** https://lua-api.factorio.com/next/LuaEntity.html */
export interface LuaEntity {
  /** (optional) unique ID */
  unitNumber?: number | undefined;
  name: string;
  type: string;
  /** https://lua-api.factorio.com/next/defines.html#defines.entity_status */
  status: string;
  isActive: boolean;
  isMoving: boolean;
  /** (optional) The player connected to this character or nil if none. */
  playerIndex?: number | undefined;
  /** (optional) The last player that changed any setting on this entity. */
  lastUserIndex?: number | undefined;
  /** (optional) */
  colour?: Colour;
}

export interface LuaSurface {
  /** unique ID */
  index: number;
  name: string;
  daytime: number;
}

export interface LuaForce {
  /** unique ID */
  index: number;
  forceName: string;
  playerIds: number[];
}

export interface Position {
  x: number;
  y: number;
}

export interface Colour {
  red: number;
  green: number;
  blue: number;
  alpha: number;
}


