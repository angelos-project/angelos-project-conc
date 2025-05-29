/**
 * Copyright (c) 2025 by Kristoffer Paulsson <kristoffer.paulsson@talenten.se>.
 *
 * This software is available under the terms of the MIT license. Parts are licensed
 * under different terms if stated. The legal terms are attached to the LICENSE file
 * and are made available on:
 *
 *      https://opensource.org/licenses/MIT
 *
 * SPDX-License-Identifier: MIT
 *
 * Contributors:
 *      Kristoffer Paulsson - initial implementation
 */
package org.angproj.conc

/**
 * A coroutine steward that remains dormant until explicitly woken up, executing a specified action
 * once for each registered wake-up event. The steward ensures that all wake-up signals are faithfully
 * queued and processed in order, making it suitable for scenarios where every trigger must be handled.
 *
 * The [wakeUp] method signals the steward to perform its action. Each call increments an internal counter,
 * guaranteeing that no wake-up is lost, even if the steward is already active. The steward processes
 * each wake-up event sequentially, executing the action once per event.
 *
 * The underlying coroutine [job] can be used for lifecycle management, such as cancellation or monitoring.
 *
 * Typical use cases include event-driven systems where every external trigger must be processed,
 * such as task queues, notification handlers, or ordered event processing.
 */
public interface Steward : Awakable<Int> {

    /**
     * Signals the steward to wake up and execute its action.
     *
     * Each call increments the internal wake-up counter. The steward will execute its action
     * once for each registered wake-up event, ensuring all events are processed in order.
     *
     * @return The current count of remaining wake-up events after this call.
     */
    override fun wakeUp(): Int
}