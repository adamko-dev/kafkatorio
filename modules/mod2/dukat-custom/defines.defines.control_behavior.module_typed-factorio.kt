@file:JsQualifier("defines.control_behavior")
@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")
package defines.control_behavior

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

external enum class type {
    container,
    generic_on_off,
    inserter,
    lamp,
    logistic_container,
    roboport,
    storage_tank,
    train_stop,
    decider_combinator,
    arithmetic_combinator,
    constant_combinator,
    transport_belt,
    accumulator,
    rail_signal,
    rail_chain_signal,
    wall,
    mining_drill,
    programmable_speaker
}