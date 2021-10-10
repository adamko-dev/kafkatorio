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

typealias AnyTable = Record<Any, Any>

external interface AnyNotNil

typealias LuaExtension<TBrand> = Any

//external var `$multi`: (values: T) -> T /* (values: T) -> T & LuaExtension<String /* "__luaMultiFunctionBrand" */> */
external fun <T> `$multi`(values: T): T /* (values: T) -> T & LuaExtension<String /* "__luaMultiFunctionBrand" */> */

external var `$range`: (start: Number, limit: Number, step: Number) -> Iterable<Number> /* (start: Number, limit: Number, step: Number) -> Iterable<Number> & LuaExtension<String /* "__luaRangeFunctionBrand" */> */

external var `$vararg`: Array<String> /* Array<String> & LuaExtension<String /* "__luaVarargConstantBrand" */> */

typealias LuaIterator<TValue, TState> = Any

@Suppress("EXTERNAL_DELEGATION", "NESTED_CLASS_IN_EXTERNAL_INTERFACE")
external interface LuaTable<TKey : AnyNotNil, TValue> {
    var length: () -> Number /* () -> Number & LuaExtension<String /* "__luaLengthMethodBrand" */> */
    var get: (key: TKey) -> TValue /* (key: TKey) -> TValue & LuaExtension<String /* "__luaTableGetMethodBrand" */> */
    var set: (key: TKey, value: TValue) -> Unit /* (key: TKey, value: TValue) -> Unit & LuaExtension<String /* "__luaTableSetMethodBrand" */> */
    var has: (key: TKey) -> Boolean /* (key: TKey) -> Boolean & LuaExtension<String /* "__luaTableHasMethodBrand" */> */
    var delete: (key: TKey) -> Boolean /* (key: TKey) -> Boolean & LuaExtension<String /* "__luaTableDeleteMethodBrand" */> */

//    companion object : Any by definedExternally
}