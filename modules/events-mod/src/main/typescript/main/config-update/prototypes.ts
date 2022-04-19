import {Converters} from "../events/converters";
import PacketEmitter from "../PacketEmitter";
import {
  FactorioPrototype2,
  KafkatorioPacketData2
} from "../../generated/kafkatorio-schema/kafkatorio-schema";
import Type = KafkatorioPacketData2.Type;
import PrototypesUpdate = KafkatorioPacketData2.PrototypesUpdate;


export function emitPrototypes() {

  const data: PrototypesUpdate = {
    prototypes: prototypes(),
    type: Type.PrototypesUpdate,
  }

  PacketEmitter.emitInstantPacket(data)
}

function prototypes(): FactorioPrototype2[] {
  let prototypes: FactorioPrototype2[] = []

  prototypes.push(...getMapTilePrototypes())

  return prototypes
}

function getMapTilePrototypes(): FactorioPrototype2.MapTile[] {
  let tiles: FactorioPrototype2.MapTile[] = []
  for (let [, tile] of game.tile_prototypes) {
    tiles.push(
        <FactorioPrototype2.MapTile>{
          type: FactorioPrototype2.Type.MapTile,

          name: tile.name,
          order: tile.order,
          layer: tile.layer,
          collisionMasks: convertCollisionMaskToNames(tile.collision_mask),
          mapColour: Converters.mapColour(tile.map_color),
          canBeMined: tile.mineable_properties.minable,
        }
    )
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
