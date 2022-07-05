local ____exports = {}
local ____kafkatorio_2Dschema = require("generated.kafkatorio-schema")
local EntityStatus = ____kafkatorio_2Dschema.EntityStatus
local FactorioEntityData = ____kafkatorio_2Dschema.FactorioEntityData
____exports.Converters = {}
local Converters = ____exports.Converters
do
    local convertEntityStatus, EntityStatusRecord
    function convertEntityStatus(status)
        if status == nil then
            return nil
        else
            return EntityStatusRecord[status]
        end
    end
    function Converters.prototypeId(____type, name)
        return (____type .. "/") .. name
    end
    local floor = math.floor
    local mapEventIdToName = {}
    for eventName, eventId in pairs(defines.events) do
        mapEventIdToName[eventId] = eventName
    end
    function Converters.eventNameString(event)
        return mapEventIdToName[event]
    end
    function Converters.mapColour(color)
        return {color.r or 0, color.g or 0, color.b or 0, color.a or 0}
    end
    function Converters.convertPlacedTiles(placedTile, oldTiles)
        local converted = {}
        for ____, tile in ipairs(oldTiles) do
            converted[#converted + 1] = {position = {x = tile.position.x, y = tile.position.y}, name = placedTile.name}
        end
        return converted
    end
    function Converters.convertRemovedTiles(surface, oldTiles)
        local converted = {}
        for ____, tile in ipairs(oldTiles) do
            converted[#converted + 1] = surface.get_tile(tile.position.x, tile.position.y)
        end
        return converted
    end
    function Converters.convertMapTablePosition(position)
        return {position.x, position.y}
    end
    function Converters.convertCollisionMaskToNames(cm)
        local masks = {}
        for name in pairs(cm) do
            masks[#masks + 1] = name
        end
        return masks
    end
    function Converters.convertResourceEntity(entity)
        if entity.type ~= "resource" then
            return nil
        else
            return {
                type = FactorioEntityData.Type.Resource,
                protoId = Converters.prototypeId(entity.type, entity.name),
                status = convertEntityStatus(entity.status),
                amount = entity.amount,
                initialAmount = entity.initial_amount,
                position = Converters.convertMapTablePosition(entity.position)
            }
        end
    end
    function Converters.prototypeIdEntity(entity)
        return Converters.prototypeId(entity.type, entity.name)
    end
    function Converters.tilePositionToChunkPosition(mapPosition)
        return {
            floor(mapPosition.x / 32),
            floor(mapPosition.y)
        }
    end
    function Converters.mapPositionToChunkPosition(mapPosition)
        return {
            floor(mapPosition.x / 32),
            floor(mapPosition.y)
        }
    end
    function Converters.entityStatus(status)
        if status == nil then
            return nil
        else
            return EntityStatusRecord[status]
        end
    end
    EntityStatusRecord = {
        [defines.entity_status.cant_divide_segments] = EntityStatus.CANT_DIVIDE_SEGMENTS,
        [defines.entity_status.charging] = EntityStatus.CHARGING,
        [defines.entity_status.closed_by_circuit_network] = EntityStatus.CLOSED_BY_CIRCUIT_NETWORK,
        [defines.entity_status.disabled] = EntityStatus.DISABLED,
        [defines.entity_status.disabled_by_control_behavior] = EntityStatus.DISABLED_BY_CONTROL_BEHAVIOR,
        [defines.entity_status.disabled_by_script] = EntityStatus.DISABLED_BY_SCRIPT,
        [defines.entity_status.discharging] = EntityStatus.DISCHARGING,
        [defines.entity_status.fluid_ingredient_shortage] = EntityStatus.FLUID_INGREDIENT_SHORTAGE,
        [defines.entity_status.fully_charged] = EntityStatus.FULLY_CHARGED,
        [defines.entity_status.full_output] = EntityStatus.FULL_OUTPUT,
        [defines.entity_status.item_ingredient_shortage] = EntityStatus.ITEM_INGREDIENT_SHORTAGE,
        [defines.entity_status.launching_rocket] = EntityStatus.LAUNCHING_ROCKET,
        [defines.entity_status.low_input_fluid] = EntityStatus.LOW_INPUT_FLUID,
        [defines.entity_status.low_power] = EntityStatus.LOW_POWER,
        [defines.entity_status.low_temperature] = EntityStatus.LOW_TEMPERATURE,
        [defines.entity_status.marked_for_deconstruction] = EntityStatus.MARKED_FOR_DECONSTRUCTION,
        [defines.entity_status.missing_required_fluid] = EntityStatus.MISSING_REQUIRED_FLUID,
        [defines.entity_status.missing_science_packs] = EntityStatus.MISSING_SCIENCE_PACKS,
        [defines.entity_status.networks_connected] = EntityStatus.NETWORKS_CONNECTED,
        [defines.entity_status.networks_disconnected] = EntityStatus.NETWORKS_DISCONNECTED,
        [defines.entity_status.normal] = EntityStatus.NORMAL,
        [defines.entity_status.not_connected_to_rail] = EntityStatus.NOT_CONNECTED_TO_RAIL,
        [defines.entity_status.not_plugged_in_electric_network] = EntityStatus.NOT_PLUGGED_IN_ELECTRIC_NETWORK,
        [defines.entity_status.no_ammo] = EntityStatus.NO_AMMO,
        [defines.entity_status.no_fuel] = EntityStatus.NO_FUEL,
        [defines.entity_status.no_ingredients] = EntityStatus.NO_INGREDIENTS,
        [defines.entity_status.no_input_fluid] = EntityStatus.NO_INPUT_FLUID,
        [defines.entity_status.no_minable_resources] = EntityStatus.NO_MINABLE_RESOURCES,
        [defines.entity_status.no_modules_to_transmit] = EntityStatus.NO_MODULES_TO_TRANSMIT,
        [defines.entity_status.no_power] = EntityStatus.NO_POWER,
        [defines.entity_status.no_recipe] = EntityStatus.NO_RECIPE,
        [defines.entity_status.no_research_in_progress] = EntityStatus.NO_RESEARCH_IN_PROGRESS,
        [defines.entity_status.opened_by_circuit_network] = EntityStatus.OPENED_BY_CIRCUIT_NETWORK,
        [defines.entity_status.out_of_logistic_network] = EntityStatus.OUT_OF_LOGISTIC_NETWORK,
        [defines.entity_status.preparing_rocket_for_launch] = EntityStatus.PREPARING_ROCKET_FOR_LAUNCH,
        [defines.entity_status.recharging_after_power_outage] = EntityStatus.RECHARGING_AFTER_POWER_OUTAGE,
        [defines.entity_status.turned_off_during_daytime] = EntityStatus.TURNED_OFF_DURING_DAYTIME,
        [defines.entity_status.waiting_for_source_items] = EntityStatus.WAITING_FOR_SOURCE_ITEMS,
        [defines.entity_status.waiting_for_space_in_destination] = EntityStatus.WAITING_FOR_SPACE_IN_DESTINATION,
        [defines.entity_status.waiting_for_target_to_be_built] = EntityStatus.WAITING_FOR_TARGET_TO_BE_BUILT,
        [defines.entity_status.waiting_for_train] = EntityStatus.WAITING_FOR_TRAIN,
        [defines.entity_status.waiting_to_launch_rocket] = EntityStatus.WAITING_TO_LAUNCH_ROCKET,
        [defines.entity_status.working] = EntityStatus.WORKING
    }
end
return ____exports
