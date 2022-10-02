import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("kafkatorio.conventions.kotlin-dsl")
//  kotlin("jvm") version embeddedKotlinVersion
//  `kotlin-dsl`
  `java-gradle-plugin`
}

val gradleJvmTarget = "11"

dependencies {
  implementation(platform(libs.kotlin.bom))
  implementation("org.jetbrains.kotlin:kotlin-compiler-embeddable")
}

//kotlin {
//  jvmToolchain {
//    (this as JavaToolchainSpec).languageVersion.set(JavaLanguageVersion.of(gradleJvmTarget))
//  }
//}
//
//tasks.withType<KotlinCompile>().configureEach {
//  kotlinOptions {
//    jvmTarget = gradleJvmTarget
//  }
//}

gradlePlugin {
  plugins {
    create("geedeecee") {
      id = "dev.adamko.geedeecee"
      displayName = "geedeecee - Gradle Docker Compose"
      implementationClass = "dev.adamko.geedeecee.GDCPlugin"
    }
  }
}
