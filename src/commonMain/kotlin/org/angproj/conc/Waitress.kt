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
import kotlinx.coroutines.sync.Mutex

/**
 * A coroutine waitress that executes a specified action in response to explicit wake-up signals,
 * but only once per sleep cycle, regardless of how many wake-up calls are made while active.
 *
 * The `Waitress` interface is designed for event-driven scenarios where actions should be performed
 * at most once per trigger, such as debouncing or single-shot event handling. When the `wakeUp` method
 * is called, the waitress is signaled to execute its action. If it is already awake and processing,
 * further calls to `wakeUp` are ignored until it returns to its dormant state.
 *
 * This mechanism ensures that redundant or rapid-fire wake-up signals do not cause repeated execution,
 * providing a simple and reliable way to handle events that should not be processed more than once per cycle.
 *
 * The underlying coroutine job can be used for lifecycle management, such as cancellation or monitoring.
 */
public interface Waitress {

    /**
     * Signals the waitress to wake up and execute its action.
     *
     * If the waitress is dormant, this call will wake it up and allow it to perform its action.
     * If the waitress is already awake and processing, the call is ignored and returns `false`.
     *
     * @return `true` if the waitress was successfully woken up, `false` if it was already awake.
     */
    public fun wakeUp(): Boolean

    /**
     * The coroutine job representing the waitress's execution.
     *
     * This job can be used to manage the lifecycle of the waitress, such as cancellation or monitoring its status.
     */
    public val job: Job
}

/**
 * Creates a waitress coroutine that remains dormant until explicitly woken up, executing the provided action
 * only once per sleep cycle, regardless of how many wake-up calls are made while it is active.
 *
 * The waitress is ideal for event-driven scenarios where actions should only be performed in response to
 * a single trigger per cycle. Each call to [wakeUp] signals the waitress to execute its action, but if the
 * waitress is already awake, additional wake-up calls are ignored until it returns to sleep. This ensures
 * that the action is not executed more than once per activation, making it suitable for debouncing or
 * single-shot event handling.
 *
 * The returned [Waitress] instance provides the [wakeUp] method for signaling and exposes the underlying
 * coroutine [job] for lifecycle management, such as cancellation or monitoring.
 *
 * @param action The suspendable lambda to execute upon each wake-up event.
 * @return A [Waitress] instance for managing and signaling the coroutine.
 */
public fun answer(action: suspend CoroutineScope.() -> Unit): Waitress = object : Waitress {
    private val mutex: Mutex = Mutex()

    override val job: Job = CoroutineScope(Dispatchers.Default).async {
        while (isActive) {
            sleep()
            action()
            yield()
        }
    }

    init { job.start() }

    private suspend fun sleep(): Unit = mutex.lock()
    override fun wakeUp(): Boolean = when(mutex.isLocked) {
        false -> false
        else -> { mutex.unlock(); true }
    }
}


