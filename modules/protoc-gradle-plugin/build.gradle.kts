import org.gradle.nativeplatform.platform.internal.ArchitectureInternal
import org.gradle.nativeplatform.platform.internal.DefaultNativePlatform

plugins {
  kotlin("jvm") version "1.5.31"
  `kotlin-dsl`
  `java-gradle-plugin`
}

gradlePlugin {
  plugins {
    create("Protobuf & Gradle") {
      id = "dev-adamko.pb-and-g"
      implementationClass = "dev.adamko.gradle.pbandg.ProtocPluginKt"
      description =
        "Protobuf & Gradle. Wrapper for protoc to generate code from Proto Buffer files."
    }
  }
}


val protocConfiguration: Configuration by configurations.creating {
  // A configuration meant for consumers that need the implementation of this component
  isCanBeResolved = true
  isCanBeConsumed = false
}

val protobufVersion = "3.19.0"
val protocVersion = "3.9.2"

dependencies {
  implementation(platform("com.google.protobuf:protobuf-bom:$protobufVersion"))

  implementation("com.google.protobuf:protobuf-java:$protobufVersion")
  implementation("com.google.protobuf:protobuf-javalite:$protobufVersion")
  implementation("com.google.protobuf:protobuf-kotlin-lite:$protobufVersion")
  implementation("com.google.protobuf:protobuf-kotlin:$protobufVersion")
  implementation("com.google.protobuf:protoc:$protobufVersion")

  implementation(platform("io.grpc:grpc-bom:1.41.0"))
  implementation("io.grpc:grpc-stub:1.41.0")
  implementation("io.grpc:grpc-protobuf:1.41.0")

  implementation("io.grpc:grpc-kotlin-stub:1.2.0")
  implementation("io.grpc:protoc-gen-grpc-kotlin:1.2.0")

  implementation("javax.annotation:javax.annotation-api:1.3.2")
//

//  implementation(npm("protoc-gen-ts", "0.15.0"))

  implementation(
    group = "com.google.protobuf",
    name = "protoc",
    version = "3.9.2",
//  configuration = "",
    classifier = "windows-x86_64",
    ext = "exe",
  )

  protocConfiguration(
    group = "com.google.protobuf",
    name = "protoc",
    version = protocVersion,
//  configuration = "",
    classifier = "windows-x86_64",
    ext = "exe",
  )

//  protocConfiguration("com.google.protobuf:protoc:3.9.2@exe")


}



// https://github.com/protocolbuffers/protobuf/tree/master/protoc-artifacts
// The name of a published protoc artifact is in the format:
//      protoc-<version>-<os>-<arch>.exe
// e.g., protoc-3.6.1-linux-x86_64.exe
// Note that artifacts for Linux/macOS also have the .exe suffix, but they are not windows binaries.
val os: OperatingSystem = DefaultNativePlatform.getCurrentOperatingSystem()
val arch: ArchitectureInternal = DefaultNativePlatform.getCurrentArchitecture()

repositories {
  mavenCentral()

//  ivy {
//    url = project.repositories.mavenCentral().url
//
//    patternLayout {
//
//      // com/google/protobuf/protoc/3.9.2/protoc-3.9.2-linux-aarch_64.exe
//      artifact("[organisation]/[module]/[revision]/[artifact]-[revision]-${os.name}-${arch.name}.[ext]")
//
////      artifact("3rd-party-artifacts/[organisation]/[module]/[revision]/[artifact]-[revision].[ext]")
////      artifact("company-artifacts/[organisation]/[module]/[revision]/[artifact]-[revision].[ext]")
////      ivy("ivy-files/[organisation]/[module]/[revision]/ivy.xml")
////      artifact("[organisation]/[module]/[revision]/[artifact]-[revision].[ext]")
//      setM2compatible(true)
//      metadataSources {
//        artifact()
//      }
//    }
//  }

}

tasks.register<Sync>("protocExecCopy") {
  group = project.name

//  from(protocConfiguration.artifacts)

  from(protocConfiguration.resolve())

  into(project.layout.buildDirectory.dir("protocCopy"))
}

//tasks.register("dlPc") {
//  group = project.name
//
//  val downloadDir = this.temporaryDir
//
//  outputs.dir(downloadDir)
//
//  doLast {
//
////    downloadDir.mkdirs()
//
//    val protocArtifactFileExt = "exe"
//
//    val osFamilyName = DefaultNativePlatform.host().operatingSystem.toFamilyName()
//
//    val arch: String =
//      DefaultNativePlatform.getCurrentArchitecture()
//        .name
//        .replace("-", "_")
//
//    val artifact = "protoc-$protocVersion-${osFamilyName}-${arch}.$protocArtifactFileExt"
//
//    val uri = uri(
//      ArtifactRepositoryContainer.MAVEN_CENTRAL_URL
//          + "com/google/protobuf/protoc/$protocVersion/$artifact"
//    )
//    // actual:
//    // https://repo.maven.apache.org/maven2/com/google/protobuf/protoc/3.9.2/protoc-3.9.2-windows-x86_64.exe
//    // https://repo.maven.apache.org/maven2/com/google/protobuf/protoc/3.9.2/protoc-3.9.2-windows-x86-64.exe
//
//    ant.invokeMethod(
//      "get", mapOf(
//        "src" to uri,
//        "dest" to downloadDir.canonicalPath,
//        "verbose" to true,
//      )
//    )
//  }
//
//}
//
