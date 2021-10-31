plugins {
  id("dev.adamko.factoriowebmap.archetype.kotlin-jvm")
  id("dev.adamko.gradle.pbandg.pb-and-g")
  `kotlin-dsl`
}

val projectId: String by project.extra
val buildDir: Directory = layout.buildDirectory.dir(projectId).get()


dependencies {
  protobufLibrary("com.google.protobuf:protobuf-javalite:3.19.1")

  api("com.google.protobuf:protobuf-javalite:3.19.1")
  api("com.google.protobuf:protobuf-kotlin-lite:3.19.1")
}

tasks.protobufCompile {
  protoFile.set(layout.projectDirectory.file("src/main/proto/FactorioServerLogRecord.proto"))
}

sourceSets {

}
