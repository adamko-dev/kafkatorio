import dev.adamko.kafkatorio.gradle.asProvider
import dev.adamko.kafkatorio.gradle.typescriptAttributes
import dev.adamko.kafkatorio.task.GenerateTypeScriptTask
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinJvmCompilation
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


plugins {
  dev.adamko.kafkatorio.lang.`kotlin-multiplatform`
  id("io.kotest.multiplatform")
  kotlin("plugin.serialization")
  distribution
}


description = "shared data structures and utilities"


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
        optIn("kotlin.ExperimentalStdlibApi")
        optIn("kotlin.ExperimentalUnsignedTypes")
        optIn("kotlin.RequiresOptIn")
        optIn("kotlin.js.ExperimentalJsExport")
        optIn("kotlin.time.ExperimentalTime")
        optIn("kotlinx.coroutines.FlowPreview")
        optIn("kotlinx.serialization.ExperimentalSerializationApi")
      }
    }

    val commonMain by getting {
      dependencies {
        implementation(project.dependencies.platform(libs.kotlin.bom))

        implementation(project.dependencies.platform(libs.kotlinx.serialization.bom))
        implementation(libs.kotlinx.serialization.core)
        implementation(libs.kotlinx.serialization.json)
        api(libs.kxs.kxsTsGen)

        implementation(dependencies.platform(libs.okio.bom))
        implementation(libs.okio.core)

        implementation(dependencies.platform(libs.kotlinx.coroutines.bom))
        implementation(libs.kotlinx.coroutines.core)
      }
    }

    val commonTest by getting {
      dependencies {

        implementation(kotlin("test"))

        implementation(project.dependencies.platform(libs.kotest.bom))
        implementation(libs.kotest.core)
        implementation(libs.kotest.datatest)
        implementation(libs.kotest.frameworkEngine)
        implementation(libs.kotest.json)
        implementation(libs.kotest.prop)
      }
    }

    val jvmMain by getting {
      dependencies {
        implementation(kotlin("reflect"))
      }
    }

    val jvmTest by getting {
      dependencies {
        implementation(libs.kotest.runnerJunit5)
      }
    }

    val jsTest by getting {
      dependencies {
//        implementation(kotlin("test-js"))
      }
    }
  }
}


val kotlinJvmMainCompilation: NamedDomainObjectProvider<KotlinJvmCompilation> =
  kotlin.jvm().compilations.named(KotlinCompilation.MAIN_COMPILATION_NAME)


val jvmJar: TaskProvider<Jar> = tasks.named<Jar>(kotlin.jvm().artifactsTaskName)

val kotlinMainRuntimeDependencies: Provider<FileCollection> =
  kotlinJvmMainCompilation.map { it.runtimeDependencyFiles }

val kotlinMainCompileDependencies: Provider<FileCollection> =
  kotlinJvmMainCompilation.map { it.compileDependencyFiles }

val kotlinMainCompileTask: Provider<KotlinCompile> =
  kotlinJvmMainCompilation.map { it.compileKotlinTask }


val generateTypeScript by tasks.registering(GenerateTypeScriptTask::class) {
  dependsOn(jvmJar)
//  dependsOn(kotlinMainCompileTask)

//  inputs.files(jvmJar.map { it.outputs.files })
//  inputs.files(kotlinMainCompileTask.map { it.javaSources })

  classpath(
    jvmJar,
    kotlinMainRuntimeDependencies,
//    kotlinMainCompileTask.map { it.libraries }
//    kotlinMainDependencies,
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


//val schemaTs by distributions.registering {
//  distributionBaseName.set(schemaTsDistributionName)
//  contents {
//    from(generateTypeScriptOutputFiles)
//  }
//}


//val schemaTsZipTask: TaskProvider<Zip> = tasks.named<Zip>("${schemaTs.name}DistZip")
//val schemaTsZipTaskArchiveFile: Provider<RegularFile> = schemaTsZipTask.flatMap { it.archiveFile }


val schemaTsZip by tasks.registering(Zip::class) {
  group = "kt2ts"

  archiveBaseName.set("schema-ts")
  from(generateTypeScriptOutputFiles)
  destinationDirectory.set(layout.buildDirectory.dir("distributions/kt2ts"))
}
val schemaTsZipTaskArchiveFile: Provider<RegularFile> = schemaTsZip.flatMap { it.archiveFile }


val typeScriptModelGenerated: Configuration by configurations.creating {
  asProvider()
  typescriptAttributes(objects)

  outgoing.artifact(schemaTsZipTaskArchiveFile)
//  outgoing.artifact(schemaTsZipTaskArchiveFile)
}
