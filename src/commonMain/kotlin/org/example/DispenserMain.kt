package org.example

import kotlinx.coroutines.*
import org.angproj.conc.Dispenser
import org.angproj.conc.loop
import kotlin.random.Random

public fun main(): Unit = runBlocking {
    val dispenser = object : Dispenser<MutableList<Int>>(MutableList(10) { Random.nextInt() }) {}

    val job = loop {
        dispenser.dispense {
            if (isEmpty()) {
                println("No more items to dispense.")
                this@loop.cancel() // Stop the loop if no items left
            } else {
                println("Dispensed item: ${removeFirst()}")
            }
        }
    }

    job.join() // Wait for the loop to finish
}