import dev.adamko.geedeecee.tasks.GDCCommandTask
import dev.adamko.gradle.factorio.FactorioModPlugin
import org.gradle.kotlin.dsl.support.serviceOf

plugins {
  id("kafkatorio.conventions.base")
  id("dev.adamko.geedeecee")
  id("dev.adamko.factorio-mod-library")
  idea
}


//val factorioServerUserId = "845"
//val factorioServerGroupId = "845"

geedeecee {
  srcDir = layout.projectDirectory.dir("src")

//  val userHome = Path(System.getProperty("user.home"))
//  val userUid = userHome.getAttribute("uid") as Int
//  val userGid = userHome.getAttribute("gid") as Int
//  int uid = Files.getAttribute(userHome, "unix:uid")

  dotEnv {
//    set("FACTORIO_SERVER_USER_ID", factorioServerUserId)
//    set("FACTORIO_SERVER_GROUP_ID", factorioServerGroupId)
    set("FACTORIO_SERVER_USER_ID", "502")
    set("FACTORIO_SERVER_GROUP_ID", "20")
  }
}

//abstract class CurrentUserUid : ValueSource<String, ValueSourceParameters.None> {
//  override fun obtain(): String {
//    val userHome = Path(System.getProperty("user.home"))
//    val id = userHome.getAttribute("unix:uid") as Int
//    return "$id"
//  }
//}
//
//abstract class CurrentUserGid : ValueSource<String, ValueSourceParameters.None> {
//  override fun obtain(): String {
//    val userHome = Path(System.getProperty("user.home"))
//    val id = userHome.getAttribute("unix:gid") as Int
//    return "$id"
//  }
//}

val factorioServerDataDir = geedeecee.srcDir.dir("factorio-server").get()


dependencies {
  factorioMod(projects.modules.eventsMod)
}


val deployModToLocalServer by tasks.registering {
  description = "Copy the mod to the Factorio Docker server."
  group = FactorioModPlugin.TASK_GROUP

  val fs = serviceOf<FileSystemOperations>()

  val sourceFiles = configurations.factorioModResolver.map { it.incoming.files }
  val destinationDir = factorioServerDataDir.dir("mods")
//
//  val userId = factorioServerUserId
//  val groupId = factorioServerGroupId

  doLast {
    logger.lifecycle("Copying ${sourceFiles.orNull?.count()} mods files:${sourceFiles.orNull?.files} into ${destinationDir.asFile}")

    fs.copy {
      from(sourceFiles)
      into(destinationDir)
//      filePermissions {
//        group { read = true; write = true }
//      }
    }
  }
}


//tasks.dockerComposeDown {
//  commandLine = parseSpaceSeparatedArgs(""" docker-compose stop """)
//}

tasks.dockerComposeBuild {
//  dependsOn(deployModToLocalServer)
}

tasks.dockerComposeUp {
  dependsOn(
//    deployModToLocalServer,
    ":modules:infra-kafka-cluster:dockerComposeUp",
    ":modules:events-server-syslog:dockerComposeUp",
  )
}


val kafkatorioServerToken = providers.gradleProperty("kafkatorio.server.token")
  .orElse("missing")


tasks.dockerComposeEnvUpdate {
  envProperties {
    set("FACTORIO_VERSION", libs.versions.factorio)
    set("KAFKATORIO_TOKEN", kafkatorioServerToken)
  }
}


//tasks.register(FactorioModPlugin.PUBLISH_MOD_LOCAL_TASK_NAME) {
//  group = FactorioModPlugin.TASK_GROUP
//  dependsOn(deployModToLocalServer)
//}


idea {
  module {
    excludeDirs.add(file("src/factorio-server"))
  }
}

val runFactorioServer by tasks.registering {
  group = rootProject.name

  dependsOn(tasks.dockerComposeUp)
}


tasks.withType<GDCCommandTask>().configureEach {
  workingDirFiles.setFrom(
    geedeecee.srcDir.asFileTree.matching {
      exclude("factorio-server/temp/**")
    }
  )
}
