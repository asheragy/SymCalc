package org.cerion.symcalc.expression.function.statistics

import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.number.IntegerNum
import org.cerion.symcalc.expression.number.RealNum
import org.junit.Assert.assertEquals
import org.junit.Test

class VarianceTest {

    @Test
    fun basicIntegerTest() {
        val list = ListExpr(IntegerNum(600), IntegerNum(470),
                IntegerNum(170), IntegerNum(430), IntegerNum(300))

        val e = Variance(list).eval()
        assertEquals(IntegerNum(21704), e)
    }

    @Test
    fun basicTest() {
        var list = ListExpr(RealNum.create(1.1), RealNum.create(1.2), RealNum.create(1.3))
        var e = Variance(list).eval()
        assertEquals(RealNum.create(0.00666).toDouble(), (e as RealNum).toDouble(), 0.00001)

        list = ListExpr(RealNum.create(1.21), RealNum.create(3.4), RealNum.create(2.0),
                RealNum.create(4.66), RealNum.create(1.5), RealNum.create(5.61), RealNum.create(7.22))
        e = Variance(list).eval()
        assertEquals(RealNum.create(4.42390).toDouble(), (e as RealNum).toDouble(), 0.00001)
    }
}