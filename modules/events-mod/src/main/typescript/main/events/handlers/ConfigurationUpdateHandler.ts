import {
  ConfigurationUpdateGameData,
  ConfigurationUpdateModData,
  KafkatorioPacketData
} from "../../../generated/kafkatorio-schema";
import PacketEmitter from "../../emitting/PacketEmitter";
import ConfigurationUpdate = KafkatorioPacketData.ConfigurationUpdate;
import KafkatorioPacketQueue from "../../emitting/KafkatorioPacketQueue";


export class ConfigurationUpdateHandler {


  emitConfigurationUpdate(changeData: ConfigurationChangedData) {

    const configUpdateData: ConfigurationUpdate = {
      type: KafkatorioPacketData.Type.ConfigurationUpdate,
      migrationApplied: changeData.migration_applied,
      modStartupSettingsChange: changeData.mod_startup_settings_changed,
      allMods: ConfigurationUpdateHandler.allMods(changeData),
      factorioData: ConfigurationUpdateHandler.factorioData(changeData)
    }

    KafkatorioPacketQueue.enqueue(configUpdateData)
  }


  private static factorioData(changeData: ConfigurationChangedData): ConfigurationUpdateGameData {
    return {
      oldVersion: changeData.old_version ?? null,
      newVersion: changeData.new_version ?? null,
    }
  }


  private static allMods(e: ConfigurationChangedData): ConfigurationUpdateModData[] {

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

}

const ConfigurationUpdates = new ConfigurationUpdateHandler()

export default ConfigurationUpdates
