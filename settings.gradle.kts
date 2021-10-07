
rootProject.name = "factorio-web-map"


enableFeaturePreview("VERSION_CATALOGS")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {

//  repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)

  repositories {
    mavenCentral()
    maven ("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev/")
    maven("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
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
