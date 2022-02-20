// Generated by TypeScriptGenerator - do not edit this file manually

type PacketType = "EVENT" | "CONFIG" | "PROTOTYPES" | "UPDATE";

interface KafkatorioPacket {
  modVersion: string;
  packetType: PacketType;
}

type FactorioEventUpdateType = "PLAYER" | "MAP_CHUNK" | "ENTITY";

interface FactorioEventUpdate {
  updateType: FactorioEventUpdateType;
}

interface FactorioEventUpdatePacket extends KafkatorioPacket {
  modVersion: string;
  packetType: PacketType;
  tick: uint;
  update: FactorioEventUpdate;
}

interface FactorioEvent extends KafkatorioPacket {
  data: FactorioObjectData;
  eventType: string;
  modVersion: string;
  packetType: PacketType;
  tick: uint;
}

interface MapEntityPosition {
  x: double;
  y: double;
}

interface FactorioLuaControl {
  force: uint;
  position: MapEntityPosition;
  surface: uint;
}

interface Colour {
  alpha: float;
  blue: float;
  green: float;
  red: float;
}

interface PlayerData extends FactorioObjectData, FactorioLuaControl {
  associatedCharactersUnitNumbers: uint[];
  characterUnitNumber: uint | null;
  chatColour: Colour;
  colour: Colour;
  force: uint;
  lastOnline: uint;
  name: string;
  objectName: ObjectName;
  position: MapEntityPosition;
  surface: uint;
}

interface EntityData extends FactorioObjectData, FactorioLuaControl {
  active: boolean;
  force: uint;
  health: double | null;
  healthRatio: double;
  name: string;
  objectName: ObjectName;
  playerIndex: uint | null;
  position: MapEntityPosition;
  surface: uint;
  type: string;
  unitNumber: uint | null;
}

interface MapTilePosition {
  x: int;
  y: int;
}

interface MapBoundingBox {
  bottomRight: MapTilePosition;
  topLeft: MapTilePosition;
}

interface MapChunkPosition {
  x: int;
  y: int;
}

interface MapTile {
  position: MapTilePosition;
  prototypeName: string;
}

interface EntityIdentifiers {
  name: string;
  type: string;
  unitNumber: uint | null;
}

interface PlayerUpdate extends FactorioEventUpdate {
  afkTime: uint | null;
  bannedReason: string | null;
  characterUnitNumber: uint | null;
  chatColour: Colour | null;
  colour: Colour | null;
  diedCause: EntityIdentifiers | null;
  disconnectReason: string | null;
  forceIndex: uint | null;
  index: uint;
  isAdmin: boolean | null;
  isConnected: boolean | null;
  isRemoved: boolean | null;
  isShowOnMap: boolean | null;
  isSpectator: boolean | null;
  kickedReason: string | null;
  lastOnline: uint | null;
  name: string | null;
  onlineTime: uint | null;
  position: MapEntityPosition | null;
  surfaceIndex: uint | null;
  tag: string | null;
  ticksToRespawn: uint | null;
  updateType: FactorioEventUpdateType;
}

interface EntityUpdate extends FactorioEventUpdate, EntityIdentifiers {
  chunkPosition: MapChunkPosition | null;
  graphicsVariation: number | null;
  health: float | null;
  isActive: boolean | null;
  isRotatable: boolean | null;
  lastUser: int | null;
  localisedDescription: string | null;
  localisedName: string | null;
  name: string;
  prototype: string | null;
  type: string;
  unitNumber: uint;
  updateType: FactorioEventUpdateType;
}

interface MapChunkUpdate extends FactorioEventUpdate {
  chunkPosition: MapChunkPosition;
  force: uint | null;
  isDeleted: boolean | null;
  player: uint | null;
  robot: EntityIdentifiers | null;
  surfaceIndex: uint;
  tiles: Map<MapTilePosition, string> | null;
  updateType: FactorioEventUpdateType;
}
