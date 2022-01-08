import {emitPacket} from "../emitKafkatorioPacket";
import {convertPrototypes} from "../../prototypes/emitPrototypes";

export function handleConfigurationUpdate(changeData: ConfigurationChangedData) {

  emitPacket<FactorioConfigurationUpdate>({
    modVersion: global.MOD_VERSION,
    allMods: allMods(changeData),
    factorioData: factorioData(changeData),
    prototypes: convertPrototypes()
  })

}

function factorioData(changeData: ConfigurationChangedData): FactorioGameDataUpdate {
  return {
    oldVersion: changeData.old_version ?? null,
    newVersion: changeData.new_version ?? null,
  }
}

function allMods(e: ConfigurationChangedData): FactorioModInfo[] {

  let namesOfChangedMods: string[] = Object.keys(e.mod_changes)
  let namesOfCurrentMods: string[] = Object.keys(script.active_mods)

  let modNames = [...new Set<string>(
      namesOfChangedMods.concat(namesOfCurrentMods)
  )]

  return modNames.map((modName) => {

    let currentVer = script.active_mods[modName] ?? e.mod_changes[modName].new_version
    let previousVer = e.mod_changes[modName].old_version

    return <FactorioModInfo>{
      modName: modName,
      currentVersion: currentVer,
      previousVersion: previousVer,
    }
  })

}
