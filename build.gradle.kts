plugins {
  idea
  base
  `project-report`
  `build-dashboard`
}

group = "dev.adamko"
version = "0.0.7"

tasks.wrapper {
  gradleVersion = "7.3"
  distributionType = Wrapper.DistributionType.ALL
}

val startInfra by tasks.registering {
  group = project.name

  dependsOn(
    ":modules:infra-kafka-pipe:processRun",
    ":modules:infra-kafka-cluster:processRun",
  )
}

val runKafkatorio by tasks.registering {
  group = project.name

  dependsOn(
    ":modules:infra-factorio-client:processRun",
    ":modules:infra-factorio-server:processRun",
  )
}
