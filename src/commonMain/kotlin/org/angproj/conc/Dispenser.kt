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
 * A generic synchronization primitive that provides a way to dispense a resource to an action.
 * The resource is protected by a mutex to ensure thread safety.
 *
 * @param E The type of the resource being dispensed.
 * @property res The resource to be dispensed.
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