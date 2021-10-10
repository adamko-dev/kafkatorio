@file:JsQualifier("table")
@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")
package table2

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

external fun concat(list: Array<Any /* String | Number */>, sep: String = definedExternally, i: Number = definedExternally, j: Number = definedExternally): String

external fun <T> insert(list: Array<T>, value: T)

external fun <T> insert(list: Array<T>, pos: Number, value: T)

external fun <T> remove(list: Array<T>, pos: Number = definedExternally): T?

external fun <T> sort(list: Array<T>, comp: (a: T, b: T) -> Boolean = definedExternally)

external fun <T : Array<Any>> unpack(list: T): T /* T & LuaExtension<String /* "__luaMultiReturnBrand" */> */

external fun <T> unpack(list: Array<T>, i: Number, j: Number = definedExternally): Array<T> /* Array<T> & LuaExtension<String /* "__luaMultiReturnBrand" */> */

external fun <T> unpack(list: Array<T>, i: Number): Array<T> /* Array<T> & LuaExtension<String /* "__luaMultiReturnBrand" */> */

external interface `T$92` {
    var n: Number
}

external fun <T : Array<Any>> pack(vararg args: T): T /* T & `T$92` */