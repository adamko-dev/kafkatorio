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
import ts.Program

external interface TranspilerOptions {
    var emitHost: EmitHost?
        get() = definedExternally
        set(value) = definedExternally
}

external interface EmitOptions : TranspileOptions {
    var writeFile: WriteFileCallback?
        get() = definedExternally
        set(value) = definedExternally
}

external interface EmitResult {
    var emitSkipped: Boolean
    var diagnostics: Array<Diagnostic>
}

external interface `T$20` {
    var emitPlan: Array<EmitFile>
}

external open class Transpiler(__0: TranspilerOptions = definedExternally) {
    open var emitHost: EmitHost
    open fun emit(emitOptions: EmitOptions): EmitResult
    open fun getEmitPlan(program: Program, diagnostics: Array<Diagnostic>, files: Array<ProcessedFile>): `T$20`
}

external fun getEmitPath(file: String, program: Program): String

external fun getEmitPathRelativeToOutDir(fileName: String, program: Program): String

external fun getSourceDir(program: Program): String

external fun getEmitOutDir(program: Program): String

external fun getProjectRoot(program: Program): String