package org.angproj.conc

import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.job
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.microseconds
import kotlin.time.DurationUnit
import kotlin.time.TimeSource

class clockTest {

    @Test
    fun testClock() = runTest {
        var counter = 0
        val clock = clock(DurationUnit.MICROSECONDS, 1) {
            counter++
            if (counter >= 5) {
                this.coroutineContext.job.cancel()
            }
        }

        delay(5.microseconds)

        clock.join()
        assertEquals(counter, 5)
    }

    @Test
    fun testClockWithTicks() = runTest {
        val ticksPerSecond = 4
        val totalSeconds = 5
        val start = TimeSource.Monotonic.markNow()

        val job = clock(DurationUnit.SECONDS, ticksPerSecond) {
            val elapsed = start.elapsedNow()
            println("Tick! Elapsed time: ${elapsed}")
            if(elapsed.inWholeSeconds >= totalSeconds) {
                this.cancel()
                println("Clock stopped.")
            }
        }

        job.join()
    }
}