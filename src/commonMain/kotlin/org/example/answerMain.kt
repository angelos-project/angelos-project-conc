package org.example

import kotlinx.coroutines.*
import org.angproj.conc.answer
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TimeSource

public fun main(): Unit = runBlocking {
    val start = TimeSource.Monotonic.markNow()
    val waitress = answer {
        println("Waitress processing event at ${start.elapsedNow()}")
    }

    repeat(5) {
        println("Waking up waitress (${it + 1})")
        waitress.wakeUp()
    }

    delay(2.seconds) // Wait for all events to be processed
    println("All events processed.")
    waitress.job.cancelAndJoin()
}