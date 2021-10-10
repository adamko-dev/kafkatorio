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
import ts.SourceFile

external interface EmitHost {
    fun directoryExists(path: String): Boolean
    fun fileExists(path: String): Boolean
    fun getCurrentDirectory(): String
    fun readFile(path: String): String?
    var writeFile: WriteFileCallback
}

external interface BaseFile {
    var code: String
    var sourceMap: String?
        get() = definedExternally
        set(value) = definedExternally
    var sourceFiles: Array<SourceFile>?
        get() = definedExternally
        set(value) = definedExternally
}

external interface ProcessedFile : BaseFile {
    var fileName: String
    var luaAst: File?
        get() = definedExternally
        set(value) = definedExternally
}

external interface EmitFile : BaseFile {
    var outputPath: String
}