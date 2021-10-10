import com.github.gradle.node.npm.task.NpmTask
import org.jetbrains.kotlin.util.parseSpaceSeparatedArgs

plugins {
  base
  id("com.github.node-gradle.node")
}

group = "${rootProject.group}.mod"
version = rootProject.version

node {
  download.set(true)
  version.set("14.18.0")

  nodeProjectDir.set(layout.projectDirectory.dir("src"))

  distBaseUrl.set(null as String?) // set root dependencyResolutionManagement
}

val projectId = rootProject.name + "-" + project.name
val modBuildDir = layout.buildDirectory.dir(projectId).get()
//val tsSrcDir = layout.projectDirectory.dir("src")

val tstlTask = tasks.register<NpmTask>("tstl") {
  description = "Typescript To Lua"
  group = projectId

  dependsOn(tasks.npmInstall)

  execOverrides { standardOutput = System.out }

  npmCommand.set(listOf("run", "build"))

  val outputDir = modBuildDir.dir("tstl")
  args.set(parseSpaceSeparatedArgs("-- --outDir $outputDir"))
  outputs.dir(outputDir)

  ignoreExitValue.set(false)
//  workingDir.set(tsSrcDir.asFile)
  inputs.dir(node.nodeProjectDir)
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

  from(layout.projectDirectory.dir("src")) {
    exclude("*.ts")
    into(rootProject.name)
  }
  from(tstlTask) {
    into(rootProject.name)
  }

  archiveFileName.set("${rootProject.name}_${rootProject.version}.zip")
  destinationDirectory.set(modBuildDir.dir("dist"))

}

tasks.build { dependsOn(tstlTask, modPackageTask) }

