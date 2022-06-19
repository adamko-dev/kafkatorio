import dev.adamko.kafkatorio.gradle.asProvider
import dev.adamko.kafkatorio.gradle.typescriptAttributes
import dev.adamko.kafkatorio.task.GenerateTypescriptTask


plugins {
  dev.adamko.kafkatorio.lang.`kotlin-multiplatform`
  id("io.kotest.multiplatform")
  kotlin("plugin.serialization")
  distribution
}


kotlin {

  js(IR) {
//    binaries.executable()
    browser {}
    // browser{} causes this error:
    // Cannot load "webpack", it is not registered!
    // Perhaps you are missing some plugin?
    // Cannot load "sourcemap", it is not registered!
    // Perhaps you are missing some plugin?
    // Server start failed on port 9876: Error: No provider for "framework:mocha"! (Resolving: framework:mocha)
    // browser {
    //   runTask {
    //     sourceMaps = true
    //   }
    //   testTask {
    //     useKarma {
    //       useChromeHeadless()
    //     }
    //   }
    // }
//    nodejs()
  }
  jvm {
    val main by compilations.getting {
      kotlinOptions {
        jvmTarget = "11"
      }
    }
    testRuns["test"].executionTask.configure {
      useJUnitPlatform()
    }
  }

  sourceSets {

    all {
      languageSettings.apply {
        optIn("kotlin.RequiresOptIn")
        optIn("kotlin.ExperimentalStdlibApi")
        optIn("kotlin.time.ExperimentalTime")
        optIn("kotlinx.serialization.ExperimentalSerializationApi")
        optIn("kotlin.js.ExperimentalJsExport")
      }
    }

    val commonMain by getting {
      dependencies {
        implementation(project.dependencies.platform(libs.kotlin.bom))

        implementation(project.dependencies.platform(libs.kotlinx.serialization.bom))
        implementation(libs.kotlinx.serialization.core)
        implementation(libs.kotlinx.serialization.json)
        api("dev.adamko.kxstsgen:kxs-ts-gen-core:0.1.3")
      }
    }

    val commonTest by getting {
      dependencies {

        implementation(kotlin("test"))

        implementation(project.dependencies.platform(libs.kotest.bom))
        implementation("io.kotest:kotest-framework-engine")
//        implementation("io.kotest:kotest-assertions-core")
//        implementation("io.kotest:kotest-property")
//        implementation("io.kotest:kotest-assertions-json")
        implementation(libs.kotest.core)
        implementation(libs.kotest.datatest)
        implementation(libs.kotest.prop)
        implementation(libs.kotest.json)
      }
    }

    val jvmMain by getting {
      dependencies {
        implementation("com.github.aSemy:ts-generator:v1.2.1")
//        implementation("com.github.ntrrgc:ts-generator:1.1.2")
        implementation(kotlin("reflect"))

//        implementation("com.github.adamko-dev:kotlinx-serialization-typescript-generator:0.0.5")
      }
    }

    val jvmTest by getting {
      dependencies {
        implementation("io.kotest:kotest-runner-junit5")
      }
    }

    val jsTest by getting {
      dependencies {
        implementation(kotlin("test-js"))
      }
    }
  }
}

val jvmJar: TaskProvider<Jar> = tasks.named<Jar>(kotlin.jvm().artifactsTaskName)
val mainCompilation: Provider<FileCollection> =
  kotlin.jvm().compilations.named("main").map { it.runtimeDependencyFiles }


val generateTypescript by tasks.registering(GenerateTypescriptTask::class) {
  dependsOn(jvmJar)
  classpath(
    jvmJar,
    mainCompilation,
  )
  output.set(layout.buildDirectory.dir("generated/typescript"))
  mainClass.set("dev.adamko.kafkatorio.events.schema.Kt2ts2Kt")
  args(temporaryDir.canonicalPath)
}

//val generateTypescript2 by tasks.registering(JavaExec::class) {
//  group = "kt2ts"
//
//  dependsOn(jvmJar)
//
//  inputs.files(mainCompilation)
//
//  classpath(
//    jvmJar,
//    mainCompilation,
//  )
//  mainClass.set("dev.adamko.kafkatorio.events.schema.Kt2ts2Kt")
//
//  args(temporaryDir)
//
//  val buildOutput = layout.buildDirectory.dir("generated/typescript")
//  outputs.dir(buildOutput)
//
//  doFirst {
//    delete(temporaryDir)
//    mkdir(temporaryDir)
//  }
//
//  doLast {
//    sync {
//      from(temporaryDir)
//      into(buildOutput)
//      include("**/*.ts")
//    }
//  }
//}

val schemaTsDistributionName: Provider<String> = providers.provider {
  "${rootProject.name}-${project.name}"
}

val generateTypescriptOutputFiles: Provider<FileTree> =
  generateTypescript.map { it.outputs.files.asFileTree }

val schemaTs by distributions.registering {
  distributionBaseName.set(schemaTsDistributionName)
  contents {
    from(generateTypescriptOutputFiles)
  }
}

val schemaTsZipTask: TaskProvider<Zip> = tasks.named<Zip>("${schemaTs.name}DistZip")
val schemaTsZipTaskArchiveFile: Provider<RegularFile> = schemaTsZipTask.flatMap { it.archiveFile }

val typescriptModelGenerated: Configuration by configurations.creating {
  asProvider()
  typescriptAttributes(objects)

  outgoing.artifact(schemaTsZipTaskArchiveFile)
}


//tasks.matching {
//  it.name == "jsGenerateExternalsIntegrated"
//}.configureEach {
//  @Suppress("UnstableApiUsage")
//  notCompatibleWithConfigurationCache("try to prevent 'Projects must be configuring' error")
////  outputs.upToDateWhen { true }
//  dependsOn(":kotlinNpmInstall")
//}
