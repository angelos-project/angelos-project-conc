package org.example

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.angproj.conc.call
import org.angproj.conc.schedule
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit

public fun main(): Unit = runBlocking {
    val scheduledJob = schedule(5.seconds) {
        println("Scheduled task executed.")
    }

    val monitorJob = call(DurationUnit.SECONDS, scheduledJob) { elapsed ->
        println("Scheduled job was cancelled after ${elapsed}.")
    }

    // Cancel the scheduled job after 2 seconds to trigger the call action
    delay(2.seconds)
    scheduledJob.cancel()

    monitorJob.join()
    println("Monitor finished.")
}