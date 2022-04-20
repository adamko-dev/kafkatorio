import {Converters} from "../events/converters";
import PacketEmitter from "../PacketEmitter";
import {
  FactorioPrototype,
  KafkatorioPacketData
} from "../../generated/kafkatorio-schema/kafkatorio-schema";
import Type = KafkatorioPacketData.Type;
import PrototypesUpdate = KafkatorioPacketData.PrototypesUpdate;


export function emitPrototypes() {

  const data: PrototypesUpdate = {
    prototypes: prototypes(),
    type: Type.PrototypesUpdate,
  }

  PacketEmitter.emitInstantPacket(data)
}

function prototypes(): FactorioPrototype[] {
  let prototypes: FactorioPrototype[] = []

  prototypes.push(...getMapTilePrototypes())

  return prototypes
}

function getMapTilePrototypes(): FactorioPrototype.MapTile[] {
  let tiles: FactorioPrototype.MapTile[] = []
  for (let [, tile] of game.tile_prototypes) {
    tiles.push({
      type: FactorioPrototype.Type.MapTile,

      name: tile.name,
      order: tile.order,
      layer: tile.layer,
      collisionMasks: convertCollisionMaskToNames(tile.collision_mask),
      mapColour: Converters.mapColour(tile.map_color),
      canBeMined: tile.mineable_properties.minable,
    })
  }
  return tiles
}

function convertCollisionMaskToNames(cm: CollisionMask): string[] {
  let masks: string[] = []
  for (let [name] of pairs(cm)) {
    masks.push(name)
  }
  return masks
}
