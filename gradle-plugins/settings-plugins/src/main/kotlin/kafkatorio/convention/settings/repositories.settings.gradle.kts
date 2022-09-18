package kafkatorio.convention.settings

@Suppress("UnstableApiUsage") // Central declaration of repositories is an incubating feature
dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)

  repositories {
    myMavenLocal()
    mavenCentral()
    jitpack()
    gradlePluginPortal()

    // Declare the Node.js download repository
    nodeDistributions()
    yarnDistributions()

    maven("https://raw.githubusercontent.com/adamko-dev/kotka-streams/artifacts/m2") {
      content { includeGroup("dev.adamko.kotka") }
    }

    kotlinxHtml()

    sonatypeSnapshot()
  }

  pluginManagement {
    repositories {
      myMavenLocal()
      gradlePluginPortal()
      mavenCentral()
      jitpack()
      sonatypeSnapshot()
    }
  }
}
