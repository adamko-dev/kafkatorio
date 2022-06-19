plugins {
  dev.adamko.kafkatorio.lang.`kotlin-jvm`
  kotlin("plugin.serialization")
}


dependencies {
  implementation(platform(projects.modules.versionsPlatform))

//  implementation(gradleKotlinDsl())

  implementation(libs.kotlinx.serialization.core)
  implementation(libs.kotlinx.serialization.json)
  implementation(libs.kotlinx.serialization.protobuf)

  implementation(libs.kotlinx.cli)

  implementation(libs.kotlinx.coroutines.core)

  implementation(libs.okio.core)

  implementation(libs.ktor.serverCore)
  implementation(libs.ktor.serverNetty)
  implementation(libs.ktor.serverContentNegotiaion)
  implementation(libs.ktor.serializationKotlinxJson)
  implementation(libs.ktor.serverResources)

  implementation(libs.kafka.streams)
  implementation(libs.kafka.kotkaStreams)

  implementation(libs.kotlinx.datetime)

  testImplementation(libs.ktor.test.serverHost)
  testImplementation(libs.kafka.streamsTestUtils)
}
