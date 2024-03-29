import kafkatorio.extensions.dropDirectory
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


plugins {
  id("kafkatorio.conventions.lang.kotlin-jvm")
  id("dev.adamko.geedeecee")
  kotlin("plugin.serialization")
  application
}


description = "Provides a Syslog server that receives Kafkatorio packets and forwards them to Kafka"

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

  //<editor-fold desc="Ktor">
  implementation(libs.ktorSerialization.kotlinxJson)

  with(libs.ktorServer) {
    implementation(auth)
    implementation(authJwt)
    implementation(core)
    implementation(callId)
    implementation(callLogging)
    implementation(contentNegotiaion)
    implementation(cio)
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

//  implementation(libs.nimbusJoseJwt)

  implementation(libs.kafka.kotkaStreams)

  implementation(libs.bundles.hoplite)
}


application {
  mainClass.set("dev.adamko.kafkatorio.processor.syslog.SyslogMainKt")
}


tasks.withType<KotlinCompile>().configureEach {
  kotlinOptions.freeCompilerArgs += listOf(
    "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
    "-opt-in=kotlinx.coroutines.FlowPreview",
    "-opt-in=kotlinx.serialization.ExperimentalSerializationApi",
  )
}


val runSyslogServer by tasks.registering {
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
