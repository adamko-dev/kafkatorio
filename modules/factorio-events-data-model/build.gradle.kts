import org.gradle.process.internal.DefaultExecSpec
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinJsCompilation
import org.jetbrains.kotlin.gradle.targets.js.npm.npmProject
import org.jetbrains.kotlin.util.parseSpaceSeparatedArgs


plugins {
  id("dev.adamko.factoriowebmap.archetype.kotlin-multiplatform")
  kotlin("plugin.serialization")
}

val projectId: String by project.extra
val buildDir: Directory = layout.buildDirectory.dir(projectId).get()

kotlin {

  //<editor-fold desc="Java">
  jvm {
    compilations.configureEach {
      kotlinOptions {
        jvmTarget = "11"
        apiVersion = "1.5"
        languageVersion = "1.5"
      }
    }
  }

  sourceSets {
    val jvmMain by getting {
      dependencies {
        implementation("com.github.ntrrgc:ts-generator:1.1.1")
        implementation("org.jetbrains.kotlin:kotlin-reflect")

        val kotlinXSerializationVersion = "1.3.0"
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$kotlinXSerializationVersion")
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinXSerializationVersion")
      }
    }
    val jvmTest by getting {
    }
  }

  //</editor-fold>

  //<editor-fold desc="JS">
  js(IR) {
    moduleName = "data-model"
    browser { }
    binaries.executable()
  }

  sourceSets {
    val jsMain: KotlinSourceSet by getting {

      dependencies {

        implementation(npm("lua-types", "^2.11.0"))
        implementation(npm("@types/node", "^12.0.8"))
        implementation(npm("typescript-to-lua", "1.1.1"))
        implementation(npm("typescript", "4.4.4"))
        implementation(npm("typed-factorio", "0.11.0-packagetypes-1", false))

      }

    }

    val jsTest by getting {
      dependencies {
        implementation(kotlin("test-js"))
      }
    }
  }
  //</editor-fold>

  sourceSets.all {
    languageSettings.apply {
      languageVersion = "1.5"
//      enableLanguageFeature("InlineClasses") // language feature name
      optIn("kotlin.OptIn")
      optIn("kotlin.RequiresOptIn")
      optIn("kotlin.ExperimentalStdlibApi")
      optIn("kotlin.time.ExperimentalTime")
      optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
      optIn("kotlin.js.ExperimentalJsExport")
      optIn("kotlinx.serialization.ExperimentalSerializationApi")
//      progressiveMode = true // false by default
    }
  }
}




//kotlin.js()
//  .compilations
//  .getByName(KotlinCompilation.MAIN_COMPILATION_NAME)
//  .npmProject
//  .useTool()


afterEvaluate {

//  val nodeJsExecTstl by tasks.creating(NodeJsExec::class) {
//    group = project.name
//
//    nodeArgs += listOf(
//      "tstl",
//    )
//  }

  val kotlinJsCompilation: KotlinJsCompilation =
    kotlin.targets
      .flatMap { it.compilations }
      .filterIsInstance<KotlinJsCompilation>()
      .first { it.name == KotlinCompilation.MAIN_COMPILATION_NAME }
  val npmProject = kotlinJsCompilation.npmProject

  tasks.create<Exec>("tstl") {
    group = project.name

    standardOutput = System.out
    errorOutput = System.err

    workingDir = npmProject.dir
    executable =
      when (npmProject.nodeJs.requireConfigured().isWindows) {
        true  -> "npx.cmd"
        false -> "npx"
      }

    args(parseSpaceSeparatedArgs("tstl --out $temporaryDir"))

  }

  tasks.create("asd") {
    group = project.name

    doLast {

      val execSpec =
        project.objects.newInstance(DefaultExecSpec::class.java).apply {
          isIgnoreExitValue = false
          standardOutput = System.out
          errorOutput = System.err
        }

      kotlin.targets
        .flatMap { it.compilations }
        .filterIsInstance<KotlinJsCompilation>()
        .filter { it.name == KotlinCompilation.MAIN_COMPILATION_NAME }
        .forEach {

          org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsExec.create(it, "blahDeBlah") {

          }

          execSpec.workingDir = it.npmProject.dir

          execSpec.executable =
            when (it.npmProject.nodeJs.requireConfigured().isWindows) {
              true  -> "npx.cmd"
              false -> "npx"
            }

          execSpec.args("tstl", "--version")

//          execSpec.commandLine


//          it.npmProject.useTool(
//            execSpec,
//            "typescript-to-lua/dist/tstl.js",
//            listOf(),
//            listOf( "--outDir","$buildDir/testAsd"),
//            listOf("--version"),
//           listOf(),
//          )
        }

      exec {
        execSpec.copyTo(this)
      }

    }
  }

}


