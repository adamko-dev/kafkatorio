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
import tsstdlib.Record

external interface BaseSettingDefinition {
    var type: String /* "bool-setting" | "int-setting" | "double-setting" | "string-setting" */
    var name: String
    var localized_name: dynamic /* JsTuple<String, Any> | String? | Number? */
        get() = definedExternally
        set(value) = definedExternally
    var localized_description: dynamic /* JsTuple<String, Any> | String? | Number? */
        get() = definedExternally
        set(value) = definedExternally
    var order: String?
        get() = definedExternally
        set(value) = definedExternally
    var hidden: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var setting_type: String /* "startup" | "runtime-global" | "runtime-per-user" */
}

external interface BoolSettingDefinition : BaseSettingDefinition {
    override var type: String /* "bool-setting" */
    var default_value: Boolean
    var forced_value: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface IntSettingDefinition : BaseSettingDefinition {
    override var type: String /* "int-setting" */
    var default_value: Number
    var minimum_value: Number?
        get() = definedExternally
        set(value) = definedExternally
    var maximum_value: Number?
        get() = definedExternally
        set(value) = definedExternally
    var allowed_values: Array<Number>?
        get() = definedExternally
        set(value) = definedExternally
}

external interface DoubleSettingDefinition : BaseSettingDefinition {
    override var type: String /* "double-setting" */
    var default_value: Number
    var minimum_value: Number?
        get() = definedExternally
        set(value) = definedExternally
    var maximum_value: Number?
        get() = definedExternally
        set(value) = definedExternally
    var allowed_values: Array<Number>?
        get() = definedExternally
        set(value) = definedExternally
}

external interface StringSettingDefinition : BaseSettingDefinition {
    override var type: String /* "string-setting" */
    var default_value: String
    var allow_blank: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var auto_trim: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var allowed_values: Array<String>?
        get() = definedExternally
        set(value) = definedExternally
}

external interface Data {
    var raw: Any
    fun extend(prototypes: Array<Any /* BoolSettingDefinition | IntSettingDefinition | DoubleSettingDefinition | StringSettingDefinition */>)
}

typealias Mods = Record<String, String>