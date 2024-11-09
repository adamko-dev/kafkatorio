import KafkatorioSettings from "./main/settings/KafkatorioSettings";
import {Mods} from "factorio:prototype";
import {SettingsData} from "factorio:common";

declare const data: SettingsData
declare const mods: Mods

KafkatorioSettings.initialiseSettings(data)
