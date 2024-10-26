plugins {
  id("kafkatorio.conventions.kotlin-dsl")
  id("kafkatorio.conventions.kotlin-serialization")
}

dependencies {
  implementation(platform(libs.kotlin.bom))
  implementation("org.jetbrains.kotlin:kotlin-serialization:${libs.versions.kotlin.get()}")
  implementation("org.jetbrains.kotlin:kotlin-reflect:${libs.versions.kotlin.get()}")
  implementation("org.jetbrains.kotlin:kotlin-compiler-embeddable:${libs.versions.kotlin.get()}")
  implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${libs.versions.kotlin.get()}")

  implementation(platform(libs.kotlinx.coroutines.bom))
  implementation(libs.kotlinx.coroutines.core)

  implementation(platform(libs.kotlinxSerialization.bom))
  implementation(libs.kotlinxSerialization.core)
  implementation(libs.kotlinxSerialization.json)
//  implementation("com.charleskorn.kaml:kaml:0.37.0")

  implementation("com.github.node-gradle:gradle-node-plugin:${libs.versions.gradleNodePlugin.get()}")

  implementation(platform(libs.kotest.bom))
  implementation("io.kotest:kotest-framework-multiplatform-plugin-gradle")

  implementation("io.kvision:io.kvision.gradle.plugin:${libs.versions.kvision.get()}")

  implementation("net.swiftzer.semver:semver:${libs.versions.semver.get()}")

  implementation(platform(libs.okio.bom))
  implementation(libs.okio.core)

  implementation(gradleKotlinDsl())

  implementation(libs.kotlinx.cli)

  implementation(platform(libs.ktor.bom))
  implementation(libs.ktorClient.auth)
  implementation(libs.ktorClient.contentNegotiation)
  implementation(libs.ktorClient.core)
  implementation(libs.ktorClient.encoding)
  implementation(libs.ktorClient.logging)
  implementation(libs.ktorClient.cio)
  implementation(libs.ktorClient.resources)
  implementation(libs.ktorSerialization.kotlinxJson)

  // https://github.com/gradle/gradle/issues/15383#issuecomment-779893192
  // https://youtrack.jetbrains.com/issue/IDEA-262280#focus=Comments-27-5397040.0-0
//  implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}

kotlin {
  compilerOptions {
    optIn.addAll(
      "kotlin.ExperimentalStdlibApi",
      "kotlin.time.ExperimentalTime",
      //"kotlinx.coroutines.ExperimentalCoroutinesApi",
      "kotlinx.serialization.ExperimentalSerializationApi",
    )
  }
}
