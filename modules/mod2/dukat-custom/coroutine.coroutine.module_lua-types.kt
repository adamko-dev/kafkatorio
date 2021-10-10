@file:JsQualifier("coroutine")
@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")
package coroutine

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

external fun create(f: (args: Any) -> Any): LuaThread

external fun resume(co: LuaThread, vararg param_val: Any): dynamic /* JsTuple<Boolean, Any> | JsTuple<Boolean, String> */

external fun status(co: LuaThread): String /* "running" | "suspended" | "normal" | "dead" */

external fun wrap(f: (args: Any) -> Any): (args: Any) -> Array<Any>

external fun yield(vararg args: Any): Array<Any> /* Array<Any> & LuaExtension<String /* "__luaMultiReturnBrand" */> */

external fun running(): dynamic /* JsTuple<LuaThread, Boolean> */