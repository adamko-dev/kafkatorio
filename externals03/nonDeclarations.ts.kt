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

typealias DotToken = PunctuationToken<SyntaxKind.DotToken>

typealias DotDotDotToken = PunctuationToken<SyntaxKind.DotDotDotToken>

typealias QuestionToken = PunctuationToken<SyntaxKind.QuestionToken>

typealias ExclamationToken = PunctuationToken<SyntaxKind.ExclamationToken>

typealias ColonToken = PunctuationToken<SyntaxKind.ColonToken>

typealias EqualsToken = PunctuationToken<SyntaxKind.EqualsToken>

typealias AsteriskToken = PunctuationToken<SyntaxKind.AsteriskToken>

typealias EqualsGreaterThanToken = PunctuationToken<SyntaxKind.EqualsGreaterThanToken>

typealias PlusToken = PunctuationToken<SyntaxKind.PlusToken>

typealias MinusToken = PunctuationToken<SyntaxKind.MinusToken>

typealias QuestionDotToken = PunctuationToken<SyntaxKind.QuestionDotToken>

typealias AssertsKeyword = KeywordToken<SyntaxKind.AssertsKeyword>

typealias AwaitKeyword = KeywordToken<SyntaxKind.AwaitKeyword>

typealias AwaitKeywordToken = AwaitKeyword

typealias AssertsToken = AssertsKeyword

typealias AbstractKeyword = ModifierToken<SyntaxKind.AbstractKeyword>

typealias AsyncKeyword = ModifierToken<SyntaxKind.AsyncKeyword>

typealias ConstKeyword = ModifierToken<SyntaxKind.ConstKeyword>

typealias DeclareKeyword = ModifierToken<SyntaxKind.DeclareKeyword>

typealias DefaultKeyword = ModifierToken<SyntaxKind.DefaultKeyword>

typealias ExportKeyword = ModifierToken<SyntaxKind.ExportKeyword>

typealias PrivateKeyword = ModifierToken<SyntaxKind.PrivateKeyword>

typealias ProtectedKeyword = ModifierToken<SyntaxKind.ProtectedKeyword>

typealias PublicKeyword = ModifierToken<SyntaxKind.PublicKeyword>

typealias ReadonlyKeyword = ModifierToken<SyntaxKind.ReadonlyKeyword>

typealias OverrideKeyword = ModifierToken<SyntaxKind.OverrideKeyword>

typealias StaticKeyword = ModifierToken<SyntaxKind.StaticKeyword>

typealias ReadonlyToken = ReadonlyKeyword

typealias ModifiersArray = NodeArray<dynamic /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>

typealias IncrementExpression = UpdateExpression

typealias ExponentiationOperator = SyntaxKind.AsteriskAsteriskToken

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

typealias DiagnosticReporter = (diagnostic: Diagnostic) -> Unit

typealias CodeActionCommand = InstallPackageAction