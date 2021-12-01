import com.github.gradle.node.npm.task.NpmTask
import dev.adamko.factoriowebmap.configurations.asConsumer
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

val projectId: String by project.extra
val tokens: Map<String, String> by project.extra

val tsSrcDir: Directory = layout.projectDirectory.dir("src/main/typescript")
val modBuildDir: Directory = layout.buildDirectory.dir(projectId).get()

node {
  nodeProjectDir.set(tsSrcDir)
}

val tstlTask = tasks.register<NpmTask>("typescriptToLua") {
  description = "Convert Typescript To Lua"
  group = projectId

  dependsOn(tasks.npmInstall)

  execOverrides { standardOutput = System.out }

  npmCommand.set(listOf("run", "build"))

  inputs.dir(project.node.nodeProjectDir)
    .skipWhenEmpty()
    .withPropertyName("sourceFiles")
    .withPathSensitivity(PathSensitivity.RELATIVE)

  val intermediateOutputDir = temporaryDir
  args.set(parseSpaceSeparatedArgs("-- --outDir $intermediateOutputDir"))
  val outputDir: Directory = modBuildDir.dir("typescriptToLua")
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
    val packageJsonContent = packageJsonFile.asFile.readText()
    val packageJson = jsonFormatter.parseToJsonElement(packageJsonContent).jsonObject
    packageJson["version"]?.jsonPrimitive?.content != projectVersion
  }

  doFirst {
    delete(temporaryDir)
    mkdir(temporaryDir)
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

tasks.assemble { dependsOn(updatePackageJsonVersion) }


val dataModelTs: Configuration by configurations.creating {
  asConsumer()
  typescriptAttributes(objects)
}

dependencies {
  dataModelTs(projects.modules.factorioEventsDataModel)
}

val updateDataModel by tasks.registering(Sync::class) {
  group = project.name

  dependsOn(dataModelTs)

  from(
    dataModelTs.incoming
      .artifactView { lenient(true) }
      .artifacts
      .artifactFiles
      .filter { it.exists() }
  )

  into(layout.projectDirectory.dir("src/main/typescript/generated"))
}

tasks.assemble { dependsOn(updateDataModel) }



idea {
  module {
    sourceDirs.add(mkdir(layout.projectDirectory.dir("src/main/typescript").asFile))
    excludeDirs.add(mkdir(layout.projectDirectory.dir("src/main/typescript/node_modules").asFile))
    resourceDirs.add(mkdir(layout.projectDirectory.dir("src/main/resources").asFile))
    resourceDirs.add(mkdir(layout.projectDirectory.dir("infra").asFile))
    excludeDirs.add(mkdir(layout.projectDirectory.dir("infra/factorio-server").asFile))
  }
}

val typescriptSrcSet =
  project.objects.sourceDirectorySet("typescript", "typescript").apply {
    srcDir(layout.projectDirectory.dir("src/main/typescript"))

    destinationDirectory.set(modBuildDir.dir("typescriptToLua").asFile)
    compiledBy(tstlTask) {

      project.objects.directoryProperty().apply {
        set(modBuildDir.dir("typescriptToLua"))
      }
    }
  }

val modPackageTask = tasks.register<Zip>("packageMod") {
  description = "Package mod files into ZIP"
  group = projectId

  dependsOn(tstlTask)

  inputs.properties(tokens)

  from(layout.projectDirectory.dir("src/main/resources/mod-data")) {
    include("**/**")
    filter<ReplaceTokens>("tokens" to tokens)
  }
  from(
    rootProject.layout.projectDirectory.file("LICENSE"),
    tstlTask,
  )

  into(rootProject.name)

  // Factorio required format is:
  // - filename: `mod-name_version.zip`
  // - zip contains one directory, `mod-name`
  archiveFileName.set("${rootProject.name}_${project.version}.zip")
  destinationDirectory.set(modBuildDir.dir("dist"))

  doLast {
    val outDir = destinationDirectory.asFile.get().toRelativeString(layout.projectDirectory.asFile)
    val outZip = "${archiveFileName.orNull}"
    logger.lifecycle("Creating mod zip $outDir/$outZip")
  }
}

//tasks.build { dependsOn(tstlTask, modPackageTask) }

val modInfraDir: Directory = layout.projectDirectory.dir("infra")
val factorioServerDataDir: Directory = modInfraDir.dir("factorio-server")

val copyModToServerTask = tasks.register<Copy>("copyModToServer") {
  description = "Copy the mod to the Factorio Docker server"
  group = "$projectId.factorioServer"

  dependsOn(modPackageTask)

  from(modPackageTask)
  into(factorioServerDataDir.dir("mods"))

  doLast {
    logger.lifecycle("Copying mod from ${source.files} to $destinationDir")
  }
}

val factorioServerStop = tasks.register<Exec>("factorioServerStop") {
  group = "$projectId.factorioServer"

  mustRunAfter(copyModToServerTask)

  workingDir(modInfraDir)
  commandLine = parseSpaceSeparatedArgs("docker-compose stop factorio-server")
}

val factorioServerUp = tasks.register<Exec>("factorioServerUp") {
  group = "$projectId.factorioServer"

  mustRunAfter(copyModToServerTask)
  dependsOn(factorioServerStop)

  workingDir(modInfraDir)
  commandLine = parseSpaceSeparatedArgs("docker-compose up -d factorio-server")
}

val factorioServerRestart: Task by tasks.creating {
  group = "$projectId.factorioServer"

  dependsOn(factorioServerStop, factorioServerUp)
}

//val serverRestartTask = tasks.register<Exec>("dockerRestart") {
//  group = "$projectId.factorioServer"
//
//  workingDir = layout.projectDirectory.dir("src/test/resources/server/").asFile
//  commandLine = parseSpaceSeparatedArgs("docker-compose restart")
//}

val copyModToClientTask = tasks.register<Copy>("copyModToClient") {
  description = "Copy the mod to the Factorio client"
  group = "$projectId.factorioClient"

  dependsOn(modPackageTask)

  from(modPackageTask)
  into("""D:\Users\Adam\AppData\Roaming\Factorio\mods""")

  doLast {
    logger.lifecycle("Copying mod from ${source.files} to $destinationDir")
  }
}

//val factorioClientLaunch = tasks.register<Exec>("launch") {
//  description = "Run Factorio client"
//  group = "$projectId.factorioClient"
//
//  dependsOn(
//      serverRestartTask,
//      copyModToClientTask
//  )
//
////  workingDir = layout.projectDirectory.dir("src/test/resources/server/").asFile
//  commandLine = parseSpaceSeparatedArgs(
//      """explorer "steam://rungameid/427520 --mp-connect localhost" """
//  )
//}

val launch = tasks.register("modLauncher") {
  description = "Build the mod, upload to Server and Client, and start both"
  group = projectId
  dependsOn(
    copyModToServerTask,
    copyModToClientTask,
    factorioServerRestart,
//      factorioClientLaunch,
  )
}

tasks.register("downloadFactorioApiDocs") {
  group = projectId

  val target = uri("https://lua-api.factorio.com/latest/runtime-api.json")
  val apiFilename = File(target.path).name
  val downloadedFile = file("$temporaryDir/$apiFilename")

  val apiFile = modBuildDir.file(apiFilename)
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

    apiFile.asFile.writeText(prettyJson)

    logger.lifecycle("Downloaded Factorio API json: $apiFile")
  }
}
