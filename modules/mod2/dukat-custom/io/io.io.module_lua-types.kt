@file:JsQualifier("io")
@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")
package io

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
import LuaFile
import tsstdlib.Iterable

external fun close(file: LuaFile = definedExternally): Boolean

external fun flush(): Boolean

external fun input(file: String = definedExternally): LuaFile

external fun input(): LuaFile

external fun input(file: LuaFile = definedExternally): LuaFile

external fun <T : Array<dynamic /* "*n" | "*l" | "*a" | "*L" | Number */>> lines(filename: String = definedExternally, vararg formats: T): Iterable<Any /* Any & LuaExtension<String /* "__luaMultiReturnBrand" */> */> /* Iterable<Any /* Any & LuaExtension<String /* "__luaMultiReturnBrand" */> */> & LuaIterator<Any /* Any & LuaExtension<String /* "__luaMultiReturnBrand" */> */, undefined> & LuaExtension<String /* "__luaIterableBrand" */> */

external fun open(filename: String, mode: String = definedExternally): dynamic /* JsTuple<LuaFile> | JsTuple<Nothing?, String, Number> */

external fun output(file: String = definedExternally): LuaFile

external fun output(): LuaFile

external fun output(file: LuaFile = definedExternally): LuaFile

external fun popen(prog: String, mode: String /* "r" | "w" */ = definedExternally): dynamic /* JsTuple<LuaFile> | JsTuple<Nothing?, String> */

external fun read(): FileReadFormatToType<String /* "*l" */>?

external fun <T> read(format: T): FileReadFormatToType<T>? /* Any & LuaExtension<String /* "__luaMultiReturnBrand" */> */

external var stderr: LuaFile

external var stdin: LuaFile

external var stdout: LuaFile

external fun tmpfile(): LuaFile

external fun type(obj: Any): String /* "file" | "closed file" */

external fun write(vararg args: Any /* String | Number */): dynamic /* JsTuple<LuaFile> | JsTuple<Nothing?, String> */