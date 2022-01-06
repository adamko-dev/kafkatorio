import {Serdes} from "../events/serdes";

export function emitTiles() {

  let tilePrototypes = new LuaTable<string, FactorioMapTilePrototype>()

  for (const [_, tile] of pairs(game.tile_prototypes)) {
    // let ttttt = game.tile_prototypes[tileName]
    tilePrototypes.set(tile.name, mapTilePrototype(tile))
  }

  // for (let tileName in game.tile_prototypes) {
  //   const tile = game.tile_prototypes[tileName]
  //   tilePrototypes.set(tileName, mapTilePrototype(tile))
  // }

  // for (const [id, player] of pairs(game.players)) {
  //   const num: number = id
  //   const p: LuaPlayer = player
  // }
  emitPrototypes(tilePrototypes)
}

function mapTilePrototype(tile: LuaTilePrototype): FactorioMapTilePrototype {
  return {
    object_name: tile.object_name,

    name: tile.name,
    order: tile.order,
    layer: tile.layer,
    collision_masks: [],
    map_color: Serdes.mapColour(tile.map_color),
    can_be_mined: tile.mineable_properties.minable,
  }
}

function emitPrototypes<T extends FactorioPrototype>(prototypes: LuaTable<string, T>) {

  let data = game.table_to_json(prototypes)

  localised_print(`FactorioPrototype: ${data}`)
}
