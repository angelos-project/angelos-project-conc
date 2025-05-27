/**
 * Copyright (c) 2025 by Kristoffer Paulsson <kristoffer.paulsson@talenten.se>.
 *
 * This software is available under the terms of the MIT license. Parts are licensed
 * under different terms if stated. The legal terms are attached to the LICENSE file
 * and are made available on:
 *
 *      https://opensource.org/licenses/MIT
 *
 * SPDX-License-Identifier: MIT
 *
 * Contributors:
 *      Kristoffer Paulsson - initial implementation
 */
package org.angproj.conc

import kotlinx.coroutines.*
import kotlin.time.*


/**
 * Schedules the execution of a coroutine after a specified delay.
 *
 * This function launches a coroutine that waits for the given [inTime] duration before executing
 * the provided [action]. It is useful for deferring tasks, implementing timeouts, or scheduling
 * events to occur after a certain period. If the coroutine is cancelled before the delay completes,
 * the action will not be executed, ensuring efficient resource usage.
 *
 * Unlike immediate execution functions, `schedule` allows for precise control over when a task
 * should begin, making it ideal for time-based operations in concurrent environments.
 *
 * The returned [Job] can be used to manage the lifecycle of the scheduled coroutine, including
 * cancellation or monitoring.
 *
 * @param inTime The delay duration before the coroutine is executed.
 * @param action The suspendable lambda to execute after the delay.
 * @return A [Job] representing the scheduled coroutine.
 */
public fun schedule(
    inTime: Duration, action: suspend CoroutineScope.() -> Unit
): Job = CoroutineScope(Dispatchers.Default).launch {
    delay(inTime)
    if(isActive) action()
}.apply { start() }



