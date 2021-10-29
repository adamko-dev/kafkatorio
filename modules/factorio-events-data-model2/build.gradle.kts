//import com.google.protobuf.gradle.builtins
//import com.google.protobuf.gradle.generateProtoTasks
//import com.google.protobuf.gradle.ofSourceSet
//import com.google.protobuf.gradle.plugins
//import com.google.protobuf.gradle.protobuf
//import com.google.protobuf.gradle.protoc


plugins {
  id("dev.adamko.factoriowebmap.archetype.base")
//  id("dev-adamko.pb-and-g")
//  kotlin("multiplatform")

//  id("dev.adamko.factoriowebmap.archetype.kotlin-jvm")
//  java
//  kotlin("js")

//  id("com.google.protobuf") version "0.8.17"

}

val projectId: String by project.extra
val buildDir: Directory = layout.buildDirectory.dir(projectId).get()
//val nodeModulesDir: Directory by project.extra

val protobufVersion = "3.19.0"

//kotlin {
////  js {
////    // To build distributions for and run tests on browser or Node.js use one or both of:
//////    browser()
////    nodejs()
////  }
//}


dependencies {
//  implementation(platform("com.google.protobuf:protobuf-bom:$protobufVersion"))
//
//  implementation("com.google.protobuf:protobuf-java:$protobufVersion")
//  implementation("com.google.protobuf:protobuf-javalite:$protobufVersion")
//  implementation("com.google.protobuf:protobuf-kotlin-lite:$protobufVersion")
//  implementation("com.google.protobuf:protobuf-kotlin:$protobufVersion")
//  implementation("com.google.protobuf:protoc:$protobufVersion")
//
//  implementation(platform("io.grpc:grpc-bom:1.41.0"))
//  implementation("io.grpc:grpc-stub:1.41.0")
//  implementation("io.grpc:grpc-protobuf:1.41.0")
//
//  implementation("io.grpc:grpc-kotlin-stub:1.2.0")
//  implementation("io.grpc:protoc-gen-grpc-kotlin:1.2.0")
//
//  implementation("javax.annotation:javax.annotation-api:1.3.2")
//

//  implementation(npm("protoc-gen-ts", "0.15.0"))

}


//protobuf {
//  protoc {
//    // The artifact spec for the Protobuf Compiler
//    artifact = "com.google.protobuf:protoc:3.9.2"
//  }
//
//  plugins {
//
//    this.id("ts") {
//
//    }
//  }
//
//  this.plugins {
////    this.named("ts") {
////
////    }
//  }
//
//  generateProtoTasks {
//    ofSourceSet("main").forEach { task ->
//      task.builtins {
//        getByName("java") {
//          option("lite")
//        }
////        getByName("kotlin") {
////          option("lite")
////        }
////        getByName("ts") {
////          option("lite")
////        }
//      }
//    }
//  }
//}
