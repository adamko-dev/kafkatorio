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
import tsstdlib.Set

external enum class SyntaxKind {
    File /* = 0 */,
    Block /* = 1 */,
    DoStatement /* = 2 */,
    VariableDeclarationStatement /* = 3 */,
    AssignmentStatement /* = 4 */,
    IfStatement /* = 5 */,
    WhileStatement /* = 6 */,
    RepeatStatement /* = 7 */,
    ForStatement /* = 8 */,
    ForInStatement /* = 9 */,
    GotoStatement /* = 10 */,
    LabelStatement /* = 11 */,
    ReturnStatement /* = 12 */,
    BreakStatement /* = 13 */,
    ExpressionStatement /* = 14 */,
    StringLiteral /* = 15 */,
    NumericLiteral /* = 16 */,
    NilKeyword /* = 17 */,
    DotsKeyword /* = 18 */,
    TrueKeyword /* = 19 */,
    FalseKeyword /* = 20 */,
    FunctionExpression /* = 21 */,
    TableFieldExpression /* = 22 */,
    TableExpression /* = 23 */,
    UnaryExpression /* = 24 */,
    BinaryExpression /* = 25 */,
    CallExpression /* = 26 */,
    MethodCallExpression /* = 27 */,
    Identifier /* = 28 */,
    TableIndexExpression /* = 29 */,
    AdditionOperator /* = 30 */,
    SubtractionOperator /* = 31 */,
    MultiplicationOperator /* = 32 */,
    DivisionOperator /* = 33 */,
    FloorDivisionOperator /* = 34 */,
    ModuloOperator /* = 35 */,
    PowerOperator /* = 36 */,
    NegationOperator /* = 37 */,
    ConcatOperator /* = 38 */,
    LengthOperator /* = 39 */,
    EqualityOperator /* = 40 */,
    InequalityOperator /* = 41 */,
    LessThanOperator /* = 42 */,
    LessEqualOperator /* = 43 */,
    GreaterThanOperator /* = 44 */,
    GreaterEqualOperator /* = 45 */,
    AndOperator /* = 46 */,
    OrOperator /* = 47 */,
    NotOperator /* = 48 */,
    BitwiseAndOperator /* = 49 */,
    BitwiseOrOperator /* = 50 */,
    BitwiseExclusiveOrOperator /* = 51 */,
    BitwiseRightShiftOperator /* = 52 */,
    BitwiseLeftShiftOperator /* = 53 */,
    BitwiseNotOperator /* = 54 */
}

typealias UnaryBitwiseOperator = SyntaxKind.BitwiseNotOperator

external interface TextRange {
    var line: Number?
        get() = definedExternally
        set(value) = definedExternally
    var column: Number?
        get() = definedExternally
        set(value) = definedExternally
}

external interface Node : TextRange {
    var kind: SyntaxKind
}

external fun createNode(kind: SyntaxKind, tsOriginal: ts.Node = definedExternally): Node

external fun <T : Node> cloneNode(node: T): T

external fun <T : Node> setNodePosition(node: T, position: TextRange): T

external fun <T : Node> setNodeOriginal(node: T, tsOriginal: ts.Node): T

external fun <T : Node> setNodeOriginal(node: T?, tsOriginal: ts.Node): T?

external fun getOriginalPos(node: Node): TextRange

external interface File : Node {
    var statements: Array<Statement>
    var luaLibFeatures: Set<LuaLibFeature>
    var trivia: String
}

external fun isFile(node: Node): Boolean

external fun createFile(statements: Array<Statement>, luaLibFeatures: Set<LuaLibFeature>, trivia: String, tsOriginal: ts.Node = definedExternally): File

external interface Block : Node {
    var statements: Array<Statement>
}

external fun isBlock(node: Node): Boolean

external fun createBlock(statements: Array<Statement>, tsOriginal: ts.Node = definedExternally): Block

external interface Statement : Node {
    var _statementBrand: Any
    var leadingComments: Array<dynamic /* String | Array<String> */>?
        get() = definedExternally
        set(value) = definedExternally
    var trailingComments: Array<dynamic /* String | Array<String> */>?
        get() = definedExternally
        set(value) = definedExternally
}

external interface DoStatement : Statement {
    var statements: Array<Statement>
}

external fun isDoStatement(node: Node): Boolean

external fun createDoStatement(statements: Array<Statement>, tsOriginal: ts.Node = definedExternally): DoStatement

external interface VariableDeclarationStatement : Statement {
    var left: Array<Identifier>
    var right: Array<Expression>?
        get() = definedExternally
        set(value) = definedExternally
}

external fun isVariableDeclarationStatement(node: Node): Boolean

external fun createVariableDeclarationStatement(left: Identifier, right: Expression = definedExternally, tsOriginal: ts.Node = definedExternally): VariableDeclarationStatement

external fun createVariableDeclarationStatement(left: Identifier): VariableDeclarationStatement

external fun createVariableDeclarationStatement(left: Identifier, right: Expression = definedExternally): VariableDeclarationStatement

external fun createVariableDeclarationStatement(left: Identifier, right: Array<Expression> = definedExternally, tsOriginal: ts.Node = definedExternally): VariableDeclarationStatement

external fun createVariableDeclarationStatement(left: Identifier, right: Array<Expression> = definedExternally): VariableDeclarationStatement

external fun createVariableDeclarationStatement(left: Array<Identifier>, right: Expression = definedExternally, tsOriginal: ts.Node = definedExternally): VariableDeclarationStatement

external fun createVariableDeclarationStatement(left: Array<Identifier>): VariableDeclarationStatement

external fun createVariableDeclarationStatement(left: Array<Identifier>, right: Expression = definedExternally): VariableDeclarationStatement

external fun createVariableDeclarationStatement(left: Array<Identifier>, right: Array<Expression> = definedExternally, tsOriginal: ts.Node = definedExternally): VariableDeclarationStatement

external fun createVariableDeclarationStatement(left: Array<Identifier>, right: Array<Expression> = definedExternally): VariableDeclarationStatement

external interface AssignmentStatement : Statement {
    var left: Array<dynamic /* Identifier | TableIndexExpression */>
    var right: Array<Expression>
}

external fun isAssignmentStatement(node: Node): Boolean

external fun createAssignmentStatement(left: Identifier, right: Expression = definedExternally, tsOriginal: ts.Node = definedExternally): AssignmentStatement

external fun createAssignmentStatement(left: Identifier): AssignmentStatement

external fun createAssignmentStatement(left: Identifier, right: Expression = definedExternally): AssignmentStatement

external fun createAssignmentStatement(left: Identifier, right: Array<Expression> = definedExternally, tsOriginal: ts.Node = definedExternally): AssignmentStatement

external fun createAssignmentStatement(left: Identifier, right: Array<Expression> = definedExternally): AssignmentStatement

external fun createAssignmentStatement(left: TableIndexExpression, right: Expression = definedExternally, tsOriginal: ts.Node = definedExternally): AssignmentStatement

external fun createAssignmentStatement(left: TableIndexExpression): AssignmentStatement

external fun createAssignmentStatement(left: TableIndexExpression, right: Expression = definedExternally): AssignmentStatement

external fun createAssignmentStatement(left: TableIndexExpression, right: Array<Expression> = definedExternally, tsOriginal: ts.Node = definedExternally): AssignmentStatement

external fun createAssignmentStatement(left: TableIndexExpression, right: Array<Expression> = definedExternally): AssignmentStatement

external fun createAssignmentStatement(left: Array<Any /* Identifier | TableIndexExpression */>, right: Expression = definedExternally, tsOriginal: ts.Node = definedExternally): AssignmentStatement

external fun createAssignmentStatement(left: Array<Any /* Identifier | TableIndexExpression */>): AssignmentStatement

external fun createAssignmentStatement(left: Array<Any /* Identifier | TableIndexExpression */>, right: Expression = definedExternally): AssignmentStatement

external fun createAssignmentStatement(left: Array<Any /* Identifier | TableIndexExpression */>, right: Array<Expression> = definedExternally, tsOriginal: ts.Node = definedExternally): AssignmentStatement

external fun createAssignmentStatement(left: Array<Any /* Identifier | TableIndexExpression */>, right: Array<Expression> = definedExternally): AssignmentStatement

external interface IfStatement : Statement {
    var condition: Expression
    var ifBlock: Block
    var elseBlock: dynamic /* Block? | IfStatement? */
        get() = definedExternally
        set(value) = definedExternally
}

external fun isIfStatement(node: Node): Boolean

external fun createIfStatement(condition: Expression, ifBlock: Block, elseBlock: Block = definedExternally, tsOriginal: ts.Node = definedExternally): IfStatement

external fun createIfStatement(condition: Expression, ifBlock: Block): IfStatement

external fun createIfStatement(condition: Expression, ifBlock: Block, elseBlock: Block = definedExternally): IfStatement

external fun createIfStatement(condition: Expression, ifBlock: Block, elseBlock: IfStatement = definedExternally, tsOriginal: ts.Node = definedExternally): IfStatement

external fun createIfStatement(condition: Expression, ifBlock: Block, elseBlock: IfStatement = definedExternally): IfStatement

external interface IterationStatement : Statement {
    var body: Block
}

external fun isIterationStatement(node: Node): Boolean

external interface WhileStatement : IterationStatement {
    var condition: Expression
}

external fun isWhileStatement(node: Node): Boolean

external fun createWhileStatement(body: Block, condition: Expression, tsOriginal: ts.Node = definedExternally): WhileStatement

external interface RepeatStatement : IterationStatement {
    var condition: Expression
}

external fun isRepeatStatement(node: Node): Boolean

external fun createRepeatStatement(body: Block, condition: Expression, tsOriginal: ts.Node = definedExternally): RepeatStatement

external interface ForStatement : IterationStatement {
    var controlVariable: Identifier
    var controlVariableInitializer: Expression
    var limitExpression: Expression
    var stepExpression: Expression?
        get() = definedExternally
        set(value) = definedExternally
}

external fun isForStatement(node: Node): Boolean

external fun createForStatement(body: Block, controlVariable: Identifier, controlVariableInitializer: Expression, limitExpression: Expression, stepExpression: Expression = definedExternally, tsOriginal: ts.Node = definedExternally): ForStatement

external interface ForInStatement : IterationStatement {
    var names: Array<Identifier>
    var expressions: Array<Expression>
}

external fun isForInStatement(node: Node): Boolean

external fun createForInStatement(body: Block, names: Array<Identifier>, expressions: Array<Expression>, tsOriginal: ts.Node = definedExternally): ForInStatement

external interface GotoStatement : Statement {
    var label: String
}

external fun isGotoStatement(node: Node): Boolean

external fun createGotoStatement(label: String, tsOriginal: ts.Node = definedExternally): GotoStatement

external interface LabelStatement : Statement {
    var name: String
}

external fun isLabelStatement(node: Node): Boolean

external fun createLabelStatement(name: String, tsOriginal: ts.Node = definedExternally): LabelStatement

external interface ReturnStatement : Statement {
    var expressions: Array<Expression>
}

external fun isReturnStatement(node: Node): Boolean

external fun createReturnStatement(expressions: Array<Expression>, tsOriginal: ts.Node = definedExternally): ReturnStatement

external interface BreakStatement : Statement

external fun isBreakStatement(node: Node): Boolean

external fun createBreakStatement(tsOriginal: ts.Node = definedExternally): BreakStatement

external interface ExpressionStatement : Statement {
    var expression: Expression
}

external fun isExpressionStatement(node: Node): Boolean

external fun createExpressionStatement(expressions: Expression, tsOriginal: ts.Node = definedExternally): ExpressionStatement

external interface Expression : Node {
    var _expressionBrand: Any
}

external interface NilLiteral : Expression

external fun isNilLiteral(node: Node): Boolean

external fun createNilLiteral(tsOriginal: ts.Node = definedExternally): NilLiteral

external interface BooleanLiteral : Expression {
    override var kind: dynamic /* SyntaxKind.TrueKeyword | SyntaxKind.FalseKeyword */
        get() = definedExternally
        set(value) = definedExternally
}

external fun isBooleanLiteral(node: Node): Boolean

external fun createBooleanLiteral(value: Boolean, tsOriginal: ts.Node = definedExternally): BooleanLiteral

external interface DotsLiteral : Expression

external fun isDotsLiteral(node: Node): Boolean

external fun createDotsLiteral(tsOriginal: ts.Node = definedExternally): DotsLiteral

external interface NumericLiteral : Expression {
    var value: Number
}

external fun isNumericLiteral(node: Node): Boolean

external fun createNumericLiteral(value: Number, tsOriginal: ts.Node = definedExternally): NumericLiteral

external interface StringLiteral : Expression {
    var value: String
}

external fun isStringLiteral(node: Node): Boolean

external fun createStringLiteral(value: String, tsOriginal: ts.Node = definedExternally): StringLiteral

external enum class FunctionExpressionFlags {
    None /* = 1 */,
    Inline /* = 2 */,
    Declaration /* = 4 */
}

external interface FunctionExpression : Expression {
    var params: Array<Identifier>?
        get() = definedExternally
        set(value) = definedExternally
    var dots: DotsLiteral?
        get() = definedExternally
        set(value) = definedExternally
    var body: Block
    var flags: FunctionExpressionFlags
}

external fun isFunctionExpression(node: Node): Boolean

external fun createFunctionExpression(body: Block, params: Array<Identifier> = definedExternally, dots: DotsLiteral = definedExternally, flags: FunctionExpressionFlags = definedExternally, tsOriginal: ts.Node = definedExternally): FunctionExpression

external interface TableFieldExpression : Expression {
    var value: Expression
    var key: Expression?
        get() = definedExternally
        set(value) = definedExternally
}

external fun isTableFieldExpression(node: Node): Boolean

external fun createTableFieldExpression(value: Expression, key: Expression = definedExternally, tsOriginal: ts.Node = definedExternally): TableFieldExpression

external interface TableExpression : Expression {
    var fields: Array<TableFieldExpression>
}

external fun isTableExpression(node: Node): Boolean

external fun createTableExpression(fields: Array<TableFieldExpression> = definedExternally, tsOriginal: ts.Node = definedExternally): TableExpression

external interface UnaryExpression : Expression {
    var operand: Expression
    var operator: dynamic /* SyntaxKind.NegationOperator | SyntaxKind.LengthOperator | SyntaxKind.NotOperator | UnaryBitwiseOperator */
        get() = definedExternally
        set(value) = definedExternally
}

external fun isUnaryExpression(node: Node): Boolean

external fun createUnaryExpression(operand: Expression, operator: SyntaxKind.NegationOperator, tsOriginal: ts.Node = definedExternally): UnaryExpression

external fun createUnaryExpression(operand: Expression, operator: SyntaxKind.NegationOperator): UnaryExpression

external fun createUnaryExpression(operand: Expression, operator: SyntaxKind.LengthOperator, tsOriginal: ts.Node = definedExternally): UnaryExpression

external fun createUnaryExpression(operand: Expression, operator: SyntaxKind.LengthOperator): UnaryExpression

external fun createUnaryExpression(operand: Expression, operator: SyntaxKind.NotOperator, tsOriginal: ts.Node = definedExternally): UnaryExpression

external fun createUnaryExpression(operand: Expression, operator: SyntaxKind.NotOperator): UnaryExpression

external fun createUnaryExpression(operand: Expression, operator: UnaryBitwiseOperator, tsOriginal: ts.Node = definedExternally): UnaryExpression

external fun createUnaryExpression(operand: Expression, operator: UnaryBitwiseOperator): UnaryExpression

external interface BinaryExpression : Expression {
    var operator: dynamic /* SyntaxKind.AdditionOperator | SyntaxKind.SubtractionOperator | SyntaxKind.MultiplicationOperator | SyntaxKind.DivisionOperator | SyntaxKind.FloorDivisionOperator | SyntaxKind.ModuloOperator | SyntaxKind.PowerOperator | SyntaxKind.ConcatOperator | SyntaxKind.EqualityOperator | SyntaxKind.InequalityOperator | SyntaxKind.LessThanOperator | SyntaxKind.LessEqualOperator | SyntaxKind.GreaterThanOperator | SyntaxKind.GreaterEqualOperator | SyntaxKind.AndOperator | SyntaxKind.OrOperator | SyntaxKind.BitwiseAndOperator | SyntaxKind.BitwiseOrOperator | SyntaxKind.BitwiseExclusiveOrOperator | SyntaxKind.BitwiseRightShiftOperator | SyntaxKind.BitwiseLeftShiftOperator */
        get() = definedExternally
        set(value) = definedExternally
    var left: Expression
    var right: Expression
}

external fun isBinaryExpression(node: Node): Boolean

external fun createBinaryExpression(left: Expression, right: Expression, operator: Any /* SyntaxKind.AdditionOperator | SyntaxKind.SubtractionOperator | SyntaxKind.MultiplicationOperator | SyntaxKind.DivisionOperator | SyntaxKind.FloorDivisionOperator | SyntaxKind.ModuloOperator | SyntaxKind.PowerOperator | SyntaxKind.ConcatOperator | SyntaxKind.EqualityOperator | SyntaxKind.InequalityOperator | SyntaxKind.LessThanOperator | SyntaxKind.LessEqualOperator | SyntaxKind.GreaterThanOperator | SyntaxKind.GreaterEqualOperator | SyntaxKind.AndOperator | SyntaxKind.OrOperator | SyntaxKind.BitwiseAndOperator | SyntaxKind.BitwiseOrOperator | SyntaxKind.BitwiseExclusiveOrOperator | SyntaxKind.BitwiseRightShiftOperator | SyntaxKind.BitwiseLeftShiftOperator */, tsOriginal: ts.Node = definedExternally): BinaryExpression

external interface CallExpression : Expression {
    var expression: Expression
    var params: Array<Expression>
}

external fun isCallExpression(node: Node): Boolean

external fun createCallExpression(expression: Expression, params: Array<Expression>, tsOriginal: ts.Node = definedExternally): CallExpression

external interface MethodCallExpression : Expression {
    var prefixExpression: Expression
    var name: Identifier
    var params: Array<Expression>
}

external fun isMethodCallExpression(node: Node): Boolean

external fun createMethodCallExpression(prefixExpression: Expression, name: Identifier, params: Array<Expression>, tsOriginal: ts.Node = definedExternally): MethodCallExpression

external interface Identifier : Expression {
    var exportable: Boolean
    var text: String
    var originalName: String?
        get() = definedExternally
        set(value) = definedExternally
    var symbolId: Number? /* Number? & `T$14`? */
        get() = definedExternally
        set(value) = definedExternally
}

external fun isIdentifier(node: Node): Boolean

external fun createIdentifier(text: String, tsOriginal: ts.Node = definedExternally, symbolId: Number /* Number & `T$14` */ = definedExternally, originalName: String = definedExternally): Identifier

external fun cloneIdentifier(identifier: Identifier, tsOriginal: ts.Node = definedExternally): Identifier

external fun createAnonymousIdentifier(tsOriginal: ts.Node = definedExternally): Identifier

external interface TableIndexExpression : Expression {
    var table: Expression
    var index: Expression
}

external fun isTableIndexExpression(node: Node): Boolean

external fun createTableIndexExpression(table: Expression, index: Expression, tsOriginal: ts.Node = definedExternally): TableIndexExpression

external fun isAssignmentLeftHandSideExpression(node: Node): Boolean

external fun isFunctionDefinition(statement: VariableDeclarationStatement): Boolean

external fun isFunctionDefinition(statement: AssignmentStatement): Boolean

external interface `T$17` {
    var statements: dynamic /* JsTuple<ReturnStatement> */
        get() = definedExternally
        set(value) = definedExternally
}

external fun isInlineFunctionExpression(expression: FunctionExpression): Boolean