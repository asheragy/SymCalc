package org.cerion.symcalc.expression.function.integer

import org.cerion.symcalc.expression.number.IntegerNum
import org.junit.Assert.*
import org.junit.Test

class FibonacciTest {

    @Test
    fun basic() {
        // TODO zero and negatives
        assertEquals(IntegerNum(1), Fibonacci(IntegerNum(1)).eval())
        assertEquals(IntegerNum(1), Fibonacci(IntegerNum(2)).eval())
        assertEquals(IntegerNum(2), Fibonacci(IntegerNum(3)).eval())
        assertEquals(IntegerNum(3), Fibonacci(IntegerNum(4)).eval())
        assertEquals(IntegerNum(5), Fibonacci(IntegerNum(5)).eval())
        assertEquals(IntegerNum(8), Fibonacci(IntegerNum(6)).eval())

        assertEquals(IntegerNum(6765), Fibonacci(IntegerNum(20)).eval())
    }
}