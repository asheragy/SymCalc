package org.cerion.symcalc.function.statistics

import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.function.arithmetic.Sqrt
import org.cerion.symcalc.expression.number.Rational
import org.cerion.symcalc.expression.number.RealDouble
import kotlin.test.Test
import kotlin.test.assertEquals

class StandardDeviationTest {

    @Test
    fun integers_evalAsRational() {
        val list = ListExpr(9, 2, 5, 4, 12, 7, 8, 11, 9, 3, 7, 4, 12, 5, 4, 10, 9, 6, 9, 4)
        assertEquals(Sqrt(Rational(178,19)).eval(), StandardDeviation(list).eval())
    }

    @Test
    fun real() {
        val list = ListExpr(1.21, 3.4, 2.0, 4.66, 1.5, 5.61, 7.22)
        assertEquals(RealDouble(2.271832698400965), StandardDeviation(list).eval())
    }
}