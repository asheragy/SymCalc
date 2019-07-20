package org.cerion.symcalc.expression.function.list

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.number.IntegerNum
import org.junit.Test

import org.junit.Assert.*

class FlattenTest {

    @Test
    fun validateParameters() {
        var e: Expr = Flatten(IntegerNum.ZERO)
        e = e.eval()
        assertTrue(e.isError)
    }

    @Test
    fun singleList() {
        var e = Flatten(ListExpr(IntegerNum.ZERO, L1)).eval()
        verify(ListExpr(IntegerNum.ZERO, IntegerNum.ONE, IntegerNum.TWO), e)

        e = Flatten(ListExpr(L1, IntegerNum.ZERO)).eval()
        verify(ListExpr(IntegerNum.ONE, IntegerNum.TWO, IntegerNum.ZERO), e)
    }

    @Test
    fun nestedList() {
        val list = ListExpr(IntegerNum.ZERO, L1, ListExpr(L2, L3))
        val expected = ListExpr(IntegerNum.ZERO, IntegerNum.ONE, IntegerNum.TWO,
                IntegerNum(3), IntegerNum(4), IntegerNum(5), IntegerNum(6))

        verify(expected, Flatten(list).eval())
    }

    private fun verify(expected: ListExpr, actual: Expr) {
        assertTrue(actual.isList)
        val l = actual as ListExpr

        assertEquals(expected.size.toLong(), l.size.toLong())
        assertEquals(expected, actual)
    }

    companion object {
        private val L1 = ListExpr(IntegerNum.ONE, IntegerNum.TWO)
        private val L2 = ListExpr(IntegerNum(3), IntegerNum(4))
        private val L3 = ListExpr(IntegerNum(5), IntegerNum(6))
    }
}