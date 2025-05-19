package org.angproj.conc

import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.job
import kotlinx.coroutines.runBlocking
import kotlin.time.Duration.Companion.microseconds
import kotlin.test.Test

class loopTest {

    @Test
    fun testLoop() = runBlocking {
        var counter = 0
        val loop = loop {
            counter++
            if (counter >= 5) {
                this.coroutineContext.job.cancel()
            }
        }

        delay(5.microseconds)

        loop.join()
        loop.cancelAndJoin()
        check(counter == 5) { "Counter should be 5, but was $counter" }
    }
}