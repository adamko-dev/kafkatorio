import {Data, DoubleSettingDefinition} from "typed-factorio/settings/types"
import {KafkatorioKeyedPacketData, KafkatorioKeyedPacketTypes} from "../types";

export class KafkatorioSettingsConfig {


  private static eventCacheExpirationDefaultSeconds: Record<KafkatorioKeyedPacketTypes, double> = {
    "kafkatorio.packet.keyed.PlayerUpdate": 0.5,
    "kafkatorio.packet.keyed.MapChunkTileUpdate": 30,
    "kafkatorio.packet.keyed.MapChunkEntityUpdate": 30,
    "kafkatorio.packet.keyed.MapChunkResourceUpdate": 30,
    "kafkatorio.packet.keyed.EntityUpdate": 5,
  }


  public getEventCacheExpirationTicks(packet: KafkatorioKeyedPacketData): uint {
    const settingName = KafkatorioSettingsConfig.cacheDurationSettingName(packet.type)
    const seconds: double = settings.global[settingName].value as double
    return seconds * 60 // convert ticks to seconds
  }


  public initialiseSettings(data: Data): void {
    for (const [type,] of pairs(KafkatorioSettingsConfig.eventCacheExpirationDefaultSeconds)) {
      data.extend(
          [KafkatorioSettingsConfig.createDefaultCacheDuration(type)]
      )
    }
  }


  private static createDefaultCacheDuration(
      type: KafkatorioKeyedPacketTypes,
      defaultExpirationSeconds: uint = KafkatorioSettingsConfig.eventCacheExpirationDefaultSeconds[type],
  ): DoubleSettingDefinition {
    return {
      type: "double-setting",
      name: KafkatorioSettingsConfig.cacheDurationSettingName(type),
      setting_type: "runtime-global",
      default_value: defaultExpirationSeconds,
      // min: 0.25 seconds, max: 2 minutes
      minimum_value: 0.25,
      maximum_value: 120,
    }
  }


  private static cacheDurationSettingName(type: KafkatorioKeyedPacketTypes): string {
    return `kafkatorio:cache-expiration-seconds_${type.replaceAll(".", "-")}`
  }

}

const KafkatorioSettings = new KafkatorioSettingsConfig()

export default KafkatorioSettings
