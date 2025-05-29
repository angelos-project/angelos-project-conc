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

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * A synchronization primitive that manages exclusive access to a shared resource in concurrent scenarios.
 *
 * The `Dispenser` class encapsulates a resource of type [E] and uses a mutex to ensure that only one coroutine
 * can access the resource at any given time. This prevents race conditions and provides thread safety when
 * sharing resources between coroutines.
 *
 * The [dispense] method allows a suspendable action to be executed with exclusive access to the resource.
 * The action is performed within a critical section, ensuring serialized access and abstracting away
 * the underlying synchronization logic.
 *
 * **Example usage:**
 * ```kotlin
 * import kotlinx.coroutines.*
 * import org.angproj.conc.Dispenser
 * import org.angproj.conc.loop
 * import kotlin.random.Random
 *
 * public fun main(): Unit = runBlocking {
 *     val dispenser = object : Dispenser<MutableList<Int>>(MutableList(10) { Random.nextInt() }) {}
 *
 *     val job = loop {
 *         dispenser.dispense {
 *             if (isEmpty()) {
 *                 println("No more items to dispense.")
 *                 this@loop.cancel() // Stop the loop if no items left
 *             } else {
 *                 println("Dispensed item: ${removeFirst()}")
 *             }
 *         }
 *     }
 *
 *     job.join() // Wait for the loop to finish
 * }
 * ```
 *
 * @param E The type of the resource being managed.
 * @property res The resource to be dispensed in a thread-safe manner.
 */
public abstract class Dispenser<E>(protected val res: E) {

    private val mutex: Mutex = Mutex()

    /**
     * Executes the given action with exclusive access to the resource.
     *
     * This method suspends until it can acquire the mutex lock, ensuring that the action is executed
     * in a thread-safe manner. The action is provided as a suspendable lambda that operates on the resource.
     *
     * @param action The suspendable lambda to execute with exclusive access to the resource.
     * @return The result of the action.
     */
    public suspend fun<R> dispense(action: suspend E.() -> R): R = mutex.withLock { res.action() }
}