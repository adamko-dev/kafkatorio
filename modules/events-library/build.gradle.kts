import dev.adamko.gradle.factorio.internal.typescriptAttributes
import kafkatorio.tasks.GenerateTypeScriptTask
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinJvmCompilation


plugins {
  id("kafkatorio.conventions.lang.kotlin-multiplatform")
  id("dev.adamko.factorio-mod-library") // only needed for 'attributes' - need to split up the plugin?
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
    testRuns.configureEach {
      executionTask.configure {
        useJUnitPlatform()
      }
    }
  }

  @OptIn(ExperimentalKotlinGradlePluginApi::class)
  compilerOptions {
    optIn.addAll(
      "kotlin.ExperimentalStdlibApi",
      "kotlin.ExperimentalUnsignedTypes",
//  "kotlin.RequiresOptIn" ,
      "kotlin.js.ExperimentalJsExport",
      "kotlin.time.ExperimentalTime",
      "kotlinx.coroutines.FlowPreview",
      "kotlinx.serialization.ExperimentalSerializationApi",
    )
  }

  sourceSets {
//    configureEach {
//      languageSettings.apply {
//        optIn("kotlin.ExperimentalStdlibApi")
//        optIn("kotlin.ExperimentalUnsignedTypes")
//        optIn("kotlin.RequiresOptIn")
//        optIn("kotlin.js.ExperimentalJsExport")
//        optIn("kotlin.time.ExperimentalTime")
//        optIn("kotlinx.coroutines.FlowPreview")
//        optIn("kotlinx.serialization.ExperimentalSerializationApi")
//      }
//    }

    commonMain {
      dependencies {
        implementation(dependencies.platform(projects.modules.versionsPlatform))

        implementation(libs.kotlinxSerialization.core)
        implementation(libs.kotlinxSerialization.json)
        api(libs.kotlinxSerialization.kxsTsGen)

        implementation(libs.okio.core)

        implementation(libs.kotlinx.coroutines.core)
      }
    }

    commonTest {
      dependencies {
        implementation(dependencies.platform(projects.modules.versionsPlatform))

        implementation(kotlin("test"))

        implementation(libs.kotest.core)
        implementation(libs.kotest.datatest)
        implementation(libs.kotest.frameworkEngine)
        implementation(libs.kotest.json)
        implementation(libs.kotest.prop)
      }
    }

    jvmMain {
      dependencies {
        implementation(dependencies.platform(projects.modules.versionsPlatform))

        implementation(kotlin("reflect"))
      }
    }

    jvmTest {
      dependencies {
        implementation(dependencies.platform(projects.modules.versionsPlatform))

        implementation(libs.kotest.runnerJunit5)
      }
    }

    jsTest {
      dependencies { }
    }
  }
}


val kotlinJvmMainCompilation: NamedDomainObjectProvider<KotlinJvmCompilation> =
  kotlin.jvm().compilations.named(KotlinCompilation.MAIN_COMPILATION_NAME)


val jvmJar: TaskProvider<Jar> = tasks.named<Jar>(kotlin.jvm().artifactsTaskName)

val kotlinMainRuntimeDependencies: Provider<FileCollection> =
  kotlinJvmMainCompilation.map { it.runtimeDependencyFiles }

//val kotlinMainCompileDependencies: Provider<FileCollection> =
//  kotlinJvmMainCompilation.map { it.compileDependencyFiles }

//val kotlinMainCompileTask: Provider<KotlinCompile> =
//  kotlinJvmMainCompilation.map { it.compileKotlinTask }


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
  mainClass.set("dev.adamko.kafkatorio.schema.Kt2ts2Kt")
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
