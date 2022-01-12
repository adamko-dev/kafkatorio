import {Converters} from "../events/converters";

export function convertPrototypes(): FactorioPrototype[] {
  let prototypes: FactorioPrototype[] = []

  prototypes.push(...getMapTilePrototypes())

  return prototypes
}

function getMapTilePrototypes(): MapTilePrototype[] {
  let tiles: MapTilePrototype[] = []
  for (let [, tile] of pairs(game.tile_prototypes)) {
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
