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
    val kind: String /* "action::set" | "action::invalidate" | "event::typesRegistry" | "action::packageInstalled" | "event::beginInstallTypes" | "event::endInstallTypes" | "event::initializationFailed" */
}

external interface TypingInstallerRequestWithProjectName {
    val projectName: String
}

external interface DiscoverTypings : TypingInstallerRequestWithProjectName {
    val fileNames: Array<String>
    val projectRootPath: String /* String & `T$3` */
    val compilerOptions: CompilerOptions
    val watchOptions: WatchOptions?
        get() = definedExternally
    val typeAcquisition: TypeAcquisition
    val unresolvedImports: SortedReadonlyArray<String>
    val cachePath: String?
        get() = definedExternally
    val kind: String /* "discover" */
}

external interface CloseProject : TypingInstallerRequestWithProjectName {
    val kind: String /* "closeProject" */
}

external interface TypesRegistryRequest {
    val kind: String /* "typesRegistry" */
}

external interface InstallPackageRequest : TypingInstallerRequestWithProjectName {
    val kind: String /* "installPackage" */
    val fileName: String /* String & `T$3` */
    val packageName: String
    val projectRootPath: String /* String & `T$3` */
}

external interface PackageInstalledResponse : ProjectResponse {
    override val kind: String /* "action::packageInstalled" */
    val success: Boolean
    val message: String
}

external interface InitializationFailedResponse : TypingInstallerResponse {
    override val kind: String /* "event::initializationFailed" */
    val message: String
    val stack: String?
        get() = definedExternally
}

external interface ProjectResponse : TypingInstallerResponse {
    val projectName: String
}

external interface InvalidateCachedTypings : ProjectResponse {
    override val kind: String /* "action::invalidate" */
}

external interface InstallTypes : ProjectResponse {
    override val kind: String /* "event::beginInstallTypes" | "event::endInstallTypes" */
    val eventId: Number
    val typingsInstallerVersion: String
    val packagesToInstall: Array<String>
}

external interface BeginInstallTypes : InstallTypes {
    override val kind: String /* "event::beginInstallTypes" */
}

external interface EndInstallTypes : InstallTypes {
    override val kind: String /* "event::endInstallTypes" */
    val installSuccess: Boolean
}

external interface SetTypings : ProjectResponse {
    val typeAcquisition: TypeAcquisition
    val compilerOptions: CompilerOptions
    val typings: Array<String>
    val unresolvedImports: SortedReadonlyArray<String>
    override val kind: String /* "action::set" */
}