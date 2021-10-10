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

external fun load(chunk: String, chunkname: String = definedExternally, mode: String /* "b" | "t" | "bt" */ = definedExternally, env: Any? = definedExternally): dynamic /* JsTuple<() -> Any> | JsTuple<Nothing?, String> */

external fun load(chunk: String): dynamic /* JsTuple<() -> Any> | JsTuple<Nothing?, String> */

external fun load(chunk: String, chunkname: String = definedExternally): dynamic /* JsTuple<() -> Any> | JsTuple<Nothing?, String> */

external fun load(chunk: String, chunkname: String = definedExternally, mode: String /* "b" | "t" | "bt" */ = definedExternally): dynamic /* JsTuple<() -> Any> | JsTuple<Nothing?, String> */

external fun load(chunk: () -> String?, chunkname: String = definedExternally, mode: String /* "b" | "t" | "bt" */ = definedExternally, env: Any? = definedExternally): dynamic /* JsTuple<() -> Any> | JsTuple<Nothing?, String> */

external fun load(chunk: () -> String?): dynamic /* JsTuple<() -> Any> | JsTuple<Nothing?, String> */

external fun load(chunk: () -> String?, chunkname: String = definedExternally): dynamic /* JsTuple<() -> Any> | JsTuple<Nothing?, String> */

external fun load(chunk: () -> String?, chunkname: String = definedExternally, mode: String /* "b" | "t" | "bt" */ = definedExternally): dynamic /* JsTuple<() -> Any> | JsTuple<Nothing?, String> */

external fun loadfile(filename: String = definedExternally, mode: String /* "b" | "t" | "bt" */ = definedExternally, env: Any? = definedExternally): dynamic /* JsTuple<() -> Any> | JsTuple<Nothing?, String> */

external fun <This, Args : Array<Any>, R, E> xpcall(f: (self: This, args: Args) -> R, msgh: (self: Unit, err: Any) -> E, context: This, vararg args: Args): dynamic /* JsTuple<Boolean, R> | JsTuple<Boolean, E> */

external fun <Args : Array<Any>, R, E> xpcall(f: (self: Unit, args: Args) -> R, msgh: (err: Any) -> E, vararg args: Args): dynamic /* JsTuple<Boolean, R> | JsTuple<Boolean, E> */