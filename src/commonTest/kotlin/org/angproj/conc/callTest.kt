package org.angproj.conc

import kotlinx.coroutines.delay
import kotlinx.coroutines.job
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.time.Duration
import kotlin.time.Duration.Companion.microseconds
import kotlin.time.DurationUnit

class callTest {

    @Test
    fun testCall() = runTest {
        var called: Duration? = null;
        var counter = 0
        val clock = clock(DurationUnit.MICROSECONDS, 1) {
            counter++
            if (counter >= 5) {
                this.coroutineContext.job.cancel()
            }
        }

        call(DurationUnit.MICROSECONDS, clock) {
            called = it
        }

        assertEquals(called, null)
        delay(5.microseconds)

        clock.join()
        assertEquals(counter, 5)
        assertNotNull(called)
    }
}