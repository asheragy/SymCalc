package org.cerion.symcalc.function.arithmetic

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.constant.ComplexInfinity
import org.cerion.symcalc.constant.Pi
import org.cerion.symcalc.function.core.N
import org.cerion.symcalc.number.Integer
import org.cerion.symcalc.number.Rational
import org.cerion.symcalc.number.RealDouble
import kotlin.test.Test
import kotlin.test.assertEquals

class DivideTest {

    @Test
    fun precisionTest() {
        var e: Expr = Divide(Integer(5), Integer(3))
        assertEquals("5/3", e.eval().toString())

        assertEquals(RealDouble(1.6666666666666667), N(e).eval())

        e = Divide(Integer(10), Integer(6))
        e = N(e).eval()
        assertEquals("1.6666666666666667", e.toString())
    }

    @Test
    fun reduces() {
        var e: Expr = Divide(Integer(10), Integer(6))
        assertEquals("5/3", e.eval().toString())

        e = Divide(Integer(10), Integer(2))
        assertEquals("5", e.eval().toString())
    }

    @Test
    fun rationalFactoredOut() {
        assertEquals(Times(Rational(1,2), Pi()), Divide(Pi(), Integer.TWO).eval())
    }

    @Test
    fun divideByZero() {
        assertEquals(ComplexInfinity(), Divide(Integer(1), Integer(0)).eval())
    }

    @Test
    fun divideByInfinity() {
        assertEquals(Integer.ZERO, Divide(Integer(1), ComplexInfinity()).eval())
    }
}