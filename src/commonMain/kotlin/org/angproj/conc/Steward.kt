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
 * An interface representing a steward that can be woken up.
 * The implementation should provide a way to wake up the steward and manage the underlying coroutine job.
 *
 * The steward is a sleeping coroutine that performs an action when woken up.
 * If the steward is already awake, calling [wakeUp] will increment the wake-up counter.
 */
public interface Steward {

    /**
     * Wakes up the steward.
     * This method should be called to signal the steward to perform its action.
     * It returns the number of times it has to run the action since the last wake-up.
     *
     * @return The number of wake-ups left to perform.
     */
    public fun wakeUp(): Int

    /**
     * The job representing the underlying coroutine that performs the action.
     * This job can be used to manage the lifecycle of the coroutine, such as cancelling it.
     */
    public val job: Job
}

/**
 * Creates a steward coroutine that remains dormant until explicitly woken up, executing the provided action
 * once for each registered wake-up event.
 *
 * The steward is ideal for event-driven scenarios where actions should only be performed in response to
 * external triggers. Each call to [wakeUp] signals the steward to execute its action, and multiple wake-ups
 * are faithfully queued and processed in order. This ensures that no wake-up is lost, even if the steward
 * is already active.
 *
 * The returned [Steward] instance provides the [wakeUp] method for signaling and exposes the underlying
 * coroutine [job] for lifecycle management, such as cancellation or monitoring.
 *
 * @param action The suspendable lambda to execute upon each wake-up event.
 * @return A [Steward] instance for managing and signaling the coroutine.
 */
public fun attend(action: suspend CoroutineScope.() -> Unit): Steward = object : Steward {
    private val mutex: Mutex = Mutex()
    private var counter = 0

    override val job: Job = CoroutineScope(Dispatchers.Default).async {
        while (isActive) {
            sleep()
            while(counter > 0) {
                counter--
                action()
            }
            yield()
        }
    }

    init { job.start() }

    private suspend fun sleep(): Unit = mutex.lock()
    override fun wakeUp(): Int {
        counter++
        if(mutex.isLocked)
            mutex.unlock()
        return counter
    }
}