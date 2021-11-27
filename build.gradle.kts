plugins {
  idea
  base
}

group = "dev.adamko.factoriowebmap"
version = "0.0.4"

tasks.wrapper {
  gradleVersion = "7.3"
  distributionType = Wrapper.DistributionType.ALL
}

idea {
  module {
    isDownloadSources = true
    isDownloadJavadoc = true
  }
}
