@file:JsQualifier("defines.deconstruction_item")
@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")
package defines.deconstruction_item

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

external enum class entity_filter_mode {
    whitelist,
    blacklist
}

external enum class tile_filter_mode {
    whitelist,
    blacklist
}

external enum class tile_selection_mode {
    normal,
    always,
    never,
    only
}