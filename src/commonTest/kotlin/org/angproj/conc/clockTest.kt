package org.angproj.conc

import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.job
import kotlinx.coroutines.runBlocking
import kotlin.time.Duration.Companion.microseconds
import kotlin.time.DurationUnit
import kotlin.test.Test

class clockTest {

    @Test
    fun testClock() = runBlocking {
        var counter = 0
        val scheduled = clock (DurationUnit.MICROSECONDS, 1) {
            counter++
            if (counter >= 5) {
                this.coroutineContext.job.cancel()
            }
        }

        delay(5.microseconds)

        scheduled.join()
        scheduled.cancelAndJoin()
        check(counter == 5) { "Counter should be 5, but was $counter" }
    }
}