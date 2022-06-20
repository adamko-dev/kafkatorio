package dev.adamko.kafkatorio

import org.gradle.api.internal.tasks.userinput.UserInputHandler
import org.gradle.kotlin.dsl.support.serviceOf

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
