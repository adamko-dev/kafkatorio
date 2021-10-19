import com.github.gradle.node.npm.task.NpmTask
import com.github.gradle.node.task.NodeTask
import org.gradle.api.internal.project.ProjectInternal
import org.jetbrains.kotlin.gradle.targets.js.dukat.DukatRunner
import org.jetbrains.kotlin.gradle.targets.js.dukat.ExternalsOutputFormat
import org.jetbrains.kotlin.gradle.targets.js.npm.tasks.RootPackageJsonTask
import org.jetbrains.kotlin.gradle.targets.js.npm.tasks.KotlinPackageJsonTask
import org.jetbrains.kotlin.util.parseSpaceSeparatedArgs
import org.jetbrains.kotlin.gradle.targets.js.npm.NpmProject
import org.jetbrains.kotlin.gradle.targets.js.npm.npmProject

plugins {
  id("dev.adamko.factoriowebmap.archetype.kotlin-js")
}

//group = rootProject.group.toString() + "." + project.name
//version = rootProject.version
//
//val projectId = rootProject.name + "-" + project.name
val projectId: String by project.extra
val modBuildDir: Directory = layout.buildDirectory.dir(projectId).get()
val nodeModulesDir: Directory by project.extra

kotlin {

  js(IR) {

    binaries.executable()

    useCommonJs()
    nodejs()

//    compilations.all {
    compilations["main"].apply {
      packageJson {
        customField(
            "scripts",
            mapOf(
                "build" to "tstl",
                "dev" to "tstl --watch",
            )
        )
      }
    }


//      kotlin.srcDir("src/main/myKotlin")
//    }
    sourceSets["main"].apply {
//    sourceSets.create("dukat").apply {
      kotlin.srcDir(project.layout.projectDirectory.dir("dukat-custom"))
    }


//    sourceSets {
//      all {
//        languageSettings.optIn("kotlin.ExperimentalStdlibApi")
//        languageSettings.optIn("kotlin.time.ExperimentalTime")
//      }
//    }
  }
}

dependencies {


  // bom is defined in archetype.kotlin-js

  implementation("org.jetbrains.kotlin-wrappers:kotlin-react")
//  implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom")
  implementation(npm("react", "17.0.2"))
//  implementation(npm("react-dom", "17.0.2"))

//        implementation("org.jetbrains.kotlinx:kotlinx-nodejs:0.0.7") {
//          because("https://github.com/Kotlin/kotlinx-nodejs")
//        }

  val generateExternals = false
  implementation(npm("typescript-to-lua", "1.0.1", generateExternals = generateExternals))
  implementation(npm("typed-factorio", "0.7.1", generateExternals = true))
  implementation(npm("lua-types", "2.11.0", generateExternals = generateExternals))
  implementation(npm("typescript", "4.4.3", generateExternals = generateExternals))

  testImplementation(kotlin("test-js"))
}

//val rootPackageJson: RootPackageJsonTask by tasks.getting(RootPackageJsonTask::class)
//val rootPackageJson by rootProject.tasks.getting(RootPackageJsonTask::class)
//val packageJson by rootProject.tasks.getting(KotlinPackageJsonTask::class)
//val packageJson : RootPackageJsonTask by tasks.named<RootPackageJsonTask>(RootPackageJsonTask.NAME)

//node {
//  download.set(true)
//  version.set("14.18.0")
//
//  distBaseUrl.set(null as String?) // set by dependencyResolutionManagement
//
//  nodeProjectDir.set(rootPackageJson.rootPackageJson.parentFile.normalize())
////  nodeProjectDir.set(packageJson.packageJson.parentFile.normalize())
//}


//fun NpmTask.setNodeModulesPath(path: String): Unit =
//    environment.put("NODE_PATH", path)
//
//fun NpmTask.setNodeModulesPath(folder: File): Unit =
//    setNodeModulesPath(folder.normalize().absolutePath)

tasks {
  register<NpmTask>("tstl") {
    description = "Typescript To Lua"
    group = projectId

    dependsOn(build)

//    setNodeModulesPath(node.nodeProjectDir.dir("node_modules").get().asFile)
//    setNodeModulesPath(node.nodeProjectDir.dir(NpmProject.NODE_MODULES).get().asFile)
    val nodeModulesDir: Directory by project.extra
    environment.put("NODE_PATH", nodeModulesDir.asFile.absolutePath)

    execOverrides { standardOutput = System.out }

    npmCommand.set(listOf("run", "build"))

    val outputDir = modBuildDir.dir("tstl")
    args.set(parseSpaceSeparatedArgs("-- --outDir $outputDir"))
    outputs.dir(outputDir)

    ignoreExitValue.set(false)
    inputs.dir(rootProject.layout.buildDirectory.dir("js"))
  }

  register<DefaultTask>("dukat-typed-factorio") {
    group = projectId

    val npmProject = kotlin.js().compilations["main"].npmProject

    val files = nodeModulesDir.dir("typed-factorio")
        .asFileTree
        .filter { it.extension in setOf("ts", "d.ts") }
        .files

    logger.lifecycle("Converting typed-factorio files: ${files.joinToString { it.name }}")

    val runner = DukatRunner(
        npmProject,
        files,
        ExternalsOutputFormat.SOURCE,
        project.layout.projectDirectory.dir("dukat-custom").asFile,
    )
    val services = (project as ProjectInternal).services
    doLast {
      runner.execute(services)
    }
  }
}


configure<NodeJsRootExtension> {
  versions.dukat.version = "0.5.8-rc.4-dev.20211001"
//  versions.dukat.version = "0.5.8-rc.4"
}
