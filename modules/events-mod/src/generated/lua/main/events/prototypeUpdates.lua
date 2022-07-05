local ____lualib = require("lualib_bundle")
local __TS__ArrayPushArray = ____lualib.__TS__ArrayPushArray
local ____exports = {}
local prototypeUpdates, getMapTilePrototypes
local ____PacketEmitter = require("main.PacketEmitter")
local PacketEmitter = ____PacketEmitter.default
local ____kafkatorio_2Dschema = require("generated.kafkatorio-schema")
local FactorioPrototype = ____kafkatorio_2Dschema.FactorioPrototype
local KafkatorioPacketData = ____kafkatorio_2Dschema.KafkatorioPacketData
local ____converters = require("main.events.converters")
local Converters = ____converters.Converters
function prototypeUpdates()
    local prototypes = {}
    __TS__ArrayPushArray(
        prototypes,
        getMapTilePrototypes()
    )
    return prototypes
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
    return tiles
end
function ____exports.emitPrototypes()
    local data = {
        prototypes = prototypeUpdates(),
        type = KafkatorioPacketData.Type.PrototypesUpdate
    }
    PacketEmitter:emitInstantPacket(data)
end
return ____exports
