@file:JsQualifier("os")
@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")
package os

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
import LuaDateInfo

external fun clock(): Number

external fun date(format: String /* "*t" */ = definedExternally, time: Number = definedExternally): dynamic /* String | LuaDateInfoResult */

external fun date(): String

external fun date(format: String /* "*t" */ = definedExternally): dynamic /* String | LuaDateInfoResult */

external fun difftime(t1: Number, t2: Number): Number

external fun getenv(varname: String): String?

external fun remove(filename: String): dynamic /* JsTuple<Boolean> | JsTuple<Nothing?, String> */

external fun rename(oldname: String, newname: String): dynamic /* JsTuple<Boolean> | JsTuple<Nothing?, String> */

external fun setlocale(locale: String = definedExternally, category: String /* "all" | "collate" | "ctype" | "monetary" | "numeric" | "time" */ = definedExternally): String?

external fun time(): Number

external fun time(table: LuaDateInfo): Number

external fun tmpname(): String

external fun execute(): Boolean

external fun execute(command: String): dynamic /* JsTuple<Boolean?, String, Number> */

external fun exit(code: Boolean = definedExternally, close: Boolean = definedExternally): Any

external fun exit(): Any

external fun exit(code: Boolean = definedExternally): Any

external fun exit(code: Number = definedExternally, close: Boolean = definedExternally): Any

external fun exit(code: Number = definedExternally): Any