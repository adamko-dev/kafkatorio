plugins {
  kotlin("jvm") version "1.5.31"
  `kotlin-dsl`
  `java-gradle-plugin`
  `maven-publish`
}

gradlePlugin {
  plugins {
    create("ProtobufAndGradle") {
      id = "dev.adamko.pb-and-g"
      implementationClass = "dev.adamko.gradle.pbandg.ProtocPluginKt"
      description =
        "Protobuf & Gradle. Wrapper for protoc to generate code from Proto Buffer files."
    }
  }
}

group = "dev.adamko.gradle.pbandg"
version = "0.0.1-SNAPSHOT"


val protocConfiguration: Configuration by configurations.creating {
  // A configuration meant for consumers that need the implementation of this component
  isCanBeResolved = true
  isCanBeConsumed = false
}

val protobufVersion = "3.19.1"
val protocVersion = "3.9.2"

dependencies {
  implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
  implementation(kotlin("stdlib"))

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


}

repositories {
  mavenCentral()
}

