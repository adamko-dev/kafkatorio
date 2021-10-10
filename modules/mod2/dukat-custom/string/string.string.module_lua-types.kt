@file:JsQualifier("string")
@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")
package string

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
import tsstdlib.Iterable
import tsstdlib.Record

external fun byte(s: String, i: Number = definedExternally): dynamic /* Number | Array */

external fun byte(s: String): dynamic /* Number | Array */

external fun byte(s: String, i: Number = definedExternally, j: Number = definedExternally): Array<Number> /* Array<Number> & LuaExtension<String /* "__luaMultiReturnBrand" */> */

external fun char(vararg args: Number): String

external fun dump(func: Function<*>): String

external fun find(s: String, pattern: String, init: Number = definedExternally, plain: Boolean = definedExternally): dynamic /* JsTuple<Number, Number, Any> | JsTuple<> */

external fun format(formatstring: String, vararg args: Any): String

external fun gmatch(s: String, pattern: String): Iterable<Array<String> /* Array<String> & LuaExtension<String /* "__luaMultiReturnBrand" */> */> /* Iterable<Array<String> /* Array<String> & LuaExtension<String /* "__luaMultiReturnBrand" */> */> & LuaIterator<Array<String> /* Array<String> & LuaExtension<String /* "__luaMultiReturnBrand" */> */, undefined> & LuaExtension<String /* "__luaIterableBrand" */> */

external fun gsub(s: String, pattern: String, repl: String, n: Number = definedExternally): dynamic /* JsTuple<String, Number> */

external fun gsub(s: String, pattern: String, repl: String): dynamic /* JsTuple<String, Number> */

external fun gsub(s: String, pattern: String, repl: Record<String, String>, n: Number = definedExternally): dynamic /* JsTuple<String, Number> */

external fun gsub(s: String, pattern: String, repl: Record<String, String>): dynamic /* JsTuple<String, Number> */

external fun gsub(s: String, pattern: String, repl: (matches: String) -> String, n: Number = definedExternally): dynamic /* JsTuple<String, Number> */

external fun gsub(s: String, pattern: String, repl: (matches: String) -> String): dynamic /* JsTuple<String, Number> */

external fun len(s: String): Number

external fun lower(s: String): String

external fun match(s: String, pattern: String, init: Number = definedExternally): Array<String> /* Array<String> & LuaExtension<String /* "__luaMultiReturnBrand" */> */

external fun rep(s: String, n: Number): String

external fun reverse(s: String): String

external fun sub(s: String, i: Number, j: Number = definedExternally): String

external fun upper(s: String): String

external fun rep(s: String, n: Number, sep: String = definedExternally): String