@file:JsQualifier("math")
@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")
package math

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

external fun abs(x: Number): Number

external fun acos(x: Number): Number

external fun asin(x: Number): Number

external fun ceil(x: Number): Number

external fun cos(x: Number): Number

external fun deg(x: Number): Number

external fun exp(x: Number): Number

external fun floor(x: Number): Number

external fun fmod(x: Number, y: Number): Number

external var huge: Number

external fun max(x: Number, vararg numbers: Number): Number

external fun min(x: Number, vararg numbers: Number): Number

external fun modf(x: Number): dynamic /* JsTuple<Number, Number> */

external var pi: Number

external fun rad(x: Number): Number

external fun sin(x: Number): Number

external fun sqrt(x: Number): Number

external fun tan(x: Number): Number

external fun log(x: Number, base: Number = definedExternally): Number

external fun atan(x: Number): Number

external fun atan2(y: Number, x: Number): Number

external fun cosh(x: Number): Number

external fun frexp(x: Number): Number

external fun ldexp(m: Number, e: Number): Number

external fun pow(x: Number, y: Number): Number

external fun sinh(x: Number): Number

external fun tanh(x: Number): Number

external fun random(m: Number = definedExternally, n: Number = definedExternally): Number

external fun randomseed(x: Number): Number