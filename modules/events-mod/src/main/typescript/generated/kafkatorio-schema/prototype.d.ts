// Generated by TypeScriptGenerator - do not edit this file manually

type PrototypeObjectName = "LuaTilePrototype";

interface FactorioPrototype {
  objectName: PrototypeObjectName;
}

interface FactorioMapTilePrototype extends FactorioPrototype {
  canBeMined: boolean;
  collisionMasks: string[];
  layer: uint;
  mapColor: Colour;
  name: string;
  objectName: PrototypeObjectName;
  order: string;
}
