import {Serdes} from "../events/serdes";

export function convertPrototypes (): FactorioPrototype[] {
  let prototypes = new Array<FactorioPrototype>()

  prototypes.push(...getMapTilePrototypes())

  return prototypes
}

function getMapTilePrototypes(): FactorioMapTilePrototype[] {
  let tiles: FactorioMapTilePrototype[] = []
  for (let [_, tile] of pairs(game.tile_prototypes)) {
    tiles.push(
        <FactorioMapTilePrototype>{
          objectName: tile.object_name,

          name: tile.name,
          order: tile.order,
          layer: tile.layer,
          collisionMasks: convertCollisionMaskToNames(tile.collision_mask),
          mapColor: Serdes.mapColour(tile.map_color),
          canBeMined: tile.mineable_properties.minable,
        }
    )
  }
  return tiles
}

function convertCollisionMaskToNames(cm: CollisionMask): string[] {
  let masks: string[] = []
  for (let [name, _] of pairs(cm)) {
    masks.push(name)
  }
  return masks
}
