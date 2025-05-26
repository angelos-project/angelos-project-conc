package org.angproj.conc

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class Counter {
    private var count = 0

    fun increment() {
        count++
    }

    fun getCount(): Int {
        return count
    }

}

class IntDispenser : Dispenser<Counter>(Counter()) {
}

class DispenserTest {

    @Test
    fun testDispenser() = runTest{
        val dispenser = IntDispenser()

        dispenser.dispense {
            increment()
        }
        dispenser.dispense {
            increment()
        }
        dispenser.dispense {
            increment()
        }
        dispenser.dispense {
            assertEquals(3, getCount())
        }
    }
}