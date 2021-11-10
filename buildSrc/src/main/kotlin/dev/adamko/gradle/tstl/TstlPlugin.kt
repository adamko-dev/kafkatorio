package dev.adamko.gradle.tstl

import com.github.gradle.node.NodePlugin
import dev.adamko.gradle.tstl.tasks.TypescriptToLuaTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.BasePlugin
import org.gradle.kotlin.dsl.withType
import org.gradle.language.base.plugins.LifecycleBasePlugin


class TstlPlugin : Plugin<Project> {
  override fun apply(project: Project) {

    project.plugins.withType<NodePlugin> {
    }

    project.plugins.withType<BasePlugin> {
      project.tasks.named(LifecycleBasePlugin.ASSEMBLE_TASK_NAME) {
        dependsOn(project.tasks.withType<TypescriptToLuaTask>())
      }
    }
  }
}