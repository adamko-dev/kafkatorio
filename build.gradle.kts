plugins {
  idea
  base

//  val kotlinVersion = "1.5.31"
//  kotlin("jvm") version kotlinVersion apply false
//  kotlin("js") version kotlinVersion apply false
//  kotlin("multiplatform") version kotlinVersion apply false

//  id("com.github.node-gradle.node") version "3.1.1" apply false
}

group = "dev.adamko.factoriowebmap"
version = "0.0.3"

tasks.wrapper {
  gradleVersion = "7.2"
  distributionType = Wrapper.DistributionType.ALL
}

idea {
  module {
    isDownloadSources = true
    isDownloadJavadoc = true
  }
}
