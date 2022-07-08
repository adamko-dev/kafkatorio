local ____exports = {}
local emit, getMapTilePrototypes, getEntityPrototypes, convertItemGroup, convertItemSubgroup
local ____PacketEmitter = require("main.PacketEmitter")
local PacketEmitter = ____PacketEmitter.default
local ____kafkatorio_2Dschema = require("generated.kafkatorio-schema")
local FactorioPrototype = ____kafkatorio_2Dschema.FactorioPrototype
local KafkatorioPacketData = ____kafkatorio_2Dschema.KafkatorioPacketData
local ____converters = require("main.events.converters")
local Converters = ____converters.Converters
function emit(protosByType)
    for ____, protos in pairs(protosByType) do
        PacketEmitter:emitInstantPacket({type = KafkatorioPacketData.Type.PrototypesUpdate, prototypes = protos})
    end
end
function getMapTilePrototypes()
    local tiles = {}
    if not (tiles.tile ~= nil) then
        tiles.tile = {}
    end
    for ____, tile in pairs(game.tile_prototypes) do
        local ____tiles_tile_0 = tiles.tile
        ____tiles_tile_0[#____tiles_tile_0 + 1] = {
            type = FactorioPrototype.Type.MapTile,
            protoId = Converters.prototypeId("tile", tile.name),
            order = tile.order,
            layer = tile.layer,
            collisionMasks = Converters.convertCollisionMaskToNames(tile.collision_mask),
            mapColour = Converters.mapColour(tile.map_color),
            canBeMined = tile.mineable_properties.minable
        }
    end
    return tiles
end
function getEntityPrototypes()
    local protos = {}
    for ____, entity in pairs(game.entity_prototypes) do
        local key = (entity.group.name .. "/") .. entity.subgroup.name
        if not (protos[key] ~= nil) then
            protos[key] = {}
        end
        local entityProto = {
            type = FactorioPrototype.Type.Entity,
            protoId = Converters.prototypeId(entity.type, entity.name),
            group = convertItemGroup(entity.group),
            subgroup = convertItemSubgroup(entity.subgroup),
            isBuilding = entity.is_building,
            isEntityWithOwner = entity.is_entity_with_owner,
            isMilitaryTarget = entity.is_military_target,
            maxHealth = entity.max_health
        }
        if entity.map_color ~= nil then
            entityProto.mapColour = Converters.mapColour(entity.map_color)
        end
        if entity.friendly_map_color ~= nil then
            entityProto.mapColourFriend = Converters.mapColour(entity.friendly_map_color)
        end
        if entity.enemy_map_color ~= nil then
            entityProto.mapColourEnemy = Converters.mapColour(entity.enemy_map_color)
        end
        entityProto.miningProperties = Converters.miningProperties(entity.mineable_properties)
        local ____protos_key_1 = protos[key]
        ____protos_key_1[#____protos_key_1 + 1] = entityProto
    end
    return protos
end
function convertItemGroup(itemGroup)
    return {name = itemGroup.name, type = itemGroup.type, parentName = nil}
end
function convertItemSubgroup(itemGroup)
    local ____itemGroup_name_4 = itemGroup.name
    local ____itemGroup_type_5 = itemGroup.type
    local ____itemGroup_group_name_2 = itemGroup.group
    if ____itemGroup_group_name_2 ~= nil then
        ____itemGroup_group_name_2 = ____itemGroup_group_name_2.name
    end
    return {name = ____itemGroup_name_4, type = ____itemGroup_type_5, parentName = ____itemGroup_group_name_2}
end
function ____exports.emitPrototypes()
    local mapTileProtos = getMapTilePrototypes()
    emit(mapTileProtos)
    local ____ = game.item_group_prototypes
    local ____ = game.item_subgroup_prototypes
    local entityProtos = getEntityPrototypes()
    emit(entityProtos)
end
return ____exports
