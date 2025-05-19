package org.angproj.conc

import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.time.Duration.Companion.microseconds
import kotlin.test.Test

class scheduleTest {

    @Test
    fun testSchedule() = runBlocking {
        var triggered = false
        val scheduled = schedule(1.microseconds) {
            triggered = true
        }

        delay(2.microseconds)

        scheduled.join()
        scheduled.cancelAndJoin()
        check(triggered) { "Scheduled task should be triggered, but was not" }
    }
}