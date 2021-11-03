/* eslint-disable */
import Long from "long";
import _m0 from "protobufjs/minimal";

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

const baseFactorioServerLogRecord: object = {
  modVersion: "",
  tick: 0,
  eventType: "",
};

export const FactorioServerLogRecord = {
  encode(
    message: FactorioServerLogRecord,
    writer: _m0.Writer = _m0.Writer.create()
  ): _m0.Writer {
    if (message.modVersion !== "") {
      writer.uint32(10).string(message.modVersion);
    }
    if (message.tick !== 0) {
      writer.uint32(16).uint32(message.tick);
    }
    if (message.eventType !== "") {
      writer.uint32(26).string(message.eventType);
    }
    if (message.data !== undefined) {
      FactorioLuaObject.encode(message.data, writer.uint32(34).fork()).ldelim();
    }
    return writer;
  },

  decode(
    input: _m0.Reader | Uint8Array,
    length?: number
  ): FactorioServerLogRecord {
    const reader = input instanceof _m0.Reader ? input : new _m0.Reader(input);
    let end = length === undefined ? reader.len : reader.pos + length;
    const message = {
      ...baseFactorioServerLogRecord,
    } as FactorioServerLogRecord;
    while (reader.pos < end) {
      const tag = reader.uint32();
      switch (tag >>> 3) {
        case 1:
          message.modVersion = reader.string();
          break;
        case 2:
          message.tick = reader.uint32();
          break;
        case 3:
          message.eventType = reader.string();
          break;
        case 4:
          message.data = FactorioLuaObject.decode(reader, reader.uint32());
          break;
        default:
          reader.skipType(tag & 7);
          break;
      }
    }
    return message;
  },

  fromPartial(
    object: DeepPartial<FactorioServerLogRecord>
  ): FactorioServerLogRecord {
    const message = {
      ...baseFactorioServerLogRecord,
    } as FactorioServerLogRecord;
    if (object.modVersion !== undefined && object.modVersion !== null) {
      message.modVersion = object.modVersion;
    } else {
      message.modVersion = "";
    }
    if (object.tick !== undefined && object.tick !== null) {
      message.tick = object.tick;
    } else {
      message.tick = 0;
    }
    if (object.eventType !== undefined && object.eventType !== null) {
      message.eventType = object.eventType;
    } else {
      message.eventType = "";
    }
    if (object.data !== undefined && object.data !== null) {
      message.data = FactorioLuaObject.fromPartial(object.data);
    } else {
      message.data = undefined;
    }
    return message;
  },
};

const baseFactorioLuaObject: object = { objectName: "" };

export const FactorioLuaObject = {
  encode(
    message: FactorioLuaObject,
    writer: _m0.Writer = _m0.Writer.create()
  ): _m0.Writer {
    if (message.objectName !== "") {
      writer.uint32(10).string(message.objectName);
    }
    if (message.luaControl !== undefined) {
      LuaControl.encode(message.luaControl, writer.uint32(18).fork()).ldelim();
    }
    return writer;
  },

  decode(input: _m0.Reader | Uint8Array, length?: number): FactorioLuaObject {
    const reader = input instanceof _m0.Reader ? input : new _m0.Reader(input);
    let end = length === undefined ? reader.len : reader.pos + length;
    const message = { ...baseFactorioLuaObject } as FactorioLuaObject;
    while (reader.pos < end) {
      const tag = reader.uint32();
      switch (tag >>> 3) {
        case 1:
          message.objectName = reader.string();
          break;
        case 2:
          message.luaControl = LuaControl.decode(reader, reader.uint32());
          break;
        default:
          reader.skipType(tag & 7);
          break;
      }
    }
    return message;
  },

  fromPartial(object: DeepPartial<FactorioLuaObject>): FactorioLuaObject {
    const message = { ...baseFactorioLuaObject } as FactorioLuaObject;
    if (object.objectName !== undefined && object.objectName !== null) {
      message.objectName = object.objectName;
    } else {
      message.objectName = "";
    }
    if (object.luaControl !== undefined && object.luaControl !== null) {
      message.luaControl = LuaControl.fromPartial(object.luaControl);
    } else {
      message.luaControl = undefined;
    }
    return message;
  },
};

const baseLuaControl: object = {
  isPlayer: false,
  surfaceIndex: 0,
  forceIndex: 0,
};

export const LuaControl = {
  encode(
    message: LuaControl,
    writer: _m0.Writer = _m0.Writer.create()
  ): _m0.Writer {
    if (message.isPlayer === true) {
      writer.uint32(16).bool(message.isPlayer);
    }
    if (message.surfaceIndex !== 0) {
      writer.uint32(24).uint32(message.surfaceIndex);
    }
    if (message.position !== undefined) {
      Position.encode(message.position, writer.uint32(34).fork()).ldelim();
    }
    if (message.forceIndex !== 0) {
      writer.uint32(40).uint32(message.forceIndex);
    }
    if (message.player !== undefined) {
      LuaPlayer.encode(message.player, writer.uint32(82).fork()).ldelim();
    }
    if (message.entity !== undefined) {
      LuaEntity.encode(message.entity, writer.uint32(90).fork()).ldelim();
    }
    return writer;
  },

  decode(input: _m0.Reader | Uint8Array, length?: number): LuaControl {
    const reader = input instanceof _m0.Reader ? input : new _m0.Reader(input);
    let end = length === undefined ? reader.len : reader.pos + length;
    const message = { ...baseLuaControl } as LuaControl;
    while (reader.pos < end) {
      const tag = reader.uint32();
      switch (tag >>> 3) {
        case 2:
          message.isPlayer = reader.bool();
          break;
        case 3:
          message.surfaceIndex = reader.uint32();
          break;
        case 4:
          message.position = Position.decode(reader, reader.uint32());
          break;
        case 5:
          message.forceIndex = reader.uint32();
          break;
        case 10:
          message.player = LuaPlayer.decode(reader, reader.uint32());
          break;
        case 11:
          message.entity = LuaEntity.decode(reader, reader.uint32());
          break;
        default:
          reader.skipType(tag & 7);
          break;
      }
    }
    return message;
  },

  fromPartial(object: DeepPartial<LuaControl>): LuaControl {
    const message = { ...baseLuaControl } as LuaControl;
    if (object.isPlayer !== undefined && object.isPlayer !== null) {
      message.isPlayer = object.isPlayer;
    } else {
      message.isPlayer = false;
    }
    if (object.surfaceIndex !== undefined && object.surfaceIndex !== null) {
      message.surfaceIndex = object.surfaceIndex;
    } else {
      message.surfaceIndex = 0;
    }
    if (object.position !== undefined && object.position !== null) {
      message.position = Position.fromPartial(object.position);
    } else {
      message.position = undefined;
    }
    if (object.forceIndex !== undefined && object.forceIndex !== null) {
      message.forceIndex = object.forceIndex;
    } else {
      message.forceIndex = 0;
    }
    if (object.player !== undefined && object.player !== null) {
      message.player = LuaPlayer.fromPartial(object.player);
    } else {
      message.player = undefined;
    }
    if (object.entity !== undefined && object.entity !== null) {
      message.entity = LuaEntity.fromPartial(object.entity);
    } else {
      message.entity = undefined;
    }
    return message;
  },
};

const baseLuaPlayer: object = {
  index: 0,
  name: "",
  data: 0,
  isConnected: false,
  isAdmin: false,
  afkTimeTicks: 0,
  onlineTimeTicks: 0,
  lastOnlineTicks: 0,
};

export const LuaPlayer = {
  encode(
    message: LuaPlayer,
    writer: _m0.Writer = _m0.Writer.create()
  ): _m0.Writer {
    if (message.index !== 0) {
      writer.uint32(8).uint32(message.index);
    }
    if (message.name !== "") {
      writer.uint32(18).string(message.name);
    }
    writer.uint32(34).fork();
    for (const v of message.data) {
      writer.int32(v);
    }
    writer.ldelim();
    if (message.colour !== undefined) {
      Colour.encode(message.colour, writer.uint32(66).fork()).ldelim();
    }
    if (message.isConnected === true) {
      writer.uint32(80).bool(message.isConnected);
    }
    if (message.isAdmin === true) {
      writer.uint32(88).bool(message.isAdmin);
    }
    if (message.afkTimeTicks !== 0) {
      writer.uint32(800).uint32(message.afkTimeTicks);
    }
    if (message.onlineTimeTicks !== 0) {
      writer.uint32(808).uint32(message.onlineTimeTicks);
    }
    if (message.lastOnlineTicks !== 0) {
      writer.uint32(816).uint32(message.lastOnlineTicks);
    }
    return writer;
  },

  decode(input: _m0.Reader | Uint8Array, length?: number): LuaPlayer {
    const reader = input instanceof _m0.Reader ? input : new _m0.Reader(input);
    let end = length === undefined ? reader.len : reader.pos + length;
    const message = { ...baseLuaPlayer } as LuaPlayer;
    message.data = [];
    while (reader.pos < end) {
      const tag = reader.uint32();
      switch (tag >>> 3) {
        case 1:
          message.index = reader.uint32();
          break;
        case 2:
          message.name = reader.string();
          break;
        case 4:
          if ((tag & 7) === 2) {
            const end2 = reader.uint32() + reader.pos;
            while (reader.pos < end2) {
              message.data.push(reader.int32());
            }
          } else {
            message.data.push(reader.int32());
          }
          break;
        case 8:
          message.colour = Colour.decode(reader, reader.uint32());
          break;
        case 10:
          message.isConnected = reader.bool();
          break;
        case 11:
          message.isAdmin = reader.bool();
          break;
        case 100:
          message.afkTimeTicks = reader.uint32();
          break;
        case 101:
          message.onlineTimeTicks = reader.uint32();
          break;
        case 102:
          message.lastOnlineTicks = reader.uint32();
          break;
        default:
          reader.skipType(tag & 7);
          break;
      }
    }
    return message;
  },

  fromPartial(object: DeepPartial<LuaPlayer>): LuaPlayer {
    const message = { ...baseLuaPlayer } as LuaPlayer;
    message.data = [];
    if (object.index !== undefined && object.index !== null) {
      message.index = object.index;
    } else {
      message.index = 0;
    }
    if (object.name !== undefined && object.name !== null) {
      message.name = object.name;
    } else {
      message.name = "";
    }
    if (object.data !== undefined && object.data !== null) {
      for (const e of object.data) {
        message.data.push(e);
      }
    }
    if (object.colour !== undefined && object.colour !== null) {
      message.colour = Colour.fromPartial(object.colour);
    } else {
      message.colour = undefined;
    }
    if (object.isConnected !== undefined && object.isConnected !== null) {
      message.isConnected = object.isConnected;
    } else {
      message.isConnected = false;
    }
    if (object.isAdmin !== undefined && object.isAdmin !== null) {
      message.isAdmin = object.isAdmin;
    } else {
      message.isAdmin = false;
    }
    if (object.afkTimeTicks !== undefined && object.afkTimeTicks !== null) {
      message.afkTimeTicks = object.afkTimeTicks;
    } else {
      message.afkTimeTicks = 0;
    }
    if (
      object.onlineTimeTicks !== undefined &&
      object.onlineTimeTicks !== null
    ) {
      message.onlineTimeTicks = object.onlineTimeTicks;
    } else {
      message.onlineTimeTicks = 0;
    }
    if (
      object.lastOnlineTicks !== undefined &&
      object.lastOnlineTicks !== null
    ) {
      message.lastOnlineTicks = object.lastOnlineTicks;
    } else {
      message.lastOnlineTicks = 0;
    }
    return message;
  },
};

const baseLuaEntity: object = {
  name: "",
  type: "",
  status: "",
  isActive: false,
  isMoving: false,
};

export const LuaEntity = {
  encode(
    message: LuaEntity,
    writer: _m0.Writer = _m0.Writer.create()
  ): _m0.Writer {
    if (message.unitNumber !== undefined) {
      writer.uint32(8).uint32(message.unitNumber);
    }
    if (message.name !== "") {
      writer.uint32(18).string(message.name);
    }
    if (message.type !== "") {
      writer.uint32(26).string(message.type);
    }
    if (message.status !== "") {
      writer.uint32(34).string(message.status);
    }
    if (message.isActive === true) {
      writer.uint32(64).bool(message.isActive);
    }
    if (message.isMoving === true) {
      writer.uint32(72).bool(message.isMoving);
    }
    if (message.playerIndex !== undefined) {
      writer.uint32(400).uint32(message.playerIndex);
    }
    if (message.lastUserIndex !== undefined) {
      writer.uint32(408).uint32(message.lastUserIndex);
    }
    if (message.colour !== undefined) {
      Colour.encode(message.colour, writer.uint32(802).fork()).ldelim();
    }
    return writer;
  },

  decode(input: _m0.Reader | Uint8Array, length?: number): LuaEntity {
    const reader = input instanceof _m0.Reader ? input : new _m0.Reader(input);
    let end = length === undefined ? reader.len : reader.pos + length;
    const message = { ...baseLuaEntity } as LuaEntity;
    while (reader.pos < end) {
      const tag = reader.uint32();
      switch (tag >>> 3) {
        case 1:
          message.unitNumber = reader.uint32();
          break;
        case 2:
          message.name = reader.string();
          break;
        case 3:
          message.type = reader.string();
          break;
        case 4:
          message.status = reader.string();
          break;
        case 8:
          message.isActive = reader.bool();
          break;
        case 9:
          message.isMoving = reader.bool();
          break;
        case 50:
          message.playerIndex = reader.uint32();
          break;
        case 51:
          message.lastUserIndex = reader.uint32();
          break;
        case 100:
          message.colour = Colour.decode(reader, reader.uint32());
          break;
        default:
          reader.skipType(tag & 7);
          break;
      }
    }
    return message;
  },

  fromPartial(object: DeepPartial<LuaEntity>): LuaEntity {
    const message = { ...baseLuaEntity } as LuaEntity;
    if (object.unitNumber !== undefined && object.unitNumber !== null) {
      message.unitNumber = object.unitNumber;
    } else {
      message.unitNumber = undefined;
    }
    if (object.name !== undefined && object.name !== null) {
      message.name = object.name;
    } else {
      message.name = "";
    }
    if (object.type !== undefined && object.type !== null) {
      message.type = object.type;
    } else {
      message.type = "";
    }
    if (object.status !== undefined && object.status !== null) {
      message.status = object.status;
    } else {
      message.status = "";
    }
    if (object.isActive !== undefined && object.isActive !== null) {
      message.isActive = object.isActive;
    } else {
      message.isActive = false;
    }
    if (object.isMoving !== undefined && object.isMoving !== null) {
      message.isMoving = object.isMoving;
    } else {
      message.isMoving = false;
    }
    if (object.playerIndex !== undefined && object.playerIndex !== null) {
      message.playerIndex = object.playerIndex;
    } else {
      message.playerIndex = undefined;
    }
    if (object.lastUserIndex !== undefined && object.lastUserIndex !== null) {
      message.lastUserIndex = object.lastUserIndex;
    } else {
      message.lastUserIndex = undefined;
    }
    if (object.colour !== undefined && object.colour !== null) {
      message.colour = Colour.fromPartial(object.colour);
    } else {
      message.colour = undefined;
    }
    return message;
  },
};

const baseLuaSurface: object = { index: 0, name: "", daytime: 0 };

export const LuaSurface = {
  encode(
    message: LuaSurface,
    writer: _m0.Writer = _m0.Writer.create()
  ): _m0.Writer {
    if (message.index !== 0) {
      writer.uint32(8).uint32(message.index);
    }
    if (message.name !== "") {
      writer.uint32(18).string(message.name);
    }
    if (message.daytime !== 0) {
      writer.uint32(29).float(message.daytime);
    }
    return writer;
  },

  decode(input: _m0.Reader | Uint8Array, length?: number): LuaSurface {
    const reader = input instanceof _m0.Reader ? input : new _m0.Reader(input);
    let end = length === undefined ? reader.len : reader.pos + length;
    const message = { ...baseLuaSurface } as LuaSurface;
    while (reader.pos < end) {
      const tag = reader.uint32();
      switch (tag >>> 3) {
        case 1:
          message.index = reader.uint32();
          break;
        case 2:
          message.name = reader.string();
          break;
        case 3:
          message.daytime = reader.float();
          break;
        default:
          reader.skipType(tag & 7);
          break;
      }
    }
    return message;
  },

  fromPartial(object: DeepPartial<LuaSurface>): LuaSurface {
    const message = { ...baseLuaSurface } as LuaSurface;
    if (object.index !== undefined && object.index !== null) {
      message.index = object.index;
    } else {
      message.index = 0;
    }
    if (object.name !== undefined && object.name !== null) {
      message.name = object.name;
    } else {
      message.name = "";
    }
    if (object.daytime !== undefined && object.daytime !== null) {
      message.daytime = object.daytime;
    } else {
      message.daytime = 0;
    }
    return message;
  },
};

const baseLuaForce: object = { index: 0, forceName: "", playerIds: 0 };

export const LuaForce = {
  encode(
    message: LuaForce,
    writer: _m0.Writer = _m0.Writer.create()
  ): _m0.Writer {
    if (message.index !== 0) {
      writer.uint32(8).uint32(message.index);
    }
    if (message.forceName !== "") {
      writer.uint32(18).string(message.forceName);
    }
    writer.uint32(802).fork();
    for (const v of message.playerIds) {
      writer.uint32(v);
    }
    writer.ldelim();
    return writer;
  },

  decode(input: _m0.Reader | Uint8Array, length?: number): LuaForce {
    const reader = input instanceof _m0.Reader ? input : new _m0.Reader(input);
    let end = length === undefined ? reader.len : reader.pos + length;
    const message = { ...baseLuaForce } as LuaForce;
    message.playerIds = [];
    while (reader.pos < end) {
      const tag = reader.uint32();
      switch (tag >>> 3) {
        case 1:
          message.index = reader.uint32();
          break;
        case 2:
          message.forceName = reader.string();
          break;
        case 100:
          if ((tag & 7) === 2) {
            const end2 = reader.uint32() + reader.pos;
            while (reader.pos < end2) {
              message.playerIds.push(reader.uint32());
            }
          } else {
            message.playerIds.push(reader.uint32());
          }
          break;
        default:
          reader.skipType(tag & 7);
          break;
      }
    }
    return message;
  },

  fromPartial(object: DeepPartial<LuaForce>): LuaForce {
    const message = { ...baseLuaForce } as LuaForce;
    message.playerIds = [];
    if (object.index !== undefined && object.index !== null) {
      message.index = object.index;
    } else {
      message.index = 0;
    }
    if (object.forceName !== undefined && object.forceName !== null) {
      message.forceName = object.forceName;
    } else {
      message.forceName = "";
    }
    if (object.playerIds !== undefined && object.playerIds !== null) {
      for (const e of object.playerIds) {
        message.playerIds.push(e);
      }
    }
    return message;
  },
};

const basePosition: object = { x: 0, y: 0 };

export const Position = {
  encode(
    message: Position,
    writer: _m0.Writer = _m0.Writer.create()
  ): _m0.Writer {
    if (message.x !== 0) {
      writer.uint32(8).int32(message.x);
    }
    if (message.y !== 0) {
      writer.uint32(16).int32(message.y);
    }
    return writer;
  },

  decode(input: _m0.Reader | Uint8Array, length?: number): Position {
    const reader = input instanceof _m0.Reader ? input : new _m0.Reader(input);
    let end = length === undefined ? reader.len : reader.pos + length;
    const message = { ...basePosition } as Position;
    while (reader.pos < end) {
      const tag = reader.uint32();
      switch (tag >>> 3) {
        case 1:
          message.x = reader.int32();
          break;
        case 2:
          message.y = reader.int32();
          break;
        default:
          reader.skipType(tag & 7);
          break;
      }
    }
    return message;
  },

  fromPartial(object: DeepPartial<Position>): Position {
    const message = { ...basePosition } as Position;
    if (object.x !== undefined && object.x !== null) {
      message.x = object.x;
    } else {
      message.x = 0;
    }
    if (object.y !== undefined && object.y !== null) {
      message.y = object.y;
    } else {
      message.y = 0;
    }
    return message;
  },
};

const baseColour: object = { red: 0, green: 0, blue: 0, alpha: 0 };

export const Colour = {
  encode(
    message: Colour,
    writer: _m0.Writer = _m0.Writer.create()
  ): _m0.Writer {
    if (message.red !== 0) {
      writer.uint32(13).float(message.red);
    }
    if (message.green !== 0) {
      writer.uint32(21).float(message.green);
    }
    if (message.blue !== 0) {
      writer.uint32(29).float(message.blue);
    }
    if (message.alpha !== 0) {
      writer.uint32(37).float(message.alpha);
    }
    return writer;
  },

  decode(input: _m0.Reader | Uint8Array, length?: number): Colour {
    const reader = input instanceof _m0.Reader ? input : new _m0.Reader(input);
    let end = length === undefined ? reader.len : reader.pos + length;
    const message = { ...baseColour } as Colour;
    while (reader.pos < end) {
      const tag = reader.uint32();
      switch (tag >>> 3) {
        case 1:
          message.red = reader.float();
          break;
        case 2:
          message.green = reader.float();
          break;
        case 3:
          message.blue = reader.float();
          break;
        case 4:
          message.alpha = reader.float();
          break;
        default:
          reader.skipType(tag & 7);
          break;
      }
    }
    return message;
  },

  fromPartial(object: DeepPartial<Colour>): Colour {
    const message = { ...baseColour } as Colour;
    if (object.red !== undefined && object.red !== null) {
      message.red = object.red;
    } else {
      message.red = 0;
    }
    if (object.green !== undefined && object.green !== null) {
      message.green = object.green;
    } else {
      message.green = 0;
    }
    if (object.blue !== undefined && object.blue !== null) {
      message.blue = object.blue;
    } else {
      message.blue = 0;
    }
    if (object.alpha !== undefined && object.alpha !== null) {
      message.alpha = object.alpha;
    } else {
      message.alpha = 0;
    }
    return message;
  },
};

type Builtin =
  | Date
  | Function
  | Uint8Array
  | string
  | number
  | boolean
  | undefined;
type DeepPartial<T> = T extends Builtin
  ? T
  : T extends Array<infer U>
  ? Array<DeepPartial<U>>
  : T extends ReadonlyArray<infer U>
  ? ReadonlyArray<DeepPartial<U>>
  : T extends {}
  ? { [K in keyof T]?: DeepPartial<T[K]> }
  : Partial<T>;

if (_m0.util.Long !== Long) {
  _m0.util.Long = Long as any;
  _m0.configure();
}
