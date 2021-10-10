@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")
package ts.server

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
import ts.CompilerOptions
import ts.WatchOptions
import ts.TypeAcquisition
import ts.SortedReadonlyArray

external interface TypingInstallerResponse {
    var kind: String /* "action::set" | "action::invalidate" | "event::typesRegistry" | "action::packageInstalled" | "event::beginInstallTypes" | "event::endInstallTypes" | "event::initializationFailed" */
}

external interface TypingInstallerRequestWithProjectName {
    var projectName: String
}

external interface DiscoverTypings : TypingInstallerRequestWithProjectName {
    var fileNames: Array<String>
    var projectRootPath: String /* String & `T$3` */
    var compilerOptions: CompilerOptions
    var watchOptions: WatchOptions?
        get() = definedExternally
        set(value) = definedExternally
    var typeAcquisition: TypeAcquisition
    var unresolvedImports: SortedReadonlyArray<String>
    var cachePath: String?
        get() = definedExternally
        set(value) = definedExternally
    var kind: String /* "discover" */
}

external interface CloseProject : TypingInstallerRequestWithProjectName {
    var kind: String /* "closeProject" */
}

external interface TypesRegistryRequest {
    var kind: String /* "typesRegistry" */
}

external interface InstallPackageRequest : TypingInstallerRequestWithProjectName {
    var kind: String /* "installPackage" */
    var fileName: String /* String & `T$3` */
    var packageName: String
    var projectRootPath: String /* String & `T$3` */
}

external interface PackageInstalledResponse : ProjectResponse {
    override var kind: String /* "action::packageInstalled" */
    var success: Boolean
    var message: String
}

external interface InitializationFailedResponse : TypingInstallerResponse {
    override var kind: String /* "event::initializationFailed" */
    var message: String
    var stack: String?
        get() = definedExternally
        set(value) = definedExternally
}

external interface ProjectResponse : TypingInstallerResponse {
    var projectName: String
}

external interface InvalidateCachedTypings : ProjectResponse {
    override var kind: String /* "action::invalidate" */
}

external interface InstallTypes : ProjectResponse {
    override var kind: String /* "event::beginInstallTypes" | "event::endInstallTypes" */
    var eventId: Number
    var typingsInstallerVersion: String
    var packagesToInstall: Array<String>
}

external interface BeginInstallTypes : InstallTypes {
    override var kind: String /* "event::beginInstallTypes" */
}

external interface EndInstallTypes : InstallTypes {
    override var kind: String /* "event::endInstallTypes" */
    var installSuccess: Boolean
}

external interface SetTypings : ProjectResponse {
    var typeAcquisition: TypeAcquisition
    var compilerOptions: CompilerOptions
    var typings: Array<String>
    var unresolvedImports: SortedReadonlyArray<String>
    override var kind: String /* "action::set" */
}