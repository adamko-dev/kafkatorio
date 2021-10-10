@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

import kotlin.js.*
import org.khronos.webgl.*
import org.w3c.dom.*
import org.w3c.dom.events.*
import org.w3c.dom.parsing.*
import org.w3c.dom.svg.*
import org.w3c.dom.url.*
import org.w3c.fetch.*
import org.w3c.files.*
import org.w3c.notifications.*
import org.w3c.performance.*
import org.w3c.workers.*
import org.w3c.xhr.*
import tsstdlib.Record
import tsstdlib.Iterable

external interface LuaThread {
    var __internal__: Any
}

external interface LuaUserdata {
    var __internal__: Any
}

external var _VERSION: String /* "Lua 5.1" | "Lua 5.2" | "Lua 5.3" | "Lua 5.4" */

external var _G: Any

external fun <V> assert(v: V): Any

external fun <V, A : Array<Any>> assert(v: V, vararg args: A): dynamic /* JsTuple<Exclude<V, Boolean?>, Any> */

external fun collectgarbage(opt: String /* "collect" | "stop" | "restart" */ = definedExternally)

external fun collectgarbage()

external fun collectgarbage(opt: String /* "setpause" | "setstepmul" | "step" */, arg: Number): dynamic /* Number | Boolean */

external fun dofile(filename: String = definedExternally): Any

external fun error(message: String, level: Number = definedExternally): Any

external fun <T> getmetatable(obj: T): LuaMetatable__1<T>?

external fun <T> ipairs(t: Record<Number, T>): Iterable<dynamic /* JsTuple<Number, NonNullable<T>> */> /* Iterable<dynamic /* JsTuple<Number, NonNullable<T>> */> & LuaIterator<dynamic /* JsTuple<Number, NonNullable<T>> */, undefined> & LuaExtension<String /* "__luaIterableBrand" */> */

external fun next(table: Any?, index: Any = definedExternally): dynamic /* JsTuple<Any, Any> | JsTuple<> */

external fun <TKey, TValue> pairs(t: LuaTable<TKey, TValue>): Iterable<dynamic /* JsTuple<TKey, NonNullable<TValue>> */> /* Iterable<dynamic /* JsTuple<TKey, NonNullable<TValue>> */> & LuaIterator<dynamic /* JsTuple<TKey, NonNullable<TValue>> */, undefined> & LuaExtension<String /* "__luaIterableBrand" */> */

external fun <T> pairs(t: T): Iterable<dynamic /* JsTuple<Any, NonNullable<Any>> */> /* Iterable<dynamic /* JsTuple<Any, NonNullable<Any>> */> & LuaIterator<dynamic /* JsTuple<Any, NonNullable<Any>> */, undefined> & LuaExtension<String /* "__luaIterableBrand" */> */

external fun <This, Args : Array<Any>, R> pcall(f: (self: This, args: Args) -> R, context: This, vararg args: Args): dynamic /* JsTuple<Boolean, R> | JsTuple<Boolean, String> */

external fun <A : Array<Any>, R> pcall(f: (self: Unit, args: A) -> R, vararg args: A): dynamic /* JsTuple<Boolean, R> | JsTuple<Boolean, String> */

external fun print(vararg args: Any)

external fun <T> rawequal(v1: T, v2: T): Boolean

external fun <T : Any?, K : Any> rawget(table: T, index: K): Any

external fun rawlen(v: Any?): Number

external fun rawlen(v: String?): Number

external fun <T : Any?, K : Any> rawset(table: T, index: K, value: Any): T

external fun <T> select(index: Number, vararg args: T): Array<T> /* Array<T> & LuaExtension<String /* "__luaMultiReturnBrand" */> */

external fun <T> select(index: String /* "#" */, vararg args: T): Number

external fun <T : Any?, TIndex> setmetatable(table: T, metatable: LuaMetatable<T, TIndex>? = definedExternally): Any

external fun tonumber(e: Any, base: Number = definedExternally): Number?

external fun tostring(v: Any): String

external fun type(v: Any): String /* "nil" | "number" | "string" | "boolean" | "table" | "function" | "thread" | "userdata" */