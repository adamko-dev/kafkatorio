package dev.adamko.kafkatorio

//import javax.inject.Inject
//import kotlinx.coroutines.runBlocking
//import kotlinx.serialization.json.Json
//import net.swiftzer.semver.SemVer
//import okio.FileSystem
//import okio.Path.Companion.toOkioPath
//import okio.Path.Companion.toPath
//import okio.openZip
//import org.gradle.api.DefaultTask
//import org.gradle.api.file.RegularFileProperty
//import org.gradle.api.model.ObjectFactory
//import org.gradle.api.provider.Property
//import org.gradle.api.provider.ProviderFactory
//import org.gradle.api.tasks.Input
//import org.gradle.api.tasks.InputFile
//import org.gradle.api.tasks.TaskAction
//import org.gradle.kotlin.dsl.property
//
//
//abstract class FactorioModPortalPublishTask @Inject constructor(
//  objects: ObjectFactory,
//  providers: ProviderFactory,
//) : DefaultTask() {
//
//  @get:InputFile
//  abstract val distributionZip: RegularFileProperty
//
//  @get:Input
//  abstract val modVersion: Property<String>
//
//  @get:Input
//  abstract val modName: Property<String>
//
//  @get:Input
//  abstract val factorioVersion: Property<String>
//
//  /** Bearer API key */
//  @get:Input
//  val portalApiKey: Property<String> = objects.property<String>().convention(
//    providers.gradleProperty("FACTORIO_MOD_PORTAL_API_KEY")
//  )
//
//  @get:Input
//  val portalUploadEndpoint: Property<String> = objects.property<String>()
//    .convention("https://mods.factorio.com/api/v2/mods/releases/init_upload")
//
//
//  @TaskAction
//  fun deploy() = runBlocking {
//    val modVersion = SemVer.parse(modVersion.get())
//
//    val portalUploadEndpoint = portalUploadEndpoint.get()
//    val modName = modName.get()
//    val portalApiKey = portalApiKey.get()
//    val distributionZip = distributionZip.get().asFile
//
//  }
//
//  private fun validateInfoJson() {
//    val modVersion = modVersion.get()
//    val factorioVersion = factorioVersion.get()
//
//    val distributionZip = distributionZip.get().asFile.toOkioPath()
//
//    val zipContents = FileSystem.SYSTEM.openZip(distributionZip)
//
//    val infoJson = zipContents.read("info.json".toPath()) {
//      generateSequence { readUtf8Line() }
//    }.joinToString("\n")
//
//    val info: FactorioModInfo = Json.decodeFromString(FactorioModInfo.serializer(), infoJson)
//
//    require(info.version == modVersion) {
//      "mod version mismatch, ${info.version} vs $modVersion "
//    }
//
//    require(info.factorioVersion == factorioVersion) {
//      "Factorio version mismatch, ${info.factorioVersion} vs $factorioVersion "
//    }
//  }
//}
