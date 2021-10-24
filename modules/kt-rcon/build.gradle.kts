plugins {
  id("dev.adamko.factoriowebmap.archetype.kotlin-jvm")
}

//group = "${rootProject.group}.server"
//version = rootProject.version
//

dependencies {

  implementation(libs.kotlinx.coroutines)

  implementation("io.ktor:ktor-network:1.6.4")

}