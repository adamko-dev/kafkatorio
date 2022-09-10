rootProject.name = "kafkatorio"

include(
  ":modules:events-library",

  ":modules:events-mod",

  ":modules:events-processor-core",
  ":modules:events-processor-entities",
  ":modules:events-processor-packets",
  ":modules:events-processor-tiles",

  ":modules:events-server",
  ":modules:events-server-api",
  ":modules:events-server-core",
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

apply(from = "./buildSrc/repositories.settings.gradle.kts")

@Suppress("UnstableApiUsage") // Central declaration of repositories is an incubating feature
dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
}
