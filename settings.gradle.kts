rootProject.name = "kafkatorio"


@Suppress("UnstableApiUsage")
pluginManagement {
  includeBuild("./gradle-plugins/settings-plugins/")
}


includeBuild("./gradle-plugins/builder-plugins/")
includeBuild("./gradle-plugins/docker-compose/")
includeBuild("./gradle-plugins/factorio-gradle/")


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


//enableFeaturePreview(org.gradle.api.internal.FeaturePreviews.Feature.TYPESAFE_PROJECT_ACCESSORS.name)
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("STABLE_CONFIGURATION_CACHE")


//apply(from = "./buildSrc/repositories.settings.gradle.kts")


@Suppress("UnstableApiUsage") // Central declaration of repositories is an incubating feature
dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
}
