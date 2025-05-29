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
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.microseconds


class answerTest {

    @Test
    fun testAnswer() = runTest {
        var counter = 0
        val waitress = answer {
            counter++
        }

        waitress.wakeUp()
        waitress.wakeUp()
        waitress.wakeUp()

        delay(1.microseconds)

        assertEquals(1, counter)
    }
}