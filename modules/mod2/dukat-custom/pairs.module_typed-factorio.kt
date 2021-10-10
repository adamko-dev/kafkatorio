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

external fun <V> pairs(table: LuaCustomTableMembers /* LuaCustomTableMembers & CustomTableIndex<dynamic /* Number | String */, V> */): Iterable<dynamic /* JsTuple<Number, V> */> /* Iterable<dynamic /* JsTuple<Number, V> */> & LuaIterator<dynamic /* JsTuple<Number, V> */, undefined> & LuaExtension<String /* "__luaIterableBrand" */> */

external fun <K : Any, V> pairs(table: LuaCustomTableMembers /* LuaCustomTableMembers & CustomTableIndex<K, V> */): Iterable<dynamic /* JsTuple<K, V> */> /* Iterable<dynamic /* JsTuple<K, V> */> & LuaIterator<dynamic /* JsTuple<K, V> */, undefined> & LuaExtension<String /* "__luaIterableBrand" */> */

external fun ipairs(table: LuaCustomTableMembers /* LuaCustomTableMembers & CustomTableIndex<Any, Any> */): Any