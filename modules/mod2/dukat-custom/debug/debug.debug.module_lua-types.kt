@file:JsQualifier("debug")
@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")
package debug

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
import LuaThread
import LuaMetatable__1
import tsstdlib.Record
import LuaUserdata
import LuaMetatable

external fun debug()

external fun gethook(thread: LuaThread = definedExternally): dynamic /* JsTuple<Nothing?, Number> | JsTuple<Function<*>, Number, Any> */

external interface FunctionInfo<T : Function<*>> {
    var nparams: Number
    var isvararg: Boolean?
    var istailcall: Boolean?
    var func: T
    var name: String?
        get() = definedExternally
        set(value) = definedExternally
    var namewhat: String /* "global" | "local" | "method" | "field" | "" */
    var source: String
    var short_src: String
    var linedefined: Number
    var lastlinedefined: Number
    var what: String /* "Lua" | "C" | "main" */
    var currentline: Number
    var nups: Number
}

external interface FunctionInfoPartial<T : Function<*>> {
    var nparams: Number?
        get() = definedExternally
        set(value) = definedExternally
    var isvararg: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var istailcall: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var func: T?
        get() = definedExternally
        set(value) = definedExternally
    var name: String?
        get() = definedExternally
        set(value) = definedExternally
    var namewhat: String? /* "global" | "local" | "method" | "field" | "" */
        get() = definedExternally
        set(value) = definedExternally
    var source: String?
        get() = definedExternally
        set(value) = definedExternally
    var short_src: String?
        get() = definedExternally
        set(value) = definedExternally
    var linedefined: Number?
        get() = definedExternally
        set(value) = definedExternally
    var lastlinedefined: Number?
        get() = definedExternally
        set(value) = definedExternally
    var what: String? /* "Lua" | "C" | "main" */
        get() = definedExternally
        set(value) = definedExternally
    var currentline: Number?
        get() = definedExternally
        set(value) = definedExternally
    var nups: Number?
        get() = definedExternally
        set(value) = definedExternally
}

external interface FunctionInfo__0 : FunctionInfo<Function<*>>

external interface FunctionInfo__0Partial : FunctionInfoPartial<Function<*>>

external fun <T : Function<*>> getinfo(f: T): FunctionInfo<T>

external fun <T : Function<*>> getinfo(f: T, what: String): FunctionInfoPartial<T>

external fun <T : Function<*>> getinfo(thread: LuaThread, f: T): FunctionInfo<T>

external fun <T : Function<*>> getinfo(thread: LuaThread, f: T, what: String): FunctionInfoPartial<T>

external fun getinfo(f: Number): FunctionInfo__0?

external fun getinfo(f: Number, what: String): FunctionInfo__0Partial?

external fun getinfo(thread: LuaThread, f: Number): FunctionInfo__0?

external fun getinfo(thread: LuaThread, f: Number, what: String): FunctionInfo__0Partial?

external fun <T : Any> getmetatable(value: T): LuaMetatable__1<T>?

external fun getregistry(): Record<String, Any>

external fun getupvalue(f: Function<*>, up: Number): dynamic /* JsTuple<String, Any> | JsTuple<> */

external fun getuservalue(u: LuaUserdata): Any

external fun sethook()

external fun sethook(hook: (event: String /* "call" | "return" | "line" | "count" */, line: Number) -> Any, mask: String, count: Number = definedExternally)

external fun sethook(hook: (event: String /* "call" | "return" | "line" | "count" */, line: Number) -> Any, mask: String)

external fun sethook(thread: LuaThread, hook: (event: String /* "call" | "return" | "line" | "count" */, line: Number) -> Any, mask: String, count: Number = definedExternally)

external fun sethook(thread: LuaThread, hook: (event: String /* "call" | "return" | "line" | "count" */, line: Number) -> Any, mask: String)

external fun setlocal(level: Number, local: Number, value: Any): String?

external fun setlocal(thread: LuaThread, level: Number, local: Number, value: Any): String?

external fun <T : Any?, TIndex> setmetatable(value: T, table: LuaMetatable<T, TIndex>? = definedExternally): Any

external fun setupvalue(f: Function<*>, up: Number, value: Any): String?

external fun setuservalue(udata: LuaUserdata, value: Any): LuaUserdata

external fun traceback(message: String? = definedExternally, level: Number? = definedExternally): String

external fun traceback(): String

external fun traceback(message: String? = definedExternally): String

external fun traceback(thread: LuaThread = definedExternally, message: String? = definedExternally, level: Number? = definedExternally): String

external fun traceback(thread: LuaThread = definedExternally): String

external fun traceback(thread: LuaThread = definedExternally, message: String? = definedExternally): String

external fun <T> traceback(message: T): T

external fun <T> traceback(thread: LuaThread, message: T): T

external fun getlocal(f: Function<*>, local: Number): dynamic /* JsTuple<String, Any> */

external fun getlocal(f: Number, local: Number): dynamic /* JsTuple<String, Any> */

external fun getlocal(thread: LuaThread, f: Function<*>, local: Number): dynamic /* JsTuple<String, Any> */

external fun getlocal(thread: LuaThread, f: Number, local: Number): dynamic /* JsTuple<String, Any> */

external fun upvalueid(f: Function<*>, n: Number): LuaUserdata

external fun upvaluejoin(f1: Function<*>, n1: Number, f2: Function<*>, n2: Number)