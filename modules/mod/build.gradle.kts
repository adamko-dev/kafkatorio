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
  description = "Typescript To Lua"
  group = projectId

  dependsOn(tasks.npmInstall)

  execOverrides { standardOutput = System.out }

  npmCommand.set(listOf(
      "run",
      "build",
//      "--workspace=factorio-web-map-mod",
  ))

  inputs.dir(node.nodeProjectDir)

  val outputDir = modBuildDir.dir("typescriptToLua")
  outputs.dir(outputDir)
  args.set(parseSpaceSeparatedArgs("-- --outDir $outputDir"))
//  args.set(listOf(
//      "--",
//      "--outDir $outputDir",
//      "--workspace=factorio-web-map-mod",
//  ))

  ignoreExitValue.set(false)
//  workingDir.set(tsSrcDir.asFile)
//  inputs.dir("node_modules")
//  inputs.dir(fileTree("src"))
//  inputs.files(
//      "tsconfig.json",
//      "package.json",
//  )
//  outputs.dir("$buildDir/factorio-web-map/")
//  outputs.dir("$buildDir/npm-output")
//  outputs.upToDateWhen { false }
}

val modPackageTask = tasks.register<Zip>("package") {
  description = "Package mod"
  group = projectId

  dependsOn(tstlTask)

  from(
      layout.projectDirectory.dir("src/main/resources/mod-data"),
      rootProject.layout.projectDirectory.file("LICENSE"),
      tstlTask,
  )

  into(rootProject.name)

  // Factorio required format is:
  // - filename: `mod-name_version.zip`
  // - zip contains dir `mod-name`
  archiveFileName.set("${rootProject.name}_${rootProject.version}.zip")
  destinationDirectory.set(modBuildDir.dir("dist"))
}

//tasks.build { dependsOn(tstlTask, modPackageTask) }

val copyModToServerTask = tasks.register<Copy>("copyModToServer") {
  description = "Copy the mod to the Factorio Docker server"
  group = projectId

  dependsOn(modPackageTask)

  from(modPackageTask)
  into(layout.projectDirectory.dir("src/test/resources/server/data/mods").asFile)
}

val copyModToClientTask = tasks.register<Copy>("copyModToClient") {
  description = "Copy the mod to the Factorio client"
  group = projectId

  dependsOn(modPackageTask)

  from(modPackageTask)
  into("""D:\Users\Adam\AppData\Roaming\Factorio\mods""")
}

val serverRestartTask = tasks.register<Exec>("serverRestart") {
  group = projectId

  workingDir = layout.projectDirectory.dir("src/test/resources/server/").asFile
  commandLine = parseSpaceSeparatedArgs("docker-compose restart")
}

tasks.register<Exec>("serverStop") {
  group = projectId

  workingDir = layout.projectDirectory.dir("src/test/resources/server/").asFile
  commandLine = parseSpaceSeparatedArgs("docker-compose stop")
}

val launch = tasks.register("modLauncher") {
  group = projectId
  dependsOn(
      copyModToServerTask,
      copyModToClientTask,
      serverRestartTask,
  )
}
