import org.jetbrains.kotlin.util.parseSpaceSeparatedArgs

plugins {
//  kotlin("js")
  id("com.github.node-gradle.node")
}

group = "${rootProject.group}.mod"
version = rootProject.version

node {
  download.set(true)
  version.set("14.18.0")
//  version.set("16.10.0")
}
// "@types/node": "ts4.4",
// 16.10.3 = ts4.4, see https://www.npmjs.com/package/@types/node


//repositories {
//  mavenCentral()
//  maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev/")
//  maven("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
//}

//kotlin {
//  js(IR) {
//
//    binaries.executable()
//
//    browser {
//      commonWebpackConfig {
//        cssSupport.enabled = true
//      }
//    }
//
//    useCommonJs()
//    nodejs()
//
//    compilations["main"].apply {
//      packageJson {
//        customField(
//            "scripts",
//            mapOf(
//                "build" to "tstl",
//                "dev" to "tstl --watch",
//            )
//        )
//      }
//    }
//  }
//
//  sourceSets {
//    val main by getting {
////      kotlin.srcDir("externals02")
////      kotlin.srcDir("externals")
//
//      dependencies {
//
//        val kotlinWrappersVersion = "0.0.1-pre.254-kotlin-1.5.31"
//        implementation(
//            project.dependencies.enforcedPlatform(
//                "org.jetbrains.kotlin-wrappers:kotlin-wrappers-bom:${kotlinWrappersVersion}"
//            )
//        )
//
//        implementation("org.jetbrains.kotlin-wrappers:kotlin-react")
//        implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom")
//        implementation(npm("react", "17.0.2"))
//        implementation(npm("react-dom", "17.0.2"))
//
////        implementation("org.jetbrains.kotlinx:kotlinx-nodejs:0.0.7") {
////          because("https://github.com/Kotlin/kotlinx-nodejs")
////        }
//
//        val generateExternals = false
//        implementation(npm("typescript-to-lua", "1.0.1", generateExternals = generateExternals))
//        implementation(npm("typed-factorio", "0.7.1", generateExternals = generateExternals))
//        implementation(npm("lua-types", "2.11.0", generateExternals = generateExternals))
//        implementation(npm("typescript", "4.4.3", generateExternals = generateExternals))
//      }
//    }
//
//    val test by getting {
//      dependencies {
//        implementation(kotlin("test-js"))
//      }
//    }
//  }
//
//}

val tstlTask = tasks.register<com.github.gradle.node.npm.task.NpmTask>("tstl") {
  description = "Typescript To Lua"
  group = "${rootProject.group}"
  dependsOn(tasks.npmInstall)

  npmCommand.set(listOf("run", "build"))
  args.set(listOf("--", "--outDir", "$buildDir/npm-output"))
//  args.set(listOf("test"))
  ignoreExitValue.set(false)
//  environment.set(mapOf("MY_CUSTOM_VARIABLE" to "hello"))
  workingDir.set(projectDir)
  execOverrides {
    standardOutput = System.out
  }
  inputs.dir("node_modules")
  inputs.dir(fileTree("src/main/typescript"))
  inputs.files(
      "tsconfig.json",
      "package.json",
  )
  outputs.dir("$buildDir/factorio-web-map/")
//  outputs.dir("$buildDir/npm-output")
//  outputs.upToDateWhen { true }
}

//sourceSets {
//  val typescript by creating {
//    setBuildDir("src/main/typescript")
//  }
//}


//dependencies {
//  val kotlinWrappersVersion = "0.0.1-pre.254-kotlin-1.5.31"
//  implementation(
//      project.dependencies.enforcedPlatform(
//          "org.jetbrains.kotlin-wrappers:kotlin-wrappers-bom:${kotlinWrappersVersion}"
//      )
//  )
//
//  implementation("org.jetbrains.kotlin-wrappers:kotlin-react")
//  implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom")
//  implementation(npm("react", "17.0.2"))
//  implementation(npm("react-dom", "17.0.2"))
//
// // @types/node

////        implementation("org.jetbrains.kotlinx:kotlinx-nodejs:0.0.7") {
////          because("https://github.com/Kotlin/kotlinx-nodejs")
////        }
//
//  val generateExternals = false
//  implementation(npm("typescript-to-lua", "1.0.1", generateExternals = generateExternals))
//  implementation(npm("typed-factorio", "0.7.1", generateExternals = generateExternals))
//  implementation(npm("lua-types", "2.11.0", generateExternals = generateExternals))
//  implementation(npm("typescript", "4.4.3", generateExternals = generateExternals))
//
//  testImplementation(kotlin("test-js"))
//}