package dev.adamko.kafkatorio.factoriomod.portal

import dev.adamko.kafkatorio.factoriomod.FactorioMod
import io.ktor.http.URLBuilder
import io.ktor.http.takeFrom
import javax.inject.Inject
import kotlinx.coroutines.runBlocking
import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.internal.tasks.userinput.UserInputHandler
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.provider.ProviderFactory
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.property
import org.gradle.kotlin.dsl.support.get


abstract class FactorioModPublishTask @Inject constructor(
  objects: ObjectFactory,
  providers: ProviderFactory,
) : DefaultTask() {

  @get:InputFile
  abstract val distributionZip: RegularFileProperty

  @get:Input
  abstract val modName: Property<String>

  @get:Input
  abstract val modVersion: Property<String>

//  @get:Input
//  abstract val factorioVersion: Property<String>

  /** Bearer API key */
  @get:Input
  val portalApiKey: Property<String> = objects.property<String>().convention(
    providers.gradleProperty("FACTORIO_MOD_PORTAL_API_KEY")
  )

  @get:Input
  val portalApiUrl: Property<String> = objects.property<String>()
    .convention("https://mods.factorio.com/api/v2/")

  /**
   * Base URL for linking to a mod page in the portal
   *
   * Example `https://mods.factorio.com/mod/my-mod-name`
   */
  @get:Input
  val modPortalBaseURl: Property<String> = objects.property<String>()
    .convention("https://mods.factorio.com/mod/")

  @get:Input
  val portalUploadEndpoint: Property<String> = objects.property<String>()
    .convention("mods/releases/init_upload")


  init {
    group = FactorioMod.TASK_GROUP
  }


  @TaskAction
  fun deploy() = runBlocking {


    val userInputHandler = services.get<UserInputHandler>()

    val portalUploadEndpoint = URLBuilder(portalApiUrl.get())
      .takeFrom(portalUploadEndpoint.get())
      .buildString()

    val client = FactorioModPortalPublishClient(
      userInputHandler = userInputHandler,
      distributionZip = distributionZip.get().asFile,
      modName = modName.get(),
      modVersion = modVersion.get(),
      portalApiKey = portalApiKey.get(),
      portalUploadEndpoint = portalUploadEndpoint,
      modPortalBaseURl = modPortalBaseURl.get(),
    )

    client.uploadMod()
  }

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
}
