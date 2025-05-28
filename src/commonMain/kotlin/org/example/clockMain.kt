package org.example

import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.angproj.conc.clock
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit
import kotlin.time.TimeSource


public fun main(): Unit = runBlocking {
    val ticksPerSecond = 4
    val totalSeconds = 5
    val start = TimeSource.Monotonic.markNow()
    var count = 0

    val job = clock(DurationUnit.SECONDS, ticksPerSecond) {
        val elapsed = start.elapsedNow()
        count++
        println("Tick $count! Elapsed time: $elapsed")
    }

    delay(totalSeconds.seconds)
    job.cancelAndJoin()
}