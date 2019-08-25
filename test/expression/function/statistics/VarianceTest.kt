package org.cerion.symcalc.expression.function.statistics

import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.number.Rational
import org.cerion.symcalc.expression.number.RealDouble
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class VarianceTest {

    @Test
    fun basic() {
        val list = ListExpr(1.21, 3.4, 2.0, 4.66, 1.5, 5.61, 7.22)
        assertEquals(RealDouble(5.16122380952381), Variance(list).eval())
    }

    @Test
    fun integer() {
        val list = ListExpr(600, 470, 170, 430, 300)
        assertEquals(Integer(27130), Variance(list).eval())
    }

    @Test
    fun real() {
        var list = ListExpr(1.1, 1.2, 1.3)
        assertEquals(RealDouble(0.009999999999999995), Variance(list).eval())

        list = ListExpr(1.21, 3.4, 2.0, 4.66, 1.5, 5.61, 7.22)
        assertEquals(RealDouble(5.16122380952381), Variance(list).eval())
    }

    @Test
    fun integers_evalAsRational() {
        val list = ListExpr(9, 2, 5, 4, 12, 7, 8, 11, 9, 3, 7, 4, 12, 5, 4, 10, 9, 6, 9, 4)
        assertEquals(Rational(178,19), Variance(list).eval())
    }

}