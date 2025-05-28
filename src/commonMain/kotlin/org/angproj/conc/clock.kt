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
import kotlin.time.DurationUnit
import kotlin.time.TimeSource
import kotlin.time.toDuration


/**
 * Launches a perpetual clock coroutine that executes the given action at a fixed rate,
 * determined by the number of ticks per specified time unit.
 *
 * The clock repeatedly invokes the provided lambda at regular intervals, making it suitable
 * for periodic tasks such as polling, scheduled updates, or recurring events. The interval
 * between executions is calculated by dividing the time unit by the number of ticks, ensuring
 * consistent and predictable timing. If the coroutine is cancelled, the clock stops automatically.
 *
 * **Example usage:**
 * ```kotlin
 * import kotlinx.coroutines.cancelAndJoin
 * import kotlinx.coroutines.delay
 * import kotlinx.coroutines.runBlocking
 * import org.angproj.conc.clock
 * import kotlin.time.Duration.Companion.seconds
 * import kotlin.time.DurationUnit
 * import kotlin.time.TimeSource
 *
 *
 * public fun main(): Unit = runBlocking {
 *     val ticksPerSecond = 4
 *     val totalSeconds = 5
 *     val start = TimeSource.Monotonic.markNow()
 *     var count = 0
 *
 *     val job = clock(DurationUnit.SECONDS, ticksPerSecond) {
 *         val elapsed = start.elapsedNow()
 *         count++
 *         println("Tick $count! Elapsed time: $elapsed")
 *     }
 *
 *     delay(totalSeconds.seconds)
 *     job.cancelAndJoin()
 * }
 * ```
 *
 * @param unit The unit of time to use for the clock (e.g., seconds, milliseconds).
 * @param ticks The number of ticks per time unit, determining the frequency of execution.
 * @param action The suspendable lambda to execute on each tick.
 * @return A [Job] representing the running clock coroutine.
 */
public fun clock(
    unit: DurationUnit, ticks: Int, action: suspend CoroutineScope.() -> Unit
): Job = CoroutineScope(Dispatchers.Default).async {
    var start = TimeSource.Monotonic.markNow()
    var counter: Long = 0

    while (isActive) {
        action()

        counter++
        val elapsed = start.elapsedNow()

        if(counter == Long.MAX_VALUE) {
            start += elapsed
            counter = 0
        }

        delay((counter.toDouble() / ticks).toDuration(unit) - elapsed)
    }
}.apply { start() }.job