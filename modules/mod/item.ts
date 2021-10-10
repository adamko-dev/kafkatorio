import * as util from "util";
import { Data, Mods } from "typed-factorio/data/types"


declare const data: Data
declare const mods: Mods

let fireArmour = util.table.deepcopy(data.raw["armor"]["heavy-armor"])

fireArmour.name = "fire-armour"
fireArmour.icon

