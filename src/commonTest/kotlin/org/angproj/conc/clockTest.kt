package org.angproj.conc

import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.job
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.microseconds
import kotlin.time.DurationUnit

class clockTest {

    @Test
    fun testClock() = runTest {
        var counter = 0
        val scheduled = clock(DurationUnit.MICROSECONDS, 1) {
            counter++
            if (counter >= 5) {
                this.coroutineContext.job.cancel()
            }
        }

        delay(5.microseconds)

        scheduled.join()
        assertEquals(counter, 5)
    }
}