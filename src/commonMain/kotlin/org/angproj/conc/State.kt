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

    /**
     * @param E The type of the state.
     * @property current The current state of the state machine.
     * @property states The finite states of the state machine.
     * @property action The action to be executed when transitioning to a new state.
     */
    public abstract class StateMachine<E>(
        protected var current: E,
        protected val states: FiniteStates<E>,
        protected val action: E.() -> Unit
    ){
        public val state: E
            get() = current

        /**
         * @return true if the state machine is in the initial state.
         * */
        public val start: Boolean
            get() = state == states.begin

        /**
         * @return true if the state machine is in the final state.
         * */
        public val done: Boolean
            get() = state == states.end

        /**
         * */
        public val choices: List<E>
            get() = states.getPaths(state)

        /**
         * @param state The state to transition to.
         * @return The list of available states after the transition.
         * */
        public fun goto(state: E): List<E> = choices.apply {
            current = elementAtOrNull(indexOf(state)) ?: error("State unavailable")
            current.action()
        }
    }

    /**
     * A class that represents a finite state machine with a defined set of states.
     *
     * @param E The type of the states in the state machine.
     * @property begin The initial state of the state machine.
     * @property end The final state of the state machine.
     */
    public abstract class FiniteStates<E>(
        public val begin: E,
        public val end: E
    ) {
        init {
            require(getPaths(begin).isNotEmpty())
            require(getPaths(end).isEmpty())
        }

        /**
         * A map that defines the paths between states.
         * The keys are the states, and the values are the list of states that can be transitioned to from the key state.
         */
        public abstract val path: Map<E, List<E>>

        /**
         * @param state The state to get the paths for.
         * @return The list of states that can be transitioned to from the given state.
         * */
        public fun getPaths(state: E): List<E> = path[state] ?: error("Not configured")

        /**
         */
        public fun execute(
            action: E.() -> Unit
        ): StateMachine<E> = object : StateMachine<E>(begin, this, action) {}
    }
}