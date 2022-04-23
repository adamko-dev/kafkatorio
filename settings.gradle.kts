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

  ":modules:factorio-mod-portal-client",
)

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

apply(from = "./buildSrc/repositories.settings.gradle.kts")

@Suppress("UnstableApiUsage") // Central declaration of repositories is an incubating feature
dependencyResolutionManagement {

  repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)

//  repositories {
//    mavenLocal {
//      content {
//        includeGroup("io.kvision")
//        includeGroup("dev.adamko.kotka")
//      }
//    }
//  }
}
