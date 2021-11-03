import com.github.gradle.node.npm.task.NpmTask
import org.apache.tools.ant.filters.ReplaceTokens
import org.jetbrains.kotlin.util.parseSpaceSeparatedArgs

plugins {
  id("dev.adamko.factoriowebmap.archetype.node")
}

val projectId: String by project.extra
val tokens: Map<String, String> by project.extra

val tsSrcDir: Directory = layout.projectDirectory.dir("src/main/typescript")
val modBuildDir: Directory = layout.buildDirectory.dir(projectId).get()

node {
  nodeProjectDir.set(tsSrcDir)
}

val tsProto: Configuration by configurations.creating {
  isCanBeConsumed = false
  isCanBeResolved = true
}

dependencies {
  tsProto(projects.modules.factorioEventsDataModel.dependencyProject.configurations.getByName("tsProto"))
}

val tsProtoFetch by tasks.creating(Sync::class) {
  group = project.name

  dependsOn(projects.modules.factorioEventsDataModel.dependencyProject.tasks.named("protobufTypescript"))

  val genSrcDir = projects
    .modules
    .factorioEventsDataModel
    .dependencyProject
    .layout
    .buildDirectory
    .dir("pbAndG/generated-sources/typescript")

  val targetDir = "$projectDir/src/main/typescript/proto"

  inputs.dir(genSrcDir)

  from(genSrcDir)
  into(targetDir)

//  doLast {
//
//    fileTree(targetDir).forEach {
//      var text = it.readText()
//
//      text = text.replace(
//        Regex(
//          """if \(_m0\.util\.Long !== Long\)\s*\{\s*_m0\.util\.Long = Long as any;\s*_m0\.configure\(\);\s*\}"""
//        ),
//        ""
//      )
//
//      text = text.replace(
//        Regex("""import Long from "long";"""),
//        ""
//      )
//
//      text = text.replace(
//        Regex("""import _m0 from "protobufjs\/minimal";"""),
//        ""
//      )
//
//      it.writeText(text)
//    }
//  }

}

tasks.assemble { dependsOn(tsProtoFetch) }


val tstlTask = tasks.register<NpmTask>("typescriptToLua") {
  description = "Convert Typescript To Lua"
  group = projectId

  dependsOn(tasks.npmInstall)

  execOverrides { standardOutput = System.out }

  npmCommand.set(listOf("run", "build"))

  inputs.dir(node.nodeProjectDir)
    .skipWhenEmpty()
    .withPropertyName("sourceFiles")
    .withPathSensitivity(PathSensitivity.RELATIVE)

  val intermediateOutputDir = temporaryDir
  args.set(parseSpaceSeparatedArgs("-- --outDir $intermediateOutputDir"))
  val outputDir = modBuildDir.dir("typescriptToLua")
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

idea {
  module {
    sourceDirs.add(layout.projectDirectory.dir("src/main/typescript").asFile)
    excludeDirs.add(layout.projectDirectory.dir("src/main/typescript/node_modules").asFile)
    resourceDirs.add(layout.projectDirectory.dir("src/main/resources").asFile)

    resourceDirs.add(layout.projectDirectory.dir("infra").asFile)
    excludeDirs.add(layout.projectDirectory.dir("infra/factorio-server").asFile)
  }
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
    val prettyJson = groovy.json.JsonOutput.prettyPrint(json)

    apiFile.asFile.writeText(prettyJson)

    logger.lifecycle("Downloaded Factorio API json: $apiFile")
  }
}

//tasks.register("downloadLuaProtobuf") {
//  group = projectId
//
//  val luaProtobufVersion = "0.3.3"
//
//  val target = uri("https://github.com/starwing/lua-protobuf/archive/refs/tags/$luaProtobufVersion.zip")
//  val downloadedFile = file("$temporaryDir/luaProtobuf-$luaProtobufVersion.zip")
//
//  val luaProto = modBuildDir.file(apiFilename)
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
//    val prettyJson = groovy.json.JsonOutput.prettyPrint(json)
//
//    apiFile.asFile.writeText(prettyJson)
//
//    logger.lifecycle("Downloaded Factorio API json: $apiFile")
//  }
//}