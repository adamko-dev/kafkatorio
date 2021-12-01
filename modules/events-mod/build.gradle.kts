import com.github.gradle.node.npm.task.NpmTask
import dev.adamko.factoriowebmap.configurations.asConsumer
import dev.adamko.factoriowebmap.configurations.asProvider
import dev.adamko.factoriowebmap.configurations.factorioModAttributes
import dev.adamko.factoriowebmap.configurations.typescriptAttributes
import groovy.json.JsonOutput
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.apache.tools.ant.filters.ReplaceTokens
import org.gradle.kotlin.dsl.support.useToRun
import org.jetbrains.kotlin.util.parseSpaceSeparatedArgs
import org.jetbrains.kotlin.util.suffixIfNot

plugins {
  idea
  id("dev.adamko.factoriowebmap.archetype.node")
}

val tokens: Map<String, String> by project.extra
val licenseFile: RegularFile by project.extra

val tsSrcDir: Directory = layout.projectDirectory.dir("src/main/typescript")

node {
  nodeProjectDir.set(tsSrcDir)
}


val dataModelTs by configurations.registering {
  asConsumer()
  typescriptAttributes(objects)
  defaultDependencies {
    project.dependencies.create(projects.modules.eventsSchema)
  }
}


val typescriptToLua by tasks.registering(NpmTask::class) {
  description = "Convert Typescript To Lua"
  group = project.name

  dependsOn(tasks.npmInstall)

  execOverrides { standardOutput = System.out }

  npmCommand.set(listOf("run", "build"))

  inputs.dir(project.node.nodeProjectDir)
    .skipWhenEmpty()
    .withPropertyName("sourceFiles")
    .withPathSensitivity(PathSensitivity.RELATIVE)

  val intermediateOutputDir = temporaryDir
  args.set(parseSpaceSeparatedArgs("-- --outDir $intermediateOutputDir"))
  val outputDir = layout.buildDirectory.dir("typescriptToLua")
  outputs.dir(outputDir)
    .withPropertyName("outputDir")

  ignoreExitValue.set(false)

  doFirst("clean") {
    delete(intermediateOutputDir)
  }

  doLast("syncTstlOutput") {
    sync {
      from(intermediateOutputDir)
      into(outputDir)
    }
  }

}

val updatePackageJsonVersion by tasks.registering {
  group = project.name
  description = """
    Read the package.json file and set the version to be the project's version.
  """.trimIndent()

  val projectVersion = "${project.version}"
  inputs.properties(
    "projectVersion" to projectVersion,
  )

  val packageJsonFile = layout.projectDirectory.file("src/main/typescript/package.json")
  outputs.file(packageJsonFile)

  val jsonFormatter = Json {
    prettyPrint = true
    prettyPrintIndent = "  "
  }

  onlyIf {
    // check to see if the version is already up-to-date
    val packageJsonContent = packageJsonFile.asFile.readText()
    val packageJson = jsonFormatter.parseToJsonElement(packageJsonContent).jsonObject
    packageJson["version"]?.jsonPrimitive?.content != projectVersion
  }

  doLast {
    val packageJsonContent = packageJsonFile.asFile.readText()
    val packageJson = jsonFormatter.parseToJsonElement(packageJsonContent).jsonObject
    val packageJsonUpdate = JsonObject(
      packageJson + ("version" to JsonPrimitive(projectVersion))
    )
    val packageJsonContentUpdated =
      jsonFormatter
        .encodeToString(packageJsonUpdate)
        .suffixIfNot("\n")
    packageJsonFile.asFile.writer().useToRun {
      write(packageJsonContentUpdated)
    }
  }
}

val updateDataModel by tasks.registering(Sync::class) {
  group = project.name
  description = "Fetch the latest shared data-model"

  dependsOn(dataModelTs)

  from(
    dataModelTs.map { c ->
      c.incoming
        .artifactView { lenient(true) }
        .artifacts
        .artifactFiles
        .filter { file -> file.exists() }
    }
  )

  into(layout.projectDirectory.dir("src/main/typescript/model"))
}


val packageMod by tasks.registering(Zip::class) {
  description = "Package mod files into ZIP"
  group = project.name

  dependsOn(typescriptToLua)

  inputs.properties(tokens)

  from(layout.projectDirectory.dir("src/main/resources/mod-data")) {
    include("**/**")
    filter<ReplaceTokens>("tokens" to tokens)
  }
  dependsOn(typescriptToLua)
  from(licenseFile)

  into(rootProject.name)

  // Factorio required format is:
  // - filename: `mod-name_version.zip`
  // - zip contains one directory, `mod-name`
  archiveFileName.set("${rootProject.name}_${project.version}.zip")
  destinationDirectory.set(layout.buildDirectory.dir("dist"))

  doLast {
    val outDir = destinationDirectory.asFile.get().toRelativeString(layout.projectDirectory.asFile)
    val outZip = "${archiveFileName.orNull}"
    logger.lifecycle("Packaged mod into $outDir/$outZip")
  }
}


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
  outgoing.artifact(packageMod)
}


tasks.assemble { dependsOn(updateDataModel, updatePackageJsonVersion) }
tasks.build { dependsOn(packageMod) }


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
