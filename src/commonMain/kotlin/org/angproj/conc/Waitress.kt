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
public interface Waitress : Awakable<Boolean> {

    /**
     * Signals the waitress to wake up and execute its action.
     *
     * If the waitress is dormant, this call will wake it up and allow it to perform its action.
     * If the waitress is already awake and processing, the call is ignored and returns `false`.
     *
     * @return `true` if the waitress was successfully woken up, `false` if it was already awake.
     */
    override fun wakeUp(): Boolean
}


