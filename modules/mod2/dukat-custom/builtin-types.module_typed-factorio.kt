@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

import kotlin.js.*
import kotlinext.js.PropertyDescriptor

typealias float = Number

typealias double = Number

typealias int = Number

typealias int8 = Number

typealias uint = Number

typealias uint8 = Number

typealias uint16 = Number

typealias uint64 = Number

//typealias table = Any?
// TODO LuaTable? AnyTable? index.module_typescript-to-lua.kt
@JsName("table")
class table  : Object2()

/** https://github.com/JetBrains/kotlin-wrappers/blob/master/kotlin-extensions/src/main/kotlin/kotlinext/js/Object.kt */
open external class Object2   {
  fun toLocaleString(): String
  fun valueOf(): dynamic
  fun hasOwnProperty(v: String): Boolean
  fun isPrototypeOf(v: Any): Boolean
  fun propertyIsEnumerable(v: String): Boolean

  companion object {
    fun <P : Any, T : P> getPrototypeOf(o: T): P
    fun <T> getOwnPropertyDescriptor(o: Any, p: String): PropertyDescriptor<T>
    fun getOwnPropertyNames(o: Any): Array<String>
    fun <T : Any> create(o: T?, properties: dynamic = definedExternally): T
    fun <T : Any, P> defineProperty(o: T, p: String, attributes: PropertyDescriptor<P>): T
    fun <T : Any> defineProperties(o: T, properties: dynamic): T
    fun <T> seal(o: T): T
    fun <R, T : R> freeze(o: T): R
    fun <T> preventExtensions(o: T): T
    fun isSealed(o: Any): Boolean
    fun isFrozen(o: Any): Boolean
    fun isExtensible(o: Any): Boolean
    fun keys(o: Any): Array<String>
    fun <T : Any, R : T> assign(dest: R, vararg src: T?): R
  }
}
