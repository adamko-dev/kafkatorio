package dev.adamko.geedeecee.config

import javax.inject.Inject
import org.gradle.api.Named
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.SupportsKotlinAssignmentOverloading
import org.gradle.api.model.ObjectFactory
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.Nested
import org.gradle.api.tasks.Optional
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.domainObjectContainer

/**
 * Key-value store of the entries in the `.env` file read by Docker Compose.
 */
// workaround for Gradle's poor handling of MapProperty
// https://github.com/gradle/gradle/issues/13364
abstract class DotEnvContent @Inject constructor(
  objects: ObjectFactory,
) : ExtensionAware {

  @get:Nested
  // must be 'protected' not 'private', else Gradle can't see the nested values.
  protected val content: NamedDomainObjectContainer<DotEnvEntry> =
    objects.domainObjectContainer(DotEnvEntry::class)

  init {
    content.whenObjectAdded {
      this@DotEnvContent.extensions.add(name, this)
    }
  }

  fun set(key: String, value: String) {
    content.maybeCreate(key).value.set(value)
  }

  fun set(key: String, value: Provider<String>) {
    content.maybeCreate(key).value.set(value)
  }

  fun convention(key: String, value: String) {
    content.maybeCreate(key).value.convention(value)
  }

  fun convention(key: String, value: Provider<String>) {
    content.maybeCreate(key).value.convention(value)
  }

  /**
   * Add all values from [other] into this container.
   *
   * The values from [other] will be [set][Property.set],
   * overriding any [convention][Property.convention] values.
   */
  fun setAll(other: DotEnvContent) {
    other.content.all {
      set(key, value)
    }
  }

  /**
   * Add all values from [other] into this container.
   *
   * The values from [other] will be [conventions][Property.convention],
   * meaning any [set][Property.set] values will take precedence.
   */
  fun addConventions(other: DotEnvContent) {
    other.content.all {
      convention(key, value)
    }
  }

  /**
   * Compute and return all values.
   * This should only be done during task execution, when all values can be safely computed.
   */
  fun compute(): Map<String, String?> {
    return content.associate { it.key to it.value.orNull }
  }

  /**
   * A single key-value entry in [DotEnvContent].
   */
  @SupportsKotlinAssignmentOverloading
  abstract class DotEnvEntry @Inject constructor(
    @get:Input
    val key: String,
  ) : Named {

    @get:Input
    @get:Optional
    abstract val value: Property<String>

    @Suppress("unused") // IJ doesn't correctly detect assignment overloading usages
    fun assign(value: String): Unit = this.value.assign(value)

    @Suppress("unused") // IJ doesn't correctly detect assignment overloading usages
    fun assign(value: Provider<String>): Unit = this.value.assign(value)

    @Internal
    override fun getName(): String = key
  }
}
