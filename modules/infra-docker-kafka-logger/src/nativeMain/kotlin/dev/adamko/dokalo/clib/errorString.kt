package dev.adamko.dokalo.clib

import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.toKString
import kotlinx.cinterop.usePinned


class ErrStringContext(
  val stringRef: CPointer<ByteVar>,
  val stringLen: ULong,
)


fun <N> errStr(call: ErrStringContext.() -> N): ErrStringResult<N> {
  val buf = ByteArray(512)

  val result = buf.usePinned { pinned ->

    val context = ErrStringContext(
      stringRef = pinned.addressOf(0),
      stringLen = (buf.size - 1).toULong(),
    )

    context.call()
  }

  return ErrStringResult(result, buf.toKString())
}


data class ErrStringResult<N : Any?>(
  val result: N,
  val errString: String?,
) {
  fun getOrThrow(onError: (errString: String?) -> String): N = result ?: error(onError(errString))
}


private fun <N : Number> ErrStringResult<N>.throwIfNotSuccessInternal(
  expectedStatusCode: N,
  msg: ErrStringResult<N>.(expected: N) -> String = { "error status code. expected:$it, but was:$result" },
) {
  if (this != expectedStatusCode) {
    error(this.msg(expectedStatusCode))
  }
}


  fun ErrStringResult<Int>.throwIfNotSuccess(
  statusCode: Int = 0,
  msg: ErrStringResult<Int>.(expected: Int) -> String = { "error status code. expected:$it, but was:$result" },
) = throwIfNotSuccessInternal(statusCode, msg)
