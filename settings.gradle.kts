rootProject.name = "factorio-web-map"


includeBuild("modules/protoc-gradle-plugin")

include(
  ":modules:mod",
//    ":modules:mod2",
  ":modules:server",
  ":modules:kt-rcon",
  ":modules:factorio-events-data-model",
//  ":modules:factorio-events-data-model2",
  ":modules:factorio-events-processor",
  ":modules:factorio-events-kafka-pipe",
)


enableFeaturePreview("VERSION_CATALOGS")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {

  // https://github.com/gradle/gradle/issues/15754#issuecomment-763614651
  // https://github.com/gradle/gradle/blob/09fcaacc9848f09aa584b4119be9271bee6916e6/build-logic/build-logic-base/settings-plugin/src/main/kotlin/gradlebuild.repositories.settings.gradle.kts#L68

  // Cannot use 'FAIL_ON_PROJECT_REPOS' because
  // - the 'gradle-guides-plugin' adds a repo (which it should not do)
  // - the 'kotlin-gradle-plugin' adds a repo (and removes it afterwards) to download NodeJS
  repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)

  repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev/")
    maven("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")

    // Declare the Node.js download repository
    ivy {
      name = "Node.js"
      setUrl("https://nodejs.org/dist/")
      patternLayout {
        artifact("v[revision]/[artifact](-v[revision]-[classifier]).[ext]")
      }
      metadataSources {
        artifact()
      }
      content {
        includeModule("org.nodejs", "node")
      }
    }
  }

  pluginManagement {

    repositories {
      mavenCentral()
      gradlePluginPortal()
      maven {
        url = uri("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev/")
      }
    }

  }

}
