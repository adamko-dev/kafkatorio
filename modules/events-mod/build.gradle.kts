import dev.adamko.gradle.factorio.mod_portal.FactorioModPublishTask
import dev.adamko.gradle.factorio.typescriptAttributes
import kafkatorio.tasks.TypeScriptToLuaTask
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonObject
import org.gradle.api.tasks.PathSensitivity.RELATIVE

plugins {
  id("kafkatorio.conventions.lang.node")
  id("dev.adamko.factorio-mod")
  distribution
}

description = """
    |The Kafkatorio Factorio mod.
    |
    |The Kafkatorio mod must be installed in a Factorio multiplayer server.
    |It collects in-game information and emits it to a server over the internet.
    |
    |(Kafkatorio is a complicated mod that requires additional setup.)
    """.trimMargin()


factorioMod {
  modName.set("kafkatorio-events")
  modTitle.set("Kafkatorio Events")
  modAuthor.set("adam@adamko.dev")
  factorioCompatibility.set(libs.versions.factorio.map { it.substringBeforeLast(".") })
}

val licenseFile: RegularFile by rootProject.extra

node {
  nodeProjectDir.set(layout.projectDirectory)
}


val typescriptEventsSchema: Configuration by configurations.creating {
  description = "Fetch the TypeScript schema from the event-schema subproject"
  asConsumer()
  typescriptAttributes(objects)
}


dependencies {
  typescriptEventsSchema(projects.modules.eventsLibrary)
}


val typescriptToLua by tasks.registering(TypeScriptToLuaTask::class) {
  dependsOn(
    tasks.npmInstall,
    installEventsTsSchema,
    tasks.updatePackageJson,
  )

  inputs.file(tasks.updatePackageJson.map { it.packageJsonFile })
    .withPropertyName("packageJsonFile")
    .withPathSensitivity(RELATIVE)

  sourceFiles.from(factorioMod.mainSources.typescript.sourceDirectories)

  outputDirectory.set(factorioMod.mainSources.typescript.destinationDirectory)
}


val installEventsTsSchema by tasks.registering(Sync::class) {
  description = "Fetch the latest shared TypeScript data-model"
  group = project.name

  dependsOn(typescriptEventsSchema)

  from(
    provider { typescriptEventsSchema }
      .map { eventsSchema ->
        eventsSchema.incoming
          .artifactView { lenient(true) }
          .artifacts
          .artifactFiles
          .filter { file -> file.exists() }
          .map { zipTree(it) }
      }
  ) {
    includeEmptyDirs = false
  }
  into(layout.projectDirectory.dir("src/main/typescript/generated"))
}


tasks.distZip {
  val distributionZipName = factorioMod.distributionZipName
  archiveFileName.set(distributionZipName)
}

val publishModToPortal by tasks.registering(FactorioModPublishTask::class) {
  dependsOn(tasks.check)

  distributionZip.set(tasks.distZip.flatMap { it.archiveFile })

  modName.set(factorioMod.modName)
  modVersion.set(factorioMod.modVersion)
}

tasks.assembleFactorioModContents {
  modFiles.apply {
    from(typescriptToLua.map { it.outputDirectory })
    from(licenseFile)
  }
}

val projectPackageJsonName: Provider<String> =
  providers.provider { "${rootProject.name}-${project.name}" }
val projectPackageJsonFile: RegularFile = layout.projectDirectory.file("package.json")
val projectVersion: Provider<String> = providers.provider { "${project.version}" }


tasks.updatePackageJson {
//  mustRunAfter(tasks.npmInstall)

  val projectPackageJsonName = projectPackageJsonName
  val nodeJsVersion = libs.versions.node
  val luaTypesVersion = libs.versions.npm.luaTypes
  val typescriptToLuaVersion = libs.versions.npm.typescriptToLua
  val typedFactorioVersion = libs.versions.npm.typedFactorio
  val typescriptVersion = libs.versions.npm.typescript

  inputs.properties(
    "projectPackageJsonName" to projectPackageJsonName,
    "nodeJsVersion" to nodeJsVersion,
    "luaTypesVersion" to luaTypesVersion,
    "typescriptToLuaVersion" to typescriptToLuaVersion,
    "typedFactorioVersion" to typedFactorioVersion,
    "typescriptVersion" to typescriptVersion,
  )

  updateExpectedJson {
    put("_DO_NOT_MODIFY", "This file is managed by Gradle task $path")
    put("name", projectPackageJsonName.get())
    putJsonObject("engines") {
      put("node", "~${nodeJsVersion.get()}")
    }
    putJsonObject("dependencies") {
      put("lua-types", luaTypesVersion.get())
      put("typescript-to-lua", typescriptToLuaVersion.get())
      put("typed-factorio", typedFactorioVersion.get())
      put("typescript", typescriptVersion.get())
    }
  }

  packageJsonFile.set(projectPackageJsonFile)
}


tasks.assemble {
  dependsOn(installEventsTsSchema)
  dependsOn(tasks.updatePackageJson)
}

//val downloadFactorioApiDocs by tasks.registering {
//  group = project.name
//
//  val target = uri("https://lua-api.factorio.com/latest/runtime-api.json")
//  val apiFilename = File(target.path).name
//  val downloadedFile = file("$temporaryDir/$apiFilename")
//
//  val apiFile = layout.buildDirectory.file(apiFilename)
//  outputs.file(apiFile)
//
//  doLast {
//
//    ant.invokeMethod(
//      "get", mapOf(
//        "src" to target,
//        "dest" to downloadedFile,
//        "verbose" to true,
//      )
//    )
//
//    val json = downloadedFile.readText()
//    val prettyJson = JsonOutput.prettyPrint(json)
//
//    apiFile.get().asFile.writeText(prettyJson)
//
//    logger.lifecycle("Downloaded Factorio API json: $apiFile")
//  }
//}
