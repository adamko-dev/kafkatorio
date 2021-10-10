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

typealias SourceMapUrl = String

external interface StartOfSourceMap {
    var file: String?
        get() = definedExternally
        set(value) = definedExternally
    var sourceRoot: String?
        get() = definedExternally
        set(value) = definedExternally
    var skipValidation: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface RawSourceMap {
    var version: Number
    var sources: Array<String>
    var names: Array<String>
    var sourceRoot: String?
        get() = definedExternally
        set(value) = definedExternally
    var sourcesContent: Array<String>?
        get() = definedExternally
        set(value) = definedExternally
    var mappings: String
    var file: String
}

external interface RawIndexMap : StartOfSourceMap {
    var version: Number
    var sections: Array<RawSection>
}

external interface RawSection {
    var offset: Position
    var map: RawSourceMap
}

external interface Position {
    var line: Number
    var column: Number
}

external interface NullablePosition {
    var line: Number?
    var column: Number?
    var lastColumn: Number?
}

external interface MappedPosition {
    var source: String
    var line: Number
    var column: Number
    var name: String?
        get() = definedExternally
        set(value) = definedExternally
}

external interface NullableMappedPosition {
    var source: String?
    var line: Number?
    var column: Number?
    var name: String?
}

external interface MappingItem {
    var source: String
    var generatedLine: Number
    var generatedColumn: Number
    var originalLine: Number
    var originalColumn: Number
    var name: String
}

external interface Mapping {
    var generated: Position
    var original: Position
    var source: String
    var name: String?
        get() = definedExternally
        set(value) = definedExternally
}

external interface CodeWithSourceMap {
    var code: String
    var map: SourceMapGenerator
}

external interface SourceMapConsumer {
    fun computeColumnSpans()
    fun originalPositionFor(generatedPosition: Position /* Position & `T$19` */): NullableMappedPosition
    fun generatedPositionFor(originalPosition: MappedPosition /* MappedPosition & `T$19` */): NullablePosition
    fun allGeneratedPositionsFor(originalPosition: MappedPosition): Array<NullablePosition>
    fun hasContentsOfAllSources(): Boolean
    fun sourceContentFor(source: String, returnNullOnMissing: Boolean = definedExternally): String?
    fun eachMapping(callback: (mapping: MappingItem) -> Unit, context: Any = definedExternally, order: Number = definedExternally)
    fun destroy()
}

external interface SourceMapConsumerConstructor {
    var prototype: SourceMapConsumer
    var GENERATED_ORDER: Number
    var ORIGINAL_ORDER: Number
    var GREATEST_LOWER_BOUND: Number
    var LEAST_UPPER_BOUND: Number
    fun fromSourceMap(sourceMap: SourceMapGenerator, sourceMapUrl: SourceMapUrl = definedExternally): Promise<BasicSourceMapConsumer>
    fun <T> with(rawSourceMap: RawSourceMap, sourceMapUrl: SourceMapUrl?, callback: (consumer: Any /* BasicSourceMapConsumer | IndexedSourceMapConsumer */) -> Any?): Promise<T>
    fun <T> with(rawSourceMap: RawIndexMap, sourceMapUrl: SourceMapUrl?, callback: (consumer: Any /* BasicSourceMapConsumer | IndexedSourceMapConsumer */) -> Any?): Promise<T>
    fun <T> with(rawSourceMap: String, sourceMapUrl: SourceMapUrl?, callback: (consumer: Any /* BasicSourceMapConsumer | IndexedSourceMapConsumer */) -> Any?): Promise<T>
}

external interface BasicSourceMapConsumer : SourceMapConsumer {
    var file: String
    var sourceRoot: String
    var sources: Array<String>
    var sourcesContent: Array<String>
}

external interface BasicSourceMapConsumerConstructor {
    var prototype: BasicSourceMapConsumer
    fun fromSourceMap(sourceMap: SourceMapGenerator): Promise<BasicSourceMapConsumer>
}

external interface IndexedSourceMapConsumer : SourceMapConsumer {
    var sources: Array<String>
}

external interface IndexedSourceMapConsumerConstructor {
    var prototype: IndexedSourceMapConsumer
}

external open class SourceMapGenerator(startOfSourceMap: StartOfSourceMap = definedExternally) {
    open fun addMapping(mapping: Mapping)
    open fun setSourceContent(sourceFile: String, sourceContent: String)
    open fun applySourceMap(sourceMapConsumer: SourceMapConsumer, sourceFile: String = definedExternally, sourceMapPath: String = definedExternally)
    override fun toString(): String
    open fun toJSON(): RawSourceMap

    companion object {
        fun fromSourceMap(sourceMapConsumer: SourceMapConsumer): SourceMapGenerator
    }
}

external open class SourceNode {
    open var children: Array<SourceNode>
    open var sourceContents: Any
    open var line: Number
    open var column: Number
    open var source: String
    open var name: String
    constructor()
    constructor(line: Number?, column: Number?, source: String?, chunks: Array<Any /* String | SourceNode */> = definedExternally, name: String = definedExternally)
    constructor(line: Number?, column: Number?, source: String?)
    constructor(line: Number?, column: Number?, source: String?, chunks: Array<Any /* String | SourceNode */> = definedExternally)
    constructor(line: Number?, column: Number?, source: String?, chunks: SourceNode = definedExternally, name: String = definedExternally)
    constructor(line: Number?, column: Number?, source: String?, chunks: SourceNode = definedExternally)
    constructor(line: Number?, column: Number?, source: String?, chunks: String = definedExternally, name: String = definedExternally)
    constructor(line: Number?, column: Number?, source: String?, chunks: String = definedExternally)
    open fun add(chunk: Array<Any /* String | SourceNode */>): SourceNode
    open fun add(chunk: SourceNode): SourceNode
    open fun add(chunk: String): SourceNode
    open fun prepend(chunk: Array<Any /* String | SourceNode */>): SourceNode
    open fun prepend(chunk: SourceNode): SourceNode
    open fun prepend(chunk: String): SourceNode
    open fun setSourceContent(sourceFile: String, sourceContent: String)
    open fun walk(fn: (chunk: String, mapping: MappedPosition) -> Unit)
    open fun walkSourceContents(fn: (file: String, content: String) -> Unit)
    open fun join(sep: String): SourceNode
    open fun replaceRight(pattern: String, replacement: String): SourceNode
    override fun toString(): String
    open fun toStringWithSourceMap(startOfSourceMap: StartOfSourceMap = definedExternally): CodeWithSourceMap

    companion object {
        fun fromStringWithSourceMap(code: String, sourceMapConsumer: SourceMapConsumer, relativePath: String = definedExternally): SourceNode
    }
}