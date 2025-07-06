import java.time.LocalDate
import kafkatorio.extensions.excludeProjectConfigurationDirs
import kafkatorio.extensions.initIdeProjectLogo

plugins {
  idea
  id("kafkatorio.conventions.base")
}

group = "dev.adamko.kafkatorio"
version = "0.10.1"

val licenseFile: RegularFile by extra(layout.projectDirectory.file("LICENSE"))

val projectTokens: MapProperty<String, String> by extra {
  objects.mapProperty<String, String>().apply {
    put("project.version", provider { "${project.version}" })
    put("rootProject.name", provider { rootProject.name })
    put("date.year", provider { "${LocalDate.now().year}" })
    put("author.email", "adam@adamko.dev")
  }
}

excludeProjectConfigurationDirs(
  idea,
  dirsToExclude = setOf(
    "gradle/kotlin-js-store",
    "gradle/wrapper",
    ".idea",
    ".gradle",
    "build",
  )
)

val runKafkatorio by tasks.registering {
  group = rootProject.name

  dependsOn(
    ":modules:infra-factorio-client:processRestart",
    ":modules:infra-factorio-server:runFactorioServer",
    ":modules:infra-kafka-pipe:dockerComposeUp",
  )
}

tasks.prepareKotlinBuildScriptModel {
  initIdeProjectLogo("docs/media/img/kafkatorio-logo.svg")
}
