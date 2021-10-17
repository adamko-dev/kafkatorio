import com.github.gradle.node.npm.task.NpmTask
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension
import org.jetbrains.kotlin.util.parseSpaceSeparatedArgs

plugins {
  id("dev.adamko.factoriowebmap.archetype.node")
}

val projectId: String by project.extra

val tsSrcDir = layout.projectDirectory.dir("src/main/typescript")
val modBuildDir = layout.buildDirectory.dir(projectId).get()

node {
  nodeProjectDir.set(tsSrcDir)
}

val tstlTask = tasks.register<NpmTask>("typescriptToLua") {
  description = "Convert Typescript To Lua"
  group = projectId

  dependsOn(tasks.npmInstall)

  execOverrides { standardOutput = System.out }

  npmCommand.set(listOf("run", "build"))

  inputs.dir(node.nodeProjectDir.asFileTree)
      .skipWhenEmpty()
      .withPropertyName("sourceFiles")
      .withPathSensitivity(PathSensitivity.RELATIVE)

  val outputDir = modBuildDir.dir("typescriptToLua")
  args.set(parseSpaceSeparatedArgs("-- --outDir $outputDir"))
  outputs.dir(outputDir)
      .withPropertyName("outputDir")


  ignoreExitValue.set(false)
}

//val typescriptSrc =
//    project.objects.sourceDirectorySet("typescript", "typescript").apply {
//      srcDir(
//          layout.projectDirectory.dir("src/main/typescript")
//      )
//      destinationDirectory.set(
//          modBuildDir.dir("typescriptToLua")
//      )
//      compiledBy(tstlTask) {
//        project.objects.directoryProperty().apply {
//          set( modBuildDir.dir("typescriptToLua"))
//        }
//      }
//    }

val modPackageTask = tasks.register<Zip>("package") {
  description = "Package mod files into ZIP"
  group = projectId

  dependsOn(tstlTask)

  from(
      layout.projectDirectory.dir("src/main/resources/mod-data"),
      rootProject.layout.projectDirectory.file("LICENSE"),
      tstlTask.get().outputs.files.filter { it.extension == "lua" },
  )

  into(rootProject.name)

  // Factorio required format is:
  // - filename: `mod-name_version.zip`
  // - zip contains one directory, `mod-name`
  archiveFileName.set("${rootProject.name}_${rootProject.version}.zip")
  destinationDirectory.set(modBuildDir.dir("dist"))
}

//tasks.build { dependsOn(tstlTask, modPackageTask) }

val copyModToServerTask = tasks.register<Copy>("copyModToServer") {
  description = "Copy the mod to the Factorio Docker server"
  group = "$projectId.factorioServer"

  dependsOn(modPackageTask)

  from(modPackageTask)
  into(layout.projectDirectory.dir("src/test/resources/server/data/mods").asFile)
}

val serverUpTask = tasks.register<Exec>("dockerUp") {
  group = "$projectId.factorioServer"

  workingDir = layout.projectDirectory.dir("src/test/resources/server/").asFile
  commandLine = parseSpaceSeparatedArgs("docker-compose up")
}


//val serverRestartTask = tasks.register<Exec>("dockerRestart") {
//  group = "$projectId.factorioServer"
//
//  workingDir = layout.projectDirectory.dir("src/test/resources/server/").asFile
//  commandLine = parseSpaceSeparatedArgs("docker-compose restart")
//}

tasks.register<Exec>("dockerStop") {
  group = "$projectId.factorioServer"

  workingDir = layout.projectDirectory.dir("src/test/resources/server/").asFile
  commandLine = parseSpaceSeparatedArgs("docker-compose stop")
}

val copyModToClientTask = tasks.register<Copy>("copyModToClient") {
  description = "Copy the mod to the Factorio client"
  group = "$projectId.factorioClient"

  dependsOn(modPackageTask)

  from(modPackageTask)
  into("""D:\Users\Adam\AppData\Roaming\Factorio\mods""")
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
      serverUpTask,
//      factorioClientLaunch,
  )
}

tasks.register("downloadFactorioApiDocs") {
  group = projectId

  val target = uri("https://lua-api.factorio.com/latest/runtime-api.json")
  val apiFilename = File(target.path).name
  val downloadedFile = file(temporaryDir.path + "/" + apiFilename)

  val apiFile = modBuildDir.file(apiFilename)
  outputs.file(apiFile)

  doLast {

    ant.invokeMethod("get", mapOf(
        "src" to target,
        "dest" to downloadedFile,
        "verbose" to true,
    ))

    val json = downloadedFile.readText()
    val prettyJson = groovy.json.JsonOutput.prettyPrint(json)

    apiFile.asFile.writeText(prettyJson)

    logger.lifecycle("Downloaded Factorio API json: $apiFile")
  }
}
