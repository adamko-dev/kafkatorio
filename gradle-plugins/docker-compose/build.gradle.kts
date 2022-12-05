plugins {
  id("kafkatorio.conventions.kotlin-dsl")
  `java-gradle-plugin`
}

dependencies {
  implementation(platform(libs.kotlin.bom))
  implementation("org.jetbrains.kotlin:kotlin-compiler-embeddable:${libs.versions.kotlin.get()}")
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
