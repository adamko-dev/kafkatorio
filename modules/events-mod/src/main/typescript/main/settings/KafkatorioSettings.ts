import {Data, DoubleSettingDefinition} from "typed-factorio/settings/types"
import {KafkatorioKeyedPacketData, KafkatorioKeyedPacketTypes} from "../types";
import {KafkatorioPacketData} from "../../generated/kafkatorio-schema/kafkatorio-schema";

export class KafkatorioSettingsConfig {


  private static eventCacheExpirationDefaultSeconds: Record<KafkatorioKeyedPacketTypes, double> = {
    "kafkatorio.packet.keyed.PlayerUpdate": 0.5,
    "kafkatorio.packet.keyed.MapChunkUpdate": 30,
    "kafkatorio.packet.keyed.EntityUpdate": 5,
  }


  private eventCacheExpirationTicks: Record<KafkatorioKeyedPacketTypes, double> = {
    "kafkatorio.packet.keyed.PlayerUpdate":
        KafkatorioSettingsConfig.eventCacheExpirationDefaultSeconds[KafkatorioPacketData.Type.PlayerUpdate],
    "kafkatorio.packet.keyed.MapChunkUpdate":
        KafkatorioSettingsConfig.eventCacheExpirationDefaultSeconds[KafkatorioPacketData.Type.MapChunkUpdate],
    "kafkatorio.packet.keyed.EntityUpdate":
        KafkatorioSettingsConfig.eventCacheExpirationDefaultSeconds[KafkatorioPacketData.Type.EntityUpdate],
  }

  public getEventCacheExpirationTicks(packet: KafkatorioKeyedPacketData): uint {
    return this.eventCacheExpirationTicks[packet.type]
  }

  private static types: KafkatorioKeyedPacketTypes[] = [
    KafkatorioPacketData.Type.PlayerUpdate,
    KafkatorioPacketData.Type.MapChunkUpdate,
    KafkatorioPacketData.Type.EntityUpdate,
  ]

  public loadSettings(): void {
    for (const type of KafkatorioSettingsConfig.types) {
      const seconds: double = settings.global[KafkatorioSettingsConfig.cacheDurationSettingName(type)].value as double
      this.eventCacheExpirationTicks[type] = seconds * 60 // convert ticks to seconds
    }
  }


  public initialiseSettings(data: Data): void {
    const settings = KafkatorioSettingsConfig.types.map(
        type => KafkatorioSettingsConfig.createDefaultCacheDuration(type)
    )
    data.extend(settings)
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
    return `kafkatorio_event-cache_default-expiration-duration-seconds_${type.replaceAll(".", "-")}`
  }

}

const KafkatorioSettings = new KafkatorioSettingsConfig()

export default KafkatorioSettings
