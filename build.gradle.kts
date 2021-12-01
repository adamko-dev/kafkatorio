plugins {
  idea
  base
  `project-report`
}

group = "dev.adamko.factoriowebmap"
version = "0.0.7"

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

tasks.withType(HtmlDependencyReportTask::class).configureEach {
  projects = project.allprojects
}
