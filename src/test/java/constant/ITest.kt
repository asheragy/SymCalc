package org.cerion.symcalc.constant

import org.cerion.symcalc.expression.number.Complex
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.function.core.Hold
import kotlin.test.Test
import kotlin.test.assertEquals

class ITest {

    @Test
    fun string() {
        assertEquals("i", I().toString())
    }

    @Test
    fun hold() {
        assertEquals(Hold(I()), Hold(I()).eval())
    }

    @Test
    fun eval() {
        assertEquals(Complex(0, 1), I().eval())
    }

    @Test
    fun arithmetic() {
        assertEquals(Complex(2, 1), Integer(2) + I())
        assertEquals(Complex(0, 5), Integer(5) * I())
    }
}