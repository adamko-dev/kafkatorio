rootProject.name = "factorio-web-map"

include(
  ":modules:mod",
  ":modules:kt-rcon",
  ":modules:factorio-events-data-model",
  ":modules:factorio-events-processor",
  ":modules:factorio-events-kafka-pipe",
)

enableFeaturePreview("VERSION_CATALOGS")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

apply(from = "./buildSrc/repositories.settings.gradle.kts")
