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

external interface LuaMetatable<T, TIndex> {
//    val <T> __pairs: ((t: T) -> dynamic)?
//    val <T : Any?> __ipairs: ((t: T) -> dynamic)?
    fun <T> __pairs(t: T): dynamic
    fun <T : Any?> __ipairs(t: T): dynamic
    val __add: ((operand: Any) -> Any)?
    val __sub: ((operand: Any) -> Any)?
    val __mul: ((operand: Any) -> Any)?
    val __div: ((operand: Any) -> Any)?
    val __mod: ((operand: Any) -> Any)?
    val __pow: ((operand: Any) -> Any)?
    val __unm: ((operand: Any) -> Any)?
    val __concat: ((operand: Any) -> Any)?
    val __len: (() -> Any)?
    val __eq: ((operand: Any) -> Boolean)?
    val __lt: ((operand: Any) -> Boolean)?
    val __le: ((operand: Any) -> Boolean)?
    var __index: TIndex?
        get() = definedExternally
        set(value) = definedExternally
    var __newindex: dynamic /* Any? | ((this: T, key: Any, value: Any) -> Unit)? */
        get() = definedExternally
        set(value) = definedExternally
    val __call: ((args: Any) -> Any)?
    val __tostring: (() -> String)?
    var __mode: String? /* "k" | "v" | "kv" */
        get() = definedExternally
        set(value) = definedExternally
    var __metatable: Any?
        get() = definedExternally
        set(value) = definedExternally
    val __gc: (() -> Unit)?
}

external interface LuaMetatable__1<T> : LuaMetatable<T, dynamic /* Any? | ((this: T, key: Any) -> Any)? */>