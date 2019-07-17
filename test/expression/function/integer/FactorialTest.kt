package org.cerion.symcalc.expression.function.integer

import org.cerion.symcalc.expression.number.IntegerNum
import org.junit.Test

import org.junit.Assert.*

class FactorialTest {

    @Test
    fun basic() {
        assertEquals(IntegerNum(1), Factorial(IntegerNum(0)).eval())
        assertEquals(IntegerNum(1), Factorial(IntegerNum(1)).eval())
        assertEquals(IntegerNum(2), Factorial(IntegerNum(2)).eval())
        assertEquals(IntegerNum(720), Factorial(IntegerNum(6)).eval())
    }
}