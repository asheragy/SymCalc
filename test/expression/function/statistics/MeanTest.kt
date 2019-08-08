package org.cerion.symcalc.expression.function.statistics

import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.number.IntegerNum
import org.cerion.symcalc.expression.number.Rational
import org.cerion.symcalc.expression.number.RealNum
import org.junit.Assert.assertEquals
import org.junit.Test

class MeanTest {

    @Test
    fun basicTest() {
        var list = ListExpr(IntegerNum.ZERO, IntegerNum.ONE, IntegerNum.TWO, IntegerNum(3))
        var e = Mean(list).eval()
        assertEquals(Rational(IntegerNum(3), IntegerNum.TWO), e)

        list = ListExpr(IntegerNum(6), Rational(87, 3), RealNum.create(23.542))
        e = Mean(list).eval()
        assertEquals(RealNum.create(19.514), e)
    }
}