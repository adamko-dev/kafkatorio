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
import ts.Program
import ts.SourceFile
import ts.CustomTransformers
import ts.Diagnostic
import ts.WriteFileCallback

external interface TranspileOptions {
    var program: Program
    var sourceFiles: Array<SourceFile>?
        get() = definedExternally
        set(value) = definedExternally
    var customTransformers: CustomTransformers?
        get() = definedExternally
        set(value) = definedExternally
    var plugins: Array<Plugin>?
        get() = definedExternally
        set(value) = definedExternally
}

external interface TranspileResult {
    var diagnostics: Array<Diagnostic>
    var transpiledFiles: Array<ProcessedFile>
}

external fun getProgramTranspileResult(emitHost: EmitHost, writeFileResult: WriteFileCallback, __2: TranspileOptions): TranspileResult