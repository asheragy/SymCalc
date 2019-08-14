package org.cerion.symcalc.expression.function.statistics

import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.number.IntegerNum
import org.cerion.symcalc.expression.number.Rational
import org.cerion.symcalc.expression.number.RealDouble
import org.junit.Assert.assertEquals
import org.junit.Test

class MeanTest {

    @Test
    fun basicTest() {
        var list = ListExpr(IntegerNum.ZERO, IntegerNum.ONE, IntegerNum.TWO, IntegerNum(3))
        assertEquals(Rational(IntegerNum(3), IntegerNum.TWO), Mean(list).eval())

        list = ListExpr(IntegerNum(6), Rational(87, 3), RealDouble(23.542))
        assertEquals(RealDouble(19.514), Mean(list).eval())
    }
}