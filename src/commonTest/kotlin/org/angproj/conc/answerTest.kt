package org.angproj.conc

import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.microseconds


class answerTest {

    @Test
    fun testAnswer() = runTest {
        var counter = 0
        val waitress = answer {
            counter++
        }

        waitress.wakeUp()
        waitress.wakeUp()
        waitress.wakeUp()

        delay(1.microseconds)

        assertEquals(1, counter)
    }
}