package org.example

import org.angproj.conc.schedule
import kotlinx.coroutines.runBlocking
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TimeSource

public fun main(): Unit = runBlocking {
    val start = TimeSource.Monotonic.markNow()

    val job = schedule(1.5.seconds) {
        println("Scheduled task executed after ${start.elapsedNow()}.")
    }

    job.join() // Wait for the scheduled task to complete
}