import PacketEmitter from "../PacketEmitter";
import {FactorioPrototype, KafkatorioPacketData} from "../../generated/kafkatorio-schema";
import {Converters} from "./converters";
import PrototypesUpdate = KafkatorioPacketData.PrototypesUpdate;


export function emitPrototypes() {

  const data: PrototypesUpdate = {
    prototypes: prototypeUpdates(),
    type: KafkatorioPacketData.Type.PrototypesUpdate,
  }

  PacketEmitter.emitInstantPacket(data)
}


function prototypeUpdates(): FactorioPrototype[] {
  let prototypes: FactorioPrototype[] = []

  prototypes.push(...getMapTilePrototypes())

  return prototypes
}


function getMapTilePrototypes(): FactorioPrototype.MapTile[] {
  let tiles: FactorioPrototype.MapTile[] = []
  for (const [, tile] of game.tile_prototypes) {
    tiles.push({
      type: FactorioPrototype.Type.MapTile,

      protoId: Converters.prototypeId("tile", tile.name),
      order: tile.order,
      layer: tile.layer,
      collisionMasks: Converters.convertCollisionMaskToNames(tile.collision_mask),
      mapColour: Converters.mapColour(tile.map_color),
      canBeMined: tile.mineable_properties.minable,
    })
  }
  return tiles
}


// function getEntityPrototypes(): FactorioPrototype.MapTile[] {
//   let tiles: FactorioPrototype.MapTile[] = []
//   for (const [, tile] of game.tile_prototypes) {
//     tiles.push({
//       type: FactorioPrototype.Type.MapTile,
//
//       name: tile.name,
//       order: tile.order,
//       layer: tile.layer,
//       collisionMasks: convertCollisionMaskToNames(tile.collision_mask),
//       mapColour: Converters.mapColour(tile.map_color),
//       canBeMined: tile.mineable_properties.minable,
//     })
//   }
//   return tiles
// }
