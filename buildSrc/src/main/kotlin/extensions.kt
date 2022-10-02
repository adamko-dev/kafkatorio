import org.gradle.api.artifacts.Configuration
import org.gradle.api.attributes.Usage
import org.gradle.api.model.ObjectFactory
import org.gradle.kotlin.dsl.named

// to be moved into gradle-plugins/builder-plugins...


fun Configuration.asProvider() {
  isVisible = false
  isCanBeResolved = false
  isCanBeConsumed = true
}


fun Configuration.asConsumer() {
  isVisible = false
  isCanBeResolved = true
  isCanBeConsumed = false
}

fun Configuration.typescriptAttributes(objects: ObjectFactory): Configuration =
  attributes {
    attribute(Usage.USAGE_ATTRIBUTE, objects.named("dev.adamko.gradle.factorio.typescriptFiles"))
  }
