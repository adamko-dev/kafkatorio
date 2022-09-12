@file:Suppress("PackageDirectoryMismatch")

package org.gradle.kotlin.dsl

import org.gradle.api.artifacts.dsl.RepositoryHandler


fun RepositoryHandler.jitpack() {
  maven("https://jitpack.io")
}


fun RepositoryHandler.myMavenLocal(enabled: Boolean = true) {
  if (enabled) {
    println("Maven local is enabled")
    mavenLocal {
      content {
//        includeGroup("dev.adamko")
//        includeGroup("dev.adamko.kxtsgen")
        includeGroup("io.kotest")
      }
    }
  }
}


fun RepositoryHandler.sonatypeSnapshot() {
  maven("https://oss.sonatype.org/content/repositories/snapshots") {
    mavenContent {
      snapshotsOnly()
      includeGroup("io.kotest")
    }
  }
  maven("https://s01.oss.sonatype.org/content/repositories/snapshots/") {
    mavenContent {
      snapshotsOnly()
      includeGroup("io.kotest")
    }
  }
}


fun RepositoryHandler.nodeDistributions() {
  // Declare the Node.js download repository
  ivy("https://nodejs.org/dist/") {
    name = "Node Distributions at $url"
    patternLayout { artifact("v[revision]/[artifact](-v[revision]-[classifier]).[ext]") }
    metadataSources { artifact() }
    content { includeModule("org.nodejs", "node") }
  }
}


fun RepositoryHandler.yarnDistributions() {
  ivy("https://github.com/yarnpkg/yarn/releases/download") {
    name = "Yarn Distributions at $url"
    patternLayout { artifact("v[revision]/[artifact](-v[revision]).[ext]") }
    metadataSources { artifact() }
    content { includeModule("com.yarnpkg", "yarn") }
  }
}


// https://stackoverflow.com/a/34327202/4161471
// example: implementation("jmeter-gradle-plugin:jmeter-gradle-plugin:1.0.3@zip")
fun RepositoryHandler.gitHub() {
  ivy("https://github.com/") {
    patternLayout {
      artifact("/[organisation]/[module]/archive/[revision].[ext]")
    }
    metadataSources { artifact() }

    content {
      includeGroup("jmeter-gradle-plugin")
    }
  }
}
