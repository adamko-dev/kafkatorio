@file:JsModule("util")
@file:JsNonModule
@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")
package util

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
import tsstdlib.Position
import ColorTable
import defines.direction
import LuaEntity
import tsstdlib.Record
import LuaTable

external fun <T> copy(table: T): T

external fun distance(position1: Position, position2: Position): Number

external fun positiontostr(position: Position): String

external fun formattime(ticks: Number): String

external fun color(hex: String): ColorTable

external fun premul_color(color: ColorTable): ColorTable

external fun premul_color(color: Any /* JsTuple<Any, float, Any, float, Any, float, Any, float> */): ColorTable

external fun mix_color(c1: ColorTable, c2: ColorTable): dynamic /* JsTuple<Any, float, Any, float, Any, float, Any, float> */

external fun mix_color(c1: ColorTable, c2: Any /* JsTuple<Any, float, Any, float, Any, float, Any, float> */): dynamic /* JsTuple<Any, float, Any, float, Any, float, Any, float> */

external fun mix_color(c1: Any /* JsTuple<Any, float, Any, float, Any, float, Any, float> */, c2: ColorTable): dynamic /* JsTuple<Any, float, Any, float, Any, float, Any, float> */

external fun mix_color(c1: Any /* JsTuple<Any, float, Any, float, Any, float, Any, float> */, c2: Any /* JsTuple<Any, float, Any, float, Any, float, Any, float> */): dynamic /* JsTuple<Any, float, Any, float, Any, float, Any, float> */

external fun multiply_color(c1: ColorTable, n: Number): dynamic /* JsTuple<Any, float, Any, float, Any, float, Any, float> */

external fun multiply_color(c1: Any /* JsTuple<Any, float, Any, float, Any, float, Any, float> */, n: Number): dynamic /* JsTuple<Any, float, Any, float, Any, float, Any, float> */

external fun moveposition(position: Position, direction: defines.direction.north, distance: Number): Position

external fun moveposition(position: Position, direction: defines.direction.east, distance: Number): Position

external fun moveposition(position: Position, direction: defines.direction.south, distance: Number): Position

external fun moveposition(position: Position, direction: defines.direction.west, distance: Number): Position

external fun moveposition(position: Position, direction: direction, distance: Number): Position?

external fun oppositedirection(direction: direction): direction

external fun <T> multiplystripes(count: Number, stripes: Array<T>): Array<T>

external fun by_pixel(x: Number, y: Number): Position

external fun by_pixel_hr(x: Number, y: Number): Position

external fun add_shift(a: Any /* JsTuple<x, int, y, int> */, b: Any /* JsTuple<x, int, y, int> */): dynamic /* JsTuple<x, int, y, int> */

external fun mul_shift(shift: Any /* JsTuple<x, int, y, int> */, scale: Number?): dynamic /* JsTuple<x, int, y, int> */

external fun format_number(amount: Number, append_suffix: Boolean = definedExternally): String

external fun increment(t: Array<Number>, index: Number, v: Number = definedExternally)

external fun <T : Any?> merge(tables: Array<T>): T

external fun insert_safe(entity: LuaEntity?, item_dict: Record<String, Number>?)

external fun remove_safe(entity: LuaEntity?, item_dict: Record<String, Number>?)

external fun split_whitespace(string: String): Array<String>

external fun clamp(x: Number, lower: Number, upper: Number): Number

external fun parse_energy(energy: String): Number

external interface `T$97` {
    var probability: Number
}

external interface `T$98` {
    var amount: Number
}

external interface `T$99` {
    var amount_min: Number
    var amount_max: Number
}

external fun product_amount(product: `T$97` /* `T$97` & dynamic */): Number

external fun empty_sprite(): Any?

external fun <T> remove_from_list(list: Array<T>, value: T): Boolean

external fun <T : Any> list_to_map(list: Array<T>): dynamic /* Record | LuaTable */

external fun <T> list_to_map(list: T): LuaTable<Any, Boolean>