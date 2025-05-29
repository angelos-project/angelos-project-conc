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

import kotlinx.coroutines.Job

/**
 * An interface representing a coroutine that can be woken up to perform an action.
 *
 * This interface defines the contract for coroutines that can be awakened to execute a specific action,
 * allowing for controlled execution and management of coroutine lifecycles.
 *
 * @param E The type of the result produced by the wake-up action.
 */
public interface Awakable<E> {

    /**
     * The coroutine job representing the implementation's execution.
     *
     * This job can be used to manage the lifecycle of the waitress, such as cancellation or monitoring its status.
     */
    public val job: Job

    /**
     * Wakes up the coroutine, allowing it to execute its action.
     *
     * This method signals the coroutine to perform its action, which is typically defined in the
     * implementation of the coroutine. The action may be executed once for each wake-up event.
     *
     * @return Some result of the wake-up performed.
     */
    public fun wakeUp(): E
}