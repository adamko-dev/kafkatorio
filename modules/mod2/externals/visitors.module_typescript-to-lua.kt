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
import ts.Statement
import ts.Node
import tsstdlib.Map
import ts.SyntaxKind

external interface NodesBySyntaxKind

typealias StatementLikeNode = Statement

typealias VisitorResult<T> = Any

typealias FunctionVisitor<T> = (node: T, context: TransformationContext) -> VisitorResult<T>

external interface ObjectVisitor<T : Node> {
    var transform: FunctionVisitor<T>
    var priority: Number?
        get() = definedExternally
        set(value) = definedExternally
}

typealias Visitors = Any

typealias VisitorMap = Map<SyntaxKind, Array<ObjectVisitor<Node>>>