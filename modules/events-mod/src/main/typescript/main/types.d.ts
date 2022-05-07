import {KafkatorioPacketData} from "../generated/kafkatorio-schema/kafkatorio-schema";

export type EventName = keyof typeof defines.events


/** Get all keys of `TYPE` that are not nullable. */
type NonNullKeys<TYPE> = {
  [KEY in keyof TYPE]: null extends Exclude<TYPE[KEY], undefined> ? never : KEY
}[keyof TYPE];


/** Exclude all nullable keys from `TYPE`. */
type ExcludeNullKeys<TYPE> = Pick<TYPE, NonNullKeys<TYPE>>


/** Make all keys of `TYPE` non-nullable. */
type NonNullableKeys<TYPE> = {
  [KEY in keyof TYPE]: NonNullable<TYPE[KEY]>;
}


type KafkatorioKeyedPacketData = Extract<KafkatorioPacketData, { key: object }>

type KafkatorioKeyedPacketTypes = KafkatorioKeyedPacketData["type"];

type KafkatorioKeyedPacketKeys = KafkatorioKeyedPacketData["key"];


type WithRequired<T, K extends keyof T> = T & { [P in K]-?: T[P] }
