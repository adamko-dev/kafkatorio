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
import ts.WriteFileCallback
import ts.Diagnostic
import tsstdlib.Record

external fun transpileFiles(rootNames: Array<String>, options: dynamic /* typealias OmitIndexSignature = dynamic */ = definedExternally, writeFile: WriteFileCallback = definedExternally): EmitResult

external fun transpileProject(configFileName: String, optionsToExtend: dynamic /* typealias OmitIndexSignature = dynamic */ = definedExternally, writeFile: WriteFileCallback = definedExternally): EmitResult

external interface TranspileVirtualProjectResult {
    var diagnostics: Array<Diagnostic>
    var transpiledFiles: Array<TranspiledFile>
}

external fun transpileVirtualProject(files: Record<String, String>, options: dynamic /* typealias OmitIndexSignature = dynamic */ = definedExternally): TranspileVirtualProjectResult

external interface TranspileStringResult {
    var diagnostics: Array<Diagnostic>
    var file: TranspiledFile?
        get() = definedExternally
        set(value) = definedExternally
}

external fun transpileString(main: String, options: dynamic /* typealias OmitIndexSignature = dynamic */ = definedExternally): TranspileStringResult