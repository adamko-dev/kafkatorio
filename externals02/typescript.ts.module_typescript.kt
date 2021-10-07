@file:JsModule("typescript")
@file:JsNonModule
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

external var versionMajorMinor: Any

external var version: String

external interface MapLike<T> {
  @nativeGetter
  operator fun get(index: String): T?
  @nativeSetter
  operator fun set(index: String, value: T)
}

external interface ReadonlyCollection<K> {
  val size: Number
  fun has(key: K): Boolean
  fun keys(): Iterator<K>
}

external interface Collection<K> : ReadonlyCollection<K> {
  fun delete(key: K): Boolean
  fun clear()
}

external interface ReadonlyESMap<K, V> : ReadonlyCollection<K> {
  fun get(key: K): V?
  fun values(): Iterator<V>
  fun entries(): Iterator<dynamic /* JsTuple<K, V> */>
  fun forEach(action: (value: V, key: K) -> Unit)
}

external interface ReadonlyMap<T> : ReadonlyESMap<String, T>

external interface ESMap<K, V> : ReadonlyESMap<K, V>, Collection<K> {
  fun set(key: K, value: V): ESMap<K, V> /* this */
}

external interface Map<T> : ESMap<String, T>

external interface ReadonlySet<T> : ReadonlyCollection<T> {
  override fun has(key: T): Boolean
  fun values(): Iterator<T>
  fun entries(): Iterator<dynamic /* JsTuple<T, T> */>
  fun forEach(action: (value: T, key: T) -> Unit)
}

external interface Set<T> : ReadonlySet<T>, Collection<T> {
  fun add(value: T): Set<T> /* this */
  override fun delete(key: T): Boolean
}

external interface `T$1`<T> {
  var value: T
  var done: Boolean?
    get() = definedExternally
    set(value) = definedExternally
}

external interface `T$2` {
  var value: Unit
  var done: Boolean?
}

external interface Iterator<T> {
  fun next(): dynamic /* `T$1`<T> | `T$2` */
}

external interface Push<T> {
  fun push(vararg values: T)
}

external interface `T$3` {
  var __pathBrand: Any
}

external interface TextRange {
  var pos: Number
  var end: Number
}

external interface ReadonlyTextRange {
  val pos: Number
  val end: Number
}

external enum class SyntaxKind {
  Unknown /* = 0 */,
  EndOfFileToken /* = 1 */,
  SingleLineCommentTrivia /* = 2 */,
  MultiLineCommentTrivia /* = 3 */,
  NewLineTrivia /* = 4 */,
  WhitespaceTrivia /* = 5 */,
  ShebangTrivia /* = 6 */,
  ConflictMarkerTrivia /* = 7 */,
  NumericLiteral /* = 8 */,
  BigIntLiteral /* = 9 */,
  StringLiteral /* = 10 */,
  JsxText /* = 11 */,
  JsxTextAllWhiteSpaces /* = 12 */,
  RegularExpressionLiteral /* = 13 */,
  NoSubstitutionTemplateLiteral /* = 14 */,
  TemplateHead /* = 15 */,
  TemplateMiddle /* = 16 */,
  TemplateTail /* = 17 */,
  OpenBraceToken /* = 18 */,
  CloseBraceToken /* = 19 */,
  OpenParenToken /* = 20 */,
  CloseParenToken /* = 21 */,
  OpenBracketToken /* = 22 */,
  CloseBracketToken /* = 23 */,
  DotToken /* = 24 */,
  DotDotDotToken /* = 25 */,
  SemicolonToken /* = 26 */,
  CommaToken /* = 27 */,
  QuestionDotToken /* = 28 */,
  LessThanToken /* = 29 */,
  LessThanSlashToken /* = 30 */,
  GreaterThanToken /* = 31 */,
  LessThanEqualsToken /* = 32 */,
  GreaterThanEqualsToken /* = 33 */,
  EqualsEqualsToken /* = 34 */,
  ExclamationEqualsToken /* = 35 */,
  EqualsEqualsEqualsToken /* = 36 */,
  ExclamationEqualsEqualsToken /* = 37 */,
  EqualsGreaterThanToken /* = 38 */,
  PlusToken /* = 39 */,
  MinusToken /* = 40 */,
  AsteriskToken /* = 41 */,
  AsteriskAsteriskToken /* = 42 */,
  SlashToken /* = 43 */,
  PercentToken /* = 44 */,
  PlusPlusToken /* = 45 */,
  MinusMinusToken /* = 46 */,
  LessThanLessThanToken /* = 47 */,
  GreaterThanGreaterThanToken /* = 48 */,
  GreaterThanGreaterThanGreaterThanToken /* = 49 */,
  AmpersandToken /* = 50 */,
  BarToken /* = 51 */,
  CaretToken /* = 52 */,
  ExclamationToken /* = 53 */,
  TildeToken /* = 54 */,
  AmpersandAmpersandToken /* = 55 */,
  BarBarToken /* = 56 */,
  QuestionToken /* = 57 */,
  ColonToken /* = 58 */,
  AtToken /* = 59 */,
  QuestionQuestionToken /* = 60 */,
  BacktickToken /* = 61 */,
  EqualsToken /* = 62 */,
  PlusEqualsToken /* = 63 */,
  MinusEqualsToken /* = 64 */,
  AsteriskEqualsToken /* = 65 */,
  AsteriskAsteriskEqualsToken /* = 66 */,
  SlashEqualsToken /* = 67 */,
  PercentEqualsToken /* = 68 */,
  LessThanLessThanEqualsToken /* = 69 */,
  GreaterThanGreaterThanEqualsToken /* = 70 */,
  GreaterThanGreaterThanGreaterThanEqualsToken /* = 71 */,
  AmpersandEqualsToken /* = 72 */,
  BarEqualsToken /* = 73 */,
  BarBarEqualsToken /* = 74 */,
  AmpersandAmpersandEqualsToken /* = 75 */,
  QuestionQuestionEqualsToken /* = 76 */,
  CaretEqualsToken /* = 77 */,
  Identifier /* = 78 */,
  PrivateIdentifier /* = 79 */,
  BreakKeyword /* = 80 */,
  CaseKeyword /* = 81 */,
  CatchKeyword /* = 82 */,
  ClassKeyword /* = 83 */,
  ConstKeyword /* = 84 */,
  ContinueKeyword /* = 85 */,
  DebuggerKeyword /* = 86 */,
  DefaultKeyword /* = 87 */,
  DeleteKeyword /* = 88 */,
  DoKeyword /* = 89 */,
  ElseKeyword /* = 90 */,
  EnumKeyword /* = 91 */,
  ExportKeyword /* = 92 */,
  ExtendsKeyword /* = 93 */,
  FalseKeyword /* = 94 */,
  FinallyKeyword /* = 95 */,
  ForKeyword /* = 96 */,
  FunctionKeyword /* = 97 */,
  IfKeyword /* = 98 */,
  ImportKeyword /* = 99 */,
  InKeyword /* = 100 */,
  InstanceOfKeyword /* = 101 */,
  NewKeyword /* = 102 */,
  NullKeyword /* = 103 */,
  ReturnKeyword /* = 104 */,
  SuperKeyword /* = 105 */,
  SwitchKeyword /* = 106 */,
  ThisKeyword /* = 107 */,
  ThrowKeyword /* = 108 */,
  TrueKeyword /* = 109 */,
  TryKeyword /* = 110 */,
  TypeOfKeyword /* = 111 */,
  VarKeyword /* = 112 */,
  VoidKeyword /* = 113 */,
  WhileKeyword /* = 114 */,
  WithKeyword /* = 115 */,
  ImplementsKeyword /* = 116 */,
  InterfaceKeyword /* = 117 */,
  LetKeyword /* = 118 */,
  PackageKeyword /* = 119 */,
  PrivateKeyword /* = 120 */,
  ProtectedKeyword /* = 121 */,
  PublicKeyword /* = 122 */,
  StaticKeyword /* = 123 */,
  YieldKeyword /* = 124 */,
  AbstractKeyword /* = 125 */,
  AsKeyword /* = 126 */,
  AssertsKeyword /* = 127 */,
  AnyKeyword /* = 128 */,
  AsyncKeyword /* = 129 */,
  AwaitKeyword /* = 130 */,
  BooleanKeyword /* = 131 */,
  ConstructorKeyword /* = 132 */,
  DeclareKeyword /* = 133 */,
  GetKeyword /* = 134 */,
  InferKeyword /* = 135 */,
  IntrinsicKeyword /* = 136 */,
  IsKeyword /* = 137 */,
  KeyOfKeyword /* = 138 */,
  ModuleKeyword /* = 139 */,
  NamespaceKeyword /* = 140 */,
  NeverKeyword /* = 141 */,
  ReadonlyKeyword /* = 142 */,
  RequireKeyword /* = 143 */,
  NumberKeyword /* = 144 */,
  ObjectKeyword /* = 145 */,
  SetKeyword /* = 146 */,
  StringKeyword /* = 147 */,
  SymbolKeyword /* = 148 */,
  TypeKeyword /* = 149 */,
  UndefinedKeyword /* = 150 */,
  UniqueKeyword /* = 151 */,
  UnknownKeyword /* = 152 */,
  FromKeyword /* = 153 */,
  GlobalKeyword /* = 154 */,
  BigIntKeyword /* = 155 */,
  OverrideKeyword /* = 156 */,
  OfKeyword /* = 157 */,
  QualifiedName /* = 158 */,
  ComputedPropertyName /* = 159 */,
  TypeParameter /* = 160 */,
  Parameter /* = 161 */,
  Decorator /* = 162 */,
  PropertySignature /* = 163 */,
  PropertyDeclaration /* = 164 */,
  MethodSignature /* = 165 */,
  MethodDeclaration /* = 166 */,
  Constructor /* = 167 */,
  GetAccessor /* = 168 */,
  SetAccessor /* = 169 */,
  CallSignature /* = 170 */,
  ConstructSignature /* = 171 */,
  IndexSignature /* = 172 */,
  TypePredicate /* = 173 */,
  TypeReference /* = 174 */,
  FunctionType /* = 175 */,
  ConstructorType /* = 176 */,
  TypeQuery /* = 177 */,
  TypeLiteral /* = 178 */,
  ArrayType /* = 179 */,
  TupleType /* = 180 */,
  OptionalType /* = 181 */,
  RestType /* = 182 */,
  UnionType /* = 183 */,
  IntersectionType /* = 184 */,
  ConditionalType /* = 185 */,
  InferType /* = 186 */,
  ParenthesizedType /* = 187 */,
  ThisType /* = 188 */,
  TypeOperator /* = 189 */,
  IndexedAccessType /* = 190 */,
  MappedType /* = 191 */,
  LiteralType /* = 192 */,
  NamedTupleMember /* = 193 */,
  TemplateLiteralType /* = 194 */,
  TemplateLiteralTypeSpan /* = 195 */,
  ImportType /* = 196 */,
  ObjectBindingPattern /* = 197 */,
  ArrayBindingPattern /* = 198 */,
  BindingElement /* = 199 */,
  ArrayLiteralExpression /* = 200 */,
  ObjectLiteralExpression /* = 201 */,
  PropertyAccessExpression /* = 202 */,
  ElementAccessExpression /* = 203 */,
  CallExpression /* = 204 */,
  NewExpression /* = 205 */,
  TaggedTemplateExpression /* = 206 */,
  TypeAssertionExpression /* = 207 */,
  ParenthesizedExpression /* = 208 */,
  FunctionExpression /* = 209 */,
  ArrowFunction /* = 210 */,
  DeleteExpression /* = 211 */,
  TypeOfExpression /* = 212 */,
  VoidExpression /* = 213 */,
  AwaitExpression /* = 214 */,
  PrefixUnaryExpression /* = 215 */,
  PostfixUnaryExpression /* = 216 */,
  BinaryExpression /* = 217 */,
  ConditionalExpression /* = 218 */,
  TemplateExpression /* = 219 */,
  YieldExpression /* = 220 */,
  SpreadElement /* = 221 */,
  ClassExpression /* = 222 */,
  OmittedExpression /* = 223 */,
  ExpressionWithTypeArguments /* = 224 */,
  AsExpression /* = 225 */,
  NonNullExpression /* = 226 */,
  MetaProperty /* = 227 */,
  SyntheticExpression /* = 228 */,
  TemplateSpan /* = 229 */,
  SemicolonClassElement /* = 230 */,
  Block /* = 231 */,
  EmptyStatement /* = 232 */,
  VariableStatement /* = 233 */,
  ExpressionStatement /* = 234 */,
  IfStatement /* = 235 */,
  DoStatement /* = 236 */,
  WhileStatement /* = 237 */,
  ForStatement /* = 238 */,
  ForInStatement /* = 239 */,
  ForOfStatement /* = 240 */,
  ContinueStatement /* = 241 */,
  BreakStatement /* = 242 */,
  ReturnStatement /* = 243 */,
  WithStatement /* = 244 */,
  SwitchStatement /* = 245 */,
  LabeledStatement /* = 246 */,
  ThrowStatement /* = 247 */,
  TryStatement /* = 248 */,
  DebuggerStatement /* = 249 */,
  VariableDeclaration /* = 250 */,
  VariableDeclarationList /* = 251 */,
  FunctionDeclaration /* = 252 */,
  ClassDeclaration /* = 253 */,
  InterfaceDeclaration /* = 254 */,
  TypeAliasDeclaration /* = 255 */,
  EnumDeclaration /* = 256 */,
  ModuleDeclaration /* = 257 */,
  ModuleBlock /* = 258 */,
  CaseBlock /* = 259 */,
  NamespaceExportDeclaration /* = 260 */,
  ImportEqualsDeclaration /* = 261 */,
  ImportDeclaration /* = 262 */,
  ImportClause /* = 263 */,
  NamespaceImport /* = 264 */,
  NamedImports /* = 265 */,
  ImportSpecifier /* = 266 */,
  ExportAssignment /* = 267 */,
  ExportDeclaration /* = 268 */,
  NamedExports /* = 269 */,
  NamespaceExport /* = 270 */,
  ExportSpecifier /* = 271 */,
  MissingDeclaration /* = 272 */,
  ExternalModuleReference /* = 273 */,
  JsxElement /* = 274 */,
  JsxSelfClosingElement /* = 275 */,
  JsxOpeningElement /* = 276 */,
  JsxClosingElement /* = 277 */,
  JsxFragment /* = 278 */,
  JsxOpeningFragment /* = 279 */,
  JsxClosingFragment /* = 280 */,
  JsxAttribute /* = 281 */,
  JsxAttributes /* = 282 */,
  JsxSpreadAttribute /* = 283 */,
  JsxExpression /* = 284 */,
  CaseClause /* = 285 */,
  DefaultClause /* = 286 */,
  HeritageClause /* = 287 */,
  CatchClause /* = 288 */,
  PropertyAssignment /* = 289 */,
  ShorthandPropertyAssignment /* = 290 */,
  SpreadAssignment /* = 291 */,
  EnumMember /* = 292 */,
  UnparsedPrologue /* = 293 */,
  UnparsedPrepend /* = 294 */,
  UnparsedText /* = 295 */,
  UnparsedInternalText /* = 296 */,
  UnparsedSyntheticReference /* = 297 */,
  SourceFile /* = 298 */,
  Bundle /* = 299 */,
  UnparsedSource /* = 300 */,
  InputFiles /* = 301 */,
  JSDocTypeExpression /* = 302 */,
  JSDocNameReference /* = 303 */,
  JSDocAllType /* = 304 */,
  JSDocUnknownType /* = 305 */,
  JSDocNullableType /* = 306 */,
  JSDocNonNullableType /* = 307 */,
  JSDocOptionalType /* = 308 */,
  JSDocFunctionType /* = 309 */,
  JSDocVariadicType /* = 310 */,
  JSDocNamepathType /* = 311 */,
  JSDocComment /* = 312 */,
  JSDocText /* = 313 */,
  JSDocTypeLiteral /* = 314 */,
  JSDocSignature /* = 315 */,
  JSDocLink /* = 316 */,
  JSDocTag /* = 317 */,
  JSDocAugmentsTag /* = 318 */,
  JSDocImplementsTag /* = 319 */,
  JSDocAuthorTag /* = 320 */,
  JSDocDeprecatedTag /* = 321 */,
  JSDocClassTag /* = 322 */,
  JSDocPublicTag /* = 323 */,
  JSDocPrivateTag /* = 324 */,
  JSDocProtectedTag /* = 325 */,
  JSDocReadonlyTag /* = 326 */,
  JSDocOverrideTag /* = 327 */,
  JSDocCallbackTag /* = 328 */,
  JSDocEnumTag /* = 329 */,
  JSDocParameterTag /* = 330 */,
  JSDocReturnTag /* = 331 */,
  JSDocThisTag /* = 332 */,
  JSDocTypeTag /* = 333 */,
  JSDocTemplateTag /* = 334 */,
  JSDocTypedefTag /* = 335 */,
  JSDocSeeTag /* = 336 */,
  JSDocPropertyTag /* = 337 */,
  SyntaxList /* = 338 */,
  NotEmittedStatement /* = 339 */,
  PartiallyEmittedExpression /* = 340 */,
  CommaListExpression /* = 341 */,
  MergeDeclarationMarker /* = 342 */,
  EndOfDeclarationMarker /* = 343 */,
  SyntheticReferenceExpression /* = 344 */,
  Count /* = 345 */,
  FirstAssignment /* = 62 */,
  LastAssignment /* = 77 */,
  FirstCompoundAssignment /* = 63 */,
  LastCompoundAssignment /* = 77 */,
  FirstReservedWord /* = 80 */,
  LastReservedWord /* = 115 */,
  FirstKeyword /* = 80 */,
  LastKeyword /* = 157 */,
  FirstFutureReservedWord /* = 116 */,
  LastFutureReservedWord /* = 124 */,
  FirstTypeNode /* = 173 */,
  LastTypeNode /* = 196 */,
  FirstPunctuation /* = 18 */,
  LastPunctuation /* = 77 */,
  FirstToken /* = 0 */,
  LastToken /* = 157 */,
  FirstTriviaToken /* = 2 */,
  LastTriviaToken /* = 7 */,
  FirstLiteralToken /* = 8 */,
  LastLiteralToken /* = 14 */,
  FirstTemplateToken /* = 14 */,
  LastTemplateToken /* = 17 */,
  FirstBinaryOperator /* = 29 */,
  LastBinaryOperator /* = 77 */,
  FirstStatement /* = 233 */,
  LastStatement /* = 249 */,
  FirstNode /* = 158 */,
  FirstJSDocNode /* = 302 */,
  LastJSDocNode /* = 337 */,
  FirstJSDocTagNode /* = 317 */,
  LastJSDocTagNode /* = 337 */
}

external enum class NodeFlags {
  None /* = 0 */,
  Let /* = 1 */,
  Const /* = 2 */,
  NestedNamespace /* = 4 */,
  Synthesized /* = 8 */,
  Namespace /* = 16 */,
  OptionalChain /* = 32 */,
  ExportContext /* = 64 */,
  ContainsThis /* = 128 */,
  HasImplicitReturn /* = 256 */,
  HasExplicitReturn /* = 512 */,
  GlobalAugmentation /* = 1024 */,
  HasAsyncFunctions /* = 2048 */,
  DisallowInContext /* = 4096 */,
  YieldContext /* = 8192 */,
  DecoratorContext /* = 16384 */,
  AwaitContext /* = 32768 */,
  ThisNodeHasError /* = 65536 */,
  JavaScriptFile /* = 131072 */,
  ThisNodeOrAnySubNodesHasError /* = 262144 */,
  HasAggregatedChildData /* = 524288 */,
  JSDoc /* = 4194304 */,
  JsonFile /* = 33554432 */,
  BlockScoped /* = 3 */,
  ReachabilityCheckFlags /* = 768 */,
  ReachabilityAndEmitFlags /* = 2816 */,
  ContextFlags /* = 25358336 */,
  TypeExcludesFlags /* = 40960 */
}

external enum class ModifierFlags {
  None /* = 0 */,
  Export /* = 1 */,
  Ambient /* = 2 */,
  Public /* = 4 */,
  Private /* = 8 */,
  Protected /* = 16 */,
  Static /* = 32 */,
  Readonly /* = 64 */,
  Abstract /* = 128 */,
  Async /* = 256 */,
  Default /* = 512 */,
  Const /* = 2048 */,
  HasComputedJSDocModifiers /* = 4096 */,
  Deprecated /* = 8192 */,
  Override /* = 16384 */,
  HasComputedFlags /* = 536870912 */,
  AccessibilityModifier /* = 28 */,
  ParameterPropertyModifier /* = 16476 */,
  NonPublicAccessibilityModifier /* = 24 */,
  TypeScriptModifier /* = 18654 */,
  ExportDefault /* = 513 */,
  All /* = 27647 */
}

external enum class JsxFlags {
  None /* = 0 */,
  IntrinsicNamedElement /* = 1 */,
  IntrinsicIndexedElement /* = 2 */,
  IntrinsicElement /* = 3 */
}

external interface Node : ReadonlyTextRange {
  fun getSourceFile(): SourceFile
  fun getChildCount(sourceFile: SourceFile = definedExternally): Number
  fun getChildAt(index: Number, sourceFile: SourceFile = definedExternally): Node
  fun getChildren(sourceFile: SourceFile = definedExternally): Array<Node>
  fun getStart(sourceFile: SourceFile = definedExternally, includeJsDocComment: Boolean = definedExternally): Number
  fun getFullStart(): Number
  fun getEnd(): Number
  fun getWidth(sourceFile: SourceFileLike = definedExternally): Number
  fun getFullWidth(): Number
  fun getLeadingTriviaWidth(sourceFile: SourceFile = definedExternally): Number
  fun getFullText(sourceFile: SourceFile = definedExternally): String
  fun getText(sourceFile: SourceFile = definedExternally): String
  fun getFirstToken(sourceFile: SourceFile = definedExternally): Node?
  fun getLastToken(sourceFile: SourceFile = definedExternally): Node?
  fun <T> forEachChild(cbNode: (node: Node) -> T?, cbNodeArray: (nodes: NodeArray<Node>) -> T? = definedExternally): T?
  val kind: SyntaxKind
  val flags: NodeFlags
  val decorators: NodeArray<Decorator>?
    get() = definedExternally
  val modifiers: ModifiersArray?
    get() = definedExternally
  val parent: Node
}

external interface JSDocContainer

external interface Token<TKind : SyntaxKind> : Node {
  override val kind: TKind
}

external interface PunctuationToken<TKind : SyntaxKind> : Token<TKind>

external interface KeywordToken<TKind : SyntaxKind> : Token<TKind>

external interface ModifierToken<TKind : SyntaxKind> : KeywordToken<TKind>

external enum class GeneratedIdentifierFlags {
  None /* = 0 */,
  ReservedInNestedScopes /* = 8 */,
  Optimistic /* = 16 */,
  FileLevel /* = 32 */,
  AllowNameSubstitution /* = 64 */
}

external interface Identifier : PrimaryExpression, Declaration {
  val text: String
  val escapedText: dynamic /* String & `T$8` | Unit & `T$8` | InternalSymbolName */
    get() = definedExternally
  val originalKeywordKind: SyntaxKind?
    get() = definedExternally
  var isInJSDocNamespace: Boolean?
    get() = definedExternally
    set(value) = definedExternally
}

external interface TransientIdentifier : Identifier {
  var resolvedSymbol: Symbol
}

external interface QualifiedName : Node {
  val left: dynamic /* Identifier | QualifiedName */
    get() = definedExternally
  val right: Identifier
}

external interface Declaration : Node {
  var _declarationBrand: Any
}

external interface NamedDeclaration : Declaration {
  val name: dynamic /* Identifier? | PrivateIdentifier? | StringLiteral? | NoSubstitutionTemplateLiteral? | NumericLiteral? | ComputedPropertyName? | ElementAccessExpression? | ObjectBindingPattern? | ArrayBindingPattern? | PropertyAccessEntityNameExpression? */
    get() = definedExternally
}

external interface DeclarationStatement : NamedDeclaration, Statement {
  override val name: dynamic /* Identifier? | StringLiteral? | NumericLiteral? */
    get() = definedExternally
}

external interface ComputedPropertyName : Node {
  override val parent: Declaration
  val expression: Expression
}

external interface PrivateIdentifier : Node {
  val text: String
  val escapedText: dynamic /* String & `T$8` | Unit & `T$8` | InternalSymbolName */
    get() = definedExternally
}

external interface Decorator : Node {
  override val parent: NamedDeclaration
  val expression: LeftHandSideExpression
}

external interface TypeParameterDeclaration : NamedDeclaration {
  override val parent: dynamic /* CallSignatureDeclaration | ConstructSignatureDeclaration | MethodSignature | IndexSignatureDeclaration | FunctionTypeNode | ConstructorTypeNode | JSDocFunctionType | FunctionDeclaration | MethodDeclaration | ConstructorDeclaration | GetAccessorDeclaration | SetAccessorDeclaration | FunctionExpression | ArrowFunction | ClassDeclaration | ClassExpression | InterfaceDeclaration | TypeAliasDeclaration | JSDocTemplateTag | InferTypeNode */
    get() = definedExternally
  override val name: Identifier
  val constraint: TypeNode?
    get() = definedExternally
  val default: TypeNode?
    get() = definedExternally
  var expression: Expression?
    get() = definedExternally
    set(value) = definedExternally
}

external interface SignatureDeclarationBase : NamedDeclaration, JSDocContainer {
  override val name: dynamic /* Identifier? | StringLiteral? | NumericLiteral? | ComputedPropertyName? | PrivateIdentifier? */
    get() = definedExternally
  val typeParameters: NodeArray<TypeParameterDeclaration>?
    get() = definedExternally
  val parameters: NodeArray<ParameterDeclaration>
  val type: TypeNode?
    get() = definedExternally
}

external interface CallSignatureDeclaration : SignatureDeclarationBase, TypeElement {
  override val kind: SyntaxKind
  override val name: dynamic /* Identifier? | StringLiteral? | NumericLiteral? | ComputedPropertyName? | PrivateIdentifier? */
    get() = definedExternally
}

external interface ConstructSignatureDeclaration : SignatureDeclarationBase, TypeElement {
  override val kind: SyntaxKind
  override val name: dynamic /* Identifier? | StringLiteral? | NumericLiteral? | ComputedPropertyName? | PrivateIdentifier? */
    get() = definedExternally
}

external interface VariableDeclaration : NamedDeclaration, JSDocContainer {
  override val parent: dynamic /* VariableDeclarationList | CatchClause */
    get() = definedExternally
  override val name: dynamic /* Identifier | ObjectBindingPattern | ArrayBindingPattern */
    get() = definedExternally
  val exclamationToken: ExclamationToken?
    get() = definedExternally
  val type: TypeNode?
    get() = definedExternally
  val initializer: Expression?
    get() = definedExternally
}

external interface VariableDeclarationList : Node {
  override val parent: dynamic /* VariableStatement | ForStatement | ForOfStatement | ForInStatement */
    get() = definedExternally
  val declarations: NodeArray<VariableDeclaration>
}

external interface ParameterDeclaration : NamedDeclaration, JSDocContainer {
  override val parent: dynamic /* CallSignatureDeclaration | ConstructSignatureDeclaration | MethodSignature | IndexSignatureDeclaration | FunctionTypeNode | ConstructorTypeNode | JSDocFunctionType | FunctionDeclaration | MethodDeclaration | ConstructorDeclaration | GetAccessorDeclaration | SetAccessorDeclaration | FunctionExpression | ArrowFunction */
    get() = definedExternally
  val dotDotDotToken: DotDotDotToken?
    get() = definedExternally
  override val name: dynamic /* Identifier | ObjectBindingPattern | ArrayBindingPattern */
    get() = definedExternally
  val questionToken: QuestionToken?
    get() = definedExternally
  val type: TypeNode?
    get() = definedExternally
  val initializer: Expression?
    get() = definedExternally
}

external interface BindingElement : NamedDeclaration {
  override val parent: dynamic /* ObjectBindingPattern | ArrayBindingPattern */
    get() = definedExternally
  val propertyName: dynamic /* Identifier? | StringLiteral? | NumericLiteral? | ComputedPropertyName? | PrivateIdentifier? */
    get() = definedExternally
  val dotDotDotToken: DotDotDotToken?
    get() = definedExternally
  override val name: dynamic /* Identifier | ObjectBindingPattern | ArrayBindingPattern */
    get() = definedExternally
  val initializer: Expression?
    get() = definedExternally
}

external interface PropertySignature : TypeElement, JSDocContainer {
  override val name: dynamic /* Identifier | StringLiteral | NumericLiteral | ComputedPropertyName | PrivateIdentifier */
    get() = definedExternally
  override val questionToken: QuestionToken?
    get() = definedExternally
  val type: TypeNode?
    get() = definedExternally
  var initializer: Expression?
    get() = definedExternally
    set(value) = definedExternally
}

external interface PropertyDeclaration : ClassElement, JSDocContainer {
  override val parent: dynamic /* ClassDeclaration | ClassExpression */
    get() = definedExternally
  override val name: dynamic /* Identifier | StringLiteral | NumericLiteral | ComputedPropertyName | PrivateIdentifier */
    get() = definedExternally
  val questionToken: QuestionToken?
    get() = definedExternally
  val exclamationToken: ExclamationToken?
    get() = definedExternally
  val type: TypeNode?
    get() = definedExternally
  val initializer: Expression?
    get() = definedExternally
}

external interface ObjectLiteralElement : NamedDeclaration {
  var _objectLiteralBrand: Any
  override val name: dynamic /* Identifier? | StringLiteral? | NumericLiteral? | ComputedPropertyName? | PrivateIdentifier? */
    get() = definedExternally
}

external interface PropertyAssignment : ObjectLiteralElement, JSDocContainer {
  override val parent: ObjectLiteralExpression
  override val name: dynamic /* Identifier | StringLiteral | NumericLiteral | ComputedPropertyName | PrivateIdentifier */
    get() = definedExternally
  val questionToken: QuestionToken?
    get() = definedExternally
  val exclamationToken: ExclamationToken?
    get() = definedExternally
  val initializer: Expression
}

external interface ShorthandPropertyAssignment : ObjectLiteralElement, JSDocContainer {
  override val parent: ObjectLiteralExpression
  override val name: Identifier
  val questionToken: QuestionToken?
    get() = definedExternally
  val exclamationToken: ExclamationToken?
    get() = definedExternally
  val equalsToken: EqualsToken?
    get() = definedExternally
  val objectAssignmentInitializer: Expression?
    get() = definedExternally
}

external interface SpreadAssignment : ObjectLiteralElement, JSDocContainer {
  override val parent: ObjectLiteralExpression
  val expression: Expression
}

external interface PropertyLikeDeclaration : NamedDeclaration {
  override val name: dynamic /* Identifier | StringLiteral | NumericLiteral | ComputedPropertyName | PrivateIdentifier */
    get() = definedExternally
}

external interface ObjectBindingPattern : Node {
  override val parent: dynamic /* VariableDeclaration | ParameterDeclaration | BindingElement */
    get() = definedExternally
  val elements: NodeArray<BindingElement>
}

external interface ArrayBindingPattern : Node {
  override val parent: dynamic /* VariableDeclaration | ParameterDeclaration | BindingElement */
    get() = definedExternally
  val elements: NodeArray<dynamic /* BindingElement | OmittedExpression */>
}

external interface FunctionLikeDeclarationBase : SignatureDeclarationBase {
  var _functionLikeDeclarationBrand: Any
  val asteriskToken: AsteriskToken?
    get() = definedExternally
  val questionToken: QuestionToken?
    get() = definedExternally
  val exclamationToken: ExclamationToken?
    get() = definedExternally
  val body: dynamic /* Block? | Expression? */
    get() = definedExternally
}

external interface FunctionDeclaration : FunctionLikeDeclarationBase, DeclarationStatement {
  override val kind: SyntaxKind
  override val name: Identifier?
    get() = definedExternally
  override val body: FunctionBody?
    get() = definedExternally
}

external interface MethodSignature : SignatureDeclarationBase, TypeElement {
  override val kind: SyntaxKind
  override val parent: dynamic /* ClassDeclaration | ClassExpression | InterfaceDeclaration | TypeLiteralNode */
    get() = definedExternally
  override val name: dynamic /* Identifier | StringLiteral | NumericLiteral | ComputedPropertyName | PrivateIdentifier */
    get() = definedExternally
}

external interface MethodDeclaration : FunctionLikeDeclarationBase, ClassElement, ObjectLiteralElement, JSDocContainer {
  override val kind: SyntaxKind
  override val parent: dynamic /* ClassDeclaration | ClassExpression | ObjectLiteralExpression */
    get() = definedExternally
  override val name: dynamic /* Identifier | StringLiteral | NumericLiteral | ComputedPropertyName | PrivateIdentifier */
    get() = definedExternally
  override val body: FunctionBody?
    get() = definedExternally
}

external interface ConstructorDeclaration : FunctionLikeDeclarationBase, ClassElement, JSDocContainer {
  override val kind: SyntaxKind
  override val parent: dynamic /* ClassDeclaration | ClassExpression */
    get() = definedExternally
  override val body: FunctionBody?
    get() = definedExternally
  override val name: dynamic
    get() = definedExternally
}

external interface SemicolonClassElement : ClassElement {
  override val parent: dynamic /* ClassDeclaration | ClassExpression */
    get() = definedExternally
}

external interface GetAccessorDeclaration : FunctionLikeDeclarationBase, ClassElement, TypeElement, ObjectLiteralElement, JSDocContainer {
  override val kind: SyntaxKind
  override val parent: dynamic /* ClassDeclaration | ClassExpression | ObjectLiteralExpression | TypeLiteralNode | InterfaceDeclaration */
    get() = definedExternally
  override val name: dynamic /* Identifier | StringLiteral | NumericLiteral | ComputedPropertyName | PrivateIdentifier */
    get() = definedExternally
  override val body: FunctionBody?
    get() = definedExternally
  override val questionToken: QuestionToken?
    get() = definedExternally
}

external interface SetAccessorDeclaration : FunctionLikeDeclarationBase, ClassElement, TypeElement, ObjectLiteralElement, JSDocContainer {
  override val kind: SyntaxKind
  override val parent: dynamic /* ClassDeclaration | ClassExpression | ObjectLiteralExpression | TypeLiteralNode | InterfaceDeclaration */
    get() = definedExternally
  override val name: dynamic /* Identifier | StringLiteral | NumericLiteral | ComputedPropertyName | PrivateIdentifier */
    get() = definedExternally
  override val body: FunctionBody?
    get() = definedExternally
  override val questionToken: QuestionToken?
    get() = definedExternally
}

external interface IndexSignatureDeclaration : SignatureDeclarationBase, ClassElement, TypeElement {
  override val kind: SyntaxKind
  override val parent: dynamic /* ClassDeclaration | ClassExpression | InterfaceDeclaration | TypeLiteralNode */
    get() = definedExternally
  override val name: dynamic /* Identifier? | StringLiteral? | NumericLiteral? | ComputedPropertyName? | PrivateIdentifier? */
    get() = definedExternally
}

external interface TypeNode : Node {
  var _typeNodeBrand: Any
}

external interface KeywordTypeNode<TKind : SyntaxKind> : KeywordToken<TKind>, TypeNode {
  override val kind: TKind
}

external interface ImportTypeNode : NodeWithTypeArguments {
  val isTypeOf: Boolean?
  val argument: TypeNode
  val qualifier: dynamic /* Identifier? | QualifiedName? */
    get() = definedExternally
}

external interface ThisTypeNode : TypeNode

external interface FunctionOrConstructorTypeNodeBase : TypeNode, SignatureDeclarationBase {
  override val kind: dynamic /* SyntaxKind.FunctionType | SyntaxKind.ConstructorType */
    get() = definedExternally
}

external interface FunctionTypeNode : FunctionOrConstructorTypeNodeBase {
  override val kind: Any
}

external interface ConstructorTypeNode : FunctionOrConstructorTypeNodeBase {
  override val kind: Any
}

external interface NodeWithTypeArguments : TypeNode {
  val typeArguments: NodeArray<TypeNode>?
    get() = definedExternally
}

external interface TypeReferenceNode : NodeWithTypeArguments {
  val typeName: dynamic /* Identifier | QualifiedName */
    get() = definedExternally
}

external interface TypePredicateNode : TypeNode {
  override val parent: dynamic /* CallSignatureDeclaration | ConstructSignatureDeclaration | MethodSignature | IndexSignatureDeclaration | FunctionTypeNode | ConstructorTypeNode | JSDocFunctionType | FunctionDeclaration | MethodDeclaration | ConstructorDeclaration | GetAccessorDeclaration | SetAccessorDeclaration | FunctionExpression | ArrowFunction | JSDocTypeExpression */
    get() = definedExternally
  val assertsModifier: AssertsToken?
    get() = definedExternally
  val parameterName: dynamic /* Identifier | ThisTypeNode */
    get() = definedExternally
  val type: TypeNode?
    get() = definedExternally
}

external interface TypeQueryNode : TypeNode {
  val exprName: dynamic /* Identifier | QualifiedName */
    get() = definedExternally
}

external interface TypeLiteralNode : TypeNode, Declaration {
  val members: NodeArray<TypeElement>
}

external interface ArrayTypeNode : TypeNode {
  val elementType: TypeNode
}

external interface TupleTypeNode : TypeNode {
  val elements: NodeArray<dynamic /* TypeNode | NamedTupleMember */>
}

external interface NamedTupleMember : TypeNode, JSDocContainer, Declaration {
  val dotDotDotToken: Token<SyntaxKind>?
    get() = definedExternally
  val name: Identifier
  val questionToken: Token<SyntaxKind>?
    get() = definedExternally
  val type: TypeNode
}

external interface OptionalTypeNode : TypeNode {
  val type: TypeNode
}

external interface RestTypeNode : TypeNode {
  val type: TypeNode
}

external interface UnionTypeNode : TypeNode {
  val types: NodeArray<TypeNode>
}

external interface IntersectionTypeNode : TypeNode {
  val types: NodeArray<TypeNode>
}

external interface ConditionalTypeNode : TypeNode {
  val checkType: TypeNode
  val extendsType: TypeNode
  val trueType: TypeNode
  val falseType: TypeNode
}

external interface InferTypeNode : TypeNode {
  val typeParameter: TypeParameterDeclaration
}

external interface ParenthesizedTypeNode : TypeNode {
  val type: TypeNode
}

external interface TypeOperatorNode : TypeNode {
  val operator: dynamic /* SyntaxKind.KeyOfKeyword | SyntaxKind.UniqueKeyword | SyntaxKind.ReadonlyKeyword */
    get() = definedExternally
  val type: TypeNode
}

external interface IndexedAccessTypeNode : TypeNode {
  val objectType: TypeNode
  val indexType: TypeNode
}

external interface MappedTypeNode : TypeNode, Declaration {
  val readonlyToken: dynamic /* ReadonlyToken? | PlusToken? | MinusToken? */
    get() = definedExternally
  val typeParameter: TypeParameterDeclaration
  val nameType: TypeNode?
    get() = definedExternally
  val questionToken: dynamic /* QuestionToken? | PlusToken? | MinusToken? */
    get() = definedExternally
  val type: TypeNode?
    get() = definedExternally
}

external interface LiteralTypeNode : TypeNode {
  val literal: dynamic /* NullLiteral | TrueLiteral | FalseLiteral | LiteralExpression | PrefixUnaryExpression */
    get() = definedExternally
}

external interface StringLiteral : LiteralExpression, Declaration

external interface TemplateLiteralTypeNode : TypeNode {
  val head: TemplateHead
  val templateSpans: NodeArray<TemplateLiteralTypeSpan>
}

external interface TemplateLiteralTypeSpan : TypeNode {
  override val parent: TemplateLiteralTypeNode
  val type: TypeNode
  val literal: dynamic /* TemplateMiddle | TemplateTail */
    get() = definedExternally
}

external interface Expression : Node {
  var _expressionBrand: Any
}

external interface OmittedExpression : Expression

external interface PartiallyEmittedExpression : LeftHandSideExpression {
  val expression: Expression
}

external interface UnaryExpression : Expression {
  var _unaryExpressionBrand: Any
}

external interface UpdateExpression : UnaryExpression {
  var _updateExpressionBrand: Any
}

external interface PrefixUnaryExpression : UpdateExpression {
  val operator: dynamic /* SyntaxKind.PlusPlusToken | SyntaxKind.MinusMinusToken | SyntaxKind.PlusToken | SyntaxKind.MinusToken | SyntaxKind.TildeToken | SyntaxKind.ExclamationToken */
    get() = definedExternally
  val operand: UnaryExpression
}

external interface PostfixUnaryExpression : UpdateExpression {
  val operand: LeftHandSideExpression
  val operator: dynamic /* SyntaxKind.PlusPlusToken | SyntaxKind.MinusMinusToken */
    get() = definedExternally
}

external interface LeftHandSideExpression : UpdateExpression {
  var _leftHandSideExpressionBrand: Any
}

external interface MemberExpression : LeftHandSideExpression {
  var _memberExpressionBrand: Any
}

external interface PrimaryExpression : MemberExpression {
  var _primaryExpressionBrand: Any
}

external interface NullLiteral : PrimaryExpression

external interface TrueLiteral : PrimaryExpression

external interface FalseLiteral : PrimaryExpression

external interface ThisExpression : PrimaryExpression

external interface SuperExpression : PrimaryExpression

external interface ImportExpression : PrimaryExpression

external interface DeleteExpression : UnaryExpression {
  val expression: UnaryExpression
}

external interface TypeOfExpression : UnaryExpression {
  val expression: UnaryExpression
}

external interface VoidExpression : UnaryExpression {
  val expression: UnaryExpression
}

external interface AwaitExpression : UnaryExpression {
  val expression: UnaryExpression
}

external interface YieldExpression : Expression {
  val asteriskToken: AsteriskToken?
    get() = definedExternally
  val expression: Expression?
    get() = definedExternally
}

external interface SyntheticExpression : Expression {
  val isSpread: Boolean?
  val type: Type
  val tupleNameSource: dynamic /* ParameterDeclaration? | NamedTupleMember? */
    get() = definedExternally
}

external interface BinaryExpression : Expression, Declaration {
  val left: Expression
  val operatorToken: BinaryOperatorToken
  val right: Expression
}

external interface AssignmentExpression<TOperator : AssignmentOperatorToken> : BinaryExpression {
  override val left: LeftHandSideExpression
  override val operatorToken: TOperator
}

external interface ObjectDestructuringAssignment : AssignmentExpression<EqualsToken> {
  override val left: ObjectLiteralExpression
}

external interface ArrayDestructuringAssignment : AssignmentExpression<EqualsToken> {
  override val left: ArrayLiteralExpression
}

external interface ConditionalExpression : Expression {
  val condition: Expression
  val questionToken: QuestionToken
  val whenTrue: Expression
  val colonToken: ColonToken
  val whenFalse: Expression
}

external interface FunctionExpression : PrimaryExpression, FunctionLikeDeclarationBase, JSDocContainer {
  override val kind: SyntaxKind
  override val name: Identifier?
    get() = definedExternally
  override val body: FunctionBody
}

external interface ArrowFunction : Expression, FunctionLikeDeclarationBase, JSDocContainer {
  override val kind: SyntaxKind
  val equalsGreaterThanToken: EqualsGreaterThanToken
  override val body: dynamic /* FunctionBody | Expression */
    get() = definedExternally
  override val name: Any
}

external interface LiteralLikeNode : Node {
  var text: String
  var isUnterminated: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var hasExtendedUnicodeEscape: Boolean?
    get() = definedExternally
    set(value) = definedExternally
}

external interface TemplateLiteralLikeNode : LiteralLikeNode {
  var rawText: String?
    get() = definedExternally
    set(value) = definedExternally
}

external interface LiteralExpression : LiteralLikeNode, PrimaryExpression {
  var _literalExpressionBrand: Any
}

external interface RegularExpressionLiteral : LiteralExpression

external interface NoSubstitutionTemplateLiteral : LiteralExpression, TemplateLiteralLikeNode, Declaration

external enum class TokenFlags {
  None /* = 0 */,
  Scientific /* = 16 */,
  Octal /* = 32 */,
  HexSpecifier /* = 64 */,
  BinarySpecifier /* = 128 */,
  OctalSpecifier /* = 256 */
}

external interface NumericLiteral : LiteralExpression, Declaration

external interface BigIntLiteral : LiteralExpression

external interface TemplateHead : TemplateLiteralLikeNode {
  override val parent: dynamic /* TemplateExpression | TemplateLiteralTypeNode */
    get() = definedExternally
}

external interface TemplateMiddle : TemplateLiteralLikeNode {
  override val parent: dynamic /* TemplateSpan | TemplateLiteralTypeSpan */
    get() = definedExternally
}

external interface TemplateTail : TemplateLiteralLikeNode {
  override val parent: dynamic /* TemplateSpan | TemplateLiteralTypeSpan */
    get() = definedExternally
}

external interface TemplateExpression : PrimaryExpression {
  val head: TemplateHead
  val templateSpans: NodeArray<TemplateSpan>
}

external interface TemplateSpan : Node {
  override val parent: TemplateExpression
  val expression: Expression
  val literal: dynamic /* TemplateMiddle | TemplateTail */
    get() = definedExternally
}

external interface ParenthesizedExpression : PrimaryExpression, JSDocContainer {
  val expression: Expression
}

external interface ArrayLiteralExpression : PrimaryExpression {
  val elements: NodeArray<Expression>
}

external interface SpreadElement : Expression {
  override val parent: dynamic /* ArrayLiteralExpression | CallExpression | NewExpression */
    get() = definedExternally
  val expression: Expression
}

external interface ObjectLiteralExpressionBase<T : ObjectLiteralElement> : PrimaryExpression, Declaration {
  val properties: NodeArray<T>
}

external interface ObjectLiteralExpression : ObjectLiteralExpressionBase<dynamic /* PropertyAssignment | ShorthandPropertyAssignment | SpreadAssignment | MethodDeclaration | GetAccessorDeclaration | SetAccessorDeclaration */>

external interface PropertyAccessExpression : MemberExpression, NamedDeclaration {
  val expression: LeftHandSideExpression
  val questionDotToken: QuestionDotToken?
    get() = definedExternally
  override val name: dynamic /* Identifier | PrivateIdentifier */
    get() = definedExternally
}

external interface PropertyAccessChain : PropertyAccessExpression {
  var _optionalChainBrand: Any
  override val name: dynamic /* Identifier | PrivateIdentifier */
    get() = definedExternally
}

external interface SuperPropertyAccessExpression : PropertyAccessExpression {
  override val expression: SuperExpression
}

external interface PropertyAccessEntityNameExpression : PropertyAccessExpression {
  var _propertyAccessExpressionLikeQualifiedNameBrand: Any?
    get() = definedExternally
    set(value) = definedExternally
  override val expression: dynamic /* Identifier | PropertyAccessEntityNameExpression */
    get() = definedExternally
  override val name: Identifier
}

external interface ElementAccessExpression : MemberExpression {
  val expression: LeftHandSideExpression
  val questionDotToken: QuestionDotToken?
    get() = definedExternally
  val argumentExpression: Expression
}

external interface ElementAccessChain : ElementAccessExpression {
  var _optionalChainBrand: Any
}

external interface SuperElementAccessExpression : ElementAccessExpression {
  override val expression: SuperExpression
}

external interface CallExpression : LeftHandSideExpression, Declaration {
  val expression: LeftHandSideExpression
  val questionDotToken: QuestionDotToken?
    get() = definedExternally
  val typeArguments: NodeArray<TypeNode>?
    get() = definedExternally
  val arguments: NodeArray<Expression>
}

external interface CallChain : CallExpression {
  var _optionalChainBrand: Any
}

external interface SuperCall : CallExpression {
  override val expression: SuperExpression
}

external interface ImportCall : CallExpression {
  override val expression: ImportExpression
}

external interface ExpressionWithTypeArguments : NodeWithTypeArguments {
  override val parent: dynamic /* HeritageClause | JSDocAugmentsTag | JSDocImplementsTag */
    get() = definedExternally
  val expression: LeftHandSideExpression
}

external interface NewExpression : PrimaryExpression, Declaration {
  val expression: LeftHandSideExpression
  val typeArguments: NodeArray<TypeNode>?
    get() = definedExternally
  val arguments: NodeArray<Expression>?
    get() = definedExternally
}

external interface TaggedTemplateExpression : MemberExpression {
  val tag: LeftHandSideExpression
  val typeArguments: NodeArray<TypeNode>?
    get() = definedExternally
  val template: dynamic /* TemplateExpression | NoSubstitutionTemplateLiteral */
    get() = definedExternally
}

external interface AsExpression : Expression {
  val expression: Expression
  val type: TypeNode
}

external interface TypeAssertion : UnaryExpression {
  val type: TypeNode
  val expression: UnaryExpression
}

external interface NonNullExpression : LeftHandSideExpression {
  val expression: Expression
}

external interface NonNullChain : NonNullExpression {
  var _optionalChainBrand: Any
}

external interface MetaProperty : PrimaryExpression {
  val keywordToken: dynamic /* SyntaxKind.NewKeyword | SyntaxKind.ImportKeyword */
    get() = definedExternally
  val name: Identifier
}

external interface JsxElement : PrimaryExpression {
  val openingElement: JsxOpeningElement
  val children: NodeArray<dynamic /* JsxText | JsxExpression | JsxElement | JsxSelfClosingElement | JsxFragment */>
  val closingElement: JsxClosingElement
}

external interface JsxTagNamePropertyAccess : PropertyAccessExpression {
  override val expression: dynamic /* Identifier | ThisExpression | JsxTagNamePropertyAccess */
    get() = definedExternally
}

external interface JsxAttributes : ObjectLiteralExpressionBase<dynamic /* JsxAttribute | JsxSpreadAttribute */> {
  override val parent: dynamic /* JsxSelfClosingElement | JsxOpeningElement */
    get() = definedExternally
}

external interface JsxOpeningElement : Expression {
  override val parent: JsxElement
  val tagName: dynamic /* Identifier | ThisExpression | JsxTagNamePropertyAccess */
    get() = definedExternally
  val typeArguments: NodeArray<TypeNode>?
    get() = definedExternally
  val attributes: JsxAttributes
}

external interface JsxSelfClosingElement : PrimaryExpression {
  val tagName: dynamic /* Identifier | ThisExpression | JsxTagNamePropertyAccess */
    get() = definedExternally
  val typeArguments: NodeArray<TypeNode>?
    get() = definedExternally
  val attributes: JsxAttributes
}

external interface JsxFragment : PrimaryExpression {
  val openingFragment: JsxOpeningFragment
  val children: NodeArray<dynamic /* JsxText | JsxExpression | JsxElement | JsxSelfClosingElement | JsxFragment */>
  val closingFragment: JsxClosingFragment
}

external interface JsxOpeningFragment : Expression {
  override val parent: JsxFragment
}

external interface JsxClosingFragment : Expression {
  override val parent: JsxFragment
}

external interface JsxAttribute : ObjectLiteralElement {
  override val parent: JsxAttributes
  override val name: Identifier
  val initializer: dynamic /* StringLiteral? | JsxExpression? */
    get() = definedExternally
}

external interface JsxSpreadAttribute : ObjectLiteralElement {
  override val parent: JsxAttributes
  val expression: Expression
}

external interface JsxClosingElement : Node {
  override val parent: JsxElement
  val tagName: dynamic /* Identifier | ThisExpression | JsxTagNamePropertyAccess */
    get() = definedExternally
}

external interface JsxExpression : Expression {
  override val parent: dynamic /* JsxElement | JsxAttribute | JsxSpreadAttribute */
    get() = definedExternally
  val dotDotDotToken: Token<SyntaxKind>?
    get() = definedExternally
  val expression: Expression?
    get() = definedExternally
}

external interface JsxText : LiteralLikeNode {
  override val parent: JsxElement
  val containsOnlyTriviaWhiteSpaces: Boolean?
}

external interface Statement : Node, JSDocContainer {
  var _statementBrand: Any
}

external interface NotEmittedStatement : Statement

external interface CommaListExpression : Expression {
  val elements: NodeArray<Expression>
}

external interface EmptyStatement : Statement

external interface DebuggerStatement : Statement

external interface MissingDeclaration : DeclarationStatement {
  override val name: Identifier?
    get() = definedExternally
}

external interface Block : Statement {
  val statements: NodeArray<Statement>
}

external interface VariableStatement : Statement {
  val declarationList: VariableDeclarationList
}

external interface ExpressionStatement : Statement {
  val expression: Expression
}

external interface IfStatement : Statement {
  val expression: Expression
  val thenStatement: Statement
  val elseStatement: Statement?
    get() = definedExternally
}

external interface IterationStatement : Statement {
  val statement: Statement
}

external interface DoStatement : IterationStatement {
  val expression: Expression
}

external interface WhileStatement : IterationStatement {
  val expression: Expression
}

external interface ForStatement : IterationStatement {
  val initializer: dynamic /* VariableDeclarationList? | Expression? */
    get() = definedExternally
  val condition: Expression?
    get() = definedExternally
  val incrementor: Expression?
    get() = definedExternally
}

external interface ForInStatement : IterationStatement {
  val initializer: dynamic /* VariableDeclarationList | Expression */
    get() = definedExternally
  val expression: Expression
}

external interface ForOfStatement : IterationStatement {
  val awaitModifier: AwaitKeywordToken?
    get() = definedExternally
  val initializer: dynamic /* VariableDeclarationList | Expression */
    get() = definedExternally
  val expression: Expression
}

external interface BreakStatement : Statement {
  val label: Identifier?
    get() = definedExternally
}

external interface ContinueStatement : Statement {
  val label: Identifier?
    get() = definedExternally
}

external interface ReturnStatement : Statement {
  val expression: Expression?
    get() = definedExternally
}

external interface WithStatement : Statement {
  val expression: Expression
  val statement: Statement
}

external interface SwitchStatement : Statement {
  val expression: Expression
  val caseBlock: CaseBlock
  var possiblyExhaustive: Boolean?
    get() = definedExternally
    set(value) = definedExternally
}

external interface CaseBlock : Node {
  override val parent: SwitchStatement
  val clauses: NodeArray<dynamic /* CaseClause | DefaultClause */>
}

external interface CaseClause : Node {
  override val parent: CaseBlock
  val expression: Expression
  val statements: NodeArray<Statement>
}

external interface DefaultClause : Node {
  override val parent: CaseBlock
  val statements: NodeArray<Statement>
}

external interface LabeledStatement : Statement {
  val label: Identifier
  val statement: Statement
}

external interface ThrowStatement : Statement {
  val expression: Expression
}

external interface TryStatement : Statement {
  val tryBlock: Block
  val catchClause: CatchClause?
    get() = definedExternally
  val finallyBlock: Block?
    get() = definedExternally
}

external interface CatchClause : Node {
  override val parent: TryStatement
  val variableDeclaration: VariableDeclaration?
    get() = definedExternally
  val block: Block
}

external interface ClassLikeDeclarationBase : NamedDeclaration, JSDocContainer {
  override val kind: dynamic /* SyntaxKind.ClassDeclaration | SyntaxKind.ClassExpression */
    get() = definedExternally
  override val name: Identifier?
    get() = definedExternally
  val typeParameters: NodeArray<TypeParameterDeclaration>?
    get() = definedExternally
  val heritageClauses: NodeArray<HeritageClause>?
    get() = definedExternally
  val members: NodeArray<ClassElement>
}

external interface ClassDeclaration : ClassLikeDeclarationBase, DeclarationStatement {
  override val kind: SyntaxKind
  override val name: Identifier?
    get() = definedExternally
}

external interface ClassExpression : ClassLikeDeclarationBase, PrimaryExpression {
  override val kind: SyntaxKind
}

external interface ClassElement : NamedDeclaration {
  var _classElementBrand: Any
  override val name: dynamic /* Identifier? | StringLiteral? | NumericLiteral? | ComputedPropertyName? | PrivateIdentifier? */
    get() = definedExternally
}

external interface TypeElement : NamedDeclaration {
  var _typeElementBrand: Any
  override val name: dynamic /* Identifier? | StringLiteral? | NumericLiteral? | ComputedPropertyName? | PrivateIdentifier? */
    get() = definedExternally
  val questionToken: QuestionToken?
    get() = definedExternally
}

external interface InterfaceDeclaration : DeclarationStatement, JSDocContainer {
  override val name: Identifier
  val typeParameters: NodeArray<TypeParameterDeclaration>?
    get() = definedExternally
  val heritageClauses: NodeArray<HeritageClause>?
    get() = definedExternally
  val members: NodeArray<TypeElement>
}

external interface HeritageClause : Node {
  override val parent: dynamic /* InterfaceDeclaration | ClassDeclaration | ClassExpression */
    get() = definedExternally
  val token: dynamic /* SyntaxKind.ExtendsKeyword | SyntaxKind.ImplementsKeyword */
    get() = definedExternally
  val types: NodeArray<ExpressionWithTypeArguments>
}

external interface TypeAliasDeclaration : DeclarationStatement, JSDocContainer {
  override val name: Identifier
  val typeParameters: NodeArray<TypeParameterDeclaration>?
    get() = definedExternally
  val type: TypeNode
}

external interface EnumMember : NamedDeclaration, JSDocContainer {
  override val parent: EnumDeclaration
  override val name: dynamic /* Identifier | StringLiteral | NumericLiteral | ComputedPropertyName | PrivateIdentifier */
    get() = definedExternally
  val initializer: Expression?
    get() = definedExternally
}

external interface EnumDeclaration : DeclarationStatement, JSDocContainer {
  override val name: Identifier
  val members: NodeArray<EnumMember>
}

external interface ModuleDeclaration : DeclarationStatement, JSDocContainer {
  override val parent: dynamic /* ModuleBlock | NamespaceDeclaration | Identifier | JSDocNamespaceDeclaration | SourceFile */
    get() = definedExternally
  override val name: dynamic /* Identifier | StringLiteral */
    get() = definedExternally
  val body: dynamic /* ModuleBlock? | NamespaceDeclaration? | Identifier? | JSDocNamespaceDeclaration? */
    get() = definedExternally
}

external interface NamespaceDeclaration : ModuleDeclaration {
  override val name: Identifier
  override val body: dynamic /* ModuleBlock | NamespaceDeclaration */
    get() = definedExternally
}

external interface JSDocNamespaceDeclaration : ModuleDeclaration {
  override val name: Identifier
  override val body: dynamic /* Identifier? | JSDocNamespaceDeclaration? */
    get() = definedExternally
}

external interface ModuleBlock : Node, Statement {
  override val parent: ModuleDeclaration
  val statements: NodeArray<Statement>
}

external interface ImportEqualsDeclaration : DeclarationStatement, JSDocContainer {
  override val parent: dynamic /* SourceFile | ModuleBlock */
    get() = definedExternally
  override val name: Identifier
  val isTypeOnly: Boolean?
  val moduleReference: dynamic /* Identifier | QualifiedName | ExternalModuleReference */
    get() = definedExternally
}

external interface ExternalModuleReference : Node {
  override val parent: ImportEqualsDeclaration
  val expression: Expression
}

external interface ImportDeclaration : Statement {
  override val parent: dynamic /* SourceFile | ModuleBlock */
    get() = definedExternally
  val importClause: ImportClause?
    get() = definedExternally
  val moduleSpecifier: Expression
}

external interface ImportClause : NamedDeclaration {
  override val parent: ImportDeclaration
  val isTypeOnly: Boolean?
  override val name: Identifier?
    get() = definedExternally
  val namedBindings: dynamic /* NamespaceImport? | NamedImports? */
    get() = definedExternally
}

external interface NamespaceImport : NamedDeclaration {
  override val parent: ImportClause
  override val name: Identifier
}

external interface NamespaceExport : NamedDeclaration {
  override val parent: ExportDeclaration
  override val name: Identifier
}

external interface NamespaceExportDeclaration : DeclarationStatement, JSDocContainer {
  override val name: Identifier
}

external interface ExportDeclaration : DeclarationStatement, JSDocContainer {
  override val parent: dynamic /* SourceFile | ModuleBlock */
    get() = definedExternally
  val isTypeOnly: Boolean?
  val exportClause: dynamic /* NamespaceExport? | NamedExports? */
    get() = definedExternally
  val moduleSpecifier: Expression?
    get() = definedExternally
}

external interface NamedImports : Node {
  override val parent: ImportClause
  val elements: NodeArray<ImportSpecifier>
}

external interface NamedExports : Node {
  override val parent: ExportDeclaration
  val elements: NodeArray<ExportSpecifier>
}

external interface ImportSpecifier : NamedDeclaration {
  override val parent: NamedImports
  val propertyName: Identifier?
    get() = definedExternally
  override val name: Identifier
}

external interface ExportSpecifier : NamedDeclaration {
  override val parent: NamedExports
  val propertyName: Identifier?
    get() = definedExternally
  override val name: Identifier
}

external interface ExportAssignment : DeclarationStatement, JSDocContainer {
  override val parent: SourceFile
  val isExportEquals: Boolean?
    get() = definedExternally
  val expression: Expression
}

external interface FileReference : TextRange {
  var fileName: String
}

external interface CheckJsDirective : TextRange {
  var enabled: Boolean?
}

external interface CommentRange : TextRange {
  var hasTrailingNewLine: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var kind: dynamic /* SyntaxKind.SingleLineCommentTrivia | SyntaxKind.MultiLineCommentTrivia */
    get() = definedExternally
    set(value) = definedExternally
}

external interface SynthesizedComment : CommentRange {
  var text: String
  var hasLeadingNewline: Boolean?
    get() = definedExternally
    set(value) = definedExternally
}

external interface JSDocTypeExpression : TypeNode {
  val type: TypeNode
}

external interface JSDocNameReference : Node {
  val name: dynamic /* Identifier | QualifiedName */
    get() = definedExternally
}

external interface JSDocType : TypeNode {
  var _jsDocTypeBrand: Any
}

external interface JSDocAllType : JSDocType

external interface JSDocUnknownType : JSDocType

external interface JSDocNonNullableType : JSDocType {
  val type: TypeNode
}

external interface JSDocNullableType : JSDocType {
  val type: TypeNode
}

external interface JSDocOptionalType : JSDocType {
  val type: TypeNode
}

external interface JSDocFunctionType : JSDocType, SignatureDeclarationBase {
  override val kind: SyntaxKind
}

external interface JSDocVariadicType : JSDocType {
  val type: TypeNode
}

external interface JSDocNamepathType : JSDocType {
  val type: TypeNode
}

external interface JSDoc : Node {
  override val parent: dynamic /* ParameterDeclaration | CallSignatureDeclaration | ConstructSignatureDeclaration | MethodSignature | PropertySignature | ArrowFunction | ParenthesizedExpression | SpreadAssignment | ShorthandPropertyAssignment | PropertyAssignment | FunctionExpression | EmptyStatement | DebuggerStatement | Block | VariableStatement | ExpressionStatement | IfStatement | DoStatement | WhileStatement | ForStatement | ForInStatement | ForOfStatement | BreakStatement | ContinueStatement | ReturnStatement | WithStatement | SwitchStatement | LabeledStatement | ThrowStatement | TryStatement | FunctionDeclaration | ConstructorDeclaration | MethodDeclaration | VariableDeclaration | PropertyDeclaration | GetAccessorDeclaration | SetAccessorDeclaration | ClassDeclaration | ClassExpression | InterfaceDeclaration | TypeAliasDeclaration | EnumMember | EnumDeclaration | ModuleDeclaration | ImportEqualsDeclaration | ImportDeclaration | NamespaceExportDeclaration | ExportAssignment | IndexSignatureDeclaration | FunctionTypeNode | ConstructorTypeNode | JSDocFunctionType | ExportDeclaration | NamedTupleMember | Token<SyntaxKind.EndOfFileToken> & JSDocContainer */
    get() = definedExternally
  val tags: NodeArray<JSDocTag>?
    get() = definedExternally
  val comment: dynamic /* String? | NodeArray<dynamic /* JSDocText | JSDocLink */>? */
    get() = definedExternally
}

external interface JSDocTag : Node {
  override val parent: dynamic /* JSDoc | JSDocTypeLiteral */
    get() = definedExternally
  val tagName: Identifier
  val comment: dynamic /* String? | NodeArray<dynamic /* JSDocText | JSDocLink */>? */
    get() = definedExternally
}

external interface JSDocLink : Node {
  val name: dynamic /* Identifier? | QualifiedName? */
    get() = definedExternally
  var text: String
}

external interface JSDocText : Node {
  var text: String
}

external interface JSDocUnknownTag : JSDocTag

external interface `T$4` {
  val expression: dynamic /* Identifier | PropertyAccessEntityNameExpression */
    get() = definedExternally
}

external interface JSDocAugmentsTag : JSDocTag {
  val `class`: ExpressionWithTypeArguments /* ExpressionWithTypeArguments & `T$4` */
}

external interface JSDocImplementsTag : JSDocTag {
  val `class`: ExpressionWithTypeArguments /* ExpressionWithTypeArguments & `T$4` */
}

external interface JSDocAuthorTag : JSDocTag

external interface JSDocDeprecatedTag : JSDocTag

external interface JSDocClassTag : JSDocTag

external interface JSDocPublicTag : JSDocTag

external interface JSDocPrivateTag : JSDocTag

external interface JSDocProtectedTag : JSDocTag

external interface JSDocReadonlyTag : JSDocTag

external interface JSDocOverrideTag : JSDocTag

external interface JSDocEnumTag : JSDocTag, Declaration {
  override val parent: JSDoc
  val typeExpression: JSDocTypeExpression
}

external interface JSDocThisTag : JSDocTag {
  val typeExpression: JSDocTypeExpression
}

external interface JSDocTemplateTag : JSDocTag {
  val constraint: JSDocTypeExpression?
  val typeParameters: NodeArray<TypeParameterDeclaration>
}

external interface JSDocSeeTag : JSDocTag {
  val name: JSDocNameReference?
    get() = definedExternally
}

external interface JSDocReturnTag : JSDocTag {
  val typeExpression: JSDocTypeExpression?
    get() = definedExternally
}

external interface JSDocTypeTag : JSDocTag {
  val typeExpression: JSDocTypeExpression
}

external interface JSDocTypedefTag : JSDocTag, NamedDeclaration {
  override val parent: JSDoc
  val fullName: dynamic /* JSDocNamespaceDeclaration? | Identifier? */
    get() = definedExternally
  override val name: Identifier?
    get() = definedExternally
  val typeExpression: dynamic /* JSDocTypeExpression? | JSDocTypeLiteral? */
    get() = definedExternally
}

external interface JSDocCallbackTag : JSDocTag, NamedDeclaration {
  override val parent: JSDoc
  val fullName: dynamic /* JSDocNamespaceDeclaration? | Identifier? */
    get() = definedExternally
  override val name: Identifier?
    get() = definedExternally
  val typeExpression: JSDocSignature
}

external interface JSDocSignature : JSDocType, Declaration {
  val typeParameters: Array<JSDocTemplateTag>?
    get() = definedExternally
  val parameters: Array<JSDocParameterTag>
  val type: JSDocReturnTag?
}

external interface JSDocPropertyLikeTag : JSDocTag, Declaration {
  override val parent: JSDoc
  val name: dynamic /* Identifier | QualifiedName */
    get() = definedExternally
  val typeExpression: JSDocTypeExpression?
    get() = definedExternally
  val isNameFirst: Boolean?
  val isBracketed: Boolean?
}

external interface JSDocPropertyTag : JSDocPropertyLikeTag

external interface JSDocParameterTag : JSDocPropertyLikeTag

external interface JSDocTypeLiteral : JSDocType {
  val jsDocPropertyTags: Array<JSDocPropertyLikeTag>?
    get() = definedExternally
  val isArrayType: Boolean?
}

external enum class FlowFlags {
  Unreachable /* = 1 */,
  Start /* = 2 */,
  BranchLabel /* = 4 */,
  LoopLabel /* = 8 */,
  Assignment /* = 16 */,
  TrueCondition /* = 32 */,
  FalseCondition /* = 64 */,
  SwitchClause /* = 128 */,
  ArrayMutation /* = 256 */,
  Call /* = 512 */,
  ReduceLabel /* = 1024 */,
  Referenced /* = 2048 */,
  Shared /* = 4096 */,
  Label /* = 12 */,
  Condition /* = 96 */
}

external interface FlowNodeBase {
  var flags: FlowFlags
  var id: Number?
    get() = definedExternally
    set(value) = definedExternally
}

external interface FlowStart : FlowNodeBase {
  var node: dynamic /* FunctionExpression? | ArrowFunction? | MethodDeclaration? */
    get() = definedExternally
    set(value) = definedExternally
}

external interface FlowLabel : FlowNodeBase {
  var antecedents: Array<dynamic /* FlowStart | FlowLabel | FlowAssignment | FlowCall | FlowCondition | FlowSwitchClause | FlowArrayMutation | FlowReduceLabel */>?
}

external interface FlowAssignment : FlowNodeBase {
  var node: dynamic /* Expression | VariableDeclaration | BindingElement */
    get() = definedExternally
    set(value) = definedExternally
  var antecedent: dynamic /* FlowStart | FlowLabel | FlowAssignment | FlowCall | FlowCondition | FlowSwitchClause | FlowArrayMutation | FlowReduceLabel */
    get() = definedExternally
    set(value) = definedExternally
}

external interface FlowCall : FlowNodeBase {
  var node: CallExpression
  var antecedent: dynamic /* FlowStart | FlowLabel | FlowAssignment | FlowCall | FlowCondition | FlowSwitchClause | FlowArrayMutation | FlowReduceLabel */
    get() = definedExternally
    set(value) = definedExternally
}

external interface FlowCondition : FlowNodeBase {
  var node: Expression
  var antecedent: dynamic /* FlowStart | FlowLabel | FlowAssignment | FlowCall | FlowCondition | FlowSwitchClause | FlowArrayMutation | FlowReduceLabel */
    get() = definedExternally
    set(value) = definedExternally
}

external interface FlowSwitchClause : FlowNodeBase {
  var switchStatement: SwitchStatement
  var clauseStart: Number
  var clauseEnd: Number
  var antecedent: dynamic /* FlowStart | FlowLabel | FlowAssignment | FlowCall | FlowCondition | FlowSwitchClause | FlowArrayMutation | FlowReduceLabel */
    get() = definedExternally
    set(value) = definedExternally
}

external interface FlowArrayMutation : FlowNodeBase {
  var node: dynamic /* CallExpression | BinaryExpression */
    get() = definedExternally
    set(value) = definedExternally
  var antecedent: dynamic /* FlowStart | FlowLabel | FlowAssignment | FlowCall | FlowCondition | FlowSwitchClause | FlowArrayMutation | FlowReduceLabel */
    get() = definedExternally
    set(value) = definedExternally
}

external interface FlowReduceLabel : FlowNodeBase {
  var target: FlowLabel
  var antecedents: Array<dynamic /* FlowStart | FlowLabel | FlowAssignment | FlowCall | FlowCondition | FlowSwitchClause | FlowArrayMutation | FlowReduceLabel */>
  var antecedent: dynamic /* FlowStart | FlowLabel | FlowAssignment | FlowCall | FlowCondition | FlowSwitchClause | FlowArrayMutation | FlowReduceLabel */
    get() = definedExternally
    set(value) = definedExternally
}

external interface IncompleteType {
  var flags: TypeFlags
  var type: Type
}

external interface AmdDependency {
  var path: String
  var name: String?
    get() = definedExternally
    set(value) = definedExternally
}

external interface SourceFile : Declaration {
  fun getLineAndCharacterOfPosition(pos: Number): LineAndCharacter
  fun getLineEndOfPosition(pos: Number): Number
  fun getLineStarts(): Array<Number>
  fun getPositionOfLineAndCharacter(line: Number, character: Number): Number
  fun update(newText: String, textChangeRange: TextChangeRange): SourceFile
  val statements: NodeArray<Statement>
  val endOfFileToken: Token<SyntaxKind>
  var fileName: String
  var text: String
  var amdDependencies: Array<AmdDependency>
  var moduleName: String?
    get() = definedExternally
    set(value) = definedExternally
  var referencedFiles: Array<FileReference>
  var typeReferenceDirectives: Array<FileReference>
  var libReferenceDirectives: Array<FileReference>
  var languageVariant: LanguageVariant
  var isDeclarationFile: Boolean?
  var hasNoDefaultLib: Boolean?
  var languageVersion: ScriptTarget
}

external interface Bundle : Node {
  val prepends: Array<dynamic /* InputFiles | UnparsedSource */>
  val sourceFiles: Array<SourceFile>
}

external interface InputFiles : Node {
  var javascriptPath: String?
    get() = definedExternally
    set(value) = definedExternally
  var javascriptText: String
  var javascriptMapPath: String?
    get() = definedExternally
    set(value) = definedExternally
  var javascriptMapText: String?
    get() = definedExternally
    set(value) = definedExternally
  var declarationPath: String?
    get() = definedExternally
    set(value) = definedExternally
  var declarationText: String
  var declarationMapPath: String?
    get() = definedExternally
    set(value) = definedExternally
  var declarationMapText: String?
    get() = definedExternally
    set(value) = definedExternally
}

external interface UnparsedSource : Node {
  var fileName: String
  var text: String
  val prologues: Array<UnparsedPrologue>
  var helpers: Array<UnscopedEmitHelper>?
  var referencedFiles: Array<FileReference>
  var typeReferenceDirectives: Array<String>?
  var libReferenceDirectives: Array<FileReference>
  var hasNoDefaultLib: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var sourceMapPath: String?
    get() = definedExternally
    set(value) = definedExternally
  var sourceMapText: String?
    get() = definedExternally
    set(value) = definedExternally
  val syntheticReferences: Array<UnparsedSyntheticReference>?
    get() = definedExternally
  val texts: Array<dynamic /* UnparsedPrepend | UnparsedTextLike */>
}

external interface UnparsedSection : Node {
  override val kind: SyntaxKind
  override val parent: UnparsedSource
  val data: String?
    get() = definedExternally
}

external interface UnparsedPrologue : UnparsedSection {
  override val parent: UnparsedSource
}

external interface UnparsedPrepend : UnparsedSection {
  override val parent: UnparsedSource
  val texts: Array<UnparsedTextLike>
}

external interface UnparsedTextLike : UnparsedSection {
  override val kind: dynamic /* SyntaxKind.UnparsedText | SyntaxKind.UnparsedInternalText */
    get() = definedExternally
  override val parent: UnparsedSource
}

external interface UnparsedSyntheticReference : UnparsedSection {
  override val parent: UnparsedSource
}

external interface JsonSourceFile : SourceFile {
  override val statements: NodeArray<Statement>
}

external interface TsConfigSourceFile : JsonSourceFile {
  var extendedSourceFiles: Array<String>?
    get() = definedExternally
    set(value) = definedExternally
}

external interface JsonMinusNumericLiteral : PrefixUnaryExpression {
  override val kind: SyntaxKind
  override val operator: Any
  override val operand: NumericLiteral
}

external interface JsonObjectExpressionStatement : ExpressionStatement {
  override val expression: dynamic /* ObjectLiteralExpression | ArrayLiteralExpression | JsonMinusNumericLiteral | NumericLiteral | StringLiteral | TrueLiteral | FalseLiteral | NullLiteral */
    get() = definedExternally
}

external interface ScriptReferenceHost {
  fun getCompilerOptions(): CompilerOptions
  fun getSourceFile(fileName: String): SourceFile?
  fun getSourceFileByPath(path: String /* String & `T$3` */): SourceFile?
  fun getCurrentDirectory(): String
}

external interface ParseConfigHost {
  var useCaseSensitiveFileNames: Boolean?
  fun readDirectory(rootDir: String, extensions: Array<String>, excludes: Array<String>?, includes: Array<String>, depth: Number = definedExternally): Array<String>
  fun fileExists(path: String): Boolean
  fun readFile(path: String): String?
  val trace: ((s: String) -> Unit)?
}

external interface `T$5` {
  var _isResolvedConfigFileName: Any
}

external open class OperationCanceledException

external interface CancellationToken {
  fun isCancellationRequested(): Boolean
  fun throwIfCancellationRequested()
}

external interface `T$6` {
  var assignable: Number
  var identity: Number
  var subtype: Number
  var strictSubtype: Number
}

external interface Program : ScriptReferenceHost {
  override fun getCurrentDirectory(): String
  fun getRootFileNames(): Array<String>
  fun getSourceFiles(): Array<SourceFile>
  fun emit(targetSourceFile: SourceFile = definedExternally, writeFile: WriteFileCallback = definedExternally, cancellationToken: CancellationToken = definedExternally, emitOnlyDtsFiles: Boolean = definedExternally, customTransformers: CustomTransformers = definedExternally): EmitResult
  fun getOptionsDiagnostics(cancellationToken: CancellationToken = definedExternally): Array<Diagnostic>
  fun getGlobalDiagnostics(cancellationToken: CancellationToken = definedExternally): Array<Diagnostic>
  fun getSyntacticDiagnostics(sourceFile: SourceFile = definedExternally, cancellationToken: CancellationToken = definedExternally): Array<DiagnosticWithLocation>
  fun getSemanticDiagnostics(sourceFile: SourceFile = definedExternally, cancellationToken: CancellationToken = definedExternally): Array<Diagnostic>
  fun getDeclarationDiagnostics(sourceFile: SourceFile = definedExternally, cancellationToken: CancellationToken = definedExternally): Array<DiagnosticWithLocation>
  fun getConfigFileParsingDiagnostics(): Array<Diagnostic>
  fun getTypeChecker(): TypeChecker
  fun getNodeCount(): Number
  fun getIdentifierCount(): Number
  fun getSymbolCount(): Number
  fun getTypeCount(): Number
  fun getInstantiationCount(): Number
  fun getRelationCacheSizes(): `T$6`
  fun isSourceFileFromExternalLibrary(file: SourceFile): Boolean
  fun isSourceFileDefaultLibrary(file: SourceFile): Boolean
  fun getProjectReferences(): Array<ProjectReference>?
  fun getResolvedProjectReferences(): Array<ResolvedProjectReference?>?
}

external interface ResolvedProjectReference {
  var commandLine: ParsedCommandLine
  var sourceFile: SourceFile
  var references: Array<ResolvedProjectReference?>?
    get() = definedExternally
    set(value) = definedExternally
}

external interface CustomTransformer {
  fun transformSourceFile(node: SourceFile): SourceFile
  fun transformBundle(node: Bundle): Bundle
}

external interface CustomTransformers {
  var before: Array<dynamic /* TransformerFactory<SourceFile> | CustomTransformerFactory */>?
    get() = definedExternally
    set(value) = definedExternally
  var after: Array<dynamic /* TransformerFactory<SourceFile> | CustomTransformerFactory */>?
    get() = definedExternally
    set(value) = definedExternally
  var afterDeclarations: Array<dynamic /* TransformerFactory<dynamic /* Bundle | SourceFile */> | CustomTransformerFactory */>?
    get() = definedExternally
    set(value) = definedExternally
}

external interface SourceMapSpan {
  var emittedLine: Number
  var emittedColumn: Number
  var sourceLine: Number
  var sourceColumn: Number
  var nameIndex: Number?
    get() = definedExternally
    set(value) = definedExternally
  var sourceIndex: Number
}

external enum class ExitStatus {
  Success /* = 0 */,
  DiagnosticsPresent_OutputsSkipped /* = 1 */,
  DiagnosticsPresent_OutputsGenerated /* = 2 */,
  InvalidProject_OutputsSkipped /* = 3 */,
  ProjectReferenceCycle_OutputsSkipped /* = 4 */,
  ProjectReferenceCycle_OutputsSkupped /* = 4 */
}

external interface EmitResult {
  var emitSkipped: Boolean?
  var diagnostics: Array<Diagnostic>
  var emittedFiles: Array<String>?
    get() = definedExternally
    set(value) = definedExternally
}

external interface `T$7` {
  var typeArguments: NodeArray<TypeNode>?
    get() = definedExternally
    set(value) = definedExternally
}

external interface TypeChecker {
  fun getTypeOfSymbolAtLocation(symbol: Symbol, node: Node): Type
  fun getDeclaredTypeOfSymbol(symbol: Symbol): Type
  fun getPropertiesOfType(type: Type): Array<Symbol>
  fun getPropertyOfType(type: Type, propertyName: String): Symbol?
  fun getPrivateIdentifierPropertyOfType(leftType: Type, name: String, location: Node): Symbol?
  fun getIndexInfoOfType(type: Type, kind: IndexKind): IndexInfo?
  fun getSignaturesOfType(type: Type, kind: SignatureKind): Array<Signature>
  fun getIndexTypeOfType(type: Type, kind: IndexKind): Type?
  fun getBaseTypes(type: InterfaceType): Array<dynamic /* ObjectType | IntersectionType | TypeParameter | IndexedAccessType */>
  fun getBaseTypeOfLiteralType(type: Type): Type
  fun getWidenedType(type: Type): Type
  fun getReturnTypeOfSignature(signature: Signature): Type
  fun getNullableType(type: Type, flags: TypeFlags): Type
  fun getNonNullableType(type: Type): Type
  fun getTypeArguments(type: TypeReference): Array<Type>
  fun typeToTypeNode(type: Type, enclosingDeclaration: Node?, flags: NodeBuilderFlags?): TypeNode?
  fun signatureToSignatureDeclaration(signature: Signature, kind: SyntaxKind, enclosingDeclaration: Node?, flags: NodeBuilderFlags?): dynamic /* CallSignatureDeclaration | ConstructSignatureDeclaration | MethodSignature | IndexSignatureDeclaration | FunctionTypeNode | ConstructorTypeNode | JSDocFunctionType | FunctionDeclaration | MethodDeclaration | ConstructorDeclaration | GetAccessorDeclaration | SetAccessorDeclaration | FunctionExpression | ArrowFunction */
  fun indexInfoToIndexSignatureDeclaration(indexInfo: IndexInfo, kind: IndexKind, enclosingDeclaration: Node?, flags: NodeBuilderFlags?): IndexSignatureDeclaration?
  fun symbolToEntityName(symbol: Symbol, meaning: SymbolFlags, enclosingDeclaration: Node?, flags: NodeBuilderFlags?): dynamic /* Identifier? | QualifiedName? */
  fun symbolToExpression(symbol: Symbol, meaning: SymbolFlags, enclosingDeclaration: Node?, flags: NodeBuilderFlags?): Expression?
  fun symbolToTypeParameterDeclarations(symbol: Symbol, enclosingDeclaration: Node?, flags: NodeBuilderFlags?): NodeArray<TypeParameterDeclaration>?
  fun symbolToParameterDeclaration(symbol: Symbol, enclosingDeclaration: Node?, flags: NodeBuilderFlags?): ParameterDeclaration?
  fun typeParameterToDeclaration(parameter: TypeParameter, enclosingDeclaration: Node?, flags: NodeBuilderFlags?): TypeParameterDeclaration?
  fun getSymbolsInScope(location: Node, meaning: SymbolFlags): Array<Symbol>
  fun getSymbolAtLocation(node: Node): Symbol?
  fun getSymbolsOfParameterPropertyDeclaration(parameter: ParameterDeclaration, parameterName: String): Array<Symbol>
  fun getShorthandAssignmentValueSymbol(location: Node?): Symbol?
  fun getExportSpecifierLocalTargetSymbol(location: ExportSpecifier): Symbol?
  fun getExportSpecifierLocalTargetSymbol(location: Identifier): Symbol?
  fun getExportSymbolOfSymbol(symbol: Symbol): Symbol
  fun getPropertySymbolOfDestructuringAssignment(location: Identifier): Symbol?
  fun getTypeOfAssignmentPattern(pattern: ObjectLiteralExpression): Type
  fun getTypeOfAssignmentPattern(pattern: ArrayLiteralExpression): Type
  fun getTypeAtLocation(node: Node): Type
  fun getTypeFromTypeNode(node: TypeNode): Type
  fun signatureToString(signature: Signature, enclosingDeclaration: Node = definedExternally, flags: TypeFormatFlags = definedExternally, kind: SignatureKind = definedExternally): String
  fun typeToString(type: Type, enclosingDeclaration: Node = definedExternally, flags: TypeFormatFlags = definedExternally): String
  fun symbolToString(symbol: Symbol, enclosingDeclaration: Node = definedExternally, meaning: SymbolFlags = definedExternally, flags: SymbolFormatFlags = definedExternally): String
  fun typePredicateToString(predicate: ThisTypePredicate, enclosingDeclaration: Node = definedExternally, flags: TypeFormatFlags = definedExternally): String
  fun typePredicateToString(predicate: ThisTypePredicate): String
  fun typePredicateToString(predicate: ThisTypePredicate, enclosingDeclaration: Node = definedExternally): String
  fun typePredicateToString(predicate: IdentifierTypePredicate, enclosingDeclaration: Node = definedExternally, flags: TypeFormatFlags = definedExternally): String
  fun typePredicateToString(predicate: IdentifierTypePredicate): String
  fun typePredicateToString(predicate: IdentifierTypePredicate, enclosingDeclaration: Node = definedExternally): String
  fun typePredicateToString(predicate: AssertsThisTypePredicate, enclosingDeclaration: Node = definedExternally, flags: TypeFormatFlags = definedExternally): String
  fun typePredicateToString(predicate: AssertsThisTypePredicate): String
  fun typePredicateToString(predicate: AssertsThisTypePredicate, enclosingDeclaration: Node = definedExternally): String
  fun typePredicateToString(predicate: AssertsIdentifierTypePredicate, enclosingDeclaration: Node = definedExternally, flags: TypeFormatFlags = definedExternally): String
  fun typePredicateToString(predicate: AssertsIdentifierTypePredicate): String
  fun typePredicateToString(predicate: AssertsIdentifierTypePredicate, enclosingDeclaration: Node = definedExternally): String
  fun getFullyQualifiedName(symbol: Symbol): String
  fun getAugmentedPropertiesOfType(type: Type): Array<Symbol>
  fun getRootSymbols(symbol: Symbol): Array<Symbol>
  fun getSymbolOfExpando(node: Node, allowDeclaration: Boolean): Symbol?
  fun getContextualType(node: Expression): Type?
  fun getResolvedSignature(node: CallExpression, candidatesOutArray: Array<Signature> = definedExternally, argumentCount: Number = definedExternally): Signature?
  fun getResolvedSignature(node: CallExpression): Signature?
  fun getResolvedSignature(node: CallExpression, candidatesOutArray: Array<Signature> = definedExternally): Signature?
  fun getResolvedSignature(node: NewExpression, candidatesOutArray: Array<Signature> = definedExternally, argumentCount: Number = definedExternally): Signature?
  fun getResolvedSignature(node: NewExpression): Signature?
  fun getResolvedSignature(node: NewExpression, candidatesOutArray: Array<Signature> = definedExternally): Signature?
  fun getResolvedSignature(node: TaggedTemplateExpression, candidatesOutArray: Array<Signature> = definedExternally, argumentCount: Number = definedExternally): Signature?
  fun getResolvedSignature(node: TaggedTemplateExpression): Signature?
  fun getResolvedSignature(node: TaggedTemplateExpression, candidatesOutArray: Array<Signature> = definedExternally): Signature?
  fun getResolvedSignature(node: Decorator, candidatesOutArray: Array<Signature> = definedExternally, argumentCount: Number = definedExternally): Signature?
  fun getResolvedSignature(node: Decorator): Signature?
  fun getResolvedSignature(node: Decorator, candidatesOutArray: Array<Signature> = definedExternally): Signature?
  fun getResolvedSignature(node: JsxSelfClosingElement, candidatesOutArray: Array<Signature> = definedExternally, argumentCount: Number = definedExternally): Signature?
  fun getResolvedSignature(node: JsxSelfClosingElement): Signature?
  fun getResolvedSignature(node: JsxSelfClosingElement, candidatesOutArray: Array<Signature> = definedExternally): Signature?
  fun getResolvedSignature(node: JsxOpeningElement, candidatesOutArray: Array<Signature> = definedExternally, argumentCount: Number = definedExternally): Signature?
  fun getResolvedSignature(node: JsxOpeningElement): Signature?
  fun getResolvedSignature(node: JsxOpeningElement, candidatesOutArray: Array<Signature> = definedExternally): Signature?
  fun getSignatureFromDeclaration(declaration: CallSignatureDeclaration): Signature?
  fun getSignatureFromDeclaration(declaration: ConstructSignatureDeclaration): Signature?
  fun getSignatureFromDeclaration(declaration: MethodSignature): Signature?
  fun getSignatureFromDeclaration(declaration: IndexSignatureDeclaration): Signature?
  fun getSignatureFromDeclaration(declaration: FunctionTypeNode): Signature?
  fun getSignatureFromDeclaration(declaration: ConstructorTypeNode): Signature?
  fun getSignatureFromDeclaration(declaration: JSDocFunctionType): Signature?
  fun getSignatureFromDeclaration(declaration: FunctionDeclaration): Signature?
  fun getSignatureFromDeclaration(declaration: MethodDeclaration): Signature?
  fun getSignatureFromDeclaration(declaration: ConstructorDeclaration): Signature?
  fun getSignatureFromDeclaration(declaration: GetAccessorDeclaration): Signature?
  fun getSignatureFromDeclaration(declaration: SetAccessorDeclaration): Signature?
  fun getSignatureFromDeclaration(declaration: FunctionExpression): Signature?
  fun getSignatureFromDeclaration(declaration: ArrowFunction): Signature?
  fun isImplementationOfOverload(node: CallSignatureDeclaration): Boolean?
  fun isImplementationOfOverload(node: ConstructSignatureDeclaration): Boolean?
  fun isImplementationOfOverload(node: MethodSignature): Boolean?
  fun isImplementationOfOverload(node: IndexSignatureDeclaration): Boolean?
  fun isImplementationOfOverload(node: FunctionTypeNode): Boolean?
  fun isImplementationOfOverload(node: ConstructorTypeNode): Boolean?
  fun isImplementationOfOverload(node: JSDocFunctionType): Boolean?
  fun isImplementationOfOverload(node: FunctionDeclaration): Boolean?
  fun isImplementationOfOverload(node: MethodDeclaration): Boolean?
  fun isImplementationOfOverload(node: ConstructorDeclaration): Boolean?
  fun isImplementationOfOverload(node: GetAccessorDeclaration): Boolean?
  fun isImplementationOfOverload(node: SetAccessorDeclaration): Boolean?
  fun isImplementationOfOverload(node: FunctionExpression): Boolean?
  fun isImplementationOfOverload(node: ArrowFunction): Boolean?
  fun isUndefinedSymbol(symbol: Symbol): Boolean
  fun isArgumentsSymbol(symbol: Symbol): Boolean
  fun isUnknownSymbol(symbol: Symbol): Boolean
  fun getConstantValue(node: EnumMember): dynamic /* String? | Number? */
  fun getConstantValue(node: PropertyAccessExpression): dynamic /* String? | Number? */
  fun getConstantValue(node: ElementAccessExpression): dynamic /* String? | Number? */
  fun isValidPropertyAccess(node: PropertyAccessExpression, propertyName: String): Boolean
  fun isValidPropertyAccess(node: QualifiedName, propertyName: String): Boolean
  fun isValidPropertyAccess(node: ImportTypeNode, propertyName: String): Boolean
  fun getAliasedSymbol(symbol: Symbol): Symbol
  fun getExportsOfModule(moduleSymbol: Symbol): Array<Symbol>
  fun getJsxIntrinsicTagNamesAt(location: Node): Array<Symbol>
  fun isOptionalParameter(node: ParameterDeclaration): Boolean
  fun getAmbientModules(): Array<Symbol>
  fun tryGetMemberInModuleExports(memberName: String, moduleSymbol: Symbol): Symbol?
  fun getApparentType(type: Type): Type
  fun getBaseConstraintOfType(type: Type): Type?
  fun getDefaultFromTypeParameter(type: Type): Type?
  fun <T> runWithCancellationToken(token: CancellationToken, cb: (checker: TypeChecker) -> T): T
}

external enum class NodeBuilderFlags {
  None /* = 0 */,
  NoTruncation /* = 1 */,
  WriteArrayAsGenericType /* = 2 */,
  GenerateNamesForShadowedTypeParams /* = 4 */,
  UseStructuralFallback /* = 8 */,
  ForbidIndexedAccessSymbolReferences /* = 16 */,
  WriteTypeArgumentsOfSignature /* = 32 */,
  UseFullyQualifiedType /* = 64 */,
  UseOnlyExternalAliasing /* = 128 */,
  SuppressAnyReturnType /* = 256 */,
  WriteTypeParametersInQualifiedName /* = 512 */,
  MultilineObjectLiterals /* = 1024 */,
  WriteClassExpressionAsTypeLiteral /* = 2048 */,
  UseTypeOfFunction /* = 4096 */,
  OmitParameterModifiers /* = 8192 */,
  UseAliasDefinedOutsideCurrentScope /* = 16384 */,
  UseSingleQuotesForStringLiteralType /* = 268435456 */,
  NoTypeReduction /* = 536870912 */,
  NoUndefinedOptionalParameterType /* = 1073741824 */,
  AllowThisInObjectLiteral /* = 32768 */,
  AllowQualifiedNameInPlaceOfIdentifier /* = 65536 */,
  AllowQualifedNameInPlaceOfIdentifier /* = 65536 */,
  AllowAnonymousIdentifier /* = 131072 */,
  AllowEmptyUnionOrIntersection /* = 262144 */,
  AllowEmptyTuple /* = 524288 */,
  AllowUniqueESSymbolType /* = 1048576 */,
  AllowEmptyIndexInfoType /* = 2097152 */,
  AllowNodeModulesRelativePaths /* = 67108864 */,
  IgnoreErrors /* = 70221824 */,
  InObjectTypeLiteral /* = 4194304 */,
  InTypeAlias /* = 8388608 */,
  InInitialEntityName /* = 16777216 */
}

external enum class TypeFormatFlags {
  None /* = 0 */,
  NoTruncation /* = 1 */,
  WriteArrayAsGenericType /* = 2 */,
  UseStructuralFallback /* = 8 */,
  WriteTypeArgumentsOfSignature /* = 32 */,
  UseFullyQualifiedType /* = 64 */,
  SuppressAnyReturnType /* = 256 */,
  MultilineObjectLiterals /* = 1024 */,
  WriteClassExpressionAsTypeLiteral /* = 2048 */,
  UseTypeOfFunction /* = 4096 */,
  OmitParameterModifiers /* = 8192 */,
  UseAliasDefinedOutsideCurrentScope /* = 16384 */,
  UseSingleQuotesForStringLiteralType /* = 268435456 */,
  NoTypeReduction /* = 536870912 */,
  AllowUniqueESSymbolType /* = 1048576 */,
  AddUndefined /* = 131072 */,
  WriteArrowStyleSignature /* = 262144 */,
  InArrayType /* = 524288 */,
  InElementType /* = 2097152 */,
  InFirstTypeArgument /* = 4194304 */,
  InTypeAlias /* = 8388608 */,
  WriteOwnNameForAnyLike /* = 0 */,
  NodeBuilderFlagsMask /* = 814775659 */
}

external enum class SymbolFormatFlags {
  None /* = 0 */,
  WriteTypeParametersOrArguments /* = 1 */,
  UseOnlyExternalAliasing /* = 2 */,
  AllowAnyNodeKind /* = 4 */,
  UseAliasDefinedOutsideCurrentScope /* = 8 */
}

external enum class TypePredicateKind {
  This /* = 0 */,
  Identifier /* = 1 */,
  AssertsThis /* = 2 */,
  AssertsIdentifier /* = 3 */
}

external interface TypePredicateBase {
  var kind: TypePredicateKind
  var type: Type?
}

external interface ThisTypePredicate : TypePredicateBase {
  var parameterName: Any?
  var parameterIndex: Any?
}

external interface IdentifierTypePredicate : TypePredicateBase {
  var parameterName: String
  var parameterIndex: Number
}

external interface AssertsThisTypePredicate : TypePredicateBase {
  var parameterName: Any?
  var parameterIndex: Any?
  override var type: Type?
}

external interface AssertsIdentifierTypePredicate : TypePredicateBase {
  var parameterName: String
  var parameterIndex: Number
  override var type: Type?
}

external enum class SymbolFlags {
  None /* = 0 */,
  FunctionScopedVariable /* = 1 */,
  BlockScopedVariable /* = 2 */,
  Property /* = 4 */,
  EnumMember /* = 8 */,
  Function /* = 16 */,
  Class /* = 32 */,
  Interface /* = 64 */,
  ConstEnum /* = 128 */,
  RegularEnum /* = 256 */,
  ValueModule /* = 512 */,
  NamespaceModule /* = 1024 */,
  TypeLiteral /* = 2048 */,
  ObjectLiteral /* = 4096 */,
  Method /* = 8192 */,
  Constructor /* = 16384 */,
  GetAccessor /* = 32768 */,
  SetAccessor /* = 65536 */,
  Signature /* = 131072 */,
  TypeParameter /* = 262144 */,
  TypeAlias /* = 524288 */,
  ExportValue /* = 1048576 */,
  Alias /* = 2097152 */,
  Prototype /* = 4194304 */,
  ExportStar /* = 8388608 */,
  Optional /* = 16777216 */,
  Transient /* = 33554432 */,
  Assignment /* = 67108864 */,
  ModuleExports /* = 134217728 */,
  Enum /* = 384 */,
  Variable /* = 3 */,
  Value /* = 111551 */,
  Type /* = 788968 */,
  Namespace /* = 1920 */,
  Module /* = 1536 */,
  Accessor /* = 98304 */,
  FunctionScopedVariableExcludes /* = 111550 */,
  BlockScopedVariableExcludes /* = 111551 */,
  ParameterExcludes /* = 111551 */,
  PropertyExcludes /* = 0 */,
  EnumMemberExcludes /* = 900095 */,
  FunctionExcludes /* = 110991 */,
  ClassExcludes /* = 899503 */,
  InterfaceExcludes /* = 788872 */,
  RegularEnumExcludes /* = 899327 */,
  ConstEnumExcludes /* = 899967 */,
  ValueModuleExcludes /* = 110735 */,
  NamespaceModuleExcludes /* = 0 */,
  MethodExcludes /* = 103359 */,
  GetAccessorExcludes /* = 46015 */,
  SetAccessorExcludes /* = 78783 */,
  TypeParameterExcludes /* = 526824 */,
  TypeAliasExcludes /* = 788968 */,
  AliasExcludes /* = 2097152 */,
  ModuleMember /* = 2623475 */,
  ExportHasLocal /* = 944 */,
  BlockScoped /* = 418 */,
  PropertyOrAccessor /* = 98308 */,
  ClassMember /* = 106500 */
}

external interface Symbol {
  val name: String
  fun getFlags(): SymbolFlags
  fun getEscapedName(): dynamic /* String & `T$8` | Unit & `T$8` | InternalSymbolName */
  fun getName(): String
  fun getDeclarations(): Array<Declaration>?
  fun getDocumentationComment(typeChecker: TypeChecker?): Array<SymbolDisplayPart>
  fun getJsDocTags(checker: TypeChecker = definedExternally): Array<JSDocTagInfo>
  var flags: SymbolFlags
  var escapedName: dynamic /* String & `T$8` | Unit & `T$8` | InternalSymbolName */
    get() = definedExternally
    set(value) = definedExternally
  var declarations: Array<Declaration>?
    get() = definedExternally
    set(value) = definedExternally
  var valueDeclaration: Declaration?
    get() = definedExternally
    set(value) = definedExternally
  var members: SymbolTable?
    get() = definedExternally
    set(value) = definedExternally
  var exports: SymbolTable?
    get() = definedExternally
    set(value) = definedExternally
  var globalExports: SymbolTable?
    get() = definedExternally
    set(value) = definedExternally
}

external enum class InternalSymbolName {
  Call /* = "__call" */,
  Constructor /* = "__constructor" */,
  New /* = "__new" */,
  Index /* = "__index" */,
  ExportStar /* = "__export" */,
  Global /* = "__global" */,
  Missing /* = "__missing" */,
  Type /* = "__type" */,
  Object /* = "__object" */,
  JSXAttributes /* = "__jsxAttributes" */,
  Class /* = "__class" */,
  Function /* = "__function" */,
  Computed /* = "__computed" */,
  Resolving /* = "__resolving__" */,
  ExportEquals /* = "export=" */,
  Default /* = "default" */,
  This /* = "this" */
}

external interface `T$8` {
  var __escapedIdentifier: Unit
}

external interface ReadonlyUnderscoreEscapedMap<T> : ReadonlyESMap<dynamic /* String & `T$8` | Unit & `T$8` | InternalSymbolName */, T>

external interface UnderscoreEscapedMap<T> : ESMap<dynamic /* String & `T$8` | Unit & `T$8` | InternalSymbolName */, T>, ReadonlyUnderscoreEscapedMap<T>

external enum class TypeFlags {
  Any /* = 1 */,
  Unknown /* = 2 */,
  String /* = 4 */,
  Number /* = 8 */,
  Boolean /* = 16 */,
  Enum /* = 32 */,
  BigInt /* = 64 */,
  StringLiteral /* = 128 */,
  NumberLiteral /* = 256 */,
  BooleanLiteral /* = 512 */,
  EnumLiteral /* = 1024 */,
  BigIntLiteral /* = 2048 */,
  ESSymbol /* = 4096 */,
  UniqueESSymbol /* = 8192 */,
  Void /* = 16384 */,
  Undefined /* = 32768 */,
  Null /* = 65536 */,
  Never /* = 131072 */,
  TypeParameter /* = 262144 */,
  Object /* = 524288 */,
  Union /* = 1048576 */,
  Intersection /* = 2097152 */,
  Index /* = 4194304 */,
  IndexedAccess /* = 8388608 */,
  Conditional /* = 16777216 */,
  Substitution /* = 33554432 */,
  NonPrimitive /* = 67108864 */,
  TemplateLiteral /* = 134217728 */,
  StringMapping /* = 268435456 */,
  Literal /* = 2944 */,
  Unit /* = 109440 */,
  StringOrNumberLiteral /* = 384 */,
  PossiblyFalsy /* = 117724 */,
  StringLike /* = 402653316 */,
  NumberLike /* = 296 */,
  BigIntLike /* = 2112 */,
  BooleanLike /* = 528 */,
  EnumLike /* = 1056 */,
  ESSymbolLike /* = 12288 */,
  VoidLike /* = 49152 */,
  UnionOrIntersection /* = 3145728 */,
  StructuredType /* = 3670016 */,
  TypeVariable /* = 8650752 */,
  InstantiableNonPrimitive /* = 58982400 */,
  InstantiablePrimitive /* = 406847488 */,
  Instantiable /* = 465829888 */,
  StructuredOrInstantiable /* = 469499904 */,
  Narrowable /* = 536624127 */
}

external interface Type {
  fun getFlags(): TypeFlags
  fun getSymbol(): Symbol?
  fun getProperties(): Array<Symbol>
  fun getProperty(propertyName: String): Symbol?
  fun getApparentProperties(): Array<Symbol>
  fun getCallSignatures(): Array<Signature>
  fun getConstructSignatures(): Array<Signature>
  fun getStringIndexType(): Type?
  fun getNumberIndexType(): Type?
  fun getBaseTypes(): Array<dynamic /* ObjectType | IntersectionType | TypeParameter | IndexedAccessType */>?
  fun getNonNullableType(): Type
  fun getConstraint(): Type?
  fun getDefault(): Type?
  fun isUnion(): Boolean
  fun isIntersection(): Boolean
  fun isUnionOrIntersection(): Boolean
  fun isLiteral(): Boolean
  fun isStringLiteral(): Boolean
  fun isNumberLiteral(): Boolean
  fun isTypeParameter(): Boolean
  fun isClassOrInterface(): Boolean
  fun isClass(): Boolean
  var flags: TypeFlags
  var symbol: Symbol
  var pattern: dynamic /* ObjectBindingPattern? | ArrayBindingPattern? | ObjectLiteralExpression? | ArrayLiteralExpression? */
    get() = definedExternally
    set(value) = definedExternally
  var aliasSymbol: Symbol?
    get() = definedExternally
    set(value) = definedExternally
  var aliasTypeArguments: Array<Type>?
    get() = definedExternally
    set(value) = definedExternally
}

external interface LiteralType : Type {
  var value: dynamic /* String | Number | PseudoBigInt */
    get() = definedExternally
    set(value) = definedExternally
  var freshType: LiteralType
  var regularType: LiteralType
}

external interface UniqueESSymbolType : Type {
  override var symbol: Symbol
  var escapedName: dynamic /* String & `T$8` | Unit & `T$8` | InternalSymbolName */
    get() = definedExternally
    set(value) = definedExternally
}

external interface StringLiteralType : LiteralType {
  override var value: String
}

external interface NumberLiteralType : LiteralType {
  override var value: Number
}

external interface BigIntLiteralType : LiteralType {
  override var value: PseudoBigInt
}

external interface EnumType : Type

external enum class ObjectFlags {
  Class /* = 1 */,
  Interface /* = 2 */,
  Reference /* = 4 */,
  Tuple /* = 8 */,
  Anonymous /* = 16 */,
  Mapped /* = 32 */,
  Instantiated /* = 64 */,
  ObjectLiteral /* = 128 */,
  EvolvingArray /* = 256 */,
  ObjectLiteralPatternWithComputedProperties /* = 512 */,
  ReverseMapped /* = 1024 */,
  JsxAttributes /* = 2048 */,
  MarkerType /* = 4096 */,
  JSLiteral /* = 8192 */,
  FreshLiteral /* = 16384 */,
  ArrayLiteral /* = 32768 */,
  ClassOrInterface /* = 3 */,
  ContainsSpread /* = 4194304 */,
  ObjectRestType /* = 8388608 */
}

external interface ObjectType : Type {
  var objectFlags: ObjectFlags
}

external interface InterfaceType : ObjectType {
  var typeParameters: Array<TypeParameter>?
  var outerTypeParameters: Array<TypeParameter>?
  var localTypeParameters: Array<TypeParameter>?
  var thisType: TypeParameter?
}

external interface InterfaceTypeWithDeclaredMembers : InterfaceType {
  var declaredProperties: Array<Symbol>
  var declaredCallSignatures: Array<Signature>
  var declaredConstructSignatures: Array<Signature>
  var declaredStringIndexInfo: IndexInfo?
    get() = definedExternally
    set(value) = definedExternally
  var declaredNumberIndexInfo: IndexInfo?
    get() = definedExternally
    set(value) = definedExternally
}

external interface TypeReference : ObjectType {
  var typeArguments: Array<Type>?
    get() = definedExternally
    set(value) = definedExternally
  var target: GenericType
  var node: dynamic /* TypeReferenceNode? | ArrayTypeNode? | TupleTypeNode? */
    get() = definedExternally
    set(value) = definedExternally
}

external interface DeferredTypeReference : TypeReference

external interface GenericType : InterfaceType, TypeReference

external enum class ElementFlags {
  Required /* = 1 */,
  Optional /* = 2 */,
  Rest /* = 4 */,
  Variadic /* = 8 */,
  Fixed /* = 3 */,
  Variable /* = 12 */,
  NonRequired /* = 14 */,
  NonRest /* = 11 */
}

external interface TupleType : GenericType {
  var elementFlags: Array<ElementFlags>
  var minLength: Number
  var fixedLength: Number
  var hasRestElement: Boolean?
  var combinedFlags: ElementFlags
  var readonly: Boolean?
  var labeledElementDeclarations: Array<dynamic /* NamedTupleMember | ParameterDeclaration */>?
    get() = definedExternally
    set(value) = definedExternally
}

external interface TupleTypeReference : TypeReference {
  override var target: GenericType
}

external interface UnionOrIntersectionType : Type {
  var types: Array<Type>
}

external interface UnionType : UnionOrIntersectionType

external interface IntersectionType : UnionOrIntersectionType

external interface EvolvingArrayType : ObjectType {
  var elementType: Type
  var finalArrayType: Type?
    get() = definedExternally
    set(value) = definedExternally
}

external interface InstantiableType : Type

external interface TypeParameter : InstantiableType

external interface IndexedAccessType : InstantiableType {
  var objectType: Type
  var indexType: Type
  var constraint: Type?
    get() = definedExternally
    set(value) = definedExternally
  var simplifiedForReading: Type?
    get() = definedExternally
    set(value) = definedExternally
  var simplifiedForWriting: Type?
    get() = definedExternally
    set(value) = definedExternally
}

external interface IndexType : InstantiableType {
  var type: dynamic /* InstantiableType | UnionOrIntersectionType */
    get() = definedExternally
    set(value) = definedExternally
}

external interface ConditionalRoot {
  var node: ConditionalTypeNode
  var checkType: Type
  var extendsType: Type
  var isDistributive: Boolean?
  var inferTypeParameters: Array<TypeParameter>?
    get() = definedExternally
    set(value) = definedExternally
  var outerTypeParameters: Array<TypeParameter>?
    get() = definedExternally
    set(value) = definedExternally
  var instantiations: Map<Type>?
    get() = definedExternally
    set(value) = definedExternally
  var aliasSymbol: Symbol?
    get() = definedExternally
    set(value) = definedExternally
  var aliasTypeArguments: Array<Type>?
    get() = definedExternally
    set(value) = definedExternally
}

external interface ConditionalType : InstantiableType {
  var root: ConditionalRoot
  var checkType: Type
  var extendsType: Type
  var resolvedTrueType: Type
  var resolvedFalseType: Type
}

external interface TemplateLiteralType : InstantiableType {
  var texts: Array<String>
  var types: Array<Type>
}

external interface StringMappingType : InstantiableType {
  override var symbol: Symbol
  var type: Type
}

external interface SubstitutionType : InstantiableType {
  var objectFlags: ObjectFlags
  var baseType: Type
  var substitute: Type
}

external enum class SignatureKind {
  Call /* = 0 */,
  Construct /* = 1 */
}

external interface Signature {
  fun getDeclaration(): dynamic /* CallSignatureDeclaration | ConstructSignatureDeclaration | MethodSignature | IndexSignatureDeclaration | FunctionTypeNode | ConstructorTypeNode | JSDocFunctionType | FunctionDeclaration | MethodDeclaration | ConstructorDeclaration | GetAccessorDeclaration | SetAccessorDeclaration | FunctionExpression | ArrowFunction */
  fun getTypeParameters(): Array<TypeParameter>?
  fun getParameters(): Array<Symbol>
  fun getReturnType(): Type
  fun getDocumentationComment(typeChecker: TypeChecker?): Array<SymbolDisplayPart>
  fun getJsDocTags(): Array<JSDocTagInfo>
  var declaration: dynamic /* CallSignatureDeclaration? | ConstructSignatureDeclaration? | MethodSignature? | IndexSignatureDeclaration? | FunctionTypeNode? | ConstructorTypeNode? | JSDocFunctionType? | FunctionDeclaration? | MethodDeclaration? | ConstructorDeclaration? | GetAccessorDeclaration? | SetAccessorDeclaration? | FunctionExpression? | ArrowFunction? | JSDocSignature? */
    get() = definedExternally
    set(value) = definedExternally
  var typeParameters: Array<TypeParameter>?
    get() = definedExternally
    set(value) = definedExternally
  var parameters: Array<Symbol>
}

external enum class IndexKind {
  String /* = 0 */,
  Number /* = 1 */
}

external interface IndexInfo {
  var type: Type
  var isReadonly: Boolean?
  var declaration: IndexSignatureDeclaration?
    get() = definedExternally
    set(value) = definedExternally
}

external enum class InferencePriority {
  NakedTypeVariable /* = 1 */,
  SpeculativeTuple /* = 2 */,
  SubstituteSource /* = 4 */,
  HomomorphicMappedType /* = 8 */,
  PartialHomomorphicMappedType /* = 16 */,
  MappedTypeConstraint /* = 32 */,
  ContravariantConditional /* = 64 */,
  ReturnType /* = 128 */,
  LiteralKeyof /* = 256 */,
  NoConstraints /* = 512 */,
  AlwaysStrict /* = 1024 */,
  MaxValue /* = 2048 */,
  PriorityImpliesCombination /* = 416 */,
  Circularity /* = -1 */
}

external interface FileExtensionInfo {
  var extension: String
  var isMixedContent: Boolean?
  var scriptKind: ScriptKind?
    get() = definedExternally
    set(value) = definedExternally
}

external interface DiagnosticMessage {
  var key: String
  var category: DiagnosticCategory
  var code: Number
  var message: String
  var reportsUnnecessary: Any?
    get() = definedExternally
    set(value) = definedExternally
  var reportsDeprecated: Any?
    get() = definedExternally
    set(value) = definedExternally
}

external interface DiagnosticMessageChain {
  var messageText: String
  var category: DiagnosticCategory
  var code: Number
  var next: Array<DiagnosticMessageChain>?
    get() = definedExternally
    set(value) = definedExternally
}

external interface Diagnostic : DiagnosticRelatedInformation {
  var reportsUnnecessary: Any?
    get() = definedExternally
    set(value) = definedExternally
  var reportsDeprecated: Any?
    get() = definedExternally
    set(value) = definedExternally
  var source: String?
    get() = definedExternally
    set(value) = definedExternally
  var relatedInformation: Array<DiagnosticRelatedInformation>?
    get() = definedExternally
    set(value) = definedExternally
}

external interface DiagnosticRelatedInformation {
  var category: DiagnosticCategory
  var code: Number
  var file: SourceFile?
  var start: Number?
  var length: Number?
  var messageText: dynamic /* String | DiagnosticMessageChain */
    get() = definedExternally
    set(value) = definedExternally
}

external interface DiagnosticWithLocation : Diagnostic

external enum class DiagnosticCategory {
  Warning /* = 0 */,
  Error /* = 1 */,
  Suggestion /* = 2 */,
  Message /* = 3 */
}

external enum class ModuleResolutionKind {
  Classic /* = 1 */,
  NodeJs /* = 2 */
}

external interface PluginImport {
  var name: String
}

external interface ProjectReference {
  var path: String
  var originalPath: String?
    get() = definedExternally
    set(value) = definedExternally
  var prepend: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var circular: Boolean?
    get() = definedExternally
    set(value) = definedExternally
}

external enum class WatchFileKind {
  FixedPollingInterval /* = 0 */,
  PriorityPollingInterval /* = 1 */,
  DynamicPriorityPolling /* = 2 */,
  FixedChunkSizePolling /* = 3 */,
  UseFsEvents /* = 4 */,
  UseFsEventsOnParentDirectory /* = 5 */
}

external enum class WatchDirectoryKind {
  UseFsEvents /* = 0 */,
  FixedPollingInterval /* = 1 */,
  DynamicPriorityPolling /* = 2 */,
  FixedChunkSizePolling /* = 3 */
}

external enum class PollingWatchKind {
  FixedInterval /* = 0 */,
  PriorityInterval /* = 1 */,
  DynamicPriority /* = 2 */,
  FixedChunkSize /* = 3 */
}

external interface CompilerOptions {
  var allowJs: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var allowSyntheticDefaultImports: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var allowUmdGlobalAccess: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var allowUnreachableCode: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var allowUnusedLabels: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var alwaysStrict: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var baseUrl: String?
    get() = definedExternally
    set(value) = definedExternally
  var charset: String?
    get() = definedExternally
    set(value) = definedExternally
  var checkJs: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var declaration: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var declarationMap: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var emitDeclarationOnly: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var declarationDir: String?
    get() = definedExternally
    set(value) = definedExternally
  var disableSizeLimit: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var disableSourceOfProjectReferenceRedirect: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var disableSolutionSearching: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var disableReferencedProjectLoad: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var downlevelIteration: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var emitBOM: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var emitDecoratorMetadata: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var experimentalDecorators: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var forceConsistentCasingInFileNames: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var importHelpers: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var importsNotUsedAsValues: ImportsNotUsedAsValues?
    get() = definedExternally
    set(value) = definedExternally
  var inlineSourceMap: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var inlineSources: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var isolatedModules: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var jsx: JsxEmit?
    get() = definedExternally
    set(value) = definedExternally
  var keyofStringsOnly: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var lib: Array<String>?
    get() = definedExternally
    set(value) = definedExternally
  var locale: String?
    get() = definedExternally
    set(value) = definedExternally
  var mapRoot: String?
    get() = definedExternally
    set(value) = definedExternally
  var maxNodeModuleJsDepth: Number?
    get() = definedExternally
    set(value) = definedExternally
  var module: ModuleKind?
    get() = definedExternally
    set(value) = definedExternally
  var moduleResolution: ModuleResolutionKind?
    get() = definedExternally
    set(value) = definedExternally
  var newLine: NewLineKind?
    get() = definedExternally
    set(value) = definedExternally
  var noEmit: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var noEmitHelpers: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var noEmitOnError: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var noErrorTruncation: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var noFallthroughCasesInSwitch: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var noImplicitAny: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var noImplicitReturns: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var noImplicitThis: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var noStrictGenericChecks: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var noUnusedLocals: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var noUnusedParameters: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var noImplicitUseStrict: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var noPropertyAccessFromIndexSignature: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var assumeChangesOnlyAffectDirectDependencies: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var noLib: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var noResolve: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var noUncheckedIndexedAccess: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var out: String?
    get() = definedExternally
    set(value) = definedExternally
  var outDir: String?
    get() = definedExternally
    set(value) = definedExternally
  var outFile: String?
    get() = definedExternally
    set(value) = definedExternally
  var paths: MapLike<Array<String>>?
    get() = definedExternally
    set(value) = definedExternally
  var preserveConstEnums: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var noImplicitOverride: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var preserveSymlinks: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var project: String?
    get() = definedExternally
    set(value) = definedExternally
  var reactNamespace: String?
    get() = definedExternally
    set(value) = definedExternally
  var jsxFactory: String?
    get() = definedExternally
    set(value) = definedExternally
  var jsxFragmentFactory: String?
    get() = definedExternally
    set(value) = definedExternally
  var jsxImportSource: String?
    get() = definedExternally
    set(value) = definedExternally
  var composite: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var incremental: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var tsBuildInfoFile: String?
    get() = definedExternally
    set(value) = definedExternally
  var removeComments: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var rootDir: String?
    get() = definedExternally
    set(value) = definedExternally
  var rootDirs: Array<String>?
    get() = definedExternally
    set(value) = definedExternally
  var skipLibCheck: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var skipDefaultLibCheck: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var sourceMap: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var sourceRoot: String?
    get() = definedExternally
    set(value) = definedExternally
  var strict: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var strictFunctionTypes: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var strictBindCallApply: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var strictNullChecks: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var strictPropertyInitialization: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var stripInternal: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var suppressExcessPropertyErrors: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var suppressImplicitAnyIndexErrors: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var target: ScriptTarget?
    get() = definedExternally
    set(value) = definedExternally
  var traceResolution: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var resolveJsonModule: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var types: Array<String>?
    get() = definedExternally
    set(value) = definedExternally
  var typeRoots: Array<String>?
    get() = definedExternally
    set(value) = definedExternally
  var esModuleInterop: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var useDefineForClassFields: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  @nativeGetter
  operator fun get(option: String): dynamic /* String? | Number? | Boolean? | Array<dynamic /* String | Number */>? | Array<String>? | MapLike<Array<String>>? | Array<PluginImport>? | Array<ProjectReference>? | TsConfigSourceFile? */
  @nativeSetter
  operator fun set(option: String, value: String?)
  @nativeSetter
  operator fun set(option: String, value: Number?)
  @nativeSetter
  operator fun set(option: String, value: Boolean?)
  @nativeSetter
  operator fun set(option: String, value: Array<dynamic /* String | Number */>?)
  @nativeSetter
  operator fun set(option: String, value: Array<String>?)
  @nativeSetter
  operator fun set(option: String, value: MapLike<Array<String>>?)
  @nativeSetter
  operator fun set(option: String, value: Array<PluginImport>?)
  @nativeSetter
  operator fun set(option: String, value: Array<ProjectReference>?)
  @nativeSetter
  operator fun set(option: String, value: TsConfigSourceFile?)
}

external interface WatchOptions {
  var watchFile: WatchFileKind?
    get() = definedExternally
    set(value) = definedExternally
  var watchDirectory: WatchDirectoryKind?
    get() = definedExternally
    set(value) = definedExternally
  var fallbackPolling: PollingWatchKind?
    get() = definedExternally
    set(value) = definedExternally
  var synchronousWatchDirectory: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var excludeDirectories: Array<String>?
    get() = definedExternally
    set(value) = definedExternally
  var excludeFiles: Array<String>?
    get() = definedExternally
    set(value) = definedExternally
  @nativeGetter
  operator fun get(option: String): dynamic /* String? | Number? | Boolean? | Array<dynamic /* String | Number */>? | Array<String>? | MapLike<Array<String>>? | Array<PluginImport>? | Array<ProjectReference>? */
  @nativeSetter
  operator fun set(option: String, value: String?)
  @nativeSetter
  operator fun set(option: String, value: Number?)
  @nativeSetter
  operator fun set(option: String, value: Boolean?)
  @nativeSetter
  operator fun set(option: String, value: Array<dynamic /* String | Number */>?)
  @nativeSetter
  operator fun set(option: String, value: Array<String>?)
  @nativeSetter
  operator fun set(option: String, value: MapLike<Array<String>>?)
  @nativeSetter
  operator fun set(option: String, value: Array<PluginImport>?)
  @nativeSetter
  operator fun set(option: String, value: Array<ProjectReference>?)
}

external interface TypeAcquisition {
  var enableAutoDiscovery: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var enable: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var include: Array<String>?
    get() = definedExternally
    set(value) = definedExternally
  var exclude: Array<String>?
    get() = definedExternally
    set(value) = definedExternally
  var disableFilenameBasedTypeAcquisition: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  @nativeGetter
  operator fun get(option: String): dynamic /* String? | Number? | Boolean? | Array<dynamic /* String | Number */>? | Array<String>? | MapLike<Array<String>>? | Array<PluginImport>? | Array<ProjectReference>? */
  @nativeSetter
  operator fun set(option: String, value: String?)
  @nativeSetter
  operator fun set(option: String, value: Number?)
  @nativeSetter
  operator fun set(option: String, value: Boolean?)
  @nativeSetter
  operator fun set(option: String, value: Array<dynamic /* String | Number */>?)
  @nativeSetter
  operator fun set(option: String, value: Array<String>?)
  @nativeSetter
  operator fun set(option: String, value: MapLike<Array<String>>?)
  @nativeSetter
  operator fun set(option: String, value: Array<PluginImport>?)
  @nativeSetter
  operator fun set(option: String, value: Array<ProjectReference>?)
}

external enum class ModuleKind {
  None /* = 0 */,
  CommonJS /* = 1 */,
  AMD /* = 2 */,
  UMD /* = 3 */,
  System /* = 4 */,
  ES2015 /* = 5 */,
  ES2020 /* = 6 */,
  ESNext /* = 99 */
}

external enum class JsxEmit {
  None /* = 0 */,
  Preserve /* = 1 */,
  React /* = 2 */,
  ReactNative /* = 3 */,
  ReactJSX /* = 4 */,
  ReactJSXDev /* = 5 */
}

external enum class ImportsNotUsedAsValues {
  Remove /* = 0 */,
  Preserve /* = 1 */,
  Error /* = 2 */
}

external enum class NewLineKind {
  CarriageReturnLineFeed /* = 0 */,
  LineFeed /* = 1 */
}

external interface LineAndCharacter {
  var line: Number
  var character: Number
}

external enum class ScriptKind {
  Unknown /* = 0 */,
  JS /* = 1 */,
  JSX /* = 2 */,
  TS /* = 3 */,
  TSX /* = 4 */,
  External /* = 5 */,
  JSON /* = 6 */,
  Deferred /* = 7 */
}

external enum class ScriptTarget {
  ES3 /* = 0 */,
  ES5 /* = 1 */,
  ES2015 /* = 2 */,
  ES2016 /* = 3 */,
  ES2017 /* = 4 */,
  ES2018 /* = 5 */,
  ES2019 /* = 6 */,
  ES2020 /* = 7 */,
  ES2021 /* = 8 */,
  ESNext /* = 99 */,
  JSON /* = 100 */,
  Latest /* = 99 */
}

external enum class LanguageVariant {
  Standard /* = 0 */,
  JSX /* = 1 */
}

external interface ParsedCommandLine {
  var options: CompilerOptions
  var typeAcquisition: TypeAcquisition?
    get() = definedExternally
    set(value) = definedExternally
  var fileNames: Array<String>
  var projectReferences: Array<ProjectReference>?
    get() = definedExternally
    set(value) = definedExternally
  var watchOptions: WatchOptions?
    get() = definedExternally
    set(value) = definedExternally
  var raw: Any?
    get() = definedExternally
    set(value) = definedExternally
  var errors: Array<Diagnostic>
  var wildcardDirectories: MapLike<WatchDirectoryFlags>?
    get() = definedExternally
    set(value) = definedExternally
  var compileOnSave: Boolean?
    get() = definedExternally
    set(value) = definedExternally
}

external enum class WatchDirectoryFlags {
  None /* = 0 */,
  Recursive /* = 1 */
}

external interface CreateProgramOptions {
  var rootNames: Array<String>
  var options: CompilerOptions
  var projectReferences: Array<ProjectReference>?
    get() = definedExternally
    set(value) = definedExternally
  var host: CompilerHost?
    get() = definedExternally
    set(value) = definedExternally
  var oldProgram: Program?
    get() = definedExternally
    set(value) = definedExternally
  var configFileParsingDiagnostics: Array<Diagnostic>?
    get() = definedExternally
    set(value) = definedExternally
}

external interface ModuleResolutionHost {
  fun fileExists(fileName: String): Boolean
  fun readFile(fileName: String): String?
  val trace: ((s: String) -> Unit)?
  val directoryExists: ((directoryName: String) -> Boolean)?
  val realpath: ((path: String) -> String)?
  val getCurrentDirectory: (() -> String)?
  val getDirectories: ((path: String) -> Array<String>)?
}

external interface ResolvedModule {
  var resolvedFileName: String
  var isExternalLibraryImport: Boolean?
    get() = definedExternally
    set(value) = definedExternally
}

external interface ResolvedModuleFull : ResolvedModule {
  var extension: Extension
  var packageId: PackageId?
    get() = definedExternally
    set(value) = definedExternally
}

external interface PackageId {
  var name: String
  var subModuleName: String
  var version: String
}

external enum class Extension {
  Ts /* = ".ts" */,
  Tsx /* = ".tsx" */,
  Dts /* = ".d.ts" */,
  Js /* = ".js" */,
  Jsx /* = ".jsx" */,
  Json /* = ".json" */,
  TsBuildInfo /* = ".tsbuildinfo" */
}

external interface ResolvedModuleWithFailedLookupLocations {
  val resolvedModule: ResolvedModuleFull?
}

external interface ResolvedTypeReferenceDirective {
  var primary: Boolean?
  var resolvedFileName: String?
  var packageId: PackageId?
    get() = definedExternally
    set(value) = definedExternally
  var isExternalLibraryImport: Boolean?
    get() = definedExternally
    set(value) = definedExternally
}

external interface ResolvedTypeReferenceDirectiveWithFailedLookupLocations {
  val resolvedTypeReferenceDirective: ResolvedTypeReferenceDirective?
  val failedLookupLocations: Array<String>
}

external interface CompilerHost : ModuleResolutionHost {
  fun getSourceFile(fileName: String, languageVersion: ScriptTarget, onError: (message: String) -> Unit = definedExternally, shouldCreateNewSourceFile: Boolean = definedExternally): SourceFile?
  val getSourceFileByPath: ((fileName: String, path: String /* String & `T$3` */, languageVersion: ScriptTarget, onError: (message: String) -> Unit, shouldCreateNewSourceFile: Boolean) -> SourceFile?)?
  val getCancellationToken: (() -> CancellationToken)?
  fun getDefaultLibFileName(options: CompilerOptions): String
  val getDefaultLibLocation: (() -> String)?
  var writeFile: WriteFileCallback
  override var getCurrentDirectory: () -> String
  fun getCanonicalFileName(fileName: String): String
  fun useCaseSensitiveFileNames(): Boolean
  fun getNewLine(): String
  val readDirectory: ((rootDir: String, extensions: Array<String>, excludes: Array<String>?, includes: Array<String>, depth: Number) -> Array<String>)?
  val resolveModuleNames: ((moduleNames: Array<String>, containingFile: String, reusedNames: Array<String>?, redirectedReference: ResolvedProjectReference?, options: CompilerOptions) -> Array<ResolvedModule?>)?
  val resolveTypeReferenceDirectives: ((typeReferenceDirectiveNames: Array<String>, containingFile: String, redirectedReference: ResolvedProjectReference?, options: CompilerOptions) -> Array<ResolvedTypeReferenceDirective?>)?
  val getEnvironmentVariable: ((name: String) -> String?)?
  val createHash: ((data: String) -> String)?
  val getParsedCommandLine: ((fileName: String) -> ParsedCommandLine?)?
}

external interface SourceMapRange : TextRange {
  var source: SourceMapSource?
    get() = definedExternally
    set(value) = definedExternally
}

external interface SourceMapSource {
  fun getLineAndCharacterOfPosition(pos: Number): LineAndCharacter
  var fileName: String
  var text: String
  var skipTrivia: ((pos: Number) -> Number)?
    get() = definedExternally
    set(value) = definedExternally
}

external enum class EmitFlags {
  None /* = 0 */,
  SingleLine /* = 1 */,
  AdviseOnEmitNode /* = 2 */,
  NoSubstitution /* = 4 */,
  CapturesThis /* = 8 */,
  NoLeadingSourceMap /* = 16 */,
  NoTrailingSourceMap /* = 32 */,
  NoSourceMap /* = 48 */,
  NoNestedSourceMaps /* = 64 */,
  NoTokenLeadingSourceMaps /* = 128 */,
  NoTokenTrailingSourceMaps /* = 256 */,
  NoTokenSourceMaps /* = 384 */,
  NoLeadingComments /* = 512 */,
  NoTrailingComments /* = 1024 */,
  NoComments /* = 1536 */,
  NoNestedComments /* = 2048 */,
  HelperName /* = 4096 */,
  ExportName /* = 8192 */,
  LocalName /* = 16384 */,
  InternalName /* = 32768 */,
  Indented /* = 65536 */,
  NoIndentation /* = 131072 */,
  AsyncFunctionBody /* = 262144 */,
  ReuseTempVariableScope /* = 524288 */,
  CustomPrologue /* = 1048576 */,
  NoHoisting /* = 2097152 */,
  HasEndOfDeclarationMarker /* = 4194304 */,
  Iterator /* = 8388608 */,
  NoAsciiEscaping /* = 16777216 */
}

external interface EmitHelperBase {
  val name: String
  val scoped: Boolean?
  val text: dynamic /* String | (node: EmitHelperUniqueNameCallback) -> String */
    get() = definedExternally
  val priority: Number?
    get() = definedExternally
  val dependencies: Array<dynamic /* ScopedEmitHelper | UnscopedEmitHelper */>?
    get() = definedExternally
}

external interface ScopedEmitHelper : EmitHelperBase {
  override val scoped: Boolean?
}

external interface UnscopedEmitHelper : EmitHelperBase {
  override val scoped: Boolean?
  override val text: String
}

external enum class EmitHint {
  SourceFile /* = 0 */,
  Expression /* = 1 */,
  IdentifierName /* = 2 */,
  MappedTypeParameter /* = 3 */,
  Unspecified /* = 4 */,
  EmbeddedStatement /* = 5 */,
  JsxAttributeValue /* = 6 */
}

external enum class OuterExpressionKinds {
  Parentheses /* = 1 */,
  TypeAssertions /* = 2 */,
  NonNullAssertions /* = 4 */,
  PartiallyEmittedExpressions /* = 8 */,
  Assertions /* = 6 */,
  All /* = 15 */
}

external interface NodeFactory {
  fun <T : Node> createNodeArray(elements: Array<T> = definedExternally, hasTrailingComma: Boolean = definedExternally): NodeArray<T>
  fun createNumericLiteral(value: String, numericLiteralFlags: TokenFlags = definedExternally): NumericLiteral
  fun createNumericLiteral(value: String): NumericLiteral
  fun createNumericLiteral(value: Number, numericLiteralFlags: TokenFlags = definedExternally): NumericLiteral
  fun createNumericLiteral(value: Number): NumericLiteral
  fun createBigIntLiteral(value: String): BigIntLiteral
  fun createBigIntLiteral(value: PseudoBigInt): BigIntLiteral
  fun createStringLiteral(text: String, isSingleQuote: Boolean = definedExternally): StringLiteral
  fun createStringLiteralFromNode(sourceNode: Identifier, isSingleQuote: Boolean = definedExternally): StringLiteral
  fun createStringLiteralFromNode(sourceNode: Identifier): StringLiteral
  fun createStringLiteralFromNode(sourceNode: StringLiteral, isSingleQuote: Boolean = definedExternally): StringLiteral
  fun createStringLiteralFromNode(sourceNode: StringLiteral): StringLiteral
  fun createStringLiteralFromNode(sourceNode: NoSubstitutionTemplateLiteral, isSingleQuote: Boolean = definedExternally): StringLiteral
  fun createStringLiteralFromNode(sourceNode: NoSubstitutionTemplateLiteral): StringLiteral
  fun createStringLiteralFromNode(sourceNode: NumericLiteral, isSingleQuote: Boolean = definedExternally): StringLiteral
  fun createStringLiteralFromNode(sourceNode: NumericLiteral): StringLiteral
  fun createRegularExpressionLiteral(text: String): RegularExpressionLiteral
  fun createIdentifier(text: String): Identifier
  fun createTempVariable(recordTempVariable: ((node: Identifier) -> Unit)?, reservedInNestedScopes: Boolean = definedExternally): Identifier
  fun createLoopVariable(reservedInNestedScopes: Boolean = definedExternally): Identifier
  fun createUniqueName(text: String, flags: GeneratedIdentifierFlags = definedExternally): Identifier
  fun getGeneratedNameForNode(node: Node?, flags: GeneratedIdentifierFlags = definedExternally): Identifier
  fun createPrivateIdentifier(text: String): PrivateIdentifier
//  fun createToken(token: SyntaxKind = definedExternally): SuperExpression
//  fun createToken(token: SyntaxKind = definedExternally): ThisExpression
//  fun createToken(token: SyntaxKind = definedExternally): NullLiteral
//  fun createToken(token: SyntaxKind = definedExternally): TrueLiteral
//  fun createToken(token: SyntaxKind = definedExternally): FalseLiteral
  fun <TKind: SyntaxKind> createToken(token: TKind): dynamic /* Token */
  fun createSuper(): SuperExpression
  fun createThis(): ThisExpression
  fun createNull(): NullLiteral
  fun createTrue(): TrueLiteral
  fun createFalse(): FalseLiteral
  fun <T : SyntaxKind> createModifier(kind: T): ModifierToken<T>
  fun createModifiersFromModifierFlags(flags: ModifierFlags): Array<dynamic /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>
  fun createQualifiedName(left: Identifier, right: String): QualifiedName
  fun createQualifiedName(left: Identifier, right: Identifier): QualifiedName
  fun createQualifiedName(left: QualifiedName, right: String): QualifiedName
  fun createQualifiedName(left: QualifiedName, right: Identifier): QualifiedName
  fun updateQualifiedName(node: QualifiedName, left: Identifier, right: Identifier): QualifiedName
  fun updateQualifiedName(node: QualifiedName, left: QualifiedName, right: Identifier): QualifiedName
  fun createComputedPropertyName(expression: Expression): ComputedPropertyName
  fun updateComputedPropertyName(node: ComputedPropertyName, expression: Expression): ComputedPropertyName
  fun createTypeParameterDeclaration(name: String, constraint: TypeNode = definedExternally, defaultType: TypeNode = definedExternally): TypeParameterDeclaration
  fun createTypeParameterDeclaration(name: String): TypeParameterDeclaration
  fun createTypeParameterDeclaration(name: String, constraint: TypeNode = definedExternally): TypeParameterDeclaration
  fun createTypeParameterDeclaration(name: Identifier, constraint: TypeNode = definedExternally, defaultType: TypeNode = definedExternally): TypeParameterDeclaration
  fun createTypeParameterDeclaration(name: Identifier): TypeParameterDeclaration
  fun createTypeParameterDeclaration(name: Identifier, constraint: TypeNode = definedExternally): TypeParameterDeclaration
  fun updateTypeParameterDeclaration(node: TypeParameterDeclaration, name: Identifier, constraint: TypeNode?, defaultType: TypeNode?): TypeParameterDeclaration
  fun createParameterDeclaration(decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, dotDotDotToken: DotDotDotToken?, name: String, questionToken: QuestionToken = definedExternally, type: TypeNode = definedExternally, initializer: Expression = definedExternally): ParameterDeclaration
  fun createParameterDeclaration(decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, dotDotDotToken: DotDotDotToken?, name: String): ParameterDeclaration
  fun createParameterDeclaration(decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, dotDotDotToken: DotDotDotToken?, name: String, questionToken: QuestionToken = definedExternally): ParameterDeclaration
  fun createParameterDeclaration(decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, dotDotDotToken: DotDotDotToken?, name: String, questionToken: QuestionToken = definedExternally, type: TypeNode = definedExternally): ParameterDeclaration
  fun createParameterDeclaration(decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, dotDotDotToken: DotDotDotToken?, name: Identifier, questionToken: QuestionToken = definedExternally, type: TypeNode = definedExternally, initializer: Expression = definedExternally): ParameterDeclaration
  fun createParameterDeclaration(decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, dotDotDotToken: DotDotDotToken?, name: Identifier): ParameterDeclaration
  fun createParameterDeclaration(decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, dotDotDotToken: DotDotDotToken?, name: Identifier, questionToken: QuestionToken = definedExternally): ParameterDeclaration
  fun createParameterDeclaration(decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, dotDotDotToken: DotDotDotToken?, name: Identifier, questionToken: QuestionToken = definedExternally, type: TypeNode = definedExternally): ParameterDeclaration
  fun createParameterDeclaration(decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, dotDotDotToken: DotDotDotToken?, name: ObjectBindingPattern, questionToken: QuestionToken = definedExternally, type: TypeNode = definedExternally, initializer: Expression = definedExternally): ParameterDeclaration
  fun createParameterDeclaration(decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, dotDotDotToken: DotDotDotToken?, name: ObjectBindingPattern): ParameterDeclaration
  fun createParameterDeclaration(decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, dotDotDotToken: DotDotDotToken?, name: ObjectBindingPattern, questionToken: QuestionToken = definedExternally): ParameterDeclaration
  fun createParameterDeclaration(decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, dotDotDotToken: DotDotDotToken?, name: ObjectBindingPattern, questionToken: QuestionToken = definedExternally, type: TypeNode = definedExternally): ParameterDeclaration
  fun createParameterDeclaration(decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, dotDotDotToken: DotDotDotToken?, name: ArrayBindingPattern, questionToken: QuestionToken = definedExternally, type: TypeNode = definedExternally, initializer: Expression = definedExternally): ParameterDeclaration
  fun createParameterDeclaration(decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, dotDotDotToken: DotDotDotToken?, name: ArrayBindingPattern): ParameterDeclaration
  fun createParameterDeclaration(decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, dotDotDotToken: DotDotDotToken?, name: ArrayBindingPattern, questionToken: QuestionToken = definedExternally): ParameterDeclaration
  fun createParameterDeclaration(decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, dotDotDotToken: DotDotDotToken?, name: ArrayBindingPattern, questionToken: QuestionToken = definedExternally, type: TypeNode = definedExternally): ParameterDeclaration
  fun updateParameterDeclaration(node: ParameterDeclaration, decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, dotDotDotToken: DotDotDotToken?, name: String, questionToken: QuestionToken?, type: TypeNode?, initializer: Expression?): ParameterDeclaration
  fun updateParameterDeclaration(node: ParameterDeclaration, decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, dotDotDotToken: DotDotDotToken?, name: Identifier, questionToken: QuestionToken?, type: TypeNode?, initializer: Expression?): ParameterDeclaration
  fun updateParameterDeclaration(node: ParameterDeclaration, decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, dotDotDotToken: DotDotDotToken?, name: ObjectBindingPattern, questionToken: QuestionToken?, type: TypeNode?, initializer: Expression?): ParameterDeclaration
  fun updateParameterDeclaration(node: ParameterDeclaration, decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, dotDotDotToken: DotDotDotToken?, name: ArrayBindingPattern, questionToken: QuestionToken?, type: TypeNode?, initializer: Expression?): ParameterDeclaration
  fun createDecorator(expression: Expression): Decorator
  fun updateDecorator(node: Decorator, expression: Expression): Decorator
  fun createPropertySignature(modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: Identifier, questionToken: QuestionToken?, type: TypeNode?): PropertySignature
  fun createPropertySignature(modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: StringLiteral, questionToken: QuestionToken?, type: TypeNode?): PropertySignature
  fun createPropertySignature(modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: NumericLiteral, questionToken: QuestionToken?, type: TypeNode?): PropertySignature
  fun createPropertySignature(modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: ComputedPropertyName, questionToken: QuestionToken?, type: TypeNode?): PropertySignature
  fun createPropertySignature(modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: PrivateIdentifier, questionToken: QuestionToken?, type: TypeNode?): PropertySignature
  fun createPropertySignature(modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: String, questionToken: QuestionToken?, type: TypeNode?): PropertySignature
  fun updatePropertySignature(node: PropertySignature, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: Identifier, questionToken: QuestionToken?, type: TypeNode?): PropertySignature
  fun updatePropertySignature(node: PropertySignature, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: StringLiteral, questionToken: QuestionToken?, type: TypeNode?): PropertySignature
  fun updatePropertySignature(node: PropertySignature, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: NumericLiteral, questionToken: QuestionToken?, type: TypeNode?): PropertySignature
  fun updatePropertySignature(node: PropertySignature, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: ComputedPropertyName, questionToken: QuestionToken?, type: TypeNode?): PropertySignature
  fun updatePropertySignature(node: PropertySignature, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: PrivateIdentifier, questionToken: QuestionToken?, type: TypeNode?): PropertySignature

  fun createPropertyDeclaration(decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: String, questionOrExclamationToken: SyntaxKind? = definedExternally, type: TypeNode?, initializer: Expression?): PropertyDeclaration
  fun createPropertyDeclaration(decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: Identifier, questionOrExclamationToken: SyntaxKind? = definedExternally, type: TypeNode?, initializer: Expression?): PropertyDeclaration
  fun createPropertyDeclaration(decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: StringLiteral, questionOrExclamationToken: SyntaxKind? = definedExternally, type: TypeNode?, initializer: Expression?): PropertyDeclaration
  fun createPropertyDeclaration(decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: NumericLiteral, questionOrExclamationToken: SyntaxKind? = definedExternally, type: TypeNode?, initializer: Expression?): PropertyDeclaration
  fun createPropertyDeclaration(decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: ComputedPropertyName, questionOrExclamationToken: SyntaxKind? = definedExternally, type: TypeNode?, initializer: Expression?): PropertyDeclaration
  fun createPropertyDeclaration(decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: PrivateIdentifier, questionOrExclamationToken: SyntaxKind? = definedExternally, type: TypeNode?, initializer: Expression?): PropertyDeclaration

  fun updatePropertyDeclaration(node: PropertyDeclaration, decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: String, questionOrExclamationToken: SyntaxKind? = definedExternally, type: TypeNode?, initializer: Expression?): PropertyDeclaration
  fun updatePropertyDeclaration(node: PropertyDeclaration, decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: Identifier, questionOrExclamationToken: SyntaxKind? = definedExternally, type: TypeNode?, initializer: Expression?): PropertyDeclaration
  fun updatePropertyDeclaration(node: PropertyDeclaration, decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: StringLiteral, questionOrExclamationToken: SyntaxKind? = definedExternally, type: TypeNode?, initializer: Expression?): PropertyDeclaration
  fun updatePropertyDeclaration(node: PropertyDeclaration, decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: NumericLiteral, questionOrExclamationToken: SyntaxKind? = definedExternally, type: TypeNode?, initializer: Expression?): PropertyDeclaration
  fun updatePropertyDeclaration(node: PropertyDeclaration, decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: ComputedPropertyName, questionOrExclamationToken: SyntaxKind? = definedExternally, type: TypeNode?, initializer: Expression?): PropertyDeclaration
  fun updatePropertyDeclaration(node: PropertyDeclaration, decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: PrivateIdentifier, questionOrExclamationToken: SyntaxKind? = definedExternally, type: TypeNode?, initializer: Expression?): PropertyDeclaration

  fun createMethodSignature(modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: String, questionToken: QuestionToken?, typeParameters: Array<TypeParameterDeclaration>?, parameters: Array<ParameterDeclaration>, type: TypeNode?): MethodSignature
  fun createMethodSignature(modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: Identifier, questionToken: QuestionToken?, typeParameters: Array<TypeParameterDeclaration>?, parameters: Array<ParameterDeclaration>, type: TypeNode?): MethodSignature
  fun createMethodSignature(modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: StringLiteral, questionToken: QuestionToken?, typeParameters: Array<TypeParameterDeclaration>?, parameters: Array<ParameterDeclaration>, type: TypeNode?): MethodSignature
  fun createMethodSignature(modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: NumericLiteral, questionToken: QuestionToken?, typeParameters: Array<TypeParameterDeclaration>?, parameters: Array<ParameterDeclaration>, type: TypeNode?): MethodSignature
  fun createMethodSignature(modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: ComputedPropertyName, questionToken: QuestionToken?, typeParameters: Array<TypeParameterDeclaration>?, parameters: Array<ParameterDeclaration>, type: TypeNode?): MethodSignature
  fun createMethodSignature(modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: PrivateIdentifier, questionToken: QuestionToken?, typeParameters: Array<TypeParameterDeclaration>?, parameters: Array<ParameterDeclaration>, type: TypeNode?): MethodSignature

  fun updateMethodSignature(node: MethodSignature, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: Identifier, questionToken: QuestionToken?, typeParameters: NodeArray<TypeParameterDeclaration>?, parameters: NodeArray<ParameterDeclaration>, type: TypeNode?): MethodSignature
  fun updateMethodSignature(node: MethodSignature, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: StringLiteral, questionToken: QuestionToken?, typeParameters: NodeArray<TypeParameterDeclaration>?, parameters: NodeArray<ParameterDeclaration>, type: TypeNode?): MethodSignature
  fun updateMethodSignature(node: MethodSignature, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: NumericLiteral, questionToken: QuestionToken?, typeParameters: NodeArray<TypeParameterDeclaration>?, parameters: NodeArray<ParameterDeclaration>, type: TypeNode?): MethodSignature
  fun updateMethodSignature(node: MethodSignature, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: ComputedPropertyName, questionToken: QuestionToken?, typeParameters: NodeArray<TypeParameterDeclaration>?, parameters: NodeArray<ParameterDeclaration>, type: TypeNode?): MethodSignature
  fun updateMethodSignature(node: MethodSignature, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: PrivateIdentifier, questionToken: QuestionToken?, typeParameters: NodeArray<TypeParameterDeclaration>?, parameters: NodeArray<ParameterDeclaration>, type: TypeNode?): MethodSignature

  fun createMethodDeclaration(decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, asteriskToken: AsteriskToken?, name: String, questionToken: QuestionToken?, typeParameters: Array<TypeParameterDeclaration>?, parameters: Array<ParameterDeclaration>, type: TypeNode?, body: Block?): MethodDeclaration
  fun createMethodDeclaration(decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, asteriskToken: AsteriskToken?, name: Identifier, questionToken: QuestionToken?, typeParameters: Array<TypeParameterDeclaration>?, parameters: Array<ParameterDeclaration>, type: TypeNode?, body: Block?): MethodDeclaration
  fun createMethodDeclaration(decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, asteriskToken: AsteriskToken?, name: StringLiteral, questionToken: QuestionToken?, typeParameters: Array<TypeParameterDeclaration>?, parameters: Array<ParameterDeclaration>, type: TypeNode?, body: Block?): MethodDeclaration
  fun createMethodDeclaration(decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, asteriskToken: AsteriskToken?, name: NumericLiteral, questionToken: QuestionToken?, typeParameters: Array<TypeParameterDeclaration>?, parameters: Array<ParameterDeclaration>, type: TypeNode?, body: Block?): MethodDeclaration
  fun createMethodDeclaration(decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, asteriskToken: AsteriskToken?, name: ComputedPropertyName, questionToken: QuestionToken?, typeParameters: Array<TypeParameterDeclaration>?, parameters: Array<ParameterDeclaration>, type: TypeNode?, body: Block?): MethodDeclaration
  fun createMethodDeclaration(decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, asteriskToken: AsteriskToken?, name: PrivateIdentifier, questionToken: QuestionToken?, typeParameters: Array<TypeParameterDeclaration>?, parameters: Array<ParameterDeclaration>, type: TypeNode?, body: Block?): MethodDeclaration

  fun updateMethodDeclaration(node: MethodDeclaration, decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, asteriskToken: AsteriskToken?, name: Identifier, questionToken: QuestionToken?, typeParameters: Array<TypeParameterDeclaration>?, parameters: Array<ParameterDeclaration>, type: TypeNode?, body: Block?): MethodDeclaration
  fun updateMethodDeclaration(node: MethodDeclaration, decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, asteriskToken: AsteriskToken?, name: StringLiteral, questionToken: QuestionToken?, typeParameters: Array<TypeParameterDeclaration>?, parameters: Array<ParameterDeclaration>, type: TypeNode?, body: Block?): MethodDeclaration
  fun updateMethodDeclaration(node: MethodDeclaration, decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, asteriskToken: AsteriskToken?, name: NumericLiteral, questionToken: QuestionToken?, typeParameters: Array<TypeParameterDeclaration>?, parameters: Array<ParameterDeclaration>, type: TypeNode?, body: Block?): MethodDeclaration
  fun updateMethodDeclaration(node: MethodDeclaration, decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, asteriskToken: AsteriskToken?, name: ComputedPropertyName, questionToken: QuestionToken?, typeParameters: Array<TypeParameterDeclaration>?, parameters: Array<ParameterDeclaration>, type: TypeNode?, body: Block?): MethodDeclaration
  fun updateMethodDeclaration(node: MethodDeclaration, decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, asteriskToken: AsteriskToken?, name: PrivateIdentifier, questionToken: QuestionToken?, typeParameters: Array<TypeParameterDeclaration>?, parameters: Array<ParameterDeclaration>, type: TypeNode?, body: Block?): MethodDeclaration

  fun createConstructorDeclaration(decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, parameters: Array<ParameterDeclaration>, body: Block?): ConstructorDeclaration
  fun updateConstructorDeclaration(node: ConstructorDeclaration, decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, parameters: Array<ParameterDeclaration>, body: Block?): ConstructorDeclaration

  fun createGetAccessorDeclaration(decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: String, parameters: Array<ParameterDeclaration>, type: TypeNode?, body: Block?): GetAccessorDeclaration
  fun createGetAccessorDeclaration(decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: Identifier, parameters: Array<ParameterDeclaration>, type: TypeNode?, body: Block?): GetAccessorDeclaration
  fun createGetAccessorDeclaration(decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: StringLiteral, parameters: Array<ParameterDeclaration>, type: TypeNode?, body: Block?): GetAccessorDeclaration
  fun createGetAccessorDeclaration(decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: NumericLiteral, parameters: Array<ParameterDeclaration>, type: TypeNode?, body: Block?): GetAccessorDeclaration
  fun createGetAccessorDeclaration(decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: ComputedPropertyName, parameters: Array<ParameterDeclaration>, type: TypeNode?, body: Block?): GetAccessorDeclaration
  fun createGetAccessorDeclaration(decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: PrivateIdentifier, parameters: Array<ParameterDeclaration>, type: TypeNode?, body: Block?): GetAccessorDeclaration
  fun updateGetAccessorDeclaration(node: GetAccessorDeclaration, decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: Identifier, parameters: Array<ParameterDeclaration>, type: TypeNode?, body: Block?): GetAccessorDeclaration
  fun updateGetAccessorDeclaration(node: GetAccessorDeclaration, decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: StringLiteral, parameters: Array<ParameterDeclaration>, type: TypeNode?, body: Block?): GetAccessorDeclaration
  fun updateGetAccessorDeclaration(node: GetAccessorDeclaration, decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: NumericLiteral, parameters: Array<ParameterDeclaration>, type: TypeNode?, body: Block?): GetAccessorDeclaration
  fun updateGetAccessorDeclaration(node: GetAccessorDeclaration, decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: ComputedPropertyName, parameters: Array<ParameterDeclaration>, type: TypeNode?, body: Block?): GetAccessorDeclaration
  fun updateGetAccessorDeclaration(node: GetAccessorDeclaration, decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: PrivateIdentifier, parameters: Array<ParameterDeclaration>, type: TypeNode?, body: Block?): GetAccessorDeclaration

  fun createSetAccessorDeclaration(decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: String, parameters: Array<ParameterDeclaration>, body: Block?): SetAccessorDeclaration
  fun createSetAccessorDeclaration(decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: Identifier, parameters: Array<ParameterDeclaration>, body: Block?): SetAccessorDeclaration
  fun createSetAccessorDeclaration(decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: StringLiteral, parameters: Array<ParameterDeclaration>, body: Block?): SetAccessorDeclaration
  fun createSetAccessorDeclaration(decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: NumericLiteral, parameters: Array<ParameterDeclaration>, body: Block?): SetAccessorDeclaration
  fun createSetAccessorDeclaration(decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: ComputedPropertyName, parameters: Array<ParameterDeclaration>, body: Block?): SetAccessorDeclaration
  fun createSetAccessorDeclaration(decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: PrivateIdentifier, parameters: Array<ParameterDeclaration>, body: Block?): SetAccessorDeclaration
  fun updateSetAccessorDeclaration(node: SetAccessorDeclaration, decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: Identifier, parameters: Array<ParameterDeclaration>, body: Block?): SetAccessorDeclaration
  fun updateSetAccessorDeclaration(node: SetAccessorDeclaration, decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: StringLiteral, parameters: Array<ParameterDeclaration>, body: Block?): SetAccessorDeclaration
  fun updateSetAccessorDeclaration(node: SetAccessorDeclaration, decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: NumericLiteral, parameters: Array<ParameterDeclaration>, body: Block?): SetAccessorDeclaration
  fun updateSetAccessorDeclaration(node: SetAccessorDeclaration, decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: ComputedPropertyName, parameters: Array<ParameterDeclaration>, body: Block?): SetAccessorDeclaration
  fun updateSetAccessorDeclaration(node: SetAccessorDeclaration, decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: PrivateIdentifier, parameters: Array<ParameterDeclaration>, body: Block?): SetAccessorDeclaration

  fun createCallSignature(typeParameters: Array<TypeParameterDeclaration>?, parameters: Array<ParameterDeclaration>, type: TypeNode?): CallSignatureDeclaration
  fun updateCallSignature(node: CallSignatureDeclaration, typeParameters: NodeArray<TypeParameterDeclaration>?, parameters: NodeArray<ParameterDeclaration>, type: TypeNode?): CallSignatureDeclaration

  fun createConstructSignature(typeParameters: Array<TypeParameterDeclaration>?, parameters: Array<ParameterDeclaration>, type: TypeNode?): ConstructSignatureDeclaration
  fun updateConstructSignature(node: ConstructSignatureDeclaration, typeParameters: NodeArray<TypeParameterDeclaration>?, parameters: NodeArray<ParameterDeclaration>, type: TypeNode?): ConstructSignatureDeclaration

  fun createIndexSignature(decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, parameters: Array<ParameterDeclaration>, type: TypeNode): IndexSignatureDeclaration
  fun updateIndexSignature(node: IndexSignatureDeclaration, decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, parameters: Array<ParameterDeclaration>, type: TypeNode): IndexSignatureDeclaration

  fun createTemplateLiteralTypeSpan(type: TypeNode, literal: TemplateMiddle): TemplateLiteralTypeSpan
  fun createTemplateLiteralTypeSpan(type: TypeNode, literal: TemplateTail): TemplateLiteralTypeSpan

  fun updateTemplateLiteralTypeSpan(node: TemplateLiteralTypeSpan, type: TypeNode, literal: TemplateMiddle): TemplateLiteralTypeSpan
  fun updateTemplateLiteralTypeSpan(node: TemplateLiteralTypeSpan, type: TypeNode, literal: TemplateTail): TemplateLiteralTypeSpan
  fun <TKind : SyntaxKind> createKeywordTypeNode(kind: TKind): KeywordTypeNode<TKind>
  fun createTypePredicateNode(assertsModifier: AssertsKeyword?, parameterName: Identifier, type: TypeNode?): TypePredicateNode
  fun createTypePredicateNode(assertsModifier: AssertsKeyword?, parameterName: ThisTypeNode, type: TypeNode?): TypePredicateNode
  fun createTypePredicateNode(assertsModifier: AssertsKeyword?, parameterName: String, type: TypeNode?): TypePredicateNode
  fun updateTypePredicateNode(node: TypePredicateNode, assertsModifier: AssertsKeyword?, parameterName: Identifier, type: TypeNode?): TypePredicateNode
  fun updateTypePredicateNode(node: TypePredicateNode, assertsModifier: AssertsKeyword?, parameterName: ThisTypeNode, type: TypeNode?): TypePredicateNode
  fun createTypeReferenceNode(typeName: String, typeArguments: Array<TypeNode> = definedExternally): TypeReferenceNode
  fun createTypeReferenceNode(typeName: String): TypeReferenceNode
  fun createTypeReferenceNode(typeName: Identifier, typeArguments: Array<TypeNode> = definedExternally): TypeReferenceNode
  fun createTypeReferenceNode(typeName: Identifier): TypeReferenceNode
  fun createTypeReferenceNode(typeName: QualifiedName, typeArguments: Array<TypeNode> = definedExternally): TypeReferenceNode
  fun createTypeReferenceNode(typeName: QualifiedName): TypeReferenceNode
  fun updateTypeReferenceNode(node: TypeReferenceNode, typeName: Identifier, typeArguments: NodeArray<TypeNode>?): TypeReferenceNode
  fun updateTypeReferenceNode(node: TypeReferenceNode, typeName: QualifiedName, typeArguments: NodeArray<TypeNode>?): TypeReferenceNode
  fun createFunctionTypeNode(typeParameters: Array<TypeParameterDeclaration>?, parameters: Array<ParameterDeclaration>, type: TypeNode): FunctionTypeNode
  fun updateFunctionTypeNode(node: FunctionTypeNode, typeParameters: NodeArray<TypeParameterDeclaration>?, parameters: NodeArray<ParameterDeclaration>, type: TypeNode): FunctionTypeNode
  fun createConstructorTypeNode(modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, typeParameters: Array<TypeParameterDeclaration>?, parameters: Array<ParameterDeclaration>, type: TypeNode): ConstructorTypeNode
  fun createConstructorTypeNode(typeParameters: Array<TypeParameterDeclaration>?, parameters: Array<ParameterDeclaration>, type: TypeNode): ConstructorTypeNode
  fun updateConstructorTypeNode(node: ConstructorTypeNode, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, typeParameters: NodeArray<TypeParameterDeclaration>?, parameters: NodeArray<ParameterDeclaration>, type: TypeNode): ConstructorTypeNode
  fun updateConstructorTypeNode(node: ConstructorTypeNode, typeParameters: NodeArray<TypeParameterDeclaration>?, parameters: NodeArray<ParameterDeclaration>, type: TypeNode): ConstructorTypeNode
  fun createTypeQueryNode(exprName: Identifier): TypeQueryNode
  fun createTypeQueryNode(exprName: QualifiedName): TypeQueryNode
  fun updateTypeQueryNode(node: TypeQueryNode, exprName: Identifier): TypeQueryNode
  fun updateTypeQueryNode(node: TypeQueryNode, exprName: QualifiedName): TypeQueryNode
  fun createTypeLiteralNode(members: Array<TypeElement>?): TypeLiteralNode
  fun updateTypeLiteralNode(node: TypeLiteralNode, members: NodeArray<TypeElement>): TypeLiteralNode
  fun createArrayTypeNode(elementType: TypeNode): ArrayTypeNode
  fun updateArrayTypeNode(node: ArrayTypeNode, elementType: TypeNode): ArrayTypeNode
  fun createTupleTypeNode(elements: Array<Any /* TypeNode | NamedTupleMember */>): TupleTypeNode
  fun updateTupleTypeNode(node: TupleTypeNode, elements: Array<Any /* TypeNode | NamedTupleMember */>): TupleTypeNode
  fun createNamedTupleMember(dotDotDotToken: DotDotDotToken?, name: Identifier, questionToken: QuestionToken?, type: TypeNode): NamedTupleMember
  fun updateNamedTupleMember(node: NamedTupleMember, dotDotDotToken: DotDotDotToken?, name: Identifier, questionToken: QuestionToken?, type: TypeNode): NamedTupleMember
  fun createOptionalTypeNode(type: TypeNode): OptionalTypeNode
  fun updateOptionalTypeNode(node: OptionalTypeNode, type: TypeNode): OptionalTypeNode
  fun createRestTypeNode(type: TypeNode): RestTypeNode
  fun updateRestTypeNode(node: RestTypeNode, type: TypeNode): RestTypeNode
  fun createUnionTypeNode(types: Array<TypeNode>): UnionTypeNode
  fun updateUnionTypeNode(node: UnionTypeNode, types: NodeArray<TypeNode>): UnionTypeNode
  fun createIntersectionTypeNode(types: Array<TypeNode>): IntersectionTypeNode
  fun updateIntersectionTypeNode(node: IntersectionTypeNode, types: NodeArray<TypeNode>): IntersectionTypeNode
  fun createConditionalTypeNode(checkType: TypeNode, extendsType: TypeNode, trueType: TypeNode, falseType: TypeNode): ConditionalTypeNode
  fun updateConditionalTypeNode(node: ConditionalTypeNode, checkType: TypeNode, extendsType: TypeNode, trueType: TypeNode, falseType: TypeNode): ConditionalTypeNode
  fun createInferTypeNode(typeParameter: TypeParameterDeclaration): InferTypeNode
  fun updateInferTypeNode(node: InferTypeNode, typeParameter: TypeParameterDeclaration): InferTypeNode
  fun createImportTypeNode(argument: TypeNode, qualifier: Identifier = definedExternally, typeArguments: Array<TypeNode> = definedExternally, isTypeOf: Boolean = definedExternally): ImportTypeNode
  fun createImportTypeNode(argument: TypeNode): ImportTypeNode
  fun createImportTypeNode(argument: TypeNode, qualifier: Identifier = definedExternally): ImportTypeNode
  fun createImportTypeNode(argument: TypeNode, qualifier: Identifier = definedExternally, typeArguments: Array<TypeNode> = definedExternally): ImportTypeNode
  fun createImportTypeNode(argument: TypeNode, qualifier: QualifiedName = definedExternally, typeArguments: Array<TypeNode> = definedExternally, isTypeOf: Boolean = definedExternally): ImportTypeNode
  fun createImportTypeNode(argument: TypeNode, qualifier: QualifiedName = definedExternally): ImportTypeNode
  fun createImportTypeNode(argument: TypeNode, qualifier: QualifiedName = definedExternally, typeArguments: Array<TypeNode> = definedExternally): ImportTypeNode
  fun updateImportTypeNode(node: ImportTypeNode, argument: TypeNode, qualifier: Identifier?, typeArguments: Array<TypeNode>?, isTypeOf: Boolean = definedExternally): ImportTypeNode
  fun updateImportTypeNode(node: ImportTypeNode, argument: TypeNode, qualifier: Identifier?, typeArguments: Array<TypeNode>?): ImportTypeNode
  fun updateImportTypeNode(node: ImportTypeNode, argument: TypeNode, qualifier: QualifiedName?, typeArguments: Array<TypeNode>?, isTypeOf: Boolean = definedExternally): ImportTypeNode
  fun updateImportTypeNode(node: ImportTypeNode, argument: TypeNode, qualifier: QualifiedName?, typeArguments: Array<TypeNode>?): ImportTypeNode
  fun createParenthesizedType(type: TypeNode): ParenthesizedTypeNode
  fun updateParenthesizedType(node: ParenthesizedTypeNode, type: TypeNode): ParenthesizedTypeNode
  fun createThisTypeNode(): ThisTypeNode
  fun createTypeOperatorNode(operator: SyntaxKind = definedExternally, type: TypeNode): TypeOperatorNode
  fun updateTypeOperatorNode(node: TypeOperatorNode, type: TypeNode): TypeOperatorNode
  fun createIndexedAccessTypeNode(objectType: TypeNode, indexType: TypeNode): IndexedAccessTypeNode
  fun updateIndexedAccessTypeNode(node: IndexedAccessTypeNode, objectType: TypeNode, indexType: TypeNode): IndexedAccessTypeNode

  fun createMappedTypeNode(readonlyToken: SyntaxKind? = definedExternally, typeParameter: TypeParameterDeclaration, nameType: TypeNode?, questionToken: SyntaxKind? = definedExternally, type: TypeNode?): MappedTypeNode

  fun updateMappedTypeNode(node: MappedTypeNode, readonlyToken: SyntaxKind? = definedExternally, typeParameter: TypeParameterDeclaration, nameType: TypeNode?, questionToken: SyntaxKind? = definedExternally, type: TypeNode?): MappedTypeNode

  fun createLiteralTypeNode(literal: NullLiteral): LiteralTypeNode
  fun createLiteralTypeNode(literal: TrueLiteral): LiteralTypeNode
  fun createLiteralTypeNode(literal: FalseLiteral): LiteralTypeNode
  fun createLiteralTypeNode(literal: LiteralExpression): LiteralTypeNode
  fun createLiteralTypeNode(literal: PrefixUnaryExpression): LiteralTypeNode
  fun updateLiteralTypeNode(node: LiteralTypeNode, literal: NullLiteral): LiteralTypeNode
  fun updateLiteralTypeNode(node: LiteralTypeNode, literal: TrueLiteral): LiteralTypeNode
  fun updateLiteralTypeNode(node: LiteralTypeNode, literal: FalseLiteral): LiteralTypeNode
  fun updateLiteralTypeNode(node: LiteralTypeNode, literal: LiteralExpression): LiteralTypeNode
  fun updateLiteralTypeNode(node: LiteralTypeNode, literal: PrefixUnaryExpression): LiteralTypeNode
  fun createTemplateLiteralType(head: TemplateHead, templateSpans: Array<TemplateLiteralTypeSpan>): TemplateLiteralTypeNode
  fun updateTemplateLiteralType(node: TemplateLiteralTypeNode, head: TemplateHead, templateSpans: Array<TemplateLiteralTypeSpan>): TemplateLiteralTypeNode
  fun createObjectBindingPattern(elements: Array<BindingElement>): ObjectBindingPattern
  fun updateObjectBindingPattern(node: ObjectBindingPattern, elements: Array<BindingElement>): ObjectBindingPattern
  fun createArrayBindingPattern(elements: Array<Any /* BindingElement | OmittedExpression */>): ArrayBindingPattern
  fun updateArrayBindingPattern(node: ArrayBindingPattern, elements: Array<Any /* BindingElement | OmittedExpression */>): ArrayBindingPattern
  fun createBindingElement(dotDotDotToken: DotDotDotToken?, propertyName: String?, name: Any /* String | Identifier | ObjectBindingPattern | ArrayBindingPattern */, initializer: Expression = definedExternally): BindingElement
  fun createBindingElement(dotDotDotToken: DotDotDotToken?, propertyName: String?, name: Any /* String | Identifier | ObjectBindingPattern | ArrayBindingPattern */): BindingElement
  fun createBindingElement(dotDotDotToken: DotDotDotToken?, propertyName: Identifier?, name: Any /* String | Identifier | ObjectBindingPattern | ArrayBindingPattern */, initializer: Expression = definedExternally): BindingElement
  fun createBindingElement(dotDotDotToken: DotDotDotToken?, propertyName: Identifier?, name: Any /* String | Identifier | ObjectBindingPattern | ArrayBindingPattern */): BindingElement
  fun createBindingElement(dotDotDotToken: DotDotDotToken?, propertyName: StringLiteral?, name: Any /* String | Identifier | ObjectBindingPattern | ArrayBindingPattern */, initializer: Expression = definedExternally): BindingElement
  fun createBindingElement(dotDotDotToken: DotDotDotToken?, propertyName: StringLiteral?, name: Any /* String | Identifier | ObjectBindingPattern | ArrayBindingPattern */): BindingElement
  fun createBindingElement(dotDotDotToken: DotDotDotToken?, propertyName: NumericLiteral?, name: Any /* String | Identifier | ObjectBindingPattern | ArrayBindingPattern */, initializer: Expression = definedExternally): BindingElement
  fun createBindingElement(dotDotDotToken: DotDotDotToken?, propertyName: NumericLiteral?, name: Any /* String | Identifier | ObjectBindingPattern | ArrayBindingPattern */): BindingElement
  fun createBindingElement(dotDotDotToken: DotDotDotToken?, propertyName: ComputedPropertyName?, name: Any /* String | Identifier | ObjectBindingPattern | ArrayBindingPattern */, initializer: Expression = definedExternally): BindingElement
  fun createBindingElement(dotDotDotToken: DotDotDotToken?, propertyName: ComputedPropertyName?, name: Any /* String | Identifier | ObjectBindingPattern | ArrayBindingPattern */): BindingElement
  fun createBindingElement(dotDotDotToken: DotDotDotToken?, propertyName: PrivateIdentifier?, name: Any /* String | Identifier | ObjectBindingPattern | ArrayBindingPattern */, initializer: Expression = definedExternally): BindingElement
  fun createBindingElement(dotDotDotToken: DotDotDotToken?, propertyName: PrivateIdentifier?, name: Any /* String | Identifier | ObjectBindingPattern | ArrayBindingPattern */): BindingElement
  fun updateBindingElement(node: BindingElement, dotDotDotToken: DotDotDotToken?, propertyName: Identifier?, name: Identifier, initializer: Expression?): BindingElement
  fun updateBindingElement(node: BindingElement, dotDotDotToken: DotDotDotToken?, propertyName: Identifier?, name: ObjectBindingPattern, initializer: Expression?): BindingElement
  fun updateBindingElement(node: BindingElement, dotDotDotToken: DotDotDotToken?, propertyName: Identifier?, name: ArrayBindingPattern, initializer: Expression?): BindingElement
  fun updateBindingElement(node: BindingElement, dotDotDotToken: DotDotDotToken?, propertyName: StringLiteral?, name: Identifier, initializer: Expression?): BindingElement
  fun updateBindingElement(node: BindingElement, dotDotDotToken: DotDotDotToken?, propertyName: StringLiteral?, name: ObjectBindingPattern, initializer: Expression?): BindingElement
  fun updateBindingElement(node: BindingElement, dotDotDotToken: DotDotDotToken?, propertyName: StringLiteral?, name: ArrayBindingPattern, initializer: Expression?): BindingElement
  fun updateBindingElement(node: BindingElement, dotDotDotToken: DotDotDotToken?, propertyName: NumericLiteral?, name: Identifier, initializer: Expression?): BindingElement
  fun updateBindingElement(node: BindingElement, dotDotDotToken: DotDotDotToken?, propertyName: NumericLiteral?, name: ObjectBindingPattern, initializer: Expression?): BindingElement
  fun updateBindingElement(node: BindingElement, dotDotDotToken: DotDotDotToken?, propertyName: NumericLiteral?, name: ArrayBindingPattern, initializer: Expression?): BindingElement
  fun updateBindingElement(node: BindingElement, dotDotDotToken: DotDotDotToken?, propertyName: ComputedPropertyName?, name: Identifier, initializer: Expression?): BindingElement
  fun updateBindingElement(node: BindingElement, dotDotDotToken: DotDotDotToken?, propertyName: ComputedPropertyName?, name: ObjectBindingPattern, initializer: Expression?): BindingElement
  fun updateBindingElement(node: BindingElement, dotDotDotToken: DotDotDotToken?, propertyName: ComputedPropertyName?, name: ArrayBindingPattern, initializer: Expression?): BindingElement
  fun updateBindingElement(node: BindingElement, dotDotDotToken: DotDotDotToken?, propertyName: PrivateIdentifier?, name: Identifier, initializer: Expression?): BindingElement
  fun updateBindingElement(node: BindingElement, dotDotDotToken: DotDotDotToken?, propertyName: PrivateIdentifier?, name: ObjectBindingPattern, initializer: Expression?): BindingElement
  fun updateBindingElement(node: BindingElement, dotDotDotToken: DotDotDotToken?, propertyName: PrivateIdentifier?, name: ArrayBindingPattern, initializer: Expression?): BindingElement
  fun createArrayLiteralExpression(elements: Array<Expression> = definedExternally, multiLine: Boolean = definedExternally): ArrayLiteralExpression
  fun updateArrayLiteralExpression(node: ArrayLiteralExpression, elements: Array<Expression>): ArrayLiteralExpression
  fun createObjectLiteralExpression(properties: Array<Any /* PropertyAssignment | ShorthandPropertyAssignment | SpreadAssignment | MethodDeclaration | GetAccessorDeclaration | SetAccessorDeclaration */> = definedExternally, multiLine: Boolean = definedExternally): ObjectLiteralExpression
  fun updateObjectLiteralExpression(node: ObjectLiteralExpression, properties: Array<Any /* PropertyAssignment | ShorthandPropertyAssignment | SpreadAssignment | MethodDeclaration | GetAccessorDeclaration | SetAccessorDeclaration */>): ObjectLiteralExpression
  fun createPropertyAccessExpression(expression: Expression, name: String): PropertyAccessExpression
  fun createPropertyAccessExpression(expression: Expression, name: Identifier): PropertyAccessExpression
  fun createPropertyAccessExpression(expression: Expression, name: PrivateIdentifier): PropertyAccessExpression
  fun updatePropertyAccessExpression(node: PropertyAccessExpression, expression: Expression, name: Identifier): PropertyAccessExpression
  fun updatePropertyAccessExpression(node: PropertyAccessExpression, expression: Expression, name: PrivateIdentifier): PropertyAccessExpression
  fun createPropertyAccessChain(expression: Expression, questionDotToken: QuestionDotToken?, name: String): PropertyAccessChain
  fun createPropertyAccessChain(expression: Expression, questionDotToken: QuestionDotToken?, name: Identifier): PropertyAccessChain
  fun createPropertyAccessChain(expression: Expression, questionDotToken: QuestionDotToken?, name: PrivateIdentifier): PropertyAccessChain
  fun updatePropertyAccessChain(node: PropertyAccessChain, expression: Expression, questionDotToken: QuestionDotToken?, name: Identifier): PropertyAccessChain
  fun updatePropertyAccessChain(node: PropertyAccessChain, expression: Expression, questionDotToken: QuestionDotToken?, name: PrivateIdentifier): PropertyAccessChain
  fun createElementAccessExpression(expression: Expression, index: Number): ElementAccessExpression
  fun createElementAccessExpression(expression: Expression, index: Expression): ElementAccessExpression
  fun updateElementAccessExpression(node: ElementAccessExpression, expression: Expression, argumentExpression: Expression): ElementAccessExpression
  fun createElementAccessChain(expression: Expression, questionDotToken: QuestionDotToken?, index: Number): ElementAccessChain
  fun createElementAccessChain(expression: Expression, questionDotToken: QuestionDotToken?, index: Expression): ElementAccessChain
  fun updateElementAccessChain(node: ElementAccessChain, expression: Expression, questionDotToken: QuestionDotToken?, argumentExpression: Expression): ElementAccessChain
  fun createCallExpression(expression: Expression, typeArguments: Array<TypeNode>?, argumentsArray: Array<Expression>?): CallExpression
  fun updateCallExpression(node: CallExpression, expression: Expression, typeArguments: Array<TypeNode>?, argumentsArray: Array<Expression>): CallExpression
  fun createCallChain(expression: Expression, questionDotToken: QuestionDotToken?, typeArguments: Array<TypeNode>?, argumentsArray: Array<Expression>?): CallChain
  fun updateCallChain(node: CallChain, expression: Expression, questionDotToken: QuestionDotToken?, typeArguments: Array<TypeNode>?, argumentsArray: Array<Expression>): CallChain
  fun createNewExpression(expression: Expression, typeArguments: Array<TypeNode>?, argumentsArray: Array<Expression>?): NewExpression
  fun updateNewExpression(node: NewExpression, expression: Expression, typeArguments: Array<TypeNode>?, argumentsArray: Array<Expression>?): NewExpression
  fun createTaggedTemplateExpression(tag: Expression, typeArguments: Array<TypeNode>?, template: TemplateExpression): TaggedTemplateExpression
  fun createTaggedTemplateExpression(tag: Expression, typeArguments: Array<TypeNode>?, template: NoSubstitutionTemplateLiteral): TaggedTemplateExpression
  fun updateTaggedTemplateExpression(node: TaggedTemplateExpression, tag: Expression, typeArguments: Array<TypeNode>?, template: TemplateExpression): TaggedTemplateExpression
  fun updateTaggedTemplateExpression(node: TaggedTemplateExpression, tag: Expression, typeArguments: Array<TypeNode>?, template: NoSubstitutionTemplateLiteral): TaggedTemplateExpression
  fun createTypeAssertion(type: TypeNode, expression: Expression): TypeAssertion
  fun updateTypeAssertion(node: TypeAssertion, type: TypeNode, expression: Expression): TypeAssertion
  fun createParenthesizedExpression(expression: Expression): ParenthesizedExpression
  fun updateParenthesizedExpression(node: ParenthesizedExpression, expression: Expression): ParenthesizedExpression
  fun createFunctionExpression(modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, asteriskToken: AsteriskToken?, name: String?, typeParameters: Array<TypeParameterDeclaration>?, parameters: Array<ParameterDeclaration>?, type: TypeNode?, body: Block): FunctionExpression
  fun createFunctionExpression(modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, asteriskToken: AsteriskToken?, name: Identifier?, typeParameters: Array<TypeParameterDeclaration>?, parameters: Array<ParameterDeclaration>?, type: TypeNode?, body: Block): FunctionExpression
  fun updateFunctionExpression(node: FunctionExpression, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, asteriskToken: AsteriskToken?, name: Identifier?, typeParameters: Array<TypeParameterDeclaration>?, parameters: Array<ParameterDeclaration>, type: TypeNode?, body: Block): FunctionExpression
  fun createArrowFunction(modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, typeParameters: Array<TypeParameterDeclaration>?, parameters: Array<ParameterDeclaration>, type: TypeNode?, equalsGreaterThanToken: EqualsGreaterThanToken?, body: FunctionBody): ArrowFunction
  fun createArrowFunction(modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, typeParameters: Array<TypeParameterDeclaration>?, parameters: Array<ParameterDeclaration>, type: TypeNode?, equalsGreaterThanToken: EqualsGreaterThanToken?, body: Expression): ArrowFunction
  fun updateArrowFunction(node: ArrowFunction, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, typeParameters: Array<TypeParameterDeclaration>?, parameters: Array<ParameterDeclaration>, type: TypeNode?, equalsGreaterThanToken: EqualsGreaterThanToken, body: FunctionBody): ArrowFunction
  fun updateArrowFunction(node: ArrowFunction, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, typeParameters: Array<TypeParameterDeclaration>?, parameters: Array<ParameterDeclaration>, type: TypeNode?, equalsGreaterThanToken: EqualsGreaterThanToken, body: Expression): ArrowFunction
  fun createDeleteExpression(expression: Expression): DeleteExpression
  fun updateDeleteExpression(node: DeleteExpression, expression: Expression): DeleteExpression
  fun createTypeOfExpression(expression: Expression): TypeOfExpression
  fun updateTypeOfExpression(node: TypeOfExpression, expression: Expression): TypeOfExpression
  fun createVoidExpression(expression: Expression): VoidExpression
  fun updateVoidExpression(node: VoidExpression, expression: Expression): VoidExpression
  fun createAwaitExpression(expression: Expression): AwaitExpression
  fun updateAwaitExpression(node: AwaitExpression, expression: Expression): AwaitExpression
  fun createPrefixUnaryExpression(operator: SyntaxKind = definedExternally, operand: Expression): PrefixUnaryExpression
  fun updatePrefixUnaryExpression(node: PrefixUnaryExpression, operand: Expression): PrefixUnaryExpression
  fun createPostfixUnaryExpression(operand: Expression, operator: SyntaxKind = definedExternally): PostfixUnaryExpression
  fun updatePostfixUnaryExpression(node: PostfixUnaryExpression, operand: Expression): PostfixUnaryExpression
  fun createBinaryExpression(
    left: Expression,
    operator: Any /* SyntaxKind.QuestionQuestionToken | ExponentiationOperator | SyntaxKind.AsteriskToken | SyntaxKind.SlashToken | SyntaxKind.PercentToken | SyntaxKind.PlusToken | SyntaxKind.MinusToken | SyntaxKind.LessThanLessThanToken | SyntaxKind.GreaterThanGreaterThanToken | SyntaxKind.GreaterThanGreaterThanGreaterThanToken | SyntaxKind.LessThanToken | SyntaxKind.LessThanEqualsToken | SyntaxKind.GreaterThanToken | SyntaxKind.GreaterThanEqualsToken | SyntaxKind.InstanceOfKeyword | SyntaxKind.InKeyword | SyntaxKind.EqualsEqualsToken | SyntaxKind.EqualsEqualsEqualsToken | SyntaxKind.ExclamationEqualsEqualsToken | SyntaxKind.ExclamationEqualsToken | SyntaxKind.AmpersandToken | SyntaxKind.BarToken | SyntaxKind.CaretToken | SyntaxKind.AmpersandAmpersandToken | SyntaxKind.BarBarToken | SyntaxKind.EqualsToken | SyntaxKind.PlusEqualsToken | SyntaxKind.MinusEqualsToken | SyntaxKind.AsteriskAsteriskEqualsToken | SyntaxKind.AsteriskEqualsToken | SyntaxKind.SlashEqualsToken | SyntaxKind.PercentEqualsToken | SyntaxKind.AmpersandEqualsToken | SyntaxKind.BarEqualsToken | SyntaxKind.CaretEqualsToken | SyntaxKind.LessThanLessThanEqualsToken | SyntaxKind.GreaterThanGreaterThanGreaterThanEqualsToken | SyntaxKind.GreaterThanGreaterThanEqualsToken | SyntaxKind.BarBarEqualsToken | SyntaxKind.AmpersandAmpersandEqualsToken | SyntaxKind.QuestionQuestionEqualsToken | SyntaxKind.CommaToken | BinaryOperatorToken */,
    right: Expression
  ): BinaryExpression

  fun updateBinaryExpression(
    node: BinaryExpression,
    left: Expression,
    operator: Any /* SyntaxKind.QuestionQuestionToken | ExponentiationOperator | SyntaxKind.AsteriskToken | SyntaxKind.SlashToken | SyntaxKind.PercentToken | SyntaxKind.PlusToken | SyntaxKind.MinusToken | SyntaxKind.LessThanLessThanToken | SyntaxKind.GreaterThanGreaterThanToken | SyntaxKind.GreaterThanGreaterThanGreaterThanToken | SyntaxKind.LessThanToken | SyntaxKind.LessThanEqualsToken | SyntaxKind.GreaterThanToken | SyntaxKind.GreaterThanEqualsToken | SyntaxKind.InstanceOfKeyword | SyntaxKind.InKeyword | SyntaxKind.EqualsEqualsToken | SyntaxKind.EqualsEqualsEqualsToken | SyntaxKind.ExclamationEqualsEqualsToken | SyntaxKind.ExclamationEqualsToken | SyntaxKind.AmpersandToken | SyntaxKind.BarToken | SyntaxKind.CaretToken | SyntaxKind.AmpersandAmpersandToken | SyntaxKind.BarBarToken | SyntaxKind.EqualsToken | SyntaxKind.PlusEqualsToken | SyntaxKind.MinusEqualsToken | SyntaxKind.AsteriskAsteriskEqualsToken | SyntaxKind.AsteriskEqualsToken | SyntaxKind.SlashEqualsToken | SyntaxKind.PercentEqualsToken | SyntaxKind.AmpersandEqualsToken | SyntaxKind.BarEqualsToken | SyntaxKind.CaretEqualsToken | SyntaxKind.LessThanLessThanEqualsToken | SyntaxKind.GreaterThanGreaterThanGreaterThanEqualsToken | SyntaxKind.GreaterThanGreaterThanEqualsToken | SyntaxKind.BarBarEqualsToken | SyntaxKind.AmpersandAmpersandEqualsToken | SyntaxKind.QuestionQuestionEqualsToken | SyntaxKind.CommaToken | BinaryOperatorToken */,
    right: Expression
  ): BinaryExpression

  fun createConditionalExpression(condition: Expression, questionToken: QuestionToken?, whenTrue: Expression, colonToken: ColonToken?, whenFalse: Expression): ConditionalExpression
  fun updateConditionalExpression(node: ConditionalExpression, condition: Expression, questionToken: QuestionToken, whenTrue: Expression, colonToken: ColonToken, whenFalse: Expression): ConditionalExpression
  fun createTemplateExpression(head: TemplateHead, templateSpans: Array<TemplateSpan>): TemplateExpression
  fun updateTemplateExpression(node: TemplateExpression, head: TemplateHead, templateSpans: Array<TemplateSpan>): TemplateExpression
  fun createTemplateHead(text: String, rawText: String = definedExternally, templateFlags: TokenFlags = definedExternally): TemplateHead
  fun createTemplateHead(text: String): TemplateHead
  fun createTemplateHead(text: String, rawText: String = definedExternally): TemplateHead
  fun createTemplateHead(text: String?, rawText: String, templateFlags: TokenFlags = definedExternally): TemplateHead
  fun createTemplateHead(text: String?, rawText: String): TemplateHead
  fun createTemplateMiddle(text: String, rawText: String = definedExternally, templateFlags: TokenFlags = definedExternally): TemplateMiddle
  fun createTemplateMiddle(text: String): TemplateMiddle
  fun createTemplateMiddle(text: String, rawText: String = definedExternally): TemplateMiddle
  fun createTemplateMiddle(text: String?, rawText: String, templateFlags: TokenFlags = definedExternally): TemplateMiddle
  fun createTemplateMiddle(text: String?, rawText: String): TemplateMiddle
  fun createTemplateTail(text: String, rawText: String = definedExternally, templateFlags: TokenFlags = definedExternally): TemplateTail
  fun createTemplateTail(text: String): TemplateTail
  fun createTemplateTail(text: String, rawText: String = definedExternally): TemplateTail
  fun createTemplateTail(text: String?, rawText: String, templateFlags: TokenFlags = definedExternally): TemplateTail
  fun createTemplateTail(text: String?, rawText: String): TemplateTail
  fun createNoSubstitutionTemplateLiteral(text: String, rawText: String = definedExternally): NoSubstitutionTemplateLiteral
  fun createNoSubstitutionTemplateLiteral(text: String): NoSubstitutionTemplateLiteral
  fun createNoSubstitutionTemplateLiteral(text: String?, rawText: String): NoSubstitutionTemplateLiteral
  fun createYieldExpression(asteriskToken: AsteriskToken, expression: Expression): YieldExpression
  fun createYieldExpression(asteriskToken: Nothing?, expression: Expression?): YieldExpression
  fun updateYieldExpression(node: YieldExpression, asteriskToken: AsteriskToken?, expression: Expression?): YieldExpression
  fun createSpreadElement(expression: Expression): SpreadElement
  fun updateSpreadElement(node: SpreadElement, expression: Expression): SpreadElement
  fun createClassExpression(decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: String?, typeParameters: Array<TypeParameterDeclaration>?, heritageClauses: Array<HeritageClause>?, members: Array<ClassElement>): ClassExpression
  fun createClassExpression(decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: Identifier?, typeParameters: Array<TypeParameterDeclaration>?, heritageClauses: Array<HeritageClause>?, members: Array<ClassElement>): ClassExpression
  fun updateClassExpression(node: ClassExpression, decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: Identifier?, typeParameters: Array<TypeParameterDeclaration>?, heritageClauses: Array<HeritageClause>?, members: Array<ClassElement>): ClassExpression
  fun createOmittedExpression(): OmittedExpression
  fun createExpressionWithTypeArguments(expression: Expression, typeArguments: Array<TypeNode>?): ExpressionWithTypeArguments
  fun updateExpressionWithTypeArguments(node: ExpressionWithTypeArguments, expression: Expression, typeArguments: Array<TypeNode>?): ExpressionWithTypeArguments
  fun createAsExpression(expression: Expression, type: TypeNode): AsExpression
  fun updateAsExpression(node: AsExpression, expression: Expression, type: TypeNode): AsExpression
  fun createNonNullExpression(expression: Expression): NonNullExpression
  fun updateNonNullExpression(node: NonNullExpression, expression: Expression): NonNullExpression
  fun createNonNullChain(expression: Expression): NonNullChain
  fun updateNonNullChain(node: NonNullChain, expression: Expression): NonNullChain
  fun createMetaProperty(keywordToken: SyntaxKind = definedExternally, name: Identifier): MetaProperty
  fun updateMetaProperty(node: MetaProperty, name: Identifier): MetaProperty
  fun createTemplateSpan(expression: Expression, literal: TemplateMiddle): TemplateSpan
  fun createTemplateSpan(expression: Expression, literal: TemplateTail): TemplateSpan
  fun updateTemplateSpan(node: TemplateSpan, expression: Expression, literal: TemplateMiddle): TemplateSpan
  fun updateTemplateSpan(node: TemplateSpan, expression: Expression, literal: TemplateTail): TemplateSpan
  fun createSemicolonClassElement(): SemicolonClassElement
  fun createBlock(statements: Array<Statement>, multiLine: Boolean = definedExternally): Block
  fun updateBlock(node: Block, statements: Array<Statement>): Block
  fun createVariableStatement(modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, declarationList: VariableDeclarationList): VariableStatement
  fun createVariableStatement(modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, declarationList: Array<VariableDeclaration>): VariableStatement
  fun updateVariableStatement(node: VariableStatement, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, declarationList: VariableDeclarationList): VariableStatement
  fun createEmptyStatement(): EmptyStatement
  fun createExpressionStatement(expression: Expression): ExpressionStatement
  fun updateExpressionStatement(node: ExpressionStatement, expression: Expression): ExpressionStatement
  fun createIfStatement(expression: Expression, thenStatement: Statement, elseStatement: Statement = definedExternally): IfStatement
  fun updateIfStatement(node: IfStatement, expression: Expression, thenStatement: Statement, elseStatement: Statement?): IfStatement
  fun createDoStatement(statement: Statement, expression: Expression): DoStatement
  fun updateDoStatement(node: DoStatement, statement: Statement, expression: Expression): DoStatement
  fun createWhileStatement(expression: Expression, statement: Statement): WhileStatement
  fun updateWhileStatement(node: WhileStatement, expression: Expression, statement: Statement): WhileStatement
  fun createForStatement(initializer: VariableDeclarationList?, condition: Expression?, incrementor: Expression?, statement: Statement): ForStatement
  fun createForStatement(initializer: Expression?, condition: Expression?, incrementor: Expression?, statement: Statement): ForStatement
  fun updateForStatement(node: ForStatement, initializer: VariableDeclarationList?, condition: Expression?, incrementor: Expression?, statement: Statement): ForStatement
  fun updateForStatement(node: ForStatement, initializer: Expression?, condition: Expression?, incrementor: Expression?, statement: Statement): ForStatement
  fun createForInStatement(initializer: VariableDeclarationList, expression: Expression, statement: Statement): ForInStatement
  fun createForInStatement(initializer: Expression, expression: Expression, statement: Statement): ForInStatement
  fun updateForInStatement(node: ForInStatement, initializer: VariableDeclarationList, expression: Expression, statement: Statement): ForInStatement
  fun updateForInStatement(node: ForInStatement, initializer: Expression, expression: Expression, statement: Statement): ForInStatement
  fun createForOfStatement(awaitModifier: AwaitKeyword?, initializer: VariableDeclarationList, expression: Expression, statement: Statement): ForOfStatement
  fun createForOfStatement(awaitModifier: AwaitKeyword?, initializer: Expression, expression: Expression, statement: Statement): ForOfStatement
  fun updateForOfStatement(node: ForOfStatement, awaitModifier: AwaitKeyword?, initializer: VariableDeclarationList, expression: Expression, statement: Statement): ForOfStatement
  fun updateForOfStatement(node: ForOfStatement, awaitModifier: AwaitKeyword?, initializer: Expression, expression: Expression, statement: Statement): ForOfStatement
  fun createContinueStatement(label: String = definedExternally): ContinueStatement
  fun createContinueStatement(): ContinueStatement
  fun createContinueStatement(label: Identifier = definedExternally): ContinueStatement
  fun updateContinueStatement(node: ContinueStatement, label: Identifier?): ContinueStatement
  fun createBreakStatement(label: String = definedExternally): BreakStatement
  fun createBreakStatement(): BreakStatement
  fun createBreakStatement(label: Identifier = definedExternally): BreakStatement
  fun updateBreakStatement(node: BreakStatement, label: Identifier?): BreakStatement
  fun createReturnStatement(expression: Expression = definedExternally): ReturnStatement
  fun updateReturnStatement(node: ReturnStatement, expression: Expression?): ReturnStatement
  fun createWithStatement(expression: Expression, statement: Statement): WithStatement
  fun updateWithStatement(node: WithStatement, expression: Expression, statement: Statement): WithStatement
  fun createSwitchStatement(expression: Expression, caseBlock: CaseBlock): SwitchStatement
  fun updateSwitchStatement(node: SwitchStatement, expression: Expression, caseBlock: CaseBlock): SwitchStatement
  fun createLabeledStatement(label: String, statement: Statement): LabeledStatement
  fun createLabeledStatement(label: Identifier, statement: Statement): LabeledStatement
  fun updateLabeledStatement(node: LabeledStatement, label: Identifier, statement: Statement): LabeledStatement
  fun createThrowStatement(expression: Expression): ThrowStatement
  fun updateThrowStatement(node: ThrowStatement, expression: Expression): ThrowStatement
  fun createTryStatement(tryBlock: Block, catchClause: CatchClause?, finallyBlock: Block?): TryStatement
  fun updateTryStatement(node: TryStatement, tryBlock: Block, catchClause: CatchClause?, finallyBlock: Block?): TryStatement
  fun createDebuggerStatement(): DebuggerStatement
  fun createVariableDeclaration(name: String, exclamationToken: ExclamationToken = definedExternally, type: TypeNode = definedExternally, initializer: Expression = definedExternally): VariableDeclaration
  fun createVariableDeclaration(name: String): VariableDeclaration
  fun createVariableDeclaration(name: String, exclamationToken: ExclamationToken = definedExternally): VariableDeclaration
  fun createVariableDeclaration(name: String, exclamationToken: ExclamationToken = definedExternally, type: TypeNode = definedExternally): VariableDeclaration
  fun createVariableDeclaration(name: Identifier, exclamationToken: ExclamationToken = definedExternally, type: TypeNode = definedExternally, initializer: Expression = definedExternally): VariableDeclaration
  fun createVariableDeclaration(name: Identifier): VariableDeclaration
  fun createVariableDeclaration(name: Identifier, exclamationToken: ExclamationToken = definedExternally): VariableDeclaration
  fun createVariableDeclaration(name: Identifier, exclamationToken: ExclamationToken = definedExternally, type: TypeNode = definedExternally): VariableDeclaration
  fun createVariableDeclaration(name: ObjectBindingPattern, exclamationToken: ExclamationToken = definedExternally, type: TypeNode = definedExternally, initializer: Expression = definedExternally): VariableDeclaration
  fun createVariableDeclaration(name: ObjectBindingPattern): VariableDeclaration
  fun createVariableDeclaration(name: ObjectBindingPattern, exclamationToken: ExclamationToken = definedExternally): VariableDeclaration
  fun createVariableDeclaration(name: ObjectBindingPattern, exclamationToken: ExclamationToken = definedExternally, type: TypeNode = definedExternally): VariableDeclaration
  fun createVariableDeclaration(name: ArrayBindingPattern, exclamationToken: ExclamationToken = definedExternally, type: TypeNode = definedExternally, initializer: Expression = definedExternally): VariableDeclaration
  fun createVariableDeclaration(name: ArrayBindingPattern): VariableDeclaration
  fun createVariableDeclaration(name: ArrayBindingPattern, exclamationToken: ExclamationToken = definedExternally): VariableDeclaration
  fun createVariableDeclaration(name: ArrayBindingPattern, exclamationToken: ExclamationToken = definedExternally, type: TypeNode = definedExternally): VariableDeclaration
  fun updateVariableDeclaration(node: VariableDeclaration, name: Identifier, exclamationToken: ExclamationToken?, type: TypeNode?, initializer: Expression?): VariableDeclaration
  fun updateVariableDeclaration(node: VariableDeclaration, name: ObjectBindingPattern, exclamationToken: ExclamationToken?, type: TypeNode?, initializer: Expression?): VariableDeclaration
  fun updateVariableDeclaration(node: VariableDeclaration, name: ArrayBindingPattern, exclamationToken: ExclamationToken?, type: TypeNode?, initializer: Expression?): VariableDeclaration
  fun createVariableDeclarationList(declarations: Array<VariableDeclaration>, flags: NodeFlags = definedExternally): VariableDeclarationList
  fun updateVariableDeclarationList(node: VariableDeclarationList, declarations: Array<VariableDeclaration>): VariableDeclarationList
  fun createFunctionDeclaration(decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, asteriskToken: AsteriskToken?, name: String?, typeParameters: Array<TypeParameterDeclaration>?, parameters: Array<ParameterDeclaration>, type: TypeNode?, body: Block?): FunctionDeclaration
  fun createFunctionDeclaration(decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, asteriskToken: AsteriskToken?, name: Identifier?, typeParameters: Array<TypeParameterDeclaration>?, parameters: Array<ParameterDeclaration>, type: TypeNode?, body: Block?): FunctionDeclaration
  fun updateFunctionDeclaration(node: FunctionDeclaration, decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, asteriskToken: AsteriskToken?, name: Identifier?, typeParameters: Array<TypeParameterDeclaration>?, parameters: Array<ParameterDeclaration>, type: TypeNode?, body: Block?): FunctionDeclaration
  fun createClassDeclaration(decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: String?, typeParameters: Array<TypeParameterDeclaration>?, heritageClauses: Array<HeritageClause>?, members: Array<ClassElement>): ClassDeclaration
  fun createClassDeclaration(decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: Identifier?, typeParameters: Array<TypeParameterDeclaration>?, heritageClauses: Array<HeritageClause>?, members: Array<ClassElement>): ClassDeclaration
  fun updateClassDeclaration(node: ClassDeclaration, decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: Identifier?, typeParameters: Array<TypeParameterDeclaration>?, heritageClauses: Array<HeritageClause>?, members: Array<ClassElement>): ClassDeclaration
  fun createInterfaceDeclaration(decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: String, typeParameters: Array<TypeParameterDeclaration>?, heritageClauses: Array<HeritageClause>?, members: Array<TypeElement>): InterfaceDeclaration
  fun createInterfaceDeclaration(decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: Identifier, typeParameters: Array<TypeParameterDeclaration>?, heritageClauses: Array<HeritageClause>?, members: Array<TypeElement>): InterfaceDeclaration
  fun updateInterfaceDeclaration(node: InterfaceDeclaration, decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: Identifier, typeParameters: Array<TypeParameterDeclaration>?, heritageClauses: Array<HeritageClause>?, members: Array<TypeElement>): InterfaceDeclaration
  fun createTypeAliasDeclaration(decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: String, typeParameters: Array<TypeParameterDeclaration>?, type: TypeNode): TypeAliasDeclaration
  fun createTypeAliasDeclaration(decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: Identifier, typeParameters: Array<TypeParameterDeclaration>?, type: TypeNode): TypeAliasDeclaration
  fun updateTypeAliasDeclaration(node: TypeAliasDeclaration, decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: Identifier, typeParameters: Array<TypeParameterDeclaration>?, type: TypeNode): TypeAliasDeclaration
  fun createEnumDeclaration(decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: String, members: Array<EnumMember>): EnumDeclaration
  fun createEnumDeclaration(decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: Identifier, members: Array<EnumMember>): EnumDeclaration
  fun updateEnumDeclaration(node: EnumDeclaration, decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: Identifier, members: Array<EnumMember>): EnumDeclaration
  fun createModuleDeclaration(decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: Identifier, body: ModuleBlock?, flags: NodeFlags = definedExternally): ModuleDeclaration
  fun createModuleDeclaration(decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: Identifier, body: ModuleBlock?): ModuleDeclaration
  fun createModuleDeclaration(decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: Identifier, body: NamespaceDeclaration?, flags: NodeFlags = definedExternally): ModuleDeclaration
  fun createModuleDeclaration(decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: Identifier, body: NamespaceDeclaration?): ModuleDeclaration
  fun createModuleDeclaration(decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: Identifier, body: Identifier?, flags: NodeFlags = definedExternally): ModuleDeclaration
  fun createModuleDeclaration(decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: Identifier, body: Identifier?): ModuleDeclaration
  fun createModuleDeclaration(decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: Identifier, body: JSDocNamespaceDeclaration?, flags: NodeFlags = definedExternally): ModuleDeclaration
  fun createModuleDeclaration(decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: Identifier, body: JSDocNamespaceDeclaration?): ModuleDeclaration
  fun createModuleDeclaration(decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: StringLiteral, body: ModuleBlock?, flags: NodeFlags = definedExternally): ModuleDeclaration
  fun createModuleDeclaration(decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: StringLiteral, body: ModuleBlock?): ModuleDeclaration
  fun createModuleDeclaration(decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: StringLiteral, body: NamespaceDeclaration?, flags: NodeFlags = definedExternally): ModuleDeclaration
  fun createModuleDeclaration(decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: StringLiteral, body: NamespaceDeclaration?): ModuleDeclaration
  fun createModuleDeclaration(decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: StringLiteral, body: Identifier?, flags: NodeFlags = definedExternally): ModuleDeclaration
  fun createModuleDeclaration(decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: StringLiteral, body: Identifier?): ModuleDeclaration
  fun createModuleDeclaration(decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: StringLiteral, body: JSDocNamespaceDeclaration?, flags: NodeFlags = definedExternally): ModuleDeclaration
  fun createModuleDeclaration(decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: StringLiteral, body: JSDocNamespaceDeclaration?): ModuleDeclaration
  fun updateModuleDeclaration(node: ModuleDeclaration, decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: Identifier, body: ModuleBlock?): ModuleDeclaration
  fun updateModuleDeclaration(node: ModuleDeclaration, decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: Identifier, body: NamespaceDeclaration?): ModuleDeclaration
  fun updateModuleDeclaration(node: ModuleDeclaration, decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: Identifier, body: Identifier?): ModuleDeclaration
  fun updateModuleDeclaration(node: ModuleDeclaration, decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: Identifier, body: JSDocNamespaceDeclaration?): ModuleDeclaration
  fun updateModuleDeclaration(node: ModuleDeclaration, decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: StringLiteral, body: ModuleBlock?): ModuleDeclaration
  fun updateModuleDeclaration(node: ModuleDeclaration, decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: StringLiteral, body: NamespaceDeclaration?): ModuleDeclaration
  fun updateModuleDeclaration(node: ModuleDeclaration, decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: StringLiteral, body: Identifier?): ModuleDeclaration
  fun updateModuleDeclaration(node: ModuleDeclaration, decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: StringLiteral, body: JSDocNamespaceDeclaration?): ModuleDeclaration
  fun createModuleBlock(statements: Array<Statement>): ModuleBlock
  fun updateModuleBlock(node: ModuleBlock, statements: Array<Statement>): ModuleBlock
  fun createCaseBlock(clauses: Array<Any /* CaseClause | DefaultClause */>): CaseBlock
  fun updateCaseBlock(node: CaseBlock, clauses: Array<Any /* CaseClause | DefaultClause */>): CaseBlock
  fun createNamespaceExportDeclaration(name: String): NamespaceExportDeclaration
  fun createNamespaceExportDeclaration(name: Identifier): NamespaceExportDeclaration
  fun updateNamespaceExportDeclaration(node: NamespaceExportDeclaration, name: Identifier): NamespaceExportDeclaration
  fun createImportEqualsDeclaration(decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, isTypeOnly: Boolean, name: String, moduleReference: Identifier): ImportEqualsDeclaration
  fun createImportEqualsDeclaration(decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, isTypeOnly: Boolean, name: String, moduleReference: QualifiedName): ImportEqualsDeclaration
  fun createImportEqualsDeclaration(decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, isTypeOnly: Boolean, name: String, moduleReference: ExternalModuleReference): ImportEqualsDeclaration
  fun createImportEqualsDeclaration(decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, isTypeOnly: Boolean, name: Identifier, moduleReference: Identifier): ImportEqualsDeclaration
  fun createImportEqualsDeclaration(decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, isTypeOnly: Boolean, name: Identifier, moduleReference: QualifiedName): ImportEqualsDeclaration
  fun createImportEqualsDeclaration(decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, isTypeOnly: Boolean, name: Identifier, moduleReference: ExternalModuleReference): ImportEqualsDeclaration
  fun updateImportEqualsDeclaration(node: ImportEqualsDeclaration, decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, isTypeOnly: Boolean, name: Identifier, moduleReference: Identifier): ImportEqualsDeclaration
  fun updateImportEqualsDeclaration(node: ImportEqualsDeclaration, decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, isTypeOnly: Boolean, name: Identifier, moduleReference: QualifiedName): ImportEqualsDeclaration
  fun updateImportEqualsDeclaration(node: ImportEqualsDeclaration, decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, isTypeOnly: Boolean, name: Identifier, moduleReference: ExternalModuleReference): ImportEqualsDeclaration
  fun createImportDeclaration(decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, importClause: ImportClause?, moduleSpecifier: Expression): ImportDeclaration
  fun updateImportDeclaration(node: ImportDeclaration, decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, importClause: ImportClause?, moduleSpecifier: Expression): ImportDeclaration
  fun createImportClause(isTypeOnly: Boolean, name: Identifier?, namedBindings: NamespaceImport?): ImportClause
  fun createImportClause(isTypeOnly: Boolean, name: Identifier?, namedBindings: NamedImports?): ImportClause
  fun updateImportClause(node: ImportClause, isTypeOnly: Boolean, name: Identifier?, namedBindings: NamespaceImport?): ImportClause
  fun updateImportClause(node: ImportClause, isTypeOnly: Boolean, name: Identifier?, namedBindings: NamedImports?): ImportClause
  fun createNamespaceImport(name: Identifier): NamespaceImport
  fun updateNamespaceImport(node: NamespaceImport, name: Identifier): NamespaceImport
  fun createNamespaceExport(name: Identifier): NamespaceExport
  fun updateNamespaceExport(node: NamespaceExport, name: Identifier): NamespaceExport
  fun createNamedImports(elements: Array<ImportSpecifier>): NamedImports
  fun updateNamedImports(node: NamedImports, elements: Array<ImportSpecifier>): NamedImports
  fun createImportSpecifier(propertyName: Identifier?, name: Identifier): ImportSpecifier
  fun updateImportSpecifier(node: ImportSpecifier, propertyName: Identifier?, name: Identifier): ImportSpecifier
  fun createExportAssignment(decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, isExportEquals: Boolean?, expression: Expression): ExportAssignment
  fun updateExportAssignment(node: ExportAssignment, decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, expression: Expression): ExportAssignment
  fun createExportDeclaration(decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, isTypeOnly: Boolean, exportClause: NamespaceExport?, moduleSpecifier: Expression = definedExternally): ExportDeclaration
  fun createExportDeclaration(decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, isTypeOnly: Boolean, exportClause: NamespaceExport?): ExportDeclaration
  fun createExportDeclaration(decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, isTypeOnly: Boolean, exportClause: NamedExports?, moduleSpecifier: Expression = definedExternally): ExportDeclaration
  fun createExportDeclaration(decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, isTypeOnly: Boolean, exportClause: NamedExports?): ExportDeclaration
  fun updateExportDeclaration(node: ExportDeclaration, decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, isTypeOnly: Boolean, exportClause: NamespaceExport?, moduleSpecifier: Expression?): ExportDeclaration
  fun updateExportDeclaration(node: ExportDeclaration, decorators: Array<Decorator>?, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, isTypeOnly: Boolean, exportClause: NamedExports?, moduleSpecifier: Expression?): ExportDeclaration
  fun createNamedExports(elements: Array<ExportSpecifier>): NamedExports
  fun updateNamedExports(node: NamedExports, elements: Array<ExportSpecifier>): NamedExports
  fun createExportSpecifier(propertyName: String?, name: String): ExportSpecifier
  fun createExportSpecifier(propertyName: String?, name: Identifier): ExportSpecifier
  fun createExportSpecifier(propertyName: Identifier?, name: String): ExportSpecifier
  fun createExportSpecifier(propertyName: Identifier?, name: Identifier): ExportSpecifier
  fun updateExportSpecifier(node: ExportSpecifier, propertyName: Identifier?, name: Identifier): ExportSpecifier
  fun createExternalModuleReference(expression: Expression): ExternalModuleReference
  fun updateExternalModuleReference(node: ExternalModuleReference, expression: Expression): ExternalModuleReference
  fun createJSDocAllType(): JSDocAllType
  fun createJSDocUnknownType(): JSDocUnknownType
  fun createJSDocNonNullableType(type: TypeNode): JSDocNonNullableType
  fun updateJSDocNonNullableType(node: JSDocNonNullableType, type: TypeNode): JSDocNonNullableType
  fun createJSDocNullableType(type: TypeNode): JSDocNullableType
  fun updateJSDocNullableType(node: JSDocNullableType, type: TypeNode): JSDocNullableType
  fun createJSDocOptionalType(type: TypeNode): JSDocOptionalType
  fun updateJSDocOptionalType(node: JSDocOptionalType, type: TypeNode): JSDocOptionalType
  fun createJSDocFunctionType(parameters: Array<ParameterDeclaration>, type: TypeNode?): JSDocFunctionType
  fun updateJSDocFunctionType(node: JSDocFunctionType, parameters: Array<ParameterDeclaration>, type: TypeNode?): JSDocFunctionType
  fun createJSDocVariadicType(type: TypeNode): JSDocVariadicType
  fun updateJSDocVariadicType(node: JSDocVariadicType, type: TypeNode): JSDocVariadicType
  fun createJSDocNamepathType(type: TypeNode): JSDocNamepathType
  fun updateJSDocNamepathType(node: JSDocNamepathType, type: TypeNode): JSDocNamepathType
  fun createJSDocTypeExpression(type: TypeNode): JSDocTypeExpression
  fun updateJSDocTypeExpression(node: JSDocTypeExpression, type: TypeNode): JSDocTypeExpression
  fun createJSDocNameReference(name: Identifier): JSDocNameReference
  fun createJSDocNameReference(name: QualifiedName): JSDocNameReference
  fun updateJSDocNameReference(node: JSDocNameReference, name: Identifier): JSDocNameReference
  fun updateJSDocNameReference(node: JSDocNameReference, name: QualifiedName): JSDocNameReference
  fun createJSDocLink(name: Identifier?, text: String): JSDocLink
  fun createJSDocLink(name: QualifiedName?, text: String): JSDocLink
  fun updateJSDocLink(node: JSDocLink, name: Identifier?, text: String): JSDocLink
  fun updateJSDocLink(node: JSDocLink, name: QualifiedName?, text: String): JSDocLink
  fun createJSDocTypeLiteral(jsDocPropertyTags: Array<JSDocPropertyLikeTag> = definedExternally, isArrayType: Boolean = definedExternally): JSDocTypeLiteral
  fun updateJSDocTypeLiteral(node: JSDocTypeLiteral, jsDocPropertyTags: Array<JSDocPropertyLikeTag>?, isArrayType: Boolean?): JSDocTypeLiteral
  fun createJSDocSignature(typeParameters: Array<JSDocTemplateTag>?, parameters: Array<JSDocParameterTag>, type: JSDocReturnTag = definedExternally): JSDocSignature
  fun updateJSDocSignature(node: JSDocSignature, typeParameters: Array<JSDocTemplateTag>?, parameters: Array<JSDocParameterTag>, type: JSDocReturnTag?): JSDocSignature
  fun createJSDocTemplateTag(tagName: Identifier?, constraint: JSDocTypeExpression?, typeParameters: Array<TypeParameterDeclaration>, comment: String = definedExternally): JSDocTemplateTag
  fun createJSDocTemplateTag(tagName: Identifier?, constraint: JSDocTypeExpression?, typeParameters: Array<TypeParameterDeclaration>): JSDocTemplateTag
  fun createJSDocTemplateTag(tagName: Identifier?, constraint: JSDocTypeExpression?, typeParameters: Array<TypeParameterDeclaration>, comment: NodeArray<Any /* JSDocText | JSDocLink */> = definedExternally): JSDocTemplateTag
  fun updateJSDocTemplateTag(node: JSDocTemplateTag, tagName: Identifier?, constraint: JSDocTypeExpression?, typeParameters: Array<TypeParameterDeclaration>, comment: String?): JSDocTemplateTag
  fun updateJSDocTemplateTag(node: JSDocTemplateTag, tagName: Identifier?, constraint: JSDocTypeExpression?, typeParameters: Array<TypeParameterDeclaration>, comment: NodeArray<Any /* JSDocText | JSDocLink */>?): JSDocTemplateTag
  fun createJSDocTypedefTag(tagName: Identifier?, typeExpression: JSDocTypeExpression = definedExternally, fullName: Identifier = definedExternally, comment: String = definedExternally): JSDocTypedefTag
  fun createJSDocTypedefTag(tagName: Identifier?): JSDocTypedefTag
  fun createJSDocTypedefTag(tagName: Identifier?, typeExpression: JSDocTypeExpression = definedExternally): JSDocTypedefTag
  fun createJSDocTypedefTag(tagName: Identifier?, typeExpression: JSDocTypeExpression = definedExternally, fullName: Identifier = definedExternally): JSDocTypedefTag
  fun createJSDocTypedefTag(tagName: Identifier?, typeExpression: JSDocTypeExpression = definedExternally, fullName: Identifier = definedExternally, comment: NodeArray<Any /* JSDocText | JSDocLink */> = definedExternally): JSDocTypedefTag
  fun createJSDocTypedefTag(tagName: Identifier?, typeExpression: JSDocTypeExpression = definedExternally, fullName: JSDocNamespaceDeclaration = definedExternally, comment: String = definedExternally): JSDocTypedefTag
  fun createJSDocTypedefTag(tagName: Identifier?, typeExpression: JSDocTypeExpression = definedExternally, fullName: JSDocNamespaceDeclaration = definedExternally): JSDocTypedefTag
  fun createJSDocTypedefTag(tagName: Identifier?, typeExpression: JSDocTypeExpression = definedExternally, fullName: JSDocNamespaceDeclaration = definedExternally, comment: NodeArray<Any /* JSDocText | JSDocLink */> = definedExternally): JSDocTypedefTag
  fun createJSDocTypedefTag(tagName: Identifier?, typeExpression: JSDocTypeLiteral = definedExternally, fullName: Identifier = definedExternally, comment: String = definedExternally): JSDocTypedefTag
  fun createJSDocTypedefTag(tagName: Identifier?, typeExpression: JSDocTypeLiteral = definedExternally): JSDocTypedefTag
  fun createJSDocTypedefTag(tagName: Identifier?, typeExpression: JSDocTypeLiteral = definedExternally, fullName: Identifier = definedExternally): JSDocTypedefTag
  fun createJSDocTypedefTag(tagName: Identifier?, typeExpression: JSDocTypeLiteral = definedExternally, fullName: Identifier = definedExternally, comment: NodeArray<Any /* JSDocText | JSDocLink */> = definedExternally): JSDocTypedefTag
  fun createJSDocTypedefTag(tagName: Identifier?, typeExpression: JSDocTypeLiteral = definedExternally, fullName: JSDocNamespaceDeclaration = definedExternally, comment: String = definedExternally): JSDocTypedefTag
  fun createJSDocTypedefTag(tagName: Identifier?, typeExpression: JSDocTypeLiteral = definedExternally, fullName: JSDocNamespaceDeclaration = definedExternally): JSDocTypedefTag
  fun createJSDocTypedefTag(tagName: Identifier?, typeExpression: JSDocTypeLiteral = definedExternally, fullName: JSDocNamespaceDeclaration = definedExternally, comment: NodeArray<Any /* JSDocText | JSDocLink */> = definedExternally): JSDocTypedefTag
  fun updateJSDocTypedefTag(node: JSDocTypedefTag, tagName: Identifier?, typeExpression: JSDocTypeExpression?, fullName: Identifier?, comment: String?): JSDocTypedefTag
  fun updateJSDocTypedefTag(node: JSDocTypedefTag, tagName: Identifier?, typeExpression: JSDocTypeExpression?, fullName: Identifier?, comment: NodeArray<Any /* JSDocText | JSDocLink */>?): JSDocTypedefTag
  fun updateJSDocTypedefTag(node: JSDocTypedefTag, tagName: Identifier?, typeExpression: JSDocTypeExpression?, fullName: JSDocNamespaceDeclaration?, comment: String?): JSDocTypedefTag
  fun updateJSDocTypedefTag(node: JSDocTypedefTag, tagName: Identifier?, typeExpression: JSDocTypeExpression?, fullName: JSDocNamespaceDeclaration?, comment: NodeArray<Any /* JSDocText | JSDocLink */>?): JSDocTypedefTag
  fun updateJSDocTypedefTag(node: JSDocTypedefTag, tagName: Identifier?, typeExpression: JSDocTypeLiteral?, fullName: Identifier?, comment: String?): JSDocTypedefTag
  fun updateJSDocTypedefTag(node: JSDocTypedefTag, tagName: Identifier?, typeExpression: JSDocTypeLiteral?, fullName: Identifier?, comment: NodeArray<Any /* JSDocText | JSDocLink */>?): JSDocTypedefTag
  fun updateJSDocTypedefTag(node: JSDocTypedefTag, tagName: Identifier?, typeExpression: JSDocTypeLiteral?, fullName: JSDocNamespaceDeclaration?, comment: String?): JSDocTypedefTag
  fun updateJSDocTypedefTag(node: JSDocTypedefTag, tagName: Identifier?, typeExpression: JSDocTypeLiteral?, fullName: JSDocNamespaceDeclaration?, comment: NodeArray<Any /* JSDocText | JSDocLink */>?): JSDocTypedefTag
  fun createJSDocParameterTag(tagName: Identifier?, name: Identifier, isBracketed: Boolean, typeExpression: JSDocTypeExpression = definedExternally, isNameFirst: Boolean = definedExternally, comment: String = definedExternally): JSDocParameterTag
  fun createJSDocParameterTag(tagName: Identifier?, name: Identifier, isBracketed: Boolean): JSDocParameterTag
  fun createJSDocParameterTag(tagName: Identifier?, name: Identifier, isBracketed: Boolean, typeExpression: JSDocTypeExpression = definedExternally): JSDocParameterTag
  fun createJSDocParameterTag(tagName: Identifier?, name: Identifier, isBracketed: Boolean, typeExpression: JSDocTypeExpression = definedExternally, isNameFirst: Boolean = definedExternally): JSDocParameterTag
  fun createJSDocParameterTag(tagName: Identifier?, name: Identifier, isBracketed: Boolean, typeExpression: JSDocTypeExpression = definedExternally, isNameFirst: Boolean = definedExternally, comment: NodeArray<Any /* JSDocText | JSDocLink */> = definedExternally): JSDocParameterTag
  fun createJSDocParameterTag(tagName: Identifier?, name: QualifiedName, isBracketed: Boolean, typeExpression: JSDocTypeExpression = definedExternally, isNameFirst: Boolean = definedExternally, comment: String = definedExternally): JSDocParameterTag
  fun createJSDocParameterTag(tagName: Identifier?, name: QualifiedName, isBracketed: Boolean): JSDocParameterTag
  fun createJSDocParameterTag(tagName: Identifier?, name: QualifiedName, isBracketed: Boolean, typeExpression: JSDocTypeExpression = definedExternally): JSDocParameterTag
  fun createJSDocParameterTag(tagName: Identifier?, name: QualifiedName, isBracketed: Boolean, typeExpression: JSDocTypeExpression = definedExternally, isNameFirst: Boolean = definedExternally): JSDocParameterTag
  fun createJSDocParameterTag(tagName: Identifier?, name: QualifiedName, isBracketed: Boolean, typeExpression: JSDocTypeExpression = definedExternally, isNameFirst: Boolean = definedExternally, comment: NodeArray<Any /* JSDocText | JSDocLink */> = definedExternally): JSDocParameterTag
  fun updateJSDocParameterTag(node: JSDocParameterTag, tagName: Identifier?, name: Identifier, isBracketed: Boolean, typeExpression: JSDocTypeExpression?, isNameFirst: Boolean, comment: String?): JSDocParameterTag
  fun updateJSDocParameterTag(node: JSDocParameterTag, tagName: Identifier?, name: Identifier, isBracketed: Boolean, typeExpression: JSDocTypeExpression?, isNameFirst: Boolean, comment: NodeArray<Any /* JSDocText | JSDocLink */>?): JSDocParameterTag
  fun updateJSDocParameterTag(node: JSDocParameterTag, tagName: Identifier?, name: QualifiedName, isBracketed: Boolean, typeExpression: JSDocTypeExpression?, isNameFirst: Boolean, comment: String?): JSDocParameterTag
  fun updateJSDocParameterTag(node: JSDocParameterTag, tagName: Identifier?, name: QualifiedName, isBracketed: Boolean, typeExpression: JSDocTypeExpression?, isNameFirst: Boolean, comment: NodeArray<Any /* JSDocText | JSDocLink */>?): JSDocParameterTag
  fun createJSDocPropertyTag(tagName: Identifier?, name: Identifier, isBracketed: Boolean, typeExpression: JSDocTypeExpression = definedExternally, isNameFirst: Boolean = definedExternally, comment: String = definedExternally): JSDocPropertyTag
  fun createJSDocPropertyTag(tagName: Identifier?, name: Identifier, isBracketed: Boolean): JSDocPropertyTag
  fun createJSDocPropertyTag(tagName: Identifier?, name: Identifier, isBracketed: Boolean, typeExpression: JSDocTypeExpression = definedExternally): JSDocPropertyTag
  fun createJSDocPropertyTag(tagName: Identifier?, name: Identifier, isBracketed: Boolean, typeExpression: JSDocTypeExpression = definedExternally, isNameFirst: Boolean = definedExternally): JSDocPropertyTag
  fun createJSDocPropertyTag(tagName: Identifier?, name: Identifier, isBracketed: Boolean, typeExpression: JSDocTypeExpression = definedExternally, isNameFirst: Boolean = definedExternally, comment: NodeArray<Any /* JSDocText | JSDocLink */> = definedExternally): JSDocPropertyTag
  fun createJSDocPropertyTag(tagName: Identifier?, name: QualifiedName, isBracketed: Boolean, typeExpression: JSDocTypeExpression = definedExternally, isNameFirst: Boolean = definedExternally, comment: String = definedExternally): JSDocPropertyTag
  fun createJSDocPropertyTag(tagName: Identifier?, name: QualifiedName, isBracketed: Boolean): JSDocPropertyTag
  fun createJSDocPropertyTag(tagName: Identifier?, name: QualifiedName, isBracketed: Boolean, typeExpression: JSDocTypeExpression = definedExternally): JSDocPropertyTag
  fun createJSDocPropertyTag(tagName: Identifier?, name: QualifiedName, isBracketed: Boolean, typeExpression: JSDocTypeExpression = definedExternally, isNameFirst: Boolean = definedExternally): JSDocPropertyTag
  fun createJSDocPropertyTag(tagName: Identifier?, name: QualifiedName, isBracketed: Boolean, typeExpression: JSDocTypeExpression = definedExternally, isNameFirst: Boolean = definedExternally, comment: NodeArray<Any /* JSDocText | JSDocLink */> = definedExternally): JSDocPropertyTag
  fun updateJSDocPropertyTag(node: JSDocPropertyTag, tagName: Identifier?, name: Identifier, isBracketed: Boolean, typeExpression: JSDocTypeExpression?, isNameFirst: Boolean, comment: String?): JSDocPropertyTag
  fun updateJSDocPropertyTag(node: JSDocPropertyTag, tagName: Identifier?, name: Identifier, isBracketed: Boolean, typeExpression: JSDocTypeExpression?, isNameFirst: Boolean, comment: NodeArray<Any /* JSDocText | JSDocLink */>?): JSDocPropertyTag
  fun updateJSDocPropertyTag(node: JSDocPropertyTag, tagName: Identifier?, name: QualifiedName, isBracketed: Boolean, typeExpression: JSDocTypeExpression?, isNameFirst: Boolean, comment: String?): JSDocPropertyTag
  fun updateJSDocPropertyTag(node: JSDocPropertyTag, tagName: Identifier?, name: QualifiedName, isBracketed: Boolean, typeExpression: JSDocTypeExpression?, isNameFirst: Boolean, comment: NodeArray<Any /* JSDocText | JSDocLink */>?): JSDocPropertyTag
  fun createJSDocTypeTag(tagName: Identifier?, typeExpression: JSDocTypeExpression, comment: String = definedExternally): JSDocTypeTag
  fun createJSDocTypeTag(tagName: Identifier?, typeExpression: JSDocTypeExpression): JSDocTypeTag
  fun createJSDocTypeTag(tagName: Identifier?, typeExpression: JSDocTypeExpression, comment: NodeArray<Any /* JSDocText | JSDocLink */> = definedExternally): JSDocTypeTag
  fun updateJSDocTypeTag(node: JSDocTypeTag, tagName: Identifier?, typeExpression: JSDocTypeExpression, comment: String?): JSDocTypeTag
  fun updateJSDocTypeTag(node: JSDocTypeTag, tagName: Identifier?, typeExpression: JSDocTypeExpression, comment: NodeArray<Any /* JSDocText | JSDocLink */>?): JSDocTypeTag
  fun createJSDocSeeTag(tagName: Identifier?, nameExpression: JSDocNameReference?, comment: String = definedExternally): JSDocSeeTag
  fun createJSDocSeeTag(tagName: Identifier?, nameExpression: JSDocNameReference?): JSDocSeeTag
  fun createJSDocSeeTag(tagName: Identifier?, nameExpression: JSDocNameReference?, comment: NodeArray<Any /* JSDocText | JSDocLink */> = definedExternally): JSDocSeeTag
  fun updateJSDocSeeTag(node: JSDocSeeTag, tagName: Identifier?, nameExpression: JSDocNameReference?, comment: String = definedExternally): JSDocSeeTag
  fun updateJSDocSeeTag(node: JSDocSeeTag, tagName: Identifier?, nameExpression: JSDocNameReference?): JSDocSeeTag
  fun updateJSDocSeeTag(node: JSDocSeeTag, tagName: Identifier?, nameExpression: JSDocNameReference?, comment: NodeArray<Any /* JSDocText | JSDocLink */> = definedExternally): JSDocSeeTag
  fun createJSDocReturnTag(tagName: Identifier?, typeExpression: JSDocTypeExpression = definedExternally, comment: String = definedExternally): JSDocReturnTag
  fun createJSDocReturnTag(tagName: Identifier?): JSDocReturnTag
  fun createJSDocReturnTag(tagName: Identifier?, typeExpression: JSDocTypeExpression = definedExternally): JSDocReturnTag
  fun createJSDocReturnTag(tagName: Identifier?, typeExpression: JSDocTypeExpression = definedExternally, comment: NodeArray<Any /* JSDocText | JSDocLink */> = definedExternally): JSDocReturnTag
  fun updateJSDocReturnTag(node: JSDocReturnTag, tagName: Identifier?, typeExpression: JSDocTypeExpression?, comment: String?): JSDocReturnTag
  fun updateJSDocReturnTag(node: JSDocReturnTag, tagName: Identifier?, typeExpression: JSDocTypeExpression?, comment: NodeArray<Any /* JSDocText | JSDocLink */>?): JSDocReturnTag
  fun createJSDocThisTag(tagName: Identifier?, typeExpression: JSDocTypeExpression, comment: String = definedExternally): JSDocThisTag
  fun createJSDocThisTag(tagName: Identifier?, typeExpression: JSDocTypeExpression): JSDocThisTag
  fun createJSDocThisTag(tagName: Identifier?, typeExpression: JSDocTypeExpression, comment: NodeArray<Any /* JSDocText | JSDocLink */> = definedExternally): JSDocThisTag
  fun updateJSDocThisTag(node: JSDocThisTag, tagName: Identifier?, typeExpression: JSDocTypeExpression?, comment: String?): JSDocThisTag
  fun updateJSDocThisTag(node: JSDocThisTag, tagName: Identifier?, typeExpression: JSDocTypeExpression?, comment: NodeArray<Any /* JSDocText | JSDocLink */>?): JSDocThisTag
  fun createJSDocEnumTag(tagName: Identifier?, typeExpression: JSDocTypeExpression, comment: String = definedExternally): JSDocEnumTag
  fun createJSDocEnumTag(tagName: Identifier?, typeExpression: JSDocTypeExpression): JSDocEnumTag
  fun createJSDocEnumTag(tagName: Identifier?, typeExpression: JSDocTypeExpression, comment: NodeArray<Any /* JSDocText | JSDocLink */> = definedExternally): JSDocEnumTag
  fun updateJSDocEnumTag(node: JSDocEnumTag, tagName: Identifier?, typeExpression: JSDocTypeExpression, comment: String?): JSDocEnumTag
  fun updateJSDocEnumTag(node: JSDocEnumTag, tagName: Identifier?, typeExpression: JSDocTypeExpression, comment: NodeArray<Any /* JSDocText | JSDocLink */>?): JSDocEnumTag
  fun createJSDocCallbackTag(tagName: Identifier?, typeExpression: JSDocSignature, fullName: Identifier = definedExternally, comment: String = definedExternally): JSDocCallbackTag
  fun createJSDocCallbackTag(tagName: Identifier?, typeExpression: JSDocSignature): JSDocCallbackTag
  fun createJSDocCallbackTag(tagName: Identifier?, typeExpression: JSDocSignature, fullName: Identifier = definedExternally): JSDocCallbackTag
  fun createJSDocCallbackTag(tagName: Identifier?, typeExpression: JSDocSignature, fullName: Identifier = definedExternally, comment: NodeArray<Any /* JSDocText | JSDocLink */> = definedExternally): JSDocCallbackTag
  fun createJSDocCallbackTag(tagName: Identifier?, typeExpression: JSDocSignature, fullName: JSDocNamespaceDeclaration = definedExternally, comment: String = definedExternally): JSDocCallbackTag
  fun createJSDocCallbackTag(tagName: Identifier?, typeExpression: JSDocSignature, fullName: JSDocNamespaceDeclaration = definedExternally): JSDocCallbackTag
  fun createJSDocCallbackTag(tagName: Identifier?, typeExpression: JSDocSignature, fullName: JSDocNamespaceDeclaration = definedExternally, comment: NodeArray<Any /* JSDocText | JSDocLink */> = definedExternally): JSDocCallbackTag
  fun updateJSDocCallbackTag(node: JSDocCallbackTag, tagName: Identifier?, typeExpression: JSDocSignature, fullName: Identifier?, comment: String?): JSDocCallbackTag
  fun updateJSDocCallbackTag(node: JSDocCallbackTag, tagName: Identifier?, typeExpression: JSDocSignature, fullName: Identifier?, comment: NodeArray<Any /* JSDocText | JSDocLink */>?): JSDocCallbackTag
  fun updateJSDocCallbackTag(node: JSDocCallbackTag, tagName: Identifier?, typeExpression: JSDocSignature, fullName: JSDocNamespaceDeclaration?, comment: String?): JSDocCallbackTag
  fun updateJSDocCallbackTag(node: JSDocCallbackTag, tagName: Identifier?, typeExpression: JSDocSignature, fullName: JSDocNamespaceDeclaration?, comment: NodeArray<Any /* JSDocText | JSDocLink */>?): JSDocCallbackTag
  fun createJSDocAugmentsTag(tagName: Identifier?, className: ExpressionWithTypeArguments /* ExpressionWithTypeArguments & `T$4` */, comment: String = definedExternally): JSDocAugmentsTag
  fun createJSDocAugmentsTag(tagName: Identifier?, className: ExpressionWithTypeArguments /* ExpressionWithTypeArguments & `T$4` */): JSDocAugmentsTag
  fun createJSDocAugmentsTag(tagName: Identifier?, className: ExpressionWithTypeArguments /* ExpressionWithTypeArguments & `T$4` */, comment: NodeArray<Any /* JSDocText | JSDocLink */> = definedExternally): JSDocAugmentsTag
  fun updateJSDocAugmentsTag(node: JSDocAugmentsTag, tagName: Identifier?, className: ExpressionWithTypeArguments /* ExpressionWithTypeArguments & `T$4` */, comment: String?): JSDocAugmentsTag
  fun updateJSDocAugmentsTag(node: JSDocAugmentsTag, tagName: Identifier?, className: ExpressionWithTypeArguments /* ExpressionWithTypeArguments & `T$4` */, comment: NodeArray<Any /* JSDocText | JSDocLink */>?): JSDocAugmentsTag
  fun createJSDocImplementsTag(tagName: Identifier?, className: ExpressionWithTypeArguments /* ExpressionWithTypeArguments & `T$4` */, comment: String = definedExternally): JSDocImplementsTag
  fun createJSDocImplementsTag(tagName: Identifier?, className: ExpressionWithTypeArguments /* ExpressionWithTypeArguments & `T$4` */): JSDocImplementsTag
  fun createJSDocImplementsTag(tagName: Identifier?, className: ExpressionWithTypeArguments /* ExpressionWithTypeArguments & `T$4` */, comment: NodeArray<Any /* JSDocText | JSDocLink */> = definedExternally): JSDocImplementsTag
  fun updateJSDocImplementsTag(node: JSDocImplementsTag, tagName: Identifier?, className: ExpressionWithTypeArguments /* ExpressionWithTypeArguments & `T$4` */, comment: String?): JSDocImplementsTag
  fun updateJSDocImplementsTag(node: JSDocImplementsTag, tagName: Identifier?, className: ExpressionWithTypeArguments /* ExpressionWithTypeArguments & `T$4` */, comment: NodeArray<Any /* JSDocText | JSDocLink */>?): JSDocImplementsTag
  fun createJSDocAuthorTag(tagName: Identifier?, comment: String = definedExternally): JSDocAuthorTag
  fun createJSDocAuthorTag(tagName: Identifier?): JSDocAuthorTag
  fun createJSDocAuthorTag(tagName: Identifier?, comment: NodeArray<Any /* JSDocText | JSDocLink */> = definedExternally): JSDocAuthorTag
  fun updateJSDocAuthorTag(node: JSDocAuthorTag, tagName: Identifier?, comment: String?): JSDocAuthorTag
  fun updateJSDocAuthorTag(node: JSDocAuthorTag, tagName: Identifier?, comment: NodeArray<Any /* JSDocText | JSDocLink */>?): JSDocAuthorTag
  fun createJSDocClassTag(tagName: Identifier?, comment: String = definedExternally): JSDocClassTag
  fun createJSDocClassTag(tagName: Identifier?): JSDocClassTag
  fun createJSDocClassTag(tagName: Identifier?, comment: NodeArray<Any /* JSDocText | JSDocLink */> = definedExternally): JSDocClassTag
  fun updateJSDocClassTag(node: JSDocClassTag, tagName: Identifier?, comment: String?): JSDocClassTag
  fun updateJSDocClassTag(node: JSDocClassTag, tagName: Identifier?, comment: NodeArray<Any /* JSDocText | JSDocLink */>?): JSDocClassTag
  fun createJSDocPublicTag(tagName: Identifier?, comment: String = definedExternally): JSDocPublicTag
  fun createJSDocPublicTag(tagName: Identifier?): JSDocPublicTag
  fun createJSDocPublicTag(tagName: Identifier?, comment: NodeArray<Any /* JSDocText | JSDocLink */> = definedExternally): JSDocPublicTag
  fun updateJSDocPublicTag(node: JSDocPublicTag, tagName: Identifier?, comment: String?): JSDocPublicTag
  fun updateJSDocPublicTag(node: JSDocPublicTag, tagName: Identifier?, comment: NodeArray<Any /* JSDocText | JSDocLink */>?): JSDocPublicTag
  fun createJSDocPrivateTag(tagName: Identifier?, comment: String = definedExternally): JSDocPrivateTag
  fun createJSDocPrivateTag(tagName: Identifier?): JSDocPrivateTag
  fun createJSDocPrivateTag(tagName: Identifier?, comment: NodeArray<Any /* JSDocText | JSDocLink */> = definedExternally): JSDocPrivateTag
  fun updateJSDocPrivateTag(node: JSDocPrivateTag, tagName: Identifier?, comment: String?): JSDocPrivateTag
  fun updateJSDocPrivateTag(node: JSDocPrivateTag, tagName: Identifier?, comment: NodeArray<Any /* JSDocText | JSDocLink */>?): JSDocPrivateTag
  fun createJSDocProtectedTag(tagName: Identifier?, comment: String = definedExternally): JSDocProtectedTag
  fun createJSDocProtectedTag(tagName: Identifier?): JSDocProtectedTag
  fun createJSDocProtectedTag(tagName: Identifier?, comment: NodeArray<Any /* JSDocText | JSDocLink */> = definedExternally): JSDocProtectedTag
  fun updateJSDocProtectedTag(node: JSDocProtectedTag, tagName: Identifier?, comment: String?): JSDocProtectedTag
  fun updateJSDocProtectedTag(node: JSDocProtectedTag, tagName: Identifier?, comment: NodeArray<Any /* JSDocText | JSDocLink */>?): JSDocProtectedTag
  fun createJSDocReadonlyTag(tagName: Identifier?, comment: String = definedExternally): JSDocReadonlyTag
  fun createJSDocReadonlyTag(tagName: Identifier?): JSDocReadonlyTag
  fun createJSDocReadonlyTag(tagName: Identifier?, comment: NodeArray<Any /* JSDocText | JSDocLink */> = definedExternally): JSDocReadonlyTag
  fun updateJSDocReadonlyTag(node: JSDocReadonlyTag, tagName: Identifier?, comment: String?): JSDocReadonlyTag
  fun updateJSDocReadonlyTag(node: JSDocReadonlyTag, tagName: Identifier?, comment: NodeArray<Any /* JSDocText | JSDocLink */>?): JSDocReadonlyTag
  fun createJSDocUnknownTag(tagName: Identifier, comment: String = definedExternally): JSDocUnknownTag
  fun createJSDocUnknownTag(tagName: Identifier): JSDocUnknownTag
  fun createJSDocUnknownTag(tagName: Identifier, comment: NodeArray<Any /* JSDocText | JSDocLink */> = definedExternally): JSDocUnknownTag
  fun updateJSDocUnknownTag(node: JSDocUnknownTag, tagName: Identifier, comment: String?): JSDocUnknownTag
  fun updateJSDocUnknownTag(node: JSDocUnknownTag, tagName: Identifier, comment: NodeArray<Any /* JSDocText | JSDocLink */>?): JSDocUnknownTag
  fun createJSDocDeprecatedTag(tagName: Identifier, comment: String = definedExternally): JSDocDeprecatedTag
  fun createJSDocDeprecatedTag(tagName: Identifier): JSDocDeprecatedTag
  fun createJSDocDeprecatedTag(tagName: Identifier, comment: NodeArray<Any /* JSDocText | JSDocLink */> = definedExternally): JSDocDeprecatedTag
  fun updateJSDocDeprecatedTag(node: JSDocDeprecatedTag, tagName: Identifier, comment: String = definedExternally): JSDocDeprecatedTag
  fun updateJSDocDeprecatedTag(node: JSDocDeprecatedTag, tagName: Identifier): JSDocDeprecatedTag
  fun updateJSDocDeprecatedTag(node: JSDocDeprecatedTag, tagName: Identifier, comment: NodeArray<Any /* JSDocText | JSDocLink */> = definedExternally): JSDocDeprecatedTag
  fun createJSDocOverrideTag(tagName: Identifier, comment: String = definedExternally): JSDocOverrideTag
  fun createJSDocOverrideTag(tagName: Identifier): JSDocOverrideTag
  fun createJSDocOverrideTag(tagName: Identifier, comment: NodeArray<Any /* JSDocText | JSDocLink */> = definedExternally): JSDocOverrideTag
  fun updateJSDocOverrideTag(node: JSDocOverrideTag, tagName: Identifier, comment: String = definedExternally): JSDocOverrideTag
  fun updateJSDocOverrideTag(node: JSDocOverrideTag, tagName: Identifier): JSDocOverrideTag
  fun updateJSDocOverrideTag(node: JSDocOverrideTag, tagName: Identifier, comment: NodeArray<Any /* JSDocText | JSDocLink */> = definedExternally): JSDocOverrideTag
  fun createJSDocText(text: String): JSDocText
  fun updateJSDocText(node: JSDocText, text: String): JSDocText
  fun createJSDocComment(comment: String? = definedExternally, tags: Array<JSDocTag>? = definedExternally): JSDoc
  fun createJSDocComment(): JSDoc
  fun createJSDocComment(comment: String? = definedExternally): JSDoc
  fun createJSDocComment(comment: NodeArray<Any /* JSDocText | JSDocLink */>? = definedExternally, tags: Array<JSDocTag>? = definedExternally): JSDoc
  fun createJSDocComment(comment: NodeArray<Any /* JSDocText | JSDocLink */>? = definedExternally): JSDoc
  fun updateJSDocComment(node: JSDoc, comment: String?, tags: Array<JSDocTag>?): JSDoc
  fun updateJSDocComment(node: JSDoc, comment: NodeArray<Any /* JSDocText | JSDocLink */>?, tags: Array<JSDocTag>?): JSDoc
  fun createJsxElement(openingElement: JsxOpeningElement, children: Array<Any /* JsxText | JsxExpression | JsxElement | JsxSelfClosingElement | JsxFragment */>, closingElement: JsxClosingElement): JsxElement
  fun updateJsxElement(node: JsxElement, openingElement: JsxOpeningElement, children: Array<Any /* JsxText | JsxExpression | JsxElement | JsxSelfClosingElement | JsxFragment */>, closingElement: JsxClosingElement): JsxElement
  fun createJsxSelfClosingElement(tagName: Identifier, typeArguments: Array<TypeNode>?, attributes: JsxAttributes): JsxSelfClosingElement
  fun createJsxSelfClosingElement(tagName: ThisExpression, typeArguments: Array<TypeNode>?, attributes: JsxAttributes): JsxSelfClosingElement
  fun createJsxSelfClosingElement(tagName: JsxTagNamePropertyAccess, typeArguments: Array<TypeNode>?, attributes: JsxAttributes): JsxSelfClosingElement
  fun updateJsxSelfClosingElement(node: JsxSelfClosingElement, tagName: Identifier, typeArguments: Array<TypeNode>?, attributes: JsxAttributes): JsxSelfClosingElement
  fun updateJsxSelfClosingElement(node: JsxSelfClosingElement, tagName: ThisExpression, typeArguments: Array<TypeNode>?, attributes: JsxAttributes): JsxSelfClosingElement
  fun updateJsxSelfClosingElement(node: JsxSelfClosingElement, tagName: JsxTagNamePropertyAccess, typeArguments: Array<TypeNode>?, attributes: JsxAttributes): JsxSelfClosingElement
  fun createJsxOpeningElement(tagName: Identifier, typeArguments: Array<TypeNode>?, attributes: JsxAttributes): JsxOpeningElement
  fun createJsxOpeningElement(tagName: ThisExpression, typeArguments: Array<TypeNode>?, attributes: JsxAttributes): JsxOpeningElement
  fun createJsxOpeningElement(tagName: JsxTagNamePropertyAccess, typeArguments: Array<TypeNode>?, attributes: JsxAttributes): JsxOpeningElement
  fun updateJsxOpeningElement(node: JsxOpeningElement, tagName: Identifier, typeArguments: Array<TypeNode>?, attributes: JsxAttributes): JsxOpeningElement
  fun updateJsxOpeningElement(node: JsxOpeningElement, tagName: ThisExpression, typeArguments: Array<TypeNode>?, attributes: JsxAttributes): JsxOpeningElement
  fun updateJsxOpeningElement(node: JsxOpeningElement, tagName: JsxTagNamePropertyAccess, typeArguments: Array<TypeNode>?, attributes: JsxAttributes): JsxOpeningElement
  fun createJsxClosingElement(tagName: Identifier): JsxClosingElement
  fun createJsxClosingElement(tagName: ThisExpression): JsxClosingElement
  fun createJsxClosingElement(tagName: JsxTagNamePropertyAccess): JsxClosingElement
  fun updateJsxClosingElement(node: JsxClosingElement, tagName: Identifier): JsxClosingElement
  fun updateJsxClosingElement(node: JsxClosingElement, tagName: ThisExpression): JsxClosingElement
  fun updateJsxClosingElement(node: JsxClosingElement, tagName: JsxTagNamePropertyAccess): JsxClosingElement
  fun createJsxFragment(openingFragment: JsxOpeningFragment, children: Array<Any /* JsxText | JsxExpression | JsxElement | JsxSelfClosingElement | JsxFragment */>, closingFragment: JsxClosingFragment): JsxFragment
  fun createJsxText(text: String, containsOnlyTriviaWhiteSpaces: Boolean = definedExternally): JsxText
  fun updateJsxText(node: JsxText, text: String, containsOnlyTriviaWhiteSpaces: Boolean = definedExternally): JsxText
  fun createJsxOpeningFragment(): JsxOpeningFragment
  fun createJsxJsxClosingFragment(): JsxClosingFragment
  fun updateJsxFragment(node: JsxFragment, openingFragment: JsxOpeningFragment, children: Array<Any /* JsxText | JsxExpression | JsxElement | JsxSelfClosingElement | JsxFragment */>, closingFragment: JsxClosingFragment): JsxFragment
  fun createJsxAttribute(name: Identifier, initializer: StringLiteral?): JsxAttribute
  fun createJsxAttribute(name: Identifier, initializer: JsxExpression?): JsxAttribute
  fun updateJsxAttribute(node: JsxAttribute, name: Identifier, initializer: StringLiteral?): JsxAttribute
  fun updateJsxAttribute(node: JsxAttribute, name: Identifier, initializer: JsxExpression?): JsxAttribute
  fun createJsxAttributes(properties: Array<Any /* JsxAttribute | JsxSpreadAttribute */>): JsxAttributes
  fun updateJsxAttributes(node: JsxAttributes, properties: Array<Any /* JsxAttribute | JsxSpreadAttribute */>): JsxAttributes
  fun createJsxSpreadAttribute(expression: Expression): JsxSpreadAttribute
  fun updateJsxSpreadAttribute(node: JsxSpreadAttribute, expression: Expression): JsxSpreadAttribute
  fun createJsxExpression(dotDotDotToken: DotDotDotToken?, expression: Expression?): JsxExpression
  fun updateJsxExpression(node: JsxExpression, expression: Expression?): JsxExpression
  fun createCaseClause(expression: Expression, statements: Array<Statement>): CaseClause
  fun updateCaseClause(node: CaseClause, expression: Expression, statements: Array<Statement>): CaseClause
  fun createDefaultClause(statements: Array<Statement>): DefaultClause
  fun updateDefaultClause(node: DefaultClause, statements: Array<Statement>): DefaultClause
  fun createHeritageClause(token: SyntaxKind = definedExternally, types: Array<ExpressionWithTypeArguments>): HeritageClause
  fun updateHeritageClause(node: HeritageClause, types: Array<ExpressionWithTypeArguments>): HeritageClause
  fun createCatchClause(variableDeclaration: String?, block: Block): CatchClause
  fun createCatchClause(variableDeclaration: VariableDeclaration?, block: Block): CatchClause
  fun updateCatchClause(node: CatchClause, variableDeclaration: VariableDeclaration?, block: Block): CatchClause
  fun createPropertyAssignment(name: String, initializer: Expression): PropertyAssignment
  fun createPropertyAssignment(name: Identifier, initializer: Expression): PropertyAssignment
  fun createPropertyAssignment(name: StringLiteral, initializer: Expression): PropertyAssignment
  fun createPropertyAssignment(name: NumericLiteral, initializer: Expression): PropertyAssignment
  fun createPropertyAssignment(name: ComputedPropertyName, initializer: Expression): PropertyAssignment
  fun createPropertyAssignment(name: PrivateIdentifier, initializer: Expression): PropertyAssignment
  fun updatePropertyAssignment(node: PropertyAssignment, name: Identifier, initializer: Expression): PropertyAssignment
  fun updatePropertyAssignment(node: PropertyAssignment, name: StringLiteral, initializer: Expression): PropertyAssignment
  fun updatePropertyAssignment(node: PropertyAssignment, name: NumericLiteral, initializer: Expression): PropertyAssignment
  fun updatePropertyAssignment(node: PropertyAssignment, name: ComputedPropertyName, initializer: Expression): PropertyAssignment
  fun updatePropertyAssignment(node: PropertyAssignment, name: PrivateIdentifier, initializer: Expression): PropertyAssignment
  fun createShorthandPropertyAssignment(name: String, objectAssignmentInitializer: Expression = definedExternally): ShorthandPropertyAssignment
  fun createShorthandPropertyAssignment(name: String): ShorthandPropertyAssignment
  fun createShorthandPropertyAssignment(name: Identifier, objectAssignmentInitializer: Expression = definedExternally): ShorthandPropertyAssignment
  fun createShorthandPropertyAssignment(name: Identifier): ShorthandPropertyAssignment
  fun updateShorthandPropertyAssignment(node: ShorthandPropertyAssignment, name: Identifier, objectAssignmentInitializer: Expression?): ShorthandPropertyAssignment
  fun createSpreadAssignment(expression: Expression): SpreadAssignment
  fun updateSpreadAssignment(node: SpreadAssignment, expression: Expression): SpreadAssignment
  fun createEnumMember(name: String, initializer: Expression = definedExternally): EnumMember
  fun createEnumMember(name: String): EnumMember
  fun createEnumMember(name: Identifier, initializer: Expression = definedExternally): EnumMember
  fun createEnumMember(name: Identifier): EnumMember
  fun createEnumMember(name: StringLiteral, initializer: Expression = definedExternally): EnumMember
  fun createEnumMember(name: StringLiteral): EnumMember
  fun createEnumMember(name: NumericLiteral, initializer: Expression = definedExternally): EnumMember
  fun createEnumMember(name: NumericLiteral): EnumMember
  fun createEnumMember(name: ComputedPropertyName, initializer: Expression = definedExternally): EnumMember
  fun createEnumMember(name: ComputedPropertyName): EnumMember
  fun createEnumMember(name: PrivateIdentifier, initializer: Expression = definedExternally): EnumMember
  fun createEnumMember(name: PrivateIdentifier): EnumMember
  fun updateEnumMember(node: EnumMember, name: Identifier, initializer: Expression?): EnumMember
  fun updateEnumMember(node: EnumMember, name: StringLiteral, initializer: Expression?): EnumMember
  fun updateEnumMember(node: EnumMember, name: NumericLiteral, initializer: Expression?): EnumMember
  fun updateEnumMember(node: EnumMember, name: ComputedPropertyName, initializer: Expression?): EnumMember
  fun updateEnumMember(node: EnumMember, name: PrivateIdentifier, initializer: Expression?): EnumMember
  fun createSourceFile(statements: Array<Statement>, endOfFileToken: Token<SyntaxKind> /* Token<SyntaxKind.EndOfFileToken> & JSDocContainer */, flags: NodeFlags): SourceFile
  fun updateSourceFile(node: SourceFile, statements: Array<Statement>, isDeclarationFile: Boolean = definedExternally, referencedFiles: Array<FileReference> = definedExternally, typeReferences: Array<FileReference> = definedExternally, hasNoDefaultLib: Boolean = definedExternally, libReferences: Array<FileReference> = definedExternally): SourceFile
  fun createNotEmittedStatement(original: Node): NotEmittedStatement
  fun createPartiallyEmittedExpression(expression: Expression, original: Node = definedExternally): PartiallyEmittedExpression
  fun updatePartiallyEmittedExpression(node: PartiallyEmittedExpression, expression: Expression): PartiallyEmittedExpression
  fun createCommaListExpression(elements: Array<Expression>): CommaListExpression
  fun updateCommaListExpression(node: CommaListExpression, elements: Array<Expression>): CommaListExpression
  fun createBundle(sourceFiles: Array<SourceFile>, prepends: Array<Any /* UnparsedSource | InputFiles */> = definedExternally): Bundle
  fun updateBundle(node: Bundle, sourceFiles: Array<SourceFile>, prepends: Array<Any /* UnparsedSource | InputFiles */> = definedExternally): Bundle
  fun createComma(left: Expression, right: Expression): BinaryExpression
  fun createAssignment(left: ObjectLiteralExpression, right: Expression): dynamic /* ObjectDestructuringAssignment | ArrayDestructuringAssignment */
  fun createAssignment(left: ArrayLiteralExpression, right: Expression): dynamic /* ObjectDestructuringAssignment | ArrayDestructuringAssignment */
  fun createAssignment(left: Expression, right: Expression): AssignmentExpression<EqualsToken>
  fun createLogicalOr(left: Expression, right: Expression): BinaryExpression
  fun createLogicalAnd(left: Expression, right: Expression): BinaryExpression
  fun createBitwiseOr(left: Expression, right: Expression): BinaryExpression
  fun createBitwiseXor(left: Expression, right: Expression): BinaryExpression
  fun createBitwiseAnd(left: Expression, right: Expression): BinaryExpression
  fun createStrictEquality(left: Expression, right: Expression): BinaryExpression
  fun createStrictInequality(left: Expression, right: Expression): BinaryExpression
  fun createEquality(left: Expression, right: Expression): BinaryExpression
  fun createInequality(left: Expression, right: Expression): BinaryExpression
  fun createLessThan(left: Expression, right: Expression): BinaryExpression
  fun createLessThanEquals(left: Expression, right: Expression): BinaryExpression
  fun createGreaterThan(left: Expression, right: Expression): BinaryExpression
  fun createGreaterThanEquals(left: Expression, right: Expression): BinaryExpression
  fun createLeftShift(left: Expression, right: Expression): BinaryExpression
  fun createRightShift(left: Expression, right: Expression): BinaryExpression
  fun createUnsignedRightShift(left: Expression, right: Expression): BinaryExpression
  fun createAdd(left: Expression, right: Expression): BinaryExpression
  fun createSubtract(left: Expression, right: Expression): BinaryExpression
  fun createMultiply(left: Expression, right: Expression): BinaryExpression
  fun createDivide(left: Expression, right: Expression): BinaryExpression
  fun createModulo(left: Expression, right: Expression): BinaryExpression
  fun createExponent(left: Expression, right: Expression): BinaryExpression
  fun createPrefixPlus(operand: Expression): PrefixUnaryExpression
  fun createPrefixMinus(operand: Expression): PrefixUnaryExpression
  fun createPrefixIncrement(operand: Expression): PrefixUnaryExpression
  fun createPrefixDecrement(operand: Expression): PrefixUnaryExpression
  fun createBitwiseNot(operand: Expression): PrefixUnaryExpression
  fun createLogicalNot(operand: Expression): PrefixUnaryExpression
  fun createPostfixIncrement(operand: Expression): PostfixUnaryExpression
  fun createPostfixDecrement(operand: Expression): PostfixUnaryExpression
  fun createImmediatelyInvokedFunctionExpression(statements: Array<Statement>): CallExpression
  fun createImmediatelyInvokedFunctionExpression(statements: Array<Statement>, param: ParameterDeclaration, paramValue: Expression): CallExpression
  fun createImmediatelyInvokedArrowFunction(statements: Array<Statement>): CallExpression
  fun createImmediatelyInvokedArrowFunction(statements: Array<Statement>, param: ParameterDeclaration, paramValue: Expression): CallExpression
  fun createVoidZero(): VoidExpression
  fun createExportDefault(expression: Expression): ExportAssignment
  fun createExternalModuleExport(exportName: Identifier): ExportDeclaration
  fun restoreOuterExpressions(outerExpression: Expression?, innerExpression: Expression, kinds: OuterExpressionKinds = definedExternally): Expression
}

external interface CoreTransformationContext {
  val factory: NodeFactory
  fun getCompilerOptions(): CompilerOptions
  fun startLexicalEnvironment()
  fun suspendLexicalEnvironment()
  fun resumeLexicalEnvironment()
  fun endLexicalEnvironment(): Array<Statement>?
  fun hoistFunctionDeclaration(node: FunctionDeclaration)
  fun hoistVariableDeclaration(node: Identifier)
}

external interface TransformationContext : CoreTransformationContext {
  fun requestEmitHelper(helper: ScopedEmitHelper)
  fun requestEmitHelper(helper: UnscopedEmitHelper)
  fun readEmitHelpers(): Array<dynamic /* ScopedEmitHelper | UnscopedEmitHelper */>?
  fun enableSubstitution(kind: SyntaxKind)
  fun isSubstitutionEnabled(node: Node): Boolean
  var onSubstituteNode: (hint: EmitHint, node: Node) -> Node
  fun enableEmitNotification(kind: SyntaxKind)
  fun isEmitNotificationEnabled(node: Node): Boolean
  var onEmitNode: (hint: EmitHint, node: Node, emitCallback: (hint: EmitHint, node: Node) -> Unit) -> Unit
}

external interface TransformationResult<T : Node> {
  var transformed: Array<T>
  var diagnostics: Array<DiagnosticWithLocation>?
    get() = definedExternally
    set(value) = definedExternally

  fun substituteNode(hint: EmitHint, node: Node): Node
  fun emitNodeWithNotification(hint: EmitHint, node: Node, emitCallback: (hint: EmitHint, node: Node) -> Unit)
  val isEmitNotificationEnabled: ((node: Node) -> Boolean)?
  fun dispose()
}

external interface NodeVisitor {
  @nativeInvoke
  operator fun <T : Node> invoke(nodes: T, visitor: Visitor?, test: (node: Node) -> Boolean = definedExternally, lift: (node: Array<Node>) -> T = definedExternally): T
  @nativeInvoke
  operator fun <T : Node> invoke(nodes: T, visitor: Visitor?): T
  @nativeInvoke
  operator fun <T : Node> invoke(nodes: T, visitor: Visitor?, test: (node: Node) -> Boolean = definedExternally): T
  @nativeInvoke
  operator fun <T : Node> invoke(nodes: T?, visitor: Visitor?, test: (node: Node) -> Boolean = definedExternally, lift: (node: Array<Node>) -> T = definedExternally): T?
  @nativeInvoke
  operator fun <T : Node> invoke(nodes: T?, visitor: Visitor?): T?
  @nativeInvoke
  operator fun <T : Node> invoke(nodes: T?, visitor: Visitor?, test: (node: Node) -> Boolean = definedExternally): T?
}

external interface NodesVisitor {
  @nativeInvoke
  operator fun <T : Node> invoke(nodes: NodeArray<T>, visitor: Visitor?, test: (node: Node) -> Boolean = definedExternally, start: Number = definedExternally, count: Number = definedExternally): NodeArray<T>
  @nativeInvoke
  operator fun <T : Node> invoke(nodes: NodeArray<T>, visitor: Visitor?): NodeArray<T>
  @nativeInvoke
  operator fun <T : Node> invoke(nodes: NodeArray<T>, visitor: Visitor?, test: (node: Node) -> Boolean = definedExternally): NodeArray<T>
  @nativeInvoke
  operator fun <T : Node> invoke(nodes: NodeArray<T>, visitor: Visitor?, test: (node: Node) -> Boolean = definedExternally, start: Number = definedExternally): NodeArray<T>
  @nativeInvoke
  operator fun <T : Node> invoke(nodes: NodeArray<T>?, visitor: Visitor?, test: (node: Node) -> Boolean = definedExternally, start: Number = definedExternally, count: Number = definedExternally): NodeArray<T>?
  @nativeInvoke
  operator fun <T : Node> invoke(nodes: NodeArray<T>?, visitor: Visitor?): NodeArray<T>?
  @nativeInvoke
  operator fun <T : Node> invoke(nodes: NodeArray<T>?, visitor: Visitor?, test: (node: Node) -> Boolean = definedExternally): NodeArray<T>?
  @nativeInvoke
  operator fun <T : Node> invoke(nodes: NodeArray<T>?, visitor: Visitor?, test: (node: Node) -> Boolean = definedExternally, start: Number = definedExternally): NodeArray<T>?
}

external interface Printer {
  fun printNode(hint: EmitHint, node: Node, sourceFile: SourceFile): String
  fun <T : Node> printList(format: ListFormat, list: NodeArray<T>, sourceFile: SourceFile): String
  fun printFile(sourceFile: SourceFile): String
  fun printBundle(bundle: Bundle): String
}

external interface PrintHandlers {
  val hasGlobalName: ((name: String) -> Boolean)?
  val onEmitNode: ((hint: EmitHint, node: Node, emitCallback: (hint: EmitHint, node: Node) -> Unit) -> Unit)?
  val isEmitNotificationEnabled: ((node: Node) -> Boolean)?
  val substituteNode: ((hint: EmitHint, node: Node) -> Node)?
}

external interface PrinterOptions {
  var removeComments: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var newLine: NewLineKind?
    get() = definedExternally
    set(value) = definedExternally
  var omitTrailingSemicolon: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var noEmitHelpers: Boolean?
    get() = definedExternally
    set(value) = definedExternally
}

external interface GetEffectiveTypeRootsHost {
  val directoryExists: ((directoryName: String) -> Boolean)?
  val getCurrentDirectory: (() -> String)?
}

external interface TextSpan {
  var start: Number
  var length: Number
}

external interface TextChangeRange {
  var span: TextSpan
  var newLength: Number
}

external interface SyntaxList : Node {
  var _children: Array<Node>
}

external enum class ListFormat {
  None /* = 0 */,
  SingleLine /* = 0 */,
  MultiLine /* = 1 */,
  PreserveLines /* = 2 */,
  LinesMask /* = 3 */,
  NotDelimited /* = 0 */,
  BarDelimited /* = 4 */,
  AmpersandDelimited /* = 8 */,
  CommaDelimited /* = 16 */,
  AsteriskDelimited /* = 32 */,
  DelimitersMask /* = 60 */,
  AllowTrailingComma /* = 64 */,
  Indented /* = 128 */,
  SpaceBetweenBraces /* = 256 */,
  SpaceBetweenSiblings /* = 512 */,
  Braces /* = 1024 */,
  Parenthesis /* = 2048 */,
  AngleBrackets /* = 4096 */,
  SquareBrackets /* = 8192 */,
  BracketsMask /* = 15360 */,
  OptionalIfUndefined /* = 16384 */,
  OptionalIfEmpty /* = 32768 */,
  Optional /* = 49152 */,
  PreferNewLine /* = 65536 */,
  NoTrailingNewLine /* = 131072 */,
  NoInterveningComments /* = 262144 */,
  NoSpaceIfEmpty /* = 524288 */,
  SingleElement /* = 1048576 */,
  SpaceAfterList /* = 2097152 */,
  Modifiers /* = 262656 */,
  HeritageClauses /* = 512 */,
  SingleLineTypeLiteralMembers /* = 768 */,
  MultiLineTypeLiteralMembers /* = 32897 */,
  SingleLineTupleTypeElements /* = 528 */,
  MultiLineTupleTypeElements /* = 657 */,
  UnionTypeConstituents /* = 516 */,
  IntersectionTypeConstituents /* = 520 */,
  ObjectBindingPatternElements /* = 525136 */,
  ArrayBindingPatternElements /* = 524880 */,
  ObjectLiteralExpressionProperties /* = 526226 */,
  ArrayLiteralExpressionElements /* = 8914 */,
  CommaListElements /* = 528 */,
  CallExpressionArguments /* = 2576 */,
  NewExpressionArguments /* = 18960 */,
  TemplateExpressionSpans /* = 262144 */,
  SingleLineBlockStatements /* = 768 */,
  MultiLineBlockStatements /* = 129 */,
  VariableDeclarationList /* = 528 */,
  SingleLineFunctionBodyStatements /* = 768 */,
  MultiLineFunctionBodyStatements /* = 1 */,
  ClassHeritageClauses /* = 0 */,
  ClassMembers /* = 129 */,
  InterfaceMembers /* = 129 */,
  EnumMembers /* = 145 */,
  CaseBlockClauses /* = 129 */,
  NamedImportsOrExportsElements /* = 525136 */,
  JsxElementOrFragmentChildren /* = 262144 */,
  JsxElementAttributes /* = 262656 */,
  CaseOrDefaultClauseStatements /* = 163969 */,
  HeritageClauseTypes /* = 528 */,
  SourceFileStatements /* = 131073 */,
  Decorators /* = 2146305 */,
  TypeArguments /* = 53776 */,
  TypeParameters /* = 53776 */,
  Parameters /* = 2576 */,
  IndexSignatureParameters /* = 8848 */,
  JSDocComment /* = 33 */
}

external interface UserPreferences {
  val disableSuggestions: Boolean?
    get() = definedExternally
  val quotePreference: String? /* "auto" | "double" | "single" */
    get() = definedExternally
  val includeCompletionsForModuleExports: Boolean?
    get() = definedExternally
  val includeCompletionsForImportStatements: Boolean?
    get() = definedExternally
  val includeCompletionsWithSnippetText: Boolean?
    get() = definedExternally
  val includeAutomaticOptionalChainCompletions: Boolean?
    get() = definedExternally
  val includeCompletionsWithInsertText: Boolean?
    get() = definedExternally
  val importModuleSpecifierPreference: String? /* "shortest" | "project-relative" | "relative" | "non-relative" */
    get() = definedExternally
  val importModuleSpecifierEnding: String? /* "auto" | "minimal" | "index" | "js" */
    get() = definedExternally
  val allowTextChangesInNewFiles: Boolean?
    get() = definedExternally
  val providePrefixAndSuffixTextForRename: Boolean?
    get() = definedExternally
  val includePackageJsonAutoImports: String? /* "auto" | "on" | "off" */
    get() = definedExternally
  val provideRefactorNotApplicableReason: Boolean?
    get() = definedExternally
}

external interface PseudoBigInt {
  var negative: Boolean?
  var base10Value: String
}

external enum class FileWatcherEventKind {
  Created /* = 0 */,
  Changed /* = 1 */,
  Deleted /* = 2 */
}

external interface System {
  var args: Array<String>
  var newLine: String
  var useCaseSensitiveFileNames: Boolean?
  fun write(s: String)
  val writeOutputIsTTY: (() -> Boolean)?
  fun readFile(path: String, encoding: String = definedExternally): String?
  val getFileSize: ((path: String) -> Number)?
  fun writeFile(path: String, data: String, writeByteOrderMark: Boolean = definedExternally)
  val watchFile: ((path: String, callback: FileWatcherCallback, pollingInterval: Number, options: WatchOptions) -> FileWatcher)?
  val watchDirectory: ((path: String, callback: DirectoryWatcherCallback, recursive: Boolean, options: WatchOptions) -> FileWatcher)?
  fun resolvePath(path: String): String
  fun fileExists(path: String): Boolean
  fun directoryExists(path: String): Boolean
  fun createDirectory(path: String)
  fun getExecutingFilePath(): String
  fun getCurrentDirectory(): String
  fun getDirectories(path: String): Array<String>
  fun readDirectory(path: String, extensions: Array<String> = definedExternally, exclude: Array<String> = definedExternally, include: Array<String> = definedExternally, depth: Number = definedExternally): Array<String>
  val getModifiedTime: ((path: String) -> Date?)?
  val setModifiedTime: ((path: String, time: Date) -> Unit)?
  val deleteFile: ((path: String) -> Unit)?
  val createHash: ((data: String) -> String)?
  val createSHA256Hash: ((data: String) -> String)?
  val getMemoryUsage: (() -> Number)?
  fun exit(exitCode: Number = definedExternally)
  val realpath: ((path: String) -> String)?
  val setTimeout: ((callback: (args: Any) -> Unit, ms: Number, args: Any) -> Any)?
  val clearTimeout: ((timeoutId: Any) -> Unit)?
  val clearScreen: (() -> Unit)?
  val base64decode: ((input: String) -> String)?
  val base64encode: ((input: String) -> String)?
}

external interface FileWatcher {
  fun close()
}

external fun getNodeMajorVersion(): Number?

external var sys: System

external interface Scanner {
  fun getStartPos(): Number
  fun getToken(): SyntaxKind
  fun getTextPos(): Number
  fun getTokenPos(): Number
  fun getTokenText(): String
  fun getTokenValue(): String
  fun hasUnicodeEscape(): Boolean
  fun hasExtendedUnicodeEscape(): Boolean
  fun hasPrecedingLineBreak(): Boolean
  fun isIdentifier(): Boolean
  fun isReservedWord(): Boolean
  fun isUnterminated(): Boolean
  fun reScanGreaterToken(): SyntaxKind
  fun reScanSlashToken(): SyntaxKind
  fun reScanAsteriskEqualsToken(): SyntaxKind
  fun reScanTemplateToken(isTaggedTemplate: Boolean): SyntaxKind
  fun reScanTemplateHeadOrNoSubstitutionTemplate(): SyntaxKind
  fun scanJsxIdentifier(): SyntaxKind
  fun scanJsxAttributeValue(): SyntaxKind
  fun reScanJsxAttributeValue(): SyntaxKind
  fun reScanJsxToken(allowMultilineJsxText: Boolean = definedExternally): dynamic /* SyntaxKind.LessThanSlashToken | SyntaxKind.EndOfFileToken | SyntaxKind.ConflictMarkerTrivia | SyntaxKind.JsxText | SyntaxKind.JsxTextAllWhiteSpaces | SyntaxKind.OpenBraceToken | SyntaxKind.LessThanToken */
  fun reScanLessThanToken(): SyntaxKind
  fun reScanQuestionToken(): SyntaxKind
  fun reScanInvalidIdentifier(): SyntaxKind
  fun scanJsxToken(): dynamic /* SyntaxKind.LessThanSlashToken | SyntaxKind.EndOfFileToken | SyntaxKind.ConflictMarkerTrivia | SyntaxKind.JsxText | SyntaxKind.JsxTextAllWhiteSpaces | SyntaxKind.OpenBraceToken | SyntaxKind.LessThanToken */
  fun scanJsDocToken(): dynamic /* SyntaxKind.EndOfFileToken | SyntaxKind.WhitespaceTrivia | SyntaxKind.AtToken | SyntaxKind.NewLineTrivia | SyntaxKind.AsteriskToken | SyntaxKind.OpenBraceToken | SyntaxKind.CloseBraceToken | SyntaxKind.LessThanToken | SyntaxKind.GreaterThanToken | SyntaxKind.OpenBracketToken | SyntaxKind.CloseBracketToken | SyntaxKind.EqualsToken | SyntaxKind.CommaToken | SyntaxKind.DotToken | SyntaxKind.Identifier | SyntaxKind.BacktickToken | SyntaxKind.Unknown | SyntaxKind.AbstractKeyword | SyntaxKind.AnyKeyword | SyntaxKind.AsKeyword | SyntaxKind.AssertsKeyword | SyntaxKind.AsyncKeyword | SyntaxKind.AwaitKeyword | SyntaxKind.BigIntKeyword | SyntaxKind.BooleanKeyword | SyntaxKind.BreakKeyword | SyntaxKind.CaseKeyword | SyntaxKind.CatchKeyword | SyntaxKind.ClassKeyword | SyntaxKind.ConstKeyword | SyntaxKind.ConstructorKeyword | SyntaxKind.ContinueKeyword | SyntaxKind.DebuggerKeyword | SyntaxKind.DeclareKeyword | SyntaxKind.DefaultKeyword | SyntaxKind.DeleteKeyword | SyntaxKind.DoKeyword | SyntaxKind.ElseKeyword | SyntaxKind.EnumKeyword | SyntaxKind.ExportKeyword | SyntaxKind.ExtendsKeyword | SyntaxKind.FalseKeyword | SyntaxKind.FinallyKeyword | SyntaxKind.ForKeyword | SyntaxKind.FromKeyword | SyntaxKind.FunctionKeyword | SyntaxKind.GetKeyword | SyntaxKind.GlobalKeyword | SyntaxKind.IfKeyword | SyntaxKind.ImplementsKeyword | SyntaxKind.ImportKeyword | SyntaxKind.InferKeyword | SyntaxKind.InKeyword | SyntaxKind.InstanceOfKeyword | SyntaxKind.InterfaceKeyword | SyntaxKind.IntrinsicKeyword | SyntaxKind.IsKeyword | SyntaxKind.KeyOfKeyword | SyntaxKind.LetKeyword | SyntaxKind.ModuleKeyword | SyntaxKind.NamespaceKeyword | SyntaxKind.NeverKeyword | SyntaxKind.NewKeyword | SyntaxKind.NullKeyword | SyntaxKind.NumberKeyword | SyntaxKind.ObjectKeyword | SyntaxKind.OfKeyword | SyntaxKind.PackageKeyword | SyntaxKind.PrivateKeyword | SyntaxKind.ProtectedKeyword | SyntaxKind.PublicKeyword | SyntaxKind.ReadonlyKeyword | SyntaxKind.OverrideKeyword | SyntaxKind.RequireKeyword | SyntaxKind.ReturnKeyword | SyntaxKind.SetKeyword | SyntaxKind.StaticKeyword | SyntaxKind.StringKeyword | SyntaxKind.SuperKeyword | SyntaxKind.SwitchKeyword | SyntaxKind.SymbolKeyword | SyntaxKind.ThisKeyword | SyntaxKind.ThrowKeyword | SyntaxKind.TrueKeyword | SyntaxKind.TryKeyword | SyntaxKind.TypeKeyword | SyntaxKind.TypeOfKeyword | SyntaxKind.UndefinedKeyword | SyntaxKind.UniqueKeyword | SyntaxKind.UnknownKeyword | SyntaxKind.VarKeyword | SyntaxKind.VoidKeyword | SyntaxKind.WhileKeyword | SyntaxKind.WithKeyword | SyntaxKind.YieldKeyword */
  fun scan(): SyntaxKind
  fun getText(): String
  fun setText(text: String?, start: Number = definedExternally, length: Number = definedExternally)
  fun setOnError(onError: ErrorCallback?)
  fun setScriptTarget(scriptTarget: ScriptTarget)
  fun setLanguageVariant(variant: LanguageVariant)
  fun setTextPos(textPos: Number)
  fun <T> lookAhead(callback: () -> T): T
  fun <T> scanRange(start: Number, length: Number, callback: () -> T): T
  fun <T> tryScan(callback: () -> T): T
}

external fun tokenToString(t: SyntaxKind): String?

external fun getPositionOfLineAndCharacter(sourceFile: SourceFileLike, line: Number, character: Number): Number

external fun getLineAndCharacterOfPosition(sourceFile: SourceFileLike, position: Number): LineAndCharacter

external fun isWhiteSpaceLike(ch: Number): Boolean

external fun isWhiteSpaceSingleLine(ch: Number): Boolean

external fun isLineBreak(ch: Number): Boolean

external fun couldStartTrivia(text: String, pos: Number): Boolean

external fun <U> forEachLeadingCommentRange(text: String, pos: Number, cb: (pos: Number, end: Number, kind: Any /* SyntaxKind.SingleLineCommentTrivia | SyntaxKind.MultiLineCommentTrivia */, hasTrailingNewLine: Boolean) -> U): U?

external fun <T, U> forEachLeadingCommentRange(text: String, pos: Number, cb: (pos: Number, end: Number, kind: Any /* SyntaxKind.SingleLineCommentTrivia | SyntaxKind.MultiLineCommentTrivia */, hasTrailingNewLine: Boolean, state: T) -> U, state: T): U?

external fun <U> forEachTrailingCommentRange(text: String, pos: Number, cb: (pos: Number, end: Number, kind: Any /* SyntaxKind.SingleLineCommentTrivia | SyntaxKind.MultiLineCommentTrivia */, hasTrailingNewLine: Boolean) -> U): U?

external fun <T, U> forEachTrailingCommentRange(text: String, pos: Number, cb: (pos: Number, end: Number, kind: Any /* SyntaxKind.SingleLineCommentTrivia | SyntaxKind.MultiLineCommentTrivia */, hasTrailingNewLine: Boolean, state: T) -> U, state: T): U?

external fun <T, U> reduceEachLeadingCommentRange(text: String, pos: Number, cb: (pos: Number, end: Number, kind: Any /* SyntaxKind.SingleLineCommentTrivia | SyntaxKind.MultiLineCommentTrivia */, hasTrailingNewLine: Boolean, state: T, memo: U) -> U, state: T, initial: U): U?

external fun <T, U> reduceEachTrailingCommentRange(text: String, pos: Number, cb: (pos: Number, end: Number, kind: Any /* SyntaxKind.SingleLineCommentTrivia | SyntaxKind.MultiLineCommentTrivia */, hasTrailingNewLine: Boolean, state: T, memo: U) -> U, state: T, initial: U): U?

external fun getLeadingCommentRanges(text: String, pos: Number): Array<CommentRange>?

external fun getTrailingCommentRanges(text: String, pos: Number): Array<CommentRange>?

external fun getShebang(text: String): String?

external fun isIdentifierStart(ch: Number, languageVersion: ScriptTarget?): Boolean

external fun isIdentifierPart(ch: Number, languageVersion: ScriptTarget?, identifierVariant: LanguageVariant = definedExternally): Boolean

external fun createScanner(languageVersion: ScriptTarget, skipTrivia: Boolean, languageVariant: LanguageVariant = definedExternally, textInitial: String = definedExternally, onError: ErrorCallback = definedExternally, start: Number = definedExternally, length: Number = definedExternally): Scanner

external fun isExternalModuleNameRelative(moduleName: String): Boolean

external fun <T : Diagnostic> sortAndDeduplicateDiagnostics(diagnostics: Array<T>): SortedReadonlyArray<T>

external fun getDefaultLibFileName(options: CompilerOptions): String

external fun textSpanEnd(span: TextSpan): Number

external fun textSpanIsEmpty(span: TextSpan): Boolean

external fun textSpanContainsPosition(span: TextSpan, position: Number): Boolean

external fun textSpanContainsTextSpan(span: TextSpan, other: TextSpan): Boolean

external fun textSpanOverlapsWith(span: TextSpan, other: TextSpan): Boolean

external fun textSpanOverlap(span1: TextSpan, span2: TextSpan): TextSpan?

external fun textSpanIntersectsWithTextSpan(span: TextSpan, other: TextSpan): Boolean

external fun textSpanIntersectsWith(span: TextSpan, start: Number, length: Number): Boolean

external fun decodedTextSpanIntersectsWith(start1: Number, length1: Number, start2: Number, length2: Number): Boolean

external fun textSpanIntersectsWithPosition(span: TextSpan, position: Number): Boolean

external fun textSpanIntersection(span1: TextSpan, span2: TextSpan): TextSpan?

external fun createTextSpan(start: Number, length: Number): TextSpan

external fun createTextSpanFromBounds(start: Number, end: Number): TextSpan

external fun textChangeRangeNewSpan(range: TextChangeRange): TextSpan

external fun textChangeRangeIsUnchanged(range: TextChangeRange): Boolean

external fun createTextChangeRange(span: TextSpan, newLength: Number): TextChangeRange

external var unchangedTextChangeRange: TextChangeRange

external fun collapseTextChangeRangesAcrossMultipleVersions(changes: Array<TextChangeRange>): TextChangeRange

external fun getTypeParameterOwner(d: Declaration): Declaration?

external interface `T$9` {
  var parent: ConstructorDeclaration
  var name: Identifier
}

external fun isParameterPropertyDeclaration(node: Node, parent: Node): Boolean

external fun isEmptyBindingPattern(node: Identifier): Boolean

external fun isEmptyBindingPattern(node: ObjectBindingPattern): Boolean

external fun isEmptyBindingPattern(node: ArrayBindingPattern): Boolean

external fun isEmptyBindingElement(node: BindingElement): Boolean

external fun walkUpBindingElementsAndPatterns(binding: BindingElement): dynamic /* VariableDeclaration | ParameterDeclaration */

external fun getCombinedModifierFlags(node: Declaration): ModifierFlags

external fun getCombinedNodeFlags(node: Node): NodeFlags

external interface `T$10` {
  fun getExecutingFilePath(): String
  fun resolvePath(path: String): String
  fun fileExists(fileName: String): Boolean
  fun readFile(fileName: String): String?
}

external fun validateLocaleAndSetLanguage(locale: String, sys: `T$10`, errors: Push<Diagnostic> = definedExternally)

external fun getOriginalNode(node: Node): Node

external fun <T : Node> getOriginalNode(node: Node, nodeTest: (node: Node) -> Boolean): T

external fun getOriginalNode(node: Node?): Node?

external fun <T : Node> getOriginalNode(node: Node?, nodeTest: (node: Node?) -> Boolean): T?

external fun <T : Node> findAncestor(node: Node?, callback: (element: Node) -> Boolean): T?

external fun findAncestor(node: Node?, callback: (element: Node) -> Any): Node?

external fun isParseTreeNode(node: Node): Boolean

external fun getParseTreeNode(node: Node?): Node?

external fun <T : Node> getParseTreeNode(node: T?, nodeTest: (node: Node) -> Boolean = definedExternally): T?

external fun <T : Node> getParseTreeNode(node: T?): T?

external fun escapeLeadingUnderscores(identifier: String): dynamic /* String & `T$8` | Unit & `T$8` | InternalSymbolName */

external fun unescapeLeadingUnderscores(identifier: String /* String & `T$8` */): String

external fun unescapeLeadingUnderscores(identifier: Unit /* Unit & `T$8` */): String

external fun unescapeLeadingUnderscores(identifier: InternalSymbolName): String

external fun idText(identifierOrPrivateName: Identifier): String

external fun idText(identifierOrPrivateName: PrivateIdentifier): String

external fun symbolName(symbol: Symbol): String

external fun getNameOfJSDocTypedef(declaration: JSDocTypedefTag): dynamic /* Identifier? | PrivateIdentifier? */

external fun getNameOfDeclaration(declaration: Declaration?): dynamic /* Identifier? | PrivateIdentifier? | StringLiteral? | NoSubstitutionTemplateLiteral? | NumericLiteral? | ComputedPropertyName? | ElementAccessExpression? | ObjectBindingPattern? | ArrayBindingPattern? | PropertyAccessEntityNameExpression? */

external fun getNameOfDeclaration(declaration: Expression?): dynamic /* Identifier? | PrivateIdentifier? | StringLiteral? | NoSubstitutionTemplateLiteral? | NumericLiteral? | ComputedPropertyName? | ElementAccessExpression? | ObjectBindingPattern? | ArrayBindingPattern? | PropertyAccessEntityNameExpression? */

external fun getJSDocParameterTags(param: ParameterDeclaration): Array<JSDocParameterTag>

external fun getJSDocTypeParameterTags(param: TypeParameterDeclaration): Array<JSDocTemplateTag>

external fun hasJSDocParameterTags(node: FunctionDeclaration): Boolean

external fun hasJSDocParameterTags(node: MethodDeclaration): Boolean

external fun hasJSDocParameterTags(node: GetAccessorDeclaration): Boolean

external fun hasJSDocParameterTags(node: SetAccessorDeclaration): Boolean

external fun hasJSDocParameterTags(node: ConstructorDeclaration): Boolean

external fun hasJSDocParameterTags(node: FunctionExpression): Boolean

external fun hasJSDocParameterTags(node: ArrowFunction): Boolean

external fun hasJSDocParameterTags(node: CallSignatureDeclaration): Boolean

external fun hasJSDocParameterTags(node: ConstructSignatureDeclaration): Boolean

external fun hasJSDocParameterTags(node: MethodSignature): Boolean

external fun hasJSDocParameterTags(node: IndexSignatureDeclaration): Boolean

external fun hasJSDocParameterTags(node: FunctionTypeNode): Boolean

external fun hasJSDocParameterTags(node: ConstructorTypeNode): Boolean

external fun hasJSDocParameterTags(node: JSDocFunctionType): Boolean

external fun getJSDocAugmentsTag(node: Node): JSDocAugmentsTag?

external fun getJSDocImplementsTags(node: Node): Array<JSDocImplementsTag>

external fun getJSDocClassTag(node: Node): JSDocClassTag?

external fun getJSDocPublicTag(node: Node): JSDocPublicTag?

external fun getJSDocPrivateTag(node: Node): JSDocPrivateTag?

external fun getJSDocProtectedTag(node: Node): JSDocProtectedTag?

external fun getJSDocReadonlyTag(node: Node): JSDocReadonlyTag?

external fun getJSDocOverrideTagNoCache(node: Node): JSDocOverrideTag?

external fun getJSDocDeprecatedTag(node: Node): JSDocDeprecatedTag?

external fun getJSDocEnumTag(node: Node): JSDocEnumTag?

external fun getJSDocThisTag(node: Node): JSDocThisTag?

external fun getJSDocReturnTag(node: Node): JSDocReturnTag?

external fun getJSDocTemplateTag(node: Node): JSDocTemplateTag?

external fun getJSDocTypeTag(node: Node): JSDocTypeTag?

external fun getJSDocType(node: Node): TypeNode?

external fun getJSDocReturnType(node: Node): TypeNode?

external fun getJSDocTags(node: Node): Array<JSDocTag>

external fun <T : JSDocTag> getAllJSDocTags(node: Node, predicate: (tag: JSDocTag) -> Boolean): Array<T>

external fun getAllJSDocTagsOfKind(node: Node, kind: SyntaxKind): Array<JSDocTag>

external fun getTextOfJSDocComment(comment: String = definedExternally): String?

external fun getTextOfJSDocComment(): String?

external fun getTextOfJSDocComment(comment: NodeArray<Any /* JSDocText | JSDocLink */> = definedExternally): String?

external fun getEffectiveTypeParameterDeclarations(node: Any /* CallSignatureDeclaration | ConstructSignatureDeclaration | MethodSignature | IndexSignatureDeclaration | FunctionTypeNode | ConstructorTypeNode | JSDocFunctionType | FunctionDeclaration | MethodDeclaration | ConstructorDeclaration | GetAccessorDeclaration | SetAccessorDeclaration | FunctionExpression | ArrowFunction | ClassDeclaration | ClassExpression | InterfaceDeclaration | TypeAliasDeclaration | JSDocTemplateTag | JSDocTypedefTag | JSDocCallbackTag | JSDocSignature */): Array<TypeParameterDeclaration>

external fun getEffectiveConstraintOfTypeParameter(node: TypeParameterDeclaration): TypeNode?

external fun isMemberName(node: Node): Boolean

external fun isPropertyAccessChain(node: Node): Boolean

external fun isElementAccessChain(node: Node): Boolean

external fun isCallChain(node: Node): Boolean

external fun isOptionalChain(node: Node): Boolean

external fun isNullishCoalesce(node: Node): Boolean

external fun isConstTypeReference(node: Node): Boolean

external fun skipPartiallyEmittedExpressions(node: Expression): Expression

external fun skipPartiallyEmittedExpressions(node: Node): Node

external fun isNonNullChain(node: Node): Boolean

external fun isBreakOrContinueStatement(node: Node): Boolean

external fun isNamedExportBindings(node: Node): Boolean

external fun isUnparsedTextLike(node: Node): Boolean

external fun isUnparsedNode(node: Node): Boolean

external fun isJSDocPropertyLikeTag(node: Node): Boolean

external fun isTokenKind(kind: SyntaxKind): Boolean

external fun isToken(n: Node): Boolean

external fun isLiteralExpression(node: Node): Boolean

external fun isTemplateLiteralToken(node: Node): Boolean

external fun isTemplateMiddleOrTemplateTail(node: Node): Boolean

external fun isImportOrExportSpecifier(node: Node): Boolean

external fun isTypeOnlyImportOrExportDeclaration(node: Node): Boolean

external fun isStringTextContainingNode(node: Node): Boolean

external fun isModifier(node: Node): Boolean

external fun isEntityName(node: Node): Boolean

external fun isPropertyName(node: Node): Boolean

external fun isBindingName(node: Node): Boolean

external fun isFunctionLike(node: Node?): Boolean

external fun isClassElement(node: Node): Boolean

external fun isClassLike(node: Node): Boolean

external fun isAccessor(node: Node): Boolean

external fun isTypeElement(node: Node): Boolean

external fun isClassOrTypeElement(node: Node): Boolean

external fun isObjectLiteralElementLike(node: Node): Boolean

external fun isTypeNode(node: Node): Boolean

external fun isFunctionOrConstructorTypeNode(node: Node): Boolean

external fun isPropertyAccessOrQualifiedName(node: Node): Boolean

external fun isCallLikeExpression(node: Node): Boolean

external fun isCallOrNewExpression(node: Node): Boolean

external fun isTemplateLiteral(node: Node): Boolean

external fun isAssertionExpression(node: Node): Boolean

external fun isIterationStatement(node: Node, lookInLabeledStatements: Boolean): Boolean

external fun isJsxOpeningLikeElement(node: Node): Boolean

external fun isCaseOrDefaultClause(node: Node): Boolean

external fun isJSDocCommentContainingNode(node: Node): Boolean

external fun isSetAccessor(node: Node): Boolean

external fun isGetAccessor(node: Node): Boolean

external fun hasOnlyExpressionInitializer(node: Node): Boolean

external fun isObjectLiteralElement(node: Node): Boolean

external fun isStringLiteralLike(node: Node): Boolean

external var factory: NodeFactory

external fun createUnparsedSourceFile(text: String): UnparsedSource

external fun createUnparsedSourceFile(inputFile: InputFiles, type: String /* "js" | "dts" */, stripInternal: Boolean = definedExternally): UnparsedSource

external fun createUnparsedSourceFile(inputFile: InputFiles, type: String /* "js" | "dts" */): UnparsedSource

external fun createUnparsedSourceFile(text: String, mapPath: String?, map: String?): UnparsedSource

external fun createInputFiles(javascriptText: String, declarationText: String): InputFiles

external fun createInputFiles(readFileText: (path: String) -> String?, javascriptPath: String, javascriptMapPath: String?, declarationPath: String, declarationMapPath: String?, buildInfoPath: String?): InputFiles

external fun createInputFiles(javascriptText: String, declarationText: String, javascriptMapPath: String?, javascriptMapText: String?, declarationMapPath: String?, declarationMapText: String?): InputFiles

external fun createSourceMapSource(fileName: String, text: String, skipTrivia: (pos: Number) -> Number = definedExternally): SourceMapSource

external fun <T : Node> setOriginalNode(node: T, original: Node?): T

external fun disposeEmitNodes(sourceFile: SourceFile?)

external fun <T : Node> setEmitFlags(node: T, emitFlags: EmitFlags): T

external fun getSourceMapRange(node: Node): SourceMapRange

external fun <T : Node> setSourceMapRange(node: T, range: SourceMapRange?): T

external fun getTokenSourceMapRange(node: Node, token: SyntaxKind): SourceMapRange?

external fun <T : Node> setTokenSourceMapRange(node: T, token: SyntaxKind, range: SourceMapRange?): T

external fun getCommentRange(node: Node): TextRange

external fun <T : Node> setCommentRange(node: T, range: TextRange): T

external fun getSyntheticLeadingComments(node: Node): Array<SynthesizedComment>?

external fun <T : Node> setSyntheticLeadingComments(node: T, comments: Array<SynthesizedComment>?): T

external fun getSyntheticTrailingComments(node: Node): Array<SynthesizedComment>?

external fun <T : Node> setSyntheticTrailingComments(node: T, comments: Array<SynthesizedComment>?): T

external fun <T : Node> addSyntheticTrailingComment(node: T, kind: SyntaxKind = definedExternally, text: String, hasTrailingNewLine: Boolean = definedExternally): T

external fun <T : Node> addSyntheticTrailingComment(node: T, kind: SyntaxKind = definedExternally, text: String): T

external fun <T : Node> moveSyntheticComments(node: T, original: Node): T

external fun getConstantValue(node: PropertyAccessExpression): dynamic /* String? | Number? */

external fun getConstantValue(node: ElementAccessExpression): dynamic /* String? | Number? */

external fun setConstantValue(node: PropertyAccessExpression, value: String): dynamic /* PropertyAccessExpression | ElementAccessExpression */

external fun setConstantValue(node: PropertyAccessExpression, value: Number): dynamic /* PropertyAccessExpression | ElementAccessExpression */

external fun setConstantValue(node: ElementAccessExpression, value: String): dynamic /* PropertyAccessExpression | ElementAccessExpression */

external fun setConstantValue(node: ElementAccessExpression, value: Number): dynamic /* PropertyAccessExpression | ElementAccessExpression */

external fun <T : Node> addEmitHelper(node: T, helper: ScopedEmitHelper): T

external fun <T : Node> addEmitHelper(node: T, helper: UnscopedEmitHelper): T

external fun <T : Node> addEmitHelpers(node: T, helpers: Array<Any /* ScopedEmitHelper | UnscopedEmitHelper */>?): T

external fun removeEmitHelper(node: Node, helper: ScopedEmitHelper): Boolean

external fun removeEmitHelper(node: Node, helper: UnscopedEmitHelper): Boolean

external fun getEmitHelpers(node: Node): Array<dynamic /* ScopedEmitHelper | UnscopedEmitHelper */>?

external fun moveEmitHelpers(source: Node, target: Node, predicate: (helper: Any /* ScopedEmitHelper | UnscopedEmitHelper */) -> Boolean)

external fun isNumericLiteral(node: Node): Boolean

external fun isBigIntLiteral(node: Node): Boolean

external fun isStringLiteral(node: Node): Boolean

external fun isJsxText(node: Node): Boolean

external fun isRegularExpressionLiteral(node: Node): Boolean

external fun isNoSubstitutionTemplateLiteral(node: Node): Boolean

external fun isTemplateHead(node: Node): Boolean

external fun isTemplateMiddle(node: Node): Boolean

external fun isTemplateTail(node: Node): Boolean

external fun isDotDotDotToken(node: Node): Boolean

external fun isPlusToken(node: Node): Boolean

external fun isMinusToken(node: Node): Boolean

external fun isAsteriskToken(node: Node): Boolean

external fun isIdentifier(node: Node): Boolean

external fun isPrivateIdentifier(node: Node): Boolean

external fun isQualifiedName(node: Node): Boolean

external fun isComputedPropertyName(node: Node): Boolean

external fun isTypeParameterDeclaration(node: Node): Boolean

external fun isParameter(node: Node): Boolean

external fun isDecorator(node: Node): Boolean

external fun isPropertySignature(node: Node): Boolean

external fun isPropertyDeclaration(node: Node): Boolean

external fun isMethodSignature(node: Node): Boolean

external fun isMethodDeclaration(node: Node): Boolean

external fun isConstructorDeclaration(node: Node): Boolean

external fun isGetAccessorDeclaration(node: Node): Boolean

external fun isSetAccessorDeclaration(node: Node): Boolean

external fun isCallSignatureDeclaration(node: Node): Boolean

external fun isConstructSignatureDeclaration(node: Node): Boolean

external fun isIndexSignatureDeclaration(node: Node): Boolean

external fun isTypePredicateNode(node: Node): Boolean

external fun isTypeReferenceNode(node: Node): Boolean

external fun isFunctionTypeNode(node: Node): Boolean

external fun isConstructorTypeNode(node: Node): Boolean

external fun isTypeQueryNode(node: Node): Boolean

external fun isTypeLiteralNode(node: Node): Boolean

external fun isArrayTypeNode(node: Node): Boolean

external fun isTupleTypeNode(node: Node): Boolean

external fun isNamedTupleMember(node: Node): Boolean

external fun isOptionalTypeNode(node: Node): Boolean

external fun isRestTypeNode(node: Node): Boolean

external fun isUnionTypeNode(node: Node): Boolean

external fun isIntersectionTypeNode(node: Node): Boolean

external fun isConditionalTypeNode(node: Node): Boolean

external fun isInferTypeNode(node: Node): Boolean

external fun isParenthesizedTypeNode(node: Node): Boolean

external fun isThisTypeNode(node: Node): Boolean

external fun isTypeOperatorNode(node: Node): Boolean

external fun isIndexedAccessTypeNode(node: Node): Boolean

external fun isMappedTypeNode(node: Node): Boolean

external fun isLiteralTypeNode(node: Node): Boolean

external fun isImportTypeNode(node: Node): Boolean

external fun isTemplateLiteralTypeSpan(node: Node): Boolean

external fun isTemplateLiteralTypeNode(node: Node): Boolean

external fun isObjectBindingPattern(node: Node): Boolean

external fun isArrayBindingPattern(node: Node): Boolean

external fun isBindingElement(node: Node): Boolean

external fun isArrayLiteralExpression(node: Node): Boolean

external fun isObjectLiteralExpression(node: Node): Boolean

external fun isPropertyAccessExpression(node: Node): Boolean

external fun isElementAccessExpression(node: Node): Boolean

external fun isCallExpression(node: Node): Boolean

external fun isNewExpression(node: Node): Boolean

external fun isTaggedTemplateExpression(node: Node): Boolean

external fun isTypeAssertionExpression(node: Node): Boolean

external fun isParenthesizedExpression(node: Node): Boolean

external fun isFunctionExpression(node: Node): Boolean

external fun isArrowFunction(node: Node): Boolean

external fun isDeleteExpression(node: Node): Boolean

external fun isTypeOfExpression(node: Node): Boolean

external fun isVoidExpression(node: Node): Boolean

external fun isAwaitExpression(node: Node): Boolean

external fun isPrefixUnaryExpression(node: Node): Boolean

external fun isPostfixUnaryExpression(node: Node): Boolean

external fun isBinaryExpression(node: Node): Boolean

external fun isConditionalExpression(node: Node): Boolean

external fun isTemplateExpression(node: Node): Boolean

external fun isYieldExpression(node: Node): Boolean

external fun isSpreadElement(node: Node): Boolean

external fun isClassExpression(node: Node): Boolean

external fun isOmittedExpression(node: Node): Boolean

external fun isExpressionWithTypeArguments(node: Node): Boolean

external fun isAsExpression(node: Node): Boolean

external fun isNonNullExpression(node: Node): Boolean

external fun isMetaProperty(node: Node): Boolean

external fun isSyntheticExpression(node: Node): Boolean

external fun isPartiallyEmittedExpression(node: Node): Boolean

external fun isCommaListExpression(node: Node): Boolean

external fun isTemplateSpan(node: Node): Boolean

external fun isSemicolonClassElement(node: Node): Boolean

external fun isBlock(node: Node): Boolean

external fun isVariableStatement(node: Node): Boolean

external fun isEmptyStatement(node: Node): Boolean

external fun isExpressionStatement(node: Node): Boolean

external fun isIfStatement(node: Node): Boolean

external fun isDoStatement(node: Node): Boolean

external fun isWhileStatement(node: Node): Boolean

external fun isForStatement(node: Node): Boolean

external fun isForInStatement(node: Node): Boolean

external fun isForOfStatement(node: Node): Boolean

external fun isContinueStatement(node: Node): Boolean

external fun isBreakStatement(node: Node): Boolean

external fun isReturnStatement(node: Node): Boolean

external fun isWithStatement(node: Node): Boolean

external fun isSwitchStatement(node: Node): Boolean

external fun isLabeledStatement(node: Node): Boolean

external fun isThrowStatement(node: Node): Boolean

external fun isTryStatement(node: Node): Boolean

external fun isDebuggerStatement(node: Node): Boolean

external fun isVariableDeclaration(node: Node): Boolean

external fun isVariableDeclarationList(node: Node): Boolean

external fun isFunctionDeclaration(node: Node): Boolean

external fun isClassDeclaration(node: Node): Boolean

external fun isInterfaceDeclaration(node: Node): Boolean

external fun isTypeAliasDeclaration(node: Node): Boolean

external fun isEnumDeclaration(node: Node): Boolean

external fun isModuleDeclaration(node: Node): Boolean

external fun isModuleBlock(node: Node): Boolean

external fun isCaseBlock(node: Node): Boolean

external fun isNamespaceExportDeclaration(node: Node): Boolean

external fun isImportEqualsDeclaration(node: Node): Boolean

external fun isImportDeclaration(node: Node): Boolean

external fun isImportClause(node: Node): Boolean

external fun isNamespaceImport(node: Node): Boolean

external fun isNamespaceExport(node: Node): Boolean

external fun isNamedImports(node: Node): Boolean

external fun isImportSpecifier(node: Node): Boolean

external fun isExportAssignment(node: Node): Boolean

external fun isExportDeclaration(node: Node): Boolean

external fun isNamedExports(node: Node): Boolean

external fun isExportSpecifier(node: Node): Boolean

external fun isMissingDeclaration(node: Node): Boolean

external fun isNotEmittedStatement(node: Node): Boolean

external fun isExternalModuleReference(node: Node): Boolean

external fun isJsxElement(node: Node): Boolean

external fun isJsxSelfClosingElement(node: Node): Boolean

external fun isJsxOpeningElement(node: Node): Boolean

external fun isJsxClosingElement(node: Node): Boolean

external fun isJsxFragment(node: Node): Boolean

external fun isJsxOpeningFragment(node: Node): Boolean

external fun isJsxClosingFragment(node: Node): Boolean

external fun isJsxAttribute(node: Node): Boolean

external fun isJsxAttributes(node: Node): Boolean

external fun isJsxSpreadAttribute(node: Node): Boolean

external fun isJsxExpression(node: Node): Boolean

external fun isCaseClause(node: Node): Boolean

external fun isDefaultClause(node: Node): Boolean

external fun isHeritageClause(node: Node): Boolean

external fun isCatchClause(node: Node): Boolean

external fun isPropertyAssignment(node: Node): Boolean

external fun isShorthandPropertyAssignment(node: Node): Boolean

external fun isSpreadAssignment(node: Node): Boolean

external fun isEnumMember(node: Node): Boolean

external fun isUnparsedPrepend(node: Node): Boolean

external fun isSourceFile(node: Node): Boolean

external fun isBundle(node: Node): Boolean

external fun isUnparsedSource(node: Node): Boolean

external fun isJSDocTypeExpression(node: Node): Boolean

external fun isJSDocNameReference(node: Node): Boolean

external fun isJSDocLink(node: Node): Boolean

external fun isJSDocAllType(node: Node): Boolean

external fun isJSDocUnknownType(node: Node): Boolean

external fun isJSDocNullableType(node: Node): Boolean

external fun isJSDocNonNullableType(node: Node): Boolean

external fun isJSDocOptionalType(node: Node): Boolean

external fun isJSDocFunctionType(node: Node): Boolean

external fun isJSDocVariadicType(node: Node): Boolean

external fun isJSDocNamepathType(node: Node): Boolean

external fun isJSDoc(node: Node): Boolean

external fun isJSDocTypeLiteral(node: Node): Boolean

external fun isJSDocSignature(node: Node): Boolean

external fun isJSDocAugmentsTag(node: Node): Boolean

external fun isJSDocAuthorTag(node: Node): Boolean

external fun isJSDocClassTag(node: Node): Boolean

external fun isJSDocCallbackTag(node: Node): Boolean

external fun isJSDocPublicTag(node: Node): Boolean

external fun isJSDocPrivateTag(node: Node): Boolean

external fun isJSDocProtectedTag(node: Node): Boolean

external fun isJSDocReadonlyTag(node: Node): Boolean

external fun isJSDocOverrideTag(node: Node): Boolean

external fun isJSDocDeprecatedTag(node: Node): Boolean

external fun isJSDocSeeTag(node: Node): Boolean

external fun isJSDocEnumTag(node: Node): Boolean

external fun isJSDocParameterTag(node: Node): Boolean

external fun isJSDocReturnTag(node: Node): Boolean

external fun isJSDocThisTag(node: Node): Boolean

external fun isJSDocTypeTag(node: Node): Boolean

external fun isJSDocTemplateTag(node: Node): Boolean

external fun isJSDocTypedefTag(node: Node): Boolean

external fun isJSDocUnknownTag(node: Node): Boolean

external fun isJSDocPropertyTag(node: Node): Boolean

external fun isJSDocImplementsTag(node: Node): Boolean

external fun <T : TextRange> setTextRange(range: T, location: TextRange?): T

external fun <T> forEachChild(node: Node, cbNode: (node: Node) -> T?, cbNodes: (nodes: NodeArray<Node>) -> T? = definedExternally): T?

external fun createSourceFile(fileName: String, sourceText: String, languageVersion: ScriptTarget, setParentNodes: Boolean = definedExternally, scriptKind: ScriptKind = definedExternally): SourceFile

external fun parseIsolatedEntityName(text: String, languageVersion: ScriptTarget): dynamic /* Identifier? | QualifiedName? */

external fun parseJsonText(fileName: String, sourceText: String): JsonSourceFile

external fun isExternalModule(file: SourceFile): Boolean

external fun updateSourceFile(sourceFile: SourceFile, newText: String, textChangeRange: TextChangeRange, aggressiveChecks: Boolean = definedExternally): SourceFile

external fun parseCommandLine(commandLine: Array<String>, readFile: (path: String) -> String? = definedExternally): ParsedCommandLine

external interface ConfigFileDiagnosticsReporter {
  var onUnRecoverableConfigFileDiagnostic: DiagnosticReporter
}

external interface ParseConfigFileHost : ParseConfigHost, ConfigFileDiagnosticsReporter {
  fun getCurrentDirectory(): String
}

external fun getParsedCommandLineOfConfigFile(configFileName: String, optionsToExtend: CompilerOptions?, host: ParseConfigFileHost, extendedConfigCache: Map<ExtendedConfigCacheEntry> = definedExternally, watchOptionsToExtend: WatchOptions = definedExternally, extraFileExtensions: Array<FileExtensionInfo> = definedExternally): ParsedCommandLine?

external interface `T$11` {
  var config: Any?
    get() = definedExternally
    set(value) = definedExternally
  var error: Diagnostic?
    get() = definedExternally
    set(value) = definedExternally
}

external fun readConfigFile(fileName: String, readFile: (path: String) -> String?): `T$11`

external fun parseConfigFileTextToJson(fileName: String, jsonText: String): `T$11`

external fun readJsonConfigFile(fileName: String, readFile: (path: String) -> String?): TsConfigSourceFile

external fun convertToObject(sourceFile: JsonSourceFile, errors: Push<Diagnostic>): Any

external fun parseJsonConfigFileContent(json: Any, host: ParseConfigHost, basePath: String, existingOptions: CompilerOptions = definedExternally, configFileName: String = definedExternally, resolutionStack: Array<String /* String & `T$3` */> = definedExternally, extraFileExtensions: Array<FileExtensionInfo> = definedExternally, extendedConfigCache: Map<ExtendedConfigCacheEntry> = definedExternally, existingWatchOptions: WatchOptions = definedExternally): ParsedCommandLine

external fun parseJsonSourceFileConfigFileContent(sourceFile: TsConfigSourceFile, host: ParseConfigHost, basePath: String, existingOptions: CompilerOptions = definedExternally, configFileName: String = definedExternally, resolutionStack: Array<String /* String & `T$3` */> = definedExternally, extraFileExtensions: Array<FileExtensionInfo> = definedExternally, extendedConfigCache: Map<ExtendedConfigCacheEntry> = definedExternally, existingWatchOptions: WatchOptions = definedExternally): ParsedCommandLine

external interface ParsedTsconfig {
  var raw: Any
  var options: CompilerOptions?
    get() = definedExternally
    set(value) = definedExternally
  var watchOptions: WatchOptions?
    get() = definedExternally
    set(value) = definedExternally
  var typeAcquisition: TypeAcquisition?
    get() = definedExternally
    set(value) = definedExternally
  var extendedConfigPath: String?
    get() = definedExternally
    set(value) = definedExternally
}

external interface ExtendedConfigCacheEntry {
  var extendedResult: TsConfigSourceFile
  var extendedConfig: ParsedTsconfig?
}

external interface `T$12` {
  var options: CompilerOptions
  var errors: Array<Diagnostic>
}

external fun convertCompilerOptionsFromJson(jsonOptions: Any, basePath: String, configFileName: String = definedExternally): `T$12`

external interface `T$13` {
  var options: TypeAcquisition
  var errors: Array<Diagnostic>
}

external fun convertTypeAcquisitionFromJson(jsonOptions: Any, basePath: String, configFileName: String = definedExternally): `T$13`

external fun getEffectiveTypeRoots(options: CompilerOptions, host: GetEffectiveTypeRootsHost): Array<String>?

external fun resolveTypeReferenceDirective(typeReferenceDirectiveName: String, containingFile: String?, options: CompilerOptions, host: ModuleResolutionHost, redirectedReference: ResolvedProjectReference = definedExternally, cache: TypeReferenceDirectiveResolutionCache = definedExternally): ResolvedTypeReferenceDirectiveWithFailedLookupLocations

external fun getAutomaticTypeDirectiveNames(options: CompilerOptions, host: ModuleResolutionHost): Array<String>

external interface TypeReferenceDirectiveResolutionCache : PerDirectoryResolutionCache<ResolvedTypeReferenceDirectiveWithFailedLookupLocations>, PackageJsonInfoCache

external interface PerDirectoryResolutionCache<T> {
  fun getOrCreateCacheForDirectory(directoryName: String, redirectedReference: ResolvedProjectReference = definedExternally): Map<T>
  fun clear()
  fun update(options: CompilerOptions)
}

external interface ModuleResolutionCache : PerDirectoryResolutionCache<ResolvedModuleWithFailedLookupLocations>, NonRelativeModuleNameResolutionCache, PackageJsonInfoCache {
  fun getPackageJsonInfoCache(): PackageJsonInfoCache
}

external interface NonRelativeModuleNameResolutionCache : PackageJsonInfoCache {
  fun getOrCreateCacheForModuleName(nonRelativeModuleName: String, redirectedReference: ResolvedProjectReference = definedExternally): PerModuleNameCache
}

external interface PackageJsonInfoCache {
  fun clear()
}

external interface PerModuleNameCache {
  fun get(directory: String): ResolvedModuleWithFailedLookupLocations?
  fun set(directory: String, result: ResolvedModuleWithFailedLookupLocations)
}

external fun createModuleResolutionCache(currentDirectory: String, getCanonicalFileName: (s: String) -> String, options: CompilerOptions = definedExternally): ModuleResolutionCache

external fun createTypeReferenceDirectiveResolutionCache(currentDirectory: String, getCanonicalFileName: (s: String) -> String, options: CompilerOptions = definedExternally, packageJsonInfoCache: PackageJsonInfoCache = definedExternally): TypeReferenceDirectiveResolutionCache

external fun resolveModuleNameFromCache(moduleName: String, containingFile: String, cache: ModuleResolutionCache): ResolvedModuleWithFailedLookupLocations?

external fun resolveModuleName(moduleName: String, containingFile: String, compilerOptions: CompilerOptions, host: ModuleResolutionHost, cache: ModuleResolutionCache = definedExternally, redirectedReference: ResolvedProjectReference = definedExternally): ResolvedModuleWithFailedLookupLocations

external fun nodeModuleNameResolver(moduleName: String, containingFile: String, compilerOptions: CompilerOptions, host: ModuleResolutionHost, cache: ModuleResolutionCache = definedExternally, redirectedReference: ResolvedProjectReference = definedExternally): ResolvedModuleWithFailedLookupLocations

external fun classicNameResolver(moduleName: String, containingFile: String, compilerOptions: CompilerOptions, host: ModuleResolutionHost, cache: NonRelativeModuleNameResolutionCache = definedExternally, redirectedReference: ResolvedProjectReference = definedExternally): ResolvedModuleWithFailedLookupLocations

external fun <T : Node> visitNode(node: T, visitor: Visitor?, test: (node: Node) -> Boolean = definedExternally, lift: (node: Array<Node>) -> T = definedExternally): T

external fun <T : Node> visitNode(node: T, visitor: Visitor?): T

external fun <T : Node> visitNode(node: T, visitor: Visitor?, test: (node: Node) -> Boolean = definedExternally): T

external fun <T : Node> visitNode(node: T?, visitor: Visitor?, test: (node: Node) -> Boolean = definedExternally, lift: (node: Array<Node>) -> T = definedExternally): T?

external fun <T : Node> visitNode(node: T?, visitor: Visitor?): T?

external fun <T : Node> visitNode(node: T?, visitor: Visitor?, test: (node: Node) -> Boolean = definedExternally): T?

external fun <T : Node> visitNodes(nodes: NodeArray<T>, visitor: Visitor?, test: (node: Node) -> Boolean = definedExternally, start: Number = definedExternally, count: Number = definedExternally): NodeArray<T>

external fun <T : Node> visitNodes(nodes: NodeArray<T>, visitor: Visitor?): NodeArray<T>

external fun <T : Node> visitNodes(nodes: NodeArray<T>, visitor: Visitor?, test: (node: Node) -> Boolean = definedExternally): NodeArray<T>

external fun <T : Node> visitNodes(nodes: NodeArray<T>, visitor: Visitor?, test: (node: Node) -> Boolean = definedExternally, start: Number = definedExternally): NodeArray<T>

external fun <T : Node> visitNodes(nodes: NodeArray<T>?, visitor: Visitor?, test: (node: Node) -> Boolean = definedExternally, start: Number = definedExternally, count: Number = definedExternally): NodeArray<T>?

external fun <T : Node> visitNodes(nodes: NodeArray<T>?, visitor: Visitor?): NodeArray<T>?

external fun <T : Node> visitNodes(nodes: NodeArray<T>?, visitor: Visitor?, test: (node: Node) -> Boolean = definedExternally): NodeArray<T>?

external fun <T : Node> visitNodes(nodes: NodeArray<T>?, visitor: Visitor?, test: (node: Node) -> Boolean = definedExternally, start: Number = definedExternally): NodeArray<T>?

external fun visitLexicalEnvironment(statements: NodeArray<Statement>, visitor: Visitor, context: TransformationContext, start: Number = definedExternally, ensureUseStrict: Boolean = definedExternally, nodesVisitor: NodesVisitor = definedExternally): NodeArray<Statement>

external fun visitParameterList(nodes: NodeArray<ParameterDeclaration>, visitor: Visitor, context: TransformationContext, nodesVisitor: NodesVisitor = definedExternally): NodeArray<ParameterDeclaration>

external fun visitParameterList(nodes: NodeArray<ParameterDeclaration>, visitor: Visitor, context: TransformationContext): NodeArray<ParameterDeclaration>

external fun visitParameterList(nodes: NodeArray<ParameterDeclaration>?, visitor: Visitor, context: TransformationContext, nodesVisitor: NodesVisitor = definedExternally): NodeArray<ParameterDeclaration>?

external fun visitParameterList(nodes: NodeArray<ParameterDeclaration>?, visitor: Visitor, context: TransformationContext): NodeArray<ParameterDeclaration>?

external fun visitFunctionBody(node: FunctionBody, visitor: Visitor, context: TransformationContext): dynamic /* FunctionBody */

external fun visitFunctionBody(node: Expression, visitor: Visitor, context: TransformationContext): dynamic /* FunctionBody | Expression */

external fun visitIterationBody(body: Statement, visitor: Visitor, context: TransformationContext): Statement

external fun <T : Node> visitEachChild(node: T, visitor: Visitor, context: TransformationContext): T

external fun <T : Node> visitEachChild(node: T?, visitor: Visitor, context: TransformationContext, nodesVisitor: Any = definedExternally, tokenVisitor: Visitor = definedExternally): T?

external fun <T : Node> visitEachChild(node: T?, visitor: Visitor, context: TransformationContext): T?

external fun <T : Node> visitEachChild(node: T?, visitor: Visitor, context: TransformationContext, nodesVisitor: Any = definedExternally): T?

external fun getTsBuildInfoEmitOutputFilePath(options: CompilerOptions): String?

external fun getOutputFileNames(commandLine: ParsedCommandLine, inputFileName: String, ignoreCase: Boolean): Array<String>

external fun createPrinter(printerOptions: PrinterOptions = definedExternally, handlers: PrintHandlers = definedExternally): Printer

external fun findConfigFile(searchPath: String, fileExists: (fileName: String) -> Boolean, configName: String = definedExternally): String?

external fun resolveTripleslashReference(moduleName: String, containingFile: String): String

external fun createCompilerHost(options: CompilerOptions, setParentNodes: Boolean = definedExternally): CompilerHost

external fun getPreEmitDiagnostics(program: Program, sourceFile: SourceFile = definedExternally, cancellationToken: CancellationToken = definedExternally): Array<Diagnostic>

external interface FormatDiagnosticsHost {
  fun getCurrentDirectory(): String
  fun getCanonicalFileName(fileName: String): String
  fun getNewLine(): String
}

external fun formatDiagnostics(diagnostics: Array<Diagnostic>, host: FormatDiagnosticsHost): String

external fun formatDiagnostic(diagnostic: Diagnostic, host: FormatDiagnosticsHost): String

external fun formatDiagnosticsWithColorAndContext(diagnostics: Array<Diagnostic>, host: FormatDiagnosticsHost): String

external fun flattenDiagnosticMessageText(diag: String?, newLine: String, indent: Number = definedExternally): String

external fun flattenDiagnosticMessageText(diag: String?, newLine: String): String

external fun flattenDiagnosticMessageText(diag: DiagnosticMessageChain?, newLine: String, indent: Number = definedExternally): String

external fun flattenDiagnosticMessageText(diag: DiagnosticMessageChain?, newLine: String): String

external fun getConfigFileParsingDiagnostics(configFileParseResult: ParsedCommandLine): Array<Diagnostic>

external fun createProgram(createProgramOptions: CreateProgramOptions): Program

external fun createProgram(rootNames: Array<String>, options: CompilerOptions, host: CompilerHost = definedExternally, oldProgram: Program = definedExternally, configFileParsingDiagnostics: Array<Diagnostic> = definedExternally): Program

external fun createProgram(rootNames: Array<String>, options: CompilerOptions): Program

external fun createProgram(rootNames: Array<String>, options: CompilerOptions, host: CompilerHost = definedExternally): Program

external fun createProgram(rootNames: Array<String>, options: CompilerOptions, host: CompilerHost = definedExternally, oldProgram: Program = definedExternally): Program

external interface ResolveProjectReferencePathHost {
  fun fileExists(fileName: String): Boolean
}

external fun resolveProjectReferencePath(ref: ProjectReference): String /* String & `T$5` */

external fun resolveProjectReferencePath(host: ResolveProjectReferencePathHost, ref: ProjectReference): String /* String & `T$5` */

external interface EmitOutput {
  var outputFiles: Array<OutputFile>
  var emitSkipped: Boolean?
}

external interface OutputFile {
  var name: String
  var writeByteOrderMark: Boolean?
  var text: String
}

external interface `T$14`<T> {
  var result: T
  var affected: dynamic /* SourceFile | Program */
    get() = definedExternally
    set(value) = definedExternally
}

external interface BuilderProgramHost {
  fun useCaseSensitiveFileNames(): Boolean
  var createHash: ((data: String) -> String)?
    get() = definedExternally
    set(value) = definedExternally
  var writeFile: WriteFileCallback?
    get() = definedExternally
    set(value) = definedExternally
}

external interface BuilderProgram {
  fun getProgram(): Program
  fun getCompilerOptions(): CompilerOptions
  fun getSourceFile(fileName: String): SourceFile?
  fun getSourceFiles(): Array<SourceFile>
  fun getOptionsDiagnostics(cancellationToken: CancellationToken = definedExternally): Array<Diagnostic>
  fun getGlobalDiagnostics(cancellationToken: CancellationToken = definedExternally): Array<Diagnostic>
  fun getConfigFileParsingDiagnostics(): Array<Diagnostic>
  fun getSyntacticDiagnostics(sourceFile: SourceFile = definedExternally, cancellationToken: CancellationToken = definedExternally): Array<Diagnostic>
  fun getDeclarationDiagnostics(sourceFile: SourceFile = definedExternally, cancellationToken: CancellationToken = definedExternally): Array<DiagnosticWithLocation>
  fun getAllDependencies(sourceFile: SourceFile): Array<String>
  fun getSemanticDiagnostics(sourceFile: SourceFile = definedExternally, cancellationToken: CancellationToken = definedExternally): Array<Diagnostic>
  fun emit(targetSourceFile: SourceFile = definedExternally, writeFile: WriteFileCallback = definedExternally, cancellationToken: CancellationToken = definedExternally, emitOnlyDtsFiles: Boolean = definedExternally, customTransformers: CustomTransformers = definedExternally): EmitResult
  fun getCurrentDirectory(): String
}

external interface SemanticDiagnosticsBuilderProgram : BuilderProgram {
  fun getSemanticDiagnosticsOfNextAffectedFile(cancellationToken: CancellationToken = definedExternally, ignoreSourceFile: (sourceFile: SourceFile) -> Boolean = definedExternally): `T$14`<Array<Diagnostic>>?
}

external interface EmitAndSemanticDiagnosticsBuilderProgram : SemanticDiagnosticsBuilderProgram {
  fun emitNextAffectedFile(writeFile: WriteFileCallback = definedExternally, cancellationToken: CancellationToken = definedExternally, emitOnlyDtsFiles: Boolean = definedExternally, customTransformers: CustomTransformers = definedExternally): `T$14`<EmitResult>?
}

external fun createSemanticDiagnosticsBuilderProgram(newProgram: Program, host: BuilderProgramHost, oldProgram: SemanticDiagnosticsBuilderProgram = definedExternally, configFileParsingDiagnostics: Array<Diagnostic> = definedExternally): SemanticDiagnosticsBuilderProgram

external fun createSemanticDiagnosticsBuilderProgram(newProgram: Program, host: BuilderProgramHost): SemanticDiagnosticsBuilderProgram

external fun createSemanticDiagnosticsBuilderProgram(newProgram: Program, host: BuilderProgramHost, oldProgram: SemanticDiagnosticsBuilderProgram = definedExternally): SemanticDiagnosticsBuilderProgram

external fun createSemanticDiagnosticsBuilderProgram(rootNames: Array<String>?, options: CompilerOptions?, host: CompilerHost = definedExternally, oldProgram: SemanticDiagnosticsBuilderProgram = definedExternally, configFileParsingDiagnostics: Array<Diagnostic> = definedExternally, projectReferences: Array<ProjectReference> = definedExternally): SemanticDiagnosticsBuilderProgram

external fun createSemanticDiagnosticsBuilderProgram(rootNames: Array<String>?, options: CompilerOptions?): SemanticDiagnosticsBuilderProgram

external fun createSemanticDiagnosticsBuilderProgram(rootNames: Array<String>?, options: CompilerOptions?, host: CompilerHost = definedExternally): SemanticDiagnosticsBuilderProgram

external fun createSemanticDiagnosticsBuilderProgram(rootNames: Array<String>?, options: CompilerOptions?, host: CompilerHost = definedExternally, oldProgram: SemanticDiagnosticsBuilderProgram = definedExternally): SemanticDiagnosticsBuilderProgram

external fun createSemanticDiagnosticsBuilderProgram(rootNames: Array<String>?, options: CompilerOptions?, host: CompilerHost = definedExternally, oldProgram: SemanticDiagnosticsBuilderProgram = definedExternally, configFileParsingDiagnostics: Array<Diagnostic> = definedExternally): SemanticDiagnosticsBuilderProgram

external fun createEmitAndSemanticDiagnosticsBuilderProgram(newProgram: Program, host: BuilderProgramHost, oldProgram: EmitAndSemanticDiagnosticsBuilderProgram = definedExternally, configFileParsingDiagnostics: Array<Diagnostic> = definedExternally): EmitAndSemanticDiagnosticsBuilderProgram

external fun createEmitAndSemanticDiagnosticsBuilderProgram(newProgram: Program, host: BuilderProgramHost): EmitAndSemanticDiagnosticsBuilderProgram

external fun createEmitAndSemanticDiagnosticsBuilderProgram(newProgram: Program, host: BuilderProgramHost, oldProgram: EmitAndSemanticDiagnosticsBuilderProgram = definedExternally): EmitAndSemanticDiagnosticsBuilderProgram

external fun createEmitAndSemanticDiagnosticsBuilderProgram(rootNames: Array<String>?, options: CompilerOptions?, host: CompilerHost = definedExternally, oldProgram: EmitAndSemanticDiagnosticsBuilderProgram = definedExternally, configFileParsingDiagnostics: Array<Diagnostic> = definedExternally, projectReferences: Array<ProjectReference> = definedExternally): EmitAndSemanticDiagnosticsBuilderProgram

external fun createEmitAndSemanticDiagnosticsBuilderProgram(rootNames: Array<String>?, options: CompilerOptions?): EmitAndSemanticDiagnosticsBuilderProgram

external fun createEmitAndSemanticDiagnosticsBuilderProgram(rootNames: Array<String>?, options: CompilerOptions?, host: CompilerHost = definedExternally): EmitAndSemanticDiagnosticsBuilderProgram

external fun createEmitAndSemanticDiagnosticsBuilderProgram(rootNames: Array<String>?, options: CompilerOptions?, host: CompilerHost = definedExternally, oldProgram: EmitAndSemanticDiagnosticsBuilderProgram = definedExternally): EmitAndSemanticDiagnosticsBuilderProgram

external fun createEmitAndSemanticDiagnosticsBuilderProgram(rootNames: Array<String>?, options: CompilerOptions?, host: CompilerHost = definedExternally, oldProgram: EmitAndSemanticDiagnosticsBuilderProgram = definedExternally, configFileParsingDiagnostics: Array<Diagnostic> = definedExternally): EmitAndSemanticDiagnosticsBuilderProgram

external fun createAbstractBuilder(newProgram: Program, host: BuilderProgramHost, oldProgram: BuilderProgram = definedExternally, configFileParsingDiagnostics: Array<Diagnostic> = definedExternally): BuilderProgram

external fun createAbstractBuilder(newProgram: Program, host: BuilderProgramHost): BuilderProgram

external fun createAbstractBuilder(newProgram: Program, host: BuilderProgramHost, oldProgram: BuilderProgram = definedExternally): BuilderProgram

external fun createAbstractBuilder(rootNames: Array<String>?, options: CompilerOptions?, host: CompilerHost = definedExternally, oldProgram: BuilderProgram = definedExternally, configFileParsingDiagnostics: Array<Diagnostic> = definedExternally, projectReferences: Array<ProjectReference> = definedExternally): BuilderProgram

external fun createAbstractBuilder(rootNames: Array<String>?, options: CompilerOptions?): BuilderProgram

external fun createAbstractBuilder(rootNames: Array<String>?, options: CompilerOptions?, host: CompilerHost = definedExternally): BuilderProgram

external fun createAbstractBuilder(rootNames: Array<String>?, options: CompilerOptions?, host: CompilerHost = definedExternally, oldProgram: BuilderProgram = definedExternally): BuilderProgram

external fun createAbstractBuilder(rootNames: Array<String>?, options: CompilerOptions?, host: CompilerHost = definedExternally, oldProgram: BuilderProgram = definedExternally, configFileParsingDiagnostics: Array<Diagnostic> = definedExternally): BuilderProgram

external interface ReadBuildProgramHost {
  fun useCaseSensitiveFileNames(): Boolean
  fun getCurrentDirectory(): String
  fun readFile(fileName: String): String?
}

external fun readBuilderProgram(compilerOptions: CompilerOptions, host: ReadBuildProgramHost): EmitAndSemanticDiagnosticsBuilderProgram?

external fun createIncrementalCompilerHost(options: CompilerOptions, system: System = definedExternally): CompilerHost

external interface IncrementalProgramOptions<T : BuilderProgram> {
  var rootNames: Array<String>
  var options: CompilerOptions
  var configFileParsingDiagnostics: Array<Diagnostic>?
    get() = definedExternally
    set(value) = definedExternally
  var projectReferences: Array<ProjectReference>?
    get() = definedExternally
    set(value) = definedExternally
  var host: CompilerHost?
    get() = definedExternally
    set(value) = definedExternally
  var createProgram: CreateProgram<T>?
    get() = definedExternally
    set(value) = definedExternally
}

external fun <T : BuilderProgram> createIncrementalProgram(__0: IncrementalProgramOptions<T>): T

external interface WatchHost {
  val onWatchStatusChange: ((diagnostic: Diagnostic, newLine: String, options: CompilerOptions, errorCount: Number) -> Unit)?
  fun watchFile(path: String, callback: FileWatcherCallback, pollingInterval: Number = definedExternally, options: CompilerOptions = definedExternally): FileWatcher
  fun watchDirectory(path: String, callback: DirectoryWatcherCallback, recursive: Boolean = definedExternally, options: CompilerOptions = definedExternally): FileWatcher
  val setTimeout: ((callback: (args: Any) -> Unit, ms: Number, args: Any) -> Any)?
  val clearTimeout: ((timeoutId: Any) -> Unit)?
}

external interface ProgramHost<T : BuilderProgram> {
  var createProgram: CreateProgram<T>
  fun useCaseSensitiveFileNames(): Boolean
  fun getNewLine(): String
  fun getCurrentDirectory(): String
  fun getDefaultLibFileName(options: CompilerOptions): String
  val getDefaultLibLocation: (() -> String)?
  val createHash: ((data: String) -> String)?
  fun fileExists(path: String): Boolean
  fun readFile(path: String, encoding: String = definedExternally): String?
  val directoryExists: ((path: String) -> Boolean)?
  val getDirectories: ((path: String) -> Array<String>)?
  val readDirectory: ((path: String, extensions: Array<String>, exclude: Array<String>, include: Array<String>, depth: Number) -> Array<String>)?
  val realpath: ((path: String) -> String)?
  val trace: ((s: String) -> Unit)?
  val getEnvironmentVariable: ((name: String) -> String?)?
  val resolveModuleNames: ((moduleNames: Array<String>, containingFile: String, reusedNames: Array<String>?, redirectedReference: ResolvedProjectReference?, options: CompilerOptions) -> Array<ResolvedModule?>)?
  val resolveTypeReferenceDirectives: ((typeReferenceDirectiveNames: Array<String>, containingFile: String, redirectedReference: ResolvedProjectReference?, options: CompilerOptions) -> Array<ResolvedTypeReferenceDirective?>)?
}

external interface WatchCompilerHost<T : BuilderProgram> : ProgramHost<T>, WatchHost {
  val useSourceOfProjectReferenceRedirect: (() -> Boolean)?
  val getParsedCommandLine: ((fileName: String) -> ParsedCommandLine?)?
  val afterProgramCreate: ((program: T) -> Unit)?
}

external interface WatchCompilerHostOfFilesAndCompilerOptions<T : BuilderProgram> : WatchCompilerHost<T> {
  var rootFiles: Array<String>
  var options: CompilerOptions
  var watchOptions: WatchOptions?
    get() = definedExternally
    set(value) = definedExternally
  var projectReferences: Array<ProjectReference>?
    get() = definedExternally
    set(value) = definedExternally
}

external interface WatchCompilerHostOfConfigFile<T : BuilderProgram> : WatchCompilerHost<T>, ConfigFileDiagnosticsReporter {
  var configFileName: String
  var optionsToExtend: CompilerOptions?
    get() = definedExternally
    set(value) = definedExternally
  var watchOptionsToExtend: WatchOptions?
    get() = definedExternally
    set(value) = definedExternally
  var extraFileExtensions: Array<FileExtensionInfo>?
    get() = definedExternally
    set(value) = definedExternally
  override var readDirectory: (path: String, extensions: Array<String>, exclude: Array<String>, include: Array<String>, depth: Number) -> Array<String>
}

external interface Watch<T> {
  fun getProgram(): T
  fun close()
}

external interface WatchOfConfigFile<T> : Watch<T>

external interface WatchOfFilesAndCompilerOptions<T> : Watch<T> {
  fun updateRootFileNames(fileNames: Array<String>)
}

external fun <T : BuilderProgram> createWatchCompilerHost(configFileName: String, optionsToExtend: CompilerOptions?, system: System, createProgram: CreateProgram<T> = definedExternally, reportDiagnostic: DiagnosticReporter = definedExternally, reportWatchStatus: WatchStatusReporter = definedExternally, watchOptionsToExtend: WatchOptions = definedExternally, extraFileExtensions: Array<FileExtensionInfo> = definedExternally): WatchCompilerHostOfConfigFile<T>

external fun <T : BuilderProgram> createWatchCompilerHost(configFileName: String, optionsToExtend: CompilerOptions?, system: System): WatchCompilerHostOfConfigFile<T>

external fun <T : BuilderProgram> createWatchCompilerHost(configFileName: String, optionsToExtend: CompilerOptions?, system: System, createProgram: CreateProgram<T> = definedExternally): WatchCompilerHostOfConfigFile<T>

external fun <T : BuilderProgram> createWatchCompilerHost(configFileName: String, optionsToExtend: CompilerOptions?, system: System, createProgram: CreateProgram<T> = definedExternally, reportDiagnostic: DiagnosticReporter = definedExternally): WatchCompilerHostOfConfigFile<T>

external fun <T : BuilderProgram> createWatchCompilerHost(configFileName: String, optionsToExtend: CompilerOptions?, system: System, createProgram: CreateProgram<T> = definedExternally, reportDiagnostic: DiagnosticReporter = definedExternally, reportWatchStatus: WatchStatusReporter = definedExternally): WatchCompilerHostOfConfigFile<T>

external fun <T : BuilderProgram> createWatchCompilerHost(configFileName: String, optionsToExtend: CompilerOptions?, system: System, createProgram: CreateProgram<T> = definedExternally, reportDiagnostic: DiagnosticReporter = definedExternally, reportWatchStatus: WatchStatusReporter = definedExternally, watchOptionsToExtend: WatchOptions = definedExternally): WatchCompilerHostOfConfigFile<T>

external fun <T : BuilderProgram> createWatchCompilerHost(rootFiles: Array<String>, options: CompilerOptions, system: System, createProgram: CreateProgram<T> = definedExternally, reportDiagnostic: DiagnosticReporter = definedExternally, reportWatchStatus: WatchStatusReporter = definedExternally, projectReferences: Array<ProjectReference> = definedExternally, watchOptions: WatchOptions = definedExternally): WatchCompilerHostOfFilesAndCompilerOptions<T>

external fun <T : BuilderProgram> createWatchCompilerHost(rootFiles: Array<String>, options: CompilerOptions, system: System): WatchCompilerHostOfFilesAndCompilerOptions<T>

external fun <T : BuilderProgram> createWatchCompilerHost(rootFiles: Array<String>, options: CompilerOptions, system: System, createProgram: CreateProgram<T> = definedExternally): WatchCompilerHostOfFilesAndCompilerOptions<T>

external fun <T : BuilderProgram> createWatchCompilerHost(rootFiles: Array<String>, options: CompilerOptions, system: System, createProgram: CreateProgram<T> = definedExternally, reportDiagnostic: DiagnosticReporter = definedExternally): WatchCompilerHostOfFilesAndCompilerOptions<T>

external fun <T : BuilderProgram> createWatchCompilerHost(rootFiles: Array<String>, options: CompilerOptions, system: System, createProgram: CreateProgram<T> = definedExternally, reportDiagnostic: DiagnosticReporter = definedExternally, reportWatchStatus: WatchStatusReporter = definedExternally): WatchCompilerHostOfFilesAndCompilerOptions<T>

external fun <T : BuilderProgram> createWatchCompilerHost(rootFiles: Array<String>, options: CompilerOptions, system: System, createProgram: CreateProgram<T> = definedExternally, reportDiagnostic: DiagnosticReporter = definedExternally, reportWatchStatus: WatchStatusReporter = definedExternally, projectReferences: Array<ProjectReference> = definedExternally): WatchCompilerHostOfFilesAndCompilerOptions<T>

external fun <T : BuilderProgram> createWatchProgram(host: WatchCompilerHostOfFilesAndCompilerOptions<T>): WatchOfFilesAndCompilerOptions<T>

external fun <T : BuilderProgram> createWatchProgram(host: WatchCompilerHostOfConfigFile<T>): WatchOfConfigFile<T>

external interface BuildOptions {
  var dry: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var force: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var verbose: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var incremental: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var assumeChangesOnlyAffectDirectDependencies: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var traceResolution: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  @nativeGetter
  operator fun get(option: String): dynamic /* String? | Number? | Boolean? | Array<dynamic /* String | Number */>? | Array<String>? | MapLike<Array<String>>? | Array<PluginImport>? | Array<ProjectReference>? */
  @nativeSetter
  operator fun set(option: String, value: String?)
  @nativeSetter
  operator fun set(option: String, value: Number?)
  @nativeSetter
  operator fun set(option: String, value: Boolean?)
  @nativeSetter
  operator fun set(option: String, value: Array<dynamic /* String | Number */>?)
  @nativeSetter
  operator fun set(option: String, value: Array<String>?)
  @nativeSetter
  operator fun set(option: String, value: MapLike<Array<String>>?)
  @nativeSetter
  operator fun set(option: String, value: Array<PluginImport>?)
  @nativeSetter
  operator fun set(option: String, value: Array<ProjectReference>?)
}

external interface SolutionBuilderHostBase<T : BuilderProgram> : ProgramHost<T> {
  val createDirectory: ((path: String) -> Unit)?
  val writeFile: ((path: String, data: String, writeByteOrderMark: Boolean) -> Unit)?
  var getCustomTransformers: ((project: String) -> CustomTransformers?)?
    get() = definedExternally
    set(value) = definedExternally

  fun getModifiedTime(fileName: String): Date?
  fun setModifiedTime(fileName: String, date: Date)
  fun deleteFile(fileName: String)
  val getParsedCommandLine: ((fileName: String) -> ParsedCommandLine?)?
  var reportDiagnostic: DiagnosticReporter
  var reportSolutionBuilderStatus: DiagnosticReporter
  val afterProgramEmitAndDiagnostics: ((program: T) -> Unit)?
}

external interface SolutionBuilderHost<T : BuilderProgram> : SolutionBuilderHostBase<T> {
  var reportErrorSummary: ReportEmitErrorSummary?
    get() = definedExternally
    set(value) = definedExternally
}

external interface SolutionBuilderWithWatchHost<T : BuilderProgram> : SolutionBuilderHostBase<T>, WatchHost

external interface SolutionBuilder<T : BuilderProgram> {
  fun build(project: String = definedExternally, cancellationToken: CancellationToken = definedExternally, writeFile: WriteFileCallback = definedExternally, getCustomTransformers: (project: String) -> CustomTransformers = definedExternally): ExitStatus
  fun clean(project: String = definedExternally): ExitStatus
  fun buildReferences(project: String, cancellationToken: CancellationToken = definedExternally, writeFile: WriteFileCallback = definedExternally, getCustomTransformers: (project: String) -> CustomTransformers = definedExternally): ExitStatus
  fun cleanReferences(project: String = definedExternally): ExitStatus
  fun getNextInvalidatedProject(cancellationToken: CancellationToken = definedExternally): dynamic /* UpdateOutputFileStampsProject? | BuildInvalidedProject<T>? | UpdateBundleProject<T>? */
}

external fun createBuilderStatusReporter(system: System, pretty: Boolean = definedExternally): DiagnosticReporter

external fun <T : BuilderProgram> createSolutionBuilderHost(system: System = definedExternally, createProgram: CreateProgram<T> = definedExternally, reportDiagnostic: DiagnosticReporter = definedExternally, reportSolutionBuilderStatus: DiagnosticReporter = definedExternally, reportErrorSummary: ReportEmitErrorSummary = definedExternally): SolutionBuilderHost<T>

external fun <T : BuilderProgram> createSolutionBuilderWithWatchHost(system: System = definedExternally, createProgram: CreateProgram<T> = definedExternally, reportDiagnostic: DiagnosticReporter = definedExternally, reportSolutionBuilderStatus: DiagnosticReporter = definedExternally, reportWatchStatus: WatchStatusReporter = definedExternally): SolutionBuilderWithWatchHost<T>

external fun <T : BuilderProgram> createSolutionBuilder(host: SolutionBuilderHost<T>, rootNames: Array<String>, defaultOptions: BuildOptions): SolutionBuilder<T>

external fun <T : BuilderProgram> createSolutionBuilderWithWatch(host: SolutionBuilderWithWatchHost<T>, rootNames: Array<String>, defaultOptions: BuildOptions, baseWatchOptions: WatchOptions = definedExternally): SolutionBuilder<T>

external enum class InvalidatedProjectKind {
  Build /* = 0 */,
  UpdateBundle /* = 1 */,
  UpdateOutputFileStamps /* = 2 */
}

external interface InvalidatedProjectBase {
  val kind: InvalidatedProjectKind
  val project: String /* String & `T$5` */
  fun done(cancellationToken: CancellationToken = definedExternally, writeFile: WriteFileCallback = definedExternally, customTransformers: CustomTransformers = definedExternally): ExitStatus
  fun getCompilerOptions(): CompilerOptions
  fun getCurrentDirectory(): String
}

external interface UpdateOutputFileStampsProject : InvalidatedProjectBase {
  fun updateOutputFileStatmps()
}

external interface BuildInvalidedProject<T : BuilderProgram> : InvalidatedProjectBase {
  fun getBuilderProgram(): T?
  fun getProgram(): Program?
  fun getSourceFile(fileName: String): SourceFile?
  fun getSourceFiles(): Array<SourceFile>
  fun getOptionsDiagnostics(cancellationToken: CancellationToken = definedExternally): Array<Diagnostic>
  fun getGlobalDiagnostics(cancellationToken: CancellationToken = definedExternally): Array<Diagnostic>
  fun getConfigFileParsingDiagnostics(): Array<Diagnostic>
  fun getSyntacticDiagnostics(sourceFile: SourceFile = definedExternally, cancellationToken: CancellationToken = definedExternally): Array<Diagnostic>
  fun getAllDependencies(sourceFile: SourceFile): Array<String>
  fun getSemanticDiagnostics(sourceFile: SourceFile = definedExternally, cancellationToken: CancellationToken = definedExternally): Array<Diagnostic>
  fun getSemanticDiagnosticsOfNextAffectedFile(cancellationToken: CancellationToken = definedExternally, ignoreSourceFile: (sourceFile: SourceFile) -> Boolean = definedExternally): `T$14`<Array<Diagnostic>>?
  fun emit(targetSourceFile: SourceFile = definedExternally, writeFile: WriteFileCallback = definedExternally, cancellationToken: CancellationToken = definedExternally, emitOnlyDtsFiles: Boolean = definedExternally, customTransformers: CustomTransformers = definedExternally): EmitResult?
}

external interface UpdateBundleProject<T : BuilderProgram> : InvalidatedProjectBase {
  fun emit(writeFile: WriteFileCallback = definedExternally, customTransformers: CustomTransformers = definedExternally): dynamic /* EmitResult? | BuildInvalidedProject<T>? */
}

external interface SourceFileLike {
  fun getLineAndCharacterOfPosition(pos: Number): LineAndCharacter
}

external interface IScriptSnapshot {
  fun getText(start: Number, end: Number): String
  fun getLength(): Number
  fun getChangeRange(oldSnapshot: IScriptSnapshot): TextChangeRange?
  val dispose: (() -> Unit)?
}

external interface PreProcessedFileInfo {
  var referencedFiles: Array<FileReference>
  var typeReferenceDirectives: Array<FileReference>
  var libReferenceDirectives: Array<FileReference>
  var importedFiles: Array<FileReference>
  var ambientExternalModules: Array<String>?
    get() = definedExternally
    set(value) = definedExternally
  var isLibFile: Boolean?
}

external interface HostCancellationToken {
  fun isCancellationRequested(): Boolean
}

external interface InstallPackageOptions {
  var fileName: String /* String & `T$3` */
  var packageName: String
}

external interface PerformanceEvent {
  var kind: String /* "UpdateGraph" | "CreatePackageJsonAutoImportProvider" */
  var durationMs: Number
}

external enum class LanguageServiceMode {
  Semantic /* = 0 */,
  PartialSemantic /* = 1 */,
  Syntactic /* = 2 */
}

external interface LanguageServiceHost : GetEffectiveTypeRootsHost {
  fun getCompilationSettings(): CompilerOptions
  val getNewLine: (() -> String)?
  val getProjectVersion: (() -> String)?
  fun getScriptFileNames(): Array<String>
  val getScriptKind: ((fileName: String) -> ScriptKind)?
  fun getScriptVersion(fileName: String): String
  fun getScriptSnapshot(fileName: String): IScriptSnapshot?
  val getProjectReferences: (() -> Array<ProjectReference>?)?
  val getLocalizedDiagnosticMessages: (() -> Any)?
  val getCancellationToken: (() -> HostCancellationToken)?
  override var getCurrentDirectory: () -> String
  fun getDefaultLibFileName(options: CompilerOptions): String
  val log: ((s: String) -> Unit)?
  val trace: ((s: String) -> Unit)?
  val error: ((s: String) -> Unit)?
  val useCaseSensitiveFileNames: (() -> Boolean)?
  val readDirectory: ((path: String, extensions: Array<String>, exclude: Array<String>, include: Array<String>, depth: Number) -> Array<String>)?
  val readFile: ((path: String, encoding: String) -> String?)?
  val realpath: ((path: String) -> String)?
  val fileExists: ((path: String) -> Boolean)?
  val getTypeRootsVersion: (() -> Number)?
  val resolveModuleNames: ((moduleNames: Array<String>, containingFile: String, reusedNames: Array<String>?, redirectedReference: ResolvedProjectReference?, options: CompilerOptions) -> Array<ResolvedModule?>)?
  val getResolvedModuleWithFailedLookupLocationsFromCache: ((modulename: String, containingFile: String) -> ResolvedModuleWithFailedLookupLocations?)?
  val resolveTypeReferenceDirectives: ((typeDirectiveNames: Array<String>, containingFile: String, redirectedReference: ResolvedProjectReference?, options: CompilerOptions) -> Array<ResolvedTypeReferenceDirective?>)?
  val getDirectories: ((directoryName: String) -> Array<String>)?
  val getCustomTransformers: (() -> CustomTransformers?)?
  val isKnownTypesPackageName: ((name: String) -> Boolean)?
  val installPackage: ((options: InstallPackageOptions) -> Promise<ApplyCodeActionCommandResult>)?
  val writeFile: ((fileName: String, content: String) -> Unit)?
  val getParsedCommandLine: ((fileName: String) -> ParsedCommandLine?)?
}

external interface `T$15` {
  var metadata: Any?
    get() = definedExternally
    set(value) = definedExternally
}

external enum class SemanticClassificationFormat {
  Original /* = "original" */,
  TwentyTwenty /* = "2020" */
}

external interface LanguageService {
  fun cleanupSemanticCache()
  fun getSyntacticDiagnostics(fileName: String): Array<DiagnosticWithLocation>
  fun getSemanticDiagnostics(fileName: String): Array<Diagnostic>
  fun getSuggestionDiagnostics(fileName: String): Array<DiagnosticWithLocation>
  fun getCompilerOptionsDiagnostics(): Array<Diagnostic>
  fun getSyntacticClassifications(fileName: String, span: TextSpan): Array<ClassifiedSpan>
  fun getSyntacticClassifications(fileName: String, span: TextSpan, format: SemanticClassificationFormat): dynamic /* Array<ClassifiedSpan> | Array<ClassifiedSpan2020> */
  fun getSemanticClassifications(fileName: String, span: TextSpan): Array<ClassifiedSpan>
  fun getSemanticClassifications(fileName: String, span: TextSpan, format: SemanticClassificationFormat): dynamic /* Array<ClassifiedSpan> | Array<ClassifiedSpan2020> */
  fun getEncodedSyntacticClassifications(fileName: String, span: TextSpan): Classifications
  fun getEncodedSemanticClassifications(fileName: String, span: TextSpan, format: SemanticClassificationFormat = definedExternally): Classifications
  fun getCompletionsAtPosition(fileName: String, position: Number, options: GetCompletionsAtPositionOptions?): CompletionInfo /* CompletionInfo & `T$15` */
  fun getCompletionEntryDetails(fileName: String, position: Number, entryName: String, formatOptions: FormatCodeOptions?, source: String?, preferences: UserPreferences?, data: CompletionEntryData?): CompletionEntryDetails?
  fun getCompletionEntryDetails(fileName: String, position: Number, entryName: String, formatOptions: FormatCodeSettings?, source: String?, preferences: UserPreferences?, data: CompletionEntryData?): CompletionEntryDetails?
  fun getCompletionEntrySymbol(fileName: String, position: Number, name: String, source: String?): Symbol?
  fun getQuickInfoAtPosition(fileName: String, position: Number): QuickInfo?
  fun getNameOrDottedNameSpan(fileName: String, startPos: Number, endPos: Number): TextSpan?
  fun getBreakpointStatementAtPosition(fileName: String, position: Number): TextSpan?
  fun getSignatureHelpItems(fileName: String, position: Number, options: SignatureHelpItemsOptions?): SignatureHelpItems?
  fun getRenameInfo(fileName: String, position: Number, options: RenameInfoOptions = definedExternally): dynamic /* RenameInfoSuccess | RenameInfoFailure */
  fun findRenameLocations(fileName: String, position: Number, findInStrings: Boolean, findInComments: Boolean, providePrefixAndSuffixTextForRename: Boolean = definedExternally): Array<RenameLocation>?
  fun getSmartSelectionRange(fileName: String, position: Number): SelectionRange
  fun getDefinitionAtPosition(fileName: String, position: Number): Array<DefinitionInfo>?
  fun getDefinitionAndBoundSpan(fileName: String, position: Number): DefinitionInfoAndBoundSpan?
  fun getTypeDefinitionAtPosition(fileName: String, position: Number): Array<DefinitionInfo>?
  fun getImplementationAtPosition(fileName: String, position: Number): Array<ImplementationLocation>?
  fun getReferencesAtPosition(fileName: String, position: Number): Array<ReferenceEntry>?
  fun findReferences(fileName: String, position: Number): Array<ReferencedSymbol>?
  fun getDocumentHighlights(fileName: String, position: Number, filesToSearch: Array<String>): Array<DocumentHighlights>?
  fun getFileReferences(fileName: String): Array<ReferenceEntry>
  fun getOccurrencesAtPosition(fileName: String, position: Number): Array<ReferenceEntry>?
  fun getNavigateToItems(searchValue: String, maxResultCount: Number = definedExternally, fileName: String = definedExternally, excludeDtsFiles: Boolean = definedExternally): Array<NavigateToItem>
  fun getNavigationBarItems(fileName: String): Array<NavigationBarItem>
  fun getNavigationTree(fileName: String): NavigationTree
  fun prepareCallHierarchy(fileName: String, position: Number): dynamic /* CallHierarchyItem? | Array<CallHierarchyItem>? */
  fun provideCallHierarchyIncomingCalls(fileName: String, position: Number): Array<CallHierarchyIncomingCall>
  fun provideCallHierarchyOutgoingCalls(fileName: String, position: Number): Array<CallHierarchyOutgoingCall>
  fun getOutliningSpans(fileName: String): Array<OutliningSpan>
  fun getTodoComments(fileName: String, descriptors: Array<TodoCommentDescriptor>): Array<TodoComment>
  fun getBraceMatchingAtPosition(fileName: String, position: Number): Array<TextSpan>
  fun getIndentationAtPosition(fileName: String, position: Number, options: EditorOptions): Number
  fun getIndentationAtPosition(fileName: String, position: Number, options: EditorSettings): Number
  fun getFormattingEditsForRange(fileName: String, start: Number, end: Number, options: FormatCodeOptions): Array<TextChange>
  fun getFormattingEditsForRange(fileName: String, start: Number, end: Number, options: FormatCodeSettings): Array<TextChange>
  fun getFormattingEditsForDocument(fileName: String, options: FormatCodeOptions): Array<TextChange>
  fun getFormattingEditsForDocument(fileName: String, options: FormatCodeSettings): Array<TextChange>
  fun getFormattingEditsAfterKeystroke(fileName: String, position: Number, key: String, options: FormatCodeOptions): Array<TextChange>
  fun getFormattingEditsAfterKeystroke(fileName: String, position: Number, key: String, options: FormatCodeSettings): Array<TextChange>
  fun getDocCommentTemplateAtPosition(fileName: String, position: Number, options: DocCommentTemplateOptions = definedExternally): TextInsertion?
  fun isValidBraceCompletionAtPosition(fileName: String, position: Number, openingBrace: Number): Boolean
  fun getJsxClosingTagAtPosition(fileName: String, position: Number): JsxClosingTagInfo?
  fun getSpanOfEnclosingComment(fileName: String, position: Number, onlyMultiLine: Boolean): TextSpan?
  val toLineColumnOffset: ((fileName: String, position: Number) -> LineAndCharacter)?
  fun getCodeFixesAtPosition(fileName: String, start: Number, end: Number, errorCodes: Array<Number>, formatOptions: FormatCodeSettings, preferences: UserPreferences): Array<CodeFixAction>
  fun getCombinedCodeFix(scope: CombinedCodeFixScope, fixId: Any, formatOptions: FormatCodeSettings, preferences: UserPreferences): CombinedCodeActions
  fun applyCodeActionCommand(action: CodeActionCommand, formatSettings: FormatCodeSettings = definedExternally): dynamic /* Promise | Promise */
  fun applyCodeActionCommand(action: CodeActionCommand): dynamic /* Promise | Promise */
  fun applyCodeActionCommand(action: Array<CodeActionCommand>, formatSettings: FormatCodeSettings = definedExternally): dynamic /* Promise | Promise */
  fun applyCodeActionCommand(action: Array<CodeActionCommand>): dynamic /* Promise | Promise */
  fun applyCodeActionCommand(fileName: String, action: CodeActionCommand): dynamic /* Promise | Promise */
  fun applyCodeActionCommand(fileName: String, action: Array<CodeActionCommand>): dynamic /* Promise | Promise */
  fun getApplicableRefactors(fileName: String, positionOrRange: Number, preferences: UserPreferences?, triggerReason: String /* "implicit" | "invoked" */ = definedExternally, kind: String = definedExternally): Array<ApplicableRefactorInfo>
  fun getApplicableRefactors(fileName: String, positionOrRange: Number, preferences: UserPreferences?): Array<ApplicableRefactorInfo>
  fun getApplicableRefactors(fileName: String, positionOrRange: Number, preferences: UserPreferences?, triggerReason: String /* "implicit" | "invoked" */ = definedExternally): Array<ApplicableRefactorInfo>
  fun getApplicableRefactors(fileName: String, positionOrRange: TextRange, preferences: UserPreferences?, triggerReason: String /* "implicit" | "invoked" */ = definedExternally, kind: String = definedExternally): Array<ApplicableRefactorInfo>
  fun getApplicableRefactors(fileName: String, positionOrRange: TextRange, preferences: UserPreferences?): Array<ApplicableRefactorInfo>
  fun getApplicableRefactors(fileName: String, positionOrRange: TextRange, preferences: UserPreferences?, triggerReason: String /* "implicit" | "invoked" */ = definedExternally): Array<ApplicableRefactorInfo>
  fun getEditsForRefactor(fileName: String, formatOptions: FormatCodeSettings, positionOrRange: Number, refactorName: String, actionName: String, preferences: UserPreferences?): RefactorEditInfo?
  fun getEditsForRefactor(fileName: String, formatOptions: FormatCodeSettings, positionOrRange: TextRange, refactorName: String, actionName: String, preferences: UserPreferences?): RefactorEditInfo?
  fun organizeImports(args: OrganizeImportsArgs, formatOptions: FormatCodeSettings, preferences: UserPreferences?): Array<FileTextChanges>
  fun getEditsForFileRename(oldFilePath: String, newFilePath: String, formatOptions: FormatCodeSettings, preferences: UserPreferences?): Array<FileTextChanges>
  fun getEmitOutput(fileName: String, emitOnlyDtsFiles: Boolean = definedExternally, forceDtsEmit: Boolean = definedExternally): EmitOutput
  fun getProgram(): Program?
  fun toggleLineComment(fileName: String, textRange: TextRange): Array<TextChange>
  fun toggleMultilineComment(fileName: String, textRange: TextRange): Array<TextChange>
  fun commentSelection(fileName: String, textRange: TextRange): Array<TextChange>
  fun uncommentSelection(fileName: String, textRange: TextRange): Array<TextChange>
  fun dispose()
}

external interface JsxClosingTagInfo {
  val newText: String
}

external interface CombinedCodeFixScope {
  var type: String /* "file" */
  var fileName: String
}

external interface OrganizeImportsArgs : CombinedCodeFixScope {
  var skipDestructiveCodeActions: Boolean?
    get() = definedExternally
    set(value) = definedExternally
}

external interface GetCompletionsAtPositionOptions : UserPreferences {
  var triggerCharacter: String? /* "." | """ | "'" | "`" | "/" | "@" | "<" | "#" | " " */
    get() = definedExternally
    set(value) = definedExternally
  var includeExternalModuleExports: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var includeInsertTextCompletions: Boolean?
    get() = definedExternally
    set(value) = definedExternally
}

external interface SignatureHelpItemsOptions {
  var triggerReason: dynamic /* SignatureHelpInvokedReason? | SignatureHelpCharacterTypedReason? | SignatureHelpRetriggeredReason? */
    get() = definedExternally
    set(value) = definedExternally
}

external interface SignatureHelpInvokedReason {
  var kind: String /* "invoked" */
  var triggerCharacter: Any?
    get() = definedExternally
    set(value) = definedExternally
}

external interface SignatureHelpCharacterTypedReason {
  var kind: String /* "characterTyped" */
  var triggerCharacter: String /* "," | "(" | "<" */
}

external interface SignatureHelpRetriggeredReason {
  var kind: String /* "retrigger" */
  var triggerCharacter: String? /* "," | "(" | "<" | ")" */
    get() = definedExternally
    set(value) = definedExternally
}

external interface ApplyCodeActionCommandResult {
  var successMessage: String
}

external interface Classifications {
  var spans: Array<Number>
  var endOfLineState: EndOfLineState
}

external interface ClassifiedSpan {
  var textSpan: TextSpan
  var classificationType: ClassificationTypeNames
}

external interface ClassifiedSpan2020 {
  var textSpan: TextSpan
  var classificationType: Number
}

external interface NavigationBarItem {
  var text: String
  var kind: ScriptElementKind
  var kindModifiers: String
  var spans: Array<TextSpan>
  var childItems: Array<NavigationBarItem>
  var indent: Number
  var bolded: Boolean?
  var grayed: Boolean?
}

external interface NavigationTree {
  var text: String
  var kind: ScriptElementKind
  var kindModifiers: String
  var spans: Array<TextSpan>
  var nameSpan: TextSpan?
  var childItems: Array<NavigationTree>?
    get() = definedExternally
    set(value) = definedExternally
}

external interface CallHierarchyItem {
  var name: String
  var kind: ScriptElementKind
  var kindModifiers: String?
    get() = definedExternally
    set(value) = definedExternally
  var file: String
  var span: TextSpan
  var selectionSpan: TextSpan
  var containerName: String?
    get() = definedExternally
    set(value) = definedExternally
}

external interface CallHierarchyIncomingCall {
  var from: CallHierarchyItem
  var fromSpans: Array<TextSpan>
}

external interface CallHierarchyOutgoingCall {
  var to: CallHierarchyItem
  var fromSpans: Array<TextSpan>
}

external interface TodoCommentDescriptor {
  var text: String
  var priority: Number
}

external interface TodoComment {
  var descriptor: TodoCommentDescriptor
  var message: String
  var position: Number
}

external interface TextChange {
  var span: TextSpan
  var newText: String
}

external interface FileTextChanges {
  var fileName: String
  var textChanges: Array<TextChange>
  var isNewFile: Boolean?
    get() = definedExternally
    set(value) = definedExternally
}

external interface CodeAction {
  var description: String
  var changes: Array<FileTextChanges>
  var commands: Array<CodeActionCommand>?
    get() = definedExternally
    set(value) = definedExternally
}

external interface CodeFixAction : CodeAction {
  var fixName: String
  var fixId: Any?
    get() = definedExternally
    set(value) = definedExternally
  var fixAllDescription: String?
    get() = definedExternally
    set(value) = definedExternally
}

external interface CombinedCodeActions {
  var changes: Array<FileTextChanges>
  var commands: Array<CodeActionCommand>?
    get() = definedExternally
    set(value) = definedExternally
}

external interface InstallPackageAction

external interface ApplicableRefactorInfo {
  var name: String
  var description: String
  var inlineable: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var actions: Array<RefactorActionInfo>
}

external interface RefactorActionInfo {
  var name: String
  var description: String
  var notApplicableReason: String?
    get() = definedExternally
    set(value) = definedExternally
  var kind: String?
    get() = definedExternally
    set(value) = definedExternally
}

external interface RefactorEditInfo {
  var edits: Array<FileTextChanges>
  var renameFilename: String?
    get() = definedExternally
    set(value) = definedExternally
  var renameLocation: Number?
    get() = definedExternally
    set(value) = definedExternally
  var commands: Array<CodeActionCommand>?
    get() = definedExternally
    set(value) = definedExternally
}

external interface TextInsertion {
  var newText: String
  var caretOffset: Number
}

external interface DocumentSpan {
  var textSpan: TextSpan
  var fileName: String
  var originalTextSpan: TextSpan?
    get() = definedExternally
    set(value) = definedExternally
  var originalFileName: String?
    get() = definedExternally
    set(value) = definedExternally
  var contextSpan: TextSpan?
    get() = definedExternally
    set(value) = definedExternally
  var originalContextSpan: TextSpan?
    get() = definedExternally
    set(value) = definedExternally
}

external interface RenameLocation : DocumentSpan {
  val prefixText: String?
    get() = definedExternally
  val suffixText: String?
    get() = definedExternally
}

external interface ReferenceEntry : DocumentSpan {
  var isWriteAccess: Boolean?
  var isDefinition: Boolean?
  var isInString: Boolean?
    get() = definedExternally
    set(value) = definedExternally
}

external interface ImplementationLocation : DocumentSpan {
  var kind: ScriptElementKind
  var displayParts: Array<SymbolDisplayPart>
}

external enum class HighlightSpanKind {
  none /* = "none" */,
  definition /* = "definition" */,
  reference /* = "reference" */,
  writtenReference /* = "writtenReference" */
}

external interface HighlightSpan {
  var fileName: String?
    get() = definedExternally
    set(value) = definedExternally
  var isInString: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var textSpan: TextSpan
  var contextSpan: TextSpan?
    get() = definedExternally
    set(value) = definedExternally
  var kind: HighlightSpanKind
}

external interface NavigateToItem {
  var name: String
  var kind: ScriptElementKind
  var kindModifiers: String
  var matchKind: String /* "exact" | "prefix" | "substring" | "camelCase" */
  var isCaseSensitive: Boolean?
  var fileName: String
  var textSpan: TextSpan
  var containerName: String
  var containerKind: ScriptElementKind
}

external enum class IndentStyle {
  None /* = 0 */,
  Block /* = 1 */,
  Smart /* = 2 */
}

external enum class SemicolonPreference {
  Ignore /* = "ignore" */,
  Insert /* = "insert" */,
  Remove /* = "remove" */
}

external interface EditorOptions {
  var BaseIndentSize: Number?
    get() = definedExternally
    set(value) = definedExternally
  var IndentSize: Number
  var TabSize: Number
  var NewLineCharacter: String
  var ConvertTabsToSpaces: Boolean?
  var IndentStyle: IndentStyle
}

external interface EditorSettings {
  var baseIndentSize: Number?
    get() = definedExternally
    set(value) = definedExternally
  var indentSize: Number?
    get() = definedExternally
    set(value) = definedExternally
  var tabSize: Number?
    get() = definedExternally
    set(value) = definedExternally
  var newLineCharacter: String?
    get() = definedExternally
    set(value) = definedExternally
  var convertTabsToSpaces: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var indentStyle: IndentStyle?
    get() = definedExternally
    set(value) = definedExternally
  var trimTrailingWhitespace: Boolean?
    get() = definedExternally
    set(value) = definedExternally
}

external interface FormatCodeOptions : EditorOptions {
  var InsertSpaceAfterCommaDelimiter: Boolean?
  var InsertSpaceAfterSemicolonInForStatements: Boolean?
  var InsertSpaceBeforeAndAfterBinaryOperators: Boolean?
  var InsertSpaceAfterConstructor: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var InsertSpaceAfterKeywordsInControlFlowStatements: Boolean?
  var InsertSpaceAfterFunctionKeywordForAnonymousFunctions: Boolean?
  var InsertSpaceAfterOpeningAndBeforeClosingNonemptyParenthesis: Boolean?
  var InsertSpaceAfterOpeningAndBeforeClosingNonemptyBrackets: Boolean?
  var InsertSpaceAfterOpeningAndBeforeClosingNonemptyBraces: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var InsertSpaceAfterOpeningAndBeforeClosingTemplateStringBraces: Boolean?
  var InsertSpaceAfterOpeningAndBeforeClosingJsxExpressionBraces: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var InsertSpaceAfterTypeAssertion: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var InsertSpaceBeforeFunctionParenthesis: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var PlaceOpenBraceOnNewLineForFunctions: Boolean?
  var PlaceOpenBraceOnNewLineForControlBlocks: Boolean?
  var insertSpaceBeforeTypeAnnotation: Boolean?
    get() = definedExternally
    set(value) = definedExternally
}

external interface FormatCodeSettings : EditorSettings {
  val insertSpaceAfterCommaDelimiter: Boolean?
    get() = definedExternally
  val insertSpaceAfterSemicolonInForStatements: Boolean?
    get() = definedExternally
  val insertSpaceBeforeAndAfterBinaryOperators: Boolean?
    get() = definedExternally
  val insertSpaceAfterConstructor: Boolean?
    get() = definedExternally
  val insertSpaceAfterKeywordsInControlFlowStatements: Boolean?
    get() = definedExternally
  val insertSpaceAfterFunctionKeywordForAnonymousFunctions: Boolean?
    get() = definedExternally
  val insertSpaceAfterOpeningAndBeforeClosingNonemptyParenthesis: Boolean?
    get() = definedExternally
  val insertSpaceAfterOpeningAndBeforeClosingNonemptyBrackets: Boolean?
    get() = definedExternally
  val insertSpaceAfterOpeningAndBeforeClosingNonemptyBraces: Boolean?
    get() = definedExternally
  val insertSpaceAfterOpeningAndBeforeClosingEmptyBraces: Boolean?
    get() = definedExternally
  val insertSpaceAfterOpeningAndBeforeClosingTemplateStringBraces: Boolean?
    get() = definedExternally
  val insertSpaceAfterOpeningAndBeforeClosingJsxExpressionBraces: Boolean?
    get() = definedExternally
  val insertSpaceAfterTypeAssertion: Boolean?
    get() = definedExternally
  val insertSpaceBeforeFunctionParenthesis: Boolean?
    get() = definedExternally
  val placeOpenBraceOnNewLineForFunctions: Boolean?
    get() = definedExternally
  val placeOpenBraceOnNewLineForControlBlocks: Boolean?
    get() = definedExternally
  val insertSpaceBeforeTypeAnnotation: Boolean?
    get() = definedExternally
  val indentMultiLineObjectLiteralBeginningOnBlankLine: Boolean?
    get() = definedExternally
  val semicolons: SemicolonPreference?
    get() = definedExternally
}

external fun getDefaultFormatCodeSettings(newLineCharacter: String = definedExternally): FormatCodeSettings

external interface DefinitionInfo : DocumentSpan {
  var kind: ScriptElementKind
  var name: String
  var containerKind: ScriptElementKind
  var containerName: String
  var unverified: Boolean?
    get() = definedExternally
    set(value) = definedExternally
}

external interface DefinitionInfoAndBoundSpan {
  var definitions: Array<DefinitionInfo>?
    get() = definedExternally
    set(value) = definedExternally
  var textSpan: TextSpan
}

external interface ReferencedSymbolDefinitionInfo : DefinitionInfo {
  var displayParts: Array<SymbolDisplayPart>
}

external interface ReferencedSymbol {
  var definition: ReferencedSymbolDefinitionInfo
  var references: Array<ReferenceEntry>
}

external enum class SymbolDisplayPartKind {
  aliasName /* = 0 */,
  className /* = 1 */,
  enumName /* = 2 */,
  fieldName /* = 3 */,
  interfaceName /* = 4 */,
  keyword /* = 5 */,
  lineBreak /* = 6 */,
  numericLiteral /* = 7 */,
  stringLiteral /* = 8 */,
  localName /* = 9 */,
  methodName /* = 10 */,
  moduleName /* = 11 */,
  operator /* = 12 */,
  parameterName /* = 13 */,
  propertyName /* = 14 */,
  punctuation /* = 15 */,
  space /* = 16 */,
  text /* = 17 */,
  typeParameterName /* = 18 */,
  enumMemberName /* = 19 */,
  functionName /* = 20 */,
  regularExpressionLiteral /* = 21 */,
  link /* = 22 */,
  linkName /* = 23 */,
  linkText /* = 24 */
}

external interface SymbolDisplayPart {
  var text: String
  var kind: String
}

external interface JSDocLinkDisplayPart : SymbolDisplayPart {
  var target: DocumentSpan
}

external interface JSDocTagInfo {
  var name: String
  var text: Array<SymbolDisplayPart>?
    get() = definedExternally
    set(value) = definedExternally
}

external interface QuickInfo {
  var kind: ScriptElementKind
  var kindModifiers: String
  var textSpan: TextSpan
  var displayParts: Array<SymbolDisplayPart>?
    get() = definedExternally
    set(value) = definedExternally
  var documentation: Array<SymbolDisplayPart>?
    get() = definedExternally
    set(value) = definedExternally
  var tags: Array<JSDocTagInfo>?
    get() = definedExternally
    set(value) = definedExternally
}

external interface RenameInfoSuccess {
  var canRename: Boolean?
  var fileToRename: String?
    get() = definedExternally
    set(value) = definedExternally
  var displayName: String
  var fullDisplayName: String
  var kind: ScriptElementKind
  var kindModifiers: String
  var triggerSpan: TextSpan
}

external interface RenameInfoFailure {
  var canRename: Boolean?
  var localizedErrorMessage: String
}

external interface RenameInfoOptions {
  val allowRenameOfImportPath: Boolean?
    get() = definedExternally
}

external interface DocCommentTemplateOptions {
  val generateReturnInDocTemplate: Boolean?
    get() = definedExternally
}

external interface SignatureHelpParameter {
  var name: String
  var documentation: Array<SymbolDisplayPart>
  var displayParts: Array<SymbolDisplayPart>
  var isOptional: Boolean?
  var isRest: Boolean?
    get() = definedExternally
    set(value) = definedExternally
}

external interface SelectionRange {
  var textSpan: TextSpan
  var parent: SelectionRange?
    get() = definedExternally
    set(value) = definedExternally
}

external interface SignatureHelpItem {
  var isVariadic: Boolean?
  var prefixDisplayParts: Array<SymbolDisplayPart>
  var suffixDisplayParts: Array<SymbolDisplayPart>
  var separatorDisplayParts: Array<SymbolDisplayPart>
  var parameters: Array<SignatureHelpParameter>
  var documentation: Array<SymbolDisplayPart>
  var tags: Array<JSDocTagInfo>
}

external interface SignatureHelpItems {
  var items: Array<SignatureHelpItem>
  var applicableSpan: TextSpan
  var selectedItemIndex: Number
  var argumentIndex: Number
  var argumentCount: Number
}

external interface CompletionInfo {
  var isGlobalCompletion: Boolean?
  var isMemberCompletion: Boolean?
  var optionalReplacementSpan: TextSpan?
    get() = definedExternally
    set(value) = definedExternally
  var isNewIdentifierLocation: Boolean?
  var isIncomplete: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var entries: Array<CompletionEntry>
}

external interface CompletionEntryData {
  var fileName: String?
    get() = definedExternally
    set(value) = definedExternally
  var ambientModuleName: String?
    get() = definedExternally
    set(value) = definedExternally
  var isPackageJsonImport: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var exportName: String
  var moduleSpecifier: String?
    get() = definedExternally
    set(value) = definedExternally
}

external interface CompletionEntry {
  var name: String
  var kind: ScriptElementKind
  var kindModifiers: String?
    get() = definedExternally
    set(value) = definedExternally
  var sortText: String
  var insertText: String?
    get() = definedExternally
    set(value) = definedExternally
  var isSnippet: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var replacementSpan: TextSpan?
    get() = definedExternally
    set(value) = definedExternally
  var hasAction: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var source: String?
    get() = definedExternally
    set(value) = definedExternally
  var sourceDisplay: Array<SymbolDisplayPart>?
    get() = definedExternally
    set(value) = definedExternally
  var isRecommended: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var isFromUncheckedFile: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var isPackageJsonImport: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var isImportStatementCompletion: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var data: CompletionEntryData?
    get() = definedExternally
    set(value) = definedExternally
}

external interface CompletionEntryDetails {
  var name: String
  var kind: ScriptElementKind
  var kindModifiers: String
  var displayParts: Array<SymbolDisplayPart>
  var documentation: Array<SymbolDisplayPart>?
    get() = definedExternally
    set(value) = definedExternally
  var tags: Array<JSDocTagInfo>?
    get() = definedExternally
    set(value) = definedExternally
  var codeActions: Array<CodeAction>?
    get() = definedExternally
    set(value) = definedExternally
  var source: Array<SymbolDisplayPart>?
    get() = definedExternally
    set(value) = definedExternally
  var sourceDisplay: Array<SymbolDisplayPart>?
    get() = definedExternally
    set(value) = definedExternally
}

external interface OutliningSpan {
  var textSpan: TextSpan
  var hintSpan: TextSpan
  var bannerText: String
  var autoCollapse: Boolean?
  var kind: OutliningSpanKind
}

external enum class OutliningSpanKind {
  Comment /* = "comment" */,
  Region /* = "region" */,
  Code /* = "code" */,
  Imports /* = "imports" */
}

external enum class OutputFileType {
  JavaScript /* = 0 */,
  SourceMap /* = 1 */,
  Declaration /* = 2 */
}

external enum class EndOfLineState {
  None /* = 0 */,
  InMultiLineCommentTrivia /* = 1 */,
  InSingleQuoteStringLiteral /* = 2 */,
  InDoubleQuoteStringLiteral /* = 3 */,
  InTemplateHeadOrNoSubstitutionTemplate /* = 4 */,
  InTemplateMiddleOrTail /* = 5 */,
  InTemplateSubstitutionPosition /* = 6 */
}

external enum class TokenClass {
  Punctuation /* = 0 */,
  Keyword /* = 1 */,
  Operator /* = 2 */,
  Comment /* = 3 */,
  Whitespace /* = 4 */,
  Identifier /* = 5 */,
  NumberLiteral /* = 6 */,
  BigIntLiteral /* = 7 */,
  StringLiteral /* = 8 */,
  RegExpLiteral /* = 9 */
}

external interface ClassificationResult {
  var finalLexState: EndOfLineState
  var entries: Array<ClassificationInfo>
}

external interface ClassificationInfo {
  var length: Number
  var classification: TokenClass
}

external interface Classifier {
  fun getClassificationsForLine(text: String, lexState: EndOfLineState, syntacticClassifierAbsent: Boolean): ClassificationResult
  fun getEncodedLexicalClassifications(text: String, endOfLineState: EndOfLineState, syntacticClassifierAbsent: Boolean): Classifications
}

external enum class ScriptElementKind {
  unknown /* = "" */,
  warning /* = "warning" */,
  keyword /* = "keyword" */,
  scriptElement /* = "script" */,
  moduleElement /* = "module" */,
  classElement /* = "class" */,
  localClassElement /* = "local class" */,
  interfaceElement /* = "interface" */,
  typeElement /* = "type" */,
  enumElement /* = "enum" */,
  enumMemberElement /* = "enum member" */,
  variableElement /* = "var" */,
  localVariableElement /* = "local var" */,
  functionElement /* = "function" */,
  localFunctionElement /* = "local function" */,
  memberFunctionElement /* = "method" */,
  memberGetAccessorElement /* = "getter" */,
  memberSetAccessorElement /* = "setter" */,
  memberVariableElement /* = "property" */,
  constructorImplementationElement /* = "constructor" */,
  callSignatureElement /* = "call" */,
  indexSignatureElement /* = "index" */,
  constructSignatureElement /* = "construct" */,
  parameterElement /* = "parameter" */,
  typeParameterElement /* = "type parameter" */,
  primitiveType /* = "primitive type" */,
  label /* = "label" */,
  alias /* = "alias" */,
  constElement /* = "const" */,
  letElement /* = "let" */,
  directory /* = "directory" */,
  externalModuleName /* = "external module name" */,
  jsxAttribute /* = "JSX attribute" */,
  string /* = "string" */,
  link /* = "link" */,
  linkName /* = "link name" */,
  linkText /* = "link text" */
}

external enum class ScriptElementKindModifier {
  none /* = "" */,
  publicMemberModifier /* = "public" */,
  privateMemberModifier /* = "private" */,
  protectedMemberModifier /* = "protected" */,
  exportedModifier /* = "export" */,
  ambientModifier /* = "declare" */,
  staticModifier /* = "static" */,
  abstractModifier /* = "abstract" */,
  optionalModifier /* = "optional" */,
  deprecatedModifier /* = "deprecated" */,
  dtsModifier /* = ".d.ts" */,
  tsModifier /* = ".ts" */,
  tsxModifier /* = ".tsx" */,
  jsModifier /* = ".js" */,
  jsxModifier /* = ".jsx" */,
  jsonModifier /* = ".json" */
}

external enum class ClassificationTypeNames {
  comment /* = "comment" */,
  identifier /* = "identifier" */,
  keyword /* = "keyword" */,
  numericLiteral /* = "number" */,
  bigintLiteral /* = "bigint" */,
  operator /* = "operator" */,
  stringLiteral /* = "string" */,
  whiteSpace /* = "whitespace" */,
  text /* = "text" */,
  punctuation /* = "punctuation" */,
  className /* = "class name" */,
  enumName /* = "enum name" */,
  interfaceName /* = "interface name" */,
  moduleName /* = "module name" */,
  typeParameterName /* = "type parameter name" */,
  typeAliasName /* = "type alias name" */,
  parameterName /* = "parameter name" */,
  docCommentTagName /* = "doc comment tag name" */,
  jsxOpenTagName /* = "jsx open tag name" */,
  jsxCloseTagName /* = "jsx close tag name" */,
  jsxSelfClosingTagName /* = "jsx self closing tag name" */,
  jsxAttribute /* = "jsx attribute" */,
  jsxText /* = "jsx text" */,
  jsxAttributeStringLiteralValue /* = "jsx attribute string literal value" */
}

external enum class ClassificationType {
  comment /* = 1 */,
  identifier /* = 2 */,
  keyword /* = 3 */,
  numericLiteral /* = 4 */,
  operator /* = 5 */,
  stringLiteral /* = 6 */,
  regularExpressionLiteral /* = 7 */,
  whiteSpace /* = 8 */,
  text /* = 9 */,
  punctuation /* = 10 */,
  className /* = 11 */,
  enumName /* = 12 */,
  interfaceName /* = 13 */,
  moduleName /* = 14 */,
  typeParameterName /* = 15 */,
  typeAliasName /* = 16 */,
  parameterName /* = 17 */,
  docCommentTagName /* = 18 */,
  jsxOpenTagName /* = 19 */,
  jsxCloseTagName /* = 20 */,
  jsxSelfClosingTagName /* = 21 */,
  jsxAttribute /* = 22 */,
  jsxText /* = 23 */,
  jsxAttributeStringLiteralValue /* = 24 */,
  bigintLiteral /* = 25 */
}

external fun createClassifier(): Classifier

external interface DocumentHighlights {
  var fileName: String
  var highlightSpans: Array<HighlightSpan>
}

external interface DocumentRegistry {
  fun acquireDocument(fileName: String, compilationSettings: CompilerOptions, scriptSnapshot: IScriptSnapshot, version: String, scriptKind: ScriptKind = definedExternally): SourceFile
  fun acquireDocumentWithKey(fileName: String, path: String /* String & `T$3` */, compilationSettings: CompilerOptions, key: String /* String & `T$16` */, scriptSnapshot: IScriptSnapshot, version: String, scriptKind: ScriptKind = definedExternally): SourceFile
  fun updateDocument(fileName: String, compilationSettings: CompilerOptions, scriptSnapshot: IScriptSnapshot, version: String, scriptKind: ScriptKind = definedExternally): SourceFile
  fun updateDocumentWithKey(fileName: String, path: String /* String & `T$3` */, compilationSettings: CompilerOptions, key: String /* String & `T$16` */, scriptSnapshot: IScriptSnapshot, version: String, scriptKind: ScriptKind = definedExternally): SourceFile
  fun getKeyForCompilationSettings(settings: CompilerOptions): String /* String & `T$16` */
  fun releaseDocument(fileName: String, compilationSettings: CompilerOptions)
  fun releaseDocument(fileName: String, compilationSettings: CompilerOptions, scriptKind: ScriptKind)
  fun releaseDocumentWithKey(path: String /* String & `T$3` */, key: String /* String & `T$16` */)
  fun releaseDocumentWithKey(path: String /* String & `T$3` */, key: String /* String & `T$16` */, scriptKind: ScriptKind)
  fun reportStats(): String
}

external interface `T$16` {
  var __bucketKey: Any
}

external fun createDocumentRegistry(useCaseSensitiveFileNames: Boolean = definedExternally, currentDirectory: String = definedExternally): DocumentRegistry

external fun preProcessFile(sourceText: String, readImportFiles: Boolean = definedExternally, detectJavaScriptImports: Boolean = definedExternally): PreProcessedFileInfo

external interface TranspileOptions {
  var compilerOptions: CompilerOptions?
    get() = definedExternally
    set(value) = definedExternally
  var fileName: String?
    get() = definedExternally
    set(value) = definedExternally
  var reportDiagnostics: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var moduleName: String?
    get() = definedExternally
    set(value) = definedExternally
  var renamedDependencies: MapLike<String>?
    get() = definedExternally
    set(value) = definedExternally
  var transformers: CustomTransformers?
    get() = definedExternally
    set(value) = definedExternally
}

external interface TranspileOutput {
  var outputText: String
  var diagnostics: Array<Diagnostic>?
    get() = definedExternally
    set(value) = definedExternally
  var sourceMapText: String?
    get() = definedExternally
    set(value) = definedExternally
}

external fun transpileModule(input: String, transpileOptions: TranspileOptions): TranspileOutput

external fun transpile(input: String, compilerOptions: CompilerOptions = definedExternally, fileName: String = definedExternally, diagnostics: Array<Diagnostic> = definedExternally, moduleName: String = definedExternally): String

external var servicesVersion: Any

external fun toEditorSettings(options: EditorOptions): EditorSettings

external fun toEditorSettings(options: EditorSettings): EditorSettings

external fun displayPartsToString(displayParts: Array<SymbolDisplayPart>?): String

external fun getDefaultCompilerOptions(): CompilerOptions

external fun getSupportedCodeFixes(): Array<String>

external fun createLanguageServiceSourceFile(fileName: String, scriptSnapshot: IScriptSnapshot, scriptTarget: ScriptTarget, version: String, setNodeParents: Boolean, scriptKind: ScriptKind = definedExternally): SourceFile

external fun updateLanguageServiceSourceFile(sourceFile: SourceFile, scriptSnapshot: IScriptSnapshot, version: String, textChangeRange: TextChangeRange?, aggressiveChecks: Boolean = definedExternally): SourceFile

external fun createLanguageService(host: LanguageServiceHost, documentRegistry: DocumentRegistry = definedExternally, syntaxOnlyOrLanguageServiceMode: Boolean = definedExternally): LanguageService

external fun createLanguageService(host: LanguageServiceHost): LanguageService

external fun createLanguageService(host: LanguageServiceHost, documentRegistry: DocumentRegistry = definedExternally): LanguageService

external fun createLanguageService(host: LanguageServiceHost, documentRegistry: DocumentRegistry = definedExternally, syntaxOnlyOrLanguageServiceMode: LanguageServiceMode = definedExternally): LanguageService

external fun getDefaultLibFilePath(options: CompilerOptions): String

external fun <T : Node> transform(source: T, transformers: Array<TransformerFactory<T>>, compilerOptions: CompilerOptions = definedExternally): TransformationResult<T>

external fun <T : Node> transform(source: T, transformers: Array<TransformerFactory<T>>): TransformationResult<T>

external fun <T : Node> transform(source: Array<T>, transformers: Array<TransformerFactory<T>>, compilerOptions: CompilerOptions = definedExternally): TransformationResult<T>

external fun <T : Node> transform(source: Array<T>, transformers: Array<TransformerFactory<T>>): TransformationResult<T>

external fun <T> createNodeArray(elements: Array<T>?, hasTrailingComma: Boolean?): NodeArray<T>

external var createNumericLiteral: (value: dynamic /* String | Number */, numericLiteralFlags: TokenFlags?) -> NumericLiteral

external var createBigIntLiteral: (value: dynamic /* String | PseudoBigInt */) -> BigIntLiteral

external object createStringLiteral {
  @nativeInvoke
  operator fun invoke(text: String, isSingleQuote: Boolean? = definedExternally): StringLiteral
  @nativeInvoke
  operator fun invoke(text: String, isSingleQuote: Boolean? = definedExternally, hasExtendedUnicodeEscape: Boolean? = definedExternally): StringLiteral
}

external var createStringLiteralFromNode: (sourceNode: dynamic /* Identifier | StringLiteral | NoSubstitutionTemplateLiteral | NumericLiteral */, isSingleQuote: Boolean?) -> StringLiteral

external var createRegularExpressionLiteral: (text: String) -> RegularExpressionLiteral

external var createLoopVariable: (reservedInNestedScopes: Boolean?) -> Identifier

external var createUniqueName: (text: String, flags: GeneratedIdentifierFlags?) -> Identifier

external var createPrivateIdentifier: (text: String) -> PrivateIdentifier

external var createSuper: () -> SuperExpression

external var createThis: () -> ThisExpression

external var createNull: () -> NullLiteral

external var createTrue: () -> TrueLiteral

external var createFalse: () -> FalseLiteral

//external var createModifier: (kind: T) -> ModifierToken<T>
external fun <T: SyntaxKind> createModifier(kind: T): ModifierToken<T>

external var createModifiersFromModifierFlags: (flags: ModifierFlags) -> Array<dynamic /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>

external var createQualifiedName: (left: dynamic /* Identifier | QualifiedName */, right: dynamic /* String | Identifier */) -> QualifiedName

external var updateQualifiedName: (node: QualifiedName, left: dynamic /* Identifier | QualifiedName */, right: Identifier) -> QualifiedName

external var createComputedPropertyName: (expression: Expression) -> ComputedPropertyName

external var updateComputedPropertyName: (node: ComputedPropertyName, expression: Expression) -> ComputedPropertyName

external var createTypeParameterDeclaration: (name: dynamic /* String | Identifier */, constraint: TypeNode?, defaultType: TypeNode?) -> TypeParameterDeclaration

external var updateTypeParameterDeclaration: (node: TypeParameterDeclaration, name: Identifier, constraint: TypeNode?, defaultType: TypeNode?) -> TypeParameterDeclaration

external var createParameter: (decorators: Array<Decorator>?, modifiers: Array<dynamic /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, dotDotDotToken: DotDotDotToken?, name: dynamic /* String | Identifier | ObjectBindingPattern | ArrayBindingPattern */, questionToken: QuestionToken?, type: TypeNode?, initializer: Expression?) -> ParameterDeclaration

external var updateParameter: (node: ParameterDeclaration, decorators: Array<Decorator>?, modifiers: Array<dynamic /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, dotDotDotToken: DotDotDotToken?, name: dynamic /* String | Identifier | ObjectBindingPattern | ArrayBindingPattern */, questionToken: QuestionToken?, type: TypeNode?, initializer: Expression?) -> ParameterDeclaration

external var createDecorator: (expression: Expression) -> Decorator

external var updateDecorator: (node: Decorator, expression: Expression) -> Decorator

external var createProperty: (decorators: Array<Decorator>?, modifiers: Array<dynamic /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: dynamic /* String | Identifier | StringLiteral | NumericLiteral | ComputedPropertyName | PrivateIdentifier */, questionOrExclamationToken: dynamic /* QuestionToken? | ExclamationToken? */, type: TypeNode?, initializer: Expression?) -> PropertyDeclaration

external var updateProperty: (node: PropertyDeclaration, decorators: Array<Decorator>?, modifiers: Array<dynamic /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: dynamic /* String | Identifier | StringLiteral | NumericLiteral | ComputedPropertyName | PrivateIdentifier */, questionOrExclamationToken: dynamic /* QuestionToken? | ExclamationToken? */, type: TypeNode?, initializer: Expression?) -> PropertyDeclaration

external var createMethod: (decorators: Array<Decorator>?, modifiers: Array<dynamic /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, asteriskToken: AsteriskToken?, name: dynamic /* String | Identifier | StringLiteral | NumericLiteral | ComputedPropertyName | PrivateIdentifier */, questionToken: QuestionToken?, typeParameters: Array<TypeParameterDeclaration>?, parameters: Array<ParameterDeclaration>, type: TypeNode?, body: Block?) -> MethodDeclaration

external var updateMethod: (node: MethodDeclaration, decorators: Array<Decorator>?, modifiers: Array<dynamic /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, asteriskToken: AsteriskToken?, name: dynamic /* Identifier | StringLiteral | NumericLiteral | ComputedPropertyName | PrivateIdentifier */, questionToken: QuestionToken?, typeParameters: Array<TypeParameterDeclaration>?, parameters: Array<ParameterDeclaration>, type: TypeNode?, body: Block?) -> MethodDeclaration

external var createConstructor: (decorators: Array<Decorator>?, modifiers: Array<dynamic /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, parameters: Array<ParameterDeclaration>, body: Block?) -> ConstructorDeclaration

external var updateConstructor: (node: ConstructorDeclaration, decorators: Array<Decorator>?, modifiers: Array<dynamic /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, parameters: Array<ParameterDeclaration>, body: Block?) -> ConstructorDeclaration

external var createGetAccessor: (decorators: Array<Decorator>?, modifiers: Array<dynamic /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: dynamic /* String | Identifier | StringLiteral | NumericLiteral | ComputedPropertyName | PrivateIdentifier */, parameters: Array<ParameterDeclaration>, type: TypeNode?, body: Block?) -> GetAccessorDeclaration

external var updateGetAccessor: (node: GetAccessorDeclaration, decorators: Array<Decorator>?, modifiers: Array<dynamic /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: dynamic /* Identifier | StringLiteral | NumericLiteral | ComputedPropertyName | PrivateIdentifier */, parameters: Array<ParameterDeclaration>, type: TypeNode?, body: Block?) -> GetAccessorDeclaration

external var createSetAccessor: (decorators: Array<Decorator>?, modifiers: Array<dynamic /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: dynamic /* String | Identifier | StringLiteral | NumericLiteral | ComputedPropertyName | PrivateIdentifier */, parameters: Array<ParameterDeclaration>, body: Block?) -> SetAccessorDeclaration

external var updateSetAccessor: (node: SetAccessorDeclaration, decorators: Array<Decorator>?, modifiers: Array<dynamic /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: dynamic /* Identifier | StringLiteral | NumericLiteral | ComputedPropertyName | PrivateIdentifier */, parameters: Array<ParameterDeclaration>, body: Block?) -> SetAccessorDeclaration

external var createCallSignature: (typeParameters: Array<TypeParameterDeclaration>?, parameters: Array<ParameterDeclaration>, type: TypeNode?) -> CallSignatureDeclaration

external var updateCallSignature: (node: CallSignatureDeclaration, typeParameters: NodeArray<TypeParameterDeclaration>?, parameters: NodeArray<ParameterDeclaration>, type: TypeNode?) -> CallSignatureDeclaration

external var createConstructSignature: (typeParameters: Array<TypeParameterDeclaration>?, parameters: Array<ParameterDeclaration>, type: TypeNode?) -> ConstructSignatureDeclaration

external var updateConstructSignature: (node: ConstructSignatureDeclaration, typeParameters: NodeArray<TypeParameterDeclaration>?, parameters: NodeArray<ParameterDeclaration>, type: TypeNode?) -> ConstructSignatureDeclaration

external var updateIndexSignature: (node: IndexSignatureDeclaration, decorators: Array<Decorator>?, modifiers: Array<dynamic /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, parameters: Array<ParameterDeclaration>, type: TypeNode) -> IndexSignatureDeclaration

//external var createKeywordTypeNode: (kind: TKind) -> KeywordTypeNode<TKind>
external fun <TKind : SyntaxKind> createKeywordTypeNode(kind: TKind): KeywordTypeNode<TKind>

external var createTypePredicateNodeWithModifier: (assertsModifier: AssertsKeyword?, parameterName: dynamic /* String | Identifier | ThisTypeNode */, type: TypeNode?) -> TypePredicateNode

external var updateTypePredicateNodeWithModifier: (node: TypePredicateNode, assertsModifier: AssertsKeyword?, parameterName: dynamic /* Identifier | ThisTypeNode */, type: TypeNode?) -> TypePredicateNode

external var createTypeReferenceNode: (typeName: dynamic /* String | Identifier | QualifiedName */, typeArguments: Array<TypeNode>?) -> TypeReferenceNode

external var updateTypeReferenceNode: (node: TypeReferenceNode, typeName: dynamic /* Identifier | QualifiedName */, typeArguments: NodeArray<TypeNode>?) -> TypeReferenceNode

external var createFunctionTypeNode: (typeParameters: Array<TypeParameterDeclaration>?, parameters: Array<ParameterDeclaration>, type: TypeNode) -> FunctionTypeNode

external var updateFunctionTypeNode: (node: FunctionTypeNode, typeParameters: NodeArray<TypeParameterDeclaration>?, parameters: NodeArray<ParameterDeclaration>, type: TypeNode) -> FunctionTypeNode

external var createConstructorTypeNode: (typeParameters: Array<TypeParameterDeclaration>?, parameters: Array<ParameterDeclaration>, type: TypeNode) -> ConstructorTypeNode

external var updateConstructorTypeNode: (node: ConstructorTypeNode, typeParameters: NodeArray<TypeParameterDeclaration>?, parameters: NodeArray<ParameterDeclaration>, type: TypeNode) -> ConstructorTypeNode

external var createTypeQueryNode: (exprName: dynamic /* Identifier | QualifiedName */) -> TypeQueryNode

external var updateTypeQueryNode: (node: TypeQueryNode, exprName: dynamic /* Identifier | QualifiedName */) -> TypeQueryNode

external var createTypeLiteralNode: (members: Array<TypeElement>?) -> TypeLiteralNode

external var updateTypeLiteralNode: (node: TypeLiteralNode, members: NodeArray<TypeElement>) -> TypeLiteralNode

external var createArrayTypeNode: (elementType: TypeNode) -> ArrayTypeNode

external var updateArrayTypeNode: (node: ArrayTypeNode, elementType: TypeNode) -> ArrayTypeNode

external var createTupleTypeNode: (elements: Array<dynamic /* TypeNode | NamedTupleMember */>) -> TupleTypeNode

external var updateTupleTypeNode: (node: TupleTypeNode, elements: Array<dynamic /* TypeNode | NamedTupleMember */>) -> TupleTypeNode

external var createOptionalTypeNode: (type: TypeNode) -> OptionalTypeNode

external var updateOptionalTypeNode: (node: OptionalTypeNode, type: TypeNode) -> OptionalTypeNode

external var createRestTypeNode: (type: TypeNode) -> RestTypeNode

external var updateRestTypeNode: (node: RestTypeNode, type: TypeNode) -> RestTypeNode

external var createUnionTypeNode: (types: Array<TypeNode>) -> UnionTypeNode

external var updateUnionTypeNode: (node: UnionTypeNode, types: NodeArray<TypeNode>) -> UnionTypeNode

external var createIntersectionTypeNode: (types: Array<TypeNode>) -> IntersectionTypeNode

external var updateIntersectionTypeNode: (node: IntersectionTypeNode, types: NodeArray<TypeNode>) -> IntersectionTypeNode

external var createConditionalTypeNode: (checkType: TypeNode, extendsType: TypeNode, trueType: TypeNode, falseType: TypeNode) -> ConditionalTypeNode

external var updateConditionalTypeNode: (node: ConditionalTypeNode, checkType: TypeNode, extendsType: TypeNode, trueType: TypeNode, falseType: TypeNode) -> ConditionalTypeNode

external var createInferTypeNode: (typeParameter: TypeParameterDeclaration) -> InferTypeNode

external var updateInferTypeNode: (node: InferTypeNode, typeParameter: TypeParameterDeclaration) -> InferTypeNode

external var createImportTypeNode: (argument: TypeNode, qualifier: dynamic /* Identifier? | QualifiedName? */, typeArguments: Array<TypeNode>?, isTypeOf: Boolean?) -> ImportTypeNode

external var updateImportTypeNode: (node: ImportTypeNode, argument: TypeNode, qualifier: dynamic /* Identifier? | QualifiedName? */, typeArguments: Array<TypeNode>?, isTypeOf: Boolean?) -> ImportTypeNode

external var createParenthesizedType: (type: TypeNode) -> ParenthesizedTypeNode

external var updateParenthesizedType: (node: ParenthesizedTypeNode, type: TypeNode) -> ParenthesizedTypeNode

external var createThisTypeNode: () -> ThisTypeNode

external var updateTypeOperatorNode: (node: TypeOperatorNode, type: TypeNode) -> TypeOperatorNode

external var createIndexedAccessTypeNode: (objectType: TypeNode, indexType: TypeNode) -> IndexedAccessTypeNode

external var updateIndexedAccessTypeNode: (node: IndexedAccessTypeNode, objectType: TypeNode, indexType: TypeNode) -> IndexedAccessTypeNode

//external var createMappedTypeNode: (readonlyToken: dynamic /* ReadonlyKeyword? | PlusToken? | MinusToken? */, typeParameter: TypeParameterDeclaration, nameType: TypeNode?, questionToken: dynamic /* QuestionToken? | PlusToken? | MinusToken? */, type: TypeNode?) -> MappedTypeNode

external var updateMappedTypeNode: (node: MappedTypeNode, readonlyToken: dynamic /* ReadonlyKeyword? | PlusToken? | MinusToken? */, typeParameter: TypeParameterDeclaration, nameType: TypeNode?, questionToken: dynamic /* QuestionToken? | PlusToken? | MinusToken? */, type: TypeNode?) -> MappedTypeNode

external var createLiteralTypeNode: (literal: dynamic /* LiteralExpression | TrueLiteral | FalseLiteral | PrefixUnaryExpression | NullLiteral */) -> LiteralTypeNode

external var updateLiteralTypeNode: (node: LiteralTypeNode, literal: dynamic /* LiteralExpression | TrueLiteral | FalseLiteral | PrefixUnaryExpression | NullLiteral */) -> LiteralTypeNode

external var createObjectBindingPattern: (elements: Array<BindingElement>) -> ObjectBindingPattern

external var updateObjectBindingPattern: (node: ObjectBindingPattern, elements: Array<BindingElement>) -> ObjectBindingPattern

external var createArrayBindingPattern: (elements: Array<dynamic /* BindingElement | OmittedExpression */>) -> ArrayBindingPattern

external var updateArrayBindingPattern: (node: ArrayBindingPattern, elements: Array<dynamic /* BindingElement | OmittedExpression */>) -> ArrayBindingPattern

external var createBindingElement: (dotDotDotToken: DotDotDotToken?, propertyName: dynamic /* String? | Identifier? | StringLiteral? | NumericLiteral? | ComputedPropertyName? | PrivateIdentifier? */, name: dynamic /* String | Identifier | ObjectBindingPattern | ArrayBindingPattern */, initializer: Expression?) -> BindingElement

external var updateBindingElement: (node: BindingElement, dotDotDotToken: DotDotDotToken?, propertyName: dynamic /* Identifier? | StringLiteral? | NumericLiteral? | ComputedPropertyName? | PrivateIdentifier? */, name: dynamic /* Identifier | ObjectBindingPattern | ArrayBindingPattern */, initializer: Expression?) -> BindingElement

external var createArrayLiteral: (elements: Array<Expression>?, multiLine: Boolean?) -> ArrayLiteralExpression

external var updateArrayLiteral: (node: ArrayLiteralExpression, elements: Array<Expression>) -> ArrayLiteralExpression

external var createObjectLiteral: (properties: Array<dynamic /* PropertyAssignment | ShorthandPropertyAssignment | SpreadAssignment | MethodDeclaration | GetAccessorDeclaration | SetAccessorDeclaration */>?, multiLine: Boolean?) -> ObjectLiteralExpression

external var updateObjectLiteral: (node: ObjectLiteralExpression, properties: Array<dynamic /* PropertyAssignment | ShorthandPropertyAssignment | SpreadAssignment | MethodDeclaration | GetAccessorDeclaration | SetAccessorDeclaration */>) -> ObjectLiteralExpression

external var createPropertyAccess: (expression: Expression, name: dynamic /* String | Identifier | PrivateIdentifier */) -> PropertyAccessExpression

external var updatePropertyAccess: (node: PropertyAccessExpression, expression: Expression, name: dynamic /* Identifier | PrivateIdentifier */) -> PropertyAccessExpression

external var createPropertyAccessChain: (expression: Expression, questionDotToken: QuestionDotToken?, name: dynamic /* String | Identifier | PrivateIdentifier */) -> PropertyAccessChain

external var updatePropertyAccessChain: (node: PropertyAccessChain, expression: Expression, questionDotToken: QuestionDotToken?, name: dynamic /* Identifier | PrivateIdentifier */) -> PropertyAccessChain

external var createElementAccess: (expression: Expression, index: dynamic /* Number | Expression */) -> ElementAccessExpression

external var updateElementAccess: (node: ElementAccessExpression, expression: Expression, argumentExpression: Expression) -> ElementAccessExpression

external var createElementAccessChain: (expression: Expression, questionDotToken: QuestionDotToken?, index: dynamic /* Number | Expression */) -> ElementAccessChain

external var updateElementAccessChain: (node: ElementAccessChain, expression: Expression, questionDotToken: QuestionDotToken?, argumentExpression: Expression) -> ElementAccessChain

external var createCall: (expression: Expression, typeArguments: Array<TypeNode>?, argumentsArray: Array<Expression>?) -> CallExpression

external var updateCall: (node: CallExpression, expression: Expression, typeArguments: Array<TypeNode>?, argumentsArray: Array<Expression>) -> CallExpression

external var createCallChain: (expression: Expression, questionDotToken: QuestionDotToken?, typeArguments: Array<TypeNode>?, argumentsArray: Array<Expression>?) -> CallChain

external var updateCallChain: (node: CallChain, expression: Expression, questionDotToken: QuestionDotToken?, typeArguments: Array<TypeNode>?, argumentsArray: Array<Expression>) -> CallChain

external var createNew: (expression: Expression, typeArguments: Array<TypeNode>?, argumentsArray: Array<Expression>?) -> NewExpression

external var updateNew: (node: NewExpression, expression: Expression, typeArguments: Array<TypeNode>?, argumentsArray: Array<Expression>?) -> NewExpression

external var createTypeAssertion: (type: TypeNode, expression: Expression) -> TypeAssertion

external var updateTypeAssertion: (node: TypeAssertion, type: TypeNode, expression: Expression) -> TypeAssertion

external var createParen: (expression: Expression) -> ParenthesizedExpression

external var updateParen: (node: ParenthesizedExpression, expression: Expression) -> ParenthesizedExpression

external var createFunctionExpression: (modifiers: Array<dynamic /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, asteriskToken: AsteriskToken?, name: dynamic /* String? | Identifier? */, typeParameters: Array<TypeParameterDeclaration>?, parameters: Array<ParameterDeclaration>?, type: TypeNode?, body: Block) -> FunctionExpression

external var updateFunctionExpression: (node: FunctionExpression, modifiers: Array<dynamic /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, asteriskToken: AsteriskToken?, name: Identifier?, typeParameters: Array<TypeParameterDeclaration>?, parameters: Array<ParameterDeclaration>, type: TypeNode?, body: Block) -> FunctionExpression

external var createDelete: (expression: Expression) -> DeleteExpression

external var updateDelete: (node: DeleteExpression, expression: Expression) -> DeleteExpression

external var createTypeOf: (expression: Expression) -> TypeOfExpression

external var updateTypeOf: (node: TypeOfExpression, expression: Expression) -> TypeOfExpression

external var createVoid: (expression: Expression) -> VoidExpression

external var updateVoid: (node: VoidExpression, expression: Expression) -> VoidExpression

external var createAwait: (expression: Expression) -> AwaitExpression

external var updateAwait: (node: AwaitExpression, expression: Expression) -> AwaitExpression

external var createPrefix: (operator: dynamic /* SyntaxKind.PlusPlusToken | SyntaxKind.MinusMinusToken | SyntaxKind.PlusToken | SyntaxKind.MinusToken | SyntaxKind.TildeToken | SyntaxKind.ExclamationToken */, operand: Expression) -> PrefixUnaryExpression

external var updatePrefix: (node: PrefixUnaryExpression, operand: Expression) -> PrefixUnaryExpression

external var createPostfix: (operand: Expression, operator: dynamic /* SyntaxKind.PlusPlusToken | SyntaxKind.MinusMinusToken */) -> PostfixUnaryExpression

external var updatePostfix: (node: PostfixUnaryExpression, operand: Expression) -> PostfixUnaryExpression

external var createBinary: (
  left: Expression, operator: dynamic /* SyntaxKind.QuestionQuestionToken | ExponentiationOperator | SyntaxKind.AsteriskToken | SyntaxKind.SlashToken | SyntaxKind.PercentToken | SyntaxKind.PlusToken | SyntaxKind.MinusToken | SyntaxKind.LessThanLessThanToken | SyntaxKind.GreaterThanGreaterThanToken | SyntaxKind.GreaterThanGreaterThanGreaterThanToken | SyntaxKind.LessThanToken | SyntaxKind.LessThanEqualsToken | SyntaxKind.GreaterThanToken | SyntaxKind.GreaterThanEqualsToken | SyntaxKind.InstanceOfKeyword | SyntaxKind.InKeyword | SyntaxKind.EqualsEqualsToken | SyntaxKind.EqualsEqualsEqualsToken | SyntaxKind.ExclamationEqualsEqualsToken | SyntaxKind.ExclamationEqualsToken | SyntaxKind.AmpersandToken | SyntaxKind.BarToken | SyntaxKind.CaretToken | SyntaxKind.AmpersandAmpersandToken | SyntaxKind.BarBarToken | SyntaxKind.EqualsToken | SyntaxKind.PlusEqualsToken | SyntaxKind.MinusEqualsToken | SyntaxKind.AsteriskAsteriskEqualsToken | SyntaxKind.AsteriskEqualsToken | SyntaxKind.SlashEqualsToken | SyntaxKind.PercentEqualsToken | SyntaxKind.AmpersandEqualsToken | SyntaxKind.BarEqualsToken | SyntaxKind.CaretEqualsToken | SyntaxKind.LessThanLessThanEqualsToken | SyntaxKind.GreaterThanGreaterThanGreaterThanEqualsToken | SyntaxKind.GreaterThanGreaterThanEqualsToken | SyntaxKind.BarBarEqualsToken | SyntaxKind.AmpersandAmpersandEqualsToken | SyntaxKind.QuestionQuestionEqualsToken | SyntaxKind.CommaToken | BinaryOperatorToken */, right: Expression
) -> BinaryExpression

external var updateConditional: (node: ConditionalExpression, condition: Expression, questionToken: QuestionToken, whenTrue: Expression, colonToken: ColonToken, whenFalse: Expression) -> ConditionalExpression

external var createTemplateExpression: (head: TemplateHead, templateSpans: Array<TemplateSpan>) -> TemplateExpression

external var updateTemplateExpression: (node: TemplateExpression, head: TemplateHead, templateSpans: Array<TemplateSpan>) -> TemplateExpression

external object createTemplateHead {
  @nativeInvoke
  operator fun invoke(text: String, rawText: String? = definedExternally, templateFlags: TokenFlags? = definedExternally): TemplateHead
  @nativeInvoke
  operator fun invoke(text: String?, rawText: String, templateFlags: TokenFlags? = definedExternally): TemplateHead
}

external object createTemplateMiddle {
  @nativeInvoke
  operator fun invoke(text: String, rawText: String? = definedExternally, templateFlags: TokenFlags? = definedExternally): TemplateMiddle
  @nativeInvoke
  operator fun invoke(text: String?, rawText: String, templateFlags: TokenFlags? = definedExternally): TemplateMiddle
}

external object createTemplateTail {
  @nativeInvoke
  operator fun invoke(text: String, rawText: String? = definedExternally, templateFlags: TokenFlags? = definedExternally): TemplateTail
  @nativeInvoke
  operator fun invoke(text: String?, rawText: String, templateFlags: TokenFlags? = definedExternally): TemplateTail
}

external object createNoSubstitutionTemplateLiteral {
  @nativeInvoke
  operator fun invoke(text: String, rawText: String? = definedExternally): NoSubstitutionTemplateLiteral
  @nativeInvoke
  operator fun invoke(text: String?, rawText: String): NoSubstitutionTemplateLiteral
}

external var updateYield: (node: YieldExpression, asteriskToken: AsteriskToken?, expression: Expression?) -> YieldExpression

external var createSpread: (expression: Expression) -> SpreadElement

external var updateSpread: (node: SpreadElement, expression: Expression) -> SpreadElement

external var createOmittedExpression: () -> OmittedExpression

external var createAsExpression: (expression: Expression, type: TypeNode) -> AsExpression

external var updateAsExpression: (node: AsExpression, expression: Expression, type: TypeNode) -> AsExpression

external var createNonNullExpression: (expression: Expression) -> NonNullExpression

external var updateNonNullExpression: (node: NonNullExpression, expression: Expression) -> NonNullExpression

external var createNonNullChain: (expression: Expression) -> NonNullChain

external var updateNonNullChain: (node: NonNullChain, expression: Expression) -> NonNullChain

external var createMetaProperty: (keywordToken: dynamic /* SyntaxKind.ImportKeyword | SyntaxKind.NewKeyword */, name: Identifier) -> MetaProperty

external var updateMetaProperty: (node: MetaProperty, name: Identifier) -> MetaProperty

external var createTemplateSpan: (expression: Expression, literal: dynamic /* TemplateMiddle | TemplateTail */) -> TemplateSpan

external var updateTemplateSpan: (node: TemplateSpan, expression: Expression, literal: dynamic /* TemplateMiddle | TemplateTail */) -> TemplateSpan

external var createSemicolonClassElement: () -> SemicolonClassElement

external var createBlock: (statements: Array<Statement>, multiLine: Boolean?) -> Block

external var updateBlock: (node: Block, statements: Array<Statement>) -> Block

external var createVariableStatement: (modifiers: Array<dynamic /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, declarationList: dynamic /* VariableDeclarationList | Array<VariableDeclaration> */) -> VariableStatement

external var updateVariableStatement: (node: VariableStatement, modifiers: Array<dynamic /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, declarationList: VariableDeclarationList) -> VariableStatement

external var createEmptyStatement: () -> EmptyStatement

external var createExpressionStatement: (expression: Expression) -> ExpressionStatement

external var updateExpressionStatement: (node: ExpressionStatement, expression: Expression) -> ExpressionStatement

external var createStatement: (expression: Expression) -> ExpressionStatement

external var updateStatement: (node: ExpressionStatement, expression: Expression) -> ExpressionStatement

external var createIf: (expression: Expression, thenStatement: Statement, elseStatement: Statement?) -> IfStatement

external var updateIf: (node: IfStatement, expression: Expression, thenStatement: Statement, elseStatement: Statement?) -> IfStatement

external var createDo: (statement: Statement, expression: Expression) -> DoStatement

external var updateDo: (node: DoStatement, statement: Statement, expression: Expression) -> DoStatement

external var createWhile: (expression: Expression, statement: Statement) -> WhileStatement

external var updateWhile: (node: WhileStatement, expression: Expression, statement: Statement) -> WhileStatement

external var createFor: (initializer: dynamic /* VariableDeclarationList? | Expression? */, condition: Expression?, incrementor: Expression?, statement: Statement) -> ForStatement

external var updateFor: (node: ForStatement, initializer: dynamic /* VariableDeclarationList? | Expression? */, condition: Expression?, incrementor: Expression?, statement: Statement) -> ForStatement

external var createForIn: (initializer: dynamic /* VariableDeclarationList | Expression */, expression: Expression, statement: Statement) -> ForInStatement

external var updateForIn: (node: ForInStatement, initializer: dynamic /* VariableDeclarationList | Expression */, expression: Expression, statement: Statement) -> ForInStatement

external var createForOf: (awaitModifier: AwaitKeyword?, initializer: dynamic /* VariableDeclarationList | Expression */, expression: Expression, statement: Statement) -> ForOfStatement

external var updateForOf: (node: ForOfStatement, awaitModifier: AwaitKeyword?, initializer: dynamic /* VariableDeclarationList | Expression */, expression: Expression, statement: Statement) -> ForOfStatement

external var createContinue: (label: dynamic /* String? | Identifier? */) -> ContinueStatement

external var updateContinue: (node: ContinueStatement, label: Identifier?) -> ContinueStatement

external var createBreak: (label: dynamic /* String? | Identifier? */) -> BreakStatement

external var updateBreak: (node: BreakStatement, label: Identifier?) -> BreakStatement

external var createReturn: (expression: Expression?) -> ReturnStatement

external var updateReturn: (node: ReturnStatement, expression: Expression?) -> ReturnStatement

external var createWith: (expression: Expression, statement: Statement) -> WithStatement

external var updateWith: (node: WithStatement, expression: Expression, statement: Statement) -> WithStatement

external var createSwitch: (expression: Expression, caseBlock: CaseBlock) -> SwitchStatement

external var updateSwitch: (node: SwitchStatement, expression: Expression, caseBlock: CaseBlock) -> SwitchStatement

external var createLabel: (label: dynamic /* String | Identifier */, statement: Statement) -> LabeledStatement

external var updateLabel: (node: LabeledStatement, label: Identifier, statement: Statement) -> LabeledStatement

external var createThrow: (expression: Expression) -> ThrowStatement

external var updateThrow: (node: ThrowStatement, expression: Expression) -> ThrowStatement

external var createTry: (tryBlock: Block, catchClause: CatchClause?, finallyBlock: Block?) -> TryStatement

external var updateTry: (node: TryStatement, tryBlock: Block, catchClause: CatchClause?, finallyBlock: Block?) -> TryStatement

external var createDebuggerStatement: () -> DebuggerStatement

external var createVariableDeclarationList: (declarations: Array<VariableDeclaration>, flags: NodeFlags?) -> VariableDeclarationList

external var updateVariableDeclarationList: (node: VariableDeclarationList, declarations: Array<VariableDeclaration>) -> VariableDeclarationList

external var createFunctionDeclaration: (decorators: Array<Decorator>?, modifiers: Array<dynamic /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, asteriskToken: AsteriskToken?, name: dynamic /* String? | Identifier? */, typeParameters: Array<TypeParameterDeclaration>?, parameters: Array<ParameterDeclaration>, type: TypeNode?, body: Block?) -> FunctionDeclaration

external var updateFunctionDeclaration: (node: FunctionDeclaration, decorators: Array<Decorator>?, modifiers: Array<dynamic /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, asteriskToken: AsteriskToken?, name: Identifier?, typeParameters: Array<TypeParameterDeclaration>?, parameters: Array<ParameterDeclaration>, type: TypeNode?, body: Block?) -> FunctionDeclaration

external var createClassDeclaration: (decorators: Array<Decorator>?, modifiers: Array<dynamic /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: dynamic /* String? | Identifier? */, typeParameters: Array<TypeParameterDeclaration>?, heritageClauses: Array<HeritageClause>?, members: Array<ClassElement>) -> ClassDeclaration

external var updateClassDeclaration: (node: ClassDeclaration, decorators: Array<Decorator>?, modifiers: Array<dynamic /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: Identifier?, typeParameters: Array<TypeParameterDeclaration>?, heritageClauses: Array<HeritageClause>?, members: Array<ClassElement>) -> ClassDeclaration

external var createInterfaceDeclaration: (decorators: Array<Decorator>?, modifiers: Array<dynamic /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: dynamic /* String | Identifier */, typeParameters: Array<TypeParameterDeclaration>?, heritageClauses: Array<HeritageClause>?, members: Array<TypeElement>) -> InterfaceDeclaration

external var updateInterfaceDeclaration: (node: InterfaceDeclaration, decorators: Array<Decorator>?, modifiers: Array<dynamic /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: Identifier, typeParameters: Array<TypeParameterDeclaration>?, heritageClauses: Array<HeritageClause>?, members: Array<TypeElement>) -> InterfaceDeclaration

external var createTypeAliasDeclaration: (decorators: Array<Decorator>?, modifiers: Array<dynamic /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: dynamic /* String | Identifier */, typeParameters: Array<TypeParameterDeclaration>?, type: TypeNode) -> TypeAliasDeclaration

external var updateTypeAliasDeclaration: (node: TypeAliasDeclaration, decorators: Array<Decorator>?, modifiers: Array<dynamic /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: Identifier, typeParameters: Array<TypeParameterDeclaration>?, type: TypeNode) -> TypeAliasDeclaration

external var createEnumDeclaration: (decorators: Array<Decorator>?, modifiers: Array<dynamic /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: dynamic /* String | Identifier */, members: Array<EnumMember>) -> EnumDeclaration

external var updateEnumDeclaration: (node: EnumDeclaration, decorators: Array<Decorator>?, modifiers: Array<dynamic /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: Identifier, members: Array<EnumMember>) -> EnumDeclaration

external var createModuleDeclaration: (decorators: Array<Decorator>?, modifiers: Array<dynamic /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: dynamic /* Identifier | StringLiteral */, body: dynamic /* ModuleBlock? | NamespaceDeclaration? | Identifier? | JSDocNamespaceDeclaration? */, flags: NodeFlags?) -> ModuleDeclaration

external var updateModuleDeclaration: (node: ModuleDeclaration, decorators: Array<Decorator>?, modifiers: Array<dynamic /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: dynamic /* Identifier | StringLiteral */, body: dynamic /* ModuleBlock? | NamespaceDeclaration? | Identifier? | JSDocNamespaceDeclaration? */) -> ModuleDeclaration

external var createModuleBlock: (statements: Array<Statement>) -> ModuleBlock

external var updateModuleBlock: (node: ModuleBlock, statements: Array<Statement>) -> ModuleBlock

external var createCaseBlock: (clauses: Array<dynamic /* CaseClause | DefaultClause */>) -> CaseBlock

external var updateCaseBlock: (node: CaseBlock, clauses: Array<dynamic /* CaseClause | DefaultClause */>) -> CaseBlock

external var createNamespaceExportDeclaration: (name: dynamic /* String | Identifier */) -> NamespaceExportDeclaration

external var updateNamespaceExportDeclaration: (node: NamespaceExportDeclaration, name: Identifier) -> NamespaceExportDeclaration

external var createImportEqualsDeclaration: (decorators: Array<Decorator>?, modifiers: Array<dynamic /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, isTypeOnly: Boolean, name: dynamic /* String | Identifier */, moduleReference: dynamic /* Identifier | QualifiedName | ExternalModuleReference */) -> ImportEqualsDeclaration

external var updateImportEqualsDeclaration: (node: ImportEqualsDeclaration, decorators: Array<Decorator>?, modifiers: Array<dynamic /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, isTypeOnly: Boolean, name: Identifier, moduleReference: dynamic /* Identifier | QualifiedName | ExternalModuleReference */) -> ImportEqualsDeclaration

external var createImportDeclaration: (decorators: Array<Decorator>?, modifiers: Array<dynamic /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, importClause: ImportClause?, moduleSpecifier: Expression) -> ImportDeclaration

external var updateImportDeclaration: (node: ImportDeclaration, decorators: Array<Decorator>?, modifiers: Array<dynamic /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, importClause: ImportClause?, moduleSpecifier: Expression) -> ImportDeclaration

external var createNamespaceImport: (name: Identifier) -> NamespaceImport

external var updateNamespaceImport: (node: NamespaceImport, name: Identifier) -> NamespaceImport

external var createNamedImports: (elements: Array<ImportSpecifier>) -> NamedImports

external var updateNamedImports: (node: NamedImports, elements: Array<ImportSpecifier>) -> NamedImports

external var createImportSpecifier: (propertyName: Identifier?, name: Identifier) -> ImportSpecifier

external var updateImportSpecifier: (node: ImportSpecifier, propertyName: Identifier?, name: Identifier) -> ImportSpecifier

external var createExportAssignment: (decorators: Array<Decorator>?, modifiers: Array<dynamic /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, isExportEquals: Boolean?, expression: Expression) -> ExportAssignment

external var updateExportAssignment: (node: ExportAssignment, decorators: Array<Decorator>?, modifiers: Array<dynamic /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, expression: Expression) -> ExportAssignment

external var createNamedExports: (elements: Array<ExportSpecifier>) -> NamedExports

external var updateNamedExports: (node: NamedExports, elements: Array<ExportSpecifier>) -> NamedExports

external var createExportSpecifier: (propertyName: dynamic /* String? | Identifier? */, name: dynamic /* String | Identifier */) -> ExportSpecifier

external var updateExportSpecifier: (node: ExportSpecifier, propertyName: Identifier?, name: Identifier) -> ExportSpecifier

external var createExternalModuleReference: (expression: Expression) -> ExternalModuleReference

external var updateExternalModuleReference: (node: ExternalModuleReference, expression: Expression) -> ExternalModuleReference

external var createJSDocTypeExpression: (type: TypeNode) -> JSDocTypeExpression

external var createJSDocTypeTag: (tagName: Identifier?, typeExpression: JSDocTypeExpression, comment: dynamic /* String? | NodeArray<dynamic /* JSDocText | JSDocLink */>? */) -> JSDocTypeTag

external var createJSDocReturnTag: (tagName: Identifier?, typeExpression: JSDocTypeExpression?, comment: dynamic /* String? | NodeArray<dynamic /* JSDocText | JSDocLink */>? */) -> JSDocReturnTag

external var createJSDocThisTag: (tagName: Identifier?, typeExpression: JSDocTypeExpression, comment: dynamic /* String? | NodeArray<dynamic /* JSDocText | JSDocLink */>? */) -> JSDocThisTag

external var createJSDocComment: (comment: dynamic /* String? | NodeArray<dynamic /* JSDocText | JSDocLink */>? */, tags: Array<JSDocTag>?) -> JSDoc

external var createJSDocParameterTag: (tagName: Identifier?, name: dynamic /* Identifier | QualifiedName */, isBracketed: Boolean, typeExpression: JSDocTypeExpression?, isNameFirst: Boolean?, comment: dynamic /* String? | NodeArray<dynamic /* JSDocText | JSDocLink */>? */) -> JSDocParameterTag

external var createJSDocClassTag: (tagName: Identifier?, comment: dynamic /* String? | NodeArray<dynamic /* JSDocText | JSDocLink */>? */) -> JSDocClassTag

external var createJSDocAugmentsTag: (tagName: Identifier?, className: ExpressionWithTypeArguments /* ExpressionWithTypeArguments & `T$4` */, comment: dynamic /* String? | NodeArray<dynamic /* JSDocText | JSDocLink */>? */) -> JSDocAugmentsTag

external var createJSDocEnumTag: (tagName: Identifier?, typeExpression: JSDocTypeExpression, comment: dynamic /* String? | NodeArray<dynamic /* JSDocText | JSDocLink */>? */) -> JSDocEnumTag

external var createJSDocTemplateTag: (tagName: Identifier?, constraint: JSDocTypeExpression?, typeParameters: Array<TypeParameterDeclaration>, comment: dynamic /* String? | NodeArray<dynamic /* JSDocText | JSDocLink */>? */) -> JSDocTemplateTag

external var createJSDocTypedefTag: (tagName: Identifier?, typeExpression: dynamic /* JSDocTypeLiteral? | JSDocTypeExpression? */, fullName: dynamic /* Identifier? | JSDocNamespaceDeclaration? */, comment: dynamic /* String? | NodeArray<dynamic /* JSDocText | JSDocLink */>? */) -> JSDocTypedefTag

external var createJSDocCallbackTag: (tagName: Identifier?, typeExpression: JSDocSignature, fullName: dynamic /* Identifier? | JSDocNamespaceDeclaration? */, comment: dynamic /* String? | NodeArray<dynamic /* JSDocText | JSDocLink */>? */) -> JSDocCallbackTag

external var createJSDocSignature: (typeParameters: Array<JSDocTemplateTag>?, parameters: Array<JSDocParameterTag>, type: JSDocReturnTag?) -> JSDocSignature

external var createJSDocPropertyTag: (tagName: Identifier?, name: dynamic /* Identifier | QualifiedName */, isBracketed: Boolean, typeExpression: JSDocTypeExpression?, isNameFirst: Boolean?, comment: dynamic /* String? | NodeArray<dynamic /* JSDocText | JSDocLink */>? */) -> JSDocPropertyTag

external var createJSDocTypeLiteral: (jsDocPropertyTags: Array<JSDocPropertyLikeTag>?, isArrayType: Boolean?) -> JSDocTypeLiteral

external var createJSDocImplementsTag: (tagName: Identifier?, className: ExpressionWithTypeArguments /* ExpressionWithTypeArguments & `T$4` */, comment: dynamic /* String? | NodeArray<dynamic /* JSDocText | JSDocLink */>? */) -> JSDocImplementsTag

external var createJSDocAuthorTag: (tagName: Identifier?, comment: dynamic /* String? | NodeArray<dynamic /* JSDocText | JSDocLink */>? */) -> JSDocAuthorTag

external var createJSDocPublicTag: (tagName: Identifier?, comment: dynamic /* String? | NodeArray<dynamic /* JSDocText | JSDocLink */>? */) -> JSDocPublicTag

external var createJSDocPrivateTag: (tagName: Identifier?, comment: dynamic /* String? | NodeArray<dynamic /* JSDocText | JSDocLink */>? */) -> JSDocPrivateTag

external var createJSDocProtectedTag: (tagName: Identifier?, comment: dynamic /* String? | NodeArray<dynamic /* JSDocText | JSDocLink */>? */) -> JSDocProtectedTag

external var createJSDocReadonlyTag: (tagName: Identifier?, comment: dynamic /* String? | NodeArray<dynamic /* JSDocText | JSDocLink */>? */) -> JSDocReadonlyTag

external var createJSDocTag: (tagName: Identifier, comment: dynamic /* String? | NodeArray<dynamic /* JSDocText | JSDocLink */>? */) -> JSDocUnknownTag

external var createJsxElement: (openingElement: JsxOpeningElement, children: Array<dynamic /* JsxText | JsxExpression | JsxElement | JsxSelfClosingElement | JsxFragment */>, closingElement: JsxClosingElement) -> JsxElement

external var updateJsxElement: (node: JsxElement, openingElement: JsxOpeningElement, children: Array<dynamic /* JsxText | JsxExpression | JsxElement | JsxSelfClosingElement | JsxFragment */>, closingElement: JsxClosingElement) -> JsxElement

external var createJsxSelfClosingElement: (tagName: dynamic /* Identifier | ThisExpression | JsxTagNamePropertyAccess */, typeArguments: Array<TypeNode>?, attributes: JsxAttributes) -> JsxSelfClosingElement

external var updateJsxSelfClosingElement: (node: JsxSelfClosingElement, tagName: dynamic /* Identifier | ThisExpression | JsxTagNamePropertyAccess */, typeArguments: Array<TypeNode>?, attributes: JsxAttributes) -> JsxSelfClosingElement

external var createJsxOpeningElement: (tagName: dynamic /* Identifier | ThisExpression | JsxTagNamePropertyAccess */, typeArguments: Array<TypeNode>?, attributes: JsxAttributes) -> JsxOpeningElement

external var updateJsxOpeningElement: (node: JsxOpeningElement, tagName: dynamic /* Identifier | ThisExpression | JsxTagNamePropertyAccess */, typeArguments: Array<TypeNode>?, attributes: JsxAttributes) -> JsxOpeningElement

external var createJsxClosingElement: (tagName: dynamic /* Identifier | ThisExpression | JsxTagNamePropertyAccess */) -> JsxClosingElement

external var updateJsxClosingElement: (node: JsxClosingElement, tagName: dynamic /* Identifier | ThisExpression | JsxTagNamePropertyAccess */) -> JsxClosingElement

external var createJsxFragment: (openingFragment: JsxOpeningFragment, children: Array<dynamic /* JsxText | JsxExpression | JsxElement | JsxSelfClosingElement | JsxFragment */>, closingFragment: JsxClosingFragment) -> JsxFragment

external var createJsxText: (text: String, containsOnlyTriviaWhiteSpaces: Boolean?) -> JsxText

external var updateJsxText: (node: JsxText, text: String, containsOnlyTriviaWhiteSpaces: Boolean?) -> JsxText

external var createJsxOpeningFragment: () -> JsxOpeningFragment

external var createJsxJsxClosingFragment: () -> JsxClosingFragment

external var updateJsxFragment: (node: JsxFragment, openingFragment: JsxOpeningFragment, children: Array<dynamic /* JsxText | JsxExpression | JsxElement | JsxSelfClosingElement | JsxFragment */>, closingFragment: JsxClosingFragment) -> JsxFragment

external var createJsxAttribute: (name: Identifier, initializer: dynamic /* StringLiteral? | JsxExpression? */) -> JsxAttribute

external var updateJsxAttribute: (node: JsxAttribute, name: Identifier, initializer: dynamic /* StringLiteral? | JsxExpression? */) -> JsxAttribute

external var createJsxAttributes: (properties: Array<dynamic /* JsxAttribute | JsxSpreadAttribute */>) -> JsxAttributes

external var updateJsxAttributes: (node: JsxAttributes, properties: Array<dynamic /* JsxAttribute | JsxSpreadAttribute */>) -> JsxAttributes

external var createJsxSpreadAttribute: (expression: Expression) -> JsxSpreadAttribute

external var updateJsxSpreadAttribute: (node: JsxSpreadAttribute, expression: Expression) -> JsxSpreadAttribute

external var createJsxExpression: (dotDotDotToken: DotDotDotToken?, expression: Expression?) -> JsxExpression

external var updateJsxExpression: (node: JsxExpression, expression: Expression?) -> JsxExpression

external var createCaseClause: (expression: Expression, statements: Array<Statement>) -> CaseClause

external var updateCaseClause: (node: CaseClause, expression: Expression, statements: Array<Statement>) -> CaseClause

external var createDefaultClause: (statements: Array<Statement>) -> DefaultClause

external var updateDefaultClause: (node: DefaultClause, statements: Array<Statement>) -> DefaultClause

external var createHeritageClause: (token: dynamic /* SyntaxKind.ExtendsKeyword | SyntaxKind.ImplementsKeyword */, types: Array<ExpressionWithTypeArguments>) -> HeritageClause

external var updateHeritageClause: (node: HeritageClause, types: Array<ExpressionWithTypeArguments>) -> HeritageClause

external var createCatchClause: (variableDeclaration: dynamic /* String? | VariableDeclaration? */, block: Block) -> CatchClause

external var updateCatchClause: (node: CatchClause, variableDeclaration: VariableDeclaration?, block: Block) -> CatchClause

external var createPropertyAssignment: (name: dynamic /* String | Identifier | StringLiteral | NumericLiteral | ComputedPropertyName | PrivateIdentifier */, initializer: Expression) -> PropertyAssignment

external var updatePropertyAssignment: (node: PropertyAssignment, name: dynamic /* Identifier | StringLiteral | NumericLiteral | ComputedPropertyName | PrivateIdentifier */, initializer: Expression) -> PropertyAssignment

external var createShorthandPropertyAssignment: (name: dynamic /* String | Identifier */, objectAssignmentInitializer: Expression?) -> ShorthandPropertyAssignment

external var updateShorthandPropertyAssignment: (node: ShorthandPropertyAssignment, name: Identifier, objectAssignmentInitializer: Expression?) -> ShorthandPropertyAssignment

external var createSpreadAssignment: (expression: Expression) -> SpreadAssignment

external var updateSpreadAssignment: (node: SpreadAssignment, expression: Expression) -> SpreadAssignment

external var createEnumMember: (name: dynamic /* String | Identifier | StringLiteral | NumericLiteral | ComputedPropertyName | PrivateIdentifier */, initializer: Expression?) -> EnumMember

external var updateEnumMember: (node: EnumMember, name: dynamic /* Identifier | StringLiteral | NumericLiteral | ComputedPropertyName | PrivateIdentifier */, initializer: Expression?) -> EnumMember

external var updateSourceFileNode: (node: SourceFile, statements: Array<Statement>, isDeclarationFile: Boolean?, referencedFiles: Array<FileReference>?, typeReferences: Array<FileReference>?, hasNoDefaultLib: Boolean?, libReferences: Array<FileReference>?) -> SourceFile

external var createNotEmittedStatement: (original: Node) -> NotEmittedStatement

external var createPartiallyEmittedExpression: (expression: Expression, original: Node?) -> PartiallyEmittedExpression

external var updatePartiallyEmittedExpression: (node: PartiallyEmittedExpression, expression: Expression) -> PartiallyEmittedExpression

external var createCommaList: (elements: Array<Expression>) -> CommaListExpression

external var updateCommaList: (node: CommaListExpression, elements: Array<Expression>) -> CommaListExpression

external var createBundle: (sourceFiles: Array<SourceFile>, prepends: Array<dynamic /* UnparsedSource | InputFiles */>?) -> Bundle

external var updateBundle: (node: Bundle, sourceFiles: Array<SourceFile>, prepends: Array<dynamic /* UnparsedSource | InputFiles */>?) -> Bundle

external object createImmediatelyInvokedFunctionExpression {
  @nativeInvoke
  operator fun invoke(statements: Array<Statement>): CallExpression
  @nativeInvoke
  operator fun invoke(statements: Array<Statement>, param: ParameterDeclaration, paramValue: Expression): CallExpression
}

external object createImmediatelyInvokedArrowFunction {
  @nativeInvoke
  operator fun invoke(statements: Array<Statement>): CallExpression
  @nativeInvoke
  operator fun invoke(statements: Array<Statement>, param: ParameterDeclaration, paramValue: Expression): CallExpression
}

external var createVoidZero: () -> VoidExpression

external var createExportDefault: (expression: Expression) -> ExportAssignment

external var createExternalModuleExport: (exportName: Identifier) -> ExportDeclaration

external var createNamespaceExport: (name: Identifier) -> NamespaceExport

external var updateNamespaceExport: (node: NamespaceExport, name: Identifier) -> NamespaceExport

external fun <TKind : SyntaxKind> createToken(kind: TKind): Token<TKind>

external var createIdentifier: (text: String) -> Identifier

external var createTempVariable: (recordTempVariable: ((node: Identifier) -> Unit)?) -> Identifier

external var getGeneratedNameForNode: (node: Node?) -> Identifier

external var createOptimisticUniqueName: (text: String) -> Identifier

external var createFileLevelUniqueName: (text: String) -> Identifier

external var createIndexSignature: (decorators: Array<Decorator>?, modifiers: Array<dynamic /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, parameters: Array<ParameterDeclaration>, type: TypeNode) -> IndexSignatureDeclaration

external var createTypePredicateNode: (parameterName: dynamic /* Identifier | ThisTypeNode | String */, type: TypeNode) -> TypePredicateNode

external var updateTypePredicateNode: (node: TypePredicateNode, parameterName: dynamic /* Identifier | ThisTypeNode */, type: TypeNode) -> TypePredicateNode

external object createLiteral {
  @nativeInvoke
  operator fun invoke(value: Any /* String | StringLiteral | NoSubstitutionTemplateLiteral | NumericLiteral | Identifier */): StringLiteral
  @nativeInvoke
  operator fun invoke(value: Any /* Number | PseudoBigInt */): NumericLiteral
  @nativeInvoke
  operator fun invoke(value: Boolean): dynamic /* TrueLiteral | FalseLiteral */
  @nativeInvoke
  operator fun invoke(value: Any /* String | Number | PseudoBigInt | Boolean */): PrimaryExpression
}

external var createMethodSignature: (typeParameters: Array<TypeParameterDeclaration>?, parameters: Array<ParameterDeclaration>, type: TypeNode?, name: dynamic /* String | Identifier | StringLiteral | NumericLiteral | ComputedPropertyName | PrivateIdentifier */, questionToken: QuestionToken?) -> MethodSignature

external var updateMethodSignature: (node: MethodSignature, typeParameters: NodeArray<TypeParameterDeclaration>?, parameters: NodeArray<ParameterDeclaration>, type: TypeNode?, name: dynamic /* Identifier | StringLiteral | NumericLiteral | ComputedPropertyName | PrivateIdentifier */, questionToken: QuestionToken?) -> MethodSignature

external object createTypeOperatorNode {
  @nativeInvoke
  operator fun invoke(type: TypeNode): TypeOperatorNode
  @nativeInvoke
  operator fun invoke(operator: Any /* SyntaxKind.KeyOfKeyword | SyntaxKind.UniqueKeyword | SyntaxKind.ReadonlyKeyword */, type: TypeNode): TypeOperatorNode
}

external object createTaggedTemplate {
  @nativeInvoke
  operator fun invoke(tag: Expression, template: Any /* TemplateExpression | NoSubstitutionTemplateLiteral */): TaggedTemplateExpression
  @nativeInvoke
  operator fun invoke(tag: Expression, typeArguments: Array<TypeNode>?, template: Any /* TemplateExpression | NoSubstitutionTemplateLiteral */): TaggedTemplateExpression
}

external object updateTaggedTemplate {
  @nativeInvoke
  operator fun invoke(node: TaggedTemplateExpression, tag: Expression, template: Any /* TemplateExpression | NoSubstitutionTemplateLiteral */): TaggedTemplateExpression
  @nativeInvoke
  operator fun invoke(node: TaggedTemplateExpression, tag: Expression, typeArguments: Array<TypeNode>?, template: Any /* TemplateExpression | NoSubstitutionTemplateLiteral */): TaggedTemplateExpression
}

external var updateBinary: (node: BinaryExpression, left: Expression, right: Expression, operator: dynamic /* SyntaxKind.QuestionQuestionToken | ExponentiationOperator | SyntaxKind.AsteriskToken | SyntaxKind.SlashToken | SyntaxKind.PercentToken | SyntaxKind.PlusToken | SyntaxKind.MinusToken | SyntaxKind.LessThanLessThanToken | SyntaxKind.GreaterThanGreaterThanToken | SyntaxKind.GreaterThanGreaterThanGreaterThanToken | SyntaxKind.LessThanToken | SyntaxKind.LessThanEqualsToken | SyntaxKind.GreaterThanToken | SyntaxKind.GreaterThanEqualsToken | SyntaxKind.InstanceOfKeyword | SyntaxKind.InKeyword | SyntaxKind.EqualsEqualsToken | SyntaxKind.EqualsEqualsEqualsToken | SyntaxKind.ExclamationEqualsEqualsToken | SyntaxKind.ExclamationEqualsToken | SyntaxKind.AmpersandToken | SyntaxKind.BarToken | SyntaxKind.CaretToken | SyntaxKind.AmpersandAmpersandToken | SyntaxKind.BarBarToken | SyntaxKind.EqualsToken | SyntaxKind.PlusEqualsToken | SyntaxKind.MinusEqualsToken | SyntaxKind.AsteriskAsteriskEqualsToken | SyntaxKind.AsteriskEqualsToken | SyntaxKind.SlashEqualsToken | SyntaxKind.PercentEqualsToken | SyntaxKind.AmpersandEqualsToken | SyntaxKind.BarEqualsToken | SyntaxKind.CaretEqualsToken | SyntaxKind.LessThanLessThanEqualsToken | SyntaxKind.GreaterThanGreaterThanGreaterThanEqualsToken | SyntaxKind.GreaterThanGreaterThanEqualsToken | SyntaxKind.BarBarEqualsToken | SyntaxKind.AmpersandAmpersandEqualsToken | SyntaxKind.QuestionQuestionEqualsToken | SyntaxKind.CommaToken | BinaryOperatorToken */) -> BinaryExpression

external object createConditional {
  @nativeInvoke
  operator fun invoke(condition: Expression, whenTrue: Expression, whenFalse: Expression): ConditionalExpression
  @nativeInvoke
  operator fun invoke(condition: Expression, questionToken: QuestionToken, whenTrue: Expression, colonToken: ColonToken, whenFalse: Expression): ConditionalExpression
}

external object createYield {
  @nativeInvoke
  operator fun invoke(expression: Expression? = definedExternally): YieldExpression
  @nativeInvoke
  operator fun invoke(asteriskToken: AsteriskToken?, expression: Expression): YieldExpression
}

external var createClassExpression: (modifiers: Array<dynamic /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: dynamic /* String? | Identifier? */, typeParameters: Array<TypeParameterDeclaration>?, heritageClauses: Array<HeritageClause>?, members: Array<ClassElement>) -> ClassExpression

external var updateClassExpression: (node: ClassExpression, modifiers: Array<dynamic /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: Identifier?, typeParameters: Array<TypeParameterDeclaration>?, heritageClauses: Array<HeritageClause>?, members: Array<ClassElement>) -> ClassExpression

external var createPropertySignature: (modifiers: Array<dynamic /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: dynamic /* Identifier | StringLiteral | NumericLiteral | ComputedPropertyName | PrivateIdentifier | String */, questionToken: QuestionToken?, type: TypeNode?, initializer: Expression?) -> PropertySignature

external var updatePropertySignature: (node: PropertySignature, modifiers: Array<dynamic /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, name: dynamic /* Identifier | StringLiteral | NumericLiteral | ComputedPropertyName | PrivateIdentifier */, questionToken: QuestionToken?, type: TypeNode?, initializer: Expression?) -> PropertySignature

external var createExpressionWithTypeArguments: (typeArguments: Array<TypeNode>?, expression: Expression) -> ExpressionWithTypeArguments

external var updateExpressionWithTypeArguments: (node: ExpressionWithTypeArguments, typeArguments: Array<TypeNode>?, expression: Expression) -> ExpressionWithTypeArguments

external object createArrowFunction {
  @nativeInvoke
  operator fun invoke(modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, typeParameters: Array<TypeParameterDeclaration>?, parameters: Array<ParameterDeclaration>, type: TypeNode?, equalsGreaterThanToken: EqualsGreaterThanToken?, body: Any /* FunctionBody | Expression */): ArrowFunction
  @nativeInvoke
  operator fun invoke(modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, typeParameters: Array<TypeParameterDeclaration>?, parameters: Array<ParameterDeclaration>, type: TypeNode?, body: Any /* FunctionBody | Expression */): ArrowFunction
}

external object updateArrowFunction {
  @nativeInvoke
  operator fun invoke(node: ArrowFunction, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, typeParameters: Array<TypeParameterDeclaration>?, parameters: Array<ParameterDeclaration>, type: TypeNode?, equalsGreaterThanToken: EqualsGreaterThanToken, body: Any /* FunctionBody | Expression */): ArrowFunction
  @nativeInvoke
  operator fun invoke(node: ArrowFunction, modifiers: Array<Any /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, typeParameters: Array<TypeParameterDeclaration>?, parameters: Array<ParameterDeclaration>, type: TypeNode?, body: Any /* FunctionBody | Expression */): ArrowFunction
}

external object createVariableDeclaration {
  @nativeInvoke
  operator fun invoke(name: Any /* String | Identifier | ObjectBindingPattern | ArrayBindingPattern */, type: TypeNode? = definedExternally, initializer: Expression? = definedExternally): VariableDeclaration
  @nativeInvoke
  operator fun invoke(name: Any /* String | Identifier | ObjectBindingPattern | ArrayBindingPattern */, exclamationToken: ExclamationToken?, type: TypeNode?, initializer: Expression?): VariableDeclaration
}

external object updateVariableDeclaration {
  @nativeInvoke
  operator fun invoke(node: VariableDeclaration, name: Any /* Identifier | ObjectBindingPattern | ArrayBindingPattern */, type: TypeNode?, initializer: Expression?): VariableDeclaration
  @nativeInvoke
  operator fun invoke(node: VariableDeclaration, name: Any /* Identifier | ObjectBindingPattern | ArrayBindingPattern */, exclamationToken: ExclamationToken?, type: TypeNode?, initializer: Expression?): VariableDeclaration
}

external var createImportClause: (name: Identifier?, namedBindings: dynamic /* NamespaceImport? | NamedImports? */, isTypeOnly: Any) -> ImportClause

external var updateImportClause: (node: ImportClause, name: Identifier?, namedBindings: dynamic /* NamespaceImport? | NamedImports? */, isTypeOnly: Boolean) -> ImportClause

external var createExportDeclaration: (decorators: Array<Decorator>?, modifiers: Array<dynamic /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, exportClause: dynamic /* NamespaceExport? | NamedExports? */, moduleSpecifier: Expression?, isTypeOnly: Any) -> ExportDeclaration

external var updateExportDeclaration: (node: ExportDeclaration, decorators: Array<Decorator>?, modifiers: Array<dynamic /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */>?, exportClause: dynamic /* NamespaceExport? | NamedExports? */, moduleSpecifier: Expression?, isTypeOnly: Boolean) -> ExportDeclaration

external var createJSDocParamTag: (name: dynamic /* Identifier | QualifiedName */, isBracketed: Boolean, typeExpression: JSDocTypeExpression?, comment: String?) -> JSDocParameterTag

external var createComma: (left: Expression, right: Expression) -> Expression

external var createLessThan: (left: Expression, right: Expression) -> Expression

external var createAssignment: (left: Expression, right: Expression) -> BinaryExpression

external var createStrictEquality: (left: Expression, right: Expression) -> BinaryExpression

external var createStrictInequality: (left: Expression, right: Expression) -> BinaryExpression

external var createAdd: (left: Expression, right: Expression) -> BinaryExpression

external var createSubtract: (left: Expression, right: Expression) -> BinaryExpression

external var createLogicalAnd: (left: Expression, right: Expression) -> BinaryExpression

external var createLogicalOr: (left: Expression, right: Expression) -> BinaryExpression

external var createPostfixIncrement: (operand: Expression) -> PostfixUnaryExpression

external var createLogicalNot: (operand: Expression) -> PrefixUnaryExpression

external var createNode: (kind: SyntaxKind, pos: Any, end: Any) -> Node

external fun <T: Node> getMutableClone(node: T): T

external var isTypeAssertion: (node: Node) -> Boolean

external var isIdentifierOrPrivateIdentifier: (node: Node) -> Boolean
