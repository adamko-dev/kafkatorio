package dev.adamko.kafkatorio.webmap

import io.kvision.BootstrapIconsModule
import io.kvision.BootstrapModule
import io.kvision.ChartModule
import io.kvision.CoreModule
import io.kvision.FontAwesomeModule
import io.kvision.html.Link
import io.kvision.module
import io.kvision.startApplication
import kotlinx.coroutines.Job


val rootJob = Job()


fun main() {
  startApplication(
    {
      Link.useDataNavigoForLinks = true
      App
    },
    module.hot,
    BootstrapModule,
//    BootstrapCssModule,
    BootstrapIconsModule,
    FontAwesomeModule,
    ChartModule,
    CoreModule,
  )
}
