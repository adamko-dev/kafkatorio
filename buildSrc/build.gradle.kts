plugins {
  `kotlin-dsl`
}

dependencies {

  val kotlinVersion = "1.5.31"

  implementation(platform("org.jetbrains.kotlin:kotlin-bom:$kotlinVersion"))

  implementation("org.jetbrains.kotlin:kotlin-gradle-plugin")

  implementation("com.github.node-gradle:gradle-node-plugin:3.1.1")

}
