local ____exports = {}
local emit, getMapTilePrototypes, getEntityPrototypes, convertItemGroup
local ____kafkatorio_2Dschema = require("generated.kafkatorio-schema")
local FactorioPrototype = ____kafkatorio_2Dschema.FactorioPrototype
local KafkatorioPacketData = ____kafkatorio_2Dschema.KafkatorioPacketData
local ____converters = require("main.events.converters")
local Converters = ____converters.Converters
local ____KafkatorioPacketQueue = require("main.emitting.KafkatorioPacketQueue")
local KafkatorioPacketQueue = ____KafkatorioPacketQueue.default
function emit(protosByType)
    for ____, protos in pairs(protosByType) do
        KafkatorioPacketQueue:enqueue({type = KafkatorioPacketData.Type.PrototypesUpdate, prototypes = protos})
    end
end
function getMapTilePrototypes()
    local tiles = {}
    for ____, tile in pairs(game.tile_prototypes) do
        tiles[#tiles + 1] = {
            type = FactorioPrototype.Type.MapTile,
            protoId = Converters.prototypeId("tile", tile.name),
            order = tile.order,
            layer = tile.layer,
            collisionMasks = Converters.convertCollisionMaskToNames(tile.collision_mask),
            mapColour = Converters.mapColour(tile.map_color),
            canBeMined = tile.mineable_properties.minable
        }
    end
    return {tiles = tiles}
end
function getEntityPrototypes()
    local protos = {}
    for ____, entity in pairs(game.entity_prototypes) do
        local key = (((tostring(entity.group.name) .. "/") .. tostring(entity.subgroup.name)) .. "/") .. entity.type
        if protos[key] == nil then
            protos[key] = {}
        end
        local entityProto = {
            type = FactorioPrototype.Type.Entity,
            protoId = Converters.prototypeId(entity.type, entity.name),
            group = convertItemGroup(entity.group),
            subgroup = convertItemGroup(entity.subgroup),
            isBuilding = entity.is_building,
            isEntityWithOwner = entity.is_entity_with_owner,
            isMilitaryTarget = entity.is_military_target,
            maxHealth = entity.max_health,
            collisionBox = Converters.collisionBox(entity.collision_box),
            tileWidth = entity.tile_width,
            tileHeight = entity.tile_height
        }
        local ____ = entity.infinite_resource
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
        local ____protos_key_0 = protos[key]
        ____protos_key_0[#____protos_key_0 + 1] = entityProto
    end
    return protos
end
function convertItemGroup(itemGroup)
    local ____itemGroup_name_3 = itemGroup.name
    local ____itemGroup_type_4 = itemGroup.type
    local ____itemGroup_group_name_1 = itemGroup.group
    if ____itemGroup_group_name_1 ~= nil then
        ____itemGroup_group_name_1 = ____itemGroup_group_name_1.name
    end
    return {name = ____itemGroup_name_3, type = ____itemGroup_type_4, parentName = ____itemGroup_group_name_1 or nil}
end
function ____exports.emitPrototypes()
    local mapTileProtos = getMapTilePrototypes()
    emit(mapTileProtos)
    local entityProtos = getEntityPrototypes()
    emit(entityProtos)
end
return ____exports
