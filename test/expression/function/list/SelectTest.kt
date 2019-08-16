package org.cerion.symcalc.expression.function.list

import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.function.integer.EvenQ
import org.cerion.symcalc.expression.number.Integer
import org.junit.Assert.assertEquals
import org.junit.Test

class SelectTest {

    @Test
    fun basicTest() {
        val list = ListExpr(Integer.ZERO, Integer.ONE, Integer.TWO, Integer(3))
        val expected = ListExpr(Integer.ZERO, Integer.TWO)

        val e = Select(list, EvenQ().symbol)
        assertEquals(expected, e.eval())
    }
}