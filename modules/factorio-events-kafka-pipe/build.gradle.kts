import org.jetbrains.kotlin.util.parseSpaceSeparatedArgs

plugins {
  id("dev.adamko.factoriowebmap.archetype.kotlin-jvm")

  application

//  id("com.github.johnrengelman.shadow") version "7.1.0"
}

description =
  """
    Send events from a Factorio server to a Kafka topic 
  """.trimIndent()

val projectId: String by project.extra

dependencies {
  implementation(libs.bundles.logging)
  implementation("eu.jrie.jetbrains:kotlin-shell-core:0.2.1")
}

application {
  mainClass.set("factorioEventsKafkaPipeKt")
}

java {
  withSourcesJar()
}

tasks.jar {
  manifest {
    attributes(
      "Implementation-Title" to project.name,
      "Implementation-Version" to project.version,
      "Main-Class" to application.mainClass,
    )
  }
  archiveBaseName.set(project.name)
  duplicatesStrategy = DuplicatesStrategy.EXCLUDE

  from(
    configurations.runtimeClasspath.map { config ->
      config.map {
        logger.trace("Adding runtimeClasspath file to jar - $it")
        when (it.isDirectory) {
          true -> it
          false -> zipTree(it)
        }
      }
    }
  )

}

tasks.create<Exec>("dockerBuildPdl2k") {
  group = projectId

  dependsOn(tasks.jar)

  val jar = tasks.jar.get().archiveFile.get()
  logger.lifecycle("building docker with jar $jar")
  inputs.file(jar)

  commandLine(
    parseSpaceSeparatedArgs(
      "docker-compose build pdl2k --build-arg JAR_LOCATION=${jar.asFile.canonicalPath}"
    )
  )

//  inputs(tasks.jar.get().archiveFile.get())

//  commandLine(
//    "docker",
//    "build",
//    "--build-arg",
//    "JAR_FILE=",
//    "-t dev-adamko/pdl2k"
//  )
}

//tasks.jar.get().archiveFile.get().asFile.canonicalPath

tasks.create<Exec>("runInDocker") {
  group = projectId
//   docker run -v `pwd`:/mnt java:8 java -jar /mnt/myapp.jar

  dependsOn(tasks.jar)
  val jarFile = tasks.jar.get().archiveFile.get().asFile
  inputs.file(jarFile)

  commandLine(
    listOf(
      "docker",
      "run",
      "--rm",
      "--name", project.name,
//      """--workdir="/app" """,
      "-v", ".//${jarFile.toRelativeString(project.projectDir).replace("\\", "//")}:/mnt/",
      "openjdk:11.0.6-jre-buster",
      "ls", "-la", "/mnt/*",
//      "java", "-jar", "/mnt/${jarFile.name}",
    )
  )

  doFirst {
    logger.lifecycle("Executing [$commandLine]")
  }
}

//afterEvaluate {
//  tasks {
//    create("jar2", Jar::class).apply {
//      dependsOn("jvmMainClasses", "jsJar")
//      group = "jar"
//      manifest {
//        attributes(
//          mapOf(
//            "Implementation-Title" to rootProject.name,
//            "Implementation-Version" to rootProject.version,
//            "Timestamp" to System.currentTimeMillis(),
//            "Main-Class" to application.mainClass
//          )
//        )
//      }
//
//      from(
//        configurations
//      )
//
//      val dependencies =
//        configurations["jvmRuntimeClasspath"]
//          .filter { it.name.endsWith(".jar") } +
//            project.tasks["jvmJar"].outputs.files
//      dependencies.forEach { from(zipTree(it)) }
//      into("/lib")
//    }
//  }
//}
//val fatJar = task("fatJar", type = Jar::class) {
//  baseName = "safescape-lib-${project.name}"
//  // manifest Main-Class attribute is optional.
//  // (Used only to provide default main class for executable jar)
//  from(configurations.runtimeClasspath.map({ if (it.isDirectory) it else zipTree(it) }))
//  with(tasks["jar"] as CopySpec)
//}
//
//tasks {
//  "build" {
//    dependsOn(fatJar)
//  }
//}
//from(configurations.runtimeClasspath) {
//  into("lib")
//}