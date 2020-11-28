package org.cerion.symcalc.expression.function.statistics

import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.number.Rational
import org.cerion.symcalc.expression.number.RealDouble
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class MeanTest {

    @Test
    fun basicTest() {
        var list = ListExpr(Integer.ZERO, Integer.ONE, Integer.TWO, Integer(3))
        assertEquals(Rational(Integer(3), Integer.TWO), Mean(list).eval())

        list = ListExpr(Integer(6), Rational(87, 3), RealDouble(23.542))
        assertEquals(RealDouble(19.514), Mean(list).eval())
    }
}