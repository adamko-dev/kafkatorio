plugins {
  idea
  base
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
