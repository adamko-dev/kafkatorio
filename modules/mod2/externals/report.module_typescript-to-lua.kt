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
import ts.DiagnosticRelatedInformation
import ts.DiagnosticCategory
import ts.SourceFile
import ts.Diagnostic
import ts.System
import ts.DiagnosticReporter

external interface `T$13` {
    var code: Any
    var reportsUnnecessary: Any?
        get() = definedExternally
        set(value) = definedExternally
    var reportsDeprecated: Any?
        get() = definedExternally
        set(value) = definedExternally
    var source: String?
        get() = definedExternally
        set(value) = definedExternally
    var relatedInformation: Array<DiagnosticRelatedInformation>?
        get() = definedExternally
        set(value) = definedExternally
    var category: DiagnosticCategory
    var file: SourceFile?
    var start: Number?
    var length: Number?
    var messageText: dynamic /* String | ts.DiagnosticMessageChain */
        get() = definedExternally
        set(value) = definedExternally
}

external var prepareDiagnosticForFormatting: (diagnostic: Diagnostic) -> `T$13`

external fun createDiagnosticReporter(pretty: Boolean, system: System = definedExternally): DiagnosticReporter