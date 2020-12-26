package org.cerion.symcalc.function.integer

import org.cerion.symcalc.number.Integer
import kotlin.test.Test
import kotlin.test.assertEquals

class FactorialTest {

    @Test
    fun basic() {
        assertEquals(Integer(1), Factorial(Integer(0)).eval())
        assertEquals(Integer(1), Factorial(Integer(1)).eval())
        assertEquals(Integer(2), Factorial(Integer(2)).eval())
        assertEquals(Integer(720), Factorial(Integer(6)).eval())
    }
}