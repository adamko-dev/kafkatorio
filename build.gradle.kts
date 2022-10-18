import java.time.LocalDate
import kafkatorio.extensions.excludeGeneratedGradleDsl

plugins {
  idea
  base
  id("kafkatorio.conventions.base")
//  `project-report`
//  `build-dashboard`
}

group = "dev.adamko.kafkatorio"
version = "0.9.10"

val licenseFile: RegularFile by extra(layout.projectDirectory.file("LICENSE"))

val projectTokens: MapProperty<String, String> by extra {
  objects.mapProperty<String, String>().apply {
    put("project.version", provider { "${project.version}" })
    put("rootProject.name", provider { rootProject.name })
    put("date.year", provider { "${LocalDate.now().year}" })
    put("author.email", "adam@adamko.dev")
  }
}

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
  gradleVersion = "7.5.1"
  distributionType = Wrapper.DistributionType.ALL
}

val runKafkatorio by tasks.registering {
  group = rootProject.name

  dependsOn(
    ":modules:infra-factorio-client:processRestart",
    ":modules:infra-factorio-server:processRestart",
    ":modules:infra-kafka-pipe:dockerUp",
  )
}

apply(from = "$projectDir/kt52647.gradle.kts")
