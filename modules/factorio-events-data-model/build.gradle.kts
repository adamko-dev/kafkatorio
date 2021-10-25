plugins {
//  id("dev.adamko.factoriowebmap.archetype.base")
//  kotlin("multiplatform")

  id("dev.adamko.factoriowebmap.archetype.kotlin-jvm")

  id("com.google.protobuf") version "0.8.17"


}

val projectId: String by project.extra
val buildDir: Directory = layout.buildDirectory.dir(projectId).get()
val nodeModulesDir: Directory by project.extra

//kotlin {
//
//  js(IR) {
//    binaries.executable()
////    browser {
////      commonWebpackConfig {
////        cssSupport.enabled = true
////      }
////    }
//
//    nodejs()
//    useCommonJs()
//
//    compilations["main"].apply {
//      packageJson {
//        customField(
//          "scripts",
//          mapOf(
//            "build" to "tstl",
//            "dev" to "tstl --watch",
//          )
//        )
//      }
//    }
//
//  }
//
//  jvm {
//    compilations.all {
//      kotlinOptions.jvmTarget = "11"
//    }
//    testRuns["test"].executionTask.configure {
//      useJUnitPlatform()
//    }
//  }
//
//}
