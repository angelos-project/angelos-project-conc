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
 * An interface representing a waitress that can be woken up.
 * The implementation should provide a way to wake up the waitress and manage the underlying coroutine job.
 *
 * The waitress is a sleeping coroutine that performs an action when woken up.
 * If the waitress is already awake, calling [wakeUp] will not have any effect.
 */
public interface Waitress {

    /**
     * Wakes up the waitress.
     * This method should be called to signal the waitress to perform its action.
     * It returns `true` if the waitress was successfully woken up, or `false` if it was already awake.
     *
     * @return `true` if the waitress was woken up, `false` otherwise.
     */
    public fun wakeUp(): Boolean

    /**
     * The job representing the underlying coroutine that performs the action.
     * This job can be used to manage the lifecycle of the coroutine, such as cancelling it.
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