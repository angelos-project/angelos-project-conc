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


/**
 * Launches a coroutine that continuously executes the provided block in an infinite loop,
 * yielding to the event loop after each iteration to ensure cooperative multitasking.
 *
 * This function is ideal for scenarios requiring persistent background processing,
 * such as event-driven logic, polling, or game loops. Unlike time-based scheduling functions,
 * `loop` does not introduce any delay between iterations except for yielding, allowing for
 * maximum responsiveness while still sharing execution time with other coroutines.
 *
 * The loop runs until the coroutine is cancelled. The returned [Job] can be used to control
 * or observe the coroutine's lifecycle.
 *
 * @param block The suspendable block of code to execute on each iteration.
 * @return A [Job] representing the running loop coroutine.
 */
public fun loop(block: suspend CoroutineScope.() -> Unit): Job = CoroutineScope(Dispatchers.Default).async {
    do {
        block()
        yield()
    } while (isActive)
}.apply { start() }.job