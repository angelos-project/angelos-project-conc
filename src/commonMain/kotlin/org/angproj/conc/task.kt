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
 * Starts a coroutine that runs the specified action.
 *
 * This method creates a coroutine using the {@link Dispatchers#Default} context and executes
 * the provided action within it. The coroutine is started immediately after being created.
 *
 * @param action The action to be executed in the coroutine. This is a suspendable lambda
 *               that operates within a {@link CoroutineScope}.
 * @return A [Job] representing the coroutine. The caller can use this to manage
 *         the coroutine's lifecycle, such as canceling or monitoring its completion.
 * @see CoroutineScope
 */
public fun task(action: suspend CoroutineScope.() -> Unit): Job = CoroutineScope(Dispatchers.Default).launch {
    action()
}.apply { start() }