local ____exports = {}
____exports.KafkatorioPacketData = {}
local KafkatorioPacketData = ____exports.KafkatorioPacketData
do
    KafkatorioPacketData.Type = Type or ({})
    KafkatorioPacketData.Type.ConfigurationUpdate = "kafkatorio.packet.instant.ConfigurationUpdate"
    KafkatorioPacketData.Type.ConsoleChatUpdate = "kafkatorio.packet.instant.ConsoleChatUpdate"
    KafkatorioPacketData.Type.ConsoleCommandUpdate = "kafkatorio.packet.instant.ConsoleCommandUpdate"
    KafkatorioPacketData.Type.PrototypesUpdate = "kafkatorio.packet.instant.PrototypesUpdate"
    KafkatorioPacketData.Type.SurfaceUpdate = "kafkatorio.packet.instant.SurfaceUpdate"
    KafkatorioPacketData.Type.EntityUpdate = "kafkatorio.packet.keyed.EntityUpdate"
    KafkatorioPacketData.Type.MapChunkUpdate = "kafkatorio.packet.keyed.MapChunkUpdate"
    KafkatorioPacketData.Type.PlayerUpdate = "kafkatorio.packet.keyed.PlayerUpdate"
    KafkatorioPacketData.Type.Error = "kafkatorio.packet.KafkatorioPacketData.Error"
end
____exports.FactorioPrototype = {}
local FactorioPrototype = ____exports.FactorioPrototype
do
    FactorioPrototype.Type = Type or ({})
    FactorioPrototype.Type.Entity = "kafkatorio.prototype.Entity"
    FactorioPrototype.Type.MapTile = "kafkatorio.prototype.MapTile"
end
____exports.MinedProductType = MinedProductType or ({})
____exports.MinedProductType.item = "item"
____exports.MinedProductType.fluid = "fluid"
return ____exports
