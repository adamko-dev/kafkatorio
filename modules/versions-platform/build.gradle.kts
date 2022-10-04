plugins {
  id("kafkatorio.conventions.base")
  `java-platform`
}


javaPlatform {
  allowDependencies()
}


dependencies {
  api(platform(libs.kotlin.bom))
  api(platform(libs.kotlinx.coroutines.bom))
  api(platform(libs.kotlinxSerialization.bom))
  api(platform(libs.kotlin.jsWrappers.bom))


  api(platform(libs.http4k.bom))

  api(platform(libs.okio.bom))

  api(platform(libs.ktor.bom))

  api(platform(libs.kotest.bom))

  constraints {
    api(libs.kafka.kotkaStreams)
    api(libs.kafka.streams)
    api(libs.kafka.streamsTestUtils)

    api(libs.kotlinxSerialization.charleskornKaml)
    api(libs.kotlinxSerialization.mamoeYamlkt)
    api(libs.kotlinxSerialization.kxsTsGen)

    api(libs.okio.core)

    api(libs.scrimage.core)

    api(libs.skrapeit)

    api(libs.mockk)
  }
}
