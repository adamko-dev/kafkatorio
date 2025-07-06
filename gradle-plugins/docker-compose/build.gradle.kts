plugins {
  id("kafkatorio.conventions.kotlin-dsl")
}

dependencies {
  implementation(platform(libs.okio.bom))
  implementation(libs.okio.core)
}

gradlePlugin {
  plugins {
    create("geedeecee") {
      id = "dev.adamko.geedeecee"
      displayName = "geedeecee - Gradle Docker Compose"
      implementationClass = "dev.adamko.geedeecee.GDCPlugin"
    }
  }
}
