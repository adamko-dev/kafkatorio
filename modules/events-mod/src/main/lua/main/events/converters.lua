local ____exports = {}
____exports.Converters = {}
local Converters = ____exports.Converters
do
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
end
return ____exports
