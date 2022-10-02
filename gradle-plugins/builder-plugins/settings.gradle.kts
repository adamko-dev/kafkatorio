rootProject.name = "builder-plugins"

pluginManagement {
  @Suppress("UnstableApiUsage")
  includeBuild("../settings-plugins/")
}

plugins {
  id("kafkatorio.conventions.settings.repositories")
}

dependencyResolutionManagement {
  versionCatalogs {
    create("libs") {
      from(files("../../gradle/libs.versions.toml"))
    }
  }
}

include(
  ":modules:common-lib",

  ":modules:build-conventions",

  ":modules:docker-compose",
  ":modules:factorio-mod",
  ":modules:typescript",
)


enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
