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
 * ## Example implementation
 * ```
 * enum class ProtocolState : State<ProtocolState>{
 *     WAITING,
 *     TALKING,
 *     HANGUP;
 *
 *     companion object : State.FiniteStates<ProtocolState>(WAITING, HANGUP) {
 *         override val path = mapOf(
 *             WAITING to listOf(TALKING),
 *             TALKING to listOf(HANGUP),
 *             HANGUP to listOf()
 *         )
 *     }
 * }
 * ```
 * */
public interface State<E: Enum<E>> {
    public abstract class StateMachine<E>(
        protected var current: E,
        protected val states: FiniteStates<E>,
        protected val action: E.() -> Unit
    ){
        public val state: E
            get() = current

        public val start: Boolean
            get() = state == states.begin

        public val done: Boolean
            get() = state == states.end

        public val choices: List<E>
            get() = states.getPaths(state)

        public fun goto(state: E): List<E> = choices.apply {
            current = elementAtOrNull(indexOf(state)) ?: error("State unavailable")
            current.action()
        }
    }

    public abstract class FiniteStates<E>(
        public val begin: E,
        public val end: E
    ) {
        init {
            require(getPaths(begin).isNotEmpty())
            require(getPaths(end).isEmpty())
        }

        public abstract val path: Map<E, List<E>>

        public fun getPaths(state: E): List<E> = path[state] ?: error("Not configured")

        public fun execute(
            action: E.() -> Unit
        ): StateMachine<E> = object : StateMachine<E>(begin, this, action) {}
    }
}