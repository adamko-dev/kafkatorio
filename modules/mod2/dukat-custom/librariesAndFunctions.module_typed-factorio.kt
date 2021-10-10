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

external interface `T$95` {
    var tag: String
    var head: String
    var body: String
    var tail: String
    var level: Number
}

external interface SerpentOptions {
    var indent: String
    var comment: dynamic /* Boolean | Number */
        get() = definedExternally
        set(value) = definedExternally
    var sortkeys: dynamic /* Boolean | (this: Unit, keys: Array<Any>, table: Any) -> Unit */
        get() = definedExternally
        set(value) = definedExternally
    var sparse: Boolean?
    var compact: Boolean?
    var fatal: Boolean?
    var nocode: Boolean?
    var nohuge: Boolean?
    var maxlevel: Number
    var maxnum: Number
    var maxlength: Number
    var metatostring: Boolean?
    var numformat: String
    var valignore: Array<String>
    var keyallow: Array<String>
    var keyignore: Array<String>
    var valtypeignore: Array<String>
    fun custom(opts: `T$95`): String
    var name: String
    var refcomment: dynamic /* Boolean | Number */
        get() = definedExternally
        set(value) = definedExternally
    var tablecomment: dynamic /* Boolean | Number */
        get() = definedExternally
        set(value) = definedExternally
}

external interface SerpentOptionsPartial {
    var indent: String?
        get() = definedExternally
        set(value) = definedExternally
    var comment: dynamic /* Boolean? | Number? */
        get() = definedExternally
        set(value) = definedExternally
    var sortkeys: dynamic /* Boolean? | ((this: Unit, keys: Array<Any>, table: Any) -> Unit)? */
        get() = definedExternally
        set(value) = definedExternally
    var sparse: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var compact: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var fatal: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var nocode: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var nohuge: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var maxlevel: Number?
        get() = definedExternally
        set(value) = definedExternally
    var maxnum: Number?
        get() = definedExternally
        set(value) = definedExternally
    var maxlength: Number?
        get() = definedExternally
        set(value) = definedExternally
    var metatostring: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var numformat: String?
        get() = definedExternally
        set(value) = definedExternally
    var valignore: Array<String>?
        get() = definedExternally
        set(value) = definedExternally
    var keyallow: Array<String>?
        get() = definedExternally
        set(value) = definedExternally
    var keyignore: Array<String>?
        get() = definedExternally
        set(value) = definedExternally
    var valtypeignore: Array<String>?
        get() = definedExternally
        set(value) = definedExternally
    var custom: ((opts: `T$95`) -> String)?
        get() = definedExternally
        set(value) = definedExternally
    var name: String?
        get() = definedExternally
        set(value) = definedExternally
    var refcomment: dynamic /* Boolean? | Number? */
        get() = definedExternally
        set(value) = definedExternally
    var tablecomment: dynamic /* Boolean? | Number? */
        get() = definedExternally
        set(value) = definedExternally
}

external fun log(ls: Any /* JsTuple<String, Any> */)

external fun log(ls: String)

external fun log(ls: Number)

external fun localised_print(ls: Any /* JsTuple<String, Any> */)

external fun localised_print(ls: String)

external fun localised_print(ls: Number)

external fun table_size(tbl: table): Number