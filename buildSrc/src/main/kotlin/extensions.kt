import org.gradle.api.Task
import org.gradle.api.artifacts.Configuration
import org.gradle.api.tasks.TaskCollection

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
//
//fun Configuration.typescriptAttributes(objects: ObjectFactory): Configuration =
//  attributes {
//    attribute(Usage.USAGE_ATTRIBUTE, objects.named("dev.adamko.gradle.factorio.typescriptFiles"))
//  }


fun <T : Task> TaskCollection<T>.withName(name: String): TaskCollection<T> =
  matching { it.name == name }
