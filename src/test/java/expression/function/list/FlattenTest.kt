package org.cerion.symcalc.expression.function.list

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.number.Integer
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FlattenTest {

    @Test
    fun validateParameters() {
        var e: Expr = Flatten(Integer.ZERO)
        e = e.eval()
        assertTrue(e.isError)
    }

    @Test
    fun singleList() {
        var e = Flatten(ListExpr(Integer.ZERO, L1)).eval()
        verify(ListExpr(Integer.ZERO, Integer.ONE, Integer.TWO), e)

        e = Flatten(ListExpr(L1, Integer.ZERO)).eval()
        verify(ListExpr(Integer.ONE, Integer.TWO, Integer.ZERO), e)
    }

    @Test
    fun nestedList() {
        val list = ListExpr(Integer.ZERO, L1, ListExpr(L2, L3))
        val expected = ListExpr(Integer.ZERO, Integer.ONE, Integer.TWO,
                Integer(3), Integer(4), Integer(5), Integer(6))

        verify(expected, Flatten(list).eval())
    }

    private fun verify(expected: ListExpr, actual: Expr) {
        assertTrue(actual.isList)
        val l = actual as ListExpr

        assertEquals(expected.size.toLong(), l.size.toLong())
        assertEquals(expected, actual)
    }

    companion object {
        private val L1 = ListExpr(Integer.ONE, Integer.TWO)
        private val L2 = ListExpr(Integer(3), Integer(4))
        private val L3 = ListExpr(Integer(5), Integer(6))
    }
}