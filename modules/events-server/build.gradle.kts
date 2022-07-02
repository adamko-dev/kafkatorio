import dev.adamko.kafkatorio.task.KafkaConsumerGroupsTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


plugins {
  dev.adamko.kafkatorio.lang.`kotlin-jvm`
  kotlin("plugin.serialization")
  application
}


description = "serves Kafkatorio content over the web"

val projectId: String by project.extra

val kafkaStateDir: Directory = layout.projectDirectory.dir("kafka-state")


dependencies {
  implementation(platform(projects.modules.versionsPlatform))

  implementation(libs.kafka.streams)
  testImplementation(libs.kafka.streamsTestUtils)

  implementation(projects.modules.eventsLibrary)

  implementation(libs.kotlinx.serialization.core)
  implementation(libs.kotlinx.serialization.json)
//  implementation("org.jetbrains.kotlinx:kotlinx-serialization-protobuf")

  implementation(libs.kotlinx.coroutines.core)

  implementation(libs.bundles.logging)

  implementation(libs.scrimage.core)

  implementation(libs.okio.core)

  //<editor-fold desc="Ktor">
  implementation(libs.ktorSerialization.kotlinxJson)

  with(libs.ktorServer) {
    implementation(core)
    implementation(callId)
    implementation(callLogging)
    implementation(contentNegotiaion)
    implementation(cio)
//    implementation(netty)
    implementation(autoHeadResponse)
    implementation(partialContent)
    implementation(resources)
    implementation(statusPages)
    implementation(websockets)
    implementation(compression)
  }

  with(libs.ktorNetwork) {
    implementation(core)
    implementation(tls)
  }

  //</editor-fold>


  implementation(libs.simpleSyslogParser)

  implementation(libs.kafka.kotkaStreams) {
    isChanging = false
  }

  implementation(libs.bundles.hoplite)
}


application {
  mainClass.set("dev.adamko.kafkatorio.server.EventsServerKt")
}


tasks.withType<KotlinCompile>().configureEach {
  kotlinOptions.freeCompilerArgs += listOf(
    "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
    "-opt-in=kotlinx.coroutines.FlowPreview",
    "-opt-in=kotlinx.serialization.ExperimentalSerializationApi",
  )
}


val kafkaStateDirDelete by tasks.registering(Delete::class) {
  group = project.name
  mustRunAfter(kafkaConsumersDelete)
  delete(kafkaStateDir)
}


val kafkaConsumersDelete by tasks.registering(KafkaConsumerGroupsTask.DeleteAll::class) {
  group = project.name
}


idea {
  module {
    excludeDirs = excludeDirs + listOf(
      kafkaStateDir.asFile,
      file("server-data"),
    )
  }
}
