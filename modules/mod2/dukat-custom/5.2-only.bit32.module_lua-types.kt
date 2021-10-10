@file:JsQualifier("bit32")
@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")
package bit32

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

external fun arshift(x: Number, disp: Number): Number

external fun band(vararg operands: Number): Number

external fun bnot(x: Number): Number

external fun bor(vararg operands: Number): Number

external fun btest(vararg operands: Number): Boolean

external fun bxor(vararg operands: Number): Number

external fun extract(n: Number, field: Number, width: Number = definedExternally): Number

external fun replace(n: Number, v: Number, field: Number, width: Number = definedExternally): Number

external fun lrotate(x: Number, disp: Number): Number

external fun lshift(x: Number, disp: Number): Number

external fun rrotate(x: Number, disp: Number): Number

external fun rshift(x: Number, disp: Number): Number