

val projectsMustBeConfiguringBuster by tasks.registering {
  val buildFile = layout.projectDirectory.file("kt52647.gradle.kts").asFile
  doLast {
    logger.warn("Applying 'Projects must be configuring' workaround - https://youtrack.jetbrains.com/issue/KT-52647")

    val newLines = buildFile.readText().lines().joinToString("\n") { line ->
      if (line.startsWith("// projects must be configuring buster")) {
        "// projects must be configuring buster ${System.currentTimeMillis()}"
      } else {
        line
      }
    }

    buildFile.writeText(newLines)
  }
}


allprojects {
  tasks
    .matching { it.name != projectsMustBeConfiguringBuster.name }
    .configureEach {
      finalizedBy(projectsMustBeConfiguringBuster)
    }
}


// projects must be configuring buster
// ^ this line will be edited by projectsMustBeConfiguringBuster
