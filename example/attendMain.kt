package org.example

import kotlinx.coroutines.*
import org.angproj.conc.attend
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TimeSource

public fun main(): Unit = runBlocking {
    val start = TimeSource.Monotonic.markNow()
    val steward = attend {
        println("Steward processing event at ${start.elapsedNow()}")
    }

    repeat(5) {
        println("Waking up steward (${it + 1})")
        steward.wakeUp()
    }

    delay(2.seconds) // Wait for all events to be processed
    println("All events processed.")
    steward.job.cancelAndJoin()
}