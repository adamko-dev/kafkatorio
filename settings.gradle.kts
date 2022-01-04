rootProject.name = "kafkatorio"

include(

  ":modules:events-mod",
  ":modules:events-schema",
  ":modules:events-server",

  ":modules:infra-factorio-client",
  ":modules:infra-factorio-server",

  ":modules:infra-kafka-cluster",
  ":modules:infra-kafka-pipe",
  ":modules:infra-kafka-connect",

  ":modules:kt-rcon",
  ":modules:web-map",
)

enableFeaturePreview("VERSION_CATALOGS")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

apply(from = "./buildSrc/repositories.settings.gradle.kts")

@Suppress("UnstableApiUsage") // centralized repositories is incubating
dependencyResolutionManagement {
  repositories {
    mavenLocal {
      content {
        includeGroup("io.kvision")
      }
    }
  }
}
