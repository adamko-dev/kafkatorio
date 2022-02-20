declare const global: {
  MOD_VERSION: string
  FACTORIO_VERSION: string
}

export type EventName = keyof typeof defines.events

type NonNullKeys<T> = {
  [P in keyof T]: null extends  Exclude<T[P], undefined>  ? never : P
}[keyof T];

type ExcludeNullKeys<T> = Pick<T, NonNullKeys<T>>
