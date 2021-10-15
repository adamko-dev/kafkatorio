package dev.adamko.ktrcon

class Password constructor(password: String) {
  private val password: ByteArray = password.encodeToByteArray()
  private val hashcode: Int = password.hashCode()

  operator fun invoke() = password.decodeToString()

  fun access(accessor: (password: String) -> Unit) {
    accessor(password.decodeToString())
  }

  override fun equals(other: Any?) = (other as? Password)?.hashcode == this.hashcode

  override fun hashCode() = hashcode
}