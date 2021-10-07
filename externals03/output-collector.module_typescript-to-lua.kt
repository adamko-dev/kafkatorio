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
import ts.SourceFile

external interface TranspiledFile {
    var outPath: String
    var sourceFiles: Array<SourceFile>
    var lua: String?
        get() = definedExternally
        set(value) = definedExternally
    var luaSourceMap: String?
        get() = definedExternally
        set(value) = definedExternally
    var declaration: String?
        get() = definedExternally
        set(value) = definedExternally
    var declarationMap: String?
        get() = definedExternally
        set(value) = definedExternally
}