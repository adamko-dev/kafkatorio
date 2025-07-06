import org.gradle.api.initialization.resolve.RepositoriesMode.PREFER_SETTINGS

rootProject.name = "kafkatorio"

pluginManagement {
  includeBuild("./gradle-plugins/settings-plugins/")
  includeBuild("./gradle-plugins/builder-plugins/")
  includeBuild("./gradle-plugins/docker-compose/")
  includeBuild("./gradle-plugins/factorio-gradle/")
}


plugins {
  id("kafkatorio.conventions.settings.repositories")
}


include(
  ":modules:events-library",

  ":modules:events-mod",

  ":modules:events-processor-core",
//  ":modules:events-processor-entities",
  ":modules:events-processors",
//  ":modules:events-processor-tiles",


//  ":modules:events-server",
//  ":modules:events-server-core",
  ":modules:events-server-web",
  ":modules:events-server-syslog",

  ":modules:infra-factorio-client",
  ":modules:infra-factorio-server",

  ":modules:infra-kafka-cluster",
  ":modules:infra-kafka-pipe",

  ":modules:kt-rcon",
  ":modules:web-map",

//  ":modules:factorio-mod-portal-client",

  ":modules:versions-platform",
)


enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("STABLE_CONFIGURATION_CACHE")


dependencyResolutionManagement {
  @Suppress("UnstableApiUsage")
  repositoriesMode = PREFER_SETTINGS
}
