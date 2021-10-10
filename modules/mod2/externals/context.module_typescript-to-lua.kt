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
import ts.GetAccessorDeclaration
import ts.SetAccessorDeclaration
import ts.Node
import ts.ImportEqualsDeclaration
import ts.Expression
import ts.SourceFile
import ts.CancellationToken
import ts.TypeChecker
import ts.Program
import ts.Diagnostic
import ts.CompilerOptions
import Node as _Node
import Expression as _Expression
import ts.QualifiedName
import ts.ExternalModuleReference

external interface AllAccessorDeclarations {
    var firstAccessor: dynamic /* GetAccessorDeclaration | SetAccessorDeclaration */
        get() = definedExternally
        set(value) = definedExternally
    var secondAccessor: dynamic /* GetAccessorDeclaration? | SetAccessorDeclaration? */
        get() = definedExternally
        set(value) = definedExternally
    var getAccessor: GetAccessorDeclaration?
    var setAccessor: SetAccessorDeclaration?
}

external interface EmitResolver {
    fun isValueAliasDeclaration(node: Node): Boolean
    fun isReferencedAliasDeclaration(node: Node, checkChildren: Boolean = definedExternally): Boolean
    fun isTopLevelValueImportEqualsWithEntityName(node: ImportEqualsDeclaration): Boolean
    fun moduleExportsSomeValue(moduleReferenceExpression: Expression): Boolean
    fun getAllAccessorDeclarations(declaration: GetAccessorDeclaration): AllAccessorDeclarations
    fun getAllAccessorDeclarations(declaration: SetAccessorDeclaration): AllAccessorDeclarations
}

external interface DiagnosticsProducingTypeChecker : TypeChecker {
    fun getEmitResolver(sourceFile: SourceFile = definedExternally, cancellationToken: CancellationToken = definedExternally): EmitResolver
}

external open class TransformationContext(program: Program, sourceFile: SourceFile, visitorMap: VisitorMap) {
    open var program: Program
    open var sourceFile: SourceFile
    open var visitorMap: Any
    open var diagnostics: Array<Diagnostic>
    open var checker: DiagnosticsProducingTypeChecker
    open var resolver: EmitResolver
    open var options: OmitIndexSignature<CompilerOptions> /* OmitIndexSignature<ts.CompilerOptions> & `T$0` */
    open var luaTarget: LuaTarget
    open var isModule: Boolean
    open var isStrict: Boolean
    open var currentNodeVisitors: Any
    open fun transformNode(node: Node): Array<_Node>
    open fun superTransformNode(node: Node): Array<_Node>
    open fun transformExpression(node: Expression): _Expression
    open fun transformExpression(node: QualifiedName): _Expression
    open fun transformExpression(node: ExternalModuleReference): _Expression
    open fun superTransformExpression(node: Expression): _Expression
    open fun superTransformExpression(node: QualifiedName): _Expression
    open fun superTransformExpression(node: ExternalModuleReference): _Expression
    open fun transformStatements(node: StatementLikeNode): Array<Statement>
    open fun transformStatements(node: Array<StatementLikeNode>): Array<Statement>
    open fun superTransformStatements(node: StatementLikeNode): Array<Statement>
    open fun superTransformStatements(node: Array<StatementLikeNode>): Array<Statement>
}