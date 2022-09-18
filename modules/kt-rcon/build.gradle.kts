plugins {
  id("kafkatorio.conventions.lang.kotlin-jvm")
}

dependencies {
  implementation(platform(projects.modules.versionsPlatform))

  implementation(libs.kotlinx.coroutines.core)

  implementation("io.ktor:ktor-network")
}
