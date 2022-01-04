import dev.adamko.kafkatorio.gradle.asProvider
import dev.adamko.kafkatorio.gradle.typescriptAttributes

plugins {
  kotlin("multiplatform")
  kotlin("plugin.serialization")
  distribution
}

kotlin {
  js(IR) {
    binaries.executable()
    nodejs()
  }
  jvm()

  @Suppress("UnstableApiUsage") // enforcedPlatform is incubating
  sourceSets {

    all {
      languageSettings.apply {
        optIn("kotlin.RequiresOptIn")
        optIn("kotlin.ExperimentalStdlibApi")
        optIn("kotlin.time.ExperimentalTime")
        optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
        optIn("kotlinx.serialization.ExperimentalSerializationApi")
        optIn("kotlin.js.ExperimentalJsExport")
      }
    }

    val commonMain by getting {
      dependencies {
        implementation(project.dependencies.platform(libs.kotlin.bom))

        implementation(project.dependencies.platform(libs.kotlinx.serialization.bom))
        implementation(libs.kotlinx.serialization.core)
        implementation(libs.kotlinx.serialization.json)

      }
    }

    val commonTest by getting {
      dependencies {

        implementation(kotlin("test"))

        implementation(project.dependencies.platform(libs.kotest.bom))
        implementation("io.kotest:kotest-assertions-core")
        implementation("io.kotest:kotest-property")
        implementation("io.kotest:kotest-assertions-json")
      }
    }

    val jvmMain by getting {
      dependencies {
        implementation("com.github.ntrrgc:ts-generator:1.1.2")
        implementation("org.jetbrains.kotlin:kotlin-reflect")
      }
    }

    val jvmTest by getting {
      dependencies {
        implementation("io.kotest:kotest-runner-junit5")
      }
    }
  }
}


val generateTypescript by tasks.registering(JavaExec::class) {
  group = project.name

  val jvmJar by tasks.getting(Jar::class)
  val main by kotlin.jvm().compilations.getting
  classpath(
    jvmJar,
    main.runtimeDependencyFiles
  )

  mainClass.set("dev.adamko.kafkatorio.events.schema.Kt2tsKt")

  val generatedFile = file("$temporaryDir/${project.name}.ts")
  args(generatedFile)

  val buildOutput = layout.buildDirectory.dir("generated/typescript")
  outputs.dir(buildOutput)

  doFirst {
    delete(temporaryDir)
    mkdir(temporaryDir)
  }

  doLast {
    sync {
      from(temporaryDir)
      into(buildOutput)
      include("**/*.ts")
    }
  }
}

val schemaTs by distributions.registering {
  distributionBaseName.set("${rootProject.name}-${project.name}")
  contents {
    from(generateTypescript.map { it.outputs.files.asFileTree })
  }
}

val typescriptModelGenerated : Configuration by configurations.creating {
  asProvider()
  typescriptAttributes(objects)

  val schemaTsZipTask = tasks.named<Zip>("${schemaTs.name}DistZip")
  outgoing.artifact(schemaTsZipTask.flatMap { it.archiveFile })
}
