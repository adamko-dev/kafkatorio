// declare interface LuaArray<TValue = any> extends LuaTable<number, TValue> {
// }

// = LuaTable<number, TValue>;

// declare type LuaArrayConstructor = (new <TValue = any>() => LuaArray<TValue>)

// declare const LuaArray: LuaArrayConstructor

// class LuaArray<TValue = any> extends LuaTable<number, TValue> {
//   add(val: TValue) {
//     this.set(this.length(), val)
//   }
// }

// declare type LuaArray<TKey extends AnyNotNil = AnyNotNil, TValue = any> & LuaTable<TKey, TValue>