@file:JsQualifier("serpent")
@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")
package serpent

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
import SerpentOptionsPartial

external fun dump(tbl: Any, options: SerpentOptionsPartial = definedExternally): String

external fun line(tbl: Any, options: SerpentOptionsPartial = definedExternally): String

external fun block(tbl: Any, options: SerpentOptionsPartial = definedExternally): String

external interface `T$96` {
    var safe: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external fun <T> load(str: String, options: `T$96` = definedExternally): dynamic /* JsTuple<Boolean, T> | JsTuple<Boolean, String> */