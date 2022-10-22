import kafkatorio.extensions.dropDirectory
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("kafkatorio.conventions.lang.kotlin-jvm")
  id("dev.adamko.geedeecee")
  kotlin("plugin.serialization")
  application
}


description = "processors for Kafkatorio packets"

val projectId: String by project.extra


dependencies {
  implementation(platform(projects.modules.versionsPlatform))

  implementation(libs.kafka.streams)

  testImplementation(libs.kafka.streamsTestUtils)

  implementation(projects.modules.eventsLibrary)
  implementation(projects.modules.eventsProcessorCore)

  implementation(libs.kotlinxSerialization.core)
  implementation(libs.kotlinxSerialization.json)

  implementation(libs.kotlinx.coroutines.core)

  implementation(libs.bundles.logging)

  implementation(libs.scrimage.core)

  implementation(libs.okio.core)

  implementation(libs.simpleSyslogParser)

  implementation(libs.kafka.kotkaStreams)
}


application {
  mainClass.set("dev.adamko.kafkatorio.processors.MainKt")
}


tasks.withType<KotlinCompile>().configureEach {
  kotlinOptions.freeCompilerArgs += listOf(
    "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
    "-opt-in=kotlinx.coroutines.FlowPreview",
    "-opt-in=kotlinx.serialization.ExperimentalSerializationApi",
  )
}


//val startScriptEntitiesProcessor = registerStartScriptTask("EntitiesProcessor") {
//  mainClass.set("dev.adamko.kafkatorio.processors.EntitiesProcessorKt")
//}
//
//val startScriptPacketsProcessor = registerStartScriptTask("PacketsProcessor") {
//  mainClass.set("dev.adamko.kafkatorio.processors.PacketsProcessorKt")
//}
//
//val startScriptTilesProcessor = registerStartScriptTask("TilesProcessor") {
//  mainClass.set("dev.adamko.kafkatorio.processors.TilesProcessorKt")
//}
//
//
//application.applicationDistribution.into("bin") {
//  from(startScriptEntitiesProcessor)
//  from(startScriptPacketsProcessor)
//  from(startScriptTilesProcessor)
//  fileMode = 0b000_111_101_101 // 0755
//  duplicatesStrategy = DuplicatesStrategy.EXCLUDE
//}

tasks.named("run") {
  val kafkaStateDir: Directory = layout.projectDirectory.dir(".state/kafka/")

  doFirst {
    kafkaStateDir.asFile.mkdirs()
  }
}


val runEventsProcessors by tasks.registering {
  group = rootProject.name

  dependsOn(tasks.run)
}


tasks.dockerContextPrepareFiles {
  from(zipTree(tasks.distZip.flatMap { it.archiveFile })) {
    eachFile {
      relativePath = relativePath.dropDirectory()

      if (!isDirectory && relativePath.pathString.matches("""bin\/.+""".toRegex())) {
        name = name.replace(file.nameWithoutExtension, "run")
      }
    }
  }
}
