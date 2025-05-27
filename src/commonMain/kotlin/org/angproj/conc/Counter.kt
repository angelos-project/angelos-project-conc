package org.angproj.conc

internal class Counter {
    private var count = 0

    fun increment() {
        count++
    }

    fun decrement() {
        count--
    }

    fun getCount(): Int {
        return count
    }

}