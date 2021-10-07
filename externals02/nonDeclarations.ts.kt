@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")
package ts

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

typealias SortedReadonlyArray<T> = Array<T>

typealias SortedArray<T> = Array<T>

typealias NodeArray<T> = Array<T>

typealias DotToken = PunctuationToken<SyntaxKind>

typealias DotDotDotToken = PunctuationToken<SyntaxKind>

typealias QuestionToken = PunctuationToken<SyntaxKind>

typealias ExclamationToken = PunctuationToken<SyntaxKind>

typealias ColonToken = PunctuationToken<SyntaxKind>

typealias EqualsToken = PunctuationToken<SyntaxKind>

typealias AsteriskToken = PunctuationToken<SyntaxKind>

typealias EqualsGreaterThanToken = PunctuationToken<SyntaxKind>

typealias PlusToken = PunctuationToken<SyntaxKind>

typealias MinusToken = PunctuationToken<SyntaxKind>

typealias QuestionDotToken = PunctuationToken<SyntaxKind>

typealias AssertsKeyword = KeywordToken<SyntaxKind>

typealias AwaitKeyword = KeywordToken<SyntaxKind>

typealias AwaitKeywordToken = AwaitKeyword

typealias AssertsToken = AssertsKeyword

typealias AbstractKeyword = ModifierToken<SyntaxKind>

typealias AsyncKeyword = ModifierToken<SyntaxKind>

typealias ConstKeyword = ModifierToken<SyntaxKind>

typealias DeclareKeyword = ModifierToken<SyntaxKind>

typealias DefaultKeyword = ModifierToken<SyntaxKind>

typealias ExportKeyword = ModifierToken<SyntaxKind>

typealias PrivateKeyword = ModifierToken<SyntaxKind>

typealias ProtectedKeyword = ModifierToken<SyntaxKind>

typealias PublicKeyword = ModifierToken<SyntaxKind>

typealias ReadonlyKeyword = ModifierToken<SyntaxKind>

typealias OverrideKeyword = ModifierToken<SyntaxKind>

typealias StaticKeyword = ModifierToken<SyntaxKind>

typealias ReadonlyToken = ReadonlyKeyword

typealias ModifiersArray = NodeArray<dynamic /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>

typealias IncrementExpression = UpdateExpression

//typealias ExponentiationOperator = SyntaxKind.AsteriskAsteriskToken

typealias BinaryOperatorToken = Token<dynamic /* SyntaxKind.QuestionQuestionToken | ExponentiationOperator | SyntaxKind.AsteriskToken | SyntaxKind.SlashToken | SyntaxKind.PercentToken | SyntaxKind.PlusToken | SyntaxKind.MinusToken | SyntaxKind.LessThanLessThanToken | SyntaxKind.GreaterThanGreaterThanToken | SyntaxKind.GreaterThanGreaterThanGreaterThanToken | SyntaxKind.LessThanToken | SyntaxKind.LessThanEqualsToken | SyntaxKind.GreaterThanToken | SyntaxKind.GreaterThanEqualsToken | SyntaxKind.InstanceOfKeyword | SyntaxKind.InKeyword | SyntaxKind.EqualsEqualsToken | SyntaxKind.EqualsEqualsEqualsToken | SyntaxKind.ExclamationEqualsEqualsToken | SyntaxKind.ExclamationEqualsToken | SyntaxKind.AmpersandToken | SyntaxKind.BarToken | SyntaxKind.CaretToken | SyntaxKind.AmpersandAmpersandToken | SyntaxKind.BarBarToken | SyntaxKind.EqualsToken | SyntaxKind.PlusEqualsToken | SyntaxKind.MinusEqualsToken | SyntaxKind.AsteriskAsteriskEqualsToken | SyntaxKind.AsteriskEqualsToken | SyntaxKind.SlashEqualsToken | SyntaxKind.PercentEqualsToken | SyntaxKind.AmpersandEqualsToken | SyntaxKind.BarEqualsToken | SyntaxKind.CaretEqualsToken | SyntaxKind.LessThanLessThanEqualsToken | SyntaxKind.GreaterThanGreaterThanGreaterThanEqualsToken | SyntaxKind.GreaterThanGreaterThanEqualsToken | SyntaxKind.BarBarEqualsToken | SyntaxKind.AmpersandAmpersandEqualsToken | SyntaxKind.QuestionQuestionEqualsToken | SyntaxKind.CommaToken */>

typealias AssignmentOperatorToken = Token<dynamic /* SyntaxKind.EqualsToken | SyntaxKind.PlusEqualsToken | SyntaxKind.MinusEqualsToken | SyntaxKind.AsteriskAsteriskEqualsToken | SyntaxKind.AsteriskEqualsToken | SyntaxKind.SlashEqualsToken | SyntaxKind.PercentEqualsToken | SyntaxKind.AmpersandEqualsToken | SyntaxKind.BarEqualsToken | SyntaxKind.CaretEqualsToken | SyntaxKind.LessThanLessThanEqualsToken | SyntaxKind.GreaterThanGreaterThanGreaterThanEqualsToken | SyntaxKind.GreaterThanGreaterThanEqualsToken | SyntaxKind.BarBarEqualsToken | SyntaxKind.AmpersandAmpersandEqualsToken | SyntaxKind.QuestionQuestionEqualsToken */>

typealias FunctionBody = Block

typealias WriteFileCallback = (fileName: String, data: String, writeByteOrderMark: Boolean, onError: (message: String) -> Unit, sourceFiles: Array<SourceFile>) -> Unit

typealias CustomTransformerFactory = (context: TransformationContext) -> CustomTransformer

typealias SymbolTable = UnderscoreEscapedMap<Symbol>

typealias JsFileExtensionInfo = FileExtensionInfo

typealias EmitHelperUniqueNameCallback = (name: String) -> String

typealias TransformerFactory<T> = (context: TransformationContext) -> Transformer<T>

typealias Transformer<T> = (node: T) -> T

typealias Visitor = (node: Node) -> dynamic

typealias FileWatcherCallback = (fileName: String, eventKind: FileWatcherEventKind) -> Unit

typealias DirectoryWatcherCallback = (fileName: String) -> Unit

typealias ErrorCallback = (message: DiagnosticMessage, length: Number) -> Unit

typealias DiagnosticReporter = (diagnostic: Diagnostic) -> Unit

typealias WatchStatusReporter = (diagnostic: Diagnostic, newLine: String, options: CompilerOptions, errorCount: Number) -> Unit

typealias CreateProgram<T> = (rootNames: Array<String>?, options: CompilerOptions?, host: CompilerHost, oldProgram: T, configFileParsingDiagnostics: Array<Diagnostic>, projectReferences: Array<ProjectReference>?) -> T

typealias ReportEmitErrorSummary = (errorCount: Number) -> Unit

typealias CodeActionCommand = InstallPackageAction
