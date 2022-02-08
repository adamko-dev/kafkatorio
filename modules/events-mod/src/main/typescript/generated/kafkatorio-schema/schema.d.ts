// Generated by TypeScriptGenerator - do not edit this file manually

type PacketType = "EVENT" | "CONFIG" | "PROTOTYPES";

interface KafkatorioPacket {
  modVersion: string;
  packetType: PacketType;
}

interface FactorioEvent extends KafkatorioPacket {
  data: FactorioObjectData;
  eventType: string;
  modVersion: string;
  packetType: PacketType;
  tick: uint;
}

interface Colour {
  alpha: float;
  blue: float;
  green: float;
  red: float;
}

interface MapEntityPosition {
  x: double;
  y: double;
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
