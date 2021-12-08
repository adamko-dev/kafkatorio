import com.github.gradle.node.npm.task.NpmTask
import dev.adamko.kafkatorio.gradle.asConsumer
import dev.adamko.kafkatorio.gradle.asProvider
import dev.adamko.kafkatorio.gradle.factorioModAttributes
import dev.adamko.kafkatorio.gradle.typescriptAttributes
import groovy.json.JsonOutput
import org.apache.tools.ant.filters.ReplaceTokens
import org.jetbrains.kotlin.gradle.targets.js.npm.SemVer
import org.jetbrains.kotlin.util.parseSpaceSeparatedArgs

plugins {
  idea
  id("dev.adamko.kafkatorio.lang.node")
  distribution
}

description =
  "Sends in-game information to a server over the internet (requires Kafkatorio Kafka-Pipe)"


// Factorio required format is:
// - filename: `mod-name_version.zip`
// - zip contains one directory, `mod-name`
val modName: String by extra("${rootProject.name}-events")
val distributionZipName: String by extra("${modName}_${project.version}.zip")

// version of Factorio that the mod is compatible with (must only be "major.minor" - patch causes error)
val modFactorioCompatibility = SemVer.from(libs.versions.factorio.get()).run { "$major.$minor" }

val licenseFile: RegularFile by rootProject.extra
val projectTokens: MutableMap<String, String> by rootProject.extra
projectTokens += mapOf(
  "mod.name" to modName,
  "mod.title" to "Kafkatorio Events",
  "mod.description" to (project.description ?: ""),
  "factorio.version" to modFactorioCompatibility,
)

val tsSrcDir: Directory = layout.projectDirectory.dir("src/main/typescript")

node {
  nodeProjectDir.set(tsSrcDir)
}

val typescriptEventsSchema: Configuration by configurations.creating {
  asConsumer()
  typescriptAttributes(objects)
}

dependencies {
  typescriptEventsSchema(projects.modules.eventsSchema)
}

val typescriptToLua by tasks.registering(NpmTask::class) {
  description = "Convert Typescript To Lua"
  group = project.name

  dependsOn(tasks.npmInstall, installEventsTsSchema)

  execOverrides { standardOutput = System.out }

  npmCommand.set(listOf("run", "build"))

  inputs.dir(tsSrcDir)
    .skipWhenEmpty()
    .withPropertyName("sourceFiles")
    .withPathSensitivity(PathSensitivity.RELATIVE)

  args.set(parseSpaceSeparatedArgs("-- --outDir $temporaryDir"))
  val outputDir = layout.buildDirectory.dir("typescriptToLua")
  outputs.dir(outputDir)
    .withPropertyName("outputDir")

  ignoreExitValue.set(false)

  doFirst("clean") {
    delete(temporaryDir)
    mkdir(temporaryDir)
  }

  doLast("syncTstlOutput") {
    sync {
      from(temporaryDir)
      into(outputDir)
    }
  }
}

val installEventsTsSchema by tasks.registering(Sync::class) {
  description = "Fetch the latest shared data-model"
  group = project.name

  dependsOn(typescriptEventsSchema)

  val outputDir = layout.projectDirectory.dir("src/main/typescript/schema")
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
    eachFile {
      relativePath = RelativePath(true, *relativePath.segments.drop(1).toTypedArray())
    }
    includeEmptyDirs = false
  }
  into(outputDir)
}

tasks.distZip {
  archiveFileName.set(distributionZipName)
}

distributions {
  main {

    distributionBaseName.set(modName)

    contents {
      from(layout.projectDirectory.dir("src/main/resources/mod-data")) {
        include("**/**")
      }
      from(licenseFile)
      from(typescriptToLua.map { it.outputs })
      filter<ReplaceTokens>("tokens" to projectTokens)
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

//val packageMod by tasks.registering(Zip::class) {
//  description = "Package mod files into ZIP"
//  group = project.name
//
//  dependsOn(typescriptToLua)
//
//  inputs.properties(tokens)
//
//  from(layout.projectDirectory.dir("src/main/resources/mod-data")) {
//    include("**/**")
//    filter<ReplaceTokens>("tokens" to tokens)
//  }
//  from(licenseFile)
//
//  into(rootProject.name)
//
//  // Factorio required format is:
//  // - filename: `mod-name_version.zip`
//  // - zip contains one directory, `mod-name`
//  archiveFileName.set("${rootProject.name}_${project.version}.zip")
//  destinationDirectory.set(layout.buildDirectory.dir("dist"))
//
//  doLast {
//    val outDir = destinationDirectory.asFile.get().toRelativeString(layout.projectDirectory.asFile)
//    val outZip = "${archiveFileName.orNull}"
//    logger.lifecycle("Packaged mod into $outDir/$outZip")
//  }
//}


val downloadFactorioApiDocs by tasks.registering {
  group = project.name

  val target = uri("https://lua-api.factorio.com/latest/runtime-api.json")
  val apiFilename = File(target.path).name
  val downloadedFile = file("$temporaryDir/$apiFilename")

  val apiFile = layout.buildDirectory.file(apiFilename)
  outputs.file(apiFile)

  doLast {

    ant.invokeMethod(
      "get", mapOf(
        "src" to target,
        "dest" to downloadedFile,
        "verbose" to true,
      )
    )

    val json = downloadedFile.readText()
    val prettyJson = JsonOutput.prettyPrint(json)

    apiFile.get().asFile.writeText(prettyJson)

    logger.lifecycle("Downloaded Factorio API json: $apiFile")
  }
}

val factorioModProvider by configurations.registering {
  asProvider()
  factorioModAttributes(objects)
  outgoing.artifact(tasks.distZip.flatMap { it.archiveFile })
}

tasks.updatePackageJson {
  propertiesToCheck.put("name", "${rootProject.name}-${project.name}")
  packageJsonFile.set(layout.projectDirectory.file("src/main/typescript/package.json"))
}

tasks.assemble { dependsOn(installEventsTsSchema, tasks.updatePackageJson) }

//
//idea {
//  // this doesn't work - no source sets?
//  module {
//    sourceDirs.add(mkdir(layout.projectDirectory.dir("src/main/typescript").asFile))
//    excludeDirs.add(mkdir(layout.projectDirectory.dir("src/main/typescript/node_modules").asFile))
//    resourceDirs.add(mkdir(layout.projectDirectory.dir("src/main/resources").asFile))
//    resourceDirs.add(mkdir(layout.projectDirectory.dir("infra").asFile))
//    excludeDirs.add(mkdir(layout.projectDirectory.dir("infra/factorio-server").asFile))
//  }
//}
//
//val typescriptSrcSet =
//  project.objects.sourceDirectorySet("typescript", "typescript").apply {
//    srcDir(layout.projectDirectory.dir("src/main/typescript"))
//
//    destinationDirectory.set(modBuildDir.dir("typescriptToLua").asFile)
//    compiledBy(tstlTask) {
//
//      project.objects.directoryProperty().apply {
//        set(modBuildDir.dir("typescriptToLua"))
//      }
//    }
//  }
