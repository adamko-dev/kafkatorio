import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.createDirectories
import kotlin.io.path.deleteRecursively
import kotlin.io.path.writeText

plugins {
  id("kafkatorio.conventions.kotlin-dsl")
}

dependencies {
  implementation(platform(libs.kotlin.bom))
  implementation("org.jetbrains.kotlin:kotlin-serialization")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.jetbrains.kotlin:kotlin-compiler-embeddable")
  implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${libs.versions.kotlin.get()}")

  implementation(platform(libs.kotlinx.coroutines.bom))
  implementation(libs.kotlinx.coroutines.core)

  implementation(platform(libs.kotlinxSerialization.bom))
  implementation(libs.kotlinxSerialization.core)
  implementation(libs.kotlinxSerialization.json)

  implementation("com.github.node-gradle:gradle-node-plugin:${libs.versions.gradleNodePlugin.get()}")

  implementation(platform(libs.kotest.bom))
  implementation("io.kotest:kotest-framework-multiplatform-plugin-gradle")

  implementation(libs.gradlePlugin.kvision)

  implementation(libs.semver)

  implementation(platform(libs.okio.bom))
  implementation(libs.okio.core)

  implementation(libs.kotlinx.cli)
}


kotlin {
  compilerOptions {
    optIn.addAll(
      "kotlin.ExperimentalStdlibApi",
      "kotlin.time.ExperimentalTime",
      "kotlinx.serialization.ExperimentalSerializationApi",
    )
  }
}

val generateBuildVersions by tasks.registering {
  val nodeVersion = libs.versions.node

  inputs.property("nodeVersion", nodeVersion)

  //val buildVersionsKt = layout.buildDirectory.file("generated/BuildVersions.kt")
  val outputDir = temporaryDir.toPath()
  outputs.dir(outputDir.toFile())

  @OptIn(ExperimentalPathApi::class)
  doLast {
    outputDir.apply {
      deleteRecursively()
      createDirectories()
      resolve("BuildVersions.kt").writeText(
        """
        package kafkatorio.conventions
        
        internal object BuildVersions {
          const val node = "${nodeVersion.get()}"
        }
        """.trimIndent()
      )
    }
  }
}

kotlin.sourceSets.main {
  kotlin.srcDir(generateBuildVersions)
}
