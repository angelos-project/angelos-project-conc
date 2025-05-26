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
 * Starts a coroutine that runs the specified action repeatedly.
 * The Steward attends to every wake-up call, executing the action for each registered wake-up.
 * If the steward is already awake, calling [wakeUp] will increment the wake-up counter.
 *
 * This method creates a coroutine using the [Dispatchers.Default] context and executes
 * the provided action in an infinite loop, allowing it to be woken up by calling [wakeUp].
 *
 * @param action The action to be executed in the coroutine. This is a suspendable lambda
 *               that operates within a [CoroutineScope].
 * @return A [Steward] instance that can be used to wake up the coroutine.
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