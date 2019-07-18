package org.cerion.symcalc.expression.function.arithmetic

import org.cerion.symcalc.assertEqual
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.function.core.N
import org.cerion.symcalc.expression.number.IntegerNum
import org.junit.Test

import org.junit.Assert.*

class DivideTest {

    @Test
    fun precisionTest() {
        var e: Expr = Divide(IntegerNum(5), IntegerNum(3))
        assertEquals("5/3", e.eval().toString())

        e = N(e).eval()
        assertEqual(1.6666666666666667, e)

        e = Divide(IntegerNum(10), IntegerNum(6))
        e = N(e).eval()
        assertEquals("1.6666666666666667", e.toString())
    }

    @Test
    fun reduces() {
        var e: Expr = Divide(IntegerNum(10), IntegerNum(6))
        assertEquals("5/3", e.eval().toString())

        e = Divide(IntegerNum(10), IntegerNum(2))
        assertEquals("5", e.eval().toString())
    }
}