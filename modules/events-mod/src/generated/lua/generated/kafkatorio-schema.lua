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
    KafkatorioPacketData.Type.MapChunkEntityUpdate = "kafkatorio.packet.keyed.MapChunkEntityUpdate"
    KafkatorioPacketData.Type.MapChunkTileUpdate = "kafkatorio.packet.keyed.MapChunkTileUpdate"
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
____exports.FactorioEntityData = {}
local FactorioEntityData = ____exports.FactorioEntityData
do
    FactorioEntityData.Type = Type or ({})
    FactorioEntityData.Type.Resource = "kafkatorio.entity.FactorioEntityData.Resource"
    FactorioEntityData.Type.Standard = "kafkatorio.entity.FactorioEntityData.Standard"
end
____exports.EntityStatus = EntityStatus or ({})
____exports.EntityStatus.CANT_DIVIDE_SEGMENTS = "CANT_DIVIDE_SEGMENTS"
____exports.EntityStatus.CHARGING = "CHARGING"
____exports.EntityStatus.CLOSED_BY_CIRCUIT_NETWORK = "CLOSED_BY_CIRCUIT_NETWORK"
____exports.EntityStatus.DISABLED = "DISABLED"
____exports.EntityStatus.DISABLED_BY_CONTROL_BEHAVIOR = "DISABLED_BY_CONTROL_BEHAVIOR"
____exports.EntityStatus.DISABLED_BY_SCRIPT = "DISABLED_BY_SCRIPT"
____exports.EntityStatus.DISCHARGING = "DISCHARGING"
____exports.EntityStatus.FLUID_INGREDIENT_SHORTAGE = "FLUID_INGREDIENT_SHORTAGE"
____exports.EntityStatus.FULLY_CHARGED = "FULLY_CHARGED"
____exports.EntityStatus.FULL_OUTPUT = "FULL_OUTPUT"
____exports.EntityStatus.ITEM_INGREDIENT_SHORTAGE = "ITEM_INGREDIENT_SHORTAGE"
____exports.EntityStatus.LAUNCHING_ROCKET = "LAUNCHING_ROCKET"
____exports.EntityStatus.LOW_INPUT_FLUID = "LOW_INPUT_FLUID"
____exports.EntityStatus.LOW_POWER = "LOW_POWER"
____exports.EntityStatus.LOW_TEMPERATURE = "LOW_TEMPERATURE"
____exports.EntityStatus.MARKED_FOR_DECONSTRUCTION = "MARKED_FOR_DECONSTRUCTION"
____exports.EntityStatus.MISSING_REQUIRED_FLUID = "MISSING_REQUIRED_FLUID"
____exports.EntityStatus.MISSING_SCIENCE_PACKS = "MISSING_SCIENCE_PACKS"
____exports.EntityStatus.NETWORKS_CONNECTED = "NETWORKS_CONNECTED"
____exports.EntityStatus.NETWORKS_DISCONNECTED = "NETWORKS_DISCONNECTED"
____exports.EntityStatus.NORMAL = "NORMAL"
____exports.EntityStatus.NOT_CONNECTED_TO_RAIL = "NOT_CONNECTED_TO_RAIL"
____exports.EntityStatus.NOT_PLUGGED_IN_ELECTRIC_NETWORK = "NOT_PLUGGED_IN_ELECTRIC_NETWORK"
____exports.EntityStatus.NO_AMMO = "NO_AMMO"
____exports.EntityStatus.NO_FUEL = "NO_FUEL"
____exports.EntityStatus.NO_INGREDIENTS = "NO_INGREDIENTS"
____exports.EntityStatus.NO_INPUT_FLUID = "NO_INPUT_FLUID"
____exports.EntityStatus.NO_MINABLE_RESOURCES = "NO_MINABLE_RESOURCES"
____exports.EntityStatus.NO_MODULES_TO_TRANSMIT = "NO_MODULES_TO_TRANSMIT"
____exports.EntityStatus.NO_POWER = "NO_POWER"
____exports.EntityStatus.NO_RECIPE = "NO_RECIPE"
____exports.EntityStatus.NO_RESEARCH_IN_PROGRESS = "NO_RESEARCH_IN_PROGRESS"
____exports.EntityStatus.OPENED_BY_CIRCUIT_NETWORK = "OPENED_BY_CIRCUIT_NETWORK"
____exports.EntityStatus.OUT_OF_LOGISTIC_NETWORK = "OUT_OF_LOGISTIC_NETWORK"
____exports.EntityStatus.PREPARING_ROCKET_FOR_LAUNCH = "PREPARING_ROCKET_FOR_LAUNCH"
____exports.EntityStatus.RECHARGING_AFTER_POWER_OUTAGE = "RECHARGING_AFTER_POWER_OUTAGE"
____exports.EntityStatus.TURNED_OFF_DURING_DAYTIME = "TURNED_OFF_DURING_DAYTIME"
____exports.EntityStatus.WAITING_FOR_SOURCE_ITEMS = "WAITING_FOR_SOURCE_ITEMS"
____exports.EntityStatus.WAITING_FOR_SPACE_IN_DESTINATION = "WAITING_FOR_SPACE_IN_DESTINATION"
____exports.EntityStatus.WAITING_FOR_TARGET_TO_BE_BUILT = "WAITING_FOR_TARGET_TO_BE_BUILT"
____exports.EntityStatus.WAITING_FOR_TRAIN = "WAITING_FOR_TRAIN"
____exports.EntityStatus.WAITING_TO_LAUNCH_ROCKET = "WAITING_TO_LAUNCH_ROCKET"
____exports.EntityStatus.WORKING = "WORKING"
____exports.MinedProduct = {}
local MinedProduct = ____exports.MinedProduct
do
    MinedProduct.Type = Type or ({})
    MinedProduct.Type.MinedProductFluid = "kafkatorio.resource.MinedProduct.MinedProductFluid"
    MinedProduct.Type.MinedProductItem = "kafkatorio.resource.MinedProduct.MinedProductItem"
end
return ____exports
