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
 * @param E The type of the resource being managed.
 * @property res The resource to be dispensed in a thread-safe manner.
 */
public abstract class Dispenser<E>(protected val res: E) {

    private val mutex: Mutex = Mutex()

    /**
     * Dispenses the resource to the action.
     * The action is executed in a critical section, ensuring that
     * only one coroutine can access the resource at a time.
     *
     * @param action The action to be executed with the resource.
     * @return The result of the action.
     */
    public suspend fun<R> dispense(action: suspend E.() -> R): R = mutex.withLock { res.action() }
}