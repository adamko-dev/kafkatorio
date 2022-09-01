import dev.adamko.kafkatorio.gradle.excludeGeneratedGradleDsl
import java.time.LocalDate

plugins {
  idea
  base
//  `project-report`
//  `build-dashboard`
}

group = "dev.adamko.kafkatorio"
version = "0.9.0"

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

val startInfra by tasks.registering {
  group = project.name

  dependsOn(
//    ":modules:infra-kafka-pipe:processRun",
    runKafkaCluster
//    ":modules:infra-kafka-connect:processRun",
  )
}

val runKafkatorio by tasks.registering {
  group = project.name

  dependsOn(
    ":modules:infra-factorio-client:processRestart",
    ":modules:infra-factorio-server:processRestart",
    ":modules:infra-kafka-pipe:dockerUp",
  )
}

val runEventsServer by tasks.registering {
  group = project.name

  dependsOn(":modules:events-server:run")
}

val runWebMap by tasks.registering {
  group = project.name

  dependsOn(":modules:web-map:browserDevelopmentRun")
}


val runKafkaCluster by tasks.registering {
  group = project.name

  dependsOn(
    ":modules:infra-kafka-cluster:processRun",
  )
}


val runFactorioServer by tasks.registering {
  group = project.name

  dependsOn(
    ":modules:infra-factorio-server:processRun",
  )
}


val runFactorioClient by tasks.registering {
  group = project.name

  dependsOn(
    ":modules:infra-factorio-client:processRun",
  )
}


apply(from = "$projectDir/kt52647.gradle.kts")
