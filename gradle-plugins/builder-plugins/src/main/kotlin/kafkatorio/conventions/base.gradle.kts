package kafkatorio.conventions


plugins {
  base
  idea
}


group = rootProject.group
version = rootProject.version


tasks.withType<WriteProperties>().configureEach {
  encoding = Charsets.UTF_8.name()
  comment = " Do not edit manually. This file was created with task '$name'"
}


tasks.withType<AbstractArchiveTask>().configureEach {
  exclude("**/.secret.*")
}
