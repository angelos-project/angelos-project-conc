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
 * Supervises a coroutine job at regular intervals. If the job is cancelled,
 * the action is executed with the elapsed time since the last call.
 *
 * @param every The time unit for the interval to check the job.
 * @param supervise The job to be supervised.
 * @param action The action to be executed at cancellation.
 * @return A [Job] of the call itself.
 */
public fun call(
    every: DurationUnit, supervise: Job, action: suspend CoroutineScope.(Duration) -> Unit
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
            supervise.isCancelled -> action(elapsed).also { this@async.cancel() }
            supervise.isCompleted -> this@async.cancel()
        }
    }
}.apply { start() }.job
