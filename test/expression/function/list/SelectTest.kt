package org.cerion.symcalc.expression.function.list

import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.function.integer.EvenQ
import org.cerion.symcalc.expression.number.IntegerNum
import org.junit.Assert.assertEquals
import org.junit.Test

class SelectTest {

    @Test
    fun basicTest() {
        val list = ListExpr(IntegerNum.ZERO, IntegerNum.ONE, IntegerNum.TWO, IntegerNum(3))
        val expected = ListExpr(IntegerNum.ZERO, IntegerNum.TWO)

        val e = Select(list, EvenQ())
        assertEquals(expected, e.eval())
    }
}