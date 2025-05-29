package org.example

import org.angproj.conc.task
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TimeSource

public fun main(): Unit = runBlocking {
    val start = TimeSource.Monotonic.markNow()

    val job = task {
        println("Task started.")
        delay(1.seconds)
        println("Task finished after ${start.elapsedNow()} second.")
    }

    job.join() // Wait for the task to complete
}