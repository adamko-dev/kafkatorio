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
import tsstdlib.Iterable
import io.FileReadFormatToType

external interface LuaFile {
    fun close(): Boolean
    fun flush(): Boolean
    fun <T : Array<dynamic /* "*n" | "*l" | "*a" | "*L" | Number */>> lines(vararg formats: T): Iterable<Any /* Any & LuaExtension<String /* "__luaMultiReturnBrand" */> */> /* Iterable<Any /* Any & LuaExtension<String /* "__luaMultiReturnBrand" */> */> & LuaIterator<Any /* Any & LuaExtension<String /* "__luaMultiReturnBrand" */> */, undefined> & LuaExtension<String /* "__luaIterableBrand" */> */
    fun read(): FileReadFormatToType<String /* "*l" */>?
    fun <T> read(format: T): FileReadFormatToType<T>? /* Any & LuaExtension<String /* "__luaMultiReturnBrand" */> */
    fun seek(whence: String /* "set" | "cur" | "end" */ = definedExternally, offset: Number = definedExternally): dynamic /* JsTuple<Number> | JsTuple<Nothing?, String> */
    fun setvbuf(mode: String /* "no" | "full" | "line" */, size: Number = definedExternally)
    fun write(vararg args: Any /* String | Number */): dynamic /* JsTuple<LuaFile> | JsTuple<Nothing?, String> */
}