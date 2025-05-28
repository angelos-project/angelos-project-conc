package org.angproj.conc

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.isActive
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.yield

/**
 * Creates a waitress coroutine that remains dormant until explicitly woken up, executing the provided action
 * only once per sleep cycle, regardless of how many wake-up calls are made while it is active.
 *
 * The waitress is ideal for event-driven scenarios where actions should only be performed in response to
 * a single trigger per cycle. Each call to [wakeUp] signals the waitress to execute its action, but if the
 * waitress is already awake, additional wake-up calls are ignored until it returns to sleep. This ensures
 * that the action is not executed more than once per activation, making it suitable for debouncing or
 * single-shot event handling.
 *
 * The returned [Waitress] instance provides the [wakeUp] method for signaling and exposes the underlying
 * coroutine [kotlinx.coroutines.job] for lifecycle management, such as cancellation or monitoring.
 *
 * @param action The suspendable lambda to execute upon each wake-up event.
 * @return A [Waitress] instance for managing and signaling the coroutine.
 */
public fun answer(action: suspend CoroutineScope.() -> Unit): Waitress = object : Waitress {
    private val mutex: Mutex = Mutex()

    override val job: Job = CoroutineScope(Dispatchers.Default).async {
        while (isActive) {
            sleep()
            action()
            yield()
        }
    }

    init { job.start() }

    private suspend fun sleep(): Unit = mutex.lock()
    override fun wakeUp(): Boolean = when(mutex.isLocked) {
        false -> false
        else -> { mutex.unlock(); true }
    }
}