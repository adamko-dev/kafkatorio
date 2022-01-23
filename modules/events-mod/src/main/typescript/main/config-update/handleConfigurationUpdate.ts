import {emitPacket} from "../emitKafkatorioPacket";
import {updateGlobal} from "./updateGlobal";
import {emitPrototypes} from "./prototypes";
import {Queue} from "../queue/queue";

export function handleConfigurationUpdate(changeData: ConfigurationChangedData) {
  updateGlobal()

  emitPacket<FactorioConfigurationUpdate>({
    modVersion: global.MOD_VERSION,
    packetType: "CONFIG",
    allMods: allMods(changeData),
    factorioData: factorioData(changeData)
  })

  Queue.reset()
  emitPrototypes()
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

    let currentVer = script.active_mods[modName] ?? e.mod_changes[modName].new_version ?? null
    let previousVer = e.mod_changes[modName]?.old_version ?? null

    return <FactorioModInfo>{
      modName: modName,
      currentVersion: currentVer,
      previousVersion: previousVer,
    }
  })

}
