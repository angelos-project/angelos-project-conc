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

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


/**
 * Launches a coroutine that executes the specified action exactly once in the background.
 *
 * This function is intended for scenarios where a single asynchronous task needs to be performed
 * without blocking the calling thread. It creates a coroutine using the [Dispatchers.Default] context,
 * ensuring efficient execution suitable for most background operations. The coroutine is started
 * immediately, and the provided suspendable [action] is invoked within its scope.
 *
 * The [task] function abstracts away coroutine setup, making it easy to run asynchronous work
 * from synchronous or blocking code. It is ideal for offloading computations, I/O, or other
 * operations that should not interfere with the org.example.main thread.
 *
 * The returned [Job] can be used to manage the coroutine's lifecycle, including cancellation
 * or monitoring its completion.
 *
 * @param action The suspendable lambda to execute within the coroutine.
 * @return A [Job] representing the running coroutine.
 */
public fun task(action: suspend CoroutineScope.() -> Unit): Job = CoroutineScope(Dispatchers.Default).launch {
    action()
}.apply { start() }