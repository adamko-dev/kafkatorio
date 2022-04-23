import dev.adamko.kafkatorio.gradle.asProvider
import dev.adamko.kafkatorio.gradle.typescriptAttributes


plugins {
  dev.adamko.kafkatorio.lang.`kotlin-multiplatform`
  id("io.kotest.multiplatform")
  kotlin("plugin.serialization")
  distribution
}


kotlin {

  js(IR) {
    binaries.executable()
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
    nodejs()
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
        api("dev.adamko.kxtsgen:kxs-ts-gen-core:dev-SNAPSHOT")
//        implementation("com.github.adamko-dev:kotlinx-serialization-typescript-generator:dev-SNAPSHOT") {
//          isChanging = true
//        }
      }
    }

    val commonTest by getting {
      dependencies {

        implementation(kotlin("test"))

        implementation(project.dependencies.platform(libs.kotest.bom))
        implementation("io.kotest:kotest-framework-engine")
        implementation("io.kotest:kotest-assertions-core")
        implementation("io.kotest:kotest-property")
        implementation("io.kotest:kotest-assertions-json")
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


val generateTypescript by tasks.registering(JavaExec::class) {
  group = "kt2ts"

  val jvmJar = tasks.named<Jar>(kotlin.jvm().artifactsTaskName)
  dependsOn(jvmJar)

  val mainCompilation = kotlin.jvm().compilations.named("main").map { it.runtimeDependencyFiles }
//  dependsOn(mainCompilation)
  inputs.files(mainCompilation)

  classpath(
    jvmJar,
    mainCompilation
  )

//  mainClass.set("dev.adamko.kafkatorio.events.schema.Kt2tsKt")
  mainClass.set("dev.adamko.kafkatorio.events.schema.Kt2ts2Kt")

  val tempOutputDirectory = file("$temporaryDir")
  args(tempOutputDirectory)

  val buildOutput = layout.buildDirectory.dir("generated/typescript")
  outputs.dir(buildOutput)

  doFirst {
    delete(temporaryDir)
    mkdir(temporaryDir)
  }

  doLast {
    sync {
      from(temporaryDir)
      into(buildOutput)
      include("**/*.ts")
    }
  }
}

val schemaTs by distributions.registering {
  distributionBaseName.set("${rootProject.name}-${project.name}")
  contents {
    from(generateTypescript.map { it.outputs.files.asFileTree })
  }
}

val typescriptModelGenerated: Configuration by configurations.creating {
  asProvider()
  typescriptAttributes(objects)

  val schemaTsZipTask = tasks.named<Zip>("${schemaTs.name}DistZip")
  outgoing.artifact(schemaTsZipTask.flatMap { it.archiveFile })
}
