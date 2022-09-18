@file:Suppress("UnstableApiUsage")

rootProject.name = "factorio-mod-manager"

pluginManagement {
  @Suppress("UnstableApiUsage")
  includeBuild("../settings-plugins/")
}

plugins {
  id("kafkatorio.convention.settings.repositories")
}
