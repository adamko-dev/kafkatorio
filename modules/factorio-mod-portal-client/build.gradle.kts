import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  dev.adamko.kafkatorio.lang.`kotlin-jvm`
  kotlin("plugin.serialization")
  application
}


dependencies {

  implementation(projects.modules.eventsServer)

  implementation(platform(libs.kotlinx.serialization.bom))
  implementation(libs.kotlinx.serialization.core)
  implementation(libs.kotlinx.serialization.json)

  implementation(libs.kotlinx.coroutines)

  implementation(platform(libs.http4k.bom))
  implementation(libs.bundles.http4k)
  implementation(libs.http4k.format.kotlinx)

  implementation(libs.skrapeit)
}

//
//application {
//  mainClass.set("dev.adamko.kafkatorio.processor.EventProcessorKt")
//}

tasks.withType<KotlinCompile> {
  kotlinOptions.freeCompilerArgs += listOf(
    "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
    "-opt-in=kotlinx.coroutines.FlowPreview",
  )
}
