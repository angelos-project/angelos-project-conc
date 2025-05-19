package org.angproj.conc

import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.microseconds

class taskTest {

    @Test
    fun testTask() = runTest {
        var triggered = false
        val tasked = task {
            triggered = true
        }

        delay(1.microseconds)

        tasked.join()
        assertTrue(triggered)
    }
}