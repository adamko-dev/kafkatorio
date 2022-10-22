plugins {
  id("kafkatorio.conventions.base")
  id("dev.adamko.geedeecee")
}

description = "Send events from a Factorio server to a Kafka topic "


geedeecee {
  srcDir.set(layout.projectDirectory.dir("src"))
}

//val dockerSrcDir: Directory by extra

tasks.dockerComposeUp {
  dependsOn(":modules:infra-kafka-cluster:dockerUp")
}
