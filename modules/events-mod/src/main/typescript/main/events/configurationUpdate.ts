import PacketEmitter from "../PacketEmitter";
import {
  ConfigurationUpdateGameData,
  ConfigurationUpdateModData,
  KafkatorioPacketData
} from "../../generated/kafkatorio-schema";
import {initGlobal} from "../global-init";
import {emitPrototypes} from "./prototypeUpdates";
import Type = KafkatorioPacketData.Type;
import ConfigurationUpdate = KafkatorioPacketData.ConfigurationUpdate;


script.on_configuration_changed((data: ConfigurationChangedData) => {
  // runs whenever the version of the base game, or a mod, changes
  initGlobal(true)
  // KafkatorioSettings.loadSettings()
  emitConfigurationUpdate(data)
  emitPrototypes()
})


function emitConfigurationUpdate(changeData: ConfigurationChangedData) {

  const configUpdateData: ConfigurationUpdate = {
    type: Type.ConfigurationUpdate,
    migrationApplied: changeData.migration_applied,
    modStartupSettingsChange: changeData.mod_startup_settings_changed,
    allMods: allMods(changeData),
    factorioData: factorioData(changeData)
  }

  PacketEmitter.emitInstantPacket(configUpdateData)
}


function factorioData(changeData: ConfigurationChangedData): ConfigurationUpdateGameData {
  return {
    oldVersion: changeData.old_version ?? null,
    newVersion: changeData.new_version ?? null,
  }
}


function allMods(e: ConfigurationChangedData): ConfigurationUpdateModData[] {

  let namesOfChangedMods: string[] = Object.keys(e.mod_changes)
  let namesOfCurrentMods: string[] = Object.keys(script.active_mods)

  let modNames = [...new Set<string>(
      namesOfChangedMods.concat(namesOfCurrentMods)
  )]

  return modNames.map((modName) => {

    let currentVer = script.active_mods[modName] ?? e.mod_changes[modName]?.new_version ?? null
    let previousVer = e.mod_changes[modName]?.old_version ?? null

    return {
      modName: modName,
      currentVersion: currentVer,
      previousVersion: previousVer,
    }
  })

}