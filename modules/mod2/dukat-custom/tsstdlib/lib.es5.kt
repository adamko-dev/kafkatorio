@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")
package tsstdlib

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

external interface FunctionConstructor {
    @nativeInvoke
    operator fun invoke(vararg args: String): Function<*>
    var prototype: Function<*>
}

external interface ConcatArray<T> {
    var length: Number
    @nativeGetter
    operator fun get(n: Number): T?
    @nativeSetter
    operator fun set(n: Number, value: T)
    fun join(separator: String = definedExternally): String
    fun slice(start: Number = definedExternally, end: Number = definedExternally): Array<T>
}

external interface ArrayConstructor {
    fun <T> from(iterable: Iterable<T>): Array<T>
    fun <T> from(iterable: ArrayLike<T>): Array<T>
    fun <T, U> from(iterable: Iterable<T>, mapfn: (v: T, k: Number) -> U, thisArg: Any = definedExternally): Array<U>
    fun <T, U> from(iterable: Iterable<T>, mapfn: (v: T, k: Number) -> U): Array<U>
    fun <T, U> from(iterable: ArrayLike<T>, mapfn: (v: T, k: Number) -> U, thisArg: Any = definedExternally): Array<U>
    fun <T, U> from(iterable: ArrayLike<T>, mapfn: (v: T, k: Number) -> U): Array<U>
    fun <T> of(vararg items: T): Array<T>
    @nativeInvoke
    operator fun invoke(arrayLength: Number = definedExternally): Array<Any>
    @nativeInvoke
    operator fun invoke(): Array<Any>
    @nativeInvoke
    operator fun <T> invoke(arrayLength: Number): Array<T>
    @nativeInvoke
    operator fun <T> invoke(vararg items: T): Array<T>
    fun isArray(arg: Any): Boolean
    var prototype: Array<Any>
}

external interface ArrayLike<T> {
    var length: Number
    @nativeGetter
    operator fun get(n: Number): T?
    @nativeSetter
    operator fun set(n: Number, value: T)
}

typealias Partial<T> = Any

typealias Pick<T, K> = Any

@Deprecated("use Kotlin Wrapper", ReplaceWith("kotlinext.js.Record<K, T>"))
typealias Record<K, T> = kotlinext.js.Record<K, T>


typealias Exclude<T, U> = Any

typealias Extract<T, U> = Any

typealias Omit<T, K> = Any

typealias NonNullable<T> = Any