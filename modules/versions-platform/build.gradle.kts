plugins {
  `java-platform`
}


javaPlatform {
  allowDependencies()
}


dependencies {
  api(platform(libs.kotlin.bom))
  api(platform(libs.kotlinx.coroutines.bom))
  api(platform(libs.kotlinx.serialization.bom))

  api(platform(libs.http4k.bom))

  api(platform(libs.okio.bom))

  api(platform(libs.ktor.bom))

  api(platform(libs.kotest.bom))

  constraints {
    api(libs.skrapeit)
    api(libs.kxs.charleskornKaml)
    api(libs.kxs.mamoeYamlkt)
    api(libs.kafka.kotkaStreams)
    api(libs.kafka.streams)
    api(libs.okio.core)
    api(libs.scrimage.core)
    api(libs.kafka.streamsTestUtils)

    api(libs.mockk)
  }
}
