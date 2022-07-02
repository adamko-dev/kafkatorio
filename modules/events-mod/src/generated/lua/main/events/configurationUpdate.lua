local ____lualib = require("lualib_bundle")
local __TS__ObjectKeys = ____lualib.__TS__ObjectKeys
local __TS__ArrayConcat = ____lualib.__TS__ArrayConcat
local Set = ____lualib.Set
local __TS__New = ____lualib.__TS__New
local __TS__Spread = ____lualib.__TS__Spread
local __TS__ArrayMap = ____lualib.__TS__ArrayMap
local ____exports = {}
local emitConfigurationUpdate, factorioData, allMods, Type
local ____PacketEmitter = require("main.PacketEmitter")
local PacketEmitter = ____PacketEmitter.default
local ____kafkatorio_2Dschema = require("generated.kafkatorio-schema")
local KafkatorioPacketData = ____kafkatorio_2Dschema.KafkatorioPacketData
local ____global_2Dinit = require("main.global-init")
local initGlobal = ____global_2Dinit.initGlobal
local ____prototypeUpdates = require("main.events.prototypeUpdates")
local emitPrototypes = ____prototypeUpdates.emitPrototypes
function emitConfigurationUpdate(changeData)
    local configUpdateData = {
        type = Type.ConfigurationUpdate,
        migrationApplied = changeData.migration_applied,
        modStartupSettingsChange = changeData.mod_startup_settings_changed,
        allMods = allMods(changeData),
        factorioData = factorioData(changeData)
    }
    PacketEmitter:emitInstantPacket(configUpdateData)
end
function factorioData(changeData)
    return {oldVersion = changeData.old_version or nil, newVersion = changeData.new_version or nil}
end
function allMods(e)
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
Type = KafkatorioPacketData.Type
script.on_configuration_changed(function(data)
    initGlobal(true)
    emitConfigurationUpdate(data)
    emitPrototypes()
end)
return ____exports
