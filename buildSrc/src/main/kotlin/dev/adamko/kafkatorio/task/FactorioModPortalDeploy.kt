package dev.adamko.kafkatorio.task

import org.gradle.api.DefaultTask
import org.http4k.client.OkHttp
import org.http4k.core.Method
import org.http4k.core.Request

abstract class FactorioModPortalDeploy : DefaultTask() {

  init {
    super.doFirst {

      val client = OkHttp()

      val request = Request(Method.GET, "https://factorio.com/login?mods=1")

      println(client(request))


    }
  }

}
