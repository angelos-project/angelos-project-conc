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
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.TimeSource
import kotlin.time.toDuration

/**
 * Periodically monitors the state of a given coroutine job and executes a specified action if the job is cancelled.
 *
 * This function launches a coroutine that checks the status of the `monitor` job at intervals defined by `every`.
 * If the monitored job is cancelled, the provided `action` lambda is invoked with the elapsed time since the last check.
 * This is useful for handling cancellation events, such as resource cleanup or custom notification logic, in a timely manner.
 * The monitoring coroutine stops itself after the action is executed or if the monitored job completes normally.
 *
 * **Example usage:**
 * ```kotlin
 * import kotlinx.coroutines.delay
 * import kotlinx.coroutines.runBlocking
 * import org.angproj.conc.call
 * import org.angproj.conc.schedule
 * import kotlin.time.Duration.Companion.seconds
 * import kotlin.time.DurationUnit
 *
 * public fun main(): Unit = runBlocking {
 *     val scheduledJob = schedule(5.seconds) {
 *         println("Scheduled task executed.")
 *     }
 *
 *     val monitorJob = call(DurationUnit.SECONDS, scheduledJob) { elapsed ->
 *         println("Scheduled job was cancelled after ${elapsed}.")
 *     }
 *
 *     // Cancel the scheduled job after 2 seconds to trigger the call action
 *     delay(2.seconds)
 *     scheduledJob.cancel()
 *
 *     monitorJob.join()
 *     println("Monitor finished.")
 * }
 * ```
 *
 * @param every The time unit interval at which to check the monitored job's state.
 * @param monitor The coroutine job to be monitored for cancellation.
 * @param action The suspendable lambda to execute if the monitored job is cancelled. Receives the elapsed time since the last check.
 * @return A [Job] representing the monitoring coroutine.
 */
public fun call(
    every: DurationUnit, monitor: Job, action: suspend CoroutineScope.(Duration) -> Unit
): Job = CoroutineScope(Dispatchers.Default).async {
    var start = TimeSource.Monotonic.markNow()
    var counter: Long = 0

    while (isActive) {
        counter++
        val elapsed = start.elapsedNow()

        if(counter == Long.MAX_VALUE) {
            start += elapsed
            counter = 0
        }

        delay(counter.toDuration(every) - elapsed)

        when {
            monitor.isCancelled -> action(elapsed).also { this@async.cancel() }
            monitor.isCompleted -> this@async.cancel()
        }
    }
}.apply { start() }.job
