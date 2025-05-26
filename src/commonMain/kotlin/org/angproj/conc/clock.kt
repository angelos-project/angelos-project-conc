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
 * Creates a perpetual clock that executes a given action at a specified rate of ticks per time unit.
 * The clock starts a new coroutine and returns a {@link Job} that can be used to manage its lifecycle.
 *
 * <p>The clock ensures that the action is executed at regular intervals based on the specified
 * number of ticks per time unit. If the coroutine is canceled, the clock stops execution.
 *
 * @param unit The unit of time to use for the clock (e.g., seconds, milliseconds).
 * @param ticks The number of ticks per time unit. Determines the frequency of the action.
 * @param action The action to perform on each tick. This is a suspendable lambda that operates
 *               within a {@link CoroutineScope}.
 * @return A {@link Job} representing the clock coroutine. The caller can use this to cancel
 *         or monitor the clock's execution.
 *
 * <p>Example usage:
 * <pre>{@code
 * Job clockJob = clock(DurationUnit.SECONDS, 2, () -> {
 *     System.out.println("Tick");
 * });
 * clockJob.invokeOnCompletion(() -> System.out.println("Clock stopped"));
 * }</pre>
 *
 * @see kotlinx.coroutines.CoroutineScope
 * @see kotlinx.coroutines.Job
 * @see kotlin.time.DurationUnit
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