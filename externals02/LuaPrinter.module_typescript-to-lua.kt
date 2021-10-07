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

external var escapeString: (value: String) -> String

external var tstlHeader: Any

typealias Printer = (program: Program, emitHost: EmitHost, fileName: String, file: File) -> PrintResult

external interface PrintResult {
    var code: String
    var sourceMap: String
    var sourceMapNode: SourceNode
}

external fun createPrinter(printers: Array<Printer>): Printer

open external class LuaPrinter(emitHost: EmitHost, program: Program, fileName: String) {
    open var emitHost: Any
    open var currentIndent: Any
    open var sourceFile: Any
    open var options: Any
    open fun print(file: File): PrintResult
    open var printInlineSourceMap: Any
    open var printStackTraceOverride: Any
    open var printFile: Any
    open fun pushIndent()
    open fun popIndent()
    open fun indent(input: String = definedExternally): dynamic /* String | SourceNode */
    open fun indent(): dynamic /* String | SourceNode */
    open fun indent(input: SourceNode = definedExternally): dynamic /* String | SourceNode */
    open fun createSourceNode(node: Node, chunks: String, name: String = definedExternally): SourceNode
    open fun createSourceNode(node: Node, chunks: String): SourceNode
    open fun createSourceNode(node: Node, chunks: SourceNode, name: String = definedExternally): SourceNode
    open fun createSourceNode(node: Node, chunks: SourceNode): SourceNode
    open fun createSourceNode(node: Node, chunks: Array<Any /* String | SourceNode */>, name: String = definedExternally): SourceNode
    open fun createSourceNode(node: Node, chunks: Array<Any /* String | SourceNode */>): SourceNode
    open fun concatNodes(vararg chunks: Any /* String | SourceNode */): SourceNode
    open fun printBlock(block: Block): SourceNode
    open var statementMayRequireSemiColon: Any
    open var nodeStartsWithParenthesis: Any
    open fun printStatementArray(statements: Array<Statement>): Array<dynamic /* String | SourceNode */>
    open fun printStatement(statement: Statement): SourceNode
    open fun printComment(comment: String): dynamic /* String | SourceNode */
    open fun printComment(comment: Array<String>): dynamic /* String | SourceNode */
    open fun printStatementExcludingComments(statement: Statement): SourceNode
    open fun printDoStatement(statement: DoStatement): SourceNode
    open fun printVariableDeclarationStatement(statement: VariableDeclarationStatement): SourceNode
    open fun printVariableAssignmentStatement(statement: AssignmentStatement): SourceNode
    open fun printIfStatement(statement: IfStatement, isElseIf: Boolean = definedExternally): SourceNode
    open fun printWhileStatement(statement: WhileStatement): SourceNode
    open fun printRepeatStatement(statement: RepeatStatement): SourceNode
    open fun printForStatement(statement: ForStatement): SourceNode
    open fun printForInStatement(statement: ForInStatement): SourceNode
    open fun printGotoStatement(statement: GotoStatement): SourceNode
    open fun printLabelStatement(statement: LabelStatement): SourceNode
    open fun printReturnStatement(statement: ReturnStatement): SourceNode
    open fun printBreakStatement(statement: BreakStatement): SourceNode
    open fun printExpressionStatement(statement: ExpressionStatement): SourceNode
    open fun printExpression(expression: Expression): SourceNode
    open fun printStringLiteral(expression: StringLiteral): SourceNode
    open fun printNumericLiteral(expression: NumericLiteral): SourceNode
    open fun printNilLiteral(expression: NilLiteral): SourceNode
    open fun printDotsLiteral(expression: DotsLiteral): SourceNode
    open fun printBooleanLiteral(expression: BooleanLiteral): SourceNode
    open var printFunctionParameters: Any
    open fun printFunctionExpression(expression: FunctionExpression): SourceNode
    open fun printFunctionDefinition(statement: Any /* VariableDeclarationStatement | AssignmentStatement */): SourceNode
    open fun printTableFieldExpression(expression: TableFieldExpression): SourceNode
    open fun printTableExpression(expression: TableExpression): SourceNode
    open fun printUnaryExpression(expression: UnaryExpression): SourceNode
    open fun printBinaryExpression(expression: BinaryExpression): SourceNode
    open var printExpressionInParenthesesIfNeeded: Any
    open var needsParenthesis: Any
    open fun printCallExpression(expression: CallExpression): SourceNode
    open fun printMethodCallExpression(expression: MethodCallExpression): SourceNode
    open fun printIdentifier(expression: Identifier): SourceNode
    open fun printTableIndexExpression(expression: TableIndexExpression): SourceNode
    open fun printOperator(kind: Any /* SyntaxKind.NegationOperator | SyntaxKind.LengthOperator | SyntaxKind.NotOperator | UnaryBitwiseOperator | SyntaxKind.AdditionOperator | SyntaxKind.SubtractionOperator | SyntaxKind.MultiplicationOperator | SyntaxKind.DivisionOperator | SyntaxKind.FloorDivisionOperator | SyntaxKind.ModuloOperator | SyntaxKind.PowerOperator | SyntaxKind.ConcatOperator | SyntaxKind.EqualityOperator | SyntaxKind.InequalityOperator | SyntaxKind.LessThanOperator | SyntaxKind.LessEqualOperator | SyntaxKind.GreaterThanOperator | SyntaxKind.GreaterEqualOperator | SyntaxKind.AndOperator | SyntaxKind.OrOperator | SyntaxKind.BitwiseAndOperator | SyntaxKind.BitwiseOrOperator | SyntaxKind.BitwiseExclusiveOrOperator | SyntaxKind.BitwiseRightShiftOperator | SyntaxKind.BitwiseLeftShiftOperator */): SourceNode
    open fun joinChunksWithComma(chunks: Array<Any /* String | SourceNode */>): Array<dynamic /* String | SourceNode */>
    open fun printExpressionList(expressions: Array<Expression>): Array<dynamic /* String | SourceNode */>
    open var buildSourceMap: Any

    companion object {
        var operatorMap: Any
    }
}