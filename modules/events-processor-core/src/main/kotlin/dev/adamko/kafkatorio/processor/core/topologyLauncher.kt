package dev.adamko.kafkatorio.processor.core

import dev.adamko.kafkatorio.processor.config.ApplicationProperties
import java.util.Properties
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.io.path.Path
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.StreamsConfig
import org.apache.kafka.streams.Topology
import org.apache.kafka.streams.TopologyDescription



suspend fun launchTopology(
  id: String,
  topology: Topology,
  additionalProperties: Map<String, String> = mapOf(),
  appProps: ApplicationProperties,
): Unit = coroutineScope {

  val props: Properties = appProps.kafkaStreamsConfig.toProperties()

  additionalProperties.forEach { (k, v) -> props.setProperty(k, v) }

  val appId = props.compute(StreamsConfig.APPLICATION_ID_CONFIG) { _, v -> "$v.$id" } as String
  props.setProperty(StreamsConfig.APPLICATION_ID_CONFIG, appId)

  launch(CoroutineName(appId)) {

    val streamsState: KafkaStreams.State = suspendCancellableCoroutine { cont ->

      val streams = KafkaStreams(topology, props)
      streams.setUncaughtExceptionHandler(StreamsExceptionHandler { e ->
        cont.resumeWithException(e)
      })

      streams.setStateListener { newState: KafkaStreams.State, _ ->
        when (newState) {
          KafkaStreams.State.PENDING_SHUTDOWN,
          KafkaStreams.State.NOT_RUNNING,
          KafkaStreams.State.PENDING_ERROR,
          KafkaStreams.State.ERROR -> if (cont.isActive) cont.resume(newState)

          else                     -> Unit // do nothing
        }
      }

      cont.invokeOnCancellation {
        println("[$appId] coroutine cancelled -> closing. cause:$it")
        streams.close()
      }

      printTopologyDescription(appId, topology)
      streams.start()
      println("launched Topology $appId")
    }
    println("exiting Topology $appId: $streamsState")
    currentCoroutineContext().job.cancel("$appId: $streamsState")
  }
}


private fun printTopologyDescription(appId: String, topology: Topology) {
  val description: TopologyDescription = topology.describe()

  val descFile = Path("./build/$appId.txt").toFile().apply {
    parentFile.mkdirs()
    if (!exists()) createNewFile()
    writeText(description.toString())
  }

  println(
    """
        |----------------
        |$appId - ${descFile.canonicalPath}
        |$description
        |----------------
      """.trimMargin()
  )
}
