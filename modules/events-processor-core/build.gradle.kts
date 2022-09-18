import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


plugins {
  id("kafkatorio.conventions.lang.kotlin-jvm")
  kotlin("plugin.serialization")
//  application
}


description = "Shared Kafkatorio processors functionality"

val projectId: String by project.extra

val kafkaStateDir: Directory = layout.projectDirectory.dir(".state/kafka")


dependencies {
  implementation(platform(projects.modules.versionsPlatform))

  implementation(libs.kafka.streams)
  testImplementation(libs.kafka.streamsTestUtils)

  implementation(projects.modules.eventsLibrary)

  implementation(libs.kotlinxSerialization.core)
  implementation(libs.kotlinxSerialization.json)

  implementation(libs.kotlinx.coroutines.core)

  implementation(libs.bundles.logging)

  implementation(libs.scrimage.core)

  implementation(libs.okio.core)

  implementation(libs.simpleSyslogParser)

  implementation(libs.kafka.kotkaStreams)

  implementation(libs.bundles.hoplite)

  implementation(libs.ktorServer.authJwt)
//  implementation(libs.nimbusJoseJwt)
}


//application {
//  mainClass.set("dev.adamko.kafkatorio.processor.admin.MainKt")
//}


tasks.withType<KotlinCompile>().configureEach {
  kotlinOptions.freeCompilerArgs += listOf(
    "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
    "-opt-in=kotlinx.coroutines.FlowPreview",
    "-opt-in=kotlinx.serialization.ExperimentalSerializationApi",
  )
}


val createKafkaTopics by tasks.registering(JavaExec::class) {
  group = rootProject.name
  classpath = sourceSets.main.get().runtimeClasspath
  mainClass.set("dev.adamko.kafkatorio.processor.admin.MainKt")
}
