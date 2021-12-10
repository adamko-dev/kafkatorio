rootProject.name = "kafkatorio"

include(

  ":modules:events-mod",
  ":modules:events-processor",
  ":modules:events-schema",

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
