rootProject.name = "builder-plugins"

pluginManagement {
  @Suppress("UnstableApiUsage")
  includeBuild("../settings-plugins/")
}

plugins {
  id("kafkatorio.convention.settings.repositories")
}
