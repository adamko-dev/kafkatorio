package kafkatorio.convention.settings

@Suppress("UnstableApiUsage") // Central declaration of repositories is an incubating feature
dependencyResolutionManagement {

  // https://github.com/gradle/gradle/issues/15754#issuecomment-763614651
  // https://github.com/gradle/gradle/blob/09fcaacc9848f09aa584b4119be9271bee6916e6/build-logic/build-logic-base/settings-plugin/src/main/kotlin/gradlebuild.repositories.settings.gradle.kts#L68

  // Cannot use 'FAIL_ON_PROJECT_REPOS' because
  // - the 'gradle-guides-plugin' adds a repo (which it should not do)
  // - the 'kotlin-gradle-plugin' adds a repo (and removes it afterwards) to download NodeJS
//  repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)

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

    maven("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven") {
      content { includeGroup("org.jetbrains.kotlinx") }
    }

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
