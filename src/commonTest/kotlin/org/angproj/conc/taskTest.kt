package org.angproj.conc

import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.time.Duration.Companion.microseconds
import kotlin.test.Test

class taskTest {

    @Test
     fun testTask() = runBlocking {
        var triggered = false
        val tasked = task {
            triggered = true
        }

        delay(1.microseconds)

        tasked.join()
        tasked.cancelAndJoin()
        check(triggered) { "Task should be triggered, but was not" }
    }
}