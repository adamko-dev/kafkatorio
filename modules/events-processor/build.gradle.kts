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
