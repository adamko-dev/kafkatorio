import dev.adamko.kafkatorio.gradle.asConsumer
import dev.adamko.kafkatorio.gradle.asProvider
import dev.adamko.kafkatorio.gradle.dropDirectories
import dev.adamko.kafkatorio.gradle.factorioModAttributes
import dev.adamko.kafkatorio.gradle.typescriptAttributes
import dev.adamko.kafkatorio.task.TypescriptToLuaTask
import net.swiftzer.semver.SemVer
import org.apache.tools.ant.filters.ReplaceTokens

plugins {
  dev.adamko.kafkatorio.lang.node
  distribution
}

description = "Sends in-game information to a server over the internet (requires additional setup)"


// Factorio required format is:
// - filename: `mod-name_version.zip`
// - zip contains one directory, `mod-name`


//val modName: String by extra { "${rootProject.name}-events" }
extra.set("modName", "kafkatorio-events")
val modName = extra.get("modName") as String

val modDescription: String by project.extra { project.description ?: "" }

val distributionZipName: String by extra { "${modName}_${rootProject.version}.zip" }

// version of Factorio that the mod is compatible with (must only be "major.minor" - patch causes error)
val modFactorioCompatibility: Provider<String> =
  libs.versions.factorio.map { SemVer.parse(it).run { "$major.$minor" } }

val licenseFile: RegularFile by rootProject.extra

@Suppress("UNCHECKED_CAST")
val projectTokens: MapProperty<String, String> =
  rootProject.extra.get("projectTokens") as? MapProperty<String, String>  ?: error("error getting projectTokens")
//val projectTokens: MapProperty<String, String> by rootProject.extra

val projectTokensX = projectTokens.apply {
  put("mod.name", modName)
  put("mod.title", "Kafkatorio Events")
  put("mod.description", modDescription)
  put("factorio.version", modFactorioCompatibility.get())
}

val tsSrcDir: Directory = layout.projectDirectory.dir("src/main/typescript")


node {
  nodeProjectDir.set(tsSrcDir)
}


val typescriptEventsSchema: Configuration by configurations.creating {
  description = "Fetch the TypeScript schema from the event-schema subproject"
  asConsumer()
  typescriptAttributes(objects)
}


dependencies {
  typescriptEventsSchema(projects.modules.eventsSchema)
}


val typescriptToLua by tasks.registering(TypescriptToLuaTask::class) {
  dependsOn(tasks.npmInstall, installEventsTsSchema, tasks.updatePackageJson)

  sourceFiles.set(tsSrcDir)
  outputDirectory.set(layout.buildDirectory.dir("typescriptToLua"))
}


val installEventsTsSchema by tasks.registering(Sync::class) {
  description = "Fetch the latest shared data-model"
  group = project.name

  dependsOn(typescriptEventsSchema)
  dependsOn(fixLink)

  val outputDir = layout.projectDirectory.dir("src/main/typescript/generated/kafkatorio-schema")
  outputs.dir(outputDir)

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
    // drop the first directory inside the zip
    eachFile { relativePath = dropDirectories() }
    includeEmptyDirs = false
  }
  into(outputDir)
}


val zipNameProvider = provider { distributionZipName }

tasks.distZip {
  val projectTokensXX = projectTokensX

  inputs.property("zipNameProvider", zipNameProvider)
  inputs.property("projectTokens", projectTokensXX)

  archiveFileName.set(zipNameProvider)
}


distributions {
  main {

    distributionBaseName.set(modName)

    contents {
      from(layout.projectDirectory.dir("src/main/resources/mod-data")) {
        include("**/**")
      }
      from(licenseFile)
      from(typescriptToLua.map { it.outputDirectory })
      filesNotMatching("**/*.png") {
        filter<ReplaceTokens>("tokens" to projectTokens.get())
      }
      includeEmptyDirs = false
      exclude {
        // exclude empty files
        it.file.run {
          isFile && useLines { lines -> lines.all { line -> line.isBlank() } }
        }
      }
    }
  }
}


tasks.withType<Zip>().configureEach {
  val projectTokensXX = projectTokensX
  inputs.property("projectTokens", projectTokensXX)
//  if (name.startsWith("dist")) {
//    notCompatibleWithConfigurationCache("NPE on projectTokens")
//  }
}

tasks.npmInstall {
  nodeModulesOutputFilter {
    exclude("**/typed-factorio/generated/classes.d.ts")
  }
}

interface ServiceProvider {
  @get:Inject
  val fs: FileSystemOperations
}

val fixLink by tasks.registering {
  dependsOn(tasks.npmInstall)


  val badLink = """{@link https://lua-api.factorio.com/latest/Data-Lifecycle.html Data Lifecycle}"""
  val goodLink = """https://lua-api.factorio.com/latest/Data-Lifecycle.html"""

  val typedFactorioDir =
    layout.projectDirectory.dir("src/main/typescript/node_modules/typed-factorio")
  val classesDTS = typedFactorioDir.file("generated/classes.d.ts")

  val services = project.objects.newInstance<ServiceProvider>()

  inputs.file(classesDTS)
  outputs.file(classesDTS)

  inputs.property("badLink", badLink)
  inputs.property("goodLink", goodLink)

  doLast {
    logger.lifecycle("fixing link ${classesDTS.asFile.canonicalPath}")

    services.fs.sync {
      from(classesDTS)
      into(temporaryDir)
      filter { line ->
        when {
          line.contains(badLink) -> line.replace(badLink, goodLink)
          else                   -> line
        }
      }
    }

    services.fs.copy {
      from(temporaryDir.resolve("classes.d.ts").canonicalPath)
      into(classesDTS.asFile.parent)
      duplicatesStrategy = DuplicatesStrategy.WARN
    }
  }
}

tasks.assemble { dependsOn(fixLink) }


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

val factorioModProvider by configurations.registering {
  asProvider()
  factorioModAttributes(objects)
  outgoing.artifact(tasks.distZip.flatMap { it.archiveFile })
}


val packageJsonName = providers.provider { "${rootProject.name}-${project.name}" }

tasks.updatePackageJson {
  inputs.property("packageJsonName", packageJsonName)
  propertiesToCheck.put("name", packageJsonName)
  packageJsonFile.set(layout.projectDirectory.file("src/main/typescript/package.json"))
}


tasks.assemble { dependsOn(installEventsTsSchema, tasks.updatePackageJson) }


// trying to get Gradle+idea to recognise the ts-src...

//val typescriptSrcSet = project.objects.sourceDirectorySet("typescript", "TypeScript").apply {
//  srcDirs(tsSrcDir)
//  compiledBy(typescriptToLua, TypescriptToLuaTask::outputDirectory)
////  destinationDirectory.set(layout.buildDirectory.dir("typescriptToLua"))
//}
//
//val tsSrcConfiguration by configurations.registering {
//  asProvider()
//  outgoing.artifact(tsSrcDir)
//}
//
//idea {
//  module {
//    // Not using += due to https://github.com/gradle/gradle/issues/8749
//    sourceDirs = sourceDirs + typescriptSrcSet.sourceDirectories
//    resourceDirs.add(file("src/main/resources"))
//    excludeDirs.add(layout.projectDirectory.dir("src/main/typescript/node_modules").asFile)
//    scopes.compute("MAIN") { _: String, scope: MutableMap<String, MutableCollection<Configuration>>? ->
//      val s = (scope ?: mutableMapOf())
//      s.compute("plus") { _, conf ->
//        val c = conf ?: mutableListOf()
//        c.add(tsSrcConfiguration.get())
//        c
//      }
//      s
//    }
//  }
//}
