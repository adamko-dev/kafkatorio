import dev.adamko.gradle.pbandg.task.ProtobufCompileTask
import org.jetbrains.kotlin.gradle.targets.js.npm.NpmProject
import org.jetbrains.kotlin.gradle.targets.js.npm.tasks.RootPackageJsonTask

plugins {
  id("dev.adamko.factoriowebmap.archetype.base")
  kotlin("multiplatform")

//  id("dev.adamko.factoriowebmap.archetype.kotlin-jvm")
  id("dev.adamko.gradle.pbandg.pb-and-g")
}

val projectId: String by project.extra
val buildDir: Directory = layout.buildDirectory.dir(projectId).get()


dependencies {
  protobufLibrary("com.google.protobuf:protobuf-javalite:3.19.1")
//
//  api("com.google.protobuf:protobuf-javalite:3.19.1")
//  api("com.google.protobuf:protobuf-kotlin-lite:3.19.1")
}

idea {
  workspace {

  }
}

kotlin {
  sourceSets.all {
    languageSettings.apply {
      languageVersion = "1.5"
//      enableLanguageFeature("InlineClasses") // language feature name
      optIn("kotlin.OptIn")
      optIn("kotlin.ExperimentalStdlibApi")
      optIn("kotlin.time.ExperimentalTime")
      optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
      optIn("kotlin.js.ExperimentalJsExport")
//      progressiveMode = true // false by default
    }
  }

  //<editor-fold desc="Protobuf">
  sourceSets {
    val proto by creating {
    }
  }
  //</editor-fold>

  //<editor-fold desc="Java">
  jvm {
    compilations.configureEach {
      kotlinOptions {
        jvmTarget = "11"
      }
    }
  }

  sourceSets {
    val jvmMain by getting {
      dependencies {
        api("com.google.protobuf:protobuf-javalite:3.19.1")
        api("com.google.protobuf:protobuf-kotlin-lite:3.19.1")
      }
    }
    val jvmTest by getting {
    }
  }
  //</editor-fold>

  //<editor-fold desc="JS">
  js(IR) {
    binaries.executable()
    browser {
      commonWebpackConfig {
        cssSupport.enabled = true
      }
    }

    useCommonJs()
    nodejs()

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

  }

  sourceSets {

    val jsMain by getting {
      kotlin.srcDir("externals02")
//      kotlin.srcDir("externals")

      dependencies {

        val kotlinWrappersVersion = "0.0.1-pre.254-kotlin-1.5.31"
        implementation(
          project.dependencies.enforcedPlatform(
            "org.jetbrains.kotlin-wrappers:kotlin-wrappers-bom:${kotlinWrappersVersion}"
          )
        )

//        implementation("org.jetbrains.kotlin-wrappers:kotlin-react")
//        implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom")
//        implementation(npm("react", "17.0.2"))
//        implementation(npm("react-dom", "17.0.2"))

//        implementation("org.jetbrains.kotlinx:kotlinx-nodejs:0.0.7") {
//          because("https://github.com/Kotlin/kotlinx-nodejs")
//        }

//        implementation(npm("typescript-to-lua", "1.0.1"))
//        implementation(npm("typed-factorio", "0.7.1"))
//        implementation(npm("lua-types", "2.11.0"))
//        implementation(npm("typescript", "4.4.3"))


        implementation(npm("ts-proto", "1.83.3"))
      }
    }

    val jsTest by getting {
      dependencies {
        implementation(kotlin("test-js"))
      }
    }
  }
  //</editor-fold>

}

//rootProject.tasks.withType<RootPackageJsonTask>().configureEach {
//}

val rootPackageJson by rootProject.tasks.getting(RootPackageJsonTask::class)
val nodePath: Directory by extra {
  val file = rootPackageJson.rootPackageJson.parentFile.normalize()
  logger.lifecycle("Kotlin/JS NODE_PATH: $file")
  project.layout.dir(provider { file }).get()
}

val nodeModulesDir: Directory by extra {
  val file = nodePath.dir(NpmProject.NODE_MODULES)
  logger.lifecycle("Kotlin/JS NODE_MODULES: $file")
  file
}

// build/js/node_modules/ts-proto/node_modules/.bin

tasks.protobufCompile {
  description = "proto2java"
  protoFile.set(layout.projectDirectory.file("src/proto/FactorioServerLogRecord.proto"))
}


tasks.create<ProtobufCompileTask>("protobufTypescript") {
  description = "proto2typescript"

//  data class PluginTs(
//    override val cliParam: String = "--plugin",
//    override val protocOptions: String = "",
//    override val outputDirectoryName: String =
//      "${project.buildDir}/js/packages/factorio-web-map-factorio-events-data-model/node_modules/.bin/protoc-gen-ts_proto.cmd",
//  ) : ProtocOutput
//
//
//  data class OutputTs(
//    override val cliParam: String = "--ts_proto_out",
//    override val protocOptions: String = "",
//    override val outputDirectoryName: String = "${project.buildDir}/pbAndG/generated-sources/typescript",
//  ) : ProtocOutput
//
//  protocOutputs.add(PluginTs())
//  protocOutputs.add(OutputTs())


//  cliArgs.add("--plugin=${rootProject.buildDir}/js/packages/factorio-web-map-factorio-events-data-model/node_modules/.bin/protoc-gen-ts_proto.cmd")

  cliArgs.add("--plugin=protoc-gen-ts_proto=${rootProject.buildDir}/js/packages/factorio-web-map-factorio-events-data-model/node_modules/.bin/protoc-gen-ts_proto.cmd")

  val outdir = temporaryDir.resolve("typescript")
  val f: File = project.mkdir(outdir)

  cliArgs.add("--ts_proto_out=${outdir.canonicalPath}")
  cliArgs.add("--ts_proto_opt=useOptionals=true")
//  cliArgs.add("--proto_path=${rootProject.rootDir}")

  protoFile.set(layout.projectDirectory.file("src/proto/FactorioServerLogRecord.proto"))
}
