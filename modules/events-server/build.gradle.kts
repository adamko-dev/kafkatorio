import dev.adamko.kafkatorio.task.KafkaConsumerGroupsTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  dev.adamko.kafkatorio.lang.`kotlin-jvm`
  kotlin("plugin.serialization")
  application
}

description = """
    Receive raw Factorio Events from Kafka and process them into targeted topics or data formats. 
    
    Factorio events -> Mod -> Kafka -> ***Factorio Events Processor*** -> processed events
  """.trimIndent()

val projectId: String by project.extra

val kafkaStateDir: Directory = layout.projectDirectory.dir("kafka-state")

dependencies {
  implementation(platform(projects.modules.versionsPlatform))

  implementation(libs.kafka.streams)
  testImplementation(libs.kafka.streamsTestUtils)

  implementation(projects.modules.eventsSchema)

//  implementation("com.ionspin.kotlin:bignum:0.3.3")

  implementation(libs.kotlinx.serialization.core)
  implementation(libs.kotlinx.serialization.json)
//  implementation("org.jetbrains.kotlinx:kotlinx-serialization-protobuf")

  implementation(libs.kotlinx.coroutines.core)

  implementation(libs.bundles.http4k)
  implementation(libs.http4k.format.kotlinx)

  implementation(libs.bundles.logging)

  implementation(libs.scrimage.core)

  implementation(libs.okio.core)

  implementation(libs.kafka.kotkaStreams) {
    isChanging = true
  }

  implementation(libs.bundles.hoplite)
}

//configurations.all {
//  resolutionStrategy.cacheDynamicVersionsFor(1, TimeUnit.MINUTES)
//}

application {
  mainClass.set("dev.adamko.kafkatorio.processor.EventProcessorKt")

//  applicationDefaultJvmArgs += listOf(
//    "-Dcom.sun.management.jmxremote.port=9186",
//    "-Dcom.sun.management.jmxremote.rmi.port=9186",
//    "-Dcom.sun.management.jmxremote.ssl=false",
//    "-Dcom.sun.management.jmxremote.local.only=false",
//    "-Dcom.sun.management.jmxremote.authenticate=false",
//  )
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

//tasks.run.configure {
//  dependsOn(kafkatorioEventsServerKafkaForceReset)
//}

idea {
  module {
    excludeDirs = excludeDirs + kafkaStateDir.asFile
  }
}

//tasks.withType<KotlinNpmInstallTask>().all {
//  notCompatibleWithConfigurationCache("error: Projects must be configuring")
//}
