local ____lualib = require("lualib_bundle")
local __TS__Class = ____lualib.__TS__Class
local __TS__StringReplaceAll = ____lualib.__TS__StringReplaceAll
local __TS__New = ____lualib.__TS__New
local ____exports = {}
____exports.KafkatorioSettingsConfig = __TS__Class()
local KafkatorioSettingsConfig = ____exports.KafkatorioSettingsConfig
KafkatorioSettingsConfig.name = "KafkatorioSettingsConfig"
function KafkatorioSettingsConfig.prototype.____constructor(self)
end
function KafkatorioSettingsConfig.prototype.getEventCacheExpirationTicks(self, packet)
    local settingName = ____exports.KafkatorioSettingsConfig:cacheDurationSettingName(packet.type)
    local seconds = settings.global[settingName].value
    return seconds * 60
end
function KafkatorioSettingsConfig.prototype.initialiseSettings(self, data)
    for ____type in pairs(____exports.KafkatorioSettingsConfig.eventCacheExpirationDefaultSeconds) do
        data:extend({____exports.KafkatorioSettingsConfig:createDefaultCacheDuration(____type)})
    end
end
function KafkatorioSettingsConfig.createDefaultCacheDuration(self, ____type, defaultExpirationSeconds)
    if defaultExpirationSeconds == nil then
        defaultExpirationSeconds = ____exports.KafkatorioSettingsConfig.eventCacheExpirationDefaultSeconds[____type]
    end
    return {
        type = "double-setting",
        name = ____exports.KafkatorioSettingsConfig:cacheDurationSettingName(____type),
        setting_type = "runtime-global",
        default_value = defaultExpirationSeconds,
        minimum_value = 0.25,
        maximum_value = 120
    }
end
function KafkatorioSettingsConfig.cacheDurationSettingName(self, ____type)
    return "kafkatorio:cache-expiration-seconds_" .. __TS__StringReplaceAll(____type, ".", "-")
end
KafkatorioSettingsConfig.eventCacheExpirationDefaultSeconds = {["kafkatorio.packet.keyed.PlayerUpdate"] = 0.5, ["kafkatorio.packet.keyed.MapChunkUpdate"] = 30, ["kafkatorio.packet.keyed.EntityUpdate"] = 5}
local KafkatorioSettings = __TS__New(____exports.KafkatorioSettingsConfig)
____exports.default = KafkatorioSettings
return ____exports
