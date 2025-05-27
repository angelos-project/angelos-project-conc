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
 * A coroutine steward that remains dormant until explicitly woken up, executing a specified action
 * once for each registered wake-up event. The steward ensures that all wake-up signals are faithfully
 * queued and processed in order, making it suitable for scenarios where every trigger must be handled.
 *
 * The [wakeUp] method signals the steward to perform its action. Each call increments an internal counter,
 * guaranteeing that no wake-up is lost, even if the steward is already active. The steward processes
 * each wake-up event sequentially, executing the action once per event.
 *
 * The underlying coroutine [job] can be used for lifecycle management, such as cancellation or monitoring.
 *
 * Typical use cases include event-driven systems where every external trigger must be processed,
 * such as task queues, notification handlers, or ordered event processing.
 */
public interface Steward {

    /**
     * Signals the steward to wake up and execute its action.
     *
     * Each call increments the internal wake-up counter. The steward will execute its action
     * once for each registered wake-up event, ensuring all events are processed in order.
     *
     * @return The current count of wake-up events after this call.
     */
    public fun wakeUp(): Int

    /**
     * The coroutine job representing the steward's execution.
     *
     * This job can be used to manage the lifecycle of the steward, such as cancellation or monitoring its status.
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