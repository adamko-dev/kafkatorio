export function updateGlobal() {
  global.MOD_VERSION = script.active_mods[script.mod_name][1]
  global.FACTORIO_VERSION = script.active_mods["base"][1]
}
