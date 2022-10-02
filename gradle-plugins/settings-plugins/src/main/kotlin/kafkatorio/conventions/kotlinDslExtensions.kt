package kafkatorio.conventions

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.jvm.toolchain.JavaToolchainSpec
import org.gradle.kotlin.dsl.plugins.dsl.*
import org.gradle.kotlin.dsl.withType

fun Project.overrideKotlinLanguageVersion(
  gradleKotlinTarget: String = "1.6"
) {
  afterEvaluate {
    tasks.withType<KotlinCompile>().configureEach {
      kotlinOptions {
        apiVersion = gradleKotlinTarget
        languageVersion = gradleKotlinTarget
      }
    }
  }
}
