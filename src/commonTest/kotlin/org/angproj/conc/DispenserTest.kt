package org.angproj.conc

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

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