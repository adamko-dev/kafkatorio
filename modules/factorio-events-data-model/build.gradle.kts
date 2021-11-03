import dev.adamko.gradle.pbandg.task.ProtobufCompileTask
import org.jetbrains.kotlin.gradle.targets.js.npm.NpmProject
import org.jetbrains.kotlin.gradle.targets.js.npm.tasks.RootPackageJsonTask

plugins {
  id("dev.adamko.factoriowebmap.archetype.base")
  kotlin("multiplatform")

  id("dev.adamko.gradle.pbandg.pb-and-g")
}

val projectId: String by project.extra
val buildDir: Directory = layout.buildDirectory.dir(projectId).get()

kotlin {

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
    browser()
  }

  sourceSets {
    val jsMain by getting {
      dependencies {
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
}

dependencies {
  protobufLibrary("com.google.protobuf:protobuf-javalite:3.19.1")
}

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

afterEvaluate {
  val protobufJava by tasks.creating(ProtobufCompileTask::class) {
    description = "proto2java"
    protoFile.set(file("$projectDir/src/proto/FactorioServerLogRecord.proto"))

    cliArgs.add("--java_out=lite:${project.mkdir("$temporaryDir/java").canonicalPath}")
  }
  val protobufKotlin by tasks.creating(ProtobufCompileTask::class) {
    description = "proto2kotlin"
    protoFile.set(file("$projectDir/src/proto/FactorioServerLogRecord.proto"))

    cliArgs.add("--kotlin_out=lite:${project.mkdir("$temporaryDir/kotlin").canonicalPath}")
  }


  val ktProto by configurations.creating {
    isCanBeResolved = false
    isCanBeConsumed = true
  }

  artifacts {
    add(ktProto.name, protobufJava.outputDir) {
      builtBy(protobufJava)
    }
    add(ktProto.name, protobufKotlin.outputDir) {
      builtBy(protobufKotlin)
    }
  }

  val protobufTypescript by tasks.creating(ProtobufCompileTask::class) {
    description = "proto2typescript"

    // linux:
//  cliArgs.add("--plugin=${rootProject.buildDir}/js/packages/factorio-web-map-factorio-events-data-model/node_modules/.bin/protoc-gen-ts_proto.cmd")
    // windows:
    cliArgs.add("--plugin=protoc-gen-ts_proto=${rootProject.buildDir}/js/packages/factorio-web-map-factorio-events-data-model/node_modules/.bin/protoc-gen-ts_proto.cmd")

    cliArgs.add("--ts_proto_out=${project.mkdir("$temporaryDir/typescript").canonicalPath}")

    cliArgs.addAll(
      "--ts_proto_opt=useOptionals=true",
      "--ts_proto_opt=outputServices=false",
      "--ts_proto_opt=outputJsonMethods=false",
      "--ts_proto_opt=outputEncodeMethods=false",
      "--ts_proto_opt=forceLong=string",
      "--ts_proto_opt=forceLong=number",
      "--ts_proto_opt=esModuleInterop=true",
      "--ts_proto_opt=exportCommonSymbols=false",
//    "--proto_path=${rootProject.rootDir}",
    )

    protoFile.set(file("$projectDir/src/proto/FactorioServerLogRecord.proto"))
  }

  val tsProto by configurations.creating {
    isCanBeResolved = false
    isCanBeConsumed = true
  }
  artifacts {
    add(tsProto.name, protobufTypescript.outputDir) {
      builtBy(protobufTypescript)
    }
  }

}
