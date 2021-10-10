rootProject.name = "buildSrc"

enableFeaturePreview("VERSION_CATALOGS")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {

  repositories {
    mavenCentral()
    gradlePluginPortal()
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
