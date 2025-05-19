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