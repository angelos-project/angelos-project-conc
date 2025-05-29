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

import kotlinx.coroutines.delay
import kotlinx.coroutines.job
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.microseconds

class loopTest {

    @Test
    fun testLoop() = runTest {
        var counter = 0
        val loop = loop {
            counter++
            if (counter >= 5) {
                this.coroutineContext.job.cancel()
            }
        }

        delay(5.microseconds)

        loop.join()
        assertEquals(counter, 5)
    }
}