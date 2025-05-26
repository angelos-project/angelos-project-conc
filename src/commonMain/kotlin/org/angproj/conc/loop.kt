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
 * Starts a coroutine that runs the specified block
 * of code in a loop and yields control to other coroutines
 * after each iteration. The loop continues until the
 * coroutine is cancelled. This function is useful for creating
 * a coroutine that needs to run continuously, such as a game
 * loop or a background task.
 *
 * @param block The block of code to be executed by each iteration of the loop.
 * @return A [Job] representing the coroutine.
 */
public fun loop(block: suspend CoroutineScope.() -> Unit): Job = CoroutineScope(Dispatchers.Default).async {
    do {
        block()
        yield()
    } while (isActive)
}.apply { start() }.job