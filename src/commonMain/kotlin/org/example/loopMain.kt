package org.example

import org.angproj.conc.loop
import kotlinx.coroutines.*

public fun main(): Unit = runBlocking {
    var counter = 0

    println("Starting loop...")
    val job = loop {
        println("Loop iteration: ${++counter}")
        if (counter >= 5) {
            this.coroutineContext.job.cancel()
        }
    }

    job.join()
    println("Loop finished.")
}