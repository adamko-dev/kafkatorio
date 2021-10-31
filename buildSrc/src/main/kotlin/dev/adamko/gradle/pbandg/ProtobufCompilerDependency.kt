package dev.adamko.gradle.pbandg

import dev.adamko.gradle.pbandg.settings.PBAndGSettings
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration


class ProtobufCompilerDependency(
  project: Project,
  pluginSettings: PBAndGSettings,
  private val conf: Configuration = project.configurations.create("protobufCompiler") {
    this.description = "Define a single dependency that provides the protoc.exe for this system"
    isVisible = false
    isCanBeConsumed = false
    isCanBeResolved = true
    isTransitive = false

    defaultDependencies {
      add(pluginSettings.dependency.get())
    }
  }
) : Configuration by conf
