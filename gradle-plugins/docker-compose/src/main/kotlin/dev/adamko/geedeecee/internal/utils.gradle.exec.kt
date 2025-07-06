package dev.adamko.geedeecee.internal

import org.gradle.api.provider.Provider
import org.gradle.process.ExecResult


/**
 * Returns `true` if the [ExecResult.getExitValue] is `0`.
 */
internal val ExecResult.isSuccess: Boolean
  get() = exitValue == 0

/**
 * Returns `true` if the [ExecResult.getExitValue] is `0`.
 */
internal val Provider<ExecResult>.isSuccess: Provider<Boolean>
  get() = map { it.isSuccess }

/**
 * Returns `false` if the [ExecResult.getExitValue] is not `0`.
 */
internal val ExecResult.isFailure: Boolean
  get() = !isSuccess

/**
 * Returns `false` if the [ExecResult.getExitValue] is not `0`.
 */
internal val Provider<ExecResult>.isFailure: Provider<Boolean>
  get() = map { it.isFailure }
