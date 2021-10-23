import eu.jrie.jetbrains.kotlinshell.shell.shell

fun main() {
  shell {

    "echo hello world"()

//    """
//      curl \
//        --no-buffer \
//        -sS \
//        --unix-socket /var/run/docker.sock \
//        "http://localhost/v1.41/containers/factorio-server/logs?stdout=true&follow=true&tail=0"
//    """.trimIndent()

  }
}