import dev.adamko.kafkatorio.task.DownloadCLibTask

plugins {
  dev.adamko.kafkatorio.lang.`kotlin-multiplatform`
  kotlin("plugin.serialization")
}


kotlin {
  val hostOs = System.getProperty("os.name")
  val nativeTarget = when {
    hostOs == "Mac OS X"         -> macosX64("native")
    hostOs == "Linux"            -> linuxX64("native")
    hostOs.startsWith("Windows") -> mingwX64("native")
    else                         -> throw GradleException("Host OS is not supported in Kotlin/Native.")
  }

  nativeTarget.apply {
//  linuxX64("native").apply {
    compilations.getByName("main") {
      cinterops {
//        val libkcat by creating {
//          defFile(layout.projectDirectory.file("src/nativeInterop/cinterop/libkcat.def"))
//          compilerOpts("-I/path")
//          compilerOpts("-I.")
//          includeDirs.allHeaders(
//            "path",
//            "$projectDir/src/nativeInterop/cinterop/librdkafka/",
//            "$projectDir/src/nativeInterop/cinterop/librdkafka/src",
//            "$projectDir/src/nativeInterop/cinterop/kcat",
//            "$projectDir/src/nativeInterop/cinterop/kcat/win32",
////            "$projectDir/src/nativeInterop/cinterop/librdkafka/librdkafka/",
//          )
//
////          extraOpts(
////            "-libraryPath",
////            layout.projectDirectory.file("src/nativeInterop/cinterop/")
////          )
//          extraOpts(
//            "-verbose",
//            "-libraryPath", "$projectDir/src/nativeInterop/cinterop/librdkafka/",
//            "-libraryPath", "$projectDir/src/nativeInterop/cinterop/kcat",
//            "-libraryPath", "$projectDir/src/nativeInterop/cinterop/librdkafka/librdkafka/",
//            "-libraryPath", "$projectDir/src/nativeInterop/cinterop/kcat/win32",
//          )
//        }
        val libkafka by creating {
          defFile(layout.projectDirectory.file("src/nativeInterop/cinterop/libkafka.def"))
          compilerOpts("-I/path")
          compilerOpts("-I.")
          includeDirs.allHeaders(
            "path",
            "$projectDir/src/nativeInterop/cinterop/librdkafka/",
            "$projectDir/src/nativeInterop/cinterop/librdkafka/src",
//            "$projectDir/src/nativeInterop/cinterop/kcat",
//            "$projectDir/src/nativeInterop/cinterop/kcat/win32",
////            "$projectDir/src/nativeInterop/cinterop/librdkafka/librdkafka/",
          )

//          extraOpts(
//            "-libraryPath",
//            layout.projectDirectory.file("src/nativeInterop/cinterop/")
//          )
          extraOpts(
            "-verbose",
            "-libraryPath", "$projectDir/src/nativeInterop/cinterop/librdkafka/",
            "-libraryPath", "$projectDir/src/nativeInterop/cinterop/librdkafka/src",
//            "-libraryPath", "$projectDir/src/nativeInterop/cinterop/kcat",
//            "-libraryPath", "$projectDir/src/nativeInterop/cinterop/librdkafka/librdkafka/",
//            "-libraryPath", "$projectDir/src/nativeInterop/cinterop/kcat/win32",
          )
        }
      }
    }
    binaries {
      executable {
        entryPoint = "dev.adamko.dokalo.main"
      }
    }
  }



  sourceSets {
    val nativeMain by getting {
      dependencies {

      }
    }
    val nativeTest by getting
  }

}


//val kcatLibDownload by tasks.registering {
//  group = "interop setup"
//
//  val kcatVersion = "1.7.0"
//  val target = uri("https://github.com/edenhill/kcat/archive/refs/tags/$kcatVersion.zip")
//  inputs.property("target", target)
//
//  val downloadedFile = file("$temporaryDir/kcat.zip")
//  outputs.file(downloadedFile)
//
//  doLast {
//    ant.invokeMethod(
//      "get", mapOf(
//        "src" to target,
//        "dest" to downloadedFile,
//        "verbose" to true,
//      )
//    )
//    logger.lifecycle("Downloaded kcat")
//  }
//}


val kcatLibInstall by tasks.registering(DownloadCLibTask::class) {
  group = "interop setup"

  val kcatVersion = "1.7.0"
  sourceUrl.set("https://github.com/edenhill/kcat/archive/refs/tags/$kcatVersion.zip")
  output.set(layout.projectDirectory.dir("src/nativeInterop/cinterop/kcat"))
}

val librdkafkaLibInstall by tasks.registering(DownloadCLibTask::class) {
  group = "interop setup"

  val librdkafkaVersion = "v1.6.2"
  sourceUrl.set("https://github.com/edenhill/librdkafka/archive/refs/tags/$librdkafkaVersion.zip")
  output.set(layout.projectDirectory.dir("src/nativeInterop/cinterop/librdkafka"))
}

tasks.assemble {
  dependsOn(
    kcatLibInstall,
    librdkafkaLibInstall,
  )
}

//dependencies {
//  implementation(platform(projects.modules.versionsPlatform))
//
////  implementation(gradleKotlinDsl())
//
//  implementation(libs.kotlinx.serialization.core)
//  implementation(libs.kotlinx.serialization.json)
//
//  implementation(libs.kotlinx.cli)
//
//  implementation(libs.kotlinx.coroutines.core)
//
//  implementation(libs.okio.core)
//
//  implementation(libs.ktor.serverCore)
//  implementation(libs.ktor.serverNetty)
//  implementation(libs.ktor.serverContentNegotiaion)
//  implementation(libs.ktor.serializationKotlinxJson)
//
//  testImplementation(libs.ktor.test.serverHost)
//}

//val libcurl by creating {
//  defFile(project.file("src/nativeInterop/cinterop/libcurl.def"))
//  packageName("com.jetbrains.handson.http")
//  compilerOpts("-I/path")
//  includeDirs.allHeaders("path")
//}
