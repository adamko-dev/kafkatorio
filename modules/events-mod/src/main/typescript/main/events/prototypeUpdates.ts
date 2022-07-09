import PacketEmitter from "../emitting/PacketEmitter";
import {
  EntityItemGroup,
  FactorioPrototype,
  KafkatorioPacketData
} from "../../generated/kafkatorio-schema";
import {Converters} from "./converters";
import KafkatorioPacketQueue from "../emitting/KafkatorioPacketQueue";


type PrototypesByType<T extends FactorioPrototype> = Record<string, T[]>


export function emitPrototypes() {

  const mapTileProtos: PrototypesByType<FactorioPrototype.MapTile> = getMapTilePrototypes()
  emit(mapTileProtos)


  const entityProtos: PrototypesByType<FactorioPrototype.Entity> = getEntityPrototypes()
  emit(entityProtos)
}


function emit<T extends FactorioPrototype>(protosByType: PrototypesByType<T>) {
  for (const [, protos] of pairs(protosByType)) {
    KafkatorioPacketQueue.enqueue({
      type: KafkatorioPacketData.Type.PrototypesUpdate,
      prototypes: protos,
    })
  }
}


function getMapTilePrototypes(): PrototypesByType<FactorioPrototype.MapTile> {
  const tiles: FactorioPrototype.MapTile[] = []

  for (const [, tile] of game.tile_prototypes) {
    tiles[tiles.length] = {
      type: FactorioPrototype.Type.MapTile,

      protoId: Converters.prototypeId("tile", tile.name),
      order: tile.order,
      layer: tile.layer,
      collisionMasks: Converters.convertCollisionMaskToNames(tile.collision_mask),
      mapColour: Converters.mapColour(tile.map_color),
      canBeMined: tile.mineable_properties.minable,
    }
  }

  return {
    "tiles": tiles
  }
}


function getEntityPrototypes(): PrototypesByType<FactorioPrototype.Entity> {
  const protos: PrototypesByType<FactorioPrototype.Entity> = {}

  for (const [, entity] of game.entity_prototypes) {
    const key: string = `${entity.group.name}/${entity.subgroup.name}`

    protos[key] ??= []

    const entityProto: FactorioPrototype.Entity = {
      type: FactorioPrototype.Type.Entity,
      protoId: Converters.prototypeId(entity.type, entity.name),

      group: convertItemGroup(entity.group),
      subgroup: convertItemSubgroup(entity.subgroup),

      isBuilding: entity.is_building,
      isEntityWithOwner: entity.is_entity_with_owner,
      isMilitaryTarget: entity.is_military_target,
      maxHealth: entity.max_health,
    }

    if (entity.map_color != undefined) {
      entityProto.mapColour = Converters.mapColour(entity.map_color)
    }
    if (entity.friendly_map_color != undefined) {
      entityProto.mapColourFriend = Converters.mapColour(entity.friendly_map_color)
    }
    if (entity.enemy_map_color != undefined) {
      entityProto.mapColourEnemy = Converters.mapColour(entity.enemy_map_color)
    }
    entityProto.miningProperties = Converters.miningProperties(entity.mineable_properties)

    protos[key].push(entityProto)
  }
  return protos
}


function convertItemGroup(itemGroup: LuaGroup): EntityItemGroup {
  return {
    name: itemGroup.name,
    type: itemGroup.type,
    parentName: null,
  }
}


function convertItemSubgroup(itemGroup: LuaGroup): EntityItemGroup {
  return {
    name: itemGroup.name,
    type: itemGroup.type,
    parentName: itemGroup.group?.name,
  }
}
