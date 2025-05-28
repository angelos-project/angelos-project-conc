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

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class Counter {
    private var count = 0

    fun increment() {
        count++
    }

    fun decrement() {
        count--
    }

    fun getCount(): Int {
        return count
    }

}

internal class IntDispenser : Dispenser<Counter>(Counter()) {
}

class DispenserTest {

    @Test
    fun testDispenser() = runTest{
        val dispenser = IntDispenser()

        dispenser.dispense { increment() }
        dispenser.dispense { increment() }
        dispenser.dispense { increment() }

        dispenser.dispense {
            assertEquals(3, getCount())
        }
    }
}