package org.angproj.conc

import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.microseconds


class attendTest {

    @Test
    fun testAttend() = runTest {
        var counter = 0
        val steward = attend {
            counter++
        }

        steward.wakeUp()
        steward.wakeUp()
        steward.wakeUp()

        delay(1.microseconds)

        assertEquals(3, counter)
    }
}