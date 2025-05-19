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
import kotlin.time.*


/**
 * Schedules a coroutine to run after a specified delay.
 *
 * @param inTime The delay before the coroutine is executed.
 * @param action The action to be executed after the delay.
 * @return A [Job] representing the scheduled coroutine.
 */
public fun schedule(
    inTime: Duration, action: suspend CoroutineScope.() -> Unit
): Job = CoroutineScope(Dispatchers.Default).launch {
    delay(inTime)
    if(isActive) action()
}.apply { start() }



