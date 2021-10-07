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
import ts.Diagnostic

typealias OmitIndexSignature<T> = Any

external interface TransformerImport {
    var transform: String
    var import: String?
        get() = definedExternally
        set(value) = definedExternally
    var after: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var afterDeclarations: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var type: String? /* "program" | "config" | "checker" | "raw" | "compilerOptions" */
        get() = definedExternally
        set(value) = definedExternally
    @nativeGetter
    operator fun get(option: String): Any?
    @nativeSetter
    operator fun set(option: String, value: Any)
}

external interface LuaPluginImport {
    var name: String
    var import: String?
        get() = definedExternally
        set(value) = definedExternally
    @nativeGetter
    operator fun get(option: String): Any?
    @nativeSetter
    operator fun set(option: String, value: Any)
}

external enum class LuaLibImportKind {
    None /* = "none" */,
    Always /* = "always" */,
    Inline /* = "inline" */,
    Require /* = "require" */
}

external enum class LuaTarget {
    Universal /* = "universal" */,
    Lua51 /* = "5.1" */,
    Lua52 /* = "5.2" */,
    Lua53 /* = "5.3" */,
    Lua54 /* = "5.4" */,
    LuaJIT /* = "JIT" */
}

external enum class BuildMode {
    Default /* = "default" */,
    Library /* = "library" */
}

external var isBundleEnabled: (options: dynamic /* typealias OmitIndexSignature = dynamic */) -> Boolean

external fun validateOptions(options: dynamic /* typealias OmitIndexSignature = dynamic */): Array<Diagnostic>