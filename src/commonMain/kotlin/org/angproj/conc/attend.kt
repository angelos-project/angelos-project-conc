package org.angproj.conc

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.isActive
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.yield

/**
 * Creates a steward coroutine that remains dormant until explicitly woken up, executing the provided action
 * once for each registered wake-up event.
 *
 * The steward is ideal for event-driven scenarios where actions should only be performed in response to
 * external triggers. Each call to [wakeUp] signals the steward to execute its action, and multiple wake-ups
 * are faithfully queued and processed in order. This ensures that no wake-up is lost, even if the steward
 * is already active.
 *
 * The returned [Steward] instance provides the [wakeUp] method for signaling and exposes the underlying
 * coroutine [kotlinx.coroutines.job] for lifecycle management, such as cancellation or monitoring.
 *
 * @param action The suspendable lambda to execute upon each wake-up event.
 * @return A [Steward] instance for managing and signaling the coroutine.
 */
public fun attend(action: suspend CoroutineScope.() -> Unit): Steward = object : Steward {
    private val mutex: Mutex = Mutex()
    private var counter = 0

    override val job: Job = CoroutineScope(Dispatchers.Default).async {
        while (isActive) {
            sleep()
            while(counter > 0) {
                counter--
                action()
            }
            yield()
        }
    }

    init { job.start() }

    private suspend fun sleep(): Unit = mutex.lock()
    override fun wakeUp(): Int {
        counter++
        if(mutex.isLocked)
            mutex.unlock()
        return counter
    }
}