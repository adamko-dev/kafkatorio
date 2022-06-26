import dev.adamko.kafkatorio.gradle.asProvider
import dev.adamko.kafkatorio.gradle.typescriptAttributes
import dev.adamko.kafkatorio.task.GenerateTypeScriptTask


plugins {
  dev.adamko.kafkatorio.lang.`kotlin-multiplatform`
  id("io.kotest.multiplatform")
  kotlin("plugin.serialization")
  distribution
}


kotlin {

  js(IR) {
    browser {}
    // this is a library - don't set binaries.executable()
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
        implementation(libs.kotest.core)
        implementation(libs.kotest.datatest)
        implementation(libs.kotest.prop)
        implementation(libs.kotest.json)
      }
    }

    val jvmMain by getting {
      dependencies {
        implementation("com.github.aSemy:ts-generator:v1.2.1")
        implementation(kotlin("reflect"))
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


val generateTypeScript by tasks.registering(GenerateTypeScriptTask::class) {
  dependsOn(jvmJar)
  classpath(
    jvmJar,
    mainCompilation,
  )
  output.set(layout.buildDirectory.dir("generated/typescript"))
  mainClass.set("dev.adamko.kafkatorio.events.schema.Kt2ts2Kt")
  args(temporaryDir.canonicalPath)
}

val schemaTsDistributionName: Provider<String> = providers.provider {
  "${rootProject.name}-${project.name}"
}

val generateTypeScriptOutputFiles: Provider<FileTree> =
  generateTypeScript.map { it.outputs.files.asFileTree }

val schemaTs by distributions.registering {
  distributionBaseName.set(schemaTsDistributionName)
  contents {
    from(generateTypeScriptOutputFiles)
  }
}

val schemaTsZipTask: TaskProvider<Zip> = tasks.named<Zip>("${schemaTs.name}DistZip")
val schemaTsZipTaskArchiveFile: Provider<RegularFile> = schemaTsZipTask.flatMap { it.archiveFile }

val typeScriptModelGenerated: Configuration by configurations.creating {
  asProvider()
  typescriptAttributes(objects)

  outgoing.artifact(schemaTsZipTaskArchiveFile)
}
