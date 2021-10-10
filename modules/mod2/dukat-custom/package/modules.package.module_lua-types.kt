@file:JsQualifier("package")
@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")
package `package`

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

external var config: String

external var cpath: String

external var loaded: Record<String, Any>

external fun loadlib(libname: String, funcname: String): dynamic /* JsTuple<Function<*>> | JsTuple<Nothing?, String, String> */

external var path: String

external var preload: Record<String, (modname: String, fileName: String) -> Any>

external fun searchpath(name: String, path: String, sep: String = definedExternally, rep: String = definedExternally): String

external var searchers: Array<dynamic /* (modname: String) -> dynamic | (modname: String) -> dynamic | String */>