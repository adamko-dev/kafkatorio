local ____lualib = require("lualib_bundle")
local __TS__Class = ____lualib.__TS__Class
local __TS__ObjectKeys = ____lualib.__TS__ObjectKeys
local __TS__ArrayConcat = ____lualib.__TS__ArrayConcat
local Set = ____lualib.Set
local __TS__New = ____lualib.__TS__New
local __TS__Spread = ____lualib.__TS__Spread
local __TS__ArrayMap = ____lualib.__TS__ArrayMap
local ____exports = {}
local ____kafkatorio_2Dschema = require("generated.kafkatorio-schema")
local KafkatorioPacketData = ____kafkatorio_2Dschema.KafkatorioPacketData
local ____PacketEmitter = require("main.PacketEmitter")
local PacketEmitter = ____PacketEmitter.default
____exports.ConfigurationUpdateHandler = __TS__Class()
local ConfigurationUpdateHandler = ____exports.ConfigurationUpdateHandler
ConfigurationUpdateHandler.name = "ConfigurationUpdateHandler"
function ConfigurationUpdateHandler.prototype.____constructor(self)
end
function ConfigurationUpdateHandler.prototype.emitConfigurationUpdate(self, changeData)
    local configUpdateData = {
        type = KafkatorioPacketData.Type.ConfigurationUpdate,
        migrationApplied = changeData.migration_applied,
        modStartupSettingsChange = changeData.mod_startup_settings_changed,
        allMods = ____exports.ConfigurationUpdateHandler:allMods(changeData),
        factorioData = ____exports.ConfigurationUpdateHandler:factorioData(changeData)
    }
    PacketEmitter:emitInstantPacket(configUpdateData)
end
function ConfigurationUpdateHandler.factorioData(self, changeData)
    return {oldVersion = changeData.old_version or nil, newVersion = changeData.new_version or nil}
end
function ConfigurationUpdateHandler.allMods(self, e)
    local namesOfChangedMods = __TS__ObjectKeys(e.mod_changes)
    local namesOfCurrentMods = __TS__ObjectKeys(script.active_mods)
    local modNames = {__TS__Spread(__TS__New(
        Set,
        __TS__ArrayConcat(namesOfChangedMods, namesOfCurrentMods)
    ))}
    return __TS__ArrayMap(
        modNames,
        function(____, modName)
            local ____script_active_mods_modName_2 = script.active_mods[modName]
            if ____script_active_mods_modName_2 == nil then
                local ____e_mod_changes_modName_new_version_0 = e.mod_changes[modName]
                if ____e_mod_changes_modName_new_version_0 ~= nil then
                    ____e_mod_changes_modName_new_version_0 = ____e_mod_changes_modName_new_version_0.new_version
                end
                ____script_active_mods_modName_2 = ____e_mod_changes_modName_new_version_0
            end
            local currentVer = ____script_active_mods_modName_2 or nil
            local ____e_mod_changes_modName_old_version_3 = e.mod_changes[modName]
            if ____e_mod_changes_modName_old_version_3 ~= nil then
                ____e_mod_changes_modName_old_version_3 = ____e_mod_changes_modName_old_version_3.old_version
            end
            local previousVer = ____e_mod_changes_modName_old_version_3 or nil
            return {modName = modName, currentVersion = currentVer, previousVersion = previousVer}
        end
    )
end
local ConfigurationUpdates = __TS__New(____exports.ConfigurationUpdateHandler)
____exports.default = ConfigurationUpdates
return ____exports
