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

val hardReset by tasks.registering(Delete::class) {
  group = rootProject.name
  doFirst("hardResetConfirmation") {
    val answer = project.serviceOf<UserInputHandler>()
      .askYesNoQuestion("Are you sure you want to permanently delete Kafkatorio data?")

    if (answer != true) {
      throw GradleException("Aborting hard reset")
    }
  }
}
