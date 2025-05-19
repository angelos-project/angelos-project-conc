package org.angproj.conc

import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.microseconds

class scheduleTest {

    @Test
    fun testSchedule() = runTest {
        var triggered = false
        val scheduled = schedule(1.microseconds) {
            triggered = true
        }

        delay(2.microseconds)

        scheduled.join()
        assertTrue(triggered)
    }
}