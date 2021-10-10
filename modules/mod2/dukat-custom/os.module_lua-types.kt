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

external interface LuaDateInfo {
    var year: Number
    var month: Number
    var day: Number
    var hour: Number?
        get() = definedExternally
        set(value) = definedExternally
    var min: Number?
        get() = definedExternally
        set(value) = definedExternally
    var sec: Number?
        get() = definedExternally
        set(value) = definedExternally
    var isdst: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface LuaDateInfoResult {
    var year: Number
    var month: Number
    var day: Number
    var hour: Number
    var min: Number
    var sec: Number
    var isdst: Boolean
    var yday: Number
    var wday: Number
}