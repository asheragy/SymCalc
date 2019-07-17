package org.cerion.symcalc.expression.function.integer

import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.number.IntegerNum
import org.junit.Test

import org.junit.Assert.*


class FactorTest {

    @Test
    fun basic() {
        // 2*2
        assertEquals(ListExpr(IntegerNum.TWO, IntegerNum.TWO), Factor(IntegerNum(4)).eval())

        // 2*2*2
        assertEquals(ListExpr(IntegerNum.TWO, IntegerNum.TWO, IntegerNum.TWO), Factor(IntegerNum(8)).eval())

        // 2*2*3
        assertEquals(ListExpr(IntegerNum.TWO, IntegerNum.TWO, IntegerNum(3)), Factor(IntegerNum(12)).eval())

        // 2*2*3*3
        assertEquals(ListExpr(IntegerNum.TWO, IntegerNum.TWO, IntegerNum(3), IntegerNum(3)), Factor(IntegerNum(36)).eval())

        // 5*5
        assertEquals(ListExpr(IntegerNum(5), IntegerNum(5)), Factor(IntegerNum(25)).eval())

        // 5*5*7
        assertEquals(ListExpr(IntegerNum(5), IntegerNum(5), IntegerNum(7)), Factor(IntegerNum(175)).eval())

        // 5*5*7*7
        assertEquals(ListExpr(IntegerNum(5), IntegerNum(5), IntegerNum(7), IntegerNum(7)), Factor(IntegerNum(1225)).eval())
    }

    @Test
    fun largerNumbers() {
        // 137 * 727 * 3803
        assertEquals(ListExpr(IntegerNum(137), IntegerNum(727), IntegerNum(3803)), Factor(IntegerNum(378774997)).eval())
    }
}