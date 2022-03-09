import {Data, DoubleSettingDefinition} from "typed-factorio/settings/types"

export class KafkatorioSettingsConfig {


  private static eventCacheExpirationDefaultSeconds: Record<FactorioEventUpdateType, uint> = {
    "PLAYER": 0.25,
    "MAP_CHUNK": 30,
    "ENTITY": 5,
  }


  private eventCacheExpirationTicks: Record<FactorioEventUpdateType, double> = {
    "PLAYER": KafkatorioSettingsConfig.eventCacheExpirationDefaultSeconds["PLAYER"],
    "MAP_CHUNK": KafkatorioSettingsConfig.eventCacheExpirationDefaultSeconds["MAP_CHUNK"],
    "ENTITY": KafkatorioSettingsConfig.eventCacheExpirationDefaultSeconds["ENTITY"],
  }


  public getEventCacheExpirationTicks(type: FactorioEventUpdateType): uint {
    return this.eventCacheExpirationTicks[type]
  }


  loadSettings(): void {
    const types: FactorioEventUpdateType[] = ["PLAYER", "MAP_CHUNK", "ENTITY"]
    for (const type of types) {
      const seconds: double = settings.global[KafkatorioSettingsConfig.cacheDurationSettingName(type)].value as double
      this.eventCacheExpirationTicks[type] = seconds * 60 // convert ticks to seconds
    }
  }


  initialiseSettings(data: Data): void {
    data.extend(
        [
          this.createDefaultCacheDuration("PLAYER"),
          this.createDefaultCacheDuration("MAP_CHUNK"),
          this.createDefaultCacheDuration("ENTITY"),
        ]
    )
  }


  private createDefaultCacheDuration(
      type: FactorioEventUpdateType,
      defaultExpirationSeconds: uint = KafkatorioSettingsConfig.eventCacheExpirationDefaultSeconds[type],
  ): DoubleSettingDefinition {
    return <DoubleSettingDefinition>{
      type: "double-setting",
      name: KafkatorioSettingsConfig.cacheDurationSettingName(type),
      setting_type: "runtime-global",
      default_value: defaultExpirationSeconds,
      // min: 0.25 seconds, max: 2 minutes
      minimum_value: 0.25,
      maximum_value: 120,
    }
  }


  private static cacheDurationSettingName(type: FactorioEventUpdateType): string {
    return `kafkatorio_event-cache_default-expiration-duration-seconds_${type}`
  }


}


const KafkatorioSettings = new KafkatorioSettingsConfig()
export default KafkatorioSettings
