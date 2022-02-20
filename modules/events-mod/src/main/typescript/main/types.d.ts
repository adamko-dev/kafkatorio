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
