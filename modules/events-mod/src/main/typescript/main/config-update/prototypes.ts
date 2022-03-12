import {Converters} from "../events/converters";
import {emitPacket} from "../emitKafkatorioPacket";


export function emitPrototypes() {
  emitPacket<FactorioPrototypes>({
    modVersion: global.MOD_VERSION,
    packetType: "PROTOTYPES",
    prototypes: prototypes(),
    tick: game.tick,
  })
}

function prototypes(): FactorioPrototype[] {
  let prototypes: FactorioPrototype[] = []

  prototypes.push(...getMapTilePrototypes())

  return prototypes
}

function getMapTilePrototypes(): MapTilePrototype[] {
  let tiles: MapTilePrototype[] = []
  for (let [, tile] of game.tile_prototypes) {
    tiles.push(
        <MapTilePrototype>{
          prototypeObjectName: tile.object_name,

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
