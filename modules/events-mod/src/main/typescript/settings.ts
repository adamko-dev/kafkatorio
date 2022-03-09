import {Data, Mods} from "typed-factorio/settings/types"
import KafkatorioSettings from "./main/settings/KafkatorioSettings";

declare const data: Data
declare const mods: Mods

KafkatorioSettings.initialiseSettings(data)
