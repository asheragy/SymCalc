package org.cerion.symcalc.function

import org.cerion.symcalc.number.Integer

class FactorialGenerator {

    private var lastN = 0
    private var lastResult = Integer.ONE

    fun getNext(n: Int): Integer {
        if (n <= lastN)
            throw IllegalArgumentException()

        for(i in lastN+1..n) {
            lastResult *= Integer(i)
        }

        lastN = n
        return lastResult
    }

}