import dev.adamko.kafkatorio.gradle.excludeGeneratedGradleDsl

plugins {
  idea
  base
  `project-report`
  `build-dashboard`
}

group = "dev.adamko.kafkatorio"
version = "0.4.0"

val licenseFile: RegularFile by extra(layout.projectDirectory.file("LICENSE"))

val projectTokens: MutableMap<String, String> by extra(
  mutableMapOf(
    "project.version" to "$version",
    "rootProject.name" to rootProject.name,
    "date.year" to "${java.time.LocalDate.now().year}",
    "author.email" to "adam@adamko.dev",
  )
)

idea {
  module {
    isDownloadSources = true
    isDownloadJavadoc = true
    excludeGeneratedGradleDsl(layout)
    excludeDirs = excludeDirs + layout.files(
      ".idea",
      "gradle/kotlin-js-store",
      "gradle/wrapper",
    )
  }
}

tasks.wrapper {
  gradleVersion = "7.4.2"
  distributionType = Wrapper.DistributionType.ALL
}

val startInfra by tasks.registering {
  group = project.name

  dependsOn(
    ":modules:infra-kafka-pipe:processRun",
    ":modules:infra-kafka-cluster:processRun",
//    ":modules:infra-kafka-connect:processRun",
  )
}

val runKafkatorio by tasks.registering {
  group = project.name

  dependsOn(
    ":modules:infra-factorio-client:processRestart",
    ":modules:infra-factorio-server:processRestart",
  )
}


val runEventsServer by tasks.registering {
  group = project.name

  dependsOn(
    ":modules:events-server:run",
  )
}
