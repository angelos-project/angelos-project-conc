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
 * Starts a coroutine that runs the specified action repeatedly.
 * The waitress answers the wake-up call only once per run, skipping any subsequent calls until the next sleep.
 * If the waitress is already awake, calling [wakeUp] will not have any effect.
 *
 * This method creates a coroutine using the [Dispatchers.Default] context and executes
 * the provided action in an infinite loop, allowing it to be woken up by calling [wakeUp].
 *
 * @param action The action to be executed in the coroutine. This is a suspendable lambda
 *               that operates within a [CoroutineScope].
 * @return A [Waitress] instance that can be used to wake up the coroutine.
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